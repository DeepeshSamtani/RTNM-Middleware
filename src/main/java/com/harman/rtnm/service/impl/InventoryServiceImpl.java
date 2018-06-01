package com.harman.rtnm.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.harman.rtnm.common.constant.Constant;
import com.harman.rtnm.model.CommonKPI;
import com.harman.rtnm.model.CounterGroup;
import com.harman.rtnm.model.KPI;
import com.harman.rtnm.model.UserDetail;
import com.harman.rtnm.model.UserTemplate;
import com.harman.rtnm.service.CounterGroupService;
import com.harman.rtnm.service.ElementService;
import com.harman.rtnm.service.InventoryService;
import com.harman.rtnm.service.KpiService;
import com.harman.rtnm.service.PropertiesService;
import com.harman.rtnm.service.UserTemplateService;
import com.harman.rtnm.vo.CounterGroupVO;
import com.harman.rtnm.vo.InventoryDetailVO;
import com.harman.rtnm.vo.UserTemplateVO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {

	@Autowired
	CounterGroupService counterGroupService;

	@Autowired
	KpiService kpiService;

	@Autowired
	ElementService elementService;

	@Autowired
	PropertiesService propertiesService;
	
	@Autowired
	UserTemplateService userTemplateService;

	ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public List<CounterGroupVO> getKpisByCounterGroupIds(InventoryDetailVO inventoryDetailVO) throws Exception {
		List<CounterGroup> counterGroupList = new ArrayList();
		List<CounterGroupVO> counterGrpVoList = new ArrayList();
		if (null != inventoryDetailVO && null != inventoryDetailVO.getDeviceType()
				&& !inventoryDetailVO.getDeviceType().isEmpty()) {
			counterGroupList = counterGroupService.getCounterGroupsByDeviceType(inventoryDetailVO.getDeviceType());
			counterGroupList = counterGroupService.getCountersByCounterGroupIds(counterGroupList, inventoryDetailVO);
		}

		List<CounterGroup> counterGrps = new ArrayList();
		List<KPI> kpiList = kpiService.getActiveKpiByDeviceType(inventoryDetailVO.getDeviceType());
		if (kpiList.isEmpty()) {
			return counterGrpVoList;
		}
		kpiList.stream().forEach(kpi -> {
			CounterGroup cggrp = new CounterGroup();
			cggrp.setCounterGroupId(kpi.getCounterGroupId());
			counterGrps.add(cggrp);
		});
		List<CounterGroup> dynamicKpiCounterGrpDetails = propertiesService.getPropertiesByCounterGroupIds(counterGrps);// counterGroupService.getCounterGroupListByIds(counterGrps);

		return counterGroupVowithPredefinedAndDynamicKpi(counterGroupList, counterGrpVoList, kpiList,
				dynamicKpiCounterGrpDetails);
	}

	private List<CounterGroupVO> counterGroupVowithPredefinedAndDynamicKpi(List<CounterGroup> counterGroupList,
			List<CounterGroupVO> counterGrpVoList, List<KPI> kpiList, List<CounterGroup> dynamicKpiCounterGrpDetails) {
		if (null != counterGroupList || null != dynamicKpiCounterGrpDetails) {

			counterGroupList.stream().forEach(cg -> {
				CounterGroupVO cgvo = new CounterGroupVO();
				cgvo.setCounterGroupId(cg.getCounterGroupId());
				cgvo.setCounterGroupDetails(cg.getCounterGroupDetails());
				cgvo.setDisplayName(cg.getCounterGroupName());
				if (null != cg.getProperties())
					cgvo.setProperties(cg.getProperties());

				List<CommonKPI> commonKpiList = new ArrayList();
				if (null != cg.getCounterList() && cg.getCounterList().size() > 0) {
					cg.getCounterList().stream().forEach(preDefinedKpi -> {
						CommonKPI kpi = new CommonKPI();
						kpi.setId(preDefinedKpi.getCounterKey().getCounterId());
						kpi.setDisplayName(preDefinedKpi.getLogicalName());
						//kpi.setUnit(preDefinedKpi.getCounterUnit());
						//kpi.setDescription(preDefinedKpi.getCounterDescription());
						kpi.setKpiType(Constant.DERIVED);
						commonKpiList.add(kpi);
					});
				}
				cgvo.setCommonKpiList(commonKpiList);
				if (null != cgvo.getCommonKpiList() && !cgvo.getCommonKpiList().isEmpty()) {
					counterGrpVoList.add(cgvo);
				}
			});

			dynamicKpiCounterGrpDetails.stream().forEach(cgs -> {
				if (counterGrpVoList.size() == 0) {
					CounterGroupVO cgvo = new CounterGroupVO();
					cgvo.setCounterGroupId(cgs.getCounterGroupId());
					//cgvo.setCounterGroupDetails(cgs.getCounterGroupDetails());
					cgvo.setDisplayName(cgs.getCounterGroupName());
					if (null != cgs.getProperties())
						cgvo.setProperties(cgs.getProperties());
					counterGrpVoList.add(cgvo);
				} else {
					List<String> listCGId = counterGrpVoList.stream().filter(Objects::nonNull)
							.map(CounterGroupVO::getCounterGroupId).filter(Objects::nonNull)
							.filter(id -> id.equals(cgs.getCounterGroupId())).collect(Collectors.toList());
					if (null != listCGId && listCGId.size() == 0) {
						CounterGroupVO cgvo = new CounterGroupVO();
						cgvo.setCounterGroupId(cgs.getCounterGroupId());
						//cgvo.setCounterGroupDetails(cgs.getCounterGroupDetails());
						cgvo.setDisplayName(cgs.getCounterGroupName());
						if (null != cgs.getProperties())
							cgvo.setProperties(cgs.getProperties());
						counterGrpVoList.add(cgvo);
					}
				}
			});

			kpiList.stream().forEach(kpi -> {

				List<String> listCGId = counterGrpVoList.stream().filter(Objects::nonNull)
						.map(CounterGroupVO::getCounterGroupId).filter(Objects::nonNull)
						.filter(id -> id.toString().equals(kpi.getCounterGroupId().toString()))
						.collect(Collectors.toList());

				if (listCGId.size() > 0) {

					CounterGroupVO counterGrpObj = counterGrpVoList.stream()
							.filter(x -> x.getCounterGroupId().equals(kpi.getCounterGroupId())).findFirst().get();

					List<CommonKPI> kpiListOfCg = new ArrayList<>();
					kpiListOfCg = counterGrpObj.getCommonKpiList();
					CommonKPI commnKpi = new CommonKPI();
					commnKpi.setId(String.valueOf(kpi.getId()));
					commnKpi.setDisplayName(kpi.getDisplayName());
					//commnKpi.setUnit(kpi.getUnit());
					//commnKpi.setDescription(kpi.getDescription());
					commnKpi.setKpiType(Constant.DYNAMIC);

					if (kpiListOfCg == null) {
						kpiListOfCg = new ArrayList<>();
					}
					kpiListOfCg.add(commnKpi);

					counterGrpObj.setCommonKpiList(kpiListOfCg);

					counterGrpVoList.stream().filter(ctr -> ctr.getCounterGroupId().equals(kpi.getCounterGroupId()))
							.findFirst().ifPresent(cgvo -> {
								counterGrpVoList.remove(cgvo);
							});

					counterGrpVoList.add(counterGrpObj);
				}
			});
		}
		return counterGrpVoList;
	}
	
	
	public List<UserTemplateVO> reportsDetails(UserDetail userDetail) throws Exception{
		List<UserTemplateVO> userTemplateVOs = new ArrayList<>();
		List<UserTemplate> userTemplates = userTemplateService.getUserTemplateByUserId(userDetail.getUserId());
		userTemplates.stream().forEach(ut->{
			UserTemplateVO userTemplateVO = new UserTemplateVO();
			userTemplateVO.setUserTemplateId(ut.getUserTemplateKey().getUsertemplateId());
			userTemplateVO.setReportName(ut.getReportName());
			userTemplateVO.setUserDetail(new UserDetail());
			userTemplateVO.getUserDetail().setUserId(ut.getUserTemplateKey().getUserDetail().getUserId());
			userTemplateVO.getUserDetail().setUserName(ut.getUserTemplateKey().getUserDetail().getUserName());
			
			Map<String, Object> reportDetailMap=null;
				try {
					reportDetailMap = objectMapper.readValue(ut.getReportDetail(),
							new TypeReference<Map<String, Object>>() {});
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			userTemplateVO.setReportConfiguration(reportDetailMap);
			userTemplateVOs.add(userTemplateVO);
		});
		return userTemplateVOs;
	}
	
}
