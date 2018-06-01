package com.harman.rtnm.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.harman.rtnm.dao.AbstractDAO;
import com.harman.rtnm.dao.DashboardDao;
import com.harman.rtnm.dao.DashboardTemplateDao;
import com.harman.rtnm.dao.SubDashboardDao;
import com.harman.rtnm.model.Dashboard;
import com.harman.rtnm.vo.DashboardVO;

@Repository
public class DashboardDaoImpl extends AbstractDAO<Dashboard> implements DashboardDao {
	@Autowired
	SubDashboardDao subDashboardDao;
	
	@Autowired
	DashboardTemplateDao dashboardTemplateDao;

	private static final long serialVersionUID = 1L;

	public Dashboard getDashboard(String dashboardId) throws Exception {
		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("from Dashboard db ");
		StringBuilder whereQuery = new StringBuilder();
		if (null != dashboardId) {
			whereQuery.append("where ");
			whereQuery.append("db.dashboardId='").append(dashboardId + "'");

		} else {
			throw new Exception("Dashboard Id can not be Null");
		}
		selectQuery.append(whereQuery);
		Dashboard dash = executeQueryForUniqueRecord(selectQuery.toString());
		Hibernate.initialize(dash.getProfiles());
		return dash;	
	}
	
	@Transactional
	public void addDashboard(Dashboard dashboard) throws Exception {
		save(dashboard);
		
	}
	@Transactional
	public void updateDashboard(Dashboard dashboard ,List<String> subdashboardIdsTodelete , List<String> templateIdsTodelete) throws Exception{
				update(dashboard);
				if(null != subdashboardIdsTodelete && !subdashboardIdsTodelete.isEmpty())
				  { subDashboardDao.deleteSubdashboards(subdashboardIdsTodelete);}
				if( null != templateIdsTodelete && !templateIdsTodelete.isEmpty())
				{
					dashboardTemplateDao.deleteDashboardTemplates(templateIdsTodelete);
				}
				
			}
	
	@Transactional
	public void deleteDashboard(DashboardVO dashboardVO) throws Exception{
		Dashboard dashboard = getRecordById(Dashboard.class , dashboardVO.getId());
		delete(dashboard);
	}

	@Override
	public Map<Long, Integer> getDashboardCountForProfiles(List<Long> profileIds) throws Exception {
		HashMap<Long, Integer> dashCountMap = new HashMap<>();
		if (null != profileIds) {
			profileIds.forEach(id -> {

				StringBuilder selectQuery = new StringBuilder();
				selectQuery.append("Select count(*) from PROFILE_DASHBOARD dp");
				StringBuilder whereQuery = new StringBuilder();
				if (null != id) {
					whereQuery.append(" where ");
					whereQuery.append("dp.PROFILE_ID='").append(id + "'");

				}
				selectQuery.append(whereQuery);
				Long count = count(selectQuery.toString());
				dashCountMap.put(id, count.intValue());
			});

		}
		return dashCountMap;
	}
}
