package com.harman.rtnm.service.impl;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.transaction.Transactional;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harman.dyns.model.common.Metric;
import com.harman.rtnm.dao.CounterDao;
import com.harman.rtnm.dao.KpiDao;
import com.harman.rtnm.model.Aggregation;
import com.harman.rtnm.model.Counter;
import com.harman.rtnm.model.CounterKey;
import com.harman.rtnm.model.KPI;
import com.harman.rtnm.model.response.DataSchema;
import com.harman.rtnm.model.response.DatasourceJobPojo;
import com.harman.rtnm.model.response.KafkaJsonPojo;
import com.harman.rtnm.service.KpiService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


@Service
public class KpiServiceImpl implements KpiService {

	private static final Logger logger = LoggerFactory.getLogger(KpiServiceImpl.class);
	
	@Autowired
	private KpiDao kpiDao;
	
	@Autowired
	private CounterDao counterDao;

	@Override
	@Transactional
	public void addKpi(Counter kpi) throws JsonParseException, JsonMappingException, IOException  {
		
		counterDao.addCounter(kpi);
		logger.debug("Entered Service Impl");
		/*
		 * updateJson(kpi);
		sendDataToKafka(kpi);		*/
	}

	/*private void sendDataToKafka(Counter kpi) throws JsonProcessingException {
		
		Counter model = new Counter();
		
		ObjectWriter ow = new ObjectMapper().writer();
//		KPI model = new KPI();
		
		String jsonStr;
		
		model.setCounterKey(kpi.getCounterKey());
		model.setLogicalName(kpi.getLogicalName());
		model.setFormula(c.getFormula());
		model.setDeviceType(kpi.getDeviceType());
		model.setIsActive(kpi.getIsActive());
		model.setUnit(kpi.getUnit());
		model.setDescription(kpi.getDescription());
		jsonStr = ow.writeValueAsString(model);


		String topicName = "dynamickpi";
		Properties props = new Properties();
		props.put("bootstrap.servers", "172.25.182.145:9092");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		// Reduce the no of requests less than 0
		props.put("linger.ms", 50);
		// The buffer.memory controls the total amount of memory available to
		// the producer for buffering.
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		
		
		System.out.println(jsonStr);
		Producer<String, String> producer = new KafkaProducer<String, String>(props);

		producer.send(new ProducerRecord<String, String>(topicName, jsonStr));
		System.out.println("Message sent successfully");
		producer.close();
		
	}*/

	@Override
	@Transactional
	public List<KPI> getKpiList(String deviceType) {
		return kpiDao.getKpiList(deviceType);
	}

	@Override
	@Transactional
	public int findKpiByFormula(String currentFormula) {
		return kpiDao.findKpiByFormula(currentFormula);
	}

	@Override
	@Transactional
	public int findKpiByName(String name) {
		return kpiDao.findKpiByName(name);
	}

	@Override
	@Transactional
	public List<KPI> getKpiDetails(List<Metric> kpiMetric) throws Exception {
		return kpiDao.getKpiDetails(kpiMetric);
	}

	
	

	@Override
	@Transactional
	public List<KPI>  getKpiById(int id) {
		return kpiDao.getKpiById(id);
	}

	@Override
	@Transactional
	public void updateKpi(KPI kpi) throws JsonProcessingException, IOException {
		kpiDao.updateKpi(kpi);		
	}	

	@Override
	@Transactional
	public Aggregation getAggregationIdByAggrType(String type){
		return kpiDao.getAggregationIdByAggrType(type);
	}

	@Override
	@Transactional
	public List<KPI> getActiveKpiByDeviceType(String deviceType) throws Exception{
		return kpiDao.getActiveKpiByDeviceType(deviceType);
	}

}
