package com.harman.rtnm.vo;

import java.util.List;
import java.util.Map;

import com.harman.rtnm.model.response.UserReport;

public class SubDashboardVO {

	private String subDashboardId;
	private String name;
	private List<UserTemplateVO> reports;
	
	public String getSubDashboardId() {
		return subDashboardId;
	}

	public void setSubDashboardId(String subDashboardId) {
		this.subDashboardId = subDashboardId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<UserTemplateVO> getReports() {
		return reports;
	}

	public void setReports(List<UserTemplateVO> reports) {
		this.reports = reports;
	}

	@Override
	public String toString() {
		return "SubDashboardVO [subDashboardId=" + subDashboardId + ", name=" + name + ", reports=" + reports + "]";
	}


	
	

}
