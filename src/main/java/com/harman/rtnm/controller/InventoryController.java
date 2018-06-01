package com.harman.rtnm.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.harman.rtnm.samsung.commonutils.util.StringUtils;
import com.harman.rtnm.model.CounterGroup;
import com.harman.rtnm.model.CounterIdRange;
import com.harman.rtnm.model.CounterKey;
import com.harman.rtnm.model.UserDetail;
import com.harman.rtnm.service.CounterGroupService;
import com.harman.rtnm.service.CounterService;
import com.harman.rtnm.service.ElementService;
import com.harman.rtnm.service.InventoryService;
import com.harman.rtnm.service.KpiService;
import com.harman.rtnm.service.SubElementService;
import com.harman.rtnm.service.UserDetailService;
import com.harman.rtnm.service.UtilService;
import com.harman.rtnm.vo.DynamicCounterVO;
import com.harman.rtnm.vo.InventoryDetailVO;
import com.harman.rtnm.vo.UserTemplateVO;

@EnableAspectJAutoProxy(proxyTargetClass = true)
@RequestMapping(value = "/inventory")
@RestController
public class InventoryController {

	@Autowired
	ElementService elementService;

	@Autowired
	CounterGroupService counterGroupService;

	@Autowired
	SubElementService subElementService;

	@Autowired
	KpiService kpiService;

	@Autowired
	CounterService counterService;

	@Autowired
	InventoryService inventoryService;

	@Autowired
	private UtilService utilService;

	@Autowired
	private UserDetailService userDetailService ;

	@RequestMapping(value = "/devicetypes", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> getDeviceTypes(@RequestBody InventoryDetailVO inventoryDetailVO) throws Exception {
		List<String> deviceTypeList = elementService.getDeviceTypes(inventoryDetailVO.getProfile().getProfileName());
		return new ResponseEntity<List<String>>(deviceTypeList, HttpStatus.OK);
	}

	/*@RequestMapping(value = "/getelementsbyelementtype", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Element>> getElementsByDeviceType(@RequestBody InventoryDetailVO inventoryDetailVO)
			throws Exception {
		List<Element> elements = null;
		if (null != inventoryDetailVO) {
			elements = elementService.getElementsByDeviceType(inventoryDetailVO.getDeviceType());
			return new ResponseEntity<List<Element>>(elements, HttpStatus.OK);
		}
		return new ResponseEntity<List<Element>>(elements, HttpStatus.BAD_REQUEST);
	}*/

	/*@RequestMapping(value = "/getcountergroupsbyelementids", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CounterGroup>> getCounterGroupByElement(@RequestBody InventoryDetailVO inventoryDetailVO)
			throws Exception {
		List<CounterGroup> counterGroupList = null;
		if (null != inventoryDetailVO) {
			List<Element> elements = null;
			if (null != inventoryDetailVO.getElements() && !inventoryDetailVO.getElements().isEmpty()) {
				elements = inventoryDetailVO.getElements();
			} else if (inventoryDetailVO.isAllNodeFlag()
					&& (null != inventoryDetailVO.getDeviceType() && !inventoryDetailVO.getDeviceType().isEmpty())) {
				elements = elementService.getElementsByDeviceType(inventoryDetailVO.getDeviceType());
			}
			counterGroupList = elementService.getcountergroups(elements);
			return new ResponseEntity<List<CounterGroup>>(counterGroupList, HttpStatus.OK);
		}
		return new ResponseEntity<List<CounterGroup>>(counterGroupList, HttpStatus.BAD_REQUEST);
	}*/

	@RequestMapping(value = "/getcountergroupsbydevicetype", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CounterGroup>> getCounterGroupsByDeviceType(@RequestBody InventoryDetailVO inventoryDetailVO)
			throws Exception {
		List<CounterGroup> counterGroupList = null;
		if (null != inventoryDetailVO) {
			String deviceType=null;
			if (null!=inventoryDetailVO.getDeviceType() && !inventoryDetailVO.getDeviceType().trim().isEmpty()) {
				deviceType=inventoryDetailVO.getDeviceType().trim();
			}
			counterGroupList=counterGroupService.getCounterGroupsByDeviceType(deviceType);			
			return new ResponseEntity<List<CounterGroup>>(counterGroupList, HttpStatus.OK);
		}
		return new ResponseEntity<List<CounterGroup>>(counterGroupList, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/getcountersbycountergroups", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CounterGroup>> getCountersByCounterGroupIds(
			@RequestBody InventoryDetailVO inventoryDetailVO) throws Exception {
		List<CounterGroup> counterGroupList = null;
		if (null != inventoryDetailVO) {
			counterGroupList = inventoryDetailVO.getCounterGroups();
			counterGroupList = counterGroupService.getCountersByCounterGroupIds(counterGroupList, inventoryDetailVO);
			return new ResponseEntity<List<CounterGroup>>(counterGroupList, HttpStatus.OK);
		}
		return new ResponseEntity<List<CounterGroup>>(counterGroupList, HttpStatus.BAD_REQUEST);
	}

	/*@RequestMapping(value="/getcounterbydevicetype", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CounterGroup>> getCounterByDeviceType(@RequestParam String deviceType) throws Exception {
		List<CounterGroup> counterGroups=null;
		if(null!=deviceType){
			counterGroups=counterGroupService.getCounterByDeviceType(deviceType);
			System.out.println(counterGroups);
			return new ResponseEntity<List<CounterGroup>>(counterGroups,HttpStatus.OK);
		}
		return new ResponseEntity<List<CounterGroup>>(counterGroups,HttpStatus.BAD_REQUEST);
	}*/

	/*@RequestMapping(value = "/getLevelByDeviceType", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> getDisplayNamesByDeviceType(@RequestBody InventoryDetailVO inventoryDetailVO)
			throws Exception {
		List<String> elements = null;
		if (null != inventoryDetailVO) {
			elements = elementService.getDisplayNamesByDeviceType(inventoryDetailVO.getDeviceType());
			return new ResponseEntity<List<String>>(elements, HttpStatus.OK);
		}
		return new ResponseEntity<List<String>>(elements, HttpStatus.BAD_REQUEST);
	}*/

	/*@RequestMapping(value = "/kpilistwithcountergroup", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CounterGroupVO>> getKpisByCounterGroupIds(
			@RequestBody InventoryDetailVO inventoryDetailVO) throws Exception {

		List<CounterGroupVO> cgs = new ArrayList<>();
		cgs = inventoryService.getKpisByCounterGroupIds(inventoryDetailVO);
		if (null != cgs)
			return new ResponseEntity<List<CounterGroupVO>>(cgs, HttpStatus.OK);
		else
			return new ResponseEntity<List<CounterGroupVO>>(cgs, HttpStatus.OK);
	}*/

	/**
	 * Fetch the detials of a Kpi along with the mapping (counterId,names)
	 * @param kpiid
	 * @return
	 */
	@RequestMapping(value = "/getkpidetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DynamicCounterVO> getKPIDetails(@RequestBody DynamicCounterVO kpiId) {
		System.out.println(" KPI ID : " + (kpiId == null));
		try {
			if(kpiId == null || StringUtils.isNullOrEmpty(kpiId.getCounterId())) {
				return new ResponseEntity<DynamicCounterVO>(HttpStatus.BAD_REQUEST);
			} else {
				CounterKey key = new CounterKey(kpiId.getCounterId(), kpiId.getGroupId());
				System.out.println(" counter id : " + kpiId.getCounterId() + " groupid : " + kpiId.getGroupId());
				DynamicCounterVO kpiDetials = counterService.getKPIDetails(key);
				if(kpiDetials == null) {
					return new ResponseEntity<DynamicCounterVO>(HttpStatus.NOT_FOUND);
				} 

				return new ResponseEntity<DynamicCounterVO>(kpiDetials, HttpStatus.OK);				
			}
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<DynamicCounterVO>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/saveCounter", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> saveDynamicCounter(@RequestBody InventoryDetailVO inventoryDetailVO) throws Exception {

		String	cgs = counterService.saveDynamicCounter(inventoryDetailVO);
		if (null != cgs)
			return new ResponseEntity<String>(cgs, HttpStatus.OK);
		else
			return new ResponseEntity<String>(cgs, HttpStatus.OK);
	}

	@RequestMapping(value = "/isCounterIdNeed", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> deviceExist(@RequestBody CounterIdRange counterIdRange) throws Exception{
		return new ResponseEntity<Boolean>(utilService.deviceExist(counterIdRange.getDeviceType()),HttpStatus.OK);
	}	

	@RequestMapping(value = "/userReports", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserTemplateVO>> userReportsDetails(@RequestBody InventoryDetailVO inventoryDetailVO) throws Exception{
		UserDetail userDetail = null;
		List<UserTemplateVO> userTemplateVOs = null;
		if (null != inventoryDetailVO.getUserName()) {
			userTemplateVOs = new ArrayList<>();
			userDetail = userDetailService.loadUserByName(inventoryDetailVO.getUserName());
			userTemplateVOs = inventoryService.reportsDetails(userDetail);
			return new ResponseEntity<List<UserTemplateVO>>(userTemplateVOs, HttpStatus.OK);
		}else
			return new ResponseEntity<List<UserTemplateVO>>(userTemplateVOs,HttpStatus.BAD_REQUEST);
	}

}
