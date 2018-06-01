package com.harman.rtnm.common.constant;

public interface Constant {
	public final String CURRENT = "current";
	public final String PREVIOUS = "previous";
	public final String LAST = "last";

	public final String REAL_TIME = "realtime";

	/**
	 * Druid Constants
	 */
	public final String TIMESTAMP_DIMENSION = "timestamp";
	public final String TOPN = "topN";
	public final String GROUP_BY = "groupBy";
	public final String SELECT = "select";
	public final String TIMESERIES = "timeseries";
	public final String EVENT = "event";
	public final String EVENTS = "events";
	public final String PAGING_IDENTIFIERS = "pagingIdentifiers";
	public final String ALL = "all";


	public final String NODENAME = "NodeName";
	public final String SUBELEMENTID = "SubElementID";
	public final String AVERAGE = "AVG";
	public final String COUNT_STREAM = "count_";
	public final String COUNT = "count";
	public final String SUM_COUNT = "sum_count";
	/*
	 * Report Directory constant
	 */
	public final String EXPORT_REPORT_DIR = "/ExportReport";
	public final String SCHEDULED_REPORT_DIR = "/ScheduledReport";
	
	/**
	 * KPI datasource
	 */
	public final String KpiDataSource = "KPI_Raw";

	/**
	 * DSR dimenssions
	 */
	public final String SCOPE_TYPE = "Scope Type";
	public final String SCOPE = "Scope";
	public final String SERVER = "Server";
	public final String NAME_INDEX = "NameIndex";
		
	public final String UNDERSCORE = "_";
	public final String SUM = "SUM";
	public final String MIN = "MIN";
	public final String MAX = "MAX";
	public final String AVG = "AVG";
	public final String FORMULA = "FORMULA";

	public final String COUNTER_GROUP_ID = "COUNTER_GROUP_ID";
	/**
	 * KPI types (like derived counters , dynamic Kpi for rest and UI purpose)
	 */	
	public final String DERIVED = "derived";
	public final String DYNAMIC = "dynamic";
	
	
	public final String GROUPED ="grouped";
	public final String RAW ="raw";
	public final String Graph ="graph";
	
	public final String EE_MOBILE = "ESN";

	//COUNTER TYPE  
	public static final String TYPE_DERIVED_KPI = "KPI";
	public static final String TYPE_COUNTER = "Counter";
	public static final String TYPE_DYNAMIC_KPI = "DKPI";
	public final String DYNAMIC_KPI = "DKPI";
	
	/*
	 * Identifiers for counter group and counter
	 */
	String PATTERN_COUNTER = "_C_";
	String PATTERN_COUNTER_GROUP = "_CG_";
	//For UI type
	public final String NUMBER = "number";
	public final String TEXT = "text";

}
