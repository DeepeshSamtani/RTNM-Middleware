package com.harman.rtnm.service;

import java.util.List;

import com.harman.rtnm.model.Counter;
import com.harman.rtnm.model.CounterGroup;
import com.harman.rtnm.model.response.UserReport;
import com.harman.rtnm.vo.ReportDetailVO;


public interface ReportService {

	UserReport getSpecificReport(ReportDetailVO reportDetailVO) throws Exception;
	
	UserReport addUserReport(ReportDetailVO reportDetailVO) throws Exception;
	
	UserReport getUserReport(ReportDetailVO reportDetailVO) throws Exception;
	
	List<Counter> addCounterGroupIdInCounterKey(List<CounterGroup> counterGrpList) ;
	
	UserReport updateUserReport(ReportDetailVO reportDetailVO) throws Exception;
	
	UserReport getReportAccordingToFilter(ReportDetailVO reportDetailVO) throws Exception;
	
	void deleteReport(ReportDetailVO reportDetailVO) throws Exception ;
	
	String getDruidURI();
}
