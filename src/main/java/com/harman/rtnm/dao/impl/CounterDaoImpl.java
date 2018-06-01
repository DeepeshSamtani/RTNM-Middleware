package com.harman.rtnm.dao.impl;

import java.util.ArrayList;
import java.util.List;


import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import com.harman.dyns.model.common.Metric;
import com.harman.rtnm.common.constant.Constant;
import com.harman.rtnm.dao.AbstractDAO;
import com.harman.rtnm.dao.CounterDao;
import com.harman.rtnm.model.Aggregation;
import com.harman.rtnm.model.Counter;
import com.harman.rtnm.model.CounterGroup;
import com.harman.rtnm.model.CounterKey;
import com.harman.rtnm.vo.DynamicCounterVO;
import com.harman.rtnm.vo.InventoryDetailVO;
import com.harman.rtnm.samsung.commonutils.util.StringUtils;

@Repository
public class CounterDaoImpl extends AbstractDAO<Counter> implements CounterDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 409076449984849418L;
	private static final Logger logger = LoggerFactory.getLogger(CounterDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Counter> getCountersFromIDs(List<Metric> metrics) throws Exception {
		List<Counter> counters = new ArrayList<>();
		StringBuilder selectQuery = new StringBuilder();
		StringBuilder whereQuery = new StringBuilder();

		selectQuery.append(" from Counter c ");

		selectQuery.append(" where (c.counterKey.counterId, c.counterKey.counterGroup.counterGroupId) in ( ");
		for (int i = 0; i < metrics.size(); i++) {
			String id = "";
			if (null != metrics.get(i) && null != metrics.get(i).getId() && !metrics.get(i).getId().isEmpty()) {
				id = metrics.get(i).getId();
				if (metrics.get(i).getId().contains((Constant.SUM + Constant.UNDERSCORE))
						|| metrics.get(i).getId().contains(Constant.MIN + Constant.UNDERSCORE)
						|| metrics.get(i).getId().contains(Constant.MAX + Constant.UNDERSCORE)
						|| metrics.get(i).getId().contains(Constant.AVG + Constant.UNDERSCORE)
						|| metrics.get(i).getId().contains(Constant.FORMULA + Constant.UNDERSCORE))
					id = metrics.get(i).getId().replaceAll((Constant.SUM + Constant.UNDERSCORE), "")
					.replaceAll((Constant.MIN + Constant.UNDERSCORE), "")
					.replaceAll((Constant.MAX + Constant.UNDERSCORE), "")
					.replaceAll((Constant.AVG + Constant.UNDERSCORE), "")
					.replaceAll((Constant.FORMULA + Constant.UNDERSCORE), "");
				if (i == (metrics.size() - 1))
					selectQuery.append("('" + id + "','" + "')" + ")");
				else
					selectQuery.append("'" + id + "'" + ",");
			} else
				throw new Exception(" CounterId can not be Null ");
		}

		selectQuery.append(whereQuery);
		counters = (List<Counter>) executeQuery(selectQuery.toString());
		return counters;
	}

	@Override
	public Counter getCounterByID(CounterKey key) throws Exception { 

		return getRecordById(Counter.class, key);

	}

	@Override
	public List<Counter> getCounterListByIDs(List<String> counterList) throws Exception {

		StringBuilder selectQuery = new StringBuilder();
		selectQuery
		.append("SELECT c.counterKey.counterId, c.logicalName, c.counterUnit, c.counterDescription, c.aggregation,"
				+ " c.counterKey.counterGroup.counterGroupId  from Counter c ");

		StringBuilder whereQuery = new StringBuilder();
		whereQuery.append(" where c.counterId in ( ");
		for (int i = 0; i < counterList.size(); i++) {
			if (null != counterList.get(i) && !counterList.get(i).isEmpty())
				if (i == (counterList.size() - 1))
					whereQuery.append("'" + counterList.get(i) + "'" + ")");
				else
					whereQuery.append("'" + counterList.get(i) + "'" + ",");
			else
				throw new Exception(" CounterId can not be Null ");
		}
		whereQuery.append("  group by c.counterKey.counterGroup.counterGroupId");
		selectQuery.append(whereQuery);
		List<Counter> newCounterList = (List<Counter>) executeQuery(selectQuery.toString());
		List<Counter> counterlist = new ArrayList<>();
		for (Object row : newCounterList) {
			if (row instanceof Object[]) {
				Object[] attribute = (Object[]) row;
				Counter counter = new Counter();
				counter.getCounterKey().setCounterId(String.valueOf(attribute[0]));
				counter.setLogicalName(String.valueOf(attribute[1]));
				counter.setCounterUnit(String.valueOf(attribute[2]));
				//counter.setCounterDescription(String.valueOf(attribute[3]));
				// //no need to UI
				// need to check
				counter.setAggregation((Aggregation) attribute[4]);
				CounterGroup cg = new CounterGroup();
				cg.setCounterGroupId(String.valueOf(attribute[5]));
				// need to change code related to new structure
				// counter.setCounterGroupc(cg);
				counterlist.add(counter);
			}
		}
		return counterlist;
	}


	/**
	 * 
	 * @param counterGroupId
	 * @return
	 */
	public String getMaxCounterIdByGroupId(String counterGroupId) {

		if(counterGroupId == null || counterGroupId.trim().isEmpty()) {
			return "";
		}
		String subCounterId = counterGroupId.substring(0, counterGroupId.indexOf(Constant.UNDERSCORE)) + Constant.PATTERN_COUNTER+"%";

		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("SELECT max(cast(substring(c.counterKey.counterId, " + subCounterId.length() + ", "
				+ "(length(c.counterKey.counterId)-" + (subCounterId.length() - 1) + ")) as int)) from Counter c ");

		StringBuilder whereQuery = new StringBuilder();
		whereQuery.append(" where c.counterKey.counterId like '" + subCounterId + "'");
		whereQuery.append(" and c.counterKey.counterGroup = '" + counterGroupId + "'");		
		selectQuery.append(whereQuery);

		List<Counter> rows = (List<Counter>) executeQuery(selectQuery.toString());

		String maxCounterId = "";
		if (null != rows && !rows.isEmpty())
			maxCounterId = String.valueOf(rows.get(0));
		else
			maxCounterId = 0 + "";

		String counterId = counterGroupId.replaceAll(Constant.PATTERN_COUNTER_GROUP, Constant.PATTERN_COUNTER);
		String tempNumber = counterId.substring(counterId.lastIndexOf(Constant.UNDERSCORE)+1);
		counterId = counterId.replace(tempNumber, maxCounterId);

		return counterId;
	}

	/**
	 * 
	 * @param counterGroupId
	 * @return
	 */
	public String getNextCounterIDByGroupID(String counterGroupId) {			
		String maxId = getMaxCounterIdByGroupId(counterGroupId);		
		return nextCounterId(maxId);

	}

	public String saveCounter(InventoryDetailVO inventoryDetailVO) throws Exception {

		String deviceType = inventoryDetailVO.getDeviceType();
		Criteria cr = sessionFactory.getCurrentSession().createCriteria(Counter.class);
		Counter dynamicCounter = inventoryDetailVO.getCounters().get(0);
		if (dynamicCounter != null) {
			if (null == dynamicCounter.getCounterKey().getCounterId()) {
				String maxCounter = getMaxCounterIdByGroupId(inventoryDetailVO.getDeviceType());
				String counterId = deviceType + "_C_" + (Integer.valueOf(maxCounter) + 1);
				System.out.println(counterId);
				inventoryDetailVO.getCounters().get(0).getCounterKey().setCounterId(counterId);
			}
			save(inventoryDetailVO.getCounters().get(0));

			return "Counter saved Sucessfully";
		} else
			return "Bad Request";

	}

	public String updateCounter(Counter counter) throws Exception {
		if (null != counter && null != counter.getCounterKey().getCounterId()
				&& !counter.getCounterKey().getCounterId().isEmpty()) {
			System.out.println("updating counter Id..");
			update(counter);
			logger.debug("Counter updated successfully : " + counter);
			return "Couter updated successfully.";
		} else {
			System.out.println("Some values are null..counter is:"+counter+"counter.getCounterKey().getCounterId():"+counter.getCounterKey().getCounterId());
			throw new Exception("CounterId should not be null or empty.");
		}
	}


	/**
	 * 
	 */
	@Override
	public boolean ifCounterExistsByDisplayName(String counterNameDisplay)
			throws Exception {		
		return (getCounterByDisplayName(counterNameDisplay) == null);
	}

	@Override
	public Counter getCounterByDisplayName(String counterNameDisplay) throws Exception {
		Criteria cr1 = sessionFactory.getCurrentSession().createCriteria(Counter.class);	
		cr1.add(Restrictions.eq("logicalName", counterNameDisplay));

		if(!(cr1.list() == null || cr1.list().isEmpty())) {
			return (Counter) cr1.list().get(0);
		}

		return null;
	}

	@Override
	public Counter addCounter(Counter counter) {
		String counterID = getNextCounterIDByGroupID(counter.getCounterKey().getCounterGroup().getCounterGroupId());
		counter.getCounterKey().setCounterId(counterID);

		save(counter);

		return counter;
	}

	@Override
	public List<Counter> getCountersDetail(List<Counter> counter) throws Exception {
		List<Counter> counters = new ArrayList<>();
		StringBuilder selectQuery = new StringBuilder();
		StringBuilder whereQuery = new StringBuilder();

		selectQuery.append("select c.counterKey.counterId, c.counterKey.counterGroup.counterGroupId, "
				+ "c.logicalName, c.counterUnit, c.counterDescription, c.aggregation, c.counterType, c.enabled,"
				+ "c.cSource from Counter c ");

		selectQuery.append(" where (c.counterKey.counterId, c.counterKey.counterGroup.counterGroupId) in ( ");
		for (int i = 0; i < counter.size(); i++) {
			String id = "";
			if (null != counter.get(i) && null != counter.get(i).getCounterKey().getCounterId()
					&& null != counter.get(i).getCounterKey().getCounterGroup().getCounterGroupId()) {
				id = counter.get(i).getCounterKey().getCounterId();
				if (counter.get(i).getCounterKey().getCounterId().contains((Constant.SUM + Constant.UNDERSCORE))
						|| counter.get(i).getCounterKey().getCounterId().contains(Constant.MIN + Constant.UNDERSCORE)
						|| counter.get(i).getCounterKey().getCounterId().contains(Constant.MAX + Constant.UNDERSCORE)
						|| counter.get(i).getCounterKey().getCounterId().contains(Constant.AVG + Constant.UNDERSCORE)
						|| counter.get(i).getCounterKey().getCounterId()
						.contains(Constant.FORMULA + Constant.UNDERSCORE))
					id = counter.get(i).getCounterKey().getCounterId()
					.replaceAll((Constant.SUM + Constant.UNDERSCORE), "")
					.replaceAll((Constant.MIN + Constant.UNDERSCORE), "")
					.replaceAll((Constant.MAX + Constant.UNDERSCORE), "")
					.replaceAll((Constant.AVG + Constant.UNDERSCORE), "")
					.replaceAll((Constant.FORMULA + Constant.UNDERSCORE), "");
				if (i == (counter.size() - 1))
					selectQuery.append("('" + id + "','"
							+ counter.get(i).getCounterKey().getCounterGroup().getCounterGroupId() + "')" + ")");
				else
					selectQuery.append("('" + id + "','"
							+ counter.get(i).getCounterKey().getCounterGroup().getCounterGroupId() + "')" + ",");
			} else
				throw new Exception(" CounterId can not be Null ");
		}

		selectQuery.append(whereQuery);
		counters = (List<Counter>) executeQuery(selectQuery.toString());
		List<Counter> counters1 = new ArrayList<>();
		for (Object row : counters) {
			if (row instanceof Object[]) {
				Object[] attribute = (Object[]) row;
				Counter counter1 = new Counter();
				counter1.setCounterKey(new CounterKey());
				counter1.getCounterKey().setCounterId(String.valueOf(attribute[0]));

				counter1.getCounterKey().setCounterGroup(new CounterGroup());
				counter1.getCounterKey().getCounterGroup().setCounterGroupId(String.valueOf(attribute[1]));

				counter1.getCounterKey().setCounterGroupTrans(new CounterGroup());
				counter1.getCounterKey().getCounterGroupTrans().setCounterGroupId(String.valueOf(attribute[1]));

				counter1.setLogicalName(String.valueOf(attribute[2]));
				counter1.setCounterUnit(String.valueOf(attribute[3]));
				counter1.setCounterDescription(String.valueOf(attribute[4]));
				counter1.setAggregation((Aggregation) attribute[5]);
				counter1.setCounterType(String.valueOf(attribute[6]));
				counter1.setEnabled(Integer.valueOf(String.valueOf(attribute[7])));
				counter1.setcSource(String.valueOf(attribute[8]));				
				counters1.add(counter1);
			}
		}
		return counters1;
	}


	@Override
	public String getMaxCounterIdByDeviceType(String deviceType) {
		StringBuilder msxSelectQuery = new StringBuilder();

		msxSelectQuery.append("select GROUP_ID, COUNT FROM "
				+ "("
				+ "select count(COUNTER.COUNTER_ID) as COUNT, COUNTER_GROUP.COUNTER_GROUP_ID as GROUP_ID "
				+ "from COUNTER join COUNTER_GROUP "
				+ "on COUNTER.COUNTER_GROUP_ID = COUNTER_GROUP.COUNTER_GROUP_ID  "
				+ "WHERE COUNTER_GROUP.DEVICE_TYPE = '"+deviceType+"' "
				+ "group by  COUNTER_GROUP.COUNTER_GROUP_ID"
				+ ") AS RESULT "
				+ "order by COUNT DESC LIMIT 1"
				);


		@SuppressWarnings("unchecked")
		List<Object[]> result = (List<Object[]>)execcuteSQLQuery(msxSelectQuery.toString());	
		String maxCounterID = "";
		if(result != null && result.size() > 0) {

			Object[] values = result.get(0);

			String tempCounterId = String.valueOf(values[0]).replaceAll(Constant.PATTERN_COUNTER_GROUP, Constant.PATTERN_COUNTER);
			String currentNumber = tempCounterId.substring(tempCounterId.lastIndexOf(Constant.UNDERSCORE)+1);
			maxCounterID = tempCounterId.replace(currentNumber, String.valueOf(values[1]));
		}
		return maxCounterID;
	}

	@Override
	public String getNextCounterIdByDeviceType(String deviceType) {
		String maxCounterID = getMaxCounterIdByDeviceType(deviceType);
		return nextCounterId(maxCounterID);
	}


	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DynamicCounterVO> getAllDynamicKPIs() throws Exception {

		Criteria criteriaKPIList = sessionFactory.getCurrentSession().createCriteria(Counter.class);	
		criteriaKPIList.add(Restrictions.eq("counterType", Constant.DYNAMIC_KPI));
		criteriaKPIList.add(Restrictions.eq("enabled", 1));

		List<Counter> listOfKpis = (List<Counter>) criteriaKPIList.list();

		List<DynamicCounterVO> resultList = new ArrayList<>(); 
		listOfKpis.stream().forEach((c) -> 
		{
			DynamicCounterVO dkpi = new DynamicCounterVO();
			dkpi.setCounter(c); //setting the counter
			dkpi.setAggregationType(c.getAggType(c.getAggregation())); //setting the aggregation type
			dkpi.setDeviceType(c.getCounterKey().getCounterGroup().getDeviceType());
			dkpi.setGroupId(c.getCounterKey().getCounterGroup().getCounterGroupId());
			dkpi.setCounterId(c.getCounterKey().getCounterId());
			try {
				parseKPIFormula(c, dkpi);
			} catch(Exception e) {
				logger.error("Error parsing formula for kpi list , kpi : " + dkpi.getCounterId() + " grp : " + dkpi.getGroupId(), e);
			}
			/*try {
				String formula = c.getKpiFormula();
				char[] expression = formula.toCharArray();
				CounterKey keyToCounter = new CounterKey();
				keyToCounter.setCounterGroup(c.getCounterKey().getCounterGroup());
				StringBuilder cId = new StringBuilder();
				for(int i=0; i < expression.length; i++) {
					cId.setLength(0);
					if(!isOperator(expression[i])) {
						while (i < expression.length && expression[i] != ' ' && !isOperator(expression[i])) {
							cId.append(expression[i++]);
						}

						if(!StringUtils.isNumeric(cId.toString())) {
							String counterId = cId.toString().trim();
							keyToCounter.setCounterId(counterId);
							Counter temp = getCounterByID(keyToCounter);
							dkpi.addMappingCounterIdName(counterId, temp.getLogicalName());
						}
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}*/

			resultList.add(dkpi);
		});

		return resultList;
	}

	@SuppressWarnings("unchecked")
	public DynamicCounterVO getKPIDetails(CounterKey kpiId) throws Exception {

		Criteria criteriaKPIList = sessionFactory.getCurrentSession().createCriteria(Counter.class);	
		criteriaKPIList.add(Restrictions.eq("counterKey.counterId", kpiId.getCounterId()));
		criteriaKPIList.add(Restrictions.eq("counterKey.counterGroup.counterGroupId", kpiId.getCounterGroup().getCounterGroupId()));	

		List<Counter> listOfKpis = (List<Counter>) criteriaKPIList.list();

		Counter counter = listOfKpis.get(0);	
		DynamicCounterVO kpi = new DynamicCounterVO(counter);
		kpi.setAggregationType(counter.getAggType(counter.getAggregation())); //setting the aggregation type
		kpi.setCounterId(counter.getCounterKey().getCounterId());
		kpi.setGroupId(counter.getCounterKey().getCounterGroup().getCounterGroupId());
		kpi.setDeviceType(counter.getCounterKey().getCounterGroup().getDeviceType());
		parseKPIFormula(counter, kpi);
		
		return kpi;
	}

	/**
	 * Parse the kpi formula and the set tke key, value for
	 * where key:counterid and value:counter displayname.
	 * This will be appended in the response. 
	 * @param counter
	 * @param kpi
	 * @throws Exception
	 */
	private void parseKPIFormula(Counter counter, DynamicCounterVO kpi) throws Exception {
		
		String formula = counter.getKpiFormula();
		char[] expression = formula.toCharArray();

		CounterKey keyToCounter = new CounterKey();
		keyToCounter.setCounterGroup(counter.getCounterKey().getCounterGroup());
		StringBuilder cId = new StringBuilder();
		for(int i=0; i < expression.length; i++) {
			cId.setLength(0);
			if(!isOperator(expression[i])) {
				while (i < expression.length && expression[i] != ' ' && !isOperator(expression[i])) {
					cId.append(expression[i++]);
				}

				if(!StringUtils.isNumeric(cId.toString())) {
					String counterId = cId.toString().trim();
					keyToCounter.setCounterId(counterId);
					Counter c = getCounterByID(keyToCounter);
					kpi.addMappingCounterIdName(counterId, c.getLogicalName());
				}
			}
		}

	}

	/*
	 * return true if the char represents an operator, false otherwise
	 */
	private boolean  isOperator(char token) {
		return (token == '+' || token == '-' ||
				token == '*' || token == '/' ||
				token == '(' || token == ')');
	}

	/*
	 * may we shall move it into something like counterUtil
	 */
	/**
	 * generate the next counter id
	 * @param counterID
	 * @return
	 */
	private String nextCounterId(String counterID) {
		if(counterID.isEmpty()) {
			return "";
		}

		char[] tokens = counterID.toCharArray();
		int i = tokens.length-1;
		for(; i >=0; i--) {
			if('_' == tokens[i]) {
				break;
			}
		}

		int number = Integer.valueOf(new String(tokens, i+1, tokens.length - (i+1)));

		String nextCounterID = new String(tokens, 0, i+1) + (number+1);

		return nextCounterID;
	}
}
