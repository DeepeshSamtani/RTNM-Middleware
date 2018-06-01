package com.harman.rtnm.model;


public class CommonKPI {

	private String id;
	
	private String deviceType;

	private String formula;

	private String counterList;

	private String displayName;

	private Boolean isActive;

	private Aggregation aggregationId;

	private String unit;

	private String description;
	
	private String aggregationType;
	
	private Long counterGroupId;
	
	private String kpiType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public Long getCounterGroupId() {
		return counterGroupId;
	}

	public void setCounterGroupId(Long counterGroupId) {
		this.counterGroupId = counterGroupId;
	}

	public String getKpiType() {
		return kpiType;
	}

	public void setKpiType(String kpiType) {
		this.kpiType = kpiType;
	}	
	
	
}
