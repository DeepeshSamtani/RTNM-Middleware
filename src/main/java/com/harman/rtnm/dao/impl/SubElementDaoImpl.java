package com.harman.rtnm.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.harman.rtnm.dao.AbstractDAO;
import com.harman.rtnm.dao.SubElementDao;
import com.harman.rtnm.model.CounterGroup;
import com.harman.rtnm.model.Element;
import com.harman.rtnm.model.SubElement;
import com.harman.rtnm.vo.SubElementVO;

@Repository
public class SubElementDaoImpl extends AbstractDAO<SubElement> implements SubElementDao {

	@Override
	public List<SubElementVO> getSubElementsByCounterGrps(List<CounterGroup> counterGroupList, List<Element> elements)
			throws Exception {

		StringBuilder selectQuery = new StringBuilder();
		StringBuilder whereQuery = new StringBuilder();
		List<SubElement> subElementList = new ArrayList<>();
		selectQuery.append(" from SubElement sub where ");
		whereQuery.append("sub.element.elementID IN ( ");
		for (int i = 0; i < elements.size(); i++) {
			if (null != elements.get(i) && null != elements.get(i).getElementID()) {
				if (i == (elements.size() - 1)) {
					whereQuery.append(elements.get(i).getElementID() + ") and ");
				} else {
					whereQuery.append(elements.get(i).getElementID() + ",");
				}
			} else
				throw new NullPointerException(" ElementID can not be Null...");
		}

		whereQuery.append("sub.subElementKey.counterGroup.counterGroupId IN (");
		for (int i = 0; i < counterGroupList.size(); i++) {
			if (null != counterGroupList.get(i) && null != counterGroupList.get(i).getCounterGroupId()) {
				if (i == (counterGroupList.size() - 1)) {
					whereQuery.append(counterGroupList.get(i).getCounterGroupId()
							+ ") group by sub.subElementKey.subElementName");
				} else {
					whereQuery.append(counterGroupList.get(i).getCounterGroupId() + ",");
				}
			} else
				throw new NullPointerException(" CounterId can not be Null...");
		}

		selectQuery.append(whereQuery);
		subElementList = (List<SubElement>) executeQuery(selectQuery.toString());

		List<SubElementVO> subElementVOList = new ArrayList<>();
		
		subElementList.forEach(se -> {
			SubElementVO subElementVO = new SubElementVO();
			subElementVO.setDisplayName(se.getSubElementKey().getSubElementName());
			subElementVOList.add(subElementVO);
		});

		return subElementVOList;
	}

}