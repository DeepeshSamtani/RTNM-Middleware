package com.harman.rtnm.model.response;

import java.io.Serializable;
import java.util.List;


public class DruidResponse extends BaseResponse<DruidResponse> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Response> response;

	public List<Response> getResponse() {
		return response;
	}

	public void setResponse(List<Response> response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return "DruidResponse [response=" + response + "]";
	}

}
