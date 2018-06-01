package com.harman.rtnm.vo;

import java.io.Serializable;
import java.util.List;

import com.harman.rtnm.model.Counter;
import com.harman.rtnm.model.CounterGroup;
import com.harman.rtnm.model.Element;
import com.harman.rtnm.model.Profile;
import com.harman.rtnm.model.User;

public class InventoryDetailVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1419866370805367084L;

	private String reportName;
	private String deviceName;
	private String deviceType;
	private List<Element> elements;
	private List<CounterGroup> counterGroups;
	private List<Counter> counters;
	private boolean allNodeFlag;
	private boolean allSubElementFlag;	
	private Profile profile;
	private User user;
	private String userName;
	
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public List<Element> getElements() {
		return elements;
	}
	public void setElements(List<Element> elements) {
		this.elements = elements;
	}
	public List<CounterGroup> getCounterGroups() {
		return counterGroups;
	}
	public void setCounterGroups(List<CounterGroup> counterGroups) {
		this.counterGroups = counterGroups;
	}
	public List<Counter> getCounters() {
		return counters;
	}
	public void setCounters(List<Counter> counters) {
		this.counters = counters;
	}
	public boolean isAllNodeFlag() {
		return allNodeFlag;
	}
	public void setAllNodeFlag(boolean allNodeFlag) {
		this.allNodeFlag = allNodeFlag;
	}
	public boolean isAllSubElementFlag() {
		return allSubElementFlag;
	}
	public void setAllSubElementFlag(boolean allSubElementFlag) {
		this.allSubElementFlag = allSubElementFlag;
	}	
	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
