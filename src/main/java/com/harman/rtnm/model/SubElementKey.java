package com.harman.rtnm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Embeddable
public class SubElementKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5041833720933269179L;

	@Column(name = "SUBELEMENT_NAME")
	private String subElementName;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COUNTER_GROUP_ID", nullable = false)
	private CounterGroup counterGroup;

	public String getSubElementName() {
		return subElementName;
	}

	public void setSubElementName(String subElementName) {
		this.subElementName = subElementName;
	}

	public CounterGroup getCounterGroup() {
		return counterGroup;
	}

	public void setCounterGroup(CounterGroup counterGroup) {
		this.counterGroup = counterGroup;
	}
	

}
