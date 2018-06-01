package com.harman.rtnm.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.harman.rtnm.dao.AlarmNotificationDao;
import com.harman.rtnm.model.AlarmNotifications;
import com.harman.rtnm.service.AlarmNotificationService;

@Service
public class AlarmNotificationServiceImpl implements AlarmNotificationService{

	@Autowired
	private AlarmNotificationDao alarmNotificationDao;
	
	@Transactional
	public ResponseEntity<Void> addAlarm(AlarmNotifications alarmNotifications) {
		alarmNotificationDao.addAlarm(alarmNotifications);
		return null;
	}

	@Transactional
	public List<AlarmNotifications> getAlarmList() {
		return alarmNotificationDao.getAlarmList();

	}

}
