package com.harman.rtnm.dao;

import java.util.List;

import com.harman.rtnm.model.DashboardTemplate;
import com.harman.rtnm.vo.DashboardDetailVO;

public interface DashboardTemplateDao {

	List<DashboardTemplate> getDashboardTemplates(DashboardDetailVO dashboardDetailVO) throws Exception;
	DashboardTemplate searchDashboardTemplate(DashboardDetailVO dashboardDetailVO) throws Exception;
	void deleteDashboardTemplates(List<String> ids) throws Exception;
	public void updateDashboardTemplate(DashboardTemplate dashboardTemplate) throws Exception;
}
