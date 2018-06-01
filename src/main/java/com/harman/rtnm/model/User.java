package com.harman.rtnm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="USER")
@Entity
public class User {

	@Id
	@Column(name="EIN_ID",unique=true, nullable=false)
	private String einId;

	
	public String getEIN() {
		return einId;
	}

	public void setEIN(String einId) {
		einId = einId;
	}
	
	
	
}
