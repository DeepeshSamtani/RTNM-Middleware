package com.harman.rtnm.common.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:mail.properties")
public class MailAttribute {

	@Value("${mail.server}")
	private String mailserver;

	@Value("${mail.from}")
	private String mailfrom;

	@Value("${mail.to}")
	private String mailto;

	@Value("{mail.default.report.name}")
	private String defaultReportName;

	public String getMailserver() {
		return mailserver;
	}

	public void setMailserver(String mailserver) {
		this.mailserver = mailserver;
	}

	public String getMailfrom() {
		return mailfrom;
	}

	public void setMailfrom(String mailfrom) {
		this.mailfrom = mailfrom;
	}

	public String getMailto() {
		return mailto;
	}

	public void setMailto(String mailto) {
		this.mailto = mailto;
	}

	public String getReportName() {
		return defaultReportName;
	}

	public void setReportName(String reportName) {
		this.defaultReportName = reportName;
	}

	@Override
	public String toString() {
		return "MailAttribute [mailserver=" + mailserver + ", mailfrom=" + mailfrom + ", mailto=" + mailto
				+ ", reportName=" + defaultReportName + "]";
	}

}
