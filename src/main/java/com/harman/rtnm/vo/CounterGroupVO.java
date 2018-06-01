package com.harman.rtnm.vo;

import java.util.List;

import com.harman.rtnm.model.CommonKPI;
import com.harman.rtnm.model.Counter;
import com.harman.rtnm.model.KPI;
import com.harman.rtnm.model.Properties;
import com.harman.rtnm.model.SubElement;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CounterGroupVO {

	private String counterGroupId;
	private String displayName;// counterGroupName;
	private String counterGroupDetails;
	private List<Counter> counterList;
	private List<SubElement> subElement;
	private List<KPI> kpiList;
	private List<CommonKPI> commonKpiList;
	private List<Properties> properties;

	public String getCounterGroupId() {
		return counterGroupId;
	}

	public void setCounterGroupId(String counterGroupId) {
		this.counterGroupId = counterGroupId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getCounterGroupDetails() {
		return counterGroupDetails;
	}

	public void setCounterGroupDetails(String counterGroupDetails) {
		this.counterGroupDetails = counterGroupDetails;
	}

	public List<Counter> getCounterList() {
		return counterList;
	}

	public void setCounterList(List<Counter> counterList) {
		this.counterList = counterList;
	}

	public List<SubElement> getSubElement() {
		return subElement;
	}

	public void setSubElement(List<SubElement> subElement) {
		this.subElement = subElement;
	}

	public List<KPI> getKpiList() {
		return kpiList;
	}

	public void setKpiList(List<KPI> kpiList) {
		this.kpiList = kpiList;
	}

	public List<CommonKPI> getCommonKpiList() {
		return commonKpiList;
	}

	public void setCommonKpiList(List<CommonKPI> commonKpiList) {
		this.commonKpiList = commonKpiList;
	}

	public List<Properties> getProperties() {
		return properties;
	}

	public void setProperties(List<Properties> properties) {
		this.properties = properties;
	}

	

}
