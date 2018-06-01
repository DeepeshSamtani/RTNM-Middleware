package com.harman.rtnm.dao;

import com.harman.rtnm.model.Datasource;

public interface DatasourceDao {
	
	Datasource getDatasourceByGranularity(String granularity, String deviceType) throws Exception;

}
