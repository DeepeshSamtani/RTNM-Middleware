package com.harman.rtnm.common.helper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harman.rtnm.common.constant.Constant;
import com.harman.rtnm.model.CounterGroup;
import com.harman.rtnm.model.FormulaMetric;
import com.harman.rtnm.model.Properties;
import com.harman.rtnm.model.request.ReportTemplate;
import com.harman.rtnm.model.response.DruidGroupByResponse;
import com.harman.rtnm.model.response.DruidSelectResponse;
import com.harman.rtnm.model.response.Response;
import com.harman.rtnm.service.ReportService;

@Component
public class ResponseParseHelper {

	@Autowired
	ReportService reportService;

	private static ScriptEngine engine = new ScriptEngineManager().getEngineByExtension("js");

	public void populateDruidGroupByResonse(Map<String, FormulaMetric> metricmap, ReportTemplate reportTemplate,
			Response resp, DruidGroupByResponse[] druidGroupByResponse, List<String> propertiesName) {
		String granularity = reportTemplate.getGranularity();
		List<Map<String, Object>> resultmap = new ArrayList<>();
		Stream.of(druidGroupByResponse).forEach(dr -> {
			String timestamp = dr.getTimestamp();
			Map<String, Object> event = (Map<String, Object>) dr.getEvent();
			if (null != event && !event.isEmpty()) {
				Map<String, Object> eventMap = new HashMap<>();
				 //System.out.println(" Integer.MIN_VALUE == "+Integer.MIN_VALUE);
				event.forEach((key, value) -> {
					if (null != value) {
						 //System.out.println(key + " : " + value);
						if (value.equals(Integer.MIN_VALUE + 1)) {
							eventMap.put(key, null);
						} else if (value instanceof Double && ((double) value <= Integer.MIN_VALUE)) {
							eventMap.remove(key);
						} else {
							metricmap.forEach((mkey, mvalue) -> {
								if (null != mvalue) {
									if (mvalue.getMetricList().contains(key)) {
										String formula = new String();
										if (mvalue.getFormulaType().equalsIgnoreCase("Formula"))
											formula = mvalue.getFormulaWithValue().replace(key, String.valueOf(value));
										else if (mvalue.getFormulaType().equalsIgnoreCase("AVG")) {
											formula = mvalue.getFormulaWithValue().replaceFirst(key,
													String.valueOf(value));
										}
										mvalue.setFormulaWithValue(formula);
										// System.out.println(" Formula " + formula);

									} else if (propertiesName.contains(key))
										eventMap.put(key, value);
								}
								if (mkey.equalsIgnoreCase(key) && null == mvalue) {
									eventMap.put(key, value);
								} else if (!granularity.equalsIgnoreCase("ALL") && (propertiesName.contains(key)))
									eventMap.put(key, value);
								else if (granularity.equalsIgnoreCase("ALL"))
									eventMap.put(key, value);
							});
							if (key.contains("_" + Constant.AVERAGE)) {
								eventMap.put(key, value);
							}
						}
					}
				});
				eventMap.put(Constant.TIMESTAMP_DIMENSION, timestamp);
				evaluateValueForFormula(metricmap, eventMap, granularity);
				// filter response with respect to counterGroup
				Map<String, Object> eventMap1 = filterEventResponseWithCounterGroup(reportTemplate, eventMap, event);
				
				resultmap.add(eventMap1);
			}
			resp.setEvents(resultmap);
		});
	}

	/**
	 * @param metricmap
	 * @param granularity
	 * @param resp
	 * @param druidSelectResponse
	 */
	public void populateDruidSelectResponse(Map<String, FormulaMetric> metricmap, ReportTemplate reportTemplate,
			Response resp, DruidSelectResponse[] druidSelectResponse, List<String> propertiesName) {

		String granularity = reportTemplate.getGranularity();

		// need to refactor this code again
		List<Map<String, Object>> resultmap = new ArrayList<>();
		Stream.of(druidSelectResponse).forEach(dr -> {
			String timestamp = dr.getTimestamp();
			List<Map<String, Object>> events = (List<Map<String, Object>>) dr.getResult().get("events");
			if (null != events && !events.isEmpty()) {
				events.forEach(eve -> {
					Map<String, Object> eventMap = new HashMap<>();
					Map<String, Object> eventData = (Map<String, Object>) eve.get("event");
					eventData.forEach((key, value) -> {
						if (null != value) {
							//System.out.println(key + " :: " + value);
							if (value instanceof Double && ((double) value <= Integer.MIN_VALUE)) {
								eventMap.remove(key);
							} else {
								metricmap.forEach((mkey, mvalue) -> {
									if (null != mvalue) {
										if (mvalue.getMetricList().contains(key)) {
											String formula = new String();
											if (mvalue.getFormulaType().equalsIgnoreCase("Formula"))
												formula = mvalue.getFormulaWithValue().replace(key,
														String.valueOf(value));
											else if (mvalue.getFormulaType().equalsIgnoreCase("AVG")) {
												formula = mvalue.getFormulaWithValue().replaceFirst(key,
														String.valueOf(value));
											}
											mvalue.setFormulaWithValue(formula);
											//System.out.println(" Formula " + formula);

										} else if (propertiesName.contains(key))
											eventMap.put(key, value);
									}
									if (mkey.equalsIgnoreCase(key) && null == mvalue) {
										eventMap.put(key, value);
									} else if (!granularity.equalsIgnoreCase("ALL") && (propertiesName.contains(key)))
										eventMap.put(key, value);
									else if (granularity.equalsIgnoreCase("ALL"))
										eventMap.put(key, value);
								});
							}
						}
					});
					eventMap.put(Constant.TIMESTAMP_DIMENSION, eventData.get(Constant.TIMESTAMP_DIMENSION));
					evaluateValueForFormula(metricmap, eventMap, granularity);

					// filetr responce with respec to counterGroup
					Map<String, Object> eventMap1 = filterEventResponseWithCounterGroup(reportTemplate, eventMap,
							eventData);

					resultmap.add(eventMap1);
				});
			}
			resp.setEvents(resultmap);
		});
	}

	/**
	 * @param reportTemplate
	 * @param eventMap
	 * @param eventData
	 * @return
	 */
	private Map<String, Object> filterEventResponseWithCounterGroup(ReportTemplate reportTemplate,
			Map<String, Object> eventMap, Map<String, Object> eventData) {
		List<CounterGroup> counterGrpList = reportTemplate.getCounterGroupsWithCounterAndProperties();
		Map<String, Object> eventMap1 = new HashMap<>();
		CounterGroup counterGrpObj = (CounterGroup) counterGrpList.stream().filter(cg -> cg.getCounterGroupId().equals((String) eventData.get(Constant.COUNTER_GROUP_ID))).findFirst().get();
		eventMap.forEach((fianlKey, finalValue) -> {
			boolean kpiFlag = false;
			boolean propertyFlag = false;
			boolean counterFlag = false;
			String key1 = null;
			String key = fianlKey.replaceAll("SUM_", "").replaceAll("MIN_", "").replaceAll("MAX_","").
					replaceAll("AVG_", "").replaceAll("FORMULA_", "");
			
			if (null != counterGrpObj.getCounterList() && !counterGrpObj.getCounterList().isEmpty()){
				counterFlag = counterGrpObj.getCounterList().stream().filter(Objects::nonNull)
						.filter(counter -> counter.getCounterKey().getCounterId().equals((String) key)).findFirst()
						.isPresent();
				if(counterFlag){
					key1 = fianlKey+Constant.UNDERSCORE+Constant.UNDERSCORE+eventMap.get(Constant.COUNTER_GROUP_ID);
					eventMap1.put(key1, finalValue);
				}
			}
			if (null != counterGrpObj.getKpis() && !counterGrpObj.getKpis().isEmpty()){
				kpiFlag = counterGrpObj.getKpis().stream().filter(Objects::nonNull)
						.filter(kpi -> kpi.getCounterKey().getCounterId().equals((String) key)).findFirst()
						.isPresent();
				if(kpiFlag){
					key1 = fianlKey+Constant.UNDERSCORE+Constant.UNDERSCORE+eventMap.get(Constant.COUNTER_GROUP_ID);
					eventMap1.put(key1, finalValue);
				}
			}
			if(null != counterGrpObj.getProperties() && !counterGrpObj.getProperties().isEmpty()){
				propertyFlag = counterGrpObj.getProperties().stream().filter(Objects::nonNull)
						.filter(pro -> pro.getPropertyKey().getPropertyId().equals((String) key)).findFirst()
						.isPresent();
				if(propertyFlag){
					Properties property = (Properties) counterGrpObj.getProperties().stream().filter(p -> p.getPropertyKey().getPropertyId().equals((String) key)).findFirst().get();
					key1 = property.getPropertyName();
					eventMap1.put(key1, finalValue);
				}
			}
			if(fianlKey.equals(Constant.COUNTER_GROUP_ID)){
				if(counterGrpObj.getCounterGroupId().equals(finalValue))
					eventMap1.put(fianlKey, counterGrpObj.getCounterGroupName());
			}
			if ((fianlKey.equals(Constant.TIMESTAMP_DIMENSION)))
				eventMap1.put(fianlKey, finalValue);
		});
		return eventMap1;
	}

	private void evaluateValueForFormula(Map<String, FormulaMetric> metricmap, Map<String, Object> eventMap,
			String granularity) {
		metricmap.forEach((key, value) -> {
			if (null != value) {
				formula(value.getFormulaWithValue());
				Object caculatedValue = evaluation(value.getFormulaWithValue());
				// System.out.println(key + " value : " + caculatedValue);
				value.setFormulaWithValue(value.getFormula());
				String counterName = new String("");
				if (granularity.equalsIgnoreCase(Constant.ALL))
					counterName = key;
				else {
					String newKey = key;
					if (key.contains("SUM_") || key.contains("AVG_"))
						newKey = newKey.replaceAll("SUM_", "").replaceAll("AVG_", "");
					if (value.getFormulaType().equalsIgnoreCase(Constant.FORMULA))
						counterName = Constant.FORMULA + Constant.UNDERSCORE + key;
					else if (value.getFormulaType().equalsIgnoreCase("AVG"))
						counterName = Constant.AVG + Constant.UNDERSCORE + newKey;
				}
				eventMap.put(counterName, caculatedValue);
			}
		});
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		final Set<Object> seen = new HashSet<>();
		return t -> seen.add(keyExtractor.apply(t));
	}

	private static void formula(String formula) {
		try {
			engine.eval("function MetricCalculation() { return " + formula + "; }");
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

	private static Object evaluation(Object values) {
		try {
			for (Field f : values.getClass().getFields())
				engine.put(f.getName(), f.get(values));

			return engine.eval("MetricCalculation()");
		} catch (Exception ex) {

		}
		return null;
	}

}
