package com.harman.rtnm.model.response;

import java.io.Serializable;
import java.util.List;

import com.harman.rtnm.model.DashboardTemplate;

public class SubDashboardReport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3150369288012436449L;

	private String name;
	private String subDashboardId;
	private String dashboardId;
	List<UserReport> reports;
	private List<DashboardTemplate> dashboardTemplates;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubDashboardId() {
		return subDashboardId;
	}

	public void setSubDashboardId(String subDashboardId) {
		this.subDashboardId = subDashboardId;
	}

	public String getDashboardId() {
		return dashboardId;
	}

	public void setDashboardId(String dashboardId) {
		this.dashboardId = dashboardId;
	}

	public List<UserReport> getReports() {
		return reports;
	}

	public void setReports(List<UserReport> reports) {
		this.reports = reports;
	}

	public List<DashboardTemplate> getDashboardTemplates() {
		return dashboardTemplates;
	}

	public void setDashboardTemplates(List<DashboardTemplate> dashboardTemplates) {
		this.dashboardTemplates = dashboardTemplates;
	}

}
