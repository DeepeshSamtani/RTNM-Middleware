package com.harman.rtnm.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.harman.rtnm.dao.AlarmNotificationDao;
import com.harman.rtnm.model.AlarmNotifications;

@Repository
public class AlarmNotificationDaoImpl implements AlarmNotificationDao{


	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void addAlarm(AlarmNotifications alarmNotifications) {
		sessionFactory.getCurrentSession().save(alarmNotifications);
	}

	@Override
	public List<AlarmNotifications> getAlarmList() {
		List<AlarmNotifications> listAlarm =  sessionFactory.getCurrentSession().createQuery("from AlarmNotifications order by severity asc").list();
		if(!listAlarm.isEmpty()) {
			return listAlarm;
		}
		else
			return listAlarm;
		
	}

}
