package com.harman.rtnm.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.harman.rtnm.dao.AbstractDAO;
import com.harman.rtnm.dao.PropertiesDao;
import com.harman.rtnm.model.Counter;
import com.harman.rtnm.model.CounterGroup;
import com.harman.rtnm.model.Properties;
import com.harman.rtnm.model.PropertyKey;

@Repository
public class PropertiesDaoImpl extends AbstractDAO<Properties> implements PropertiesDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 233712763313816754L;

	/**
	 * getPropertiesByCounterGroupIds(List<CounterGroup> counterGroupList)
	 * 
	 * @param counterGroupList
	 *            List of counterGroup
	 * @return List<CounterGroup> List of counterGroups with list of Properties
	 */
	@Override
	public List<CounterGroup> getPropertiesByCounterGroupIds(List<CounterGroup> counterGroupList) throws Exception {

		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("select p.propertyKey.counterGroupc.counterGroupId ,p.propertyKey.counterGroupc.counterGroupName , p.propertyName , p.propertyValues ,p.propertyKey.propertyId from Properties as p");
	//	selectQuery.append("from Properties as p");
		StringBuilder whereQuery = new StringBuilder();
		whereQuery.append(" where p.propertyKey.counterGroupc.counterGroupId in (");
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
		List<Properties> propertiesList = (List<Properties>)executeQuery(selectQuery.toString());
		Map<String, List<Properties>> counterGrpWithProptMap = new HashMap<>();
		List<CounterGroup> counterGrpList = new ArrayList<>();

		for (Object row : propertiesList) {
			if (row instanceof Object[]) {
				Object[] attribute = (Object[]) row;
				Properties property= new Properties();
         		PropertyKey key = new PropertyKey();
         		key.setPropertyId(String.valueOf(attribute[4]));
         CounterGroup cg= 	new CounterGroup();
         cg.setCounterGroupId(String.valueOf(attribute[0]));
        	key.setCounterGroups(cg);
//				property.setPropertyKey(key);
				property.setPropertyKey(key);
				property.setPropertyName(String.valueOf(attribute[2]));
				if(!("null").equalsIgnoreCase(String.valueOf(attribute[3])))
				{
					property.setPropertyValues(String.valueOf(attribute[3]));	
				}
				
				
				String counterGroupId = String.valueOf(attribute[0]);
				if (null != counterGroupId) {
					List<Properties> propertyList = null;
					if (!counterGrpWithProptMap.containsKey(counterGroupId)) {
						propertyList = new ArrayList<>();
					} else {
						propertyList = counterGrpWithProptMap.get(counterGroupId);
					}
					propertyList.add(property);
					counterGrpWithProptMap.put(counterGroupId, propertyList);
				}
			}
		}
		
//		propertiesList.forEach(property -> {
//
//			String counterGroupId = property.getPropertyKey().getCounterGroups().getCounterGroupId();
//			if (null != counterGroupId) {
//				List<Properties> propertyList = null;
//				if (!counterGrpWithProptMap.containsKey(counterGroupId)) {
//					propertyList = new ArrayList<>();
//				} else {
//					propertyList = counterGrpWithProptMap.get(counterGroupId);
//				}
//				propertyList.add(property);
//				counterGrpWithProptMap.put(counterGroupId, propertyList);
//			}
//		});

		counterGrpWithProptMap.forEach((key, value) -> {
			if (null != key) {
				CounterGroup counterGroup = new CounterGroup();
				counterGroup.setCounterGroupId(value.get(0).getPropertyKey().getCounterGroups().getCounterGroupId());
//				counterGroup.setCounterGroupDetails(
//						value.get(0).getPropertyKey().getCounterGroups().getCounterGroupDetails());
//				counterGroup
//						.setCounterGroupName(value.get(0).getPropertyKey().getCounterGroups().getCounterGroupName());
//				counterGroup.setDeviceType(value.get(0).getPropertyKey().getCounterGroups().getDeviceType());
				// counterGroup =
				// value.get(0).getPropertyKey().getCounterGroupc();
				counterGroup.setProperties(value);
				counterGrpList.add(counterGroup);
			}
		});
		propertiesList.clear();
		counterGrpWithProptMap.clear();
		return counterGrpList;
	}

	@Override
	public List<String> propertiesValueByCounter(List<String> counters) throws Exception {

		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append(
				"select p.propertyKey.propertyName from Properties as p , Counter as c, CounterGroup as cg where ");

		selectQuery.append("c.counterGroupc.counterGroupId = cg.counterGroupId and "
				+ "cg.counterGroupId = p.propertyKey.counterGroupc.counterGroupId and c.counterId in (");
		for (int i = 0; i < counters.size(); i++) {
			if (null != counters.get(i) && !counters.get(i).isEmpty())
				if (i == (counters.size() - 1))
					selectQuery.append("'" + counters.get(i) + "'" + ")");
				else
					selectQuery.append("'" + counters.get(i) + "'" + ",");
			else
				throw new Exception(" CounterId can not be Null ");
		}

		selectQuery.append(" group by p.propertyKey.propertyName");
		List<Properties> rows = executeQuery(selectQuery.toString());

		List<String> propertyNames = new ArrayList<>();

		for (Object row : rows) {
			if (row instanceof String) {
				propertyNames.add(row.toString());
			}
		}
		return propertyNames;
	}

	@Override
	public List<Properties> propertyDetailsByCounterGroup(List<CounterGroup> counterGroup) throws Exception {

		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("select p.propertyKey.propertyId, p.propertyName from Properties as p where ");
		selectQuery.append("p.propertyKey.counterGroupc.counterGroupId in (");
		for (int i = 0; i < counterGroup.size(); i++) {
			if (null != counterGroup.get(i) && !counterGroup.get(i).getCounterGroupId().isEmpty())
				if (i == (counterGroup.size() - 1))
					selectQuery.append("'" + counterGroup.get(i).getCounterGroupId() + "'" + ")");
				else
					selectQuery.append("'" + counterGroup.get(i).getCounterGroupId() + "'" + ",");
			else
				throw new Exception(" CounterGroupId can not be Null ");
		}
		selectQuery.append(" group by p.propertyKey.propertyId");
		
		List<Properties> rows = executeQuery(selectQuery.toString());
		List<Properties> properties = new ArrayList<>();

		for (Object row : rows) {
			if (row instanceof Object[]) {
				Object[] attribute = (Object[]) row;
				Properties property = new Properties();
				PropertyKey  key =new PropertyKey();
				property.setPropertyKey(key);
				property.getPropertyKey().setPropertyId(String.valueOf(attribute[0]));
				property.setPropertyName(String.valueOf(attribute[1]));
				properties.add(property);
			}
		}
		return properties;
	}
}
