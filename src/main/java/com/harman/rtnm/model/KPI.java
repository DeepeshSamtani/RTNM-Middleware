package com.harman.rtnm.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Table(name = "KPI", uniqueConstraints = @UniqueConstraint(columnNames = { "DISPLAY_NAME", "FORMULA" }))
@Entity
public class KPI implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4491283527027780769L;

	@Id
	@JsonIgnoreProperties
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private int id;

	@Column(name = "DEVICE_TYPE")
	private String deviceType;

	@Column(name = "FORMULA")
	private String formula;

	@Column(name = "COUNTER_LIST")
	private String counterList;

	@Column(name = "DISPLAY_NAME",updatable=false)
	private String displayName;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name ="aggregationId_ID")
	private Aggregation aggregationId;

	@Column(name = "UNIT")
	private String unit;

	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name="AGGREGATION_TYPE")
	private String aggregationType;
	
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@Transient
	private List<Counter> counterConfig;
	
	@Column(name="COUNTER_GROUP_ID")
	private String counterGroupId;
	
	public int getId() {
		return id;
	}

	public String getCounterGroupId() {
		return counterGroupId;
	}

	public void setCounterGroupId(String counterGroupId) {
		this.counterGroupId = counterGroupId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getCounterList() {
		return counterList;
	}

	public void setCounterList(String counterList) {
		this.counterList = counterList;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}


	public Aggregation getAggregationId() {
		return aggregationId;
	}

	public void setAggregationId(Aggregation aggregationId) {
		this.aggregationId = aggregationId;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	public String getAggregationType() {
		return aggregationType;
	}

	public void setAggregationType(String aggregationType) {
		this.aggregationType = aggregationType;
	}

	public List<Counter> getCounterConfig() {
		return counterConfig;
	}

	public void setCounterConfig(List<Counter> counterConfig) {
		this.counterConfig = counterConfig;
	}

	@Override
	public String toString() {
		return "KPI [id=" + id + ", deviceType=" + deviceType + ", formula=" + formula + ", counterList=" + counterList
				+ ", displayName=" + displayName + ", isActive=" + isActive + ", aggregationId=" + aggregationId
				+ ", unit=" + unit + ", description=" + description + ", aggregationType=" + aggregationType
				+ ", counterConfig=" + counterConfig + ", counterGroupId=" + counterGroupId + "]";
	}

	

	

	

}
