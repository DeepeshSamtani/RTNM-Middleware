package com.harman.rtnm.client.druid;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AbstractRestClient extends RestTemplate {

	public <T> T post(String uri, Object request, Class<T> claz) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(request, headers);
		return postForObject(uri, requestEntity, claz);
	}

}
