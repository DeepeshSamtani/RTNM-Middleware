package com.harman.rtnm.dao;

import java.util.List;

import com.harman.rtnm.model.Task;
import com.harman.rtnm.model.UserDetail;

public interface TaskDao {
	void createTask(Task task);
	void updateTask(Task task);
	List<Task> getTaskDetails(Long seconds);
	List<Task> getTasksByUser(UserDetail userDetails);
	int deleteTask(int taskId);
	
}
