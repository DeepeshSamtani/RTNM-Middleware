package com.harman.rtnm.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Embeddable
public class PropertyKey implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5498312758959104983L;

	@Column(name = "PROPERTY_ID")
	private String propertyId;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "COUNTER_GROUP_ID")
	private CounterGroup counterGroupc;

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public CounterGroup getCounterGroups() {
		return counterGroupc;
	}

	public void setCounterGroups(CounterGroup counterGroupc) {
		this.counterGroupc = counterGroupc;
	}
	
}
