package com.harman.rtnm.service;

import java.util.List;

import com.harman.rtnm.model.DashboardTemplate;
import com.harman.rtnm.vo.DashboardDetailVO;

public interface DashboardTemplateService {

	List<DashboardTemplate> getDashboardTemplates(DashboardDetailVO dashboardDetailVO) throws Exception;
	DashboardTemplate searchDashboardTemplate(DashboardDetailVO dashboardDetailVO) throws Exception;
	void updateDashboardTemplate(DashboardTemplate dashboardTemplate) throws Exception;
	
}
