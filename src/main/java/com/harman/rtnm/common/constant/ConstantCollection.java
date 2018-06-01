package com.harman.rtnm.common.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConstantCollection {

	
	
	public static List<String> deviceTypeList = new ArrayList<>();	
	public static Map<String,Long> periodCalculationOfUgw = new HashMap();
	public static Map<String, String> nodeNameProprtyWithdeviceType = new HashMap<>();
	
	
	static {
		deviceTypeList.add("MME");
		deviceTypeList.add("CGP");
		
		periodCalculationOfUgw.put("HOURLY", 60L);
		periodCalculationOfUgw.put("DAILY", 1440L);
		periodCalculationOfUgw.put("WEEKLY", 10080L);
		periodCalculationOfUgw.put("MONTHLY", 43200L);
		periodCalculationOfUgw.put("YEARLY", 525600L);
		
		nodeNameProprtyWithdeviceType.put("MME", "MME_NAME");
		nodeNameProprtyWithdeviceType.put("CGP", "CGP_NAME");
		nodeNameProprtyWithdeviceType.put("UGW", "UGW_NAME");
		nodeNameProprtyWithdeviceType.put("PCRF_CGP", "CGP_NAME");
		nodeNameProprtyWithdeviceType.put("PCRF_VOICE", "PCRF_NAME");
		nodeNameProprtyWithdeviceType.put("PCRF_USCDB", "USCDB_NAME");
		nodeNameProprtyWithdeviceType.put("PCRF_SPS", "");		
	}
	
}
