package com.harman.rtnm.service;

import com.harman.rtnm.model.response.ProfileResponse;

public interface ProfileService {

	public ProfileResponse profileDetail(Long profileId,  boolean isreportConfigurationNeed)throws Exception;
	
}
