package com.harman.rtnm.service;

import com.harman.rtnm.model.response.DashboardReport;
import com.harman.rtnm.model.response.SubDashboardReport;
import com.harman.rtnm.vo.DashboardDetailVO;

public interface SubDashboardService {
	
	DashboardReport getSubDashboardsForDashboard(String dashboardId) throws Exception;
	SubDashboardReport getSpecificSubDashboard(DashboardDetailVO dashboardDetailVO) throws Exception;

}
