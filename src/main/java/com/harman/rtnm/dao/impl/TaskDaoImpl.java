package com.harman.rtnm.dao.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Repository;

import com.harman.rtnm.dao.AbstractDAO;
import com.harman.rtnm.dao.TaskDao;
import com.harman.rtnm.model.Task;
import com.harman.rtnm.model.UserDetail;

@Repository
public class TaskDaoImpl extends AbstractDAO<Task> implements  TaskDao {

	private static final long serialVersionUID = 1L;

	@Override
	public void createTask(Task task) {
		save(task);

	}
	
	@Override
	public void updateTask(Task task) {
		update(task);
	}
	
	@Override
	public List<Task> getTasksByUser(UserDetail userDetail) {
		String query = "from Task t where t.userDetail.userId = " + userDetail.getUserId();
		return executeQuery(query);
	}

	
	@Override
	public List<Task> getTaskDetails(Long seconds) {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String date = dateFormat.format(new Date());
		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		String startTime = timeFormat.format(new Date());
		String endTime = timeFormat.format(new Date().getTime() + TimeUnit.SECONDS.toMillis(seconds));
		String query = "From Task where  taskExecutionDate = " + "'" + date + "'"
				+ " AND taskExecutionTime >= " + "'" + startTime + "'" + " AND taskExecutionTime <= " + "'" + endTime
				+ "' order by taskExecutionTime";

		System.out.println(query);
		return executeQuery(query);
	}

	@Override
	public int deleteTask(int taskId){
		String query = "delete  from Task t where t.taskId= " +taskId;
		return executeUpdate(query);
		
	}

}
