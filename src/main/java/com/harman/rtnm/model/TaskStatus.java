package com.harman.rtnm.model;


import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TASK_STATUS")
public class TaskStatus implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5055985735835889412L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "STATUS_ID")
	private int statusId;

	@Column(name = "TASK_EXECUTION_STATUS")
	private String taskExecutionStatus;
	
	@Column(name = "TASK_EXECUTION_DATE_TIME")
	private String taskExecutionDateTime;
	
	@Column(name = "TASK_PICKED_TIME")
	private String taskPickedTime;
	
	@Column(name = "TASK_END_TIME")
	private String taskEndTime;

	@Column(name = "COMMENTS")
	private String comments;
	
	
	@ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
	@JoinColumn(name = "TASK_ID")
	private Task task;


	public int getStatusId() {
		return statusId;
	}


	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}


	public String getTaskExecutionStatus() {
		return taskExecutionStatus;
	}


	public void setTaskExecutionStatus(String taskExecutionStatus) {
		this.taskExecutionStatus = taskExecutionStatus;
	}


	public String getTaskExecutionDateTime() {
		return taskExecutionDateTime;
	}


	public void setTaskExecutionDateTime(String taskExecutionDateTime) {
		this.taskExecutionDateTime = taskExecutionDateTime;
	}


	public String getTaskPickedTime() {
		return taskPickedTime;
	}


	public void setTaskPickedTime(String taskPickedTime) {
		this.taskPickedTime = taskPickedTime;
	}


	public String getTaskEndTime() {
		return taskEndTime;
	}


	public void setTaskEndTime(String taskEndTime) {
		this.taskEndTime = taskEndTime;
	}


	public String getComments() {
		return comments;
	}


	public void setComments(String comments) {
		this.comments = comments;
	}


	public Task getTask() {
		return task;
	}


	public void setTask(Task task) {
		this.task = task;
	}

	@Override
	public String toString() {
		return "TaskStatus [statusId=" + statusId + ", taskExecutionStatus=" + taskExecutionStatus
				+ ", taskExecutionDateTime=" + taskExecutionDateTime + ", taskPickedTime=" + taskPickedTime
				+ ", taskEndTime=" + taskEndTime + ", comments=" + comments + ", task=" + task + "]";
	}
}