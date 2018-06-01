package com.harman.rtnm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="ACCESS")
@Entity
public class Access implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3567949008685583395L;
	
	@Id
	@Column(name = "ACCESS_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long accessId;
		
	@Column(name="ACCESS_NAME",unique=true, nullable=false)
	private String accessName;

	@Column(name="DISPLAY_NAME",unique=true, nullable=false)
	private String displayName;

	public Long getAccessId() {
		return accessId;
	}

	public void setAccessId(Long accessId) {
		this.accessId = accessId;
	}

	public String getAccessName() {
		return accessName;
	}

	public void setAccessName(String accessName) {
		this.accessName = accessName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	
	
}
