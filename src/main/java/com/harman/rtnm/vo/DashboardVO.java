package com.harman.rtnm.vo;

import java.util.List;

import com.harman.rtnm.model.Profile;

public class DashboardVO {
	private String name;
	private String id;
	private List<SubDashboardVO> subDashboards;
	private List<Profile> profiles;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SubDashboardVO> getSubDashboards() {
		return subDashboards;
	}

	public void setSubDashboards(List<SubDashboardVO> subDashboards) {
		this.subDashboards = subDashboards;
	}

	public List<Profile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	

}
