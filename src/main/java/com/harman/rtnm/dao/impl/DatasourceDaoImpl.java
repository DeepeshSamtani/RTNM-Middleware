package com.harman.rtnm.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.harman.rtnm.dao.AbstractDAO;
import com.harman.rtnm.dao.DatasourceDao;
import com.harman.rtnm.model.Datasource;

@Repository
public class DatasourceDaoImpl  extends AbstractDAO<Datasource> implements DatasourceDao{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7441952573446947470L;
	

	@Override
	public Datasource getDatasourceByGranularity(String granularity, String deviceType) throws Exception {
		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("from Datasource ds ");
		StringBuilder whereQuery = new StringBuilder();
		if (null != granularity) {
			whereQuery.append("where ");
			if (!StringUtils.isEmpty(granularity)) {
				whereQuery.append("ds.granularity='").append(granularity+ "'");
				whereQuery.append(" and ");
				whereQuery.append("ds.deviceType='").append(deviceType+ "'");
			}else {
				throw new Exception("Granularity can not be Null");
			} 
		}
		selectQuery.append(whereQuery);
		Datasource datasource=executeQueryForUniqueRecord(selectQuery.toString());
		return datasource;
	}
	
	

}
