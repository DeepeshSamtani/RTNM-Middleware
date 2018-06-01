package com.harman.rtnm.model.response;

import java.io.Serializable;
import java.util.List;

import com.harman.rtnm.model.Counter;
import com.harman.rtnm.model.GraphData;
import com.harman.rtnm.model.KPI;
import com.harman.rtnm.model.SeriesData;
import com.harman.rtnm.ui.model.ParentHeader;


public class UserReport extends BaseResponse<UserReport> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8241377376252889637L;

	private List<Counter> metrics;
	private List<KPI> kpiMetrics;
	private List<Response> response;
	private String userName;
	private String reportName;
	private GraphData graphData;
	private List<ParentHeader> header;
	
	public List<Counter> getMetrics() {
		return metrics;
	}
	public void setMetrics(List<Counter> metrics) {
		this.metrics = metrics;
	}
	public List<KPI> getKpiMetrics() {
		return kpiMetrics;
	}
	public void setKpiMetrics(List<KPI> kpiMetrics) {
		this.kpiMetrics = kpiMetrics;
	}
	public List<Response> getResponse() {
		return response;
	}
	public void setResponse(List<Response> response) {
		this.response = response;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public GraphData getGraphData() {
		return graphData;
	}
	public void setGraphData(GraphData graphData) {
		this.graphData = graphData;
	}	
	public List<ParentHeader> getHeader() {
		return header;
	}
	public void setHeader(List<ParentHeader> header) {
		this.header = header;
	}
	@Override
	public String toString() {
		return "UserReport [metrics=" + metrics + ", kpiMetrics=" + kpiMetrics + ", response=" + response
				+ ", userName=" + userName + ", reportName=" + reportName + ", graphData=" + graphData + ", parenrt="
				+ header + "]";
	}

}
