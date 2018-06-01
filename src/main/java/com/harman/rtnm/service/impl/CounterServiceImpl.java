package com.harman.rtnm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.harman.dyns.model.common.Metric;
import com.harman.rtnm.dao.CounterDao;
import com.harman.rtnm.dao.TableIdTestDao;
import com.harman.rtnm.model.Counter;
import com.harman.rtnm.model.CounterKey;
import com.harman.rtnm.service.CounterService;
import com.harman.rtnm.vo.DynamicCounterVO;
import com.harman.rtnm.vo.InventoryDetailVO;

@Service
@Transactional
public class CounterServiceImpl implements CounterService {

	@Autowired
	CounterDao counterDao;

	@Autowired
	TableIdTestDao tableIdTestDao;

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Counter getCounterById(CounterKey counterId)throws Exception {
		return counterDao.getCounterByID(counterId);
	}

	/*@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<Counter> getCountersFromIDs(List<Metric> counters) throws Exception{
		return counterDao.getCountersFromIDs(counters);
	}*/

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<Counter> getCounterList(List<String> counterList) throws Exception {
		return counterDao.getCounterListByIDs(counterList);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public String saveDynamicCounter(InventoryDetailVO inivetoryDetailVO) throws Exception {

		//return counterDao.saveCounter(inivetoryDetailVO);

		return tableIdTestDao.maxId(inivetoryDetailVO.getDeviceType());


	}

	
	@Override
	@Transactional
	public Counter addCounter(Counter counter) 
			throws Exception {
		return counterDao.addCounter(counter);
	}

	@Override
	@Transactional
	public boolean ifCounterExistsByDisplayName(String counterNameDisplay)
			throws Exception {
		return counterDao.ifCounterExistsByDisplayName(counterNameDisplay);
	}

	@Override
	@Transactional
	public Counter getCounterByDisplayName(String counterNameDisplay) throws Exception {

		return counterDao.getCounterByDisplayName(counterNameDisplay);
	}

	@Override
	@Transactional
	public List<Counter> getCountersDetail(List<Counter> counter)
			throws Exception {

		return counterDao.getCountersDetail(counter);
	}

	@Override
	@Transactional
	public String updateCounter(Counter counter) throws Exception {		
		return counterDao.updateCounter(counter);
	}

	@Override
	@Transactional
	public List<Counter> getCountersFromIDs(List<Metric> counters)
			throws Exception {
		
		return null;
	}

	@Override
	@Transactional
	public String getNextCounterIDByGroupID(String counterGroupId) {
		return counterDao.getNextCounterIDByGroupID(counterGroupId);
	}

	@Override
	@Transactional
	public String getMaxCounterIdByGroupId(String counterGroupId) {
		return counterDao.getMaxCounterIdByGroupId(counterGroupId);
	}

	@Override
	@Transactional
	public String getMaxCounterIdByDeviceType(String deviceType) {
		return counterDao.getMaxCounterIdByDeviceType(deviceType);
	}

	@Override
	@Transactional
	public String getNextCounterIdByDeviceType(String deviceType) {
		return counterDao.getNextCounterIdByDeviceType(deviceType);
	}

	@Override
	@Transactional
	public List<DynamicCounterVO> getAllDynamicKPIs() throws Exception {
		return counterDao.getAllDynamicKPIs();
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public DynamicCounterVO getKPIDetails(CounterKey kpiId) throws Exception {			
		return counterDao.getKPIDetails(kpiId);
	}
	
}
