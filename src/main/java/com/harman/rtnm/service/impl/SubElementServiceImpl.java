package com.harman.rtnm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.harman.rtnm.dao.SubElementDao;
import com.harman.rtnm.model.CounterGroup;
import com.harman.rtnm.model.Element;
import com.harman.rtnm.service.SubElementService;
import com.harman.rtnm.vo.SubElementVO;

@Service
@Transactional
public class SubElementServiceImpl implements SubElementService {

	@Autowired
	SubElementDao subElementDao;
	
	@Override
	public List<SubElementVO> getSubElementsByCounterGrps(List<CounterGroup> counterGroupList, List<Element> elements)throws Exception {
		return subElementDao.getSubElementsByCounterGrps(counterGroupList,elements);		
	}

	
	
}
