package com.harman.rtnm.dao;

import java.util.List;

import com.harman.rtnm.model.CounterGroup;
import com.harman.rtnm.model.Element;

public interface CounterGroupDao {

	List<CounterGroup> getCountersByCounterGroupId(List<CounterGroup> counterGroupList, String counterType)throws Exception;
	
	List<CounterGroup> getCounterGroupListByElementId(List<Element> element)throws Exception;
	
	List<CounterGroup> getCounterGroupListByIds(List<String> counterGroup)throws Exception;
	
	List<CounterGroup> getCounterbyDevicetype(String deviceType)throws Exception;

	List<CounterGroup> getCounterGroupsByDeviceType(String deviceType)throws Exception;
	
	List<CounterGroup> getCountersAndKpisByCounterGroupId(List<CounterGroup> counterGroupList)throws Exception;
	
	CounterGroup getCounterGroupById(String groupId) throws Exception;
	
}
