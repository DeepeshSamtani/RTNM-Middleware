package com.harman.rtnm.model.response;

import java.util.List;
import java.util.Map;

public class DruidTopNResponse extends DruidBaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2137677189860671549L;
	
	private List<Map<String, Object>> result;

	public List<Map<String, Object>> getResult() {
		return result;
	}

	public void setResult(List<Map<String, Object>> result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "DruidTopNResponse [result=" + result + "]";
	}

}
