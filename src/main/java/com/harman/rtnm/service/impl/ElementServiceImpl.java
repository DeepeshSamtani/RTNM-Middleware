package com.harman.rtnm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.harman.rtnm.dao.CounterGroupDao;
import com.harman.rtnm.dao.ElementDao;
import com.harman.rtnm.model.CounterGroup;
import com.harman.rtnm.model.Element;
import com.harman.rtnm.service.ElementService;

@Service
@Transactional
public class ElementServiceImpl implements ElementService {

	@Autowired
	ElementDao elementDao;

	@Autowired
	CounterGroupDao counterGroupDao;

	@Override
	public List<Element> getElements() throws Exception {
		return elementDao.getElements();
	}

	@Override
	public Element getElementById(Element element) throws Exception {
		return elementDao.getElementById(element);
	}

	@Override
	public List<CounterGroup> getcountergroups(List<Element> elements) throws Exception {
		return counterGroupDao.getCounterGroupListByElementId(elements);
	}

	@Override
	public List<String> getDeviceTypes(String profile) throws Exception {
		return elementDao.getAllDeviceTypes(profile);
	}

	@Override
	public List<Element> getElementsByDeviceType(String deviceType) throws Exception {
		return elementDao.getElementsByDeviceType(deviceType);
	}

	@Override
	public List<String> getDisplayNamesByDeviceType(String deviceType) throws Exception {
		return elementDao.getDisplayNames(deviceType);
	}

	@Override
	public List<String> getNodesWithDeviceTypeAndNetworkName(String deviceType, String networkName) throws Exception {
		return elementDao.getNodesWithDeviceTypeAndNetworkName(deviceType, networkName);
	}

}
