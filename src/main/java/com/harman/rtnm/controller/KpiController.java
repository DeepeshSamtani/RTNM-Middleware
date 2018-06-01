package com.harman.rtnm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.harman.rtnm.common.constant.Constant;
import com.harman.rtnm.common.helper.DruidHelper;
import com.harman.rtnm.model.Aggregation;
import com.harman.rtnm.model.Counter;
import com.harman.rtnm.model.CounterGroup;
import com.harman.rtnm.model.CounterKey;
import com.harman.rtnm.service.CounterGroupService;
import com.harman.rtnm.service.CounterService;
import com.harman.rtnm.service.KpiService;
import com.harman.rtnm.util.SFTPUtil;
import com.harman.rtnm.vo.DynamicCounterVO;

@RequestMapping(value = "/kpi")
@RestController
public class KpiController {

	@Autowired
	private KpiService kpiService;

	@Autowired
	private CounterService counterService;

	@Autowired
	private DruidHelper druidHelper; 

	@Autowired
	private CounterGroupService counterGroupService;

	@Autowired
	private SFTPUtil sftpUtil;
	
	/**
	 * Adding the newly created dynamic KPI or counter to the Database.
	 * It also generates the counter ID and compares it with the exisiting 
	 * counterId against the device type. If the newly generated id is 
	 * greater than the existing one, it updates the JSON spec for DRUID 
	 * and also sends the information to adaptor.
	 * 
	 * @param dynamicCounter
	 * @return
	 */
	@RequestMapping(value = "/addkpi", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addKpi(@RequestBody DynamicCounterVO dynamicCounter) {

		try {
			dynamicCounter.setCounterType(Constant.TYPE_DYNAMIC_KPI);

			boolean dupeFound = !counterService.ifCounterExistsByDisplayName(dynamicCounter.getCounter().getLogicalName());
			if(dupeFound) {
				return new ResponseEntity<String>("Duplicate Name Found", HttpStatus.OK);
			} else {

				//generate delta file for adaptor
				String maxCounterID = counterService.getMaxCounterIdByDeviceType(dynamicCounter.getDeviceType());
				
				Aggregation aggregation = new Aggregation();
				aggregation = kpiService.getAggregationIdByAggrType(dynamicCounter.getAggregationType());
				dynamicCounter.setAggregationID(aggregation);

				CounterGroup counterGroup = counterGroupService.getCounterGroupById(dynamicCounter.getGroupId());
				CounterKey counterKey = new CounterKey();
				counterKey.setCounterGroup(counterGroup);
				dynamicCounter.getCounter().setCounterKey(counterKey);

				Counter generatedCounter = counterService.addCounter(dynamicCounter.getCounter());

				//sending data to adaptor
				sftpUtil.createFileAndSend(generatedCounter, maxCounterID, dynamicCounter.getDeviceType());

				//update json
				druidHelper.udpateMetricSpec(generatedCounter, maxCounterID, dynamicCounter.getDeviceType());
				return new ResponseEntity<String>("KPI Added Successfully", HttpStatus.CREATED);
			}

		} catch(Exception ex) {	
			return new ResponseEntity<String>(ex.getMessage(),  HttpStatus.BAD_REQUEST);			
		} 
	}

	/**
	 * Request for updating the existing Dynamic KPI.  
	 * @param dynamicCounter
	 * @return
	 */
	@RequestMapping(value = "/updatekpi", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updatekpi(@RequestBody DynamicCounterVO dynamicCounter) {
		try {
			System.out.println("update KPI....");
			CounterKey counterKey = new CounterKey(dynamicCounter.getCounterId(), dynamicCounter.getGroupId());
			Counter counterInDB = counterService.getCounterById(counterKey);
			/*boolean dupeFound = !counterService.ifCounterExistsByDisplayName(dynamicCounter.getCounter().getLogicalName());
			if(dupeFound) {
				return new ResponseEntity<String>("Duplicate Name Found", HttpStatus.OK);
			}*/
			/*Counter counterName = counterService.getCounterByDisplayName(dynamicCounter.getCounter().getLogicalName());
			boolean dupeFound = !(counterName.getLogicalName().equalsIgnoreCase(dynamicCounter.getCounter().getLogicalName())
									&& counterName.getCounterKey().getCounterId().equals(counterKey.getCounterId())
									&& counterName.getCounterKey().getCounterGroup().getCounterGroupId().equals(counterKey.getCounterGroup().getCounterGroupId()));
			if(dupeFound) {
				return new ResponseEntity<String>("Duplicate Name Found", HttpStatus.OK);
			}*/

			if(counterInDB != null) {
				if (null != dynamicCounter.getAggregationType()
						&& !dynamicCounter.getAggregationType().equals(counterInDB.getAggType(counterInDB.getAggregation()))) {
					Aggregation aggregation = new Aggregation();
					aggregation = kpiService.getAggregationIdByAggrType(dynamicCounter.getAggregationType());
					dynamicCounter.setAggregationID(aggregation);
				} else {
					dynamicCounter.setAggregationID(counterInDB.getAggregation());
				}

				dynamicCounter.getCounter().setCounterKey(counterKey);
				
				/*counterService.updateCounter(dynamicCounter.getCounter());
				 * commenting above line as its setting other field than DynamicCounterVO as null in db*/
				if(dynamicCounter.getCounter().getEnabled().equals(0))
				{
				//Its delete request
					System.out.println("Its delete request");
					counterInDB.setEnabled(dynamicCounter.getCounter().getEnabled());
					counterService.updateCounter(counterInDB);
				}
				else
				{
				//Its update request
					System.out.println("Its update request");
					counterService.updateCounter(dynamicCounter.getCounter());
				}
				
				
				//tranfer the files onto collector server
				sftpUtil.createFileAndSend(dynamicCounter.getCounter(), null, dynamicCounter.getDeviceType());
				
				return new ResponseEntity<String>("KPI Altered Successfully", HttpStatus.OK);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("KPI Not Found");
			}
		} catch (Exception e) {
			System.out.println("error mesaage"+e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Request to get the list of all/only the Dynamic KPIs to be fetched
	 * from the counter table.
	 * 
	 * @param details nothing considered as of now
	 * @return list of KPIs to be 
	 */
	@RequestMapping(value = "/kpilist", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<DynamicCounterVO>> getKpiList(@RequestBody DynamicCounterVO details) {
		List<DynamicCounterVO> listOfKpis = null;
		try {
			listOfKpis = counterService.getAllDynamicKPIs();
			if(listOfKpis.size() == 0) {
				return new ResponseEntity<List<DynamicCounterVO>>(listOfKpis, HttpStatus.OK);
			}
		} catch(Exception e) {
			new ResponseEntity<List<DynamicCounterVO>>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<List<DynamicCounterVO>>(listOfKpis, HttpStatus.OK);

	}

	/**
	 * Disabling the existing dynamic KPI.</br>
	 * It only marks the KPI as enabled=0, and does not
	 * delete the physical record from the database.
	 * @param dynamicCounter
	 * @return
	 */
	@RequestMapping(value = "/deletekpi", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteKPI(@RequestBody DynamicCounterVO dynamicCounter) {

		/*
		 * soft delete for not showing this counter/metric data
		 * on the GUI. Though the collection and other aggregation
		 * functions will be still performed in the backgound. 
		 */

		return updatekpi(dynamicCounter);
	}
}