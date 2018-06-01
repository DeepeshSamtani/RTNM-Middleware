package com.harman.rtnm.service;

import com.harman.rtnm.model.TaskStatus;

public interface  TaskStatusService {
	
	void createTaskStatus(TaskStatus taskStatus) throws Exception;
	void updateTaskStatus(TaskStatus taskStatus);



}
