/**
 * 
 */
package com.harman.rtnm.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.harman.rtnm.model.Aggregation;
import com.harman.rtnm.model.Counter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamicCounterVO implements Serializable {

	/**
	 * serial id for serialization versioning.
	 */
	private static final long serialVersionUID = 4339762769253977416L;
	
	@JsonProperty(value = "counter", required = false)
	private Counter counter;
	
	@JsonProperty(value = "deviceType", required = false)
	private String deviceType;
	
	@JsonProperty(value = "aggregationType", required = false)
	private String aggregationType;	
	
	@JsonProperty(value = "counterGroupId", required = false)
	private String groupId;
	
	@JsonProperty(value = "counterId", required = false)
	private String counterId;
	
	@JsonProperty(value = "mapCounterIdNames", required = false)
	private Map<String, String> mapCounterIdNames;
		
	//default constructor
	public DynamicCounterVO() {
		
	}
	
	public DynamicCounterVO(Counter counter) {
		this.counter = counter;
	}
	
	/**
	 * @return the deviceType
	 */
	public String getDeviceType() {
		return deviceType;
	}
	/**
	 * @param deviceType the deviceType to set
	 */
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	/**
	 * @return the aggregationType
	 */
	public String getAggregationType() {
		return aggregationType;
	}
	
	/**
	 * @param aggregationType the aggregationType to set
	 */
	public void setAggregationType(String aggregationType) {
		this.aggregationType = aggregationType;
	}
	
	/**
	 * 
	 * @param counterType the counter type to be set
	 */
	public void setCounterType(String counterType) {
		counter.setCounterType(counterType);
		
	}
	
	public void setAggregationID(Aggregation aggregation) {
		counter.setAggregation(aggregation);
	}
	
	/**
	 * @return the counter
	 */
	public Counter getCounter() {
		return counter;
	}
	
	/**
	 * @param counter the counter to set
	 */
	public void setCounter(Counter counter) {
		this.counter = counter;
	}
	
	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}
	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	/**
	 * @return the counterId
	 */
	public String getCounterId() {
		return counterId;
	}
	
	/**
	 * @param counterId the counterId to set
	 */	
	public void setCounterId(String counterId) {
		this.counterId = counterId;
	}
	
	/**
	 * @return the mapCounterIdNames
	 */
	public Map<String, String> getMapCounterIdNames() {
		return mapCounterIdNames;
	}

	/**
	 * @param mapCounterIdNames the mapCounterIdNames to set
	 */
	public void setMapCounterIdNames(Map<String, String> mapCounterIdNames) {
		this.mapCounterIdNames = mapCounterIdNames;
	}
	
	/*
	 * insert the entry into the map where counter id is the key and name is the value
	 */
	public void addMappingCounterIdName(String counterId, String counterName) {
		if(mapCounterIdNames == null) {
			this.mapCounterIdNames = new HashMap<String, String>();
		}
		
		this.mapCounterIdNames.put(counterId, counterName);
	}
	
	
	public String toString() {
		return  "["+ deviceType + "," + groupId + ", counter : " + counter + "]";
	}
	
}
