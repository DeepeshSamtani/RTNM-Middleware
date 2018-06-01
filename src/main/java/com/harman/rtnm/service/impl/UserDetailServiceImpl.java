package com.harman.rtnm.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harman.rtnm.dao.UserDetailDao;
import com.harman.rtnm.model.Profile;
import com.harman.rtnm.model.UserDetail;
import com.harman.rtnm.model.UserTemplate;
import com.harman.rtnm.model.response.DashboardReport;
import com.harman.rtnm.model.response.LoginResponse;
import com.harman.rtnm.model.response.ProfileResponse;
import com.harman.rtnm.model.response.UserReport;
import com.harman.rtnm.service.DashboardTemplateService;
import com.harman.rtnm.service.ProfileService;
import com.harman.rtnm.service.UserDetailService;
import com.harman.rtnm.service.UserTemplateService;
import com.harman.rtnm.vo.UserDetailVO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserDetailServiceImpl implements UserDetailService {

	@Autowired
	private UserDetailDao userDetailDao;
	
	@Autowired
	UserTemplateService userTemplateService;
	
	@Autowired
	DashboardTemplateService dashboardTemplateService;
	
	@Autowired
	ProfileService profileService;
	
	ObjectMapper mapper = new ObjectMapper();
	
	@Override
	@Transactional
	public LoginResponse getUserDataWithProfile(UserDetailVO userDetailVO) throws Exception, JsonParseException, JsonMappingException, IOException {
		
		String userName = userDetailVO.getUserName();
		Profile profile = userDetailVO.getProfiles().get(0); 
		UserDetail userDetail = null;
		List<UserReport> userReportList=null;
		LoginResponse response = null;
		
		if (null != userName) {
			userDetail = userDetailDao.loadUserByName(userName);
		}
		if (null != userDetail) {
			userReportList  = convertUserTemplateToUserReport(
					userTemplateService.getUserTemplateByUserId(userDetail.getUserId()), userName);
			ProfileResponse poProfileResponse = profileService.profileDetail(profile.getProfileId(),true) ;
			
		    response = new LoginResponse();
			response.setReports(userReportList);
			response.setProfileResponse(poProfileResponse);
		}
		return response;
	}
	
	private List<UserReport> convertUserTemplateToUserReport(List<UserTemplate> userTemplatelist,String userName) throws JsonParseException, JsonMappingException, IOException{
		List<UserReport> userReportList=new ArrayList<>();
		for (UserTemplate userTemplate : userTemplatelist) {
			UserReport userReport = new UserReport();
			userReport.setUserTemplateId(userTemplate.getUserTemplateKey().getUsertemplateId());
			userReport.setReportName(userTemplate.getReportName());
			userReport.setUserName(userName);
			userReportList.add(userReport);
		}
		return userReportList;
	}

	@Override
	@Transactional
	public UserDetail loadUserByName(String userName)throws Exception{
		return userDetailDao.loadUserByName(userName);
	}

	@Override
	@Transactional
	public List<Profile> getProfilesByUserName(String userName) throws Exception {
		UserDetail userDetail = null;
		List<Profile> profiles = new ArrayList<>();
		if (null != userName) {
			userDetail = userDetailDao.loadUserByName(userName);
			userDetail.getProfiles().forEach(p->{
				Profile profile = new Profile();
				profile.setProfileId(p.getProfileId());
				profile.setProfileName(p.getProfileName());
				profile.setDataAccess(p.getDataAccess());
				profiles.add(profile);
			});
		}
		return profiles;
	}	
	
}