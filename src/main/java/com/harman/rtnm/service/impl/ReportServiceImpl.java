package com.harman.rtnm.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.harman.dyns.model.common.Attribute;
import com.harman.dyns.model.common.Metric;
import com.harman.dyns.model.common.Rules;
import com.harman.dyns.model.druid.request.ReportRequest;
import com.harman.rtnm.common.constant.Constant;
import com.harman.rtnm.common.helper.DruidHelper;
import com.harman.rtnm.common.helper.FilterHelper;
import com.harman.rtnm.common.helper.RandomNumberHelper;
import com.harman.rtnm.common.helper.ResponseParseHelper;
import com.harman.rtnm.common.property.CommonAttribute;
import com.harman.rtnm.model.Aggregation;
import com.harman.rtnm.model.Counter;
import com.harman.rtnm.model.CounterGroup;
import com.harman.rtnm.model.CounterKey;
import com.harman.rtnm.model.DashboardTemplate;
import com.harman.rtnm.model.FormulaMetric;
import com.harman.rtnm.model.GraphData;
import com.harman.rtnm.model.KPI;
import com.harman.rtnm.model.Properties;
import com.harman.rtnm.model.SeriesData;
import com.harman.rtnm.model.UserDetail;
import com.harman.rtnm.model.UserTemplate;
import com.harman.rtnm.model.UserTemplateKey;
import com.harman.rtnm.model.request.ReportTemplate;
import com.harman.rtnm.model.response.Response;
import com.harman.rtnm.model.response.UserReport;
import com.harman.rtnm.service.CounterService;
import com.harman.rtnm.service.DashboardTemplateService;
import com.harman.rtnm.service.KpiService;
import com.harman.rtnm.service.PropertiesService;
import com.harman.rtnm.service.ReportService;
import com.harman.rtnm.service.UserDetailService;
import com.harman.rtnm.service.UserTemplateService;
import com.harman.rtnm.ui.model.Entity;
import com.harman.rtnm.ui.model.ParentHeader;
import com.harman.rtnm.vo.DashboardDetailVO;
import com.harman.rtnm.vo.ReportDetailVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	DruidHelper druidHelper;

	@Autowired
	ResponseParseHelper responseParseHelper;

	@Autowired
	CounterService counterService;

	@Autowired
	FilterHelper filterHelper;

	@Autowired
	KpiService kpiService;

	@Autowired
	UserDetailService userDetailService;

	@Autowired
	UserTemplateService userTemplateService;

	@Autowired
	PropertiesService propertiesService;

	@Autowired
	DashboardTemplateService dashboardTemplateService;
	
	@Autowired
	CommonAttribute commonAttribute;

	ObjectMapper mapper = new ObjectMapper();

	/**
	 * This method gives data with configurations to UI
	 * 
	 * @param ReportDetailVO
	 *            reportDetailVO
	 * @return UserReport userreport
	 * 
	 */
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public UserReport getSpecificReport(ReportDetailVO reportDetailVO) throws Exception {
		UserReport userreport = new UserReport();
		ReportTemplate reportTemplate = mapper.convertValue(reportDetailVO.getJsonString(), ReportTemplate.class);

		List<CounterGroup> counterGrpList = reportTemplate.getCounterGroupsWithCounterAndProperties();

		List<Counter> counterList1 = addCounterGroupIdInCounterKey(counterGrpList);

		Map<String, FormulaMetric> metricmap = new HashMap<>();
		List<Metric> metricsForDruid = null;
		Boolean kpiFlag = null;
		List<Counter> counterList = null;
		String dataType = reportTemplate.getReportDataType();
		String queryType = new String();
		if (null != dataType && dataType.equalsIgnoreCase(Constant.RAW)) {
			queryType = Constant.SELECT;
		} else if (null != dataType && dataType.equalsIgnoreCase(Constant.GROUPED)) {
			queryType = Constant.GROUP_BY;
		}

		if (null != reportTemplate && null != reportTemplate.getConfiguration()
				&& null != reportTemplate.getConfiguration().getMetrics()) {
			counterList = counterService.getCountersDetail(counterList1);

			metricsForDruid = druidHelper.getCorrespondingCounters(counterList, metricmap,
					reportTemplate.getGranularity(), queryType);

			/*
			 * Code goes here for applying multiple aggregations on a single metric with RAW
			 * data
			 */
			boolean applyMultipleAggs = reportTemplate.getConfiguration().getMultipleAggregationsOnRawGranularity();
			if (applyMultipleAggs && queryType.equals(Constant.GROUP_BY)) {
				druidHelper.applyMultipleAggs(metricsForDruid, reportTemplate.getConfiguration(), metricmap);
			}

			druidHelper.processRawAggregatedFormulas(reportTemplate.getConfiguration().getMetrics(), metricmap,
					metricsForDruid);
			reportTemplate.getConfiguration().setMetrics(metricsForDruid);
			addAggregationListInCounter(counterList, reportTemplate.getGranularity());
			userreport.setMetrics(counterList);

			// add aggregation list in counterGroupwithproperty in reportTemplate
			setAggregationList(counterGrpList, counterList);

			kpiFlag = false;
		}

		if (null != reportTemplate.getFilterMap() && !reportTemplate.getFilterMap().isEmpty()) {
			reportTemplate.setFilter(filterHelper.generateFilterFromMap(reportTemplate.getFilterMap()));
		}
		Rules rules = filterHelper.applyFilterForCounter(reportTemplate.getFilter(), metricsForDruid, counterGrpList);
		reportTemplate.setFilter(rules);

		// List<String> properties = getPropertiesByMetrics(metricsForDruid);
		List<String> properties = new ArrayList<>();

		if (null != reportTemplate) {

			// Explicitly Add COUNTER_GROUP_ID dimension as per our Druid
			// dataSource structure
			Attribute attribute = new Attribute();
			attribute.setId(Constant.COUNTER_GROUP_ID);
			attribute.setDisplayName(Constant.COUNTER_GROUP_ID);
			reportTemplate.getConfiguration().getDimensions().add(attribute);

			if (null != reportTemplate.getConfiguration() && null != reportTemplate.getConfiguration().getDimensions()
					&& reportTemplate.getConfiguration().getDimensions().isEmpty()) {
				List<Properties> properties2 = propertyDetailsByCounterGroup(counterGrpList);
				prepareDimensionsForEmptyRequest(reportTemplate, reportDetailVO, properties2);
				properties.clear();
				properties2.stream().forEach(p -> {
					properties.add(p.getPropertyKey().getPropertyId());
				});
				properties.add(Constant.COUNTER_GROUP_ID);
			} else {
				reportTemplate.getConfiguration().getDimensions().stream().forEach(dim -> {
					properties.add(dim.getId());
				});
			}
		}

		reportDetailVO.setJsonString(mapper.convertValue(reportTemplate, Map.class));

		Response druidResponse = druidHelper.prepareDruidResponse(reportTemplate, metricmap, properties);
		userreport.setResponse(new ArrayList<>());
		userreport.getResponse().add(druidResponse);
		userreport.setReportConfiguration(reportTemplate);
		userreport.setHeader(headerListForUITable(reportTemplate, counterGrpList, counterList));
		userreport.setUserTemplateId(reportDetailVO.getUserTemplateId());

		if (userreport.getReportConfiguration().getConfiguration().getType().equalsIgnoreCase(Constant.Graph)) {
			if (userreport.getReportConfiguration().getConfiguration().getSubType().toLowerCase().contains("bar")
					|| userreport.getReportConfiguration().getConfiguration().getSubType().toLowerCase()
							.contains("line")) {
				prepareSeriesDataForGraph(druidResponse, userreport, reportTemplate.getGranularity());
			}
		}
		return userreport;
	}
	
	
	/*public String getDruidURI() {
		return druidHelper.getDruidURI();
	}*/

	/**
	 * @param counterGrpList
	 * @param counterList
	 */
	private void setAggregationList(List<CounterGroup> counterGrpList, List<Counter> counterList) {
		counterList.stream().forEach(counter ->{
			String couterGrpId = counter.getCounterKey().getCounterGroup().getCounterGroupId();
			CounterGroup counterGrp =(CounterGroup) counterGrpList.stream().filter(cntrgrp-> couterGrpId.equalsIgnoreCase(cntrgrp.getCounterGroupId())).findFirst().get();
			
			if(null!=counter.getCounterType() && counter.getCounterType().contains("KPI")){
				//add aggregationList for KPI
				Set<String> aggList = new HashSet(); 
				aggList.addAll(counter.getAggregationList());
				counterGrp.getKpis().stream().filter(Objects::nonNull).filter( k -> Objects.nonNull(k) && k.getCounterKey().getCounterId().
						equals(counter.getCounterKey().getCounterId())).findFirst().get().
						setAggregationList(aggList);
				
			}else{
				// add aggregationList for Counter
				Set<String> aggList = new HashSet(); 
				aggList.addAll(counter.getAggregationList());
				counterGrp.getCounterList().stream().filter(Objects::nonNull).filter( c -> Objects.nonNull(c) && c.getCounterKey().getCounterId().
						equals(counter.getCounterKey().getCounterId())).findFirst().get().
						setAggregationList(aggList);
				
			}				
		});
	}

	/**
	 * @param reportTemplate
	 * @param counterGrpList
	 * @param counterList
	 */
	private List<ParentHeader> headerListForUITable(ReportTemplate reportTemplate, List<CounterGroup> counterGrpList,
			List<Counter> counterList) {
		List<ParentHeader> header = new ArrayList();
		ParentHeader parentHeader = new ParentHeader();
		parentHeader.setId(Constant.TIMESTAMP_DIMENSION);
		parentHeader.setDisplayName("Timestamp");
		parentHeader.setValue(Constant.TEXT);
		ParentHeader parentHeaderCG = new ParentHeader();
		parentHeaderCG.setId(Constant.COUNTER_GROUP_ID);
		parentHeaderCG.setDisplayName("Counter Group");
		parentHeaderCG.setValue(Constant.TEXT);
		header.add(parentHeader);
		header.add(parentHeaderCG);

		Set<String> uniqueProperties = getUniqueProperties(counterGrpList);
		uniqueProperties.stream().forEach(p -> {
			ParentHeader parentHeaderProperty = new ParentHeader();
			parentHeaderProperty.setId(p);
			parentHeaderProperty.setDisplayName(p);
			parentHeaderProperty.setValue(Constant.TEXT);
			header.add(parentHeaderProperty);
		});

		if (null != counterList && !counterList.isEmpty()) {
			counterList.stream().forEach(c -> {
				if (null != c.getAggregationList() && !c.getAggregationList().isEmpty()) {
					if (c.getAggregationList().size() == 1) {
						ParentHeader parentHeaderC = new ParentHeader();
						parentHeaderC.setDisplayName(c.getLogicalName() + " (" + c.getCounterUnit() + ")");
						parentHeaderC.setValue(Constant.NUMBER);
						if (reportTemplate.getGranularity().equalsIgnoreCase(Constant.ALL))
							parentHeaderC.setId(c.getCounterKey().getCounterId() + Constant.UNDERSCORE
									+ Constant.UNDERSCORE + c.getCounterKey().getCounterGroup().getCounterGroupId());
						else
							parentHeaderC.setId(c.getAggregationList().stream().findFirst().get() + Constant.UNDERSCORE
									+ c.getCounterKey().getCounterId() + Constant.UNDERSCORE + Constant.UNDERSCORE
									+ c.getCounterKey().getCounterGroup().getCounterGroupId());
						
						header.add(parentHeaderC);
					} else {
						ParentHeader parentHeaderCounter = new ParentHeader();
						if (reportTemplate.getGranularity().equalsIgnoreCase(Constant.ALL)) {
							parentHeaderCounter.setDisplayName(c.getLogicalName() + " (" + c.getCounterUnit() + ")");
							parentHeaderCounter.setValue(Constant.NUMBER);
							parentHeaderCounter.setId(c.getCounterKey().getCounterId() + Constant.UNDERSCORE
									+ Constant.UNDERSCORE + c.getCounterKey().getCounterGroup().getCounterGroupId());
						} else {
							parentHeaderCounter.setDisplayName(c.getLogicalName() + " (" + c.getCounterUnit() + ")");
							parentHeaderCounter.setValue(Constant.NUMBER);
							parentHeaderCounter.setId(c.getLogicalName());
							List<Entity> entities = new ArrayList<>();
							c.getAggregationList().forEach(agg -> {
								Entity childCounter = new Entity();
								childCounter.setValue(Constant.NUMBER);
								childCounter.setDisplayName(agg);
								childCounter.setId(agg + Constant.UNDERSCORE + c.getCounterKey().getCounterId()
										+ Constant.UNDERSCORE + Constant.UNDERSCORE
										+ c.getCounterKey().getCounterGroup().getCounterGroupId());
								entities.add(childCounter);
							});
							parentHeaderCounter.setChildren(entities);
						}
						header.add(parentHeaderCounter);
					}
				}
			});
		}
		return header;
	}

	/**
	 * @param counterGrpList
	 * @return
	 */
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<Counter> addCounterGroupIdInCounterKey(List<CounterGroup> counterGrpList) {
		List<Counter> counterList1 = new ArrayList<>();
		counterGrpList.stream().forEach(cg -> {
			// counterList1
			List<Counter> counterListCg = cg.getCounterList();
			if (null != counterListCg && !counterListCg.isEmpty()) {
				counterListCg.stream().forEach(c -> {
					Counter counter = c;
					counter.getCounterKey().setCounterGroup(new CounterGroup());
					counter.getCounterKey().getCounterGroup().setCounterGroupId(cg.getCounterGroupId());
					counterList1.add(counter);
				});
			}
			List<Counter> kpiListCg = cg.getKpis();
			if (null != kpiListCg && !kpiListCg.isEmpty()) {
				kpiListCg.stream().forEach(c -> {
					Counter kpi = c;
					kpi.getCounterKey().setCounterGroup(new CounterGroup());
					kpi.getCounterKey().getCounterGroup().setCounterGroupId(cg.getCounterGroupId());
					counterList1.add(kpi);
				});
			}
		});
		return counterList1;
	}

	@Override
	public UserReport addUserReport(ReportDetailVO reportDetailVO) throws Exception {
		UserReport userreport = new UserReport();
		UserDetail userDetail = new UserDetail();
		if (null != reportDetailVO.getUserName()) {
			userreport.setUserName(reportDetailVO.getUserName());
			// load user
			userDetail = userDetailService.loadUserByName(reportDetailVO.getUserName());
		}
	     int count=userTemplateService.getUserTemplateCountByUserId(userDetail.getUserId());
	     if(count ==  commonAttribute.getMaxReportCount())
	     {
	    	 throw new Exception ("Report size has been reached to Maximum Limit");
	     }
				
		ReportTemplate reportRequest = mapper.convertValue(reportDetailVO.getJsonString(), ReportTemplate.class);
		// ReportRequest reportRequest =
		// mapper.convertValue(reportDetailVO.getJsonString(),
		// ReportRequest.class);
		
		List<CounterGroup> counterGrpList = reportRequest.getCounterGroupsWithCounterAndProperties();
		Map<String, FormulaMetric> metricmap = new HashMap<>();
		List<Metric> metricsForDruid = null;
		Boolean kpiFlag = null;

		if (null != reportRequest && null != reportRequest.getConfiguration()
				&& null != reportRequest.getConfiguration().getMetrics()) {
			// List<Counter> counterList =
			// counterService.getCountersFromIDs(reportRequest.getConfiguration().getMetrics());

			List<Counter> counterList1 = addCounterGroupIdInCounterKey(counterGrpList);
			List<Counter> counterList = counterService.getCountersDetail(counterList1);
			
			String dataType = reportRequest.getReportDataType();
			String queryType = new String();
			if (null != dataType && dataType.equalsIgnoreCase(Constant.RAW)) {
				queryType = Constant.SELECT;
			} else if (null != dataType && dataType.equalsIgnoreCase(Constant.GROUPED)) {
				queryType = Constant.GROUP_BY;
			}
			metricsForDruid = druidHelper.getCorrespondingCounters(counterList, metricmap,
					reportRequest.getGranularity(), queryType);
			reportRequest.getConfiguration().setMetrics(metricsForDruid);
			userreport.setMetrics(counterList);
			kpiFlag = false;
		} else if (null != reportRequest && null != reportRequest.getConfiguration()
				&& null != reportRequest.getConfiguration().getKpiMetrics()) {
			metricsForDruid = druidHelper.getCorrespondingKpi(reportRequest.getConfiguration().getKpiMetrics(),
					metricmap, reportRequest.getGranularity(), reportRequest.getConfiguration().getType());
			List<KPI> kpiList = kpiService.getKpiDetails(reportRequest.getConfiguration().getKpiMetrics());
			userreport.setKpiMetrics(kpiList);
			kpiFlag = true;
		}

		Rules rules = filterHelper.applyFilterForCounter(reportRequest.getFilter(), metricsForDruid,
				reportRequest.getCounterGroupsWithCounterAndProperties());
		System.out.println(rules);
		reportRequest.setFilter(rules);

		// List<String> properties = getPropertiesByMetrics(metricsForDruid);
		List<String> properties = new ArrayList<>();

		if (null != reportRequest) {
			if (null != reportRequest.getConfiguration() && null != reportRequest.getConfiguration().getDimensions()
					&& reportRequest.getConfiguration().getDimensions().isEmpty()) {
				List<Properties> properties2 = propertyDetailsByCounterGroup(counterGrpList);
				prepareDimensionsForEmptyRequest(reportRequest, reportDetailVO, properties2);
			} else {
				reportRequest.getConfiguration().getDimensions().stream().forEach(dim -> {
					properties.add(dim.getId());
				});
			}
		}

		// No need to send data to UI while Save report
		// Response druidResponse =
		// druidHelper.prepareDruidResponse(reportRequest, metricmap,
		// properties);
		// userreport.setResponse(new ArrayList<>());
		// userreport.getResponse().add(druidResponse);



		UserTemplate userTemplate = new UserTemplate();
		// userTemplate.setReportDetail(reportRequest);
		ObjectMapper mapperObj = new ObjectMapper();
		String reportRequestJson = mapperObj.writeValueAsString(reportRequest);
		// System.out.println(reportRequestJson);
		userTemplate.setReportDetail(reportRequestJson.getBytes());
		// userTemplate.setReportDetail((mapper.writeValueAsBytes(reportDetailVO.getJsonString())));

		if (null != reportRequest.getConfiguration().getName())
			userTemplate.setReportName(reportRequest.getConfiguration().getName());

		UserTemplateKey userTemplateKey = new UserTemplateKey();
		userTemplateKey.setUserDetail(userDetail);
		userTemplateKey.setUsertemplateId(RandomNumberHelper.generateRandomAlphaNumericString());
		userTemplate.setUserTemplateKey(userTemplateKey);
		userTemplateService.saveUserTemplate(userTemplate);
		userreport.setUserTemplateId(userTemplate.getUserTemplateKey().getUsertemplateId());
		userreport.setReportName(userTemplate.getReportName());

		return userreport;
	}

	@Override
	public UserReport getUserReport(ReportDetailVO reportDetailVO) throws Exception {
		UserReport userReport = new UserReport();
		UserTemplate userTemplate = new UserTemplate();
		if (null != reportDetailVO && null != reportDetailVO.getIsDashboardReport()
				&& reportDetailVO.getIsDashboardReport()) {
			DashboardDetailVO dashboardDetailVO = new DashboardDetailVO();
			dashboardDetailVO.setDashboardId(reportDetailVO.getDashboardId());
			dashboardDetailVO.setDashboardTemplateId(reportDetailVO.getUserTemplateId());
			dashboardDetailVO.setSubDashboardId(reportDetailVO.getSubDashboardId());
			DashboardTemplate dashboardTemplate = dashboardTemplateService.searchDashboardTemplate(dashboardDetailVO);
			if (null != dashboardTemplate)
				BeanUtils.copyProperties(dashboardTemplate, userTemplate);

		} else {
			userTemplate = userTemplateService.searchUserTemplate(reportDetailVO);
		}

		Map<String, Object> inputjson = mapper.readValue(new String(userTemplate.getReportDetail()),
				new TypeReference<Map<String, Object>>() {
				});

		if (inputjson != null && reportDetailVO.getScheduledReport() != null && reportDetailVO.getScheduledReport()) {
			inputjson.put("intervals", reportDetailVO.getJsonString().get("intervals"));
			inputjson.put("granularity", reportDetailVO.getJsonString().get("granularity"));
		}
		reportDetailVO.setJsonString(inputjson);
		userReport = getSpecificReport(reportDetailVO);

		if (null != reportDetailVO.getUserName()) {
			userReport.setUserName(reportDetailVO.getUserName());
		} else {
			if (null != userTemplate.getUserTemplateKey())
				userReport.setUserTemplateId(userTemplate.getUserTemplateKey().getUsertemplateId());
			else
				userReport.setUserTemplateId(reportDetailVO.getUserTemplateId());
		}
		if(null!=userTemplate && null!=userTemplate.getReportName()){
			userReport.setReportName(userTemplate.getReportName());
		}
		
		// userReport.setReportConfiguration(mapper.convertValue(inputjson,
		// ReportRequest.class));
		// userReport.setReportConfiguration(mapper.convertValue(inputjson,
		// ReportRequest.class));

		return userReport;
	}

	public void addAggregationListInCounter(List<Counter> counterLIst, String granularity) {
		counterLIst.stream().forEach(cntr -> {
			Aggregation aggregation = cntr.getAggregation();
			if (null != aggregation) {
				Set<String> aggSet = cntr.getAggregationList();

				if (null == aggSet) {
					aggSet = new HashSet<String>();
					cntr.setAggregationList(aggSet);
				}

				//if (!granularity.equalsIgnoreCase(Constant.ALL)) {
					if (aggregation.getHourSum() == 1 && aggregation.getDaySum() == 1 && aggregation.getWeekSum() == 1
							&& aggregation.getMonthSum() == 1 && aggregation.getYearSum() == 1)
						cntr.getAggregationList().add("SUM");
					if (aggregation.getHourMin() == 1 && aggregation.getDayMin() == 1 && aggregation.getWeekMin() == 1
							&& aggregation.getMonthMin() == 1 && aggregation.getYearMin() == 1)
						cntr.getAggregationList().add("MIN");
					if (aggregation.getHourMax() == 1 && aggregation.getDayMax() == 1 && aggregation.getWeekMax() == 1
							&& aggregation.getMonthMax() == 1 && aggregation.getYearMax() == 1)
						cntr.getAggregationList().add("MAX");
					if (aggregation.getHourAvg() == 1 && aggregation.getDayAvg() == 1 && aggregation.getWeekAvg() == 1
							&& aggregation.getMonthAvg() == 1 && aggregation.getYearAvg() == 1)
						cntr.getAggregationList().add("AVG");
					if (null != aggregation.getHourFormula() && null != aggregation.getDayFormula()
							&& null != aggregation.getWeekFormula() && null != aggregation.getMonthFormula()
							&& null != aggregation.getYearFormula())
						cntr.getAggregationList().add("FORMULA");

				//}
			}
		});
	}

	public void prepareSeriesDataForGraph(Response druidResponse, UserReport userReport, String granularity) {
		Map<String, List<List<Object>>> dataMap = new HashMap<>();
		Map<String, Set<String>> propertyValuesMap = new HashMap<>();
		List<Counter> metricList = new ArrayList<>();
		ReportTemplate reportTemplate = ((ReportTemplate) userReport.getReportConfiguration());
		List<CounterGroup> counterGroupsWithCounterAndProperties = reportTemplate
				.getCounterGroupsWithCounterAndProperties();

		Set<String> dimensionList = getUniqueProperties(counterGroupsWithCounterAndProperties);
		if (null != reportTemplate && null != reportTemplate.getCounterGroupsWithCounterAndProperties()) {
			counterGroupsWithCounterAndProperties.forEach(countergroup -> {
				if (null != countergroup.getCounterList()) {
					countergroup.getCounterList().forEach(counter -> {
						Counter countr = new Counter();
						CounterKey key = new CounterKey();
						CounterGroup cg = new CounterGroup();

						if (null != counter) {

							countr.setAggregationList(counter.getAggregationList());
							countr.setCounterKey(counter.getCounterKey());
							countr.setCounterUnit(counter.getCounterUnit());
							countr.setLogicalName(counter.getLogicalName());
							cg.setCounterGroupName(countergroup.getCounterGroupName());
							key.setCounterGroup(cg);
							countr.setCounterKey(key);
							String mergedId = counter.getCounterKey().getCounterId() + "__"
									+ countergroup.getCounterGroupId();
							countr.getCounterKey().setCounterId(mergedId);

							metricList.add(countr);
						}
					});
				}
				if (null != countergroup.getKpis()) {
					countergroup.getKpis().forEach(kpi -> {
						Counter kpic = new Counter();
						CounterKey key = new CounterKey();
						CounterGroup cg = new CounterGroup();

						if (null != kpi) {

							kpic.setAggregationList(kpi.getAggregationList());
							kpic.setCounterKey(kpi.getCounterKey());
							kpic.setCounterUnit(kpi.getCounterUnit());
							kpic.setLogicalName(kpi.getLogicalName());
							cg.setCounterGroupName(countergroup.getCounterGroupName());
							key.setCounterGroup(cg);
							kpic.setCounterKey(key);
							String mergedId = kpi.getCounterKey().getCounterId() + "__"
									+ countergroup.getCounterGroupId();
							kpic.getCounterKey().setCounterId(mergedId);

							metricList.add(kpic);
						}
					});
				}

			});
		}

		if (null != metricList) {
			metricList.forEach(metric -> {
				List<String> aggregationList = new ArrayList<>();
				if (null != metric.getAggregationList()) {
					aggregationList.addAll(metric.getAggregationList());
				}
				if (null != druidResponse && null != druidResponse.getEvents()
						&& !druidResponse.getEvents().isEmpty()) {
					druidResponse.getEvents().forEach(event -> {
						String metricName = "";
						/*if (aggregationList != null && !aggregationList.isEmpty()) {
							metricName = aggregationList.get(0) + "_" + metric.getCounterKey().getCounterId();
						} else {
							metricName = metric.getCounterKey().getCounterId();
						}*/
						
						if(null!=granularity && !granularity.trim().isEmpty()){
							if(!granularity.equals(Constant.ALL.toUpperCase()))
								metricName = aggregationList.get(0) + "_" + metric.getCounterKey().getCounterId();
							else
								metricName = metric.getCounterKey().getCounterId();
						}						
						
						
						if (null != event && !event.isEmpty()) {
							String key = "";
							String mapKey = null;
							Set<String> values = null;
							if (null != dimensionList) {
								for (String dimension : dimensionList) {

									if (null != (String) event.get(dimension)) {
										if (key.isEmpty()) {
											key = key + (String) event.get(dimension);
										} else {
											key = key + "_" + (String) event.get(dimension);
										}

										if (null != propertyValuesMap.get(dimension)) {
											values = propertyValuesMap.get(dimension);
											values.add((String) event.get(dimension));

										} else {
											values = new HashSet<>();
											values.add((String) event.get(dimension));
										}
										propertyValuesMap.put(dimension, values);
									}

								}
							}
						
								if (aggregationList != null && !aggregationList.isEmpty()) {
									mapKey = key + "_" + aggregationList.get(0) + "_"
											+ metric.getCounterKey().getCounterGroup().getCounterGroupName() + "_"
											+ metric.getLogicalName() + '(' + metric.getCounterUnit() + ')';
								} else {
									if (key.isEmpty()) {
										mapKey = metric.getCounterKey().getCounterGroup().getCounterGroupName() + "_"
												+ metric.getLogicalName() + '(' + metric.getCounterUnit() + ')';
									} else {
										mapKey = key + "_"
												+ metric.getCounterKey().getCounterGroup().getCounterGroupName() + "_"
												+ metric.getLogicalName() + '(' + metric.getCounterUnit() + ')';
									}
								}
							
							List<Object> innerList = new ArrayList<>();
							List<List<Object>> outerList = null;

							Date date;
							try {
														    
							    LocalDateTime ldt = LocalDateTime.parse((String) event.get("timestamp"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
						        ZoneId zoneId = ZoneId.of("Asia/Kolkata");
						        Calendar calendar = Calendar.getInstance();
								calendar.setTimeZone(TimeZone.getTimeZone(zoneId));
								date = Date.from(ldt.atZone(ZoneId.ofOffset("GMT", ZoneOffset.UTC)).toInstant());
							    calendar.setTime(date);
							    long epoch = calendar.getTimeInMillis();
							    innerList.add(epoch);
								
							} catch (Exception e) {
								e.printStackTrace();
							}
							innerList.add(event.get(metricName));
							if (null != event.get(metricName)) {
								if (null != dataMap.get(mapKey)) {
									outerList = dataMap.get(mapKey);
									outerList.add(innerList);
								} else {
									outerList = new ArrayList<>();
									outerList.add(innerList);
								}
								if (null != outerList) {
									dataMap.put(mapKey, outerList);
								}
							}
						}
					});
			}
			});
		}

		List<SeriesData> seriesDataList = new ArrayList<>();
		
		TreeMap<String, List<List<Object>>>mapSorted = new TreeMap<>(dataMap);
		mapSorted.entrySet().forEach(data -> {
			SeriesData seriesData = new SeriesData();
			seriesData.setName(data.getKey());
			seriesData.setData(data.getValue());
			seriesData.setId(data.getKey());
			seriesDataList.add(seriesData);

		}); 
		GraphData graphData = new GraphData();
		graphData.setPropertyValuesMap(propertyValuesMap);
		graphData.setSeriesData(seriesDataList);
		System.out.println("propsValuse  " + propertyValuesMap);
		userReport.setGraphData(graphData);
	}

	public List<String> getPropertiesByMetrics(List<Metric> metrics) throws Exception {
		List<String> counters = new ArrayList<String>();

		metrics.stream().forEach(metric -> {
			String counterId = new String();
			if (metric.getId().contains(Constant.SUM) || metric.getId().contains(Constant.MIN)
					|| metric.getId().contains(Constant.MAX) || metric.getId().contains(Constant.AVG)) {
				counterId = metric.getId().replaceAll(Constant.SUM + Constant.UNDERSCORE, "")
						.replaceAll(Constant.MIN + Constant.UNDERSCORE, "")
						.replaceAll(Constant.MAX + Constant.UNDERSCORE, "")
						.replaceAll(Constant.AVG + Constant.UNDERSCORE, "");
				counters.add(counterId);
			} else
				counters.add(metric.getId());

		});
		return getPropertiesByCountersStr(counters);
	}

	public List<String> getPropertiesByCountersStr(List<String> counters) throws Exception {
		return propertiesService.propertiesValueByCounter(counters);
	}

	public List<Properties> propertyDetailsByCounterGroup(List<CounterGroup> counterGroup) throws Exception {
		return propertiesService.propertyDetailsByCounterGroup(counterGroup);
	}

	/**
	 * @param reportRequest
	 * @param properties
	 */
	private void prepareDimensionsForEmptyRequest(ReportRequest reportRequest, ReportDetailVO reportDetailVO,
			List<Properties> properties) {
		if (null != reportRequest) {
			if (null != reportRequest.getConfiguration() && null != reportRequest.getConfiguration().getDimensions()
					&& reportRequest.getConfiguration().getDimensions().isEmpty()) {
				properties.stream().forEach(prop -> {
					Attribute attribute = new Attribute();
					attribute.setId(prop.getPropertyKey().getPropertyId());
					attribute.setDisplayName(prop.getPropertyKey().getPropertyId());
					reportRequest.getConfiguration().getDimensions().add(attribute);
					// reportDetailVO.get
				});
			}
		}
	}

	@Override
	public UserReport updateUserReport(ReportDetailVO reportDetailVO) throws Exception {

		ReportTemplate reportTemplate = mapper.convertValue(reportDetailVO.getJsonString(), ReportTemplate.class);
		UserReport userreport = new UserReport();
		UserDetail userDetail = new UserDetail();
		if (null != reportDetailVO.getUserName()) {
			userreport.setUserName(reportDetailVO.getUserName());
			// load user
			userDetail = userDetailService.loadUserByName(reportDetailVO.getUserName());
		}
		ObjectMapper mapperObj = new ObjectMapper();
		String reportRequestJson = mapperObj.writeValueAsString(reportTemplate);
		if (reportDetailVO.getIsDashboardReport()) {

			DashboardTemplate dashtemplate = new DashboardTemplate();
			dashtemplate.setReportDetail(reportRequestJson.getBytes());
			if (null != reportTemplate.getConfiguration().getName())
				dashtemplate.setReportName(reportTemplate.getConfiguration().getName());

			
			dashtemplate.setDashboardtemplateId(reportDetailVO.getUserTemplateId());
	
			 dashboardTemplateService.updateDashboardTemplate(dashtemplate);
			userreport.setUserTemplateId(dashtemplate.getDashboardtemplateId());
			userreport.setReportName(dashtemplate.getReportName());
			
		}
		else { 
		UserTemplate userTemplate = new UserTemplate();
		// userTemplate.setReportDetail(reportRequest);
		
		// System.out.println(reportRequestJson);
		userTemplate.setReportDetail(reportRequestJson.getBytes());
		// userTemplate.setReportDetail((mapper.writeValueAsBytes(reportDetailVO.getJsonString())));

		if (null != reportTemplate.getConfiguration().getName())
			userTemplate.setReportName(reportTemplate.getConfiguration().getName());

		UserTemplateKey userTemplateKey = new UserTemplateKey();
		userTemplateKey.setUsertemplateId(reportDetailVO.getUserTemplateId());
		userTemplateKey.setUserDetail(userDetail);
		userTemplateKey.setUsertemplateId(reportDetailVO.getUserTemplateId());
		userTemplate.setUserTemplateKey(userTemplateKey);
		int updatedRow = userTemplateService.updateUserTemplate(userTemplate);
		userreport.setUserTemplateId(userTemplate.getUserTemplateKey().getUsertemplateId());
		userreport.setReportName(userTemplate.getReportName());
		}

		return userreport;

	}
	

	public Set<String> getUniqueProperties(List<CounterGroup> counterGrpList)
	{
		Set<String> uniqueProperties = new HashSet();
		counterGrpList.stream().forEach(cg -> {
			cg.getProperties().stream().forEach(prop -> {
				uniqueProperties.add(prop.getPropertyName());
			});
		});
		
		return uniqueProperties;

	}
	
	public UserReport getReportAccordingToFilter(ReportDetailVO reportDetailVO) throws Exception{
		
		UserReport userReport = new UserReport();
		UserTemplate userTemplate = new UserTemplate();
		if (null != reportDetailVO && null != reportDetailVO.getIsDashboardReport()
				&& reportDetailVO.getIsDashboardReport()) {
			DashboardDetailVO dashboardDetailVO = new DashboardDetailVO();
			dashboardDetailVO.setDashboardId(reportDetailVO.getDashboardId());
			dashboardDetailVO.setDashboardTemplateId(reportDetailVO.getUserTemplateId());
			dashboardDetailVO.setSubDashboardId(reportDetailVO.getSubDashboardId());
			DashboardTemplate dashboardTemplate = dashboardTemplateService.searchDashboardTemplate(dashboardDetailVO);
			if (null != dashboardTemplate)
				BeanUtils.copyProperties(dashboardTemplate, userTemplate);

		} else {
			userTemplate = userTemplateService.searchUserTemplate(reportDetailVO);
		}

		Map<String, Object> fetchedinputjson = mapper.readValue(new String(userTemplate.getReportDetail()),
				new TypeReference<Map<String, Object>>() {
				});
		if(null != fetchedinputjson)
		{
		fetchedinputjson.putAll(reportDetailVO.getJsonString());
		reportDetailVO.setJsonString(fetchedinputjson);
		userReport = getSpecificReport(reportDetailVO);
		}
		if (null != reportDetailVO.getUserName()) {
			userReport.setUserName(reportDetailVO.getUserName());
		} else {
			if (null != userTemplate.getUserTemplateKey())
				userReport.setUserTemplateId(userTemplate.getUserTemplateKey().getUsertemplateId());
			else
				userReport.setUserTemplateId(reportDetailVO.getUserTemplateId());
		}
		// userReport.setReportConfiguration(mapper.convertValue(inputjson,
		// ReportRequest.class));
		// userReport.setReportConfiguration(mapper.convertValue(inputjson,
		// ReportRequest.class));

		return userReport;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.SUPPORTS)
	public void deleteReport(ReportDetailVO reportDetailVO) throws Exception {
		UserTemplate userTemplate = new UserTemplate();
		UserDetail userDetail = new UserDetail();
		if (null != reportDetailVO.getUserName()) {
			userDetail = userDetailService.loadUserByName(reportDetailVO.getUserName());
		}
		if (null != reportDetailVO && null != reportDetailVO.getIsDashboardReport()
				&& reportDetailVO.getIsDashboardReport()) {
			//code to be impleneted for dashboard template delete
		} else {
			UserTemplateKey userTemplateKey = new UserTemplateKey();
			userTemplateKey.setUsertemplateId(reportDetailVO.getUserTemplateId());
			userTemplateKey.setUserDetail(userDetail);
			userTemplate.setUserTemplateKey(userTemplateKey);
		}

		userTemplateService.deleteUserTemplate(userTemplate);
	}


	@Override
	public String getDruidURI() {
		return druidHelper.getDruidURI();
	}
}
