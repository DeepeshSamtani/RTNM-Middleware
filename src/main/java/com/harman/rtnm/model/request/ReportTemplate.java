package com.harman.rtnm.model.request;

import java.util.List;

import com.harman.dyns.model.druid.request.ReportRequest;
import com.harman.rtnm.model.CounterGroup;
import com.harman.rtnm.vo.CounterGroupVO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportTemplate extends ReportRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// private List<CounterGroup> counterGroupsWithData;
	private List<CounterGroup> counterGroupsWithCounterAndProperties;

	public List<CounterGroup> getCounterGroupsWithCounterAndProperties() {
		return counterGroupsWithCounterAndProperties;
	}

	public void setCounterGroupsWithCounterAndProperties(List<CounterGroup> counterGroupsWithCounterAndProperties) {
		this.counterGroupsWithCounterAndProperties = counterGroupsWithCounterAndProperties;
	}

}
