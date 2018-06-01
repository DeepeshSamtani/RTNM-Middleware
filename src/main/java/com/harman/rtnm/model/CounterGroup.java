package com.harman.rtnm.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Table(name = "COUNTER_GROUP")
@Entity
public class CounterGroup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9139179858991094381L;

	@Id
	@Column(name = "COUNTER_GROUP_ID")
	private String counterGroupId;

	@JsonProperty("displayName")
	@Column(name = "COUNTER_GROUP_NAME")
	private String counterGroupName;

	@Column(name = "COUNTER_GROUP_DETAILS")
	private String counterGroupDetails;

	@Column(name = "DEVICE_TYPE")
	private String deviceType;
	
	@Column(name = "NE_LEVEL")
	private String neLevel;
	
	@Column(name = "CG_SOURCE")
	private String cgSource;

	
	@OneToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY, mappedBy = "counterKey.counterGroup")
	private List<Counter> counterList;
	
	
	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "propertyKey.counterGroupc")
	private List<Properties> properties;
	
	@Transient
	private List<Counter> kpis;

	//default constructor
	public CounterGroup() {
		
	}
	
	public CounterGroup(String id) {
		this.counterGroupId = id;
	}
	
	public String getCounterGroupId() {
		return counterGroupId;
	}

	public void setCounterGroupId(String counterGroupId) {
		this.counterGroupId = counterGroupId;
	}

	public String getCounterGroupName() {
		return counterGroupName;
	}

	public void setCounterGroupName(String counterGroupName) {
		this.counterGroupName = counterGroupName;
	}

	public String getCounterGroupDetails() {
		return counterGroupDetails;
	}

	public void setCounterGroupDetails(String counterGroupDetails) {
		this.counterGroupDetails = counterGroupDetails;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getNeLevel() {
		return neLevel;
	}

	public void setNeLevel(String neLevel) {
		this.neLevel = neLevel;
	}

	public String getCgSource() {
		return cgSource;
	}

	public void setCgSource(String cgSource) {
		this.cgSource = cgSource;
	}

	public List<Counter> getCounterList() {
		return counterList;
	}

	public void setCounterList(List<Counter> counterList) {
		this.counterList = counterList;
	}

	public List<Properties> getProperties() {
		return properties;
	}

	public void setProperties(List<Properties> properties) {
		this.properties = properties;
	}

	public List<Counter> getKpis() {
		return kpis;
	}

	public void setKpis(List<Counter> kpis) {
		this.kpis = kpis;
	}	
	
	

}
