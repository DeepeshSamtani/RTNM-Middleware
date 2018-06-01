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

import com.harman.rtnm.model.AlarmNotifications;
import com.harman.rtnm.service.AlarmNotificationService;

@RequestMapping(value = "/alarm")
@RestController
public class AlarmNotificationController {

	@Autowired
	private AlarmNotificationService alarmNotificationService;

	@RequestMapping(value = "/addalarm", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> addAlarm(@RequestBody AlarmNotifications alarmNotifications) {
		alarmNotificationService.addAlarm(alarmNotifications);
        return new ResponseEntity<Void>(HttpStatus.OK);

	}

	@RequestMapping(value = "/alarmlist", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AlarmNotifications>> getAlarmList() {
		List<AlarmNotifications> listOfAlarms = null;
			listOfAlarms = alarmNotificationService.getAlarmList();
			if (listOfAlarms.isEmpty()) 
				return new ResponseEntity<List<AlarmNotifications>>(HttpStatus.OK);	
			else
				return new ResponseEntity<List<AlarmNotifications>>(listOfAlarms, HttpStatus.OK);

	}

}
