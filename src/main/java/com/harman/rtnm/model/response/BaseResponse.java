package com.harman.rtnm.model.response;

import java.io.Serializable;

import com.harman.dyns.model.druid.request.ReportRequest;

public class BaseResponse<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5793271177401250834L;

	private String userTemplateId;

	private ReportRequest reportConfiguration;

	public String getUserTemplateId() {
		return userTemplateId;
	}

	public void setUserTemplateId(String userTemplateId) {
		this.userTemplateId = userTemplateId;
	}

	public ReportRequest getReportConfiguration() {
		return reportConfiguration;
	}

	public void setReportConfiguration(ReportRequest reportConfiguration) {
		this.reportConfiguration = reportConfiguration;
	}

	@Override
	public String toString() {
		return "BaseResponse [userTemplateId=" + userTemplateId + ", reportConfiguration=" + reportConfiguration + "]";
	}

}
