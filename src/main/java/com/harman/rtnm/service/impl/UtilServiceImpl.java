package com.harman.rtnm.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harman.rtnm.dao.UtilDao;
import com.harman.rtnm.service.UtilService;

@Service
public class UtilServiceImpl implements UtilService {

	@Autowired
	private UtilDao utilDao;
	

	@Override
	@Transactional
	public boolean deviceExist(String deviceType) throws Exception {
		return utilDao.deviceExist(deviceType);

	}

}
