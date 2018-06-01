package com.harman.rtnm.common.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:export_details.properties")
public class ExportDetailsProperties {

	@Value("${export.file.path}")
	private String exportFilePath;
	
	@Value("${tomcat.port}")
	private String tomcatPort;

	public String getExportFilePath() {
		return exportFilePath;
	}

	public void setExportFilePath(String exportFilePath) {
		this.exportFilePath = exportFilePath;
	}

	public String getTomcatPort() {
		return tomcatPort;
	}

	public void setTomcatPort(String tomcatPort) {
		this.tomcatPort = tomcatPort;
	}
	
	
}
