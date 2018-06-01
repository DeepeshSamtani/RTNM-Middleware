package com.harman.rtnm.dao;

import com.harman.rtnm.model.UserDetail;

public interface UserDetailDao {

	public String checkUser(String userName)throws Exception;
	
	public UserDetail loadUserByName(String userName)throws Exception;

}
