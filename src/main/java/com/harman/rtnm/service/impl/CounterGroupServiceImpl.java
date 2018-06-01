package com.harman.rtnm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.harman.rtnm.common.constant.Constant;
import com.harman.rtnm.common.constant.ConstantCollection;
import com.harman.rtnm.dao.CounterGroupDao;
import com.harman.rtnm.model.CounterGroup;
import com.harman.rtnm.model.Properties;
import com.harman.rtnm.service.CounterGroupService;
import com.harman.rtnm.service.ElementService;
import com.harman.rtnm.service.PropertiesService;
import com.harman.rtnm.vo.InventoryDetailVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class CounterGroupServiceImpl implements CounterGroupService {

	@Autowired
	CounterGroupDao counterGroupDao;

	@Autowired
	PropertiesService propertiesService;

	@Autowired
	ElementService elementService;

	ObjectMapper mapper = new ObjectMapper();

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<CounterGroup> getCountersByCounterGroupIds(List<CounterGroup> counterGroupList,
			InventoryDetailVO inventoryDetailVO) throws Exception {
		
		List<CounterGroup> counterGrpListCounter = counterGroupDao.getCountersAndKpisByCounterGroupId(counterGroupList);
		List<CounterGroup> counterGrpListProprty = propertiesService
				.getPropertiesByCounterGroupIds(counterGrpListCounter);

		// filter on the basis of profile data access EE, ESN(EMobile) or ALL
		List<String> nodes = new ArrayList<>();
		if (ConstantCollection.nodeNameProprtyWithdeviceType.containsKey(inventoryDetailVO.getDeviceType())) {
			String dataAccess = null;
			if (null != inventoryDetailVO.getProfile().getDataAccess()
					&& inventoryDetailVO.getProfile().getDataAccess().equalsIgnoreCase("Emobile")) {
				dataAccess = Constant.EE_MOBILE;
			} else if (null != inventoryDetailVO.getProfile().getDataAccess()
					&& inventoryDetailVO.getProfile().getDataAccess().equalsIgnoreCase(Constant.ALL)) {
				dataAccess = null;
			} else {
				dataAccess = "EE";
			}
			nodes = elementService.getNodesWithDeviceTypeAndNetworkName(inventoryDetailVO.getDeviceType(), dataAccess);
		}

		if (ConstantCollection.nodeNameProprtyWithdeviceType.containsKey(inventoryDetailVO.getDeviceType())) {
			List<String> nodes1 = nodes;
			counterGrpListProprty.stream().forEach(cg -> {
				String propertyname = ConstantCollection.nodeNameProprtyWithdeviceType
						.get(inventoryDetailVO.getDeviceType());
				if (null != cg.getProperties() && !cg.getProperties().isEmpty()) {
					Properties property = cg.getProperties().stream()
							.filter(prop -> prop.getPropertyName().equals(propertyname)).findFirst().get();
					property.setPropertyValues("");
					Map<String, List<String>> propValues = (Map<String, List<String>>) new HashMap();
					propValues.put("values", nodes1);
					String jsonValue = null;
					try {
						jsonValue = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(propValues);
					} catch (JsonProcessingException e) {
						// write logger
					}
					property.setPropertyValues(jsonValue);
				}
			});
		}
		List<CounterGroup> counterGrpList = new ArrayList<>();

		counterGrpListProprty.stream().forEach(cgProp -> {

			CounterGroup cntrGrp = counterGrpListCounter.stream()
					.filter(cgp -> cgp.getCounterGroupId().equals(cgProp.getCounterGroupId())).findFirst().get();
			if (null != cgProp.getProperties() && !cgProp.getProperties().isEmpty())
				cntrGrp.setProperties(cgProp.getProperties());

			counterGrpListCounter.stream()
					.filter(ctrGrp -> ctrGrp.getCounterGroupId().equalsIgnoreCase(cgProp.getCounterGroupId()))
					.findFirst().ifPresent(cgvo -> {
						counterGrpListCounter.remove(cgvo);
					});

			counterGrpListCounter.add(cntrGrp);
		});
		return counterGrpListCounter;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<CounterGroup> getCounterKpiPropertyWithCounterGroup() throws Exception {

		return null;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<CounterGroup> getCounterGroupListByIds(List<String> counterGroup) throws Exception {
		return counterGroupDao.getCounterGroupListByIds(counterGroup);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<CounterGroup> getCounterByDeviceType(String deviceType) throws Exception {
		return counterGroupDao.getCounterbyDevicetype(deviceType);
	}

	@Override
	public List<CounterGroup> getCounterGroupsByDeviceType(String deviceType) throws Exception {
		return counterGroupDao.getCounterGroupsByDeviceType(deviceType);
	}

	@Override
	public CounterGroup getCounterGroupById(String groupID) throws Exception {
		return counterGroupDao.getCounterGroupById(groupID);
	}

}
