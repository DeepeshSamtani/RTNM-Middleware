package com.harman.rtnm.service;

import java.util.List;

import com.harman.rtnm.model.Task;
import com.harman.rtnm.vo.TaskVO;

public interface TaskService {
	
void createTask(TaskVO taskVO) throws Exception;
void updateTask(Task task)throws Exception;
List<TaskVO> getTasksByUser(String userName)throws Exception;
int deleteTask(int taskId)throws Exception;

}
