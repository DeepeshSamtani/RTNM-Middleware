package com.harman.rtnm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.harman.rtnm.dao.StorageDetailsDao;
import com.harman.rtnm.model.StorageDetails;
import com.harman.rtnm.service.StorageDetailsService;

@Service
public class StorageDetailsServiceImpl implements StorageDetailsService {

	@Autowired
	StorageDetailsDao storageDetailsDao;
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public void saveStorageDetails(StorageDetails storageDetails) {
		storageDetailsDao.saveStorageDetails(storageDetails);

	}

}
