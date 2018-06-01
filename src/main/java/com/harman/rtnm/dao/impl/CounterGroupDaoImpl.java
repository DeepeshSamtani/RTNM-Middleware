package com.harman.rtnm.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Repository;

import com.harman.rtnm.common.constant.Constant;
import com.harman.rtnm.dao.AbstractDAO;
import com.harman.rtnm.dao.CounterGroupDao;
import com.harman.rtnm.model.Counter;
import com.harman.rtnm.model.CounterGroup;
import com.harman.rtnm.model.CounterKey;
import com.harman.rtnm.model.Element;

@Repository
public class CounterGroupDaoImpl extends AbstractDAO<CounterGroup> implements CounterGroupDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1888916797402118110L;

	@Override
	public List<CounterGroup> getCountersByCounterGroupId(List<CounterGroup> counterGroupList, String counterType)
			throws Exception {

		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("select cg.counterGroupId, cg.counterGroupName, cg.counterGroupDetails, "
				+ "c.counterKey, c.logicalName, c.counterUnit, c.counterDescription, c.aggregation,"
				+ "c.counterType, c.enabled, c.cSource" + " from CounterGroup cg, Counter as c");

		StringBuilder whereQuery = new StringBuilder();
		whereQuery.append(" where cg.counterGroupId = c.counterKey.counterGroup.counterGroupId and ");
		// whereQuery.append(" c.counterType = '" + counterType + "' and ");
		whereQuery.append(" c.enabled = 1 " + " and ");
		whereQuery.append(" cg.counterGroupId in ( ");
		for (int i = 0; i < counterGroupList.size(); i++) {
			if (null != counterGroupList.get(i) && !counterGroupList.get(i).getCounterGroupId().toString().isEmpty())
				if (i == (counterGroupList.size() - 1))
					whereQuery.append("'" + counterGroupList.get(i).getCounterGroupId() + "'" + ")");
				else
					whereQuery.append("'" + counterGroupList.get(i).getCounterGroupId() + "'" + ",");
			else
				throw new Exception(" CounterGroupId can not be Null ");
		}

		selectQuery.append(whereQuery);

		List<CounterGroup> counterGpList = (List<CounterGroup>) executeQuery(selectQuery.toString());
		List<CounterGroup> countergplist = new ArrayList<>();
		Map<String, List<Counter>> counters = new HashMap<>();
		List<CounterGroup> counterGroupList2 = new ArrayList<>();

		for (Object row : counterGpList) {
			if (row instanceof Object[]) {
				Object[] attribute = (Object[]) row;

				if (counters.isEmpty() || (!counters.containsKey(String.valueOf(attribute[0])))) {
					counters.put(String.valueOf(attribute[0]), new ArrayList<Counter>());
					CounterGroup counterGroup = new CounterGroup();
					counterGroup.setCounterGroupId(String.valueOf(attribute[0]));
					counterGroup.setCounterGroupName(String.valueOf(attribute[1]));
					// counterGroup.setCounterGroupDetails(String.valueOf(attribute[2]));
					countergplist.add(counterGroup);
				}

				Counter counter = new Counter();
				counter.getCounterKey().setCounterId(String.valueOf(attribute[3]));
				counter.setLogicalName(String.valueOf(attribute[4]));
				// counter.setCounterUnit(String.valueOf(attribute[5]));
				// counter.setCounterDescription(String.valueOf(attribute[6]));
				// counter.setAggregation((Aggregation) attribute[7]);
				counter.setCounterType(String.valueOf(attribute[8]));
				// counter.setEnabled(Integer.valueOf(String.valueOf(attribute[9])));
				// counter.setcSource(String.valueOf(attribute[10]));

				if (!counters.isEmpty()) {
					List<Counter> countersList = counters.get(String.valueOf(attribute[0]));
					countersList.add(counter);

					counters.put(String.valueOf(attribute[0]), countersList);
				}
			}
		}

		counters.forEach((cgIdStr, counterList) -> {
			CounterGroup cntrGrp = countergplist.stream().filter(crtGrp -> crtGrp.getCounterGroupId().equals(cgIdStr))
					.findFirst().get();
			cntrGrp.setCounterList(counterList);
			counterGroupList2.add(cntrGrp);
		});
		counters.clear();
		countergplist.clear();
		counterGpList.clear();
		return counterGroupList2;
	}

	@Override
	public List<CounterGroup> getCounterGroupListByElementId(List<Element> elements) throws Exception {

		StringBuilder selectQuery = new StringBuilder();
		selectQuery
		.append("select cg.counterGroupId, cg.counterGroupName, cg.counterGroupDetails from CounterGroup cg ");

		StringBuilder whereQuery = new StringBuilder();
		whereQuery
		.append(", SubElement as se" + " where cg.counterGroupId = se.subElementKey.counterGroup.counterGroupId"
				+ " and se.element.elementID in ( ");
		for (int i = 0; i < elements.size(); i++) {
			if (null != elements.get(i).getElementID() && !String.valueOf(elements.get(i).getElementID()).isEmpty())
				if (i == (elements.size() - 1))
					whereQuery.append(elements.get(i).getElementID() + ")");
				else
					whereQuery.append(elements.get(i).getElementID() + ",");
			else
				throw new Exception(" ElementID can not be Null ");

		}

		whereQuery.append("  group by cg.counterGroupId");
		selectQuery.append(whereQuery);
		List<CounterGroup> counterGroupList = (List<CounterGroup>) executeQuery(selectQuery.toString());
		List<CounterGroup> countergplist = new LinkedList<>();

		for (Object row : counterGroupList) {
			if (row instanceof Object[]) {
				Object[] attribute = (Object[]) row;
				CounterGroup counterGroup = new CounterGroup();
				counterGroup.setCounterGroupId(String.valueOf(attribute[0]));
				counterGroup.setCounterGroupName(String.valueOf(attribute[1]));
				// counterGroup.setCounterGroupDetails(String.valueOf(attribute[2]));
				countergplist.add(counterGroup);
			}
		}
		return countergplist;
	}

	public List<CounterGroup> getCounterGroupListByIds(List<String> counterGroup) throws Exception {

		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("from CounterGroup cg ");

		StringBuilder whereQuery = new StringBuilder();
		whereQuery.append(" where cg.counterGroupId in ( ");

		for (int i = 0; i < counterGroup.size(); i++) {
			if (null != counterGroup.get(i) && !String.valueOf(counterGroup.get(i)).isEmpty())
				if (i == (counterGroup.size() - 1))
					whereQuery.append("'" + counterGroup.get(i) + "'" + ")");
				else
					whereQuery.append("'" + counterGroup.get(i) + "'" + ",");
			else
				throw new Exception(" CounterGroupId can not be Null ");
		}
		selectQuery.append(whereQuery);
		List<CounterGroup> counterGroupList = (List<CounterGroup>) executeQuery(selectQuery.toString());
		return counterGroupList;
	}

	@Override
	public List<CounterGroup> getCounterbyDevicetype(String deviceType) throws Exception {
		StringBuilder selectQuery = new StringBuilder();
		selectQuery
		.append("select COUNTER.COUNTER_ID from Counter c,Element e,SubElement se where e.elementID=se.element.elementID and se.subElementKey.counterGroup.counterGroupId=c.counterGroupc.counterGroupId and e.deviceType='"
				+ deviceType + "' group by c.counterId;");
		return (List<CounterGroup>) executeQuery(selectQuery.toString());
	}

	@Override
	public List<CounterGroup> getCounterGroupsByDeviceType(String deviceType) throws Exception {
		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("select cg.counterGroupId, cg.counterGroupName, cg.counterGroupDetails, cg.deviceType ,"
				+ "cg.neLevel, cg.cgSource from CounterGroup cg ");
		if (null != deviceType && !deviceType.isEmpty()) {
			selectQuery.append("where cg.deviceType='" + deviceType + "'");
		} else {
			throw new Exception(" deviceType can not be Null or Empty");
		}

		List<CounterGroup> counterGroupList = (List<CounterGroup>) executeQuery(selectQuery.toString());
		List<CounterGroup> countergplist = new ArrayList<>();

		for (Object row : counterGroupList) {
			if (row instanceof Object[]) {
				Object[] attribute = (Object[]) row;
				CounterGroup counterGroup = new CounterGroup();
				counterGroup.setCounterGroupId(String.valueOf(attribute[0]));
				counterGroup.setCounterGroupName(String.valueOf(attribute[1]));
				// counterGroup.setCounterGroupDetails(String.valueOf(attribute[2]));
				// counterGroup.setDeviceType(String.valueOf(attribute[3]));
				countergplist.add(counterGroup);
			}
		}
		return countergplist;
	}

	@Override
	public List<CounterGroup> getCountersAndKpisByCounterGroupId(List<CounterGroup> counterGroupList) throws Exception {

		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("select cg.counterGroupId, cg.counterGroupName, cg.counterGroupDetails, "
				+ "c.counterKey, c.logicalName, c.counterUnit, c.counterDescription, c.aggregation,"
				+ "c.counterType, c.enabled, c.cSource" + " from CounterGroup cg, Counter as c");

		StringBuilder whereQuery = new StringBuilder();
		whereQuery.append(" where cg.counterGroupId = c.counterKey.counterGroup.counterGroupId and ");
		// whereQuery.append(" c.counterType = '" + counterType + "' and ");
		whereQuery.append(" c.enabled = 1 " + " and ");
		whereQuery.append(" cg.counterGroupId in ( ");
		for (int i = 0; i < counterGroupList.size(); i++) {
			if (null != counterGroupList.get(i) && !counterGroupList.get(i).getCounterGroupId().toString().isEmpty())
				if (i == (counterGroupList.size() - 1))
					whereQuery.append("'" + counterGroupList.get(i).getCounterGroupId() + "'" + ")");
				else
					whereQuery.append("'" + counterGroupList.get(i).getCounterGroupId() + "'" + ",");
			else
				throw new Exception(" CounterGroupId can not be Null ");
		}

		selectQuery.append(whereQuery);

		List<CounterGroup> counterGpList = (List<CounterGroup>) executeQuery(selectQuery.toString());
		List<CounterGroup> countergplist = new ArrayList<>();

		for (Object row : counterGpList) {
			if (row instanceof Object[]) {
				Object[] attribute = (Object[]) row;
				CounterGroup counterGrpObj ;

				//need to handle NoSuchElementException for get() method in collection of object
				try {
					counterGrpObj =	countergplist.stream()
							.filter(x -> x.getCounterGroupId().equalsIgnoreCase(String.valueOf(attribute[0]))).findFirst()
							.get();
				} catch (NoSuchElementException e) {
					counterGrpObj= null;
				}

				if (null == counterGrpObj) {
					CounterGroup counterGroup = new CounterGroup();
					counterGroup.setCounterGroupId(String.valueOf(attribute[0]));
					counterGroup.setCounterGroupName(String.valueOf(attribute[1]));
					// counterGroup.setCounterGroupDetails(String.valueOf(attribute[2]));
					countergplist.add(counterGroup);

				}

				counterGrpObj = countergplist.stream()
						.filter(x -> x.getCounterGroupId().equalsIgnoreCase(String.valueOf(attribute[0]))).findFirst()
						.get();
				Counter counter = new Counter();
				counter.setCounterKey((CounterKey)(attribute[3]));
				counter.setLogicalName(String.valueOf(attribute[4]));
				counter.setCounterUnit(String.valueOf(attribute[5]));
			    counter.setCounterDescription(String.valueOf(attribute[6]));
				// counter.setAggregation((Aggregation) attribute[7]);
				counter.setCounterType(String.valueOf(attribute[8]));
				// counter.setEnabled(Integer.valueOf(String.valueOf(attribute[9])));
				// counter.setcSource(String.valueOf(attribute[10]));

				if(counter.getCounterType().equalsIgnoreCase(Constant.TYPE_COUNTER)){
					if (null == counterGrpObj.getCounterList())
						counterGrpObj.setCounterList(new ArrayList<Counter>());
					counterGrpObj.getCounterList().add(counter);
				} else if(counter.getCounterType().equalsIgnoreCase(Constant.TYPE_DERIVED_KPI) || counter.getCounterType().equalsIgnoreCase(Constant.DYNAMIC_KPI)){
					if (null == counterGrpObj.getKpis())
						counterGrpObj.setKpis(new ArrayList<Counter>());
					counterGrpObj.getKpis().add(counter);
				}
				countergplist.stream()
				.filter(ctrGrp -> ctrGrp.getCounterGroupId().equalsIgnoreCase(String.valueOf(attribute[0])))
				.findFirst().ifPresent(cgvo -> {
					countergplist.remove(cgvo);
				});

				countergplist.add(counterGrpObj);
			}
		}
		return countergplist;
	}

	@Override
	public CounterGroup getCounterGroupById(String groupId) throws Exception {
		String[] groupIds = {groupId};
		List<CounterGroup> group = getCounterGroupListByIds(Arrays.asList(groupIds));

		return group.get(0);
	}
}
