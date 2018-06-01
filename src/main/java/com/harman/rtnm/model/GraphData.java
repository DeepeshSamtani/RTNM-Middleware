package com.harman.rtnm.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class GraphData {
	
	private List<SeriesData> seriesData;
	private Map<String,Set<String>> propertyValuesMap;
	

	public List<SeriesData> getSeriesData() {
		return seriesData;
	}

	public void setSeriesData(List<SeriesData> seriesData) {
		this.seriesData = seriesData;
	}

	public Map<String, Set<String>> getPropertyValuesMap() {
		return propertyValuesMap;
	}

	public void setPropertyValuesMap(Map<String, Set<String>> propertyValuesMap) {
		this.propertyValuesMap = propertyValuesMap;
	}
 
	
	
}
