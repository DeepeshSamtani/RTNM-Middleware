package com.harman.rtnm.model;


import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class SubDashboardKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "SUB_DASHBOARD_ID", nullable = false)
	private String subDashboardId;
	
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "DASHBOARD_ID")
	private Dashboard dashboardId;

	public String getSubDashboardId() {
		return subDashboardId;
	}

	public void setSubDashboardId(String subDashboardId) {
		this.subDashboardId = subDashboardId;
	}

	public Dashboard getDashboardId() {
		return dashboardId;
	}

	public void setDashboardId(Dashboard dashboardId) {
		this.dashboardId = dashboardId;
	}

	
}

