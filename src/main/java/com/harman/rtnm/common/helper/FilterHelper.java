package com.harman.rtnm.common.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.harman.dyns.model.common.Metric;
import com.harman.dyns.model.common.Rule;
import com.harman.dyns.model.common.Rules;
import com.harman.rtnm.common.constant.Constant;
import com.harman.rtnm.common.constant.FilterValueEnum;
import com.harman.rtnm.model.CounterGroup;

@Component
public class FilterHelper {

	/**
	 * This method use to set filter condition
	 * 
	 * @param rule_filter
	 * @param metricsList
	 * @param reportDetailVO
	 * @return
	 */
	public Rules applyFilterForCounter(Rules rule_filter, List<Metric> metricsList, List<CounterGroup> counterGroupList) {
		Rules filter = rule_filter;
		List<Rule> rules;
		if (null != rule_filter) {
			rules = filter.getRules();
		} else {
			filter = new Rules();
			rules = new ArrayList<Rule>();
		}

		/*
		 * if (!StringUtils.isEmpty(attribute.getName())) {
		 * filter.setCondition(FilterValueEnum.AND_CONDITION.getValue()); Rule
		 * rule = new Rule();
		 * rule.setType(FilterValueEnum.FILTER_TYPE_SELECTOR.getValue());
		 * rule.setField(FilterValueEnum.DIMENSION_NODENAME.getValue());
		 * rule.setValue(attribute.getName()); rules.add(rule); }
		 */

		// This handle for not required Integer.MIN data
		if (null != metricsList && !metricsList.isEmpty()) {
			if (null == filter.getCondition() || StringUtils.isEmpty(filter.getCondition())) {
				//Rules outerRule = filter;

				filter.setCondition(FilterValueEnum.AND_CONDITION.getValue());
				filter.setRules(new ArrayList<Rule>());

				
				if(null!=counterGroupList && !counterGroupList.isEmpty()){
					Rule outerRule2 = new Rule();
					outerRule2.setCondition(FilterValueEnum.OR_CONDITION.getValue());
					outerRule2.setRules(new ArrayList<Rule>());
					counterGroupList.forEach(cnrtGrp -> {
						// selector filter					
							Rule innerRule = new Rule();
							innerRule.setType(FilterValueEnum.FILTER_TYPE_SELECTOR.getValue());
							innerRule.setField(Constant.COUNTER_GROUP_ID);
							innerRule.setValue(cnrtGrp.getCounterGroupId());
							outerRule2.getRules().add(innerRule);
					});
					rules.add(outerRule2);
				}
				
				Rule outerRule = new Rule();
				outerRule.setCondition(FilterValueEnum.OR_CONDITION.getValue());
				outerRule.setRules(new ArrayList<Rule>());
				metricsList.forEach(metric -> {
					// bound filter
					if (!metric.getId().contains(Constant.COUNT_STREAM.toUpperCase())) {
						Rule innerRule = new Rule();
						innerRule.setType(FilterValueEnum.FILTER_TYPE_BOUND.getValue());
						innerRule.setId(metric.getId());
						innerRule.setValue(String.valueOf(Integer.MIN_VALUE));
						outerRule.getRules().add(innerRule);
					}
				});
				rules.add(outerRule);
				
				if (!rules.isEmpty()) {
					filter.setRules(rules);
				}
				/*if (!outerRule.getRules().isEmpty()) {
					filter = outerRule;
				}*/
			} else {
				if(null!=counterGroupList && !counterGroupList.isEmpty()){
					Rule outerRule2 = new Rule();
					outerRule2.setCondition(FilterValueEnum.OR_CONDITION.getValue());
					outerRule2.setRules(new ArrayList<Rule>());
					counterGroupList.forEach(cnrtGrp -> {
						// selector filter					
							Rule innerRule = new Rule();
							innerRule.setType(FilterValueEnum.FILTER_TYPE_SELECTOR.getValue());
							innerRule.setField(Constant.COUNTER_GROUP_ID);
							innerRule.setValue(cnrtGrp.getCounterGroupId());
							outerRule2.getRules().add(innerRule);
					});
					rules.add(outerRule2);
				}
				Rule outerRule = new Rule();

				outerRule.setCondition(FilterValueEnum.OR_CONDITION.getValue());
				outerRule.setRules(new ArrayList<Rule>());

				metricsList.forEach(metric -> {

					// bound filter
					if (!metric.getId().contains(Constant.COUNT_STREAM.toUpperCase())) {
						Rule innerRule = new Rule();
						innerRule.setType(FilterValueEnum.FILTER_TYPE_BOUND.getValue());
						innerRule.setId(metric.getId());
						innerRule.setValue(String.valueOf(Integer.MIN_VALUE));
						outerRule.getRules().add(innerRule);
					}
				});
				
				rules.add(outerRule);
				if (!rules.isEmpty()) {
					filter.setRules(rules);
				}
			}

		}
		/*
		 * if(null==filter.getCondition() ||
		 * StringUtils.isEmpty(filter.getCondition())){ if (!rules.isEmpty()) {
		 * filter = (Rules) rules; } }else{ if (!rules.isEmpty()) {
		 * filter.setRules(rules); } }
		 */

		return filter;
	}

	public Rules generateFilterFromMap(Map<String, Object> filterMap) {
		Rules filter = new Rules();
		filter.setCondition(FilterValueEnum.AND_CONDITION.getValue());
		filter.setRules(new ArrayList<Rule>());

		if (null != filterMap && !filterMap.isEmpty()) {

			Rule outerRule2 = new Rule();
			outerRule2.setCondition(FilterValueEnum.OR_CONDITION.getValue());
			outerRule2.setRules(new ArrayList<Rule>());
			List<Rule> rules = new ArrayList<Rule>();

			filterMap.forEach((key, val) -> {
				List<String> value = (List<String>) val;
				value.forEach(v -> {
					// selector filter
					Rule innerRule = new Rule();
					innerRule.setType(FilterValueEnum.FILTER_TYPE_SELECTOR.getValue());
					innerRule.setField(key);
					innerRule.setValue(v);
					outerRule2.getRules().add(innerRule);
				});
			});
			filter.getRules().add(outerRule2);
		}		
		return filter;
	}

}
