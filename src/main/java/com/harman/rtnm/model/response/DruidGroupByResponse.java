package com.harman.rtnm.model.response;

import java.util.Map;

public class DruidGroupByResponse extends DruidBaseResponse {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2961538125986369283L;
	private String version;
	private Map<String, Object> event;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Map<String, Object> getEvent() {
		return event;
	}

	public void setEvent(Map<String, Object> event) {
		this.event = event;
	}

	@Override
	public String toString() {
		return "DruidGroupByResponse [version=" + version + ", event=" + event + "]";
	}

}
