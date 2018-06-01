package com.harman.rtnm.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.harman.rtnm.common.helper.ReportDownloadHelper;
import com.harman.rtnm.model.request.ReportTemplate;
import com.harman.rtnm.model.response.ExportDetails;
import com.harman.rtnm.service.ReportDownloadService;
import com.harman.rtnm.service.ReportService;
import com.harman.rtnm.vo.ReportDetailVO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class ReportDownloadServiceImpl implements ReportDownloadService {

	@Autowired
	ReportDownloadHelper reportDownloadHelper;

	@Autowired
	ReportService reportService;

	ObjectMapper mapper = new ObjectMapper();

	@Override
	public String exportReport(ReportDetailVO reportDetailVO) throws Exception {

		ExportDetails exportDetails = new ExportDetails();

		if (!StringUtils.isEmpty(reportDetailVO.getUserTemplateId())) {
			exportDetails.setUserReport(reportService.getUserReport(reportDetailVO));
		} else {
			exportDetails.setUserReport(reportService.getSpecificReport(reportDetailVO));
			if (!StringUtils.isEmpty(reportDetailVO.getUserName()))
				exportDetails.getUserReport().setUserName(reportDetailVO.getUserName());
			else
				throw new Exception("userName can not be Null");

			if (!StringUtils.isEmpty(reportDetailVO.getJsonString()))
				exportDetails.getUserReport().setReportConfiguration(
						mapper.convertValue(reportDetailVO.getJsonString(), ReportTemplate.class));
		}

		exportDetails.setFileHeaderList(new ArrayList<>());
		exportDetails.setFileContent(reportDownloadHelper.processDruidResponseData(exportDetails,reportDetailVO) );
		exportDetails.setReportType(reportDetailVO.getFileType());
		exportDetails.setReportFlag(true);
		return reportDownloadHelper.generateReport(exportDetails);

	}

}