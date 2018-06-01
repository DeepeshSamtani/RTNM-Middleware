package com.harman.rtnm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="ROLE")
@Entity
public class Role {

	@Id
	@Column(name="ROLE_ID",unique=true, nullable=false)
	private String roleId;
	
	@Column(name="ROLE_NAME",unique=true, nullable=false)
	private String name;

	
	
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
