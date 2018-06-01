package com.harman.rtnm.service;

import java.util.List;

import com.harman.rtnm.model.Profile;
import com.harman.rtnm.model.UserDetail;
import com.harman.rtnm.model.response.LoginResponse;
import com.harman.rtnm.vo.UserDetailVO;

public interface UserDetailService {

	public LoginResponse getUserDataWithProfile(UserDetailVO userDetailVO)throws Exception;
	
	public UserDetail loadUserByName(String userName)throws Exception;

	public List<Profile> getProfilesByUserName(String userName) throws Exception ;

}
