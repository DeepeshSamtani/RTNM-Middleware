package com.harman.rtnm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.harman.rtnm.dao.PropertiesDao;
import com.harman.rtnm.model.CounterGroup;
import com.harman.rtnm.model.Properties;
import com.harman.rtnm.service.PropertiesService;


@Service
@Transactional
public class PropertiesServiceImpl implements PropertiesService{

	@Autowired
	PropertiesDao propertiesDao;
	
	
	@Override
	public List<CounterGroup> getPropertiesByCounterGroupIds(List<CounterGroup> counterGroupList) throws Exception {
		return propertiesDao.getPropertiesByCounterGroupIds(counterGroupList);
	}


	@Override
	public List<String> propertiesValueByCounter(List<String> counters) throws Exception {
		return propertiesDao.propertiesValueByCounter(counters);
	}
	
	@Override
	public List<Properties> propertyDetailsByCounterGroup(List<CounterGroup> counterGroup) throws Exception{
		return propertiesDao.propertyDetailsByCounterGroup(counterGroup);
	}

}
