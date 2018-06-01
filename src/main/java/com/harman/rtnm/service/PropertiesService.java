package com.harman.rtnm.service;

import java.util.List;

import com.harman.rtnm.model.CounterGroup;
import com.harman.rtnm.model.Properties;

public interface PropertiesService {

	List<CounterGroup> getPropertiesByCounterGroupIds(List<CounterGroup> counterGroupList) throws Exception;

	List<String> propertiesValueByCounter(List<String> counters) throws Exception;

	List<Properties> propertyDetailsByCounterGroup(List<CounterGroup> counterGroup) throws Exception;
}
