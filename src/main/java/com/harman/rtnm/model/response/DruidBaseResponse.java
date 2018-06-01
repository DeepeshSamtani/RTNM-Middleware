package com.harman.rtnm.model.response;

import java.io.Serializable;

public class DruidBaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1513029036774229799L;

	private String timestamp;

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "DruidBaseResponse [timestamp=" + timestamp + "]";
	}
}