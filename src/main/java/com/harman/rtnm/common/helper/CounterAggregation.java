package com.harman.rtnm.common.helper;

import java.util.List;

public class CounterAggregation {

	private Long counterId;
	private List<String> correspondingCounter;
	private String aggregationFormula;
	private String subElement;
	private double result;
	
	public String getSubElement() {
		return subElement;
	}

	public void setSubElement(String subElement) {
		this.subElement = subElement;
	}

	public Long getCounterId() {
		return counterId;
	}

	public void setCounterId(Long counterId) {
		this.counterId = counterId;
	}

	public List<String> getCorrespondingCounter() {
		return correspondingCounter;
	}

	public void setCorrespondingCounter(List<String> correspondingCounter) {
		this.correspondingCounter = correspondingCounter;
	}

	public String getAggregationFormula() {
		return aggregationFormula;
	}

	public void setAggregationFormula(String aggregationFormula) {
		this.aggregationFormula = aggregationFormula;
	}

	public double getResult() {
		return result;
	}

	public void setResult(double result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "CounterAggregation [counterId=" + counterId + ", correspondingCounter=" + correspondingCounter
				+ ", aggregationFormula=" + aggregationFormula + ", subElement=" + subElement + ", result=" + result
				+ "]";
	}

}