package com.harman.rtnm.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.harman.rtnm.model.CounterGroup;

public class ReportDetailVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1941203817104131278L;
	
	
	private String userName;
	
	private String userTemplateId;
	
	private Map<String, Object> jsonString;
	
	private String fileType;
	
	private EmailVO email;
	
	private Boolean scheduledReport;
	
	private Boolean isDashboardReport;
	
	private String dashboardId;
	
	private String subDashboardId;
	
	private List<CounterGroup> counterGroupsWithCounterAndProperties;
	
	
	
	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getUserTemplateId() {
		return userTemplateId;
	}

	public void setUserTemplateId(String userTemplateId) {
		this.userTemplateId = userTemplateId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Map<String, Object> getJsonString() {
		return jsonString;
	}

	public void setJsonString(Map<String, Object> jsonString) {
		this.jsonString = jsonString;
	}

	public EmailVO getEmail() {
		return email;
	}

	public void setEmail(EmailVO email) {
		this.email = email;
	}

	public Boolean getScheduledReport() {
		return scheduledReport;
	}

	public void setScheduledReport(Boolean scheduledReport) {
		this.scheduledReport = scheduledReport;
	}

	public Boolean getIsDashboardReport() {
		return isDashboardReport;
	}

	public void setIsDashboardReport(Boolean isDashboardReport) {
		this.isDashboardReport = isDashboardReport;
	}

	public String getDashboardId() {
		return dashboardId;
	}

	public void setDashboardId(String dashboardId) {
		this.dashboardId = dashboardId;
	}

	public String getSubDashboardId() {
		return subDashboardId;
	}

	public void setSubDashboardId(String subDashboardId) {
		this.subDashboardId = subDashboardId;
	}
	
	

	public List<CounterGroup> getCounterGroupsWithCounterAndProperties() {
		return counterGroupsWithCounterAndProperties;
	}

	public void setCounterGroupsWithCounterAndProperties(List<CounterGroup> counterGroupsWithCounterAndProperties) {
		this.counterGroupsWithCounterAndProperties = counterGroupsWithCounterAndProperties;
	}

	@Override
	public String toString() {
		return "ReportDetailVO [userName=" + userName + ", userTemplateId=" + userTemplateId + ", jsonString="
				+ jsonString + ", fileType=" + fileType + ", email=" + email + ", scheduledReport=" + scheduledReport
				+ ", isDashboardReport=" + isDashboardReport + ", dashboardId=" + dashboardId + ", subDashboardId="
				+ subDashboardId + ", counterGroupsWithCounterAndProperties=" + counterGroupsWithCounterAndProperties
				+ "]";
	}



		
	
	
	
}
