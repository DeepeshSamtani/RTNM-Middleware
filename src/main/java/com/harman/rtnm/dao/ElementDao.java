package com.harman.rtnm.dao;

import java.util.List;
import java.util.Set;

import com.harman.rtnm.model.CounterGroup;
import com.harman.rtnm.model.Element;

public interface ElementDao {

	List<Element> getElements();
	
	Element getElementById(Element element);	
	
	public List<String> getAllDeviceTypes(String profile) throws Exception;
	
	public  List<Element>  getElementsByDeviceType(String deviceType) throws Exception;
	
	List<String> getDisplayNames(String deviceType) throws Exception;
	/*Set<CounterGroup> getCounterGroupSetByElementId(Element element, Set<CounterGroup> counterGroup);*/
	
	public List<String> getNodesWithDeviceTypeAndNetworkName(String deviceType, String networkName) throws Exception ;
}
