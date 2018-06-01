package com.harman.rtnm.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Embeddable
public class DashboardTemplateKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "DASHBOARD_TEMPLATE_ID")
	private String dashboardtemplateId;
	
	
	@JoinColumns({
	        @JoinColumn(name="DASHBOARD_ID", referencedColumnName="DASHBOARD_ID"),
	        @JoinColumn(name="SUB_DASHBOARD_ID", referencedColumnName="SUB_DASHBOARD_ID")})
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY )
	private SubDashboard subDashboards;

	public String getDashboardtemplateId() {
		return dashboardtemplateId;
	}

	public void setDashboardtemplateId(String dashboardtemplateId) {
		this.dashboardtemplateId = dashboardtemplateId;
	}

	public SubDashboard getSubDashboards() {
		return subDashboards;
	}

	public void setSubDashboards(SubDashboard subDashboards) {
		this.subDashboards = subDashboards;
	}
	
}
