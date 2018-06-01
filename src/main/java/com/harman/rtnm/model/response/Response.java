package com.harman.rtnm.model.response;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*private String timestamp;
	private String version;*/
	private Map<String, Object> pagingIdentifiers;
	/*private List<String> dimensions;
	private List<String> metrics;*/
	private List<Map<String, Object>> events;
	
	
	/*public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}*/
	public Map<String, Object> getPagingIdentifiers() {
		return pagingIdentifiers;
	}
	public void setPagingIdentifiers(Map<String, Object> pagingIdentifiers) {
		this.pagingIdentifiers = pagingIdentifiers;
	}
	/*public List<String> getDimensions() {
		return dimensions;
	}
	public void setDimensions(List<String> dimensions) {
		this.dimensions = dimensions;
	}
	public List<String> getMetrics() {
		return metrics;
	}
	public void setMetrics(List<String> metrics) {
		this.metrics = metrics;
	}*/
	public List<Map<String, Object>> getEvents() {
		return events;
	}
	public void setEvents(List<Map<String, Object>> events) {
		this.events = events;
	}
	@Override
	public String toString() {
		return "Response [pagingIdentifiers=" + pagingIdentifiers + ", events=" + events + "]";
	}
	
}
