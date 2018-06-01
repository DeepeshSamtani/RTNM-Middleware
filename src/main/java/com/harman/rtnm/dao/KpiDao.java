package com.harman.rtnm.dao;

import java.util.List;

import com.harman.dyns.model.common.Metric;
import com.harman.rtnm.model.Aggregation;
import com.harman.rtnm.model.KPI;

public interface KpiDao {

	public void addKpi(KPI kpi)  ;
	public List<KPI> getKpiList(String deviceType);
	public int findKpiByFormula(String currentFormula);
	public int findKpiByName(String name);
	List<KPI> getKpiDetails(List<Metric> kpiMetric) throws Exception;
//	public void updateKpi(KPI kpi);
	public List<KPI>  getKpiById(int id);
	public void updateKpi(KPI kpi);
	public Aggregation getAggregationIdByAggrType(String type);
	public List<KPI> getActiveKpiByDeviceType(String deviceType) throws Exception;
	
}
