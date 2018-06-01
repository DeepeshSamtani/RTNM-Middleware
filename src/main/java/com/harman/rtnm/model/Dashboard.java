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
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "DASHBOARD_DETAILS")
public class Dashboard implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4482578617706295712L;

	@Id
	@Column(name = "DASHBOARD_ID", nullable = false)
	private String dashboardId;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "TYPE", nullable = false)
	private String type;

	@Column(name = "VIEW", nullable = false)
	private String view;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER , orphanRemoval = true )
	@JoinTable(name = "DASHBOARD_SUBDASHBOARD", joinColumns = @JoinColumn(name = "DASHBOARD_ID",unique = false), inverseJoinColumns = @JoinColumn(name = "SUB_DASHBOARD_ID",unique = false))
	private List<SubDashboard> subDashboards;

	@JsonIgnore
	@ManyToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinTable(name = "PROFILE_DASHBOARD", joinColumns = @JoinColumn(name = "DASHBOARD_ID",unique = false), inverseJoinColumns = @JoinColumn(name = "PROFILE_ID",unique = false),uniqueConstraints = {@UniqueConstraint(columnNames={"DASHBOARD_ID", "PROFILE_ID"})} )
	private List<Profile> profiles;
	
	

	public String getDashboardId() {
		return dashboardId;
	}

	public void setDashboardId(String dashboardId) {
		this.dashboardId = dashboardId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}


	public List<SubDashboard> getSubDashboards() {
		return subDashboards;
	}

	public void setSubDashboards(List<SubDashboard> subDashboards) {
		this.subDashboards = subDashboards;
	}

	public List<Profile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}

	
}
