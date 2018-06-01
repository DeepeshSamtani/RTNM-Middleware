package com.harman.rtnm.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harman.dyns.model.druid.request.ReportRequest;
import com.harman.rtnm.dao.ProfileDao;
import com.harman.rtnm.model.Access;
import com.harman.rtnm.model.Profile;
import com.harman.rtnm.model.request.ReportTemplate;
import com.harman.rtnm.model.response.DashboardReport;
import com.harman.rtnm.model.response.ProfileResponse;
import com.harman.rtnm.model.response.SubDashboardReport;
import com.harman.rtnm.model.response.UserReport;
import com.harman.rtnm.service.ProfileService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProfileServiceImpl implements ProfileService {

	@Autowired
	ProfileDao profileDao;

	ObjectMapper objectMapper = new ObjectMapper();

	@Override
	@Transactional
	public ProfileResponse profileDetail(Long profileId, boolean isreportConfigurationNeed) throws Exception {
		Profile profileDb = profileDao.profileDetail(profileId);
		ProfileResponse profile = new ProfileResponse();
		if (null != profileDb) {
			profile.setProfileId(profileDb.getProfileId());
			profile.setDataAccess(profileDb.getDataAccess());
			profile.setProfileName(profileDb.getProfileName());
			profile.setCommunityString(profileDb.getCommunityString());
			profile.setApplicableProfiles(profileDb.getApplicableProfiles());
			if (null != profileDb.getDashboards()) {
				List<DashboardReport> dashboards = new ArrayList<>();
				profileDb.getDashboards().stream().forEach(dash -> {
					DashboardReport dashboard = new DashboardReport();
					dashboard.setId(dash.getDashboardId());
					dashboard.setName(dash.getName());
					if (null != dash.getType())
						dashboard.setType(dash.getType());
					if (null != dash.getView())
						dashboard.setView(dash.getView());
					if (null != dash.getSubDashboards()) {
						List<SubDashboardReport> subDashboards = new ArrayList<>();
						dash.getSubDashboards().stream().forEach(subdash -> {
							SubDashboardReport subDashboard = new SubDashboardReport();
							subDashboard.setSubDashboardId(subdash.getSubDashboardId());
							subDashboard.setName(subdash.getName());
							if (null != subdash.getDashboardTemplates()) {
								List<UserReport> dashboardTemplates = new ArrayList<>();
								subdash.getDashboardTemplates().forEach(dashboardGraph -> {
									UserReport userReport = new UserReport();
									userReport.setUserTemplateId(dashboardGraph.getDashboardtemplateId());
									userReport.setReportName(dashboardGraph.getReportName());
									if (isreportConfigurationNeed) {
										Map<String, Object> reportDetailMap = null;
									
										try {
											
											Map<String, Object> inputjson = objectMapper.readValue(new String(dashboardGraph.getReportDetail()),
													new TypeReference<Map<String, Object>>() {
													});
											userReport.setReportConfiguration(objectMapper.convertValue(inputjson, ReportTemplate.class));
													
										} catch (JsonParseException e) {
											e.printStackTrace();
										} catch (JsonMappingException e) {
											e.printStackTrace();
										} catch (IOException e) {
											e.printStackTrace();
										}

										
									}
									dashboardTemplates.add(userReport);
								});

								subDashboard.setReports(dashboardTemplates);
							}
							subDashboards.add(subDashboard);
						});
						dashboard.setSubDashboards(subDashboards);
					}
					dashboards.add(dashboard);
				});
				profile.setDashboards(dashboards);
			}
			if (null != profileDb.getAccesses() && !profileDb.getAccesses().isEmpty()) {
				List<Access> accesses = new ArrayList<>();
				profileDb.getAccesses().stream().forEach(acc -> {
					Access access = new Access();
					access.setAccessId(acc.getAccessId());
					access.setAccessName(acc.getAccessName());
					access.setDisplayName(acc.getDisplayName());
					accesses.add(access);
				});
				profile.setAccesses(accesses);
			}
		}

		return profile;
	}
}
