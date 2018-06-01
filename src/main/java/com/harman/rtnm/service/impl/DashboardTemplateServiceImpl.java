package com.harman.rtnm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.harman.rtnm.dao.DashboardTemplateDao;
import com.harman.rtnm.model.DashboardTemplate;
import com.harman.rtnm.service.DashboardTemplateService;
import com.harman.rtnm.vo.DashboardDetailVO;

@Service
public class DashboardTemplateServiceImpl implements DashboardTemplateService {
     
	@Autowired
	DashboardTemplateDao dashboardTemplateDao;
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<DashboardTemplate> getDashboardTemplates(DashboardDetailVO dashboardDetailVO) throws Exception {
		// TODO Auto-generated method stub
		return dashboardTemplateDao.getDashboardTemplates(dashboardDetailVO);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public DashboardTemplate searchDashboardTemplate(DashboardDetailVO dashboardDetailVO) throws Exception {
		// TODO Auto-generated method stub
		return dashboardTemplateDao.searchDashboardTemplate(dashboardDetailVO);
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.SUPPORTS)
	public void updateDashboardTemplate(DashboardTemplate dashboardTemplate) throws Exception{
		dashboardTemplateDao.updateDashboardTemplate(dashboardTemplate);
	}

}
