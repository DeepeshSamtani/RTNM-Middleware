package com.harman.rtnm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "STORAGE_DETAILS")
public class StorageDetails implements Serializable {

	private static final long serialVersionUID = 1L;
		
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	
	
	@NotNull
	@Column(name = "USER_TEMPLATE_ID",nullable=false)
	private String userTemplateId;
	
	@Column(name = "USER_ID")
	private int userId;
	
	@Column(name = "FILE_PATH")
	private String filePath;
	
	@Column(name = "FILE_PATH_URL")
	private String filePathUrl;
	


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getUserTemplateId() {
		return userTemplateId;
	}

	public void setUserTemplateId(String userTemplateId) {
		this.userTemplateId = userTemplateId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePathUrl() {
		return filePathUrl;
	}

	public void setFilePathUrl(String filePathUrl) {
		this.filePathUrl = filePathUrl;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "StorageDetails [id=" + id + ", userTemplateId=" + userTemplateId + ", userId=" + userId + ", filePath="
				+ filePath + ", filePathUrl=" + filePathUrl + "]";
	}

	

		
}
