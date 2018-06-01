package com.harman.rtnm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.harman.rtnm.common.helper.DateHelper;
import com.harman.rtnm.model.Counter;
import com.harman.rtnm.samsung.commonutils.util.IOUtils;
import com.harman.rtnm.samsung.commonutils.util.StringUtils;
import com.harman.rtnm.samsung.commonutils.writer.NpmFileWriter;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;


@Component
@PropertySource("classpath:adaptor-servers-details.properties")
public class SFTPUtil {

	private static final String HOST = "adaptor.host.";
	private static final String DESTINATION_FILE = "adaptor.destination.";
	private static final String USER = "adaptor.user.";
	private static final String PASS = "adaptor.pass.";

	private static final Logger LOGGER = LoggerFactory.getLogger(SFTPUtil.class);

	@Autowired
	private Environment properties;

	public SFTPUtil() { }

	/**
	 * Send the information regarding the dynamic 
	 * udpates either related to counter or kpi.
	 * @param counter
	 */
	public void createFileAndSend(final Counter counter, final String maxCounterId, final String deviceType) {		
		Runnable sendDeltaToAdaptor = () ->  {
			try {
				process(counter, maxCounterId, deviceType);
			} catch(Exception e) {
				LOGGER.error(" Error in updating the json spec for dyanmic kpi/counter", e);
			}
		};								
		new Thread(sendDeltaToAdaptor).start();
	}
	
	private void process(final Counter counter, String maxCounterId, String deviceType) {
		try {
			File fileToBeSent = createFile(deviceType, counter, maxCounterId, properties.getProperty(DESTINATION_FILE+deviceType));
			send(properties.getProperty(HOST+deviceType), properties.getProperty(USER+deviceType),
										properties.getProperty(PASS+deviceType),
										properties.getProperty(DESTINATION_FILE+deviceType), fileToBeSent);
			
		} catch(Throwable e) {
			LOGGER.error(" failed to send the updates to adaptor. ", e);
		}
	}

	//COUNTER_GROUP_ID,KPI_ID,UDPATE_HEADER,KPI_FORMULA,DEVICE_TYPE
	private File createFile(String deviceType, Counter counter, String maxCounterId, String dirName) throws IOException {
		StringBuilder data = new StringBuilder();
		data.append(counter.getCounterKey().getCounterGroup().getCounterGroupId()).append(StringUtils.SYMBOL_COMMA);
		data.append(counter.getCounterKey().getCounterId()).append(StringUtils.SYMBOL_COMMA);
		if(maxCounterId != null && maxCounterId.compareTo(counter.getCounterKey().getCounterId()) < 0) {
			data.append(StringUtils.YES).append(StringUtils.SYMBOL_COMMA);
		} else {
			data.append(StringUtils.NO).append(StringUtils.SYMBOL_COMMA);
		}
		data.append(counter.getKpiFormula()).append(StringUtils.SYMBOL_COMMA);
		data.append(deviceType);
		
		String fileName = dirName+File.separator+(deviceType.concat(StringUtils.SYMBOL_UNDERSCORE).concat(DateHelper.getCurrentTimeAsHHMMSS()));
		//create file and write the data
		File file = IOUtils.getFile(fileName);
		NpmFileWriter writer = new NpmFileWriter(file);
		
		writer.write(data.toString());
		IOUtils.close(writer);
		
		return file;
	}

	/**
	 * 
	 * @param sftpHost
	 * @param dirName
	 * @param sftpUser
	 * @param sftpPassword
	 */
	private void send (String sftpHost, String sftpUser, String sftpPassword, String dirName, final File file) {

		int SFTPPORT = 22;		
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		LOGGER.debug("preparing the host information for sftp.");
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(sftpUser, sftpHost, SFTPPORT);
			session.setPassword(sftpPassword);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			LOGGER.debug("Host connected.");
			channel = session.openChannel("sftp");
			channel.connect();
			LOGGER.debug("sftp channel opened and connected.");
			channelSftp = (ChannelSftp) channel;
			channelSftp.cd(dirName);
			channelSftp.put(new FileInputStream(file), file.getName());
			LOGGER.info("File transfered successfully to host.");
		} catch (Exception ex) {
			LOGGER.error("Exception found while tranfer the response.");
		}
		finally {
			channelSftp.exit();
			LOGGER.debug("sftp Channel exited.");
			channel.disconnect();
			LOGGER.debug("Channel disconnected.");
			session.disconnect();
			LOGGER.debug("Host Session disconnected.");
		}
	}
}
