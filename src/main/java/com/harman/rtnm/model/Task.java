package com.harman.rtnm.model;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TASK")
public class Task implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int taskId;

	@Column(name = "TASK_NAME")
	private String taskName;

	@Column(name = "TASK_STATUS")
	private String taskStatus;

	@NotNull
	@Column(name = "TASK_EXECUTIONTIME", nullable = false)
	private String taskExecutionTime;

	@NotNull
	@Column(name = "TASK_EXECUTIONDATE", nullable = false)
	private String taskExecutionDate;

	@Column(name = "TASK_TYPE")
	private String taskType;

	@Lob
	@Column(name = "TASK_PARAM_VALUE")
	private byte[] taskParamValue;

	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinColumn(name = "USER_ID")
	private UserDetail userDetail;

	@Column(name = "USERTEMPLATE_ID")
	private String userTemplateId;
	
	@Column(name = "FREQUENCY")
	private String frequency;

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getTaskExecutionTime() {
		return taskExecutionTime;
	}

	public void setTaskExecutionTime(String taskExecutionTime) {
		this.taskExecutionTime = taskExecutionTime;
	}

	public String getTaskExecutionDate() {
		return taskExecutionDate;
	}

	public void setTaskExecutionDate(String taskExecutionDate) {
		this.taskExecutionDate = taskExecutionDate;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public byte[] getTaskParamValue() {
		return taskParamValue;
	}

	public void setTaskParamValue(byte[] taskParamValue) {
		this.taskParamValue = taskParamValue;
	}

	public UserDetail getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}

	public String getUserTemplateId() {
		return userTemplateId;
	}

	public void setUserTemplateId(String userTemplateId) {
		this.userTemplateId = userTemplateId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	@Override
	public String toString() {
		return "Task [taskId=" + taskId + ", taskName=" + taskName + ", taskStatus=" + taskStatus
				+ ", taskExecutionTime=" + taskExecutionTime + ", taskExecutionDate=" + taskExecutionDate
				+ ", taskType=" + taskType + ", taskParamValue=" + Arrays.toString(taskParamValue) + ", userDetail="
				+ userDetail + ", userTemplateId=" + userTemplateId + ", frequency=" + frequency + "]";
	}



	
	
}
