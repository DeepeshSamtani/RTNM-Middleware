package com.harman.rtnm.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.harman.rtnm.model.AlarmNotifications;

public interface AlarmNotificationService {

	public ResponseEntity<Void> addAlarm(AlarmNotifications alarmNotifications);
	public List<AlarmNotifications> getAlarmList();

}
