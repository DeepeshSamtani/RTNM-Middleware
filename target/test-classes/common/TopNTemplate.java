package com.harman.dyns.model.common;

import java.util.Map;

public class TopNTemplate
{
	
	private int templateid;
	private String userid;
	private Map<String,Object>  parammap;

	public int getTemplateid() {
		return templateid;
	}

	public void setTemplateid(int templateid) {
		this.templateid = templateid;
	}

	public Map<String, Object> getParammap() {
		return parammap;
	}

	public void setParammap(Map<String, Object> parammap) {
		this.parammap = parammap;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	
	
}