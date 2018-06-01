package com.harman.rtnm.service;

import com.harman.rtnm.vo.ReportDetailVO;

public interface ReportDownloadService {


	 String exportReport(ReportDetailVO reportDetailVO) throws Exception;
}
