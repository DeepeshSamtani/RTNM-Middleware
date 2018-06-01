package com.harman.rtnm.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.harman.rtnm.dao.AbstractDAO;
import com.harman.rtnm.dao.DashboardTemplateDao;
import com.harman.rtnm.model.DashboardTemplate;
import com.harman.rtnm.model.SubDashboard;
import com.harman.rtnm.vo.DashboardDetailVO;

@Repository
public class DashboardTemplateDaoImpl extends AbstractDAO<DashboardTemplate> implements DashboardTemplateDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public List<DashboardTemplate> getDashboardTemplates(DashboardDetailVO dashboardDetailVO) throws Exception {
		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("from DashboardTemplate dt ");
		StringBuilder whereQuery = new StringBuilder();
		whereQuery.append(" where ");
		if (!dashboardDetailVO.getDashboardId().isEmpty()) {
			whereQuery.append(" dt.dashboardTemplateKey.subDashboards.subDashboardKey.dashboardId='")
					.append(dashboardDetailVO.getDashboardId() + "'");
		} else {
			throw new Exception("Dashboard ID is required. ");
		}
		if (!StringUtils.isEmpty(dashboardDetailVO.getSubDashboardId())) {
			whereQuery.append(" and dt.dashboardTemplateKey.subDashboards.subDashboardKey.subDashboardId='")
					.append(dashboardDetailVO.getSubDashboardId() + "'");
		} else {
			throw new Exception("SubDashboard ID is required. ");
		}
		selectQuery.append(whereQuery);
		return executeQuery(selectQuery.toString());
	}

	@Override
	public DashboardTemplate searchDashboardTemplate(DashboardDetailVO dashboardDetailVO) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("from DashboardTemplate dt ");
		StringBuilder whereQuery = new StringBuilder();
		whereQuery.append("where ");
		if(null != dashboardDetailVO && !dashboardDetailVO.getDashboardTemplateId().isEmpty())
		{
			whereQuery.append(" dt.dashboardtemplateId='")
   		.append(dashboardDetailVO.getDashboardTemplateId() + "'");
		}
		else{
			throw new Exception ("  Please provide TemplateId .");
		}
		selectQuery.append(whereQuery);
		return executeQueryForUniqueRecord(selectQuery.toString());
	}
	
	public void deleteDashboardTemplates(List<String> ids) throws Exception{
		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("from DashboardTemplate dt ");
		StringBuilder whereQuery = new StringBuilder();
		whereQuery.append("where ");
		whereQuery.append("dt.dashboardtemplateId in ( ");
		for (int i = 0; i < ids.size(); i++) {

			whereQuery.append(" '" + ids.get(i) + "'");
			if (i != (ids.size() - 1)) {
				whereQuery.append(",");
			}

		}
		whereQuery.append(") ");
		selectQuery.append(whereQuery);
		List<DashboardTemplate> subdashList = executeQuery(selectQuery.toString());

		deleteList(subdashList);
	}
	
	public void updateDashboardTemplate(DashboardTemplate dashboardTemplate) throws Exception{
		
		update(dashboardTemplate);
		
	}

}
