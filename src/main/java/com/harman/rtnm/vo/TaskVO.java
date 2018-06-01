package com.harman.rtnm.vo;


import java.io.Serializable;


public class TaskVO extends MessageVO implements Serializable {
	/**
	 * 
	 */


	private int taskId;

	private String taskExecutionTime;

	private String taskType;

	private String taskExecutionDate;

	private String taskStatus;

	private String taskName;

	private String userName;

	private String userTemplateId;
	
	private String dailytaskHourfrequency;

	private ReportDetailVO taskParamValue;


	public int getTaskId() {
		return taskId;
	}


	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}


	public String getTaskExecutionTime() {
		return taskExecutionTime;
	}


	public void setTaskExecutionTime(String taskExecutionTime) {
		this.taskExecutionTime = taskExecutionTime;
	}





	public String getTaskType() {
		return taskType;
	}


	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}





	public String getTaskExecutionDate() {
		return taskExecutionDate;
	}


	public void setTaskExecutionDate(String taskExecutionDate) {
		this.taskExecutionDate = taskExecutionDate;
	}


	public String getTaskStatus() {
		return taskStatus;
	}


	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}


	public String getTaskName() {
		return taskName;
	}


	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getUserTemplateId() {
		return userTemplateId;
	}


	public void setUserTemplateId(String userTemplateId) {
		this.userTemplateId = userTemplateId;
	}


	public String getDailytaskHourfrequency() {
		return dailytaskHourfrequency;
	}


	public void setDailytaskHourfrequency(String dailytaskHourfrequency) {
		this.dailytaskHourfrequency = dailytaskHourfrequency;
	}


	public ReportDetailVO getTaskParamValue() {
		return taskParamValue;
	}


	public void setTaskParamValue(ReportDetailVO taskParamValue) {
		this.taskParamValue = taskParamValue;
	}


	@Override
	public String toString() {
		return "TaskVO [taskId=" + taskId + ", taskExecutionTime=" + taskExecutionTime + ", taskType=" + taskType
				+ ", taskExecutionDate=" + taskExecutionDate + ", taskStatus=" + taskStatus + ", taskName=" + taskName
				+ ", userName=" + userName + ", userTemplateId=" + userTemplateId + ", dailytaskHourfrequency="
				+ dailytaskHourfrequency + ", taskParamValue=" + taskParamValue + "]";
	}



}
