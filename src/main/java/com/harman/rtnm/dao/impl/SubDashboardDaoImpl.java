package com.harman.rtnm.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.harman.rtnm.dao.AbstractDAO;
import com.harman.rtnm.dao.SubDashboardDao;
import com.harman.rtnm.model.SubDashboard;
import com.harman.rtnm.vo.DashboardDetailVO;

@Repository
public class SubDashboardDaoImpl extends AbstractDAO<SubDashboard> implements SubDashboardDao {

	public SubDashboard getSpecificSubDashboard(DashboardDetailVO dashboardDetailVO) throws Exception {
		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("from SubDashboard sb ");
		StringBuilder whereQuery = new StringBuilder();
		if (null != dashboardDetailVO.getSubDashboardId()) {
			whereQuery.append("where ");
			whereQuery.append("sb.subDashboardId='")
					.append(dashboardDetailVO.getSubDashboardId() + "'");
		} else {
			throw new Exception("Dashboard Id can not be Null");
		}
		selectQuery.append(whereQuery);

		return executeQueryForUniqueRecord(selectQuery.toString());
	}
	
	public void deleteSubdashboards(List<String> ids) throws Exception
	{
		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("from SubDashboard sb ");
		StringBuilder whereQuery = new StringBuilder();
		whereQuery.append("where ");
		whereQuery.append("sb.subDashboardId in ( ");
		for (int i = 0; i < ids.size(); i++) {

			whereQuery.append(" '" + ids.get(i) + "'");
			if (i != (ids.size() - 1)) {
				whereQuery.append(",");
			}

		}
		whereQuery.append(") ");
		selectQuery.append(whereQuery);
		List<SubDashboard> subdashList = executeQuery(selectQuery.toString());

		deleteList(subdashList);
	}
}
