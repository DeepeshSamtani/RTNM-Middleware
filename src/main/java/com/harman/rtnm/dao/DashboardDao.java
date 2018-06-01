package com.harman.rtnm.dao;

import java.util.List;
import java.util.Map;

import com.harman.rtnm.model.Dashboard;
import com.harman.rtnm.vo.DashboardVO;

public interface DashboardDao {
	Dashboard getDashboard(String dashboardId) throws Exception;
	void addDashboard(Dashboard dashboard) throws Exception;
	void updateDashboard(Dashboard dashboard ,	List<String> subdashboardIdsTodelete ,List<String> templateIdsTodelete) throws Exception;
	void deleteDashboard(DashboardVO dashboardVO) throws Exception;
	Map<Long,Integer> getDashboardCountForProfiles(List<Long> profileIds) throws Exception;

}
