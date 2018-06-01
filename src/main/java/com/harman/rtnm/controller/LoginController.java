package com.harman.rtnm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.harman.rtnm.model.Profile;
import com.harman.rtnm.model.UserDetail;
import com.harman.rtnm.model.response.LoginResponse;
import com.harman.rtnm.service.UserDetailService;
import com.harman.rtnm.vo.UserDetailVO;

@RestController
@RequestMapping(value="/api")
public class LoginController {

	@Autowired
	private UserDetailService loginService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Profile>>  login(@RequestBody UserDetail userDetail){
		
		List<Profile> profiles = null;
		try {
			
			profiles = loginService.getProfilesByUserName(userDetail.getUserName());
			if(null!=profiles)
			return new ResponseEntity<List<Profile>>(profiles, HttpStatus.OK);
			else
			return new ResponseEntity<List<Profile>>(profiles, HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			return new ResponseEntity<List<Profile>>(profiles, HttpStatus.UNAUTHORIZED);
		}
		
	}
	
	@RequestMapping(value = "/afterLogedIn", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public  ResponseEntity<LoginResponse> afterLogedIn(@RequestBody UserDetailVO UserDetailVO){
		LoginResponse loginResponse=null;
		try {
			
			loginResponse=loginService.getUserDataWithProfile(UserDetailVO);
			if(null!=loginResponse)
			return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
			else
			return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.UNAUTHORIZED);
		}
	}
	
}
