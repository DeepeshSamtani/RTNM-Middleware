package com.harman.rtnm.dao;

import java.util.List;

import com.harman.rtnm.model.CounterGroup;
import com.harman.rtnm.model.Properties;

public interface PropertiesDao {
	
	List<CounterGroup> getPropertiesByCounterGroupIds(List<CounterGroup> counterGroupList)throws Exception;

	List<String> propertiesValueByCounter(List<String> counters)throws Exception;
	
	List<Properties> propertyDetailsByCounterGroup(List<CounterGroup> counterGroup) throws Exception ;
}
