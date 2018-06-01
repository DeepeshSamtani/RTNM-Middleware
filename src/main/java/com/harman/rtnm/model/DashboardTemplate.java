package com.harman.rtnm.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table(name="DASHBOARD_TEMPLATE")
@Entity
public class DashboardTemplate implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7559006185034981788L;

	@Id
	@Column(name = "DASHBOARD_TEMPLATE_ID")
	private String dashboardtemplateId;
	
	@Column(name="REPORT_NAME", nullable=false)
	private String reportName;
	
	@Lob
	@Column(name = "REPORT_DETAILS",nullable = false)
	private byte[] reportDetail;	

	@JsonIgnore
	@OneToMany( fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "dashboardTemplates")
	private List<SubDashboard> subDashboards;

	public String getDashboardtemplateId() {
		return dashboardtemplateId;
	}

	public void setDashboardtemplateId(String dashboardtemplateId) {
		this.dashboardtemplateId = dashboardtemplateId;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public byte[] getReportDetail() {
		return reportDetail;
	}

	public void setReportDetail(byte[] reportDetail) {
		this.reportDetail = reportDetail;
	}

//	public List<SubDashboard> getSubDashboards() {
//		return subDashboards;
//	}
//
//	public void setSubDashboards(List<SubDashboard> subDashboards) {
//		this.subDashboards = subDashboards;
//	}
//	
	
}
