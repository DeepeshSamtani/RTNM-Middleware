package com.harman.rtnm.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.harman.dyns.model.common.Metric;
import com.harman.rtnm.dao.AbstractDAO;
import com.harman.rtnm.dao.KpiDao;
import com.harman.rtnm.model.Aggregation;
import com.harman.rtnm.model.Counter;
import com.harman.rtnm.model.KPI;

@Repository
public class KpiDaoImpl extends AbstractDAO<KPI> implements KpiDao {

	@Autowired
	private SessionFactory sessionFactory;

	private static final Logger logger = LoggerFactory.getLogger(KpiDaoImpl.class);
	/**
	 * 
	 */


	@Override
	public void addKpi(KPI kpi) {
		try {
			save(kpi);
			logger.debug("KPI Saved successfully : " + kpi);
		} catch (ConstraintViolationException  e) {
			e.printStackTrace();
		}
	}


	
	@Override
	public List<KPI> getKpiList(String deviceType) {
				
		if("ALL".equals(deviceType)){
			Criteria cr = sessionFactory.getCurrentSession().createCriteria(KPI.class);	
			KPI kpi = new KPI();
			List<KPI> listKpi = (List<KPI>) cr.list();
			List<KPI> kpilistMain= new ArrayList<KPI>();

			StringBuffer sb = new StringBuffer();
			int i=0;
			for(KPI k: listKpi){
				Criteria cr1 = sessionFactory.getCurrentSession().createCriteria(KPI.class);
				cr1.add(Restrictions.eq("id", k.getId()));
				cr1.setProjection(Projections.distinct(Projections.property("counterList")));

				List<String> listObj = cr1.list();
				StringBuffer sbCounterList = new StringBuffer();
				for(String str: listObj){
					sbCounterList.append(str);
				}
				
				String str = sbCounterList.toString();
				List<Counter> crCounterId = new ArrayList<Counter>();
				List<Counter> results=new ArrayList<Counter>();
				List<String> myList = new ArrayList<String>(Arrays.asList(str.split(",")));
				
				for(String counterId : myList){
					
					String hql = "FROM Counter WHERE id = :id";
					Query query = sessionFactory.getCurrentSession().createQuery(hql);
					query.setParameter("id", (counterId));	
					results = (List<Counter>)query.list();
					if(!results.isEmpty())
						crCounterId.add(results.get(0));
				}
				List<KPI> kpilist=(List<KPI>)cr.list();
				kpilist.get(i).setCounterConfig(crCounterId);
				kpilistMain.add(kpilist.get(i));
				i++;
			}
			
			return kpilistMain;
			
			
		}
		else{
			
			Criteria cr = sessionFactory.getCurrentSession().createCriteria(KPI.class);	
			cr.add(Restrictions.eq("deviceType", deviceType));
			KPI kpi = new KPI();
			List<KPI> listKpi = (List<KPI>) cr.list();
			List<KPI> kpilistMain= new ArrayList<KPI>();

			StringBuffer sb = new StringBuffer();
			int i=0;
			for(KPI k: listKpi){
				Criteria cr1 = sessionFactory.getCurrentSession().createCriteria(KPI.class);
				cr1.add(Restrictions.eq("id", k.getId()));
				cr1.setProjection(Projections.distinct(Projections.property("counterList")));

				List<String> listObj = cr1.list();
				StringBuffer sbCounterList = new StringBuffer();
				for(String str: listObj){
					sbCounterList.append(str);
				}
				
				String str = sbCounterList.toString();
				List<Counter> crCounterId = new ArrayList<Counter>();
				List<Counter> results=new ArrayList<Counter>();
				List<String> myList = new ArrayList<String>(Arrays.asList(str.split(",")));
				
				for(String counterId : myList){
					
					String hql = "FROM Counter WHERE id = :id";
					Query query = sessionFactory.getCurrentSession().createQuery(hql);
					query.setParameter("id", (counterId));	
					results = (List<Counter>)query.list();
					crCounterId.add(results.get(0));
				}
				List<KPI> kpilist=(List<KPI>)cr.list();
				kpilist.get(i).setCounterConfig(crCounterId);
				kpilistMain.add(kpilist.get(i));
				i++;
			}
			
			return kpilistMain;
			
			
		}
	}
	
	@Override
	public List<KPI> getKpiDetails(List<Metric> kpiMetric) throws Exception {
		List<KPI> kpiList = null;
		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("from KPI k ");

		StringBuilder whereQuery = new StringBuilder();
		whereQuery.append("where k.id in ('");
		for (int i = 0; i < kpiMetric.size(); i++) {
			if (null != kpiMetric.get(i).getId() && !kpiMetric.get(i).getId().isEmpty())
				if (i == (kpiMetric.size() - 1))
					whereQuery.append(kpiMetric.get(i).getId() + "')");
				else
					whereQuery.append(kpiMetric.get(i).getId() + "','");
			else
				throw new Exception(" KPI name can not be Null ");
		}
		selectQuery.append(whereQuery);
		kpiList = (List<KPI>) executeQuery(selectQuery.toString());

		return kpiList;
	}
	

	@Override
	public int findKpiByFormula(String currentFormula) {
		Criteria cr1 = sessionFactory.getCurrentSession().createCriteria(KPI.class);	
		cr1.add(Restrictions.eq("formula", currentFormula));
		if(!cr1.list().isEmpty()){
			return 1;
		}
		else{
			return 0;
		}
	}

	@Override
	public int findKpiByName(String name) {
		Criteria cr1 = sessionFactory.getCurrentSession().createCriteria(KPI.class);	
		cr1.add(Restrictions.eq("displayName", name));
		if(!cr1.list().isEmpty()){
			return 1;
		}
		else{
			return 0;
		}
	}


	@Override
	public List<KPI> getKpiById(int id) {

		KPI kpi = new KPI();
				
		Criteria cr1 = sessionFactory.getCurrentSession().createCriteria(KPI.class);
		cr1.add(Restrictions.eq("id", id));
		cr1.setProjection(Projections.distinct(Projections.property("counterList")));
		
		List<String> listObj = cr1.list();
		StringBuffer sb = new StringBuffer();
		for(String str: listObj){
		 sb.append(str);
		}
				
		String str = sb.toString();
		List<Counter> crCounterId = new ArrayList<Counter>();
		List<Counter> results=null;
		List<String> myList = new ArrayList<String>(Arrays.asList(str.split(",")));
		for(String counterId : myList){
			
			String hql = "FROM Counter WHERE id = :id";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setParameter("id", counterId);	
			results = (List<Counter>)query.list();
			crCounterId.add(results.get(0));
			
		}

		Criteria cr = sessionFactory.getCurrentSession().createCriteria(KPI.class);	
		cr.add(Restrictions.eq("id", id));
		
		List<KPI> kpilist=(List<KPI>)cr.list();
		kpilist.get(0).setCounterConfig(crCounterId);
		return kpilist;

	}


	@Override
	public void updateKpi(KPI kpi) {
	    sessionFactory.getCurrentSession().update(kpi);
	  }
	


	@Override
	public Aggregation getAggregationIdByAggrType(String type) {
		int aggrId = 0;
		Aggregation aggregation = new Aggregation();
		if ("SUM".toString().equals(type)) {
			aggregation.setDaySum(1);
			aggregation.setMonthSum(1);
			aggregation.setHourSum(1);
			aggregation.setMonthSum(1);
			aggregation.setWeekSum(1);
			aggregation.setYearSum(1);

		}
		if ("MIN".toString().equals(type)) {
			aggregation.setDayMin(1);
			aggregation.setMonthMin(1);
			aggregation.setHourMin(1);
			aggregation.setMonthMin(1);
			aggregation.setWeekMin(1);
			aggregation.setYearMin(1);

		}
		if ("AVG".toString().equals(type)) {
			aggregation.setDayAvg(1);
			aggregation.setMonthAvg(1);
			aggregation.setHourAvg(1);
			aggregation.setMonthAvg(1);
			aggregation.setWeekAvg(1);
			aggregation.setYearAvg(1);

		}
		if ("MAX".toString().equals(type)) {
			aggregation.setDayMax(1);
			aggregation.setMonthMax(1);
			aggregation.setHourMax(1);
			aggregation.setMonthMax(1);
			aggregation.setWeekMax(1);
			aggregation.setYearMax(1);

		}

		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("select agg.id from Aggregation agg ");
		StringBuilder whereQuery = new StringBuilder();
		whereQuery.append(" where agg.hourSum = " + aggregation.getHourSum());
		whereQuery.append(" and  agg.hourMin = " + aggregation.getHourMin());
		whereQuery.append(" and  agg.hourMax = " + aggregation.getHourMax());
		whereQuery.append(" and  agg.hourAvg = " + aggregation.getHourAvg());
		whereQuery.append(" and  agg.hourFormula is " + aggregation.getHourFormula());
		whereQuery.append(" and  agg.daySum = " + aggregation.getDaySum());
		whereQuery.append(" and  agg.dayMin = " + aggregation.getDayMin());
		whereQuery.append(" and  agg.dayMax = " + aggregation.getDayMax());
		whereQuery.append(" and  agg.dayAvg = " + aggregation.getDayAvg());
		whereQuery.append(" and  agg.dayFormula is " + aggregation.getDayFormula());
		whereQuery.append(" and agg.weekSum = " + aggregation.getWeekSum());
		whereQuery.append(" and  agg.weekMin = " + aggregation.getWeekMin());
		whereQuery.append(" and  agg.weekMax = " + aggregation.getWeekMax());
		whereQuery.append(" and  agg.weekAvg = " + aggregation.getWeekAvg());
		whereQuery.append(" and  agg.weekFormula is " + aggregation.getWeekFormula());
		whereQuery.append(" and agg.monthSum = " + aggregation.getMonthSum());
		whereQuery.append(" and  agg.monthMin = " + aggregation.getMonthMin());
		whereQuery.append(" and  agg.monthMax = " + aggregation.getMonthMax());
		whereQuery.append(" and  agg.monthAvg = " + aggregation.getMonthAvg());
		whereQuery.append(" and  agg.monthFormula is " + aggregation.getMonthFormula());
		whereQuery.append(" and agg.yearSum = " + aggregation.getYearSum());
		whereQuery.append(" and  agg.yearMin = " + aggregation.getYearMin());
		whereQuery.append(" and  agg.yearMax = " + aggregation.getYearMax());
		whereQuery.append(" and  agg.yearAvg = " + aggregation.getYearAvg());
		whereQuery.append(" and  agg.yearFormula is " + aggregation.getYearFormula());

		selectQuery.append(whereQuery);
		Query query = sessionFactory.getCurrentSession().createQuery(selectQuery.toString());
		List<Integer> list = (List<Integer>) query.list();
		if (list != null && list.size() > 0) {
			aggrId = list.get(0);
		} 
		aggregation.setId(aggrId);
		return aggregation;
	}

	@Override
	public List<KPI> getActiveKpiByDeviceType(String deviceType) throws Exception{
		/*Criteria criteria = sessionFactory.getCurrentSession().createCriteria(KPI.class);
		criteria.add(Restrictions.eq("isActive", true));		
		criteria.setResultTransformer(Transformers.aliasToBean(KPI.class));
		List<KPI> kpiList = criteria.list();

		System.out.println("size of cg : " + kpiList.size());
		return kpiList;*/
		
		StringBuilder selectQuery = new StringBuilder();
		selectQuery
				.append(" from KPI k ");

		StringBuilder whereQuery = new StringBuilder();
		whereQuery
				.append("where k.isActive = 1 and k.deviceType='"+deviceType+"'");
		selectQuery.append(whereQuery);
		List<KPI> counterGroupList = (List<KPI>) executeQuery(selectQuery.toString());
		
		return counterGroupList;
	}

}
