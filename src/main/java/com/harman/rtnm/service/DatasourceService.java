package com.harman.rtnm.service;

import com.harman.rtnm.model.Datasource;

public interface DatasourceService {
	
	Datasource getDatasourceByGranularity(String granularity,  String deviceType) throws Exception;

}
