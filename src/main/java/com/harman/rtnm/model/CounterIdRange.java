package com.harman.rtnm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="COUNTER_ID_RANGE")
@Entity
public class CounterIdRange {
	@Id
	@Column(name="DEVICE_TYPE")
	private String deviceType;
	
	@Column(name="MIN_RANGE")
	private int minRange;
	
	@Column(name="MAX_RANGE")
	private int maxRange;
	
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public int getMinRange() {
		return minRange;
	}
	public void setMinRange(int minRange) {
		this.minRange = minRange;
	}
	public int getMaxRange() {
		return maxRange;
	}
	public void setMaxRange(int maxRange) {
		this.maxRange = maxRange;
	}
	@Override
	public String toString() {
		return "CounterIdRange [deviceType=" + deviceType + ", minRange=" + minRange + ", maxRange=" + maxRange + "]";
	}
	
	
}
