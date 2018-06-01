package com.harman.rtnm.dao;

import java.util.List;
import java.util.Set;

import com.harman.dyns.model.common.Metric;
import com.harman.rtnm.model.Counter;
import com.harman.rtnm.model.CounterKey;
import com.harman.rtnm.vo.DynamicCounterVO;
import com.harman.rtnm.vo.InventoryDetailVO;

/**
 * 
 * The Data Access Object designed for perfroming the 
 * the DML operation on the Counter table, which is the
 * part of meta data.
 *
 */
public interface CounterDao {

	/**
	 * TODO: Add comments
	 * @param counters
	 * @return
	 * @throws Exception
	 */
	List<Counter> getCountersFromIDs(List<Metric> counters) throws Exception;

	/**
	 * TODO: Add comments
	 * @param counterId
	 * @return
	 * @throws Exception
	 */
	Counter getCounterByID(CounterKey counterId) throws Exception;

	/**
	 * TODO: Add comments
	 * @param counterList
	 * @return
	 * @throws Exception
	 */
	public List<Counter> getCounterListByIDs(List<String> counterList) throws Exception;

	/**
	 * TODO: add comments
	 * @param inventoryDetailVO
	 * @return
	 * @throws Exception
	 */
	public String saveCounter(InventoryDetailVO inventoryDetailVO) throws Exception;
	
	/**
	 * TODO: add comments
	 * @param counter
	 * @return
	 * @throws Exception
	 */
	List<Counter> getCountersDetail(List<Counter> counter) throws Exception;
	
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
	Counter getCounterByDisplayName(String counterNameDisplay) throws Exception;
	
	/**
	 * Add or insert a new {@link Counter} record into the counter table
	 * @param counter details to be inserted into the counter table.
	 */
	Counter addCounter(Counter counter);
	
	/**
	 * 
	 * @param counter
	 * @return
	 * @throws Exception
	 */
	public String updateCounter(Counter counter) throws Exception;
	
	/**
	 * 
	 * @param counterGroupId
	 * @return
	 */
	public String getMaxCounterIdByGroupId(String counterGroupId);
	
	/**
	 * 
	 * @param deviceType
	 * @return
	 */
	public String getMaxCounterIdByDeviceType(String deviceType);
	
	/**
	 * 
	 * @param counterGroupId
	 * @return
	 */
	public String getNextCounterIDByGroupID(String counterGroupId);	
	
	/**
	 * 
	 * @param deviceType
	 * @return
	 */
	public String getNextCounterIdByDeviceType(String deviceType);

	/**
	 * 
	 * @return
	 */
	public List<DynamicCounterVO> getAllDynamicKPIs() throws Exception;
	
	/**
	 * 
	 * @param kpiId
	 * @return
	 * @throws Exception
	 */
	public DynamicCounterVO getKPIDetails(CounterKey kpiId) throws Exception;

}
