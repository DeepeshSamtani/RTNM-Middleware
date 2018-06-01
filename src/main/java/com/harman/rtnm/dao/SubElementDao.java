package com.harman.rtnm.dao;

import java.util.List;

import com.harman.rtnm.model.CounterGroup;
import com.harman.rtnm.model.Element;
import com.harman.rtnm.vo.SubElementVO;

public interface SubElementDao {

	public List<SubElementVO> getSubElementsByCounterGrps(List<CounterGroup> counterGroupList, List<Element> elements) throws Exception ;
	
}
