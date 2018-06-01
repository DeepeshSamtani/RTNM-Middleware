package com.harman.rtnm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.harman.rtnm.dao.TaskStatusDao;
import com.harman.rtnm.model.TaskStatus;
import com.harman.rtnm.service.TaskStatusService;

@Service
public class TaskStatusServiceImpl implements TaskStatusService {

	@Autowired
	TaskStatusDao taskStatusDao;
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public void createTaskStatus(TaskStatus taskStatus) throws Exception {
		taskStatusDao.createTaskStatus(taskStatus);

	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public void updateTaskStatus(TaskStatus taskStatus) {
		taskStatusDao.updateTaskStatus(taskStatus);

	}

}
