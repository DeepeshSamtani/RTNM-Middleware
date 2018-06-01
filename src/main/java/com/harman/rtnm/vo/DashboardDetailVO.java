package com.harman.rtnm.vo;

import java.io.Serializable;
import java.util.Map;

public class DashboardDetailVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6565406058340553963L;
	private String userName;
	private String dashboardTemplateId;
	private String dashboardId;
	private String subDashboardId;
	private Map<String, Object> jsonString;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDashboardTemplateId() {
		return dashboardTemplateId;
	}

	public void setDashboardTemplateId(String dashboardTemplateId) {
		this.dashboardTemplateId = dashboardTemplateId;
	}

	public String getDashboardId() {
		return dashboardId;
	}

	public void setDashboardId(String dashboardId) {
		this.dashboardId = dashboardId;
	}

	
	public Map<String, Object> getJsonString() {
		return jsonString;
	}

	public void setJsonString(Map<String, Object> jsonString) {
		this.jsonString = jsonString;
	}

	
	public String getSubDashboardId() {
		return subDashboardId;
	}

	public void setSubDashboardId(String subDashboardId) {
		this.subDashboardId = subDashboardId;
	}

	@Override
	public String toString() {
		return "DashboardDetailVO [userName=" + userName + ", dashboardTemplateId=" + dashboardTemplateId
				+ ", dashboardId=" + dashboardId + ", subDashboardId=" + subDashboardId + ", jsonString=" + jsonString
				+ "]";
	}



}
