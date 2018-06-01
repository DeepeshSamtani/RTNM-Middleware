package com.harman.rtnm.dao.impl;

import org.springframework.stereotype.Repository;

import com.harman.rtnm.dao.AbstractDAO;
import com.harman.rtnm.dao.TaskStatusDao;
import com.harman.rtnm.model.TaskStatus;

@Repository
public class TaskStatusDaoImpl extends AbstractDAO<TaskStatus> implements TaskStatusDao {

	@Override
	public void createTaskStatus(TaskStatus taskStatus) {
		// TODO Auto-generated method stub
		save(taskStatus);
	}

	@Override
	public void updateTaskStatus(TaskStatus taskStatus) {
		update(taskStatus);

	}

}
