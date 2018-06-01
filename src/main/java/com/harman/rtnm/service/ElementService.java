package com.harman.rtnm.service;

import java.util.List;

import com.harman.rtnm.model.CounterGroup;
import com.harman.rtnm.model.Element;

public interface ElementService {

	List<Element> getElements()throws Exception;
	
	Element getElementById(Element element)throws Exception;
	
	public List<CounterGroup> getcountergroups(List<Element> elements) throws Exception ;
	
	List<String> getDeviceTypes(String profile)throws Exception;
	
	public List<Element> getElementsByDeviceType(String dviceType)throws Exception;
	
	List<String> getDisplayNamesByDeviceType(String deviceType) throws Exception;
	
	List<String> getNodesWithDeviceTypeAndNetworkName(String deviceType, String networkName) throws Exception ;

}
