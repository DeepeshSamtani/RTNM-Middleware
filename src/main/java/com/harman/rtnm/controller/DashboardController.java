package com.harman.rtnm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.harman.rtnm.model.Dashboard;
import com.harman.rtnm.model.response.DashboardReport;
import com.harman.rtnm.model.response.SubDashboardReport;
import com.harman.rtnm.service.DashboardService;
import com.harman.rtnm.service.SubDashboardService;
import com.harman.rtnm.vo.DashboardDetailVO;
import com.harman.rtnm.vo.DashboardVO;

@EnableAspectJAutoProxy(proxyTargetClass = true)
@RequestMapping(value = "/dashboard")
@RestController
public class DashboardController {

	@Autowired
	DashboardService dashboardService;

	@Autowired
	SubDashboardService subDashboardService;

	@RequestMapping(value = "/getSubDashboards", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DashboardReport> getsubDashboardsForDashboard(
			@RequestBody DashboardDetailVO dashboardDetailVO) throws Exception {
		DashboardReport subDashboards = null;
		if (null != dashboardDetailVO) {
			subDashboards = subDashboardService.getSubDashboardsForDashboard(dashboardDetailVO.getDashboardId());
			return new ResponseEntity<DashboardReport>(subDashboards, HttpStatus.OK);
		}
		return new ResponseEntity<DashboardReport>(subDashboards, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/getSpecificSubDashboard", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SubDashboardReport> getSpecificSubDashboard(@RequestBody DashboardDetailVO dashboardDetailVO)
			throws Exception {
		SubDashboardReport subDashboard = null;
		if (null != dashboardDetailVO) {
			subDashboard = subDashboardService.getSpecificSubDashboard(dashboardDetailVO);
			return new ResponseEntity<SubDashboardReport>(subDashboard, HttpStatus.OK);
		}
		return new ResponseEntity<SubDashboardReport>(subDashboard, HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "/addDashboard", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DashboardReport> addDashboard(@RequestBody DashboardVO dashboardVO)
			throws Exception {
		DashboardReport dashboardReport = null;
		if (null != dashboardVO) {
			dashboardReport=dashboardService.addDashboard(dashboardVO);
			return new ResponseEntity<DashboardReport>(dashboardReport, HttpStatus.OK);
		}
		return new ResponseEntity<DashboardReport>(dashboardReport, HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "/updateDashboard", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DashboardReport> updateDashboard(@RequestBody DashboardVO dashboardVO)
			throws Exception {
		DashboardReport dashboardReport = null;
		if (null != dashboardVO) {
			dashboardReport=dashboardService.updateDashboard(dashboardVO);
			return new ResponseEntity<DashboardReport>(dashboardReport, HttpStatus.OK);
		}
		return new ResponseEntity<DashboardReport>(dashboardReport, HttpStatus.BAD_REQUEST);
	}
	
	
	@RequestMapping(value = "/deleteDashboard", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DashboardReport> deleteDashboard(@RequestBody DashboardVO dashboardVO)
			throws Exception {
		DashboardReport dashboardReport = new DashboardReport();
		if (null != dashboardVO) {
			dashboardReport.setId(dashboardVO.getId());
			dashboardService.deleteDashboard(dashboardVO);
			return new ResponseEntity<DashboardReport>(dashboardReport, HttpStatus.OK);
		}
		return new ResponseEntity<DashboardReport>(dashboardReport, HttpStatus.BAD_REQUEST);
	}
}
