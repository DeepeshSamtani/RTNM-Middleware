package com.harman.rtnm.common.property;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.harman.rtnm.samsung.commonutils.util.StringUtils;


//import com.harman.rtnm.samsung.commonutils.util.StringUtils;

@Component
@PropertySource("classpath:druid.properties")
public class DruidAttribute {

	@Value("${druid.broker.host}")
	private String druidBrokerHost;

	@Value("${druid.broker.port}")
	private String druidBrokerPort;

	@Value("${druid.broker.baseURL}")
	private String druidBrokerBaseURL;

	@Value("${druid.broker.jsonResourceURL}")
	private String druidBrokerJsonResorceURL;

	@Value("${druid.supervisor.ip}")
	private String druidSupervisorHost;	

	@Value("${druid.supervisor.port}")
	private String druidSupervisorPort;

	@Value("${druid.supervisor.jsonResourceURL}")
	private String druidSupervisorJsonResorceURL;

	@Value("${metric.specs.list}")
	private String metricSpecsList;

	private Map<String,String> mapDeviceTypeMetricSpec = null;

	public String getDruidBrokerHost() {
		return druidBrokerHost;
	}

	public void setDruidBrokerHost(String druidBrokerHost) {
		this.druidBrokerHost = druidBrokerHost;
	}

	public String getDruidBrokerPort() {
		return druidBrokerPort;
	}

	public void setDruidBrokerPort(String druidBrokerPort) {
		this.druidBrokerPort = druidBrokerPort;
	}

	public String getDruidBrokerBaseURL() {
		return druidBrokerBaseURL;
	}

	public void setDruidBrokerBaseURL(String druidBrokerBaseURL) {
		this.druidBrokerBaseURL = druidBrokerBaseURL;
	}

	public String getDruidBrokerJsonResorceURL() {
		return druidBrokerJsonResorceURL;
	}

	public void setDruidBrokerJsonResorceURL(String druidBrokerJsonResorceURL) {
		this.druidBrokerJsonResorceURL = druidBrokerJsonResorceURL;
	}


	/**
	 * @return the druidSupervisorIp
	 */
	public String getDruidSupervisorHost() {
		return druidSupervisorHost;
	}

	/**
	 * @param druidSupervisorIp the druidSupervisorIp to set
	 */
	public void setDruidSupervisorHost(String druidSupervisorIp) {
		this.druidSupervisorHost = druidSupervisorIp;
	}

	/**
	 * @return the druidSupervisorPort
	 */
	public String getDruidSupervisorPort() {
		return druidSupervisorPort;
	}

	/**
	 * @param druidSupervisorPort the druidSupervisorPort to set
	 */
	public void setDruidSupervisorPort(String druidSupervisorPort) {
		this.druidSupervisorPort = druidSupervisorPort;
	}

	/**
	 * @return the druidSupervisorJsonResorceURL
	 */
	public String getDruidSupervisorJsonResorceURL() {
		return druidSupervisorJsonResorceURL;
	}

	/**
	 * @param druidSupervisorJsonResorceURL the druidSupervisorJsonResorceURL to set
	 */
	public void setDruidSupervisorJsonResorceURL(String druidSupervisorJsonResorceURL) {
		this.druidSupervisorJsonResorceURL = druidSupervisorJsonResorceURL;
	}

	public void setMetricSpecsList(String metricSpecsList) {
		this.metricSpecsList = metricSpecsList;
	}	

	/**
	 * @return the metricSpecsList
	 */
	public String getMetricSpecsList() {
		return metricSpecsList;
	}

	private void loadMetricSpecNames(String metricSpecList) {
		this.mapDeviceTypeMetricSpec = StringUtils.stringToKeyValueMap(metricSpecList, StringUtils.SYMBOL_COMMA, StringUtils.SYMBOL_COLON); 
	}

	public String getMetricSpecName(String deviceType) {
		if(this.mapDeviceTypeMetricSpec == null || this.mapDeviceTypeMetricSpec.size() == 0) {
			loadMetricSpecNames(getMetricSpecsList());
		}

		return mapDeviceTypeMetricSpec.get(deviceType);
	}

	/**
	 * @param metricSpecsList the metricSpecsList to set
	 */

	@Override
	public String toString() {
		return "DruidAttribute [druidBrokerHost=" + druidBrokerHost + ", druidBrokerPort=" + druidBrokerPort
				+ ", druidBrokerBaseURL=" + druidBrokerBaseURL + ", druidBrokerJsonResorceURL="
				+ druidBrokerJsonResorceURL + "]";
	}
}
