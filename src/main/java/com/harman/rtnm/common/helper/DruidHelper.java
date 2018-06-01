package com.harman.rtnm.common.helper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.harman.dyns.model.common.Attribute;
import com.harman.dyns.model.common.Configuration;
import com.harman.dyns.model.common.Metric;
import com.harman.dyns.model.druid.request.DruidBaseRequest.DruidBaseRequestBuilder;
import com.harman.rtnm.client.druid.DruidRestClient;
import com.harman.dyns.model.druid.request.DruidGroupByRequest;
import com.harman.dyns.model.druid.request.DruidSelectRequest;
import com.harman.dyns.model.druid.request.DruidTopNRequest;
import com.harman.dyns.model.druid.request.ReportRequest;
import com.harman.rtnm.common.constant.Constant;
import com.harman.rtnm.common.constant.ConstantCollection;
import com.harman.rtnm.common.constant.DateConstant;
import com.harman.rtnm.common.property.CommonAttribute;
import com.harman.rtnm.common.property.DruidAttribute;
import com.harman.rtnm.model.Counter;
import com.harman.rtnm.model.Datasource;
import com.harman.rtnm.model.FormulaMetric;
import com.harman.rtnm.model.request.ReportTemplate;
import com.harman.rtnm.model.response.DataSchema;
import com.harman.rtnm.model.response.DatasourceJobPojo;
import com.harman.rtnm.model.response.DruidGroupByResponse;
import com.harman.rtnm.model.response.DruidSelectResponse;
import com.harman.rtnm.model.response.DruidTimeSeriesResponse;
import com.harman.rtnm.model.response.DruidTopNResponse;
import com.harman.rtnm.model.response.KafkaJsonPojo;
import com.harman.rtnm.model.response.Response;
import com.harman.rtnm.service.CounterService;
import com.harman.rtnm.service.DatasourceService;
import com.harman.rtnm.service.ElementService;
import com.harman.rtnm.util.ObjectFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javassist.bytecode.stackmap.BasicBlock.Catch;

@Component
public class DruidHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(DruidHelper.class);

	@Autowired
	CommonAttribute commonAttribute;

	@Autowired
	DatasourceService datasourceService;

	@Autowired
	ElementService elementService;

	@Autowired
	DruidAttribute druidAttribute;

	@Autowired
	CounterService counterService;

	@Autowired
	ResponseParseHelper responseParseHelper;

	static ScriptEngine engine = new ScriptEngineManager().getEngineByExtension("js");
	static Map<String, String> druidGranularityMap = new HashMap<String, String>();

	ObjectMapper mapper = new ObjectMapper();

	static {
		DruidHelper.druidGranularityMap.put("ALL", "minute");
		DruidHelper.druidGranularityMap.put("HOURLY", "hour");
		DruidHelper.druidGranularityMap.put("DAILY", "day");
		DruidHelper.druidGranularityMap.put("WEEKLY", "week");
		DruidHelper.druidGranularityMap.put("MONTHLY", "month");
		DruidHelper.druidGranularityMap.put("YEARLY", "year");
	}

	private DruidRestClient<?> druidRestClient = ObjectFactory.getDruidRestClient();

	public String getDruidURI() {
		return "http://" + druidAttribute.getDruidBrokerHost() + druidAttribute.getDruidBrokerPort()
				+ druidAttribute.getDruidBrokerBaseURL() + druidAttribute.getDruidBrokerJsonResorceURL();
		// return "http://localhost:7709/druid/v2/?pretty";

	}

	// EXAMPLE : "http://172.25.182.145:8090/druid/indexer/v1/supervisor"
	public String getSupervisorURI() {
		return "http://" + druidAttribute.getDruidSupervisorHost() + ":" + druidAttribute.getDruidSupervisorPort()
				+ druidAttribute.getDruidSupervisorJsonResorceURL();
	}

	/**
	 * Method to generate date string from inputInterval
	 * 
	 * Type
	 * 
	 * @param interval
	 *            like "current 30 minutes", "previous 1 hour", "last 1 day"
	 * @return interval string like
	 *         2016-10-14T00:00:00.000Z/2016-10-15T00:00:00.000Z
	 * @throws Exception
	 */
	public String generateIntervalString(String interval) throws Exception {
		LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of(commonAttribute.getZoneId()));

		if (interval.toLowerCase().contains(Constant.CURRENT)) {
			return DateHelper.generateDruidIntervalForCurrent(currentDateTime, interval);
		} else if (interval.toLowerCase().contains(Constant.PREVIOUS)) {
			return DateHelper.generateDruidIntervalForPrevious(currentDateTime, interval);
		} else if (interval.toLowerCase().contains(Constant.LAST)) {
			return DateHelper.generateDruidIntervalForLast(currentDateTime, interval);
		} else if (interval.equalsIgnoreCase(DateConstant.TODAY) || interval.equalsIgnoreCase(DateConstant.YESTERDAY)) {
			return DateHelper.generateDruidIntervalForYesterdayToday(currentDateTime, interval);
		}

		return interval;
	}

	/**
	 * 
	 * @param policyTaskType
	 *            like 'daily, weekly etc'
	 * @return
	 * @throws Exception
	 */
	public String generateDateIntervalFromPolicy(Map<String, Object> taskParamValMap) throws Exception {
		LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of(commonAttribute.getZoneId()));
		int month;
		int dayOfMonth;
		int hour;
		int minute;

		if (!StringUtils.isEmpty(taskParamValMap.get("taskExecutionDate")))
			dayOfMonth = Integer.valueOf((String) taskParamValMap.get("taskExecutionDate"));
		else
			dayOfMonth = currentDateTime.getDayOfMonth();

		if (!StringUtils.isEmpty(taskParamValMap.get("taskExecutionMonth")))
			month = Integer.valueOf((String) taskParamValMap.get("taskExecutionMonth"));
		else
			month = currentDateTime.getMonthValue();

		if (!StringUtils.isEmpty(taskParamValMap.get("taskExecutionTime"))) {
			String time = (String) taskParamValMap.get("taskExecutionTime");
			String[] inputTimeSplit = time.split(":");
			hour = Integer.valueOf(inputTimeSplit[0]);
			minute = Integer.valueOf(inputTimeSplit[1]);
		} else {
			hour = currentDateTime.getHour();
			minute = currentDateTime.getMinute();
		}

		currentDateTime = currentDateTime.of(currentDateTime.getYear(), month, dayOfMonth, hour, minute,
				currentDateTime.getSecond(), currentDateTime.getNano());
		return DateHelper.generateDateIntervalFromPolicy(currentDateTime, (String) taskParamValMap.get("taskType"));
	}

	public void prepareTopNResponse(List<Response> responseList, String metricName, ReportRequest reportRequest,
			DruidBaseRequestBuilder druidBaseRequestBuilder) throws Exception {
		DruidTopNRequest topnRequest = new DruidTopNRequest.TopNRequestBuilder(metricName, druidBaseRequestBuilder,
				reportRequest).build();
		DruidTopNResponse[] druidTopNResponseArray = (DruidTopNResponse[]) druidRestClient.druidResponse(getDruidURI(),
				topnRequest);

		for (DruidTopNResponse druidTopNResponse : druidTopNResponseArray) {
			Response response = new Response();
			List<Map<String, Object>> resultList = druidTopNResponse.getResult();
			resultList.forEach(resultMap -> {
				resultMap.put(Constant.TIMESTAMP_DIMENSION, druidTopNResponse.getTimestamp());

				// Enriching druid response
				resultMap = getRespWithPropertiesValues(reportRequest, resultMap);
			});
			//// response.setEvents(resultList);
			responseList.add(response);
		}
	}

	/**
	 * 
	 * @param reportRequest
	 * @return
	 * @throws Exception
	 */
	public Response prepareDruidResponse(ReportTemplate reportTemplate, Map<String, FormulaMetric> metricmap,
			List<String> properties) throws Exception {
		
		ReportRequest reportRequest = reportTemplate;
		Response response = new Response();
		String datasourceName = null;
		String queryType = null;

		Datasource datasource = datasourceService.getDatasourceByGranularity(reportRequest.getGranularity(),
				reportRequest.getConfiguration().getDeviceType());
		datasourceName = datasource.getDatasource().trim();

		String dataType = reportRequest.getReportDataType();
		if (null != dataType && dataType.equalsIgnoreCase(Constant.RAW)) {
			queryType = Constant.SELECT;
		} else if (null != dataType && dataType.equalsIgnoreCase(Constant.GROUPED)) {
			queryType = Constant.GROUP_BY;
		}

		String granularity = new String("ALL");
		if (queryType.equalsIgnoreCase(Constant.GROUP_BY) && !reportRequest.getConfiguration().getSingleBucketResponse()) {
			granularity = DruidHelper.druidGranularityMap.get((reportRequest.getGranularity()).trim().toUpperCase());
		}



		DruidBaseRequestBuilder druidBaseRequestBuilder = new DruidBaseRequestBuilder(queryType.trim(),
				datasourceName.trim(), granularity, generateIntervalString(reportRequest.getIntervals()));

		if (Constant.SELECT.equalsIgnoreCase(queryType.trim())) {
			prepareSelectResponse(reportTemplate, metricmap, response, druidBaseRequestBuilder, properties);
		} else if (Constant.GROUP_BY.equalsIgnoreCase(queryType.trim())) {
			prepareGroupByResponse(reportTemplate, metricmap, response, druidBaseRequestBuilder, properties);
		} /*
			 * else if (Constant.TIMESERIES.equalsIgnoreCase(queryType.trim()))
			 * { prepareTimeseriesResponse(reportRequest, metricmap, response,
			 * druidBaseRequestBuilder); }
			 */
		return response;

	}

	
	
	/**
	 * @param reportTemplate
	 * @param metricmap
	 * @param resp
	 * @param druidBaseRequestBuilder
	 * @throws Exception
	 */
	private void prepareSelectResponse(ReportTemplate reportTemplate, Map<String, FormulaMetric> metricmap,
			Response resp, DruidBaseRequestBuilder druidBaseRequestBuilder, List<String> properties) throws Exception {
		DruidSelectRequest druidSelectRequest = new DruidSelectRequest.SelectRequestBuilder(druidBaseRequestBuilder,
				reportTemplate).build();
		DruidSelectResponse[] druidSelectResponse = (DruidSelectResponse[]) druidRestClient.druidResponse(getDruidURI(),
				druidSelectRequest);
		if (null != druidSelectResponse && druidSelectResponse.length > 0) {
			responseParseHelper.populateDruidSelectResponse(metricmap, reportTemplate, resp, druidSelectResponse,
					properties);
		}
	}

	/**
	 * @param reportTemplate
	 * @param metricmap
	 * @param resp
	 * @param druidBaseRequestBuilder
	 * @throws Exception
	 */
	private void prepareGroupByResponse(ReportTemplate reportTemplate, Map<String, FormulaMetric> metricmap,
			Response resp, DruidBaseRequestBuilder druidBaseRequestBuilder, List<String> properties) throws Exception {
		reportTemplate.getConfiguration().setGroupByDimensions(properties);
		DruidGroupByRequest druidSelectRequest = new DruidGroupByRequest.GroupByRequestBuilder(druidBaseRequestBuilder,
				reportTemplate).build();
		DruidGroupByResponse[] druidGroupByResponse = (DruidGroupByResponse[]) druidRestClient
				.druidResponse(getDruidURI(), druidSelectRequest);

		if (null != druidGroupByResponse && druidGroupByResponse.length > 0) {
			responseParseHelper.populateDruidGroupByResonse(metricmap, reportTemplate, resp, druidGroupByResponse,
					properties);
		}
	}

	private void prepareTimeseriesResponse(ReportRequest reportRequest, Map<String, FormulaMetric> metricmap,
			Response resp, DruidBaseRequestBuilder druidBaseRequestBuilder) throws Exception {
		DruidGroupByRequest druidSelectRequest = new DruidGroupByRequest.GroupByRequestBuilder(druidBaseRequestBuilder,
				reportRequest).build();
		DruidTimeSeriesResponse[] druidGroupByResponse = (DruidTimeSeriesResponse[]) druidRestClient
				.druidResponse(getDruidURI(), druidSelectRequest);

		if (null != druidGroupByResponse && druidGroupByResponse.length > 0) {
			// responseParseHelper.populateDruidGroupByResonse(metricmap,
			// reportRequest.getGranularity(), resp, druidGroupByResponse);
		}
	}

	/**
	 * 
	 * @param reportRequest
	 * @param event
	 * @return
	 */
	private Map<String, Object> getRespWithPropertiesValues(ReportRequest reportRequest, Map<String, Object> event) {
		Map<String, Object> filterColums = new HashMap<>();
		List<Attribute> properties = reportRequest.getConfiguration().getProperties();
		Attribute entity = reportRequest.getConfiguration().getEntity();
		List<Attribute> dimensions = reportRequest.getConfiguration().getDimensions();
		dimensions.forEach((dimension) -> {
			String columValue = String.valueOf(event.get(dimension.getId()));
			filterColums.put(dimension.getId(), columValue);
		});
		/*
		 * if (properties != null && !properties.isEmpty()) { Map<String,
		 * Object> propertResults =
		 * propertyCacheService.getPropertyValues(filterColums, properties,
		 * entity); event.putAll(propertResults); }
		 */

		return event;
	}

	public List<Metric> getCorrespondingKpi(List<Metric> metricList, Map<String, FormulaMetric> metricmap,
			String granularity, String queryType) {
		if (null != metricList) {
			metricList.forEach(m -> {
				String kpi = m.getId();
				m.setAggregationType("SUM");

				metricmap.put(kpi, null);
			});
		}
		return metricList;
	}

	/**
	 * 
	 * @param counterlist
	 * @param metricmap
	 * @return
	 */
	// NEED TO CHANGE
	public List<Metric> getCorrespondingCounters(List<Counter> counterlist, Map<String, FormulaMetric> metricmap,
			String granularity, String queryType) {
		List<Metric> metrics = new ArrayList<>();

		String granularityType = getGranularityType(granularity);
		if (!counterlist.isEmpty()) {
			counterlist.stream().forEach(counter -> {

				Map<String, String> aggregationMap = mapper.convertValue(counter.getAggregation(),
						new TypeReference<Map<String, String>>() {
				});			
				for (Map.Entry<String, String> aggregationVal : aggregationMap.entrySet()) {

					if (null != granularityType && aggregationVal.getKey().contains(granularityType)) {
					
						if (aggregationVal.getKey().contains("Formula") && !granularity.equalsIgnoreCase("ALL")) {

							if (null != aggregationVal.getValue()) {
								String formula = fromlaWithAggregatdCounter(aggregationVal.getValue());
								FormulaMetric formulaMetric = new FormulaMetric();								
								List<String> metricList = new ArrayList<>();								

								formulaMetric.setFormula(formula);
								formulaMetric.setFormulaWithValue(formulaMetric.getFormula());
								formulaMetric.setFormulaType(Constant.FORMULA);
								// for groupBy take aggregations of counters
								// it
								// may be dual aggregations
								Map<String, Set<String>> countersWithAgg = getCountersWithAggregations(
										aggregationVal.getValue());

								if (null != countersWithAgg && !countersWithAgg.isEmpty()) {
									countersWithAgg.forEach((key, value) -> {
										if (null != value && !((Set) value).isEmpty()) {
											value.stream().forEach(agg -> {
												Metric metric = new Metric();
												metric.setId(agg + Constant.UNDERSCORE + key);
												metric.setAggregationType(agg);
												metrics.add(metric);
												metricList.add(agg + Constant.UNDERSCORE + key);
											});
										}

									});
								}
								formulaMetric.setMetricList(metricList);
								metricmap.put(String.valueOf(counter.getCounterKey().getCounterId()), formulaMetric);

							}

							
						} else if (aggregationVal.getKey().contains("Avg") && !granularity.equalsIgnoreCase("ALL")) {

							if (Integer.parseInt(aggregationVal.getValue()) == 1) {
								if (!StringUtils.isEmpty(counter.getCounterKey().getCounterId())) {
									FormulaMetric formula = new FormulaMetric();
									Metric metric = new Metric();
									metric.setId(Constant.SUM + Constant.UNDERSCORE
											+ counter.getCounterKey().getCounterId());
									metric.setAggregationType("AVG");
									Metric count_metric = new Metric();
									if (queryType.equalsIgnoreCase(Constant.SELECT)) {
										count_metric.setId(String.valueOf(counter.getCounterKey().getCounterId()));
										count_metric.setAggregationType(Constant.COUNT);
									} else if (queryType.equalsIgnoreCase(Constant.GROUP_BY)) {
										count_metric.setId(Constant.COUNT.toUpperCase() + Constant.UNDERSCORE
												+ counter.getCounterKey().getCounterId());
										count_metric.setAggregationType(Constant.COUNT);
									} else {
										count_metric.setId(Constant.COUNT.toUpperCase() + Constant.UNDERSCORE
												+ counter.getCounterKey().getCounterId());
										count_metric.setAggregationType(Constant.SUM_COUNT);
									}
									metrics.add(metric);
									metrics.add(count_metric);
									String druidCounterId = new String("");
									if (queryType.equalsIgnoreCase(Constant.GROUP_BY)) {
										druidCounterId = Constant.AVG + Constant.UNDERSCORE
												+ counter.getCounterKey().getCounterId();
									}
									// if
									// (!queryType.equalsIgnoreCase(Constant.GROUP_BY))
									// {
									formula.setMetricList(new ArrayList<>());
									String counterid = counter.getCounterKey().getCounterId();
									String count = Constant.COUNT_STREAM.toUpperCase()
											+ counter.getCounterKey().getCounterId();
									String sumId = new String();

									if (granularity.equalsIgnoreCase(Constant.ALL))
										sumId = counterid;
									else
										sumId = Constant.SUM + Constant.UNDERSCORE+ counter.getCounterKey().getCounterId();

									/*
									 * if (granularity.equalsIgnoreCase(
									 * Constant.ALL)) druidCounterId =
									 * counterid; else druidCounterId =
									 * Constant.SUM + Constant.UNDERSCORE +
									 * counterid;
									 */
									formula.getMetricList().add(sumId);
									formula.getMetricList().add(count);

									formula.setFormula(sumId + "/" + count);
									formula.setFormulaWithValue(formula.getFormula());
									formula.setFormulaType("AVG");
									/*
									 * } else { formula = null; }
									 */

									metricmap.put(druidCounterId, formula);

								}
							}
						} else {
							if (!StringUtils.isEmpty(counter.getCounterKey().getCounterId())) {
								Metric metric = null;

								if ((aggregationVal.getKey().contains("Formula") && null != aggregationVal.getValue()
										&& granularity.equalsIgnoreCase(Constant.ALL))
										|| (aggregationVal.getKey().contains("Sum")
												&& Integer.parseInt(aggregationVal.getValue()) == 1)) {
									metric = populateMetricData(counter, Constant.SUM, Constant.SUM, metricmap,
											granularity);
								} else if (aggregationVal.getKey().contains("Min")
										&& Integer.parseInt(aggregationVal.getValue()) == 1) {
									metric = populateMetricData(counter, Constant.MIN, Constant.MIN, metricmap,
											granularity);
								} else if (aggregationVal.getKey().contains("Max")
										&& Integer.parseInt(aggregationVal.getValue()) == 1) {
									metric = populateMetricData(counter, Constant.MAX, Constant.MAX, metricmap,
											granularity);
								} else if (aggregationVal.getKey().contains("Avg")
										&& Integer.parseInt(aggregationVal.getValue()) == 1) {
									metric = populateMetricData(counter, Constant.AVG, Constant.SUM, metricmap,
											granularity);
								}

								if (null != metric) {
									metrics.add(metric);
									// metricmap.put(String.valueOf(counter.getCounterKey().getCounterId()),
									// null);
									// System.out.println(metricmap.size());
								}
							}
						}
						
					}
				}
			});
		}

		List<Metric> uniqueMetrics = metrics.stream().filter(distinctByKey(p -> p.getId()))
				.collect(Collectors.toList());
		return uniqueMetrics;
	}

	 /* 
	  * 
	 * @param metricsForDruid
	 * @return
	 */
	public void applyMultipleAggs(List<Metric> metricsForDruid, Configuration configuration, Map<String, FormulaMetric> metricMap) {
		List<Metric> listReqstMetricList = configuration.getMetrics();

		AtomicBoolean existsAlready  = new AtomicBoolean(false);
		listReqstMetricList.forEach(metric -> {

			/*
			 * need to ignore the already existing aggregations 
			 * loaded from the database.
			 */
			for (Metric m : metricsForDruid) {
				existsAlready.set(m.getId().equalsIgnoreCase(metric.getId()) && m.getAggregationType().equalsIgnoreCase(metric.getAggregationType()));
				if(existsAlready.get()) {
					break;
				}
			}

			if(!existsAlready.get()) {
				metricsForDruid.add(metric);
				metricMap.put(metric.getId(), null);
			}
		});		
	}
	
	public void processRawAggregatedFormulas(List<Metric> rawMetricList, Map<String, FormulaMetric> metricmap, List<Metric> metricsForDruid) {
		rawMetricList.stream().forEach(rawMetric ->  {
			String metricFormula = rawMetric.getFormula();			
			if (StringUtils.hasText(metricFormula) && rawMetric.getAggregationType().equalsIgnoreCase(Constant.FORMULA)) {
				String formula = fromlaWithAggregatdCounter(metricFormula);

				FormulaMetric formulaMetric = new FormulaMetric();								
				List<String> metricList = new ArrayList<>();								

				formulaMetric.setFormula(formula);
				formulaMetric.setFormulaWithValue(formulaMetric.getFormula());
				formulaMetric.setFormulaType(Constant.FORMULA);
				System.out.println(" formula is : " + formula +" formula with value : " + formulaMetric.getFormulaWithValue());
				// for groupBy take aggregations of counters
				// it
				// may be dual aggregations
				Map<String, Set<String>> countersWithAgg = getCountersWithAggregations(formula);

				if (null != countersWithAgg && !countersWithAgg.isEmpty()) {
					countersWithAgg.forEach((key, value) -> {
						if (null != value && !((Set) value).isEmpty()) {
							value.stream().forEach(agg -> {
								Metric metric = new Metric();
								metric.setId(agg + Constant.UNDERSCORE + key);
								metric.setAggregationType(agg);
								metricsForDruid.add(metric);
								metricList.add(agg + Constant.UNDERSCORE + key);
							});
						}

					});
				}
				formulaMetric.setMetricList(metricList);
				metricmap.put(String.valueOf(rawMetric.getId()), formulaMetric);

			}
		});		
	}


	/**
	 * @param counter
	 * @return
	 */
	private Metric populateMetricData(Counter counter, String aggregation, String druidAggregation,
			Map<String, FormulaMetric> metricmap, String granularity) {
		String druidCounterName = new String();
		if (granularity.equalsIgnoreCase(Constant.ALL))
			druidCounterName = String.valueOf(counter.getCounterKey().getCounterId());
		else
			druidCounterName = druidAggregation + Constant.UNDERSCORE + counter.getCounterKey().getCounterId();
		Metric metric;
		metric = new Metric();
		metric.setId(druidCounterName);
		metric.setAggregationType(aggregation.trim().toUpperCase());
		metricmap.put(druidCounterName, null);
		return metric;
	}

	/**
	 * @param granularity
	 * @param formulaUGW
	 */
	private String ugwFormulaWithPeriod(String granularity, String formulaUGW) {
		if (formulaUGW.contains("sum(this.PERIOD_LEN)"))
			formulaUGW = formulaUGW.replace("sum(this.PERIOD_LEN)",
					String.valueOf(ConstantCollection.periodCalculationOfUgw.get(granularity.toUpperCase())));
		return formulaUGW;
	}

	private static String getGranularityType(String granularity) {
		if (granularity.equalsIgnoreCase(DateConstant.HOURLY))
			return DateConstant.HOUR;
		else if (granularity.equalsIgnoreCase(DateConstant.DAILY))
			return DateConstant.DAY;
		else if (granularity.equalsIgnoreCase(DateConstant.WEEKLY))
			return DateConstant.WEEK;
		else if (granularity.equalsIgnoreCase(DateConstant.MONTHLY))
			return DateConstant.MONTH;
		else if (granularity.equalsIgnoreCase(DateConstant.YEARLY))
			return DateConstant.YEAR;
		else if (granularity.equalsIgnoreCase(DateConstant.ALL))
			return DateConstant.HOUR;
		else
			return null;
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		final Set<Object> seen = new HashSet<>();
		return t -> seen.add(keyExtractor.apply(t));
	}

	public Map<String, Set<String>> getCountersWithAggregations(String formula) {
		List<String> parsedCounters = Stream.of(formula.replace("this.C", "").replace("this.", "").trim().split("\\W"))
				.filter(c -> c.length() > 2).collect(Collectors.toList());
		Map<String, Set<String>> countersAggtype = new HashMap<>();
		for (int i = 0; i < parsedCounters.size(); i++) {
			if (isAlpha(parsedCounters.get(i))) {
				Set<String> aggSet = null;				
				if (null == countersAggtype.get(parsedCounters.get(i + 1))) {
					aggSet = new HashSet<>();
				} else {
					aggSet = countersAggtype.get(parsedCounters.get(i + 1));
				}
				aggSet.add(parsedCounters.get(i));
				countersAggtype.put(parsedCounters.get(i + 1), aggSet);
			}
		}
		return countersAggtype;
	}

	public boolean isAlpha(String name) {
		return name.matches("[a-zA-Z]+");
	}

	public String fromlaWithAggregatdCounter(String formula) {
		formula = formula.replace("(this.C", "_");
		formula = formula.replace("(this.", "_");
		
		formula = formula.replace("SUM", "(SUM" );
		formula = formula.replace("sum", "(SUM" );
		formula = formula.replace("MIN", "(MIN" );
		formula = formula.replace("min", "(MIN" );
		formula = formula.replace("MAX", "(MAX" );
		formula = formula.replace("max", "(MAX" );
		
		return formula;
		
	}
	
	public String fromlaWithAggregationCounter(String formula) {

		formula = formula.replace("(this.C", "_");
		formula = formula.replace("(this.", "_");

		String[] token = formula.split("\\)");
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < token.length; i++) {
			// System.out.println("token ::" + token[i]);
			if (token[i].isEmpty()) {
				sb.append(")");
			} else {
				if (!token[i].isEmpty() && !token[i].contains("SUM_") && !(i == (token.length - 1))) {
					sb.append(token[i] + ")");
				} else
					sb.append(token[i]);
			}
		}

		StringBuilder finalString = new StringBuilder(formula);
		finalString = finalString.reverse();
		char[] charlist = String.valueOf(finalString).toCharArray();
		for (int j = 0; j < charlist.length; j++) {
			if (("" + charlist[j]).equals(")")) {
				sb.append(")");
			} else {
				break;
			}
		}
		System.out.println("Last formula : " + sb);
		return sb.toString();
	}

	/**
	 * Updating the Json spec and running the indexing job, IFF there is a new
	 * counterID added at the device level. i.e. the number of columns to be
	 * stored in the druid db have been increased due to the new KPI/Counter
	 * addition.
	 * 
	 * @param counter
	 * @param maxCounterID
	 */
	public void udpateMetricSpec(final Counter counter, String maxCounterID, final String devcieType) {
		System.out.println("updating metric spec in druid..."+counter+"..devcieType is"+devcieType+"...maxCounterId is"+maxCounterID);
		String generatedCounterID = counter.getCounterKey().getCounterId();
		if (generatedCounterID.compareTo(maxCounterID) > 0) {
			Runnable updateJsonRunnable = () -> {
				try {
					ClientResponse clientResponse=updateJson(counter, devcieType);
					if(clientResponse==null)
					{
						System.out.println("can not update metric spec in Druid");
					}
				} catch (Exception e) {
					LOGGER.error(" Error in updating the json spec for dyanmic kpi/counter", e);
				}
			};
			new Thread(updateJsonRunnable).start();
		}
	}

	@SuppressWarnings("unchecked")
	private ClientResponse updateJson(Counter counter, String deviceType) throws JsonParseException, JsonMappingException, MalformedURLException, IOException
			 {
		try{
		String output;
		Client client = new Client();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		Map<String, Object> result = mapper.readValue(
				new URL(getSupervisorURI() + "/" + druidAttribute.getMetricSpecName(deviceType)),
				new TypeReference<Map<String, Object>>() {
				});

		// tuningConfig Properties
		DatasourceJobPojo datasourceJobPojo = new DatasourceJobPojo();
		datasourceJobPojo.setType(result.get("type"));
		datasourceJobPojo.setContext(result.get("context"));
		String tuningConfig = mapper.writeValueAsString((result.get("tuningConfig")));
		Object tJson = mapper.readValue(tuningConfig, Object.class);
		datasourceJobPojo.setTuningConfig(tJson);

		// ioConfig Properties

		String ioConfig = mapper.writeValueAsString((result.get("ioConfig")));
		Object ioConfigJson = mapper.readValue(ioConfig, Object.class);
		datasourceJobPojo.setIoConfig(ioConfigJson);

		// DataSchema Properties

		String dataSchemaString = mapper.writeValueAsString(result.get("dataSchema"));
		Map<String, Object> result1 = mapper.readValue(dataSchemaString, new TypeReference<Map<String, Object>>() {
		});
		DataSchema dataSchema = new DataSchema();
		dataSchema.setDataSource((String) result1.get("dataSource"));

		String parser = mapper.writeValueAsString((result1.get("parser")));
		Object parserJson = mapper.readValue(parser, Object.class);
		dataSchema.setParser(parserJson);
		datasourceJobPojo.setDataSchema(dataSchema);

		// Granularity Spec
		String granularitySpec = mapper.writeValueAsString((result1.get("granularitySpec")));
		Object granularitySpecJson = mapper.readValue(granularitySpec, Object.class);
		dataSchema.setGranularitySpec(granularitySpecJson);
		datasourceJobPojo.setDataSchema(dataSchema);

		List<KafkaJsonPojo> metricsSpecList = (List<KafkaJsonPojo>) result1.get("metricsSpec");
		KafkaJsonPojo kafkaJsonPojo = new KafkaJsonPojo();
		List<KafkaJsonPojo> pojos = mapper.convertValue(metricsSpecList, new TypeReference<List<KafkaJsonPojo>>() {
		});

		String counterID = counter.getCounterKey().getCounterId();
		kafkaJsonPojo.setType("doubleSum");
		kafkaJsonPojo.setName(counterID);
		kafkaJsonPojo.setFieldName(counterID);
		kafkaJsonPojo.setExpression(null);

		if (counter.getEnabled() > 0) {
			metricsSpecList.add(kafkaJsonPojo);
			dataSchema.setMetricsSpec(metricsSpecList);
		} /*
			 * else { //We are not removing the counter from the Json Spec The
			 * collection keeps on happening and data gets stored in the Druid
			 * db, but it is never shown on the GUI. Iterator<KafkaJsonPojo> itr
			 * = pojos.iterator(); while(itr.hasNext()){ kafkaJsonPojo =
			 * (KafkaJsonPojo)itr.next();
			 * if(kafkaJsonPojo.getName().equals(counterID)) { itr.remove(); } }
			 * dataSchema.setMetricsSpec(pojos); }
			 */
		System.out.println(
				"datasourceJobPojo----------------- " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(datasourceJobPojo));

		// Sending Druid Job for KPI to supervisor

		WebResource resource = client.resource(getSupervisorURI());
		return resource.type("application/json").post(ClientResponse.class,
				mapper.writerWithDefaultPrettyPrinter().writeValueAsString(datasourceJobPojo));
	
		
}
	catch (Exception e) {
		System.out.println(e);
		return null;
	}
			 }
}
