package com.harman.rtnm.service;

import java.util.List;

import com.harman.rtnm.model.UserDetail;
import com.harman.rtnm.model.UserTemplate;
import com.harman.rtnm.vo.ReportDetailVO;

public interface UserTemplateService {

	void saveUserTemplate(UserTemplate userTemplate);
	
	UserTemplate searchUserTemplate(ReportDetailVO reportDetailVO) throws Exception;
	
	List<UserTemplate> getUserTemplateByUserId(int userId) throws Exception ;
	
	int updateUserTemplate(UserTemplate userTemplate) throws Exception;
	
	void deleteUserTemplate(UserTemplate userTemplate) throws Exception;
	
	int getUserTemplateCountByUserId (int userId) throws Exception;
}
