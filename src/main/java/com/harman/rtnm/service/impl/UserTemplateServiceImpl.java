package com.harman.rtnm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.harman.rtnm.dao.UserTemplateDao;
import com.harman.rtnm.model.UserDetail;
import com.harman.rtnm.model.UserTemplate;
import com.harman.rtnm.service.UserDetailService;
import com.harman.rtnm.service.UserTemplateService;
import com.harman.rtnm.vo.ReportDetailVO;

@Service
@Transactional
public class UserTemplateServiceImpl implements UserTemplateService {

	@Autowired
	UserTemplateDao userTemplateDao;
	
	@Autowired
	UserDetailService userDetailService;
	
	@Override
	@Transactional	
	public void saveUserTemplate(UserTemplate userTemplate) {
		userTemplateDao.saveUserTemplate(userTemplate);
	}

	@Override
	@Transactional	
	public UserTemplate searchUserTemplate(ReportDetailVO reportDetailVO) throws Exception {
		UserDetail userDetail=null;
		
		if(null!=reportDetailVO.getUserName())
		 userDetail=userDetailService.loadUserByName(reportDetailVO.getUserName());
		else
			throw new Exception("userName can not be Null");
		
		return userTemplateDao.searchUserTemplate(reportDetailVO,userDetail.getUserId());
	}

	@Override
	@Transactional	
	public List<UserTemplate> getUserTemplateByUserId(int userId) throws Exception {
		return userTemplateDao.getUserTemplateByUserId(userId);
	}
	
	@Override
	@Transactional	
	public int updateUserTemplate(UserTemplate userTemplate) throws Exception{
		return userTemplateDao.updatUserTemplate(userTemplate);
	}

	@Override
	@Transactional
	public void deleteUserTemplate(UserTemplate userTemplate) throws Exception {
		// TODO Auto-generated method stub
		 userTemplateDao.deleteUserTemplate(userTemplate);
	}
	
	@Override
	@Transactional
	public int getUserTemplateCountByUserId(int userId) throws Exception{
		return userTemplateDao.getUserTemplateCountByUserId(userId);
	}
	
}