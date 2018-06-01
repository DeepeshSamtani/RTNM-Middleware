package com.harman.rtnm.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.harman.rtnm.dao.AbstractDAO;
import com.harman.rtnm.dao.SiteOptionDao;
import com.harman.rtnm.model.common.SiteOption;


@Repository
public class SiteOptionDaoImpl extends AbstractDAO<SiteOption> implements SiteOptionDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3710925223590239936L;

	public List<SiteOption> loadSiteOption() {
		return (List<SiteOption>) loadClass(SiteOption.class);
	}

}
