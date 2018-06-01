package com.harman.rtnm.service;

import java.util.List;

import com.harman.rtnm.model.UserDetail;
import com.harman.rtnm.model.UserTemplate;
import com.harman.rtnm.vo.CounterGroupVO;
import com.harman.rtnm.vo.InventoryDetailVO;
import com.harman.rtnm.vo.UserTemplateVO;

public interface InventoryService {

	
	public List<CounterGroupVO> getKpisByCounterGroupIds(InventoryDetailVO inventoryDetailVO) throws Exception ;
	
	public List<UserTemplateVO> reportsDetails(UserDetail userDetail) throws Exception;
}
