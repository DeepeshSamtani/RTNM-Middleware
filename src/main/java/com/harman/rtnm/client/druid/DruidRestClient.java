package com.harman.rtnm.client.druid;

import org.apache.log4j.Logger;

import com.harman.dyns.model.druid.request.DruidGroupByRequest;
import com.harman.dyns.model.druid.request.DruidSelectRequest;
import com.harman.dyns.model.druid.request.DruidTimeseriesRequest;
import com.harman.dyns.model.druid.request.DruidTopNRequest;
import com.harman.rtnm.model.response.DruidBaseResponse;
import com.harman.rtnm.model.response.DruidGroupByResponse;
import com.harman.rtnm.model.response.DruidSelectResponse;
import com.harman.rtnm.model.response.DruidTimeSeriesResponse;
import com.harman.rtnm.model.response.DruidTopNResponse;
import com.harman.rtnm.util.ObjectFactory;
import com.fasterxml.jackson.databind.ObjectMapper;


public class DruidRestClient< T extends DruidBaseResponse> extends AbstractRestClient {

	private static final Logger logger = Logger.getLogger(DruidRestClient.class);
	private ObjectMapper mapper = ObjectFactory.getObjectMapper();


	@SuppressWarnings("unchecked")
	public T[] druidResponse(String uri, Object request) throws Exception {

		//This is purpose of logging response it will disable after successful test
		if(logger.isDebugEnabled()) {
			Object json = mapper.readValue(mapper.writeValueAsString(request), Object.class);
			System.out.println("Druid query :: "+mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));
			logger.debug("Druid query :: "+mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));
		}
		if (request instanceof DruidTopNRequest) {
			return (T[]) post(uri, request, DruidTopNResponse[].class);
		}
		if (request instanceof DruidGroupByRequest) {
			return (T[]) post(uri, request, DruidGroupByResponse[].class);
		}
		if (request instanceof DruidSelectRequest) {
			return (T[]) post(uri, request, DruidSelectResponse[].class);
		}
		if (request instanceof DruidTimeseriesRequest) {
			return (T[]) post(uri, request, DruidTimeSeriesResponse[].class);
		}

		return null;
	}



}