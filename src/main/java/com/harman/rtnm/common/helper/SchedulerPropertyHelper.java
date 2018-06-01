package com.harman.rtnm.common.helper;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:scheduler.properties")
public class SchedulerPropertyHelper {

	@Value("${scheduled.cron.time.seconds}")
	private String timer;

	public String getTimer() {
		return timer;
	}

}
