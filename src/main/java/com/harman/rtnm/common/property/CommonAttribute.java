package com.harman.rtnm.common.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:common.properties")
public class CommonAttribute {
	
	@Value("${common.zoneId}")
	private String zoneId;
	
	@Value("${max.report.size}")
	private int maxReportCount;
	
	@Value("${max.dashboard.size}")
	private int  maxDashboardCount;

	public String getZoneId() {
		return zoneId;
	}
	
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public int getMaxReportCount() {
		return maxReportCount;
	}

	public void setMaxReportCount(int maxReportCount) {
		this.maxReportCount = maxReportCount;
	}

	public int getMaxDashboardCount() {
		return maxDashboardCount;
	}

	public void setMaxDashboardCount(int maxDashboardCount) {
		this.maxDashboardCount = maxDashboardCount;
	}

	@Override
	public String toString() {
		return "CommonAttribute [zoneId=" + zoneId + ", maxReportCount=" + maxReportCount + ", maxDashboardCount="
				+ maxDashboardCount + "]";
	}


}
