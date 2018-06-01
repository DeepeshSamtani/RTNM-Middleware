package com.harman.rtnm.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.harman.dyns.model.druid.request.ReportRequest;
import com.harman.rtnm.dao.SubDashboardDao;
import com.harman.rtnm.model.Dashboard;
import com.harman.rtnm.model.DashboardTemplate;
import com.harman.rtnm.model.SubDashboard;
import com.harman.rtnm.model.response.DashboardReport;
import com.harman.rtnm.model.response.SubDashboardReport;
import com.harman.rtnm.model.response.UserReport;
import com.harman.rtnm.service.DashboardService;
import com.harman.rtnm.service.ReportService;
import com.harman.rtnm.service.SubDashboardService;
import com.harman.rtnm.vo.DashboardDetailVO;
import com.harman.rtnm.vo.ReportDetailVO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SubDashboardServiceImpl implements SubDashboardService {

	@Autowired
	SubDashboardDao subDashboardDao;

	@Autowired
	ReportService reportService;

	@Autowired
	DashboardService dashboardService;

	ObjectMapper mapper = new ObjectMapper();

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public DashboardReport getSubDashboardsForDashboard(String dashboardId) throws Exception {
		// TODO Auto-generated method stub
		DashboardReport dashboardReport = dashboardService.getDashboardReportWithSubDashboards(dashboardId);
		return dashboardReport;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public SubDashboardReport getSpecificSubDashboard(DashboardDetailVO dashboardDetailVO) throws Exception {
		SubDashboardReport subDashboardReport = new SubDashboardReport();
		List<UserReport> reports = new ArrayList<>();
		SubDashboard subDashboard = subDashboardDao.getSpecificSubDashboard(dashboardDetailVO);
		if (null != subDashboard && !subDashboard.getDashboardTemplates().isEmpty()) {
			subDashboard.getDashboardTemplates().forEach(report -> {
				ReportDetailVO reportDetailVO = new ReportDetailVO();
				BeanUtils.copyProperties(dashboardDetailVO, reportDetailVO);
				try {
					Map<String, Object> inputjson = mapper.readValue(new String(report.getReportDetail()),
							new TypeReference<Map<String, Object>>() {
							});
					reportDetailVO.setJsonString(inputjson);
					//UserReport userReport = new UserReport();
				     UserReport userReport = reportService.getSpecificReport(reportDetailVO);
					userReport.setUserTemplateId(report.getDashboardtemplateId());
					userReport.setResponse(null);
					userReport.setGraphData(null);
					reports.add(userReport);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		//subDashboardReport.setDashboardId(subDashboard.getSubDashboardKey().getDashboardId());
		subDashboardReport.setName(subDashboard.getName());
		subDashboardReport.setSubDashboardId(subDashboard.getSubDashboardId());
		subDashboardReport.setReports(reports);
		return subDashboardReport;
	}

}
