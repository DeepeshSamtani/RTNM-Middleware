package com.harman.rtnm.dao;

import java.util.List;

import com.harman.rtnm.model.SubDashboard;
import com.harman.rtnm.vo.DashboardDetailVO;

public interface SubDashboardDao {

	 SubDashboard getSpecificSubDashboard(DashboardDetailVO dashboardDetailVO) throws Exception;
	 void deleteSubdashboards(List<String> ids) throws Exception;
}
