package com.harman.rtnm.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.harman.rtnm.common.constant.Constant;
import com.harman.rtnm.samsung.commonutils.util.StringUtils;

@Entity
@Table(name = "COUNTER" )
@JsonIgnoreProperties(ignoreUnknown = true)
public class Counter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1463346356298683450L;

	@EmbeddedId
	@JsonProperty(value="counterKey", required=false)
	private CounterKey counterKey;
	
	@JsonProperty(value="displayName", required= false, defaultValue="")
	@Column(name = "LOGICAL_NAME")
	private String logicalName;
	
	@Column(name = "COUNTER_UNIT")
	@JsonProperty(value="counterUnit", required= false, defaultValue="number")
	private String counterUnit;

	@Column(name = "COUNTER_DESCRIPTION")
	@JsonProperty(value="counterDescription", required= false, defaultValue="Counter Description")
	private String counterDescription;
	
	@ManyToOne(cascade = {CascadeType.ALL, CascadeType.PERSIST}, fetch = FetchType.EAGER)
	@JoinColumn(name = "AGGREGATION_TYPE")
	private Aggregation aggregation;
	
	/*
	 * TODO: check if providing a default value can cause issues
	 * or this field is to marked as mandatory.
	 */
	@Column(name = "COUNTER_TYPE")
	@JsonProperty(value="counterType", required= false, defaultValue = "Counter")
	private String counterType;	
	
	@Column(name = "ENABLED")	
	@JsonProperty(value="enabled", required= false, defaultValue = "1")
	private Integer enabled;
	
	@Column(name = "C_SOURCE")	
	@JsonProperty(value="sourceName", required= false, defaultValue="")
	private String cSource;
	
	/**
	 * contains the mathematical expression
	 */
	@Column(name = "KPI_FORMULA")
	@JsonProperty(value = "formula", required= false, defaultValue="")
	private String kpiFormula;
	
	@Transient
	private Set<String> aggregationList;

	public CounterKey getCounterKey() {
		return counterKey;
	}

	public void setCounterKey(CounterKey counterKey) {
		this.counterKey = counterKey;
	}

	public String getLogicalName() {
		return logicalName;
	}

	public void setLogicalName(String logicalName) {
		this.logicalName = logicalName;
	}

	public String getCounterUnit() {
		return counterUnit;
	}

	public void setCounterUnit(String counterUnit) {
		this.counterUnit = counterUnit;
	}

	public String getCounterDescription() {
		return counterDescription;
	}

	public void setCounterDescription(String counterDescription) {
		this.counterDescription = counterDescription;
	}

	public Aggregation getAggregation() {
		return aggregation;
	}

	public void setAggregation(Aggregation aggregation) {
		this.aggregation = aggregation;
	}

	public String getCounterType() {
		return counterType;
	}

	public void setCounterType(String counterType) {
		this.counterType = counterType;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public String getcSource() {
		return cSource;
	}

	public void setcSource(String cSource) {
		this.cSource = cSource;
	}

	public Set<String> getAggregationList() {
		return aggregationList;
	}

	public void setAggregationList(Set<String> aggregationList) {
		this.aggregationList = aggregationList;
	}
		
	/**
	 * @return the kpiFormula
	 */
	public String getKpiFormula() {
		return kpiFormula;
	}

	/**
	 * @param kpiFormula the kpiFormula to set
	 */
	public void setKpiFormula(String kpiFormula) {
		this.kpiFormula = kpiFormula;
	}
	
	/**
	 * Returns a comma separated list for all the applicable 
	 * Aggregation types
	 * @param aggregation
	 * @return
	 */
	public String getAggType(Aggregation aggregation) {
		StringBuilder aggTypes = new StringBuilder();

		aggTypes = (aggregation.getHourSum() > 0) ? aggTypes.append(Constant.SUM).append(StringUtils.SYMBOL_COMMA) : aggTypes;
		aggTypes = (aggregation.getHourMin() > 0) ? aggTypes.append(Constant.MIN).append(StringUtils.SYMBOL_COMMA) : aggTypes;
		aggTypes = (aggregation.getHourMax() > 0) ? aggTypes.append(Constant.MAX).append(StringUtils.SYMBOL_COMMA) : aggTypes;
		aggTypes = (aggregation.getHourAvg() > 0) ? aggTypes.append(Constant.AVG).append(StringUtils.SYMBOL_COMMA) : aggTypes;
		
		//remove the last comma
		aggTypes.setLength(aggTypes.length()-1);
		
		return aggTypes.toString();
	}

	public String toString() {
		return "[logicalName : "+ logicalName +", counterUnit : " + counterUnit + ", counterDescription : " + counterDescription 
					+ ", counterType : "+ counterType + ", enabled : " + enabled + ", csource :" + cSource+ ", Formula : " + kpiFormula +"]";		
	}
}
