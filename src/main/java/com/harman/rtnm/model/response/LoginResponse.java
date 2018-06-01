package com.harman.rtnm.model.response;

import java.io.Serializable;
import java.util.List;

import com.harman.rtnm.model.Profile;

public class LoginResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1427664932787950035L;

	List<UserReport> reports = null;
	List<DashboardReport> dashboards = null;
	ProfileResponse profileResponse = new ProfileResponse();

	public List<UserReport> getReports() {
		return reports;
	}

	public void setReports(List<UserReport> reports) {
		this.reports = reports;
	}

	public List<DashboardReport> getDashboards() {
		return dashboards;
	}

	public void setDashboards(List<DashboardReport> dashboards) {
		this.dashboards = dashboards;
	}

	public ProfileResponse getProfileResponse() {
		return profileResponse;
	}

	public void setProfileResponse(ProfileResponse profileResponse) {
		this.profileResponse = profileResponse;
	}	

}
