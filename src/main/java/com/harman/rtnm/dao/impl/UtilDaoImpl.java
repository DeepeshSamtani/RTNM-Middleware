package com.harman.rtnm.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.harman.rtnm.dao.UtilDao;
import com.harman.rtnm.model.CounterIdRange;

@Repository
public class UtilDaoImpl implements UtilDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public boolean deviceExist(String deviceType) {
		CounterIdRange counterIdRange = new CounterIdRange();
		Criteria cr = sessionFactory.getCurrentSession().createCriteria(CounterIdRange.class);
		cr.add(Restrictions.eqOrIsNull("deviceType", deviceType));
		if(cr.list().isEmpty())
			return true;
		else
			return false;
	}

}
