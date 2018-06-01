package com.harman.rtnm.dao.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.harman.rtnm.common.constant.Constant;
import com.harman.rtnm.dao.AbstractDAO;
import com.harman.rtnm.dao.ElementDao;
import com.harman.rtnm.model.Element;

@Repository
public class ElementDaoImpl extends AbstractDAO<Element> implements ElementDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2368659338919656138L;

	@SuppressWarnings("unchecked")
	@Override
	public List<Element> getElements() {
		return (List<Element>) loadClass(Element.class);
	}

	@Override
	public Element getElementById(Element element) {
		element = getRecordById(Element.class, element.getElementID());
		return element;
	}

	@Override
	public List<String> getAllDeviceTypes(String profile) throws Exception {

		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("select e.deviceType from Element e ");
		StringBuilder whereQuery = new StringBuilder();
		if (null != profile && profile.equalsIgnoreCase(Constant.EE_MOBILE))
			whereQuery.append(" where e.networkName = '" + Constant.EE_MOBILE + "'");
		else if (null != profile && !profile.equalsIgnoreCase(Constant.EE_MOBILE)) {
			whereQuery.append(" where e.networkName != '" + Constant.EE_MOBILE + "'");
		}
		whereQuery.append("  group by  e.deviceType");
		selectQuery.append(whereQuery);

		List<Element> elements = (List<Element>) executeQuery(selectQuery.toString());
		List<String> deviceTypes = null;
		if (null != elements && !elements.isEmpty()) {
			deviceTypes = new ArrayList<>();
			for (Object row : elements) {
				if (row instanceof String) {
					String deviceType = (String) row;
					deviceTypes.add(deviceType);
				}
			}
		}
		return deviceTypes;
	}

	@Override
	public List<Element> getElementsByDeviceType(String deviceType) throws Exception {

		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("select e.elementID,  e.elementUserLabel, e.vendor, e.universe, e.networkName, "
				+ "e.neVersion,  e.deviceType" + " from Element e ");
		StringBuilder whereQuery = new StringBuilder();
		whereQuery.append("  where e.deviceType = ");
		if (null != deviceType && !deviceType.isEmpty())
			whereQuery.append("'" + deviceType + "'");
		else {
			throw new Exception(" DeviceType can not be Null.");
		}
		selectQuery.append(whereQuery);
		List<Element> elements = (List<Element>) executeQuery(selectQuery.toString());
		List<Element> elementlist = new LinkedList<>();

		for (Object row : elements) {
			if (row instanceof Object[]) {
				Object[] attribute = (Object[]) row;
				Element element1 = new Element();
				element1.setElementID(Long.valueOf(String.valueOf(attribute[0])));
				/*
				 * if( ConstantCollection.deviceTypeList.stream().anyMatch(dt ->
				 * dt.equalsIgnoreCase(deviceType.toUpperCase()))){ String
				 * nodeName = String.valueOf(attribute[1]); if (nodeName != null
				 * && nodeName.length() > 1) { nodeName = nodeName.substring(0,
				 * nodeName.length() - 1); }
				 * element1.setElementUserLabel(nodeName); } else
				 */
				element1.setElementUserLabel(String.valueOf(attribute[1]));
				// element1.setVendor(String.valueOf(attribute[2]));
				// element1.setUniverse(String.valueOf(attribute[3]));
				// element1.setNetworkName(String.valueOf(attribute[4]));
				// element1.setNeVersion(String.valueOf(attribute[5]));
				// element1.setDeviceType(String.valueOf(attribute[6]));
				elementlist.add(element1);
			}
		}
		return elementlist;
	}

	@Override
	public List<String> getDisplayNames(String deviceType) throws Exception {

		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("select e.displayName from UIMetaData e");
		StringBuilder whereQuery = new StringBuilder();
		whereQuery.append("  where e.deviceType = ");

		if (null != deviceType && !deviceType.isEmpty())
			whereQuery.append("'" + deviceType + "'");
		else {
			throw new Exception(" DeviceType can not be Null ");
		}

		selectQuery.append(whereQuery);
		List<Element> elements = (List<Element>) execcuteSQLQuery(selectQuery.toString());
		List<String> listOfDisplayNames = null;
		if (null != elements && !elements.isEmpty()) {
			listOfDisplayNames = new ArrayList<>();
			for (Object row : elements) {
				if (row instanceof String) {
					String displayName = (String) row;
					listOfDisplayNames.add(displayName);
				}
			}
		}
		return listOfDisplayNames;
	}

	@Override
	public List<String> getNodesWithDeviceTypeAndNetworkName(String deviceType, String networkName) throws Exception {

		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("select e.elementUserLabel from Element e where e.deviceType = '" + deviceType + "'");

		if (null != networkName && !networkName.isEmpty() && networkName.equalsIgnoreCase(Constant.EE_MOBILE)) {
			selectQuery.append("and e.networkName = '" + Constant.EE_MOBILE + "'");
		}else if(null != networkName && !networkName.isEmpty() && !networkName.equals(Constant.ALL) && !networkName.equalsIgnoreCase(Constant.EE_MOBILE)){
			selectQuery.append("and e.networkName != '" + Constant.EE_MOBILE + "'");
		}

		List<Element> elements = executeQuery(selectQuery.toString());
		List<String> nodes = new ArrayList();
		if (null != elements && !elements.isEmpty()) {
			nodes = new ArrayList<>();
			for (Object row : elements) {
				if (row instanceof String) {
					String node = (String) row;
					nodes.add(node);
				}
			}
		}
		return nodes;
	}
}
