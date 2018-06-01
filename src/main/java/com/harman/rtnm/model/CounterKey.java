package com.harman.rtnm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Embeddable
public class CounterKey implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5616563070265210375L;
	
	@Column(name = "COUNTER_ID")
	private String counterId;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "COUNTER_GROUP_ID")
	private CounterGroup counterGroup;

	@Transient
	private CounterGroup counterGroupTrans;
	
	//default constructor
	public CounterKey() { }
	
	public CounterKey(String counterId, String groupId) {
		this.counterId = counterId;
		this.counterGroup = new CounterGroup(groupId);
	}
	
	public String getCounterId() {
		return counterId;
	}
	public void setCounterId(String counterId) {
		this.counterId = counterId;
	}

	public CounterGroup getCounterGroup() {
		return counterGroup;
	}

	public void setCounterGroup(CounterGroup counterGroup) {
		this.counterGroup = counterGroup;
	}

	public CounterGroup getCounterGroupTrans() {
		return counterGroupTrans;
	}

	public void setCounterGroupTrans(CounterGroup counterGroupTrans) {
		this.counterGroupTrans = counterGroupTrans;
	}
	
}
