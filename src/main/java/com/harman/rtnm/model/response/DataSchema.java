package com.harman.rtnm.model.response;

import java.util.List;

public class DataSchema {

	private Object granularitySpec;

    private String dataSource;

    private List metricsSpec;

    private Object parser;
    
	public Object getGranularitySpec() {
		return granularitySpec;
	}

	public void setGranularitySpec(Object granularitySpec) {
		this.granularitySpec = granularitySpec;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public List getMetricsSpec() {
		return metricsSpec;
	}

	public void setMetricsSpec(List metricsSpec) {
		this.metricsSpec = metricsSpec;
	}

	public Object getParser() {
		return parser;
	}

	public void setParser(Object parser) {
		this.parser = parser;
	}

	@Override
	public String toString() {
		return "DataSchema [granularitySpec=" + granularitySpec + ", dataSource=" + dataSource + ", metricsSpec="
				+ metricsSpec + ", parser=" + parser + "]";
	}
    
    
}
