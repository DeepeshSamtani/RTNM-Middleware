package com.harman.rtnm.dao;

import java.util.List;

import com.harman.rtnm.model.AlarmNotifications;

public interface AlarmNotificationDao {

	public void addAlarm(AlarmNotifications alarmNotifications);
	public List<AlarmNotifications> getAlarmList();

}
