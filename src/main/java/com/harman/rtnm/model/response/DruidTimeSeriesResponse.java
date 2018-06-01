package com.harman.rtnm.model.response;

import java.util.Map;

public class DruidTimeSeriesResponse extends DruidBaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = -630708051735182485L;
	private Map<String, Object> result;

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "TimeseriesDataResponse [result=" + result + "]";
	}

}
