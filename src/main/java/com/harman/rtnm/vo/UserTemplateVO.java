package com.harman.rtnm.vo;

import java.io.Serializable;
import java.util.Map;

import com.harman.rtnm.model.Dashboard;
import com.harman.rtnm.model.UserDetail;

public class UserTemplateVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8856589631702706938L;

	private String userTemplateId;
	private UserDetail userDetail;
	private String reportName;
	private Map<String, Object> reportConfiguration;
	private Dashboard dashboard;

	public String getUserTemplateId() {
		return userTemplateId;
	}

	public void setUserTemplateId(String userTemplateId) {
		this.userTemplateId = userTemplateId;
	}

	public UserDetail getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public Map<String, Object> getReportConfiguration() {
		return reportConfiguration;
	}

	public void setReportConfiguration(Map<String, Object> reportConfiguration) {
		this.reportConfiguration = reportConfiguration;
	}

	public Dashboard getDashboard() {
		return dashboard;
	}

	public void setDashboard(Dashboard dashboard) {
		this.dashboard = dashboard;
	}

}
