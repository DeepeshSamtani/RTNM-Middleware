package com.harman.rtnm.service;

import java.util.List;

import com.harman.rtnm.model.CounterGroup;
import com.harman.rtnm.vo.InventoryDetailVO;

public interface CounterGroupService {	

	List<CounterGroup> getCountersByCounterGroupIds(List<CounterGroup> counterGroupList, InventoryDetailVO inventoryDetailVO)throws Exception;
	
	List<CounterGroup> getCounterByDeviceType(String deviceType)throws Exception;

	List<CounterGroup> getCounterGroupListByIds(List<String> counterGroup)throws Exception;

	List<CounterGroup> getCounterGroupsByDeviceType(String deviceType) throws Exception;
	
	List<CounterGroup> getCounterKpiPropertyWithCounterGroup() throws Exception;
	
	CounterGroup getCounterGroupById(String counterGroupId) throws Exception;
}
