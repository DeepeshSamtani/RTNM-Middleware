
package com.harman.rtnm.service;

import java.util.List;

import com.harman.dyns.model.common.Metric;
import com.harman.rtnm.model.Counter;
import com.harman.rtnm.model.CounterKey;
import com.harman.rtnm.vo.DynamicCounterVO;
import com.harman.rtnm.vo.InventoryDetailVO;

public interface CounterService {
	/**
	 * TODO: Add comments
	 * @param counters
	 * @return
	 * @throws Exception
	 */
	List<Counter> getCountersFromIDs(List<Metric> counters)throws Exception;
	
	/**
	 * TODO: add comments
	 * @param counterId
	 * @return
	 * @throws Exception
	 */
	Counter getCounterById(CounterKey counterId) throws Exception;
	
	/**
	 * Check if there is any {@link Counter} stored with the given display name.
	 * @param counterNameDisplay name of the counter shown over GUI.
	 * @return {@link Counter} object.
	 * @throws Exception 
	 */
	boolean ifCounterExistsByDisplayName(String counterNameDisplay) throws Exception;
	
	/**
	 * Get the {@link Counter} from Counter table based on the display name.
	 * @param counterNameDisplay name of the counter shown over GUI.
	 * @return {@link Counter} object.
	 * @throws Exception 
	 */
	Counter getCounterByDisplayName(String counterDisplayName) throws Exception;
	
	/**
	 * TODO: add comments
	 * @param counterList
	 * @return
	 * @throws Exception
	 */
	List<Counter> getCounterList(List<String> counterList) throws Exception ;
		
	/**
	 * TODO: add comments
	 * @param inivetoryDetailVO
	 * @return
	 * @throws Exception
	 */
	String saveDynamicCounter(InventoryDetailVO inivetoryDetailVO) throws Exception ;
	
	/**
	 * 
	 * @param counter
	 * @return
	 * @throws Exception
	 */
	String updateCounter(Counter counter) throws Exception;
	
	/**
	 * Save the {@link Counter} values into Counter Table.
	 * @param counter
	 * @return response description
	 * @throws Exception thrown in case the table is not updated.
	 */
	Counter addCounter(Counter counter) throws Exception;

	/**
	 * TODO: add comments
	 * @param counter
	 * @return
	 * @throws Exception
	 */
	public List<Counter> getCountersDetail(List<Counter> counter) throws Exception;	
	
	/**
	 * 
	 * @param counterGroupId
	 * @return
	 * @throws Exception
	 */
	public String getNextCounterIDByGroupID(String counterGroupId) throws Exception;
	
	/**
	 * 
	 * @param counterGroupId
	 * @return
	 * @throws Exception
	 */
	public String getMaxCounterIdByGroupId(String counterGroupId) throws Exception;
	
	/**
	 * 
	 * @param deviceType
	 * @return
	 * @throws Exception
	 */
	public String getMaxCounterIdByDeviceType(String deviceType) throws Exception;
	
	/**
	 * 
	 * @param deviceType
	 * @return
	 * @throws Exception
	 */
	public String getNextCounterIdByDeviceType(String deviceType) throws Exception;

	/**
	 * To load the list of the {@link Counter}s which are KPIs and 
	 * are enabled. 
	 * @return
	 * @throws Exception
	 */
	public List<DynamicCounterVO> getAllDynamicKPIs() throws Exception;

	
	/**
	 * 
	 */
	public DynamicCounterVO getKPIDetails(CounterKey kpiId) throws Exception;
}
