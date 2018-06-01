package com.harman.rtnm.vo;

import java.io.Serializable;
import java.util.List;

import com.harman.rtnm.model.Profile;

public class UserDetailVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1208416632245909411L;

	private int userId;
	private String email;
	private String userName;
	private String mobile;
	private List<Profile> profiles;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public List<Profile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}

}
