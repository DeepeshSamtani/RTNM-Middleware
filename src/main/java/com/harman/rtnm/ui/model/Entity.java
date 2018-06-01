package com.harman.rtnm.ui.model;

import java.io.Serializable;

public class Entity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8371220357984479645L;

	private String id;
	private String displayName;
	private String value;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	} 
	
	
	
	
}
