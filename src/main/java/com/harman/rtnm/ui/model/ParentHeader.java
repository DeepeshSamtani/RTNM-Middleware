package com.harman.rtnm.ui.model;

import java.io.Serializable;
import java.util.List;

public class ParentHeader implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4752932475015099763L;
	
	private String id;
	private String displayName;
	private String value; 
	private List<Entity> children;
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
	public List<Entity> getChildren() {
		return children;
	}
	public void setChildren(List<Entity> children) {
		this.children = children;
	}	
	
}
