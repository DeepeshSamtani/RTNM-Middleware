package com.harman.rtnm.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harman.rtnm.model.response.UserReport;
import com.harman.rtnm.service.ReportService;
import com.harman.rtnm.vo.ReportDetailVO;

@EnableAspectJAutoProxy(proxyTargetClass = true)
@RequestMapping(value = "/reports")
@RestController
public class ReportController {

	@Autowired
	ReportService reportService;

	@RequestMapping(value = "/getIWANSpecificSrcByteReport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getIWANSpecificSrcByteReport(@RequestBody ReportDetailVO reportDetailVO)
			throws Exception {

		List<Object> myIwanDataList = null;
		List<Object> myIwanResponseDataList = new ArrayList();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		String currentDay = formatter.format(new Date());
		Calendar c = Calendar.getInstance();
		Map<String, Object> returndata = new HashMap<String, Object>();

		c.set(Calendar.HOUR_OF_DAY, -24);
		String previousDay = formatter.format(c.getTime());

		String mapData = new String("{  \r\n" + "    \"queryType\": \"groupBy\",\r\n"
				+ "      \"dataSource\": \"iwan\",\r\n" + "      \"granularity\": \"all\",\r\n"
				+ "      \"dimensions\": [\"IPV4_SRC_ADDR\"],\r\n" + "      \"aggregations\": [   \r\n" + "	  {\r\n"
				+ "        \"type\": \"longSum\",\r\n" + "        \"name\": \"total_in_bytes\",\r\n"
				+ "        \"fieldName\": \"IN_BYTES\"\r\n" + "     },    \r\n" + "	   {\r\n"
				+ "        \"type\": \"doubleSum\",\r\n" + "        \"name\": \"total_out_bytes\",\r\n"
				+ "        \"fieldName\": \"OUT_BYTES\"\r\n" + "		} \r\n" + "	],\r\n"
				+ "      \"postAggregations\": [    {\r\n" + "        \"type\": \"arithmetic\",\r\n"
				+ "              \"name\": \"avg_usage\",\r\n" + "              \"fn\": \"+\",\r\n"
				+ "              \"fields\": [        {\r\n" + "            \"type\": \"fieldAccess\",\r\n"
				+ "            \"fieldName\": \"total_in_bytes\"\r\n" + "        },          {\r\n"
				+ "            \"type\": \"fieldAccess\",\r\n" + "            \"fieldName\": \"total_out_bytes\"\r\n"
				+ "        }      ]    \r\n" + "    }  ],\r\n" + "      \"intervals\": [ \"" + previousDay + "/"
				+ currentDay + "\" ]\r\n" + "}");

		try {
			StringEntity requestEntity = new StringEntity(mapData, ContentType.APPLICATION_JSON);

			CloseableHttpClient client = HttpClients.createDefault();

			HttpPost postMethod = new HttpPost(reportService.getDruidURI());
			postMethod.setEntity(requestEntity);

			HttpResponse response = client.execute(postMethod);

			HttpEntity entity = response.getEntity();

			Header encodingHeader = entity.getContentEncoding();
			// you need to know the encoding to parse correctly
			Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8
					: Charsets.toCharset(encodingHeader.getValue());
			// use org.apache.http.util.EntityUtils to read json as string
			String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);

			myIwanDataList = new ArrayList();
			ObjectMapper objectMapper = new ObjectMapper();
			myIwanDataList = objectMapper.readValue(json, ArrayList.class);

			Double all24hourusage = 0d;

			for (Iterator iterator = myIwanDataList.iterator(); iterator.hasNext();) {

				Map<?, ?> map = objectMapper.readValue(objectMapper.writeValueAsString(iterator.next()), Map.class);
				Map<String, Double> eventMap = objectMapper.readValue(objectMapper.writeValueAsString(map.get("event")),
						Map.class);
				all24hourusage = all24hourusage + eventMap.get("avg_usage");
			}

			for (Iterator iterator = myIwanDataList.iterator(); iterator.hasNext();) {

				Map<?, ?> map = objectMapper.readValue(objectMapper.writeValueAsString(iterator.next()), Map.class);
				Map<?, ?> eventMap = objectMapper.readValue(objectMapper.writeValueAsString(map.get("event")),
						Map.class);

				Double div = ((Double) eventMap.get("avg_usage") / all24hourusage) * 100;
				Double roundOffPercentage = Math.round(div * 100) / 100.0;

				Map<String, String> dataPoint = new HashMap<String, String>();

				dataPoint.put("name", eventMap.get("IPV4_SRC_ADDR").toString());

				DecimalFormat decimalFormat = new DecimalFormat("#.##");
				String numberAsString = decimalFormat.format(roundOffPercentage);

				dataPoint.put("y", numberAsString);
				myIwanResponseDataList.add(dataPoint);
			}

			returndata.put("data", myIwanResponseDataList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return returndata;
	}

	@RequestMapping(value = "/getIWANSpecificDestByteReport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getIWANSpecificDestByteReport(@RequestBody ReportDetailVO reportDetailVO)
			throws Exception {

		List<Object> myIwanDataList = null;
		List<Object> myIwanResponseDataList = new ArrayList();
		Map<String, Object> returndata = new HashMap<String, Object>();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		String currentDay = formatter.format(new Date());
		Calendar c = Calendar.getInstance();

		c.set(Calendar.HOUR_OF_DAY, -24);
		String previousDay = formatter.format(c.getTime());

		String mapData = new String("{  \r\n" + "    \"queryType\": \"groupBy\",\r\n"
				+ "      \"dataSource\": \"iwan\",\r\n" + "      \"granularity\": \"all\",\r\n"
				+ "      \"dimensions\": [\"IPV4_DST_ADDR\"],\r\n" + "      \"aggregations\": [   \r\n" + "	  {\r\n"
				+ "        \"type\": \"longSum\",\r\n" + "        \"name\": \"total_in_bytes\",\r\n"
				+ "        \"fieldName\": \"IN_BYTES\"\r\n" + "     },    \r\n" + "	   {\r\n"
				+ "        \"type\": \"doubleSum\",\r\n" + "        \"name\": \"total_out_bytes\",\r\n"
				+ "        \"fieldName\": \"OUT_BYTES\"\r\n" + "		} \r\n" + "	],\r\n"
				+ "      \"postAggregations\": [    {\r\n" + "        \"type\": \"arithmetic\",\r\n"
				+ "              \"name\": \"avg_usage\",\r\n" + "              \"fn\": \"+\",\r\n"
				+ "              \"fields\": [        {\r\n" + "            \"type\": \"fieldAccess\",\r\n"
				+ "            \"fieldName\": \"total_in_bytes\"\r\n" + "        },          {\r\n"
				+ "            \"type\": \"fieldAccess\",\r\n" + "            \"fieldName\": \"total_out_bytes\"\r\n"
				+ "        }      ]    \r\n" + "    }  ],\r\n" + "      \"intervals\": [ \"" + previousDay + "/"
				+ currentDay + "\" ]\r\n" + "}");

		try {
			StringEntity requestEntity = new StringEntity(mapData, ContentType.APPLICATION_JSON);

			CloseableHttpClient client = HttpClients.createDefault();

			HttpPost postMethod = new HttpPost(reportService.getDruidURI());
			postMethod.setEntity(requestEntity);

			HttpResponse response = client.execute(postMethod);

			HttpEntity entity = response.getEntity();
			Header encodingHeader = entity.getContentEncoding();
			// you need to know the encoding to parse correctly
			Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8
					: Charsets.toCharset(encodingHeader.getValue());
			// use org.apache.http.util.EntityUtils to read json as string
			String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);

			myIwanDataList = new ArrayList();
			ObjectMapper objectMapper = new ObjectMapper();
			myIwanDataList = objectMapper.readValue(json, ArrayList.class);

			Double all24hourusage = 0d;

			for (Iterator iterator = myIwanDataList.iterator(); iterator.hasNext();) {

				Map<?, ?> map = objectMapper.readValue(objectMapper.writeValueAsString(iterator.next()), Map.class);
				Map<String, Double> eventMap = objectMapper.readValue(objectMapper.writeValueAsString(map.get("event")),
						Map.class);
				all24hourusage = all24hourusage + eventMap.get("avg_usage");
			}

			for (Iterator iterator = myIwanDataList.iterator(); iterator.hasNext();) {

				Map<?, ?> map = objectMapper.readValue(objectMapper.writeValueAsString(iterator.next()), Map.class);
				Map<?, ?> eventMap = objectMapper.readValue(objectMapper.writeValueAsString(map.get("event")),
						Map.class);

				Double div = ((Double) eventMap.get("avg_usage") / all24hourusage) * 100;
				Double percentage = Math.round(div * 100) / 100.0;
				// eventMap.put( "avg_dest_usage_percentage", percentage);

				Map<String, String> dataPoint = new HashMap<String, String>();

				dataPoint.put("name", eventMap.get("IPV4_DST_ADDR").toString());

				DecimalFormat decimalFormat = new DecimalFormat("#.##");
				String numberAsString = decimalFormat.format(percentage);

				dataPoint.put("y", numberAsString);
				myIwanResponseDataList.add(dataPoint);

			}

			returndata.put("data", myIwanResponseDataList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return returndata;
	}

	@RequestMapping(value = "/getIWANSpecificSrcPacketReport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getIWANSpecificSrcPacketReport(@RequestBody ReportDetailVO reportDetailVO)
			throws Exception {

		List<Object> myIwanDataList = null;
		List<Object> myIwanResponseDataList = new ArrayList();
		Map<String, Object> returndata = new HashMap<String, Object>();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		String currentDay = formatter.format(new Date());
		Calendar c = Calendar.getInstance();

		c.set(Calendar.HOUR_OF_DAY, -24);
		String previousDay = formatter.format(c.getTime());

		String mapData = new String("{  \r\n" + "    \"queryType\": \"groupBy\",\r\n"
				+ "      \"dataSource\": \"iwan\",\r\n" + "      \"granularity\": \"all\",\r\n"
				+ "      \"dimensions\": [\"IPV4_SRC_ADDR\"],\r\n" + "      \"aggregations\": [    {\r\n"
				+ "        \"type\": \"longSum\",\r\n" + "        \"name\": \"total_in_pkts\",\r\n"
				+ "        \"fieldName\": \"IN_PKTS\"\r\n" + "    },      {\r\n"
				+ "        \"type\": \"doubleSum\",\r\n" + "        \"name\": \"total_out_pkts\",\r\n"
				+ "        \"fieldName\": \"OUT_PKTS\"\r\n" + "    }  ],\r\n" + "      \"postAggregations\": [    {\r\n"
				+ "        \"type\": \"arithmetic\",\r\n" + "              \"name\": \"avg_pkt_usage\",\r\n"
				+ "              \"fn\": \"+\",\r\n" + "              \"fields\": [        {\r\n"
				+ "            \"type\": \"fieldAccess\",\r\n" + "            \"fieldName\": \"total_in_pkts\"\r\n"
				+ "        },          {\r\n" + "            \"type\": \"fieldAccess\",\r\n"
				+ "            \"fieldName\": \"total_out_pkts\"\r\n" + "        }      ]    \r\n" + "    }  ],\r\n"
				+ "      \"intervals\": [ \"" + previousDay + "/" + currentDay + "\" ]\r\n" + "}");
		try {

			StringEntity requestEntity = new StringEntity(mapData, ContentType.APPLICATION_JSON);
			CloseableHttpClient client = HttpClients.createDefault();

			//HttpPost postMethod = new HttpPost("http://18.218.251.76:8082/druid/v2");
			
			HttpPost postMethod = new HttpPost(reportService.getDruidURI());
			postMethod.setEntity(requestEntity);

			HttpResponse response = client.execute(postMethod);

			HttpEntity entity = response.getEntity();
			Header encodingHeader = entity.getContentEncoding();

			// use org.apache.http.util.EntityUtils to read json as string
			String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);

			myIwanDataList = new ArrayList();
			ObjectMapper objectMapper = new ObjectMapper();
			myIwanDataList = objectMapper.readValue(json, ArrayList.class);

			for (Iterator iterator = myIwanDataList.iterator(); iterator.hasNext();) {

				Map<?, ?> map = objectMapper.readValue(objectMapper.writeValueAsString(iterator.next()), Map.class);
				Map<?, ?> eventMap = objectMapper.readValue(objectMapper.writeValueAsString(map.get("event")),
						Map.class);

				DecimalFormat decimalFormat = new DecimalFormat("#.##");

				String numberAsString = decimalFormat.format((Double) eventMap.get("avg_pkt_usage"));

				String datapointArray[] = new String[2];
				datapointArray[0] = eventMap.get("IPV4_SRC_ADDR").toString();
				datapointArray[1] = numberAsString;
				myIwanResponseDataList.add(datapointArray);
			}

			returndata.put("data", myIwanResponseDataList);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returndata;
	}

	@RequestMapping(value = "/getIWANSpecificDestPacketReport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getIWANSpecificDestPacketReport(@RequestBody ReportDetailVO reportDetailVO)
			throws Exception {

		List<Object> myIwanDataList = null;
		List<Object> myIwanResponseDataList = new ArrayList();
		Map<String, Object> returndata = new HashMap<String, Object>();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		String currentDay = formatter.format(new Date());
		Calendar c = Calendar.getInstance();

		c.set(Calendar.HOUR_OF_DAY, -24);
		String previousDay = formatter.format(c.getTime());

		String mapData = new String("{  \r\n" + "    \"queryType\": \"groupBy\",\r\n"
				+ "      \"dataSource\": \"iwan\",\r\n" + "      \"granularity\": \"all\",\r\n"
				+ "      \"dimensions\": [\"IPV4_DST_ADDR\"],\r\n" + "      \"aggregations\": [    {\r\n"
				+ "        \"type\": \"longSum\",\r\n" + "        \"name\": \"total_in_pkts\",\r\n"
				+ "        \"fieldName\": \"IN_PKTS\"\r\n" + "    },      {\r\n"
				+ "        \"type\": \"doubleSum\",\r\n" + "        \"name\": \"total_out_pkts\",\r\n"
				+ "        \"fieldName\": \"OUT_PKTS\"\r\n" + "    }  ],\r\n" + "      \"postAggregations\": [    {\r\n"
				+ "        \"type\": \"arithmetic\",\r\n" + "              \"name\": \"avg_pkt_usage\",\r\n"
				+ "              \"fn\": \"+\",\r\n" + "              \"fields\": [        {\r\n"
				+ "            \"type\": \"fieldAccess\",\r\n" + "            \"fieldName\": \"total_in_pkts\"\r\n"
				+ "        },          {\r\n" + "            \"type\": \"fieldAccess\",\r\n"
				+ "            \"fieldName\": \"total_out_pkts\"\r\n" + "        }      ]    \r\n" + "    }  ],\r\n"
				+ "      \"intervals\": [ \"" + previousDay + "/" + currentDay + "\" ]\r\n" + "}");
		try {
			StringEntity requestEntity = new StringEntity(mapData, ContentType.APPLICATION_JSON);

			CloseableHttpClient client = HttpClients.createDefault();

			HttpPost postMethod = new HttpPost(reportService.getDruidURI());
			postMethod.setEntity(requestEntity);

			HttpResponse response = client.execute(postMethod);

			HttpEntity entity = response.getEntity();
			Header encodingHeader = entity.getContentEncoding();

			// you need to know the encoding to parse correctly
			// use org.apache.http.util.EntityUtils to read json as string
			String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);
			myIwanDataList = new ArrayList();
			ObjectMapper objectMapper = new ObjectMapper();
			myIwanDataList = objectMapper.readValue(json, ArrayList.class);

			for (Iterator iterator = myIwanDataList.iterator(); iterator.hasNext();) {

				Map<?, ?> map = objectMapper.readValue(objectMapper.writeValueAsString(iterator.next()), Map.class);
				Map<?, ?> eventMap = objectMapper.readValue(objectMapper.writeValueAsString(map.get("event")),
						Map.class);

				DecimalFormat decimalFormat = new DecimalFormat("#.##");

				String numberAsString = decimalFormat.format((Double) eventMap.get("avg_pkt_usage"));

				String datapointArray[] = new String[2];
				datapointArray[0] = eventMap.get("IPV4_DST_ADDR").toString();
				datapointArray[1] = numberAsString;
				myIwanResponseDataList.add(datapointArray);
			}
			returndata.put("data", myIwanResponseDataList);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returndata;
	}

	@RequestMapping(value = "/getIWANProtocolSpecificByteUsage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getIWANProtocolSpecificByteUsage(@RequestBody ReportDetailVO reportDetailVO)
			throws Exception {
     
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
			
			Properties prop = new Properties();
			InputStream input = null;
			ClassLoader classLoader = new ReportController().getClass().getClassLoader();
			File file = new File(classLoader.getResource("protocol.properties").getFile());
			input = new FileInputStream(file.getPath());
			// load a properties file
			prop.load(input);
			
			StringEntity requestEntity = new StringEntity(mapData, ContentType.APPLICATION_JSON);

			CloseableHttpClient client = HttpClients.createDefault();

			HttpPost postMethod = new HttpPost(reportService.getDruidURI());
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
				if (protocolValue > 142 && protocolValue < 253) {
					datapointArray[0] = "Unassigned";
				}else {
				datapointArray[0] = prop.getProperty(String.valueOf(protocolValue));
				}
				datapointArray[1] = avg_usage;
				myIwanResponseDataList.add(datapointArray);
			}
			returndata.put("data", myIwanResponseDataList);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returndata;
	}

	@RequestMapping(value = "/getspecificreport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserReport> getSpecificReport(@RequestBody ReportDetailVO reportDetailVO) throws Exception {
		UserReport userReport = null;
		if (null != reportDetailVO) {
			userReport = reportService.getSpecificReport(reportDetailVO);
			return new ResponseEntity<UserReport>(userReport, HttpStatus.OK);
		}
		return new ResponseEntity<UserReport>(userReport, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/saveUserReport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserReport> saveUserReport(@RequestBody ReportDetailVO reportDetailVO) throws Exception {
		UserReport userReport = null;
		if (null != reportDetailVO) {
			userReport = reportService.addUserReport(reportDetailVO);
			return new ResponseEntity<UserReport>(userReport, HttpStatus.OK);
		}
		return new ResponseEntity<UserReport>(userReport, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/getUserReport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserReport> getUserReport(@RequestBody ReportDetailVO reportDetailVO) throws Exception {
		UserReport userReport = null;
		if (null != reportDetailVO) {
			userReport = reportService.getUserReport(reportDetailVO);
			return new ResponseEntity<UserReport>(userReport, HttpStatus.OK);
		}
		return new ResponseEntity<UserReport>(userReport, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/updateReport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserReport> updateReport(@RequestBody ReportDetailVO reportDetailVO) throws Exception {
		UserReport userReport = null;
		if (null != reportDetailVO) {
			try {
				userReport = reportService.updateUserReport(reportDetailVO);
				return new ResponseEntity<UserReport>(userReport, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<UserReport>(userReport, HttpStatus.BAD_REQUEST);
			}
		}
		return new ResponseEntity<UserReport>(userReport, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/getReportAccordingToFilter", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserReport> getReportAccordingToFilter(@RequestBody ReportDetailVO reportDetailVO)
			throws Throwable {
		UserReport userReport = null;

		if (null != reportDetailVO) {
			try {
				userReport = reportService.getReportAccordingToFilter(reportDetailVO);
				return new ResponseEntity<UserReport>(userReport, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<UserReport>(userReport, HttpStatus.BAD_REQUEST);
			}
		}
		return new ResponseEntity<UserReport>(userReport, HttpStatus.OK);
	}

	@RequestMapping(value = "/deleteReport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ReportDetailVO> deleteReport(@RequestBody ReportDetailVO reportDetailVO) throws Exception {
		if (null != reportDetailVO) {
			try {
				reportService.deleteReport(reportDetailVO);
				return new ResponseEntity<ReportDetailVO>(reportDetailVO, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<ReportDetailVO>(reportDetailVO, HttpStatus.BAD_REQUEST);
			}
		}
		return new ResponseEntity<ReportDetailVO>(reportDetailVO, HttpStatus.BAD_REQUEST);
	}

}
