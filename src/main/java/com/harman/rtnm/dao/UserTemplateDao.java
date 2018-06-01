package com.harman.rtnm.dao;

import java.util.List;

import com.harman.rtnm.model.UserTemplate;
import com.harman.rtnm.vo.ReportDetailVO;

public interface UserTemplateDao {

	public void saveUserTemplate(UserTemplate userTemplate);
	
	UserTemplate searchUserTemplate(ReportDetailVO reportDetailVO,int userId) throws Exception;
	
	List<UserTemplate> getUserTemplateByUserId(int userId) throws Exception;
	
	int updatUserTemplate(UserTemplate userTemplate) throws Exception;
	
	void deleteUserTemplate(UserTemplate userTemplate) throws Exception;
	
	int getUserTemplateCountByUserId(int userId) throws Exception;
}
