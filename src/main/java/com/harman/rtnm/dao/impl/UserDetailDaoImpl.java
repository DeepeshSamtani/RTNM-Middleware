package com.harman.rtnm.dao.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.harman.rtnm.dao.AbstractDAO;
import com.harman.rtnm.dao.UserDetailDao;
import com.harman.rtnm.model.UserDetail;

@Repository
public class UserDetailDaoImpl extends AbstractDAO<UserDetail> implements UserDetailDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public String checkUser(String userName) throws Exception{
		Criteria cr = sessionFactory.getCurrentSession().createCriteria(UserDetail.class);
		cr.add(Restrictions.eq("userName",userName));
		cr.setProjection(Projections.rowCount());
		long count = (long) cr.uniqueResult();
		if (count!=0){
			return "Logged in Successfully";
		}
		else{
			return "Wrong Username";
		}
	}
	
	/**
	 * This method is used to fetch the user from the database using userId
	 */
	@Transactional
	@Override
	public UserDetail loadUserByName(String userName) throws Exception{
		String query = "from UserDetail ud where ud.userName='" + userName + "'";
		UserDetail userDetail = (UserDetail)executeQueryForUniqueRecord(query);
		return userDetail;
	}


}
