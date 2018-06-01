package com.harman.rtnm.service;

import java.io.IOException;
import java.util.List;

import com.harman.dyns.model.common.Metric;
import com.harman.rtnm.model.Aggregation;
import com.harman.rtnm.model.Counter;
import com.harman.rtnm.model.KPI;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface KpiService {
	
	public void addKpi(Counter kpi) throws JsonParseException, JsonMappingException, IOException;
	
	public List<KPI> getKpiList(String deviceType);
	public int findKpiByFormula(String currentFormula);
	public int findKpiByName(String name);
	
	List<KPI> getKpiDetails(List<Metric> kpiMetric) throws Exception;
	public void updateKpi(KPI kpi) throws JsonProcessingException, IOException;
		
	public List<KPI>  getKpiById(int id);
	
	public Aggregation getAggregationIdByAggrType(String type);
	
	public List<KPI> getActiveKpiByDeviceType(String deviceType) throws Exception;
}
