package com.harman.rtnm.service;

import java.util.List;

import com.harman.rtnm.model.CounterGroup;
import com.harman.rtnm.model.Element;
import com.harman.rtnm.vo.SubElementVO;

public interface SubElementService {

	public List<SubElementVO> getSubElementsByCounterGrps(List<CounterGroup> counterGroupList, List<Element> elements)throws Exception;
	
}
