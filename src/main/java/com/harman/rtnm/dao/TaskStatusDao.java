package com.harman.rtnm.dao;

import com.harman.rtnm.model.TaskStatus;

public interface TaskStatusDao {

	void createTaskStatus(TaskStatus taskStatus);
	void updateTaskStatus(TaskStatus taskStatus);
}
