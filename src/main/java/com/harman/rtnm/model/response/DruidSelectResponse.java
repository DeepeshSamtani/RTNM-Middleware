package com.harman.rtnm.model.response;

import java.util.Map;

public class DruidSelectResponse extends DruidBaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 405569692647572075L;
	private Map<String, Object> result;

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "DruidSelectResponse [result=" + result + "]";
	}
}