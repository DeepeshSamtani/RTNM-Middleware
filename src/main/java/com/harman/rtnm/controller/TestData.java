package com.harman.rtnm.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.codec.Charsets;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.core.JsonParser.Feature;
public class TestData {

	public static void main(String[] args) {

		Properties prop = new Properties();
		InputStream input = null;

		List<Object> myIwanDataList = null;
		List<Object> myIwanResponseDataList = new ArrayList();
		Map<String, Object> returndata = new HashMap<String, Object>();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		String currentDay = formatter.format(new Date());
		Calendar c = Calendar.getInstance();

		c.set(Calendar.HOUR_OF_DAY, -24);
		String previousDay = formatter.format(c.getTime());

		String mapData = new String("{\r\n" + "	\"queryType\": \"groupBy\",\r\n" + "	\"dataSource\": \"iwan\",\r\n"
				+ "	\"granularity\": \"all\",\r\n" + "	\"dimensions\": [\"PROTOCOL\"],\r\n"
				+ "	\"aggregations\": [{\r\n" + "			\"type\": \"longSum\",\r\n"
				+ "			\"name\": \"total_in_bytes\",\r\n" + "			\"fieldName\": \"IN_BYTES\"\r\n"
				+ "		},\r\n" + "		{\r\n" + "			\"type\": \"doubleSum\",\r\n"
				+ "			\"name\": \"total_out_bytes\",\r\n" + "			\"fieldName\": \"OUT_BYTES\"\r\n"
				+ "		}\r\n" + "	],\r\n" + "	\"postAggregations\": [{\r\n" + "		\"type\": \"arithmetic\",\r\n"
				+ "		\"name\": \"avg_usage\",\r\n" + "		\"fn\": \"+\",\r\n" + "		\"fields\": [{\r\n"
				+ "			\"type\": \"fieldAccess\",\r\n" + "			\"fieldName\": \"total_in_bytes\"\r\n"
				+ "		}, {\r\n" + "			\"type\": \"fieldAccess\",\r\n"
				+ "			\"fieldName\": \"total_out_bytes\"\r\n" + "		}]\r\n" + "	}],\r\n"
				+ "      \"intervals\": [ \"" + previousDay + "/" + currentDay + "\" ]\r\n" + "}");

		try {
			
			ClassLoader classLoader = new ReportController().getClass().getClassLoader();
			File file = new File(classLoader.getResource("protocol.properties").getFile());
			input = new FileInputStream(file.getPath());
			// load a properties file
			prop.load(input);
			
			StringEntity requestEntity = new StringEntity(mapData, ContentType.APPLICATION_JSON);

			CloseableHttpClient client = HttpClients.createDefault();

			HttpPost postMethod = new HttpPost("http://18.218.251.76:8082/druid/v2");
			postMethod.setEntity(requestEntity);

			HttpResponse response = client.execute(postMethod);

			HttpEntity entity = response.getEntity();
			Header encodingHeader = entity.getContentEncoding();

			String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);
			myIwanDataList = new ArrayList();
			ObjectMapper objectMapper = new ObjectMapper();
			myIwanDataList = objectMapper.readValue(json, ArrayList.class);

			for (Iterator iterator = myIwanDataList.iterator(); iterator.hasNext();) {

				Map<?, ?> map = objectMapper.readValue(objectMapper.writeValueAsString(iterator.next()), Map.class);
				Map<?, ?> eventMap = objectMapper.readValue(objectMapper.writeValueAsString(map.get("event")),
						Map.class);

				DecimalFormat decimalFormat = new DecimalFormat("#.##");
				String avg_usage = decimalFormat.format((Double) eventMap.get("avg_usage"));
				int protocolValue = Integer.valueOf((String) eventMap.get("PROTOCOL"));

				String datapointArray[] = new String[2];
				datapointArray[0] = prop.getProperty(String.valueOf(protocolValue));
				datapointArray[1] = avg_usage;
				myIwanResponseDataList.add(datapointArray);
			}
			returndata.put("data", myIwanResponseDataList);
			System.out.println("check");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
