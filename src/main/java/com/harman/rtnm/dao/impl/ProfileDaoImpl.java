package com.harman.rtnm.dao.impl;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import com.harman.rtnm.dao.AbstractDAO;
import com.harman.rtnm.dao.ProfileDao;
import com.harman.rtnm.model.Profile;

@Repository
public class ProfileDaoImpl extends AbstractDAO<Profile> implements ProfileDao{

	@Override
	@Transactional
	public Profile profileDetail(Long profileId)throws Exception{
		if(null!=profileId){
			String query = "from Profile p where p.profileId=" + profileId ;
			Profile profile = (Profile)executeQueryForUniqueRecord(query);
			//Hibernate.initialize(profile.getDashboards());
			return profile;
		}else
			throw new Exception("Bad Request : ProfileId can not be NULL.");		
	}
	
}
