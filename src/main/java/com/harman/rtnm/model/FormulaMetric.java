package com.harman.rtnm.model;

import java.util.List;

public class FormulaMetric {

	private String metricName;
	private List<String> metricList;
	private String formula;
	private String formulaWithValue;
	private String formulaType;

	public String getMetricName() {
		return metricName;
	}

	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}

	public List<String> getMetricList() {
		return metricList;
	}

	public void setMetricList(List<String> metricList) {
		this.metricList = metricList;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getFormulaWithValue() {
		return formulaWithValue;
	}

	public void setFormulaWithValue(String formulaWithValue) {
		this.formulaWithValue = formulaWithValue;
	}	

	public String getFormulaType() {
		return formulaType;
	}

	public void setFormulaType(String formulaType) {
		this.formulaType = formulaType;
	}

	@Override
	public String toString() {
		return "FormulaMetric [metricName=" + metricName + ", metricList=" + metricList + ", formula=" + formula
				+ ", formulaWithValue=" + formulaWithValue + "]";
	}

}
