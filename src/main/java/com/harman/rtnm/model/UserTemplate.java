package com.harman.rtnm.model;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name="USER_TEMPLATE")
@Entity
public class UserTemplate implements Serializable{	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3818019165894540970L;

	@Column(name="REPORT_NAME",unique=true, nullable=false)
	private String reportName;
	
	@Lob
	@Column(name = "REPORT_DETAILS",nullable = false)
	private byte[] reportDetail;

	@EmbeddedId
    private UserTemplateKey userTemplateKey;


	
	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public byte[] getReportDetail() {
		return reportDetail;
	}

	public void setReportDetail(byte[] reportDetail) {
		this.reportDetail = reportDetail;
	}

	public UserTemplateKey getUserTemplateKey() {
		return userTemplateKey;
	}

	public void setUserTemplateKey(UserTemplateKey userTemplateKey) {
		this.userTemplateKey = userTemplateKey;
	}	


	@Override
	public String toString() {
		return "UserTemplate [reportName=" + reportName + ", reportDetail=" + Arrays.toString(reportDetail)
				+ ", userTemplateKey=" + userTemplateKey + "]";
	}
}