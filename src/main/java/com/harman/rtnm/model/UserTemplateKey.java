package com.harman.rtnm.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Embeddable
public class UserTemplateKey implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8862956956427040363L;

	@Column(name="USER_TEMPLATE_ID")
	private String usertemplateId;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "USER_ID", nullable = false,updatable = true)
	private UserDetail userDetail;

	public String getUsertemplateId() {
		return usertemplateId;
	}

	public void setUsertemplateId(String usertemplateId) {
		this.usertemplateId = usertemplateId;
	}

	public UserDetail getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}

	@Override
	public String toString() {
		return "UserTemplateKey [usertemplateId=" + usertemplateId + ", userDetail=" + userDetail + "]";
	}
	
}