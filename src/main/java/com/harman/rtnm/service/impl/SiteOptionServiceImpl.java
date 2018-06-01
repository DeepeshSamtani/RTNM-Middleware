package com.harman.rtnm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.harman.rtnm.dao.SiteOptionDao;
import com.harman.rtnm.model.common.SiteOption;
import com.harman.rtnm.service.SiteOptionService;




@Service
public class SiteOptionServiceImpl implements SiteOptionService {
	
	@Autowired
	SiteOptionDao siteOptionDao;

	@Transactional(propagation = Propagation.REQUIRED,readOnly=true)
	public List<SiteOption> loadSiteOption() {
		List<SiteOption> siteOptionData=siteOptionDao.loadSiteOption();
		return siteOptionData;
	}

}
