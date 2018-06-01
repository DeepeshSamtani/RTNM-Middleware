package com.harman.rtnm.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import com.harman.rtnm.model.common.SiteOption;
import com.harman.rtnm.service.SiteOptionService;



@EnableAspectJAutoProxy(proxyTargetClass = true)
@Component
public class LoadApplicationData {

	public static HashMap<String, String> siteOptionMap = new HashMap<String, String>();
	
	@Autowired
	SiteOptionService siteOptionService;

	@PostConstruct
	public void loadApplicationData() throws Exception {
		if (siteOptionMap.isEmpty()) {
			List<SiteOption> siteOptionData = siteOptionService.loadSiteOption();
			siteOptionData.forEach((siteData) -> {
				siteOptionMap.put(siteData.getDimension(), siteData.getValue());
			});
		}
	}
}
