package com.harman.rtnm.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Table(name = "PROPERTIES")
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Properties implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6597031221759758943L;
	
	@EmbeddedId
	private PropertyKey propertyKey;
	
	@Column(name = "PROPERTY_NAME")
	private String propertyName;
	
	@Column(name = "PROPERTY_VALUES")
	private String propertyValues;

	@Transient
	private List<String> selectedValues;
	
	public PropertyKey getPropertyKey() {
		return propertyKey;
	}

	public void setPropertyKey(PropertyKey propertyKey) {
		this.propertyKey = propertyKey;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyValues() {
		return propertyValues;
	}

	public void setPropertyValues(String propertyValues) {
		this.propertyValues = propertyValues;
	}

	public List<String> getSelectedValues() {
		return selectedValues;
	}

	public void setSelectedValues(List<String> selectedValues) {
		this.selectedValues = selectedValues;
	}
	
	

}
