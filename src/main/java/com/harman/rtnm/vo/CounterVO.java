package com.harman.rtnm.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CounterVO {

	private Long counterId;
	private String displayName;
	private String physicalName;
	private String unit;
	private CounterGroupVO counterGroup;

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Long getCounterId() {
		return counterId;
	}

	public void setCounterId(Long counterId) {
		this.counterId = counterId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPhysicalName() {
		return physicalName;
	}

	public void setPhysicalName(String physicalName) {
		this.physicalName = physicalName;
	}

	public CounterGroupVO getCounterGroup() {
		return counterGroup;
	}

	public void setCounterGroup(CounterGroupVO counterGroup) {
		this.counterGroup = counterGroup;
	}

}
