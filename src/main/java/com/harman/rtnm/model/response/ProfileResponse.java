package com.harman.rtnm.model.response;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.harman.rtnm.model.Access;
import com.harman.rtnm.model.Profile;

public class ProfileResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8485790360172721602L;

	private Long profileId;

	private String profileName;

	private String communityString;

	private String dataAccess;

	private List<Access> accesses;

	private Set<Profile> applicableProfiles;
	
	private List<DashboardReport> dashboards;

	public Long getProfileId() {
		return profileId;
	}

	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getCommunityString() {
		return communityString;
	}

	public void setCommunityString(String communityString) {
		this.communityString = communityString;
	}

	public String getDataAccess() {
		return dataAccess;
	}

	public void setDataAccess(String dataAccess) {
		this.dataAccess = dataAccess;
	}

	public List<Access> getAccesses() {
		return accesses;
	}

	public void setAccesses(List<Access> accesses) {
		this.accesses = accesses;
	}

	public List<DashboardReport> getDashboards() {
		return dashboards;
	}

	public void setDashboards(List<DashboardReport> dashboards) {
		this.dashboards = dashboards;
	}

	public Set<Profile> getApplicableProfiles() {
		return applicableProfiles;
	}

	public void setApplicableProfiles(Set<Profile> applicableProfiles) {
		this.applicableProfiles = applicableProfiles;
	}
	

}
