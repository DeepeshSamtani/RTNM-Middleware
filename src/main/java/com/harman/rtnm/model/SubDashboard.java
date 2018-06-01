package com.harman.rtnm.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "SUB_DASHBOARD_DETAILS")
public class SubDashboard implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2751931251421628111L;

	@Id
	@Column(name = "SUB_DASHBOARD_ID", nullable = false)
	private String subDashboardId;
	
	@Column(name = "NAME", nullable = false)
	private String name;
		
//	@JsonIgnore
//	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "subDashboards")
//	private List<Dashboard> dashboards;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER , orphanRemoval = true)
	@JoinTable(name = "SUBDASHBOARD_DASHBOARD_TEMPLATE", joinColumns = @JoinColumn(name = "SUB_DASHBOARD_ID"), inverseJoinColumns = @JoinColumn(name = "DASHBOARD_TEMPLATE_ID"))
	private List<DashboardTemplate> dashboardTemplates;

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
//
//	public List<Dashboard> getDashboards() {
//		return dashboards;
//	}
//
//	public void setDashboards(List<Dashboard> dashboards) {
//		this.dashboards = dashboards;
//	}

	public List<DashboardTemplate> getDashboardTemplates() {
		return dashboardTemplates;
	}

	public void setDashboardTemplates(List<DashboardTemplate> dashboardTemplates) {
		this.dashboardTemplates = dashboardTemplates;
	}

}
