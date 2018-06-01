package com.harman.rtnm.dao.impl;

import org.springframework.stereotype.Repository;

import com.harman.rtnm.dao.AbstractDAO;
import com.harman.rtnm.dao.StorageDetailsDao;
import com.harman.rtnm.model.StorageDetails;

@Repository
public class StorageDetailsDaoImpl extends AbstractDAO<StorageDetails> implements StorageDetailsDao {

	@Override
	public void saveStorageDetails(StorageDetails storageDetails) {
		save(storageDetails);

	}

}
