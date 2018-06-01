package com.harman.rtnm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.harman.rtnm.dao.ScheduleTimeMetadataDao;
import com.harman.rtnm.service.ScheduleTimeMetadataService;

@Service
public class ScheduleTimeMetadataServiceImpl implements ScheduleTimeMetadataService {
	
     @Autowired
     ScheduleTimeMetadataDao scheduleTimeMetadataDao;
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public String getScheduleTimebyTaskType(String taskType) {
		return scheduleTimeMetadataDao.getScheduleTimebyTaskType(taskType);
	}

}
