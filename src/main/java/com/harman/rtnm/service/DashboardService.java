package com.harman.rtnm.service;

import com.harman.rtnm.model.response.DashboardReport;
import com.harman.rtnm.vo.DashboardVO;

public interface DashboardService {
	DashboardReport getDashboardReportWithSubDashboards(String dashboardId) throws Exception;
	DashboardReport addDashboard(DashboardVO dashboardVO) throws Exception;
	DashboardReport updateDashboard(DashboardVO dashboardVO) throws Exception;
	void deleteDashboard(DashboardVO dashboardVO) throws Exception; 
	

}
