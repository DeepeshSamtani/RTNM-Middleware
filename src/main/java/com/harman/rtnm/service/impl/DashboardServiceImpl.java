package com.harman.rtnm.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.harman.rtnm.common.helper.RandomNumberHelper;
import com.harman.rtnm.common.property.CommonAttribute;
import com.harman.rtnm.dao.DashboardDao;
import com.harman.rtnm.dao.DashboardTemplateDao;
import com.harman.rtnm.dao.SubDashboardDao;
import com.harman.rtnm.model.Dashboard;
import com.harman.rtnm.model.DashboardTemplate;
import com.harman.rtnm.model.Profile;
import com.harman.rtnm.model.SubDashboard;
import com.harman.rtnm.model.request.ReportTemplate;
import com.harman.rtnm.model.response.DashboardReport;
import com.harman.rtnm.model.response.SubDashboardReport;
import com.harman.rtnm.model.response.UserReport;
import com.harman.rtnm.service.DashboardService;
import com.harman.rtnm.service.DashboardTemplateService;
import com.harman.rtnm.service.ReportService;
import com.harman.rtnm.service.UserDetailService;
import com.harman.rtnm.vo.DashboardVO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DashboardServiceImpl implements DashboardService {

	@Autowired
	DashboardTemplateService dashboardTemplateService;

	@Autowired
	UserDetailService userDetailService;

	@Autowired
	ReportService reportService;

	@Autowired
	DashboardDao dashboardDao;
	
	@Autowired
	DashboardTemplateDao dashboardTemplateDao;
	
	@Autowired
	SubDashboardDao subDashboardDao;
	
	@Autowired
	CommonAttribute commonAttribute;

	ObjectMapper mapper = new ObjectMapper();

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public DashboardReport getDashboardReportWithSubDashboards(String dashboardId) throws Exception {
		DashboardReport dashboardReport = new DashboardReport();
		Dashboard dashboard = dashboardDao.getDashboard(dashboardId);
		if (null != dashboard) {
			BeanUtils.copyProperties(dashboard, dashboardReport);
			dashboardReport.setId(dashboard.getDashboardId());
			dashboardReport.setSubDashboards(convertSubDashboardsToSubdashboardReports(dashboard));
			dashboardReport.setProfiles(dashboard.getProfiles());
			dashboardReport.getProfiles().forEach(profile ->{
				profile.setDashboards(null);
				profile.setApplicableProfiles(null);
			});

		}
		return dashboardReport;
	}

	public List<UserReport> convertDashboardTemplateToUserReport(List<DashboardTemplate> dashboardTemplatelist)
			throws JsonParseException, JsonMappingException, IOException {
		List<UserReport> reportList = new ArrayList<>();
		for (DashboardTemplate dashboardTemplate : dashboardTemplatelist) {
			UserReport report = new UserReport();
			report.setUserTemplateId(dashboardTemplate.getDashboardtemplateId());
			report.setReportName(dashboardTemplate.getReportName());
			Map<String, Object> inputjson = mapper.readValue(new String(dashboardTemplate.getReportDetail()),
					new TypeReference<Map<String, Object>>() {
					});
			report.setReportConfiguration(mapper.convertValue(inputjson, ReportTemplate.class));
			reportList.add(report);
		}
		return reportList;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.SUPPORTS)
	public DashboardReport addDashboard(DashboardVO dashboardVO) throws Exception{
		boolean createDashboard= false;
		Dashboard dashboard = new Dashboard();
		 DashboardReport dashboardReport = new DashboardReport();
		dashboard.setName(dashboardVO.getName());
		dashboard.setDashboardId(RandomNumberHelper.generateRandomAlphaNumericString());
		dashboard.setType("common");
		dashboard.setView("both");
		//dashboardVO.getProfiles().stream().collect(Collectors.toMap(Profile::getProfileId, Profile::getCommunityString));
		Map<Long,String> profiles = new HashMap<>();
		dashboardVO.getProfiles().forEach(e->{
			profiles.put(e.getProfileId(), e.getProfileName());
		});
		Map<Long, Integer> dashCountMap = dashboardDao.getDashboardCountForProfiles(new ArrayList<>(profiles.keySet()));
		Iterator<Entry<Long,Integer>> iterator = dashCountMap.entrySet().iterator();
	   while(iterator.hasNext())
		{
			Entry<Long, Integer> entry = iterator.next();
			if (entry.getValue() != commonAttribute.getMaxDashboardCount() && entry.getValue() < commonAttribute.getMaxDashboardCount()) {
				createDashboard = true;
				iterator.remove();
			}
		}
	
		System.out.println("dashCountMap  "+dashCountMap);
		if(createDashboard){
		if(null != dashboardVO.getSubDashboards())
		{   List<SubDashboard> subdashboards= new ArrayList<>();
			dashboardVO.getSubDashboards().forEach(subdashboard -> {
				SubDashboard subdash = new SubDashboard();
				if (null != subdashboard) {
					BeanUtils.copyProperties(subdashboard, subdash);
					subdash.setSubDashboardId(RandomNumberHelper.generateRandomAlphaNumericString());
					List<DashboardTemplate> templateList = new ArrayList<>();
					if (null != subdashboard.getReports()) {
						subdashboard.getReports().forEach(report -> {
							DashboardTemplate template = new DashboardTemplate();
							template.setDashboardtemplateId(
									RandomNumberHelper.generateRandomAlphaNumericStringWithSevendigit());
							
							template.setReportName(report.getReportName());
							ObjectMapper mapperObj = new ObjectMapper();
							String reportRequestJson;
							try {
								reportRequestJson = mapperObj.writeValueAsString(report.getReportConfiguration());
								template.setReportDetail(reportRequestJson.getBytes());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							templateList.add(template);
						
						});
						subdash.setDashboardTemplates(templateList);
					}
					
				}
				subdashboards.add(subdash);
			});
			dashboard.setSubDashboards(subdashboards);
			List<Profile> profilesforDash = new ArrayList<>();
				dashboardVO.getProfiles().forEach(profile -> {
					if (dashCountMap.get(profile.getProfileId()) == null) {
						profilesforDash.add(profile);
					}
				});
			dashboard.setProfiles(profilesforDash);
		}
		  
		  dashboardDao.addDashboard(dashboard);
		  dashboardReport = ConvertDasboardToDashboardReport(dashboard);
	    }
		else{
			throw new Exception("Dashboard can not be created .Maximum Limit has been reached for all profiles.");
		}
		if(null != dashCountMap && dashCountMap.size() > 0)
		{
			String profileNames = "";
			for (Long key : dashCountMap.keySet()) {
				if (profileNames.isEmpty()) {
					profileNames = profiles.get(key);
				} else {
					profileNames = profileNames + " and " + profiles.get(key);
				}
			}

			dashboardReport.setMessage("Dashboard is not created for " + profileNames + "  profile");
		}
		  dashboardReport.setProfiles(dashboard.getProfiles());
		  return dashboardReport;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.SUPPORTS)
	public DashboardReport updateDashboard(DashboardVO dashboardVO) throws Exception {
		List<String> existingtemplateIds = new ArrayList<>();
		DashboardReport dashboardReport=null;
		Dashboard dashboardDB = dashboardDao.getDashboard(dashboardVO.getId());
		if(null != dashboardDB)
		{ List<String> existingsubdashboardIds = dashboardDB.getSubDashboards().stream()
				.map(subdashboard -> subdashboard.getSubDashboardId()).collect(Collectors.toList());
		dashboardDB.getSubDashboards().forEach(subdashboard -> {
			List<String> ids = subdashboard.getDashboardTemplates().stream()
					.map(report -> report.getDashboardtemplateId()).collect(Collectors.toList());
			existingtemplateIds.addAll(ids);
		});
		Dashboard dashboard = convertDashboardVOToDashboard(dashboardVO, existingsubdashboardIds, existingtemplateIds);
		List<String> updatedsubdashboardIds = dashboard.getSubDashboards().stream()
				.map(subdashboard -> subdashboard.getSubDashboardId()).collect(Collectors.toList());
		List<String> updatedtemplateIds = new ArrayList<>();
		dashboard.getSubDashboards().forEach(subdashboard -> {
			List<String> ids = subdashboard.getDashboardTemplates().stream()
					.map(report -> report.getDashboardtemplateId()).collect(Collectors.toList());
			updatedtemplateIds.addAll(ids);
		});
		List<String> subdashboardIdsTodelete = existingsubdashboardIds.stream()
				.filter(p -> !updatedsubdashboardIds.contains(p)).collect(Collectors.toList());
		List<String> templateIdsTodelete = existingtemplateIds.stream().filter(p -> !updatedtemplateIds.contains(p))
				.collect(Collectors.toList());
		
		dashboardDao.updateDashboard(dashboard ,subdashboardIdsTodelete ,templateIdsTodelete);
		
		dashboardReport = ConvertDasboardToDashboardReport(dashboard);
		dashboardReport.setProfiles(dashboardVO.getProfiles()); 
		}
		else{
			throw new Exception("Dashboard Does not exist.");
		}
		return dashboardReport;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.SUPPORTS)
	public void deleteDashboard(DashboardVO dashboardVO) throws Exception{
			dashboardDao.deleteDashboard(dashboardVO);
	}
	
	
	public DashboardReport ConvertDasboardToDashboardReport(Dashboard dashboard)
	{
		DashboardReport dashboardReport = new DashboardReport();
		dashboardReport.setId(dashboard.getDashboardId());
		dashboardReport.setName(dashboard.getName());
		dashboardReport.setType(dashboard.getType());
		dashboardReport.setView(dashboard.getView());
		dashboardReport.setSubDashboards(convertSubDashboardsToSubdashboardReports(dashboard));		
		return dashboardReport;
	}

	List<SubDashboardReport> convertSubDashboardsToSubdashboardReports(Dashboard dashboard) {
		List<SubDashboardReport> subDashboardReportList = new ArrayList<>();
		if (null != dashboard.getSubDashboards()) {
			dashboard.getSubDashboards().forEach(subdashoard -> {

				SubDashboardReport subDashboardReport = new SubDashboardReport();
				try {
					subDashboardReport.setName(subdashoard.getName());
					subDashboardReport.setDashboardId(dashboard.getDashboardId());
					subDashboardReport.setSubDashboardId(subdashoard.getSubDashboardId());
					subDashboardReport
							.setReports(convertDashboardTemplateToUserReport(subdashoard.getDashboardTemplates()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				subDashboardReportList.add(subDashboardReport);
			});
		}
		return subDashboardReportList;
	}

	public Dashboard convertDashboardVOToDashboard(DashboardVO dashboardVO ,List<String> subdashboardIds,List<String> templateIds) throws Exception {
		Dashboard dashboard = new Dashboard();
		dashboard.setName(dashboardVO.getName());
		dashboard.setDashboardId(dashboardVO.getId());
		dashboard.setType("common");
		dashboard.setView("both");
					
		if (null != dashboardVO.getSubDashboards()) {
			List<SubDashboard> subdashboards = new ArrayList<>();
			dashboardVO.getSubDashboards().forEach(subdashboard -> {
				SubDashboard subdash = new SubDashboard();
				if (null != subdashboard) {
					BeanUtils.copyProperties(subdashboard, subdash);
					if (subdashboardIds.contains(subdashboard.getSubDashboardId())) {
						subdash.setSubDashboardId(subdashboard.getSubDashboardId());
					} else {
						subdash.setSubDashboardId(RandomNumberHelper.generateRandomAlphaNumericString());
					}
					List<DashboardTemplate> templateList = new ArrayList<>();
					if (null != subdashboard.getReports()) {

						subdashboard.getReports().forEach(report -> {
							DashboardTemplate template = new DashboardTemplate();
							if (templateIds.contains(report.getUserTemplateId())) {
								template.setDashboardtemplateId(report.getUserTemplateId());
							} else {
								template.setDashboardtemplateId(
										RandomNumberHelper.generateRandomAlphaNumericStringWithSevendigit());
							}
							template.setReportName(report.getReportName());
							ObjectMapper mapperObj = new ObjectMapper();
							String reportRequestJson;
							try {
								reportRequestJson = mapperObj.writeValueAsString(report.getReportConfiguration());
								template.setReportDetail(reportRequestJson.getBytes());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							templateList.add(template);

						});
						subdash.setDashboardTemplates(templateList);
					}

				}
				subdashboards.add(subdash);
			});
			dashboard.setSubDashboards(subdashboards);
			dashboard.setProfiles(dashboardVO.getProfiles());
		}
		return dashboard;
	}
}
