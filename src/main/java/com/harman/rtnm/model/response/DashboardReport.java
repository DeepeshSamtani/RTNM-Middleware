package com.harman.rtnm.model.response;

import java.io.Serializable;
import java.util.List;

import com.harman.rtnm.model.Profile;
import com.harman.rtnm.model.SubDashboard;

public class DashboardReport implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8299019498923043736L;
	
	private String id;
	private String name;
	private String type;
	private String view;
	
	private List<SubDashboardReport> subDashboards;
	private List<Profile> profiles;
	private String message;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public List<SubDashboardReport> getSubDashboards() {
		return subDashboards;
	}
	public void setSubDashboards(List<SubDashboardReport> subDashboards) {
		this.subDashboards = subDashboards;
	}	
	
	public List<Profile> getProfiles() {
		return profiles;
	}
	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "DashboardReport [id=" + id + ", name=" + name + ", type=" + type + ", view=" + view + ", subDashboards="
				+ subDashboards + "]";
	}

   
	
	
	
}
