package com.harman.rtnm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.harman.rtnm.model.CounterIdRange;
import com.harman.rtnm.model.KPI;
import com.harman.rtnm.service.UtilService;
import com.sun.mail.iap.Response;

@RequestMapping(value = "/util")
@RestController
public class UtilController {

	@Autowired
	private UtilService utilService;
	
	@RequestMapping(value = "/deviceexist", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity deviceExist(@RequestBody CounterIdRange counterIdRange) throws Exception{
		return new ResponseEntity(utilService.deviceExist(counterIdRange.getDeviceType()),HttpStatus.OK);
	}
}
