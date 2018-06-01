package demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.harman.rtnm.common.helper.DruidHelper;
import com.harman.rtnm.model.KPI;

public class AppTest {

	static Map<String, String> druidGranularityMap = new HashMap();
	
	static{
		druidGranularityMap.put("ALL","minute");
		druidGranularityMap.put("HOURLY","hour");
		druidGranularityMap.put("DAILY","day");
		druidGranularityMap.put("WEEKLY","week");
		druidGranularityMap.put("MONTHLY","month");		
		druidGranularityMap.put("YEARLY","year");
	}
	
	
	public static void main(String[] args) {
		AppTest apptest = new  AppTest();
		//apptest.checkRandomVal();
		//apptest.testSomeStuff();
		apptest.testSet();
	}
	
	public void testSomeStuff(){
		String granularity = "ALL";
		//String ganularityTest = druidGranularityMap.get(granularity.trim().toUpperCase());
		//System.out.println("ganularityTest : "+ganularityTest);
		System.out.println("not working :: "+druidGranularityMap.get(granularity.trim().toUpperCase()));
		
	}
	
	
	public void checkRandomVal(){
		Random r = new Random();
		
		System.out.println("random val = "+r);
		System.out.println("random val = "+r+1);
		System.out.println(r.nextInt((17 - 5) + 1) + 13);
		System.out.println(r.nextInt((17 - 5) + 1) + 13);
		for (int i = 0; i < 10; i++) {
			System.out.println(r.nextInt((17 - 5) + 1) + 13);
		}
		
	}
	
	public void testSet(){
		List<String> kpiList = new ArrayList<String>();
		
		kpiList.add("117490512,117491112,117491113");
		kpiList.add("117490512,117491113,117491115");
		Set<String> counterSet = new HashSet<>();
		kpiList.forEach(kp -> {
			String[] counterArray = kp.split("\\,");
			counterSet.addAll(Arrays.asList(counterArray));					
		});
		
		counterSet.forEach(cs ->{
			System.out.println(cs);
		});
	}
	
}
