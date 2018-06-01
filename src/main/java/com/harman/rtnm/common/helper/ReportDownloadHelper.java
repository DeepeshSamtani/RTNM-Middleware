package com.harman.rtnm.common.helper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletContext;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.harman.dyns.model.common.Attribute;
import com.harman.dyns.model.common.Configuration;
import com.harman.dyns.model.common.Metric;
import com.harman.rtnm.common.constant.Constant;
import com.harman.rtnm.common.constant.FileType;
import com.harman.rtnm.common.property.ExportDetailsProperties;
import com.harman.rtnm.common.property.MailAttribute;
import com.harman.rtnm.model.Counter;
import com.harman.rtnm.model.CounterGroup;
import com.harman.rtnm.model.response.ExportDetails;
import com.harman.rtnm.model.response.Response;
import com.harman.rtnm.model.response.UserReport;
import com.harman.rtnm.service.CounterService;
import com.harman.rtnm.service.ReportService;
import com.harman.rtnm.util.ReportPDFWriter;
import com.harman.rtnm.vo.EmailVO;
import com.harman.rtnm.vo.ReportDetailVO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Document;

import au.com.bytecode.opencsv.CSVWriter;

@Component
public class ReportDownloadHelper {

	public static ObjectMapper mapper = new ObjectMapper();

	@Autowired
	ServletContext servletContext;

	@Autowired
	CounterService counterService;

	@Autowired
	MailAttribute mailAttribute;

	@Autowired
	Environment environment;
	
	@Autowired
	ReportService reportService;
	
	@Autowired
	ExportDetailsProperties exportDetailsProperties;
	
	/**
	 * @param listOfDashBoards
	 * @throws Exception
	 * @throws IOException
	 */
	public List<String[]> processDruidResponseData(ExportDetails exportDetails , ReportDetailVO reportDetailVO) throws Exception {
		List<String[]> eventDataList = new ArrayList<>();
		parseResponse(exportDetails, eventDataList, exportDetails.getUserReport(),reportDetailVO);
		return eventDataList;
	}

	// List<String> fileHeaderList
	private void parseResponse(ExportDetails exportDetails, List<String[]> eventDataList, UserReport userReport ,ReportDetailVO reportDetailVO)
			throws Exception {

		List<String> tableHeaderList = new ArrayList<>();
		if (!StringUtils.isEmpty(userReport.getReportConfiguration().getConfiguration().getName())) {
			exportDetails.setFilename(userReport.getReportConfiguration().getConfiguration().getName());
		} else {
			exportDetails.setFilename("EENPM_Report");
		}
		if (!StringUtils.isEmpty(userReport.getResponse())
				&& !StringUtils.isEmpty(userReport.getReportConfiguration().getConfiguration())) {

			/*List<Attribute> attributes = null;
			List<Counter> counterList = null;

			// add properties in Report
			if (null != userReport.getReportConfiguration().getConfiguration().getDimensions()) {
				attributes = userReport.getReportConfiguration().getConfiguration().getDimensions();
				// Set Display Name used for Table column Name.
				attributes.forEach(attribute -> {
					attribute.setDisplayName(attribute.getId());					
				});
			}
			

			// add all counter in Report
			List<Metric> metricList = userReport.getReportConfiguration().getConfiguration().getMetrics();
			if (null != metricList && !metricList.isEmpty()) {

				counterList = counterService.getCountersFromIDs(metricList);

				counterList.forEach(counter -> {
					metricList.forEach(metric -> {
						if (metric.getId().contains(counter.getCounterId().toString())) {
							metric.setDisplayName(counter.getLogicalName()+"("+counter.getCounterUnit()+")");
						}
					});
				});

				attributes.addAll(metricList);
			}
*/
			//getHeaderAndFieldAttributes(attributes, exportDetails.getFileHeaderList(), tableHeaderList);

			setHeadersForFiles(userReport, exportDetails.getFileHeaderList(), tableHeaderList ,reportDetailVO);
			
			if (null != userReport.getResponse() && !userReport.getResponse().isEmpty()) {

				List<Response> responseList = userReport.getResponse();
				if (!StringUtils.isEmpty(responseList)) {
					responseList.forEach(response -> {
						if (response.getEvents() != null) {
							response.getEvents().forEach(event -> {
								parseListOfEventsForRespose(tableHeaderList, eventDataList, event);
							});
						}
					});
				}
			}
		}
	}

	public static void getHeaderAndFieldAttributes(List<Attribute> attributes, List<String> displayHeaderList,
			List<String> tableHeaderList) {
		displayHeaderList.clear();
		tableHeaderList.clear();
		displayHeaderList.add("TimeStamp");
		tableHeaderList.add("timestamp");

		if (null != attributes && !attributes.isEmpty()) {
			attributes.forEach((attribute) -> {
				displayHeaderList.add(attribute.getDisplayName().trim());
				tableHeaderList.add(attribute.getId().trim());
			});
		}	
	}
	
	public void setHeadersForFiles(UserReport userReport , List<String> displayHeaderList,
			List<String> tableHeaderList, ReportDetailVO reportDetailVO) throws Exception {

		Set<String> headers = new HashSet();

		List<Attribute> attributes = new ArrayList<>();
		List<Attribute> attrArr = new ArrayList<>();
		headers.remove("timestamp");
		Set<String> uniqueProperties = new HashSet();
		List<Metric> metricList = new ArrayList<>();
		List<Counter> counterList = null;
		List<CounterGroup> counterGrpListfinal = new ArrayList<>();
		List<CounterGroup> counterGrpList = reportDetailVO.getCounterGroupsWithCounterAndProperties();
		if (null != counterGrpList && !counterGrpList.isEmpty()) {

			counterGrpList.forEach(counterGp -> {
				CounterGroup cg = new CounterGroup();
				cg.setCounterGroupId(counterGp.getCounterGroupId());
				List<Counter> clist = new ArrayList();
				cg.setCounterList(clist);
				if (null != counterGp.getCounterList() && !counterGp.getCounterList().isEmpty()) {
					cg.getCounterList().addAll(counterGp.getCounterList());
				}
				if (null != counterGp.getKpis() && !counterGp.getKpis().isEmpty()) {
					cg.getCounterList().addAll(counterGp.getKpis());
				}

				counterGp.getProperties().stream().forEach(prop -> {
					uniqueProperties.add(prop.getPropertyName());

				});

				if (!cg.getCounterList().isEmpty()) {
					counterGrpListfinal.add(cg);
				}
			});
			List<Counter> counterList1 = reportService.addCounterGroupIdInCounterKey(counterGrpListfinal);
			counterList = counterService.getCountersDetail(counterList1);
			// counterList = counterService.getCountersFromIDs(metricList);

		}
		uniqueProperties.forEach(property -> {

			Attribute attr = new Attribute();
			attr.setDisplayName(property);
			attr.setId(property);
			attrArr.add(attr);
		});
		if (null != counterList && !counterList.isEmpty()) {
			// Set<String> headerSet = headers;
			counterList.forEach(c -> {
				Attribute attr = new Attribute();
				attr.setDisplayName(c.getLogicalName() + "(" + c.getCounterUnit() + ")");
				String idCt = c.getCounterKey().getCounterId() + "__"
						+ c.getCounterKey().getCounterGroup().getCounterGroupId();
				if (null != c.getAggregationList() && !c.getAggregationList().isEmpty()) {
					c.getAggregationList().forEach(aggre -> {
						attr.setId(aggre + "_" + idCt);
						attrArr.add(attr);
					});
				}
				// if(!headers.isEmpty())
				// { idCt = headers.stream().filter(h ->
				// h.contains(c.getCounterKey().getCounterId().concat("__").concat(c.getCounterKey().getCounterGroup().getCounterGroupId()))).findFirst().get();
				// }
				//
				else {
					attr.setId(idCt);
					attrArr.add(attr);
				}
			});
		}
	
		attributes.addAll(attrArr);

		displayHeaderList.add("TimeStamp");
		tableHeaderList.add("timestamp");
		attributes.forEach(attribute -> {
			displayHeaderList.add(attribute.getDisplayName().trim());
			tableHeaderList.add(attribute.getId().trim());
		});

	}

	/**
	 * @param tableFieldList
	 * @param eventDataList
	 * @param eventObj
	 * @throws Exception
	 */
	private static void parseListOfEventsForRespose(List<String> tableFieldList, List<String[]> eventDataList,
			Map<String, Object> eventObj) {
		Map<String, Object> eventDataMap = (HashMap<String, Object>) eventObj;

		List<String> datalist = new ArrayList<>();
		tableFieldList.forEach((columnName) -> {
			if (eventDataMap.get(columnName.trim()) instanceof Double) {
				datalist.add(String
						.valueOf(Double.valueOf(String.valueOf(eventDataMap.get(columnName.trim()))).longValue()));
			} else {
				/*if (columnName.equalsIgnoreCase("NodeName")) {
					String temp = String.valueOf(eventDataMap.get(columnName.trim()));
					temp = temp.substring(0, temp.length() - 1);
					datalist.add(temp);
				} else */
					datalist.add(String.valueOf(eventDataMap.get(columnName.trim())));
			}
		});
		if (!datalist.isEmpty()) {
			eventDataList.add(datalist.toArray(new String[datalist.size()]));
		}
	}

	public String generateReport(ExportDetails exportDetails) throws Exception {
		if (!StringUtils.isEmpty(exportDetails.getReportType())) {
			validateFile(exportDetails);

			if (FileType.CSV.getValue().equalsIgnoreCase(exportDetails.getReportType())) {
				writeCSVData(exportDetails);
				return exportDetails.getFilePath();
			} else if (FileType.XLS.getValue().equalsIgnoreCase(exportDetails.getReportType())) {
				// formatXLSContent(exportDetails);
				return exportDetails.getFilePath();
			} else if (FileType.PDF.getValue().equalsIgnoreCase(exportDetails.getReportType())) {
				createPDF(exportDetails);
				return exportDetails.getFilePath();
			}
		}
		return null;
	}

	public void createPDF(ExportDetails exportDetails) throws Exception {
		prepareFilePath(exportDetails);
		Document document = new Document();
		ReportPDFWriter.getInstance().createFinalPDF(exportDetails, document);
	}

	public void prepareFilePath(ExportDetails exportDetails) throws Exception {
		// This will use for append file name.
		String timeStamp = String.valueOf(System.currentTimeMillis());
		exportDetails.setReportcreationTime(timeStamp);
		// String exportFilePath =
		// String.valueOf(LoadApplicationData.siteOptionMap.get("exportFilePath"));
		// String contextPath = servletContext.getContextPath();
		String slash = "/", parentFilePath, url;

		String fileName = exportDetails.getFilename().trim().replace(" ", "_");

		parentFilePath = exportDetails.getFilePath() + slash + fileName + "_" + timeStamp;
		url = exportDetails.getFilePathURL() + slash + fileName + "_" + timeStamp;

		if (null != exportDetails) {
			if (FileType.CSV.getValue().equalsIgnoreCase(exportDetails.getReportType())) {
				exportDetails.setFilePathURL(url + "." + FileType.CSV.getValue());
				exportDetails.setFilePath(parentFilePath + "." + FileType.CSV.getValue());
			} else if (FileType.XLS.getValue().equalsIgnoreCase(exportDetails.getReportType())) {
				exportDetails.setFilePathURL(url + "." + FileType.XLS.getValue());
				exportDetails.setFilePath(parentFilePath + "." + FileType.XLS.getValue());
			} else if (FileType.PDF.getValue().equalsIgnoreCase(exportDetails.getReportType())) {
				exportDetails.setFilePathURL(url + "." + FileType.PDF.getValue());
				exportDetails.setFilePath(parentFilePath + "." + FileType.PDF.getValue());
			}
		}
	}

	public void validateFile(ExportDetails exportDetails) throws Exception {
		//String exportFilePath = String.valueOf(LoadApplicationData.siteOptionMap.get("exportFilePath"));
		String exportFilePath = exportDetailsProperties.getExportFilePath();
		String contextPath = servletContext.getContextPath(), filePath, url;
		String slash = "/";

		// directory export report
		filePath = exportFilePath + contextPath + Constant.EXPORT_REPORT_DIR;
		exportDetails.setFilePath(filePath);
		url = "http://" + InetAddress.getLocalHost().getHostAddress() + ":"
				+ exportDetailsProperties.getTomcatPort() + contextPath
				+ Constant.EXPORT_REPORT_DIR;
		exportDetails.setFilePathURL(url);

		File exportfile = new File(filePath);
		if (!exportfile.exists()) {
			if (exportfile.mkdirs()) {
				// made directory or sub directory
			}
		}
	}

	public void writeCSVData(ExportDetails exportDetails) throws Exception {
		prepareFilePath(exportDetails);
		CSVWriter csvWriter = new CSVWriter(new FileWriter(exportDetails.getFilePath()));
		List<String> fieldList = exportDetails.getFileHeaderList();
		List<String[]> csvdata = exportDetails.getFileContent();

		List<String[]> reportDetail = new ArrayList<>();
		reportDetail.add(null != exportDetails.getFilename()
				? new String[] { "Report : " + exportDetails.getFilename() } : null);
		// reportDetail.add(null!=exportDetails.getUserReport().getEmail()?new
		// String[]{"Email Id :
		// "+exportDetails.getUserReport().getEmail()}:null);
		reportDetail.add(null != exportDetails.getUserReport().getUserName()
				? new String[] { "User Name : " + exportDetails.getUserReport().getUserName() } : null);
		reportDetail
				.add(null != DateHelper.getDateTimeFormatFromMillisec(exportDetails.getReportcreationTime())
						? new String[] { "Report Creation Date : "
								+ DateHelper.getDateTimeFormatFromMillisec(exportDetails.getReportcreationTime()) }
						: null);
		for (String[] strings : reportDetail) {
			if (null != strings)
				csvWriter.writeNext(strings);
		}

		StringBuilder buildheaderstring = new StringBuilder();
		fieldList.forEach((headerName) -> {
			buildheaderstring.append(headerName).append(",");
		});
		String[] headerArray = buildheaderstring.deleteCharAt(buildheaderstring.length() - 1).toString().split(",");
		csvWriter.writeNext(headerArray);
		for (String[] csvRowArray : csvdata) {
			csvWriter.writeNext(csvRowArray);
		}
		csvWriter.close();
	}

	public String exctractReportName(Configuration configuration)
			throws IOException, JsonParseException, JsonMappingException, JsonProcessingException {
		String fileName = null;
		if (!configuration.getName().isEmpty() && null != configuration.getName()) {
			fileName = configuration.getName().trim();
		} else {
			fileName = mailAttribute.getReportName();
		}
		return fileName;
	}

	public void sendMail(String PathURL, String fileType, String userName, String reportName,
			ReportDetailVO reportDetailVO, String taskFailReason) throws Exception {

		String subject = "mail testing";
		String body = "Hello";
		String cc = null;
		String toAddresses = null;
		if (PathURL == null && fileType == null && reportName == null) {
			subject = "Exception";
			body = taskFailReason;
		}

		String mailServer = mailAttribute.getMailserver();
		String from = mailAttribute.getMailfrom();
		// String toAddresses = userDetails.getEmail();
		if (reportDetailVO != null) {
			EmailVO emailVO = reportDetailVO.getEmail();
			cc = emailVO.getCc();
			toAddresses = emailVO.getTo();
			subject = emailVO.getSubject();
		}

		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", mailServer);
		Session session = Session.getDefaultInstance(properties);
		MimeMessage message = new MimeMessage(session);

		// InternetAddress address = new InternetAddress(toAddresses);
		message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddresses.trim()));
		message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc.trim()));

		message.setFrom(new InternetAddress(from));
		message.setSubject(subject);
		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setText(body);
		mimeBodyPart.setContent(body, "text/html");

		Multipart multipart = new MimeMultipart();
		if (PathURL != null) {
			DataSource dataSource = new FileDataSource(PathURL);
			mimeBodyPart.setDataHandler(new DataHandler(dataSource));
			mimeBodyPart.setFileName(reportName + "." + fileType.toLowerCase());
		}

		/*
		 * File att = new File(new
		 * File("C://Users//611165938//Pictures//Test//"), "ReadMe.txt");
		 * mimeBodyPart.attachFile(att);
		 */

		multipart.addBodyPart(mimeBodyPart);
		message.setContent(multipart);
		message.setSentDate(new Date());
		Transport.send(message);
		System.out.println("Email has been sent successfully to  " + toAddresses);
	}
}
