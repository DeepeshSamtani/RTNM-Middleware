package com.harman.rtnm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.harman.rtnm.dao.DatasourceDao;
import com.harman.rtnm.model.Datasource;
import com.harman.rtnm.service.DatasourceService;


@Service
@Transactional
public class DatasourceServiceImpl implements DatasourceService{

	@Autowired
	DatasourceDao datasourceDao;
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Datasource getDatasourceByGranularity(String granularity, String deviceType) throws Exception {
		return datasourceDao.getDatasourceByGranularity(granularity, deviceType);
	}

}
