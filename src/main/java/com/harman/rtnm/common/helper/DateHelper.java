package com.harman.rtnm.common.helper;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.harman.rtnm.common.constant.DateConstant;
import com.harman.rtnm.common.constant.ScheduleFrequency;
import com.harman.rtnm.common.exception.BusinessException;
import com.harman.rtnm.util.ObjectFactory;
import com.harman.rtnm.vo.TaskVO;

public class DateHelper {

	private static final DateFormat dateFormatForDisplay = ObjectFactory.getDateFormatForDisplay();

	private static final DateTimeFormatter dateTimeFormatterForDisplay = DateTimeFormatter
			.ofPattern(DateConstant.dateFormatForDisplay);

	private static final SimpleDateFormat simpleDateFormat = ObjectFactory.getSimpleDateFormat();

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateConstant.dateFormat);

	public static long calculateHoursDifference(String interval) throws ParseException {
		SimpleDateFormat formatter;
		if (interval.contains("T")) {
			formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		} else {
			formatter = new SimpleDateFormat("yyyy-MM-dd");
		}

		String[] datelist = interval.split("/");

		Date date1 = formatter.parse(datelist[0]);
		Date date2 = formatter.parse(datelist[1]);

		return ChronoUnit.HOURS.between(date1.toInstant(), date2.toInstant());
	}

	public static String generateDruidIntervalForCurrent(LocalDateTime currentDate, String inputInterval)
			throws Exception {
		StringBuilder returndate = new StringBuilder();
		LocalDateTime fromDate = null;
		LocalDateTime toDate = null;
		String[] inputDatesplit = null;
		String inputType = null;
		Integer inputValue = null;

		inputDatesplit = inputInterval.split(" ");
		inputValue = Integer.parseInt(inputDatesplit[1]);
		inputType = inputDatesplit[2].toLowerCase();

		if (inputType.contains(DateConstant.MINUTE)) {
			int currentMinutes = currentDate.getMinute();
			int startMinute = 0;
			int temp = inputValue;

			while (temp <= 60) {
				if (currentMinutes <= temp) {
					startMinute = temp - inputValue + 1;
					break;
				}
				temp = temp + inputValue;
			}
			fromDate = LocalDateTime.of(currentDate.getYear(), currentDate.getMonth(), currentDate.getDayOfMonth(),
					currentDate.getHour(), startMinute);
			toDate = fromDate.plusMinutes(inputValue - 1);
		} else if (inputType.contains(DateConstant.HOUR)) {
			fromDate = currentDate.minusHours(inputValue - 1).minusMinutes(currentDate.getMinute())
					.minusSeconds(currentDate.getSecond()).minusNanos(currentDate.getNano());
			toDate = fromDate.plusHours(inputValue);
		} else if (inputType.contains(DateConstant.DAY)) {
			fromDate = currentDate.minusHours(currentDate.getHour()).minusMinutes(currentDate.getMinute())
					.minusSeconds(currentDate.getSecond()).minusDays(inputValue - 1).minusNanos(currentDate.getNano());
			toDate = LocalDateTime.of(currentDate.toLocalDate(), LocalTime.MAX);
		} else if (inputType.contains(DateConstant.WEEK)) {
			fromDate = currentDate.minusWeeks(inputValue - 1);
			fromDate = fromDate.minusDays(fromDate.getDayOfWeek().getValue() - 1);
			fromDate = LocalDateTime.of(fromDate.toLocalDate(), LocalTime.MIN);
			// Go forward to get Sunday
			while (currentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
				currentDate = currentDate.plusDays(1);
			}

			toDate = LocalDateTime.of(currentDate.toLocalDate(), LocalTime.MAX);
		} else if (inputType.contains(DateConstant.MONTH)) {
			fromDate = currentDate.minusMonths(inputValue - 1);
			fromDate = fromDate.minusDays(fromDate.getDayOfMonth() - 1);
			fromDate = LocalDateTime.of(fromDate.toLocalDate(), LocalTime.MIN);

			toDate = currentDate.withDayOfMonth(currentDate.getMonth().maxLength());
			toDate = LocalDateTime.of(toDate.toLocalDate(), LocalTime.MAX);
		} else if (inputType.contains(DateConstant.YEAR)) {
			fromDate = currentDate.minusYears(inputValue - 1).minusHours(currentDate.getHour())
					.minusMinutes(currentDate.getMinute()).minusSeconds(currentDate.getSecond())
					.minusDays(currentDate.getDayOfYear() - 1).minusNanos(currentDate.getNano());

			toDate = currentDate.with(TemporalAdjusters.lastDayOfYear());
			toDate = LocalDateTime.of(toDate.toLocalDate(), LocalTime.MAX);
		}

		returndate.append(fromDate.format(formatter));
		returndate.append("/");
		returndate.append(toDate.format(formatter));

		return returndate.toString();
	}

	public static String generateDruidIntervalForPrevious(LocalDateTime currentDate, String inputInterval) {
		StringBuilder returndate = new StringBuilder();
		LocalDateTime fromDate = null;
		LocalDateTime toDate = null;
		String[] inputDatesplit = null;
		String inputType = null;
		Integer inputValue = null;

		inputDatesplit = inputInterval.split(" ");
		inputValue = Integer.parseInt(inputDatesplit[1]);
		inputType = inputDatesplit[2].toLowerCase();

		if (inputType.contains(DateConstant.MINUTE)) {
			int currentMinutes = currentDate.getMinute();
			int startMinute = 0;
			int temp = inputValue;

			while (temp <= 60) {
				if (currentMinutes <= temp) {
					startMinute = temp - inputValue + 1;
					break;
				}
				temp = temp + inputValue;
			}
			fromDate = LocalDateTime.of(currentDate.getYear(), currentDate.getMonth(), currentDate.getDayOfMonth(),
					currentDate.getHour(), startMinute);
			fromDate = fromDate.minusMinutes(inputValue);
			toDate = fromDate.plusMinutes(inputValue - 1);
		} else if (inputType.contains(DateConstant.HOUR)) {
			fromDate = currentDate.minusHours(inputValue).minusMinutes(currentDate.getMinute())
					.minusSeconds(currentDate.getSecond()).minusNanos(currentDate.getNano());
			toDate = fromDate.plusHours(inputValue);
		} else if (inputType.contains(DateConstant.DAY)) {
			fromDate = currentDate.minusHours(currentDate.getHour()).minusMinutes(currentDate.getMinute())
					.minusSeconds(currentDate.getSecond()).minusDays(inputValue).minusNanos(currentDate.getNano());
			toDate = LocalDateTime.of(currentDate.minusDays(1).toLocalDate(), LocalTime.MAX);
		} else if (inputType.contains(DateConstant.WEEK)) {
			fromDate = currentDate.minusWeeks(inputValue);
			fromDate = fromDate.minusDays(fromDate.getDayOfWeek().getValue() - 1);
			fromDate = LocalDateTime.of(fromDate.toLocalDate(), LocalTime.MIN);

			// Go forward to get Sunday
			// LocalDate sunday = today;
			while (currentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
				currentDate = currentDate.plusDays(1);
			}

			toDate = LocalDateTime.of(currentDate.minusWeeks(1).toLocalDate(), LocalTime.MAX);
		} else if (inputType.contains(DateConstant.MONTH)) {
			fromDate = currentDate.minusMonths(inputValue);
			fromDate = fromDate.minusDays(fromDate.getDayOfMonth() - 1);
			fromDate = LocalDateTime.of(fromDate.toLocalDate(), LocalTime.MIN);

			toDate = currentDate.withDayOfMonth(currentDate.getMonth().maxLength());
			toDate = LocalDateTime.of(toDate.minusMonths(1).toLocalDate(), LocalTime.MAX);
		} else if (inputType.contains(DateConstant.YEAR)) {
			fromDate = currentDate.minusYears(inputValue).minusHours(currentDate.getHour())
					.minusMinutes(currentDate.getMinute()).minusSeconds(currentDate.getSecond())
					.minusDays(currentDate.getDayOfYear() - 1).minusNanos(currentDate.getNano());

			toDate = currentDate.with(TemporalAdjusters.lastDayOfYear());
			toDate = LocalDateTime.of(toDate.minusYears(1).toLocalDate(), LocalTime.MAX);
		}

		returndate.append(fromDate.format(formatter));
		returndate.append("/");
		returndate.append(toDate.format(formatter));

		return returndate.toString();
	}

	public static String generateDruidIntervalForLast(LocalDateTime toDate, String inputInterval) throws Exception {

		LocalDateTime fromDate = null;
		StringBuilder returndate = new StringBuilder();
		String[] inputDatesplit = null;
		String inputType = null;
		Integer inputValue = null;

		inputDatesplit = inputInterval.split(" ");
		inputValue = Integer.parseInt(inputDatesplit[1]);
		inputType = inputDatesplit[2].toLowerCase();

		if (inputType.contains(DateConstant.MINUTE)) {
			fromDate = toDate.minusMinutes(inputValue);
		} else if (inputType.contains(DateConstant.HOUR)) {
			fromDate = toDate.minusHours(inputValue);
		} else if (inputType.contains(DateConstant.DAY)) {
			fromDate = toDate.minusDays(inputValue);
		} else if (inputType.contains(DateConstant.WEEK)) {
			fromDate = toDate.minusWeeks(inputValue);
		} else if (inputType.contains(DateConstant.MONTH)) {
			fromDate = toDate.minusMonths(inputValue);
		} else if (inputType.contains(DateConstant.YEAR)) {
			fromDate = toDate.minusYears(inputValue);
		}

		returndate.append(fromDate.format(formatter));
		returndate.append("/");
		returndate.append(toDate.format(formatter));

		return returndate.toString();
	}

	public static String generateDruidIntervalForYesterdayToday(LocalDateTime toDate, String inputInterval)
			throws Exception {

		LocalDateTime fromDate = null;
		StringBuilder returndate = new StringBuilder();

		switch (inputInterval) {

		case DateConstant.TODAY:
			fromDate = toDate.minusHours(toDate.getHour()).minusMinutes(toDate.getMinute())
					.minusSeconds(toDate.getSecond()).minusNanos(toDate.getNano());
			break;
		case DateConstant.YESTERDAY:
			fromDate = toDate.minusHours(toDate.getHour()).minusMinutes(toDate.getMinute())
					.minusSeconds(toDate.getSecond()).minusDays(1).minusNanos(toDate.getNano());
			toDate = LocalDateTime.of(fromDate.toLocalDate(), LocalTime.MAX);
			break;
		}

		returndate.append(fromDate.format(formatter));
		returndate.append("/");
		returndate.append(toDate.format(formatter));

		return returndate.toString();
	}

	/**
	 * 
	 * @param currentDate
	 * @param policyTaskType
	 * @return
	 */
	public static String generateDateIntervalFromPolicy(LocalDateTime currentDate, String policyTaskType) {
		StringBuilder returndate = new StringBuilder();
		LocalDateTime toDate = null;
		if (DateConstant.HOURLY.equalsIgnoreCase(policyTaskType)) {
			toDate = currentDate.plusHours(1);
		} else if (DateConstant.DAILY.equalsIgnoreCase(policyTaskType)) {
			toDate = currentDate.plusDays(1);
		} else if (DateConstant.WEEKLY.equalsIgnoreCase(policyTaskType)) {
			toDate = currentDate.plusWeeks(1);
		} else if (DateConstant.MONTHLY.equalsIgnoreCase(policyTaskType)) {
			toDate = currentDate.plusMonths(1);
		} else if (DateConstant.YEARLY.equalsIgnoreCase(policyTaskType)) {
			toDate = currentDate.plusYears(1);
		}
		returndate.append(currentDate.format(dateTimeFormatterForDisplay));
		returndate.append("/");
		returndate.append(toDate.format(dateTimeFormatterForDisplay));
		return returndate.toString();
	}

	public static long convertDateToTimestamp(String date) throws Exception {
		Timestamp timestamp = new Timestamp(simpleDateFormat.parse(date).getTime());
		return timestamp.getTime();
	}

	public static String convertDateIntoString(Date date) {
		if (null != date)
			return dateFormatForDisplay.format(date);
		return null;
	}
	/*
	 * public static String getTaskExecutionDate(TaskVO taskVO) throws Exception
	 * {
	 * 
	 * DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); // DateFormat
	 * timeFormatter = new SimpleDateFormat("HH:mm:ss"); LocalDateTime
	 * localDateTime = LocalDateTime.now(); LocalTime currentTime =
	 * LocalTime.now(); LocalDate today = LocalDate.now(); String currentDate =
	 * dateFormat.format(java.sql.Date.valueOf(today)); String taskExecutionDate
	 * = null;
	 * 
	 * if (taskVO.getTaskType().equalsIgnoreCase("hourly")) { String[] timeArray
	 * = taskVO.getTaskExecutionTime().split(":"); LocalTime executionTime =
	 * LocalTime.of(localDateTime.getHour(), Integer.parseInt(timeArray[1]));
	 * 
	 * if (executionTime.isBefore(currentTime)) { executionTime =
	 * LocalTime.of(localDateTime.getHour() + 1,
	 * Integer.parseInt(timeArray[1])); }
	 * 
	 * taskExecutionDate = dateFormat.format(new Date());
	 * taskVO.setTaskExecutionTime(executionTime.toString());
	 * 
	 * } else if (taskVO.getTaskType().equalsIgnoreCase("daily")) {
	 * taskExecutionDate = dateFormat.format(new Date()); taskExecutionDate =
	 * getExecutionDate(taskExecutionDate, currentTime, currentDate, taskVO,
	 * localDateTime, today, dateFormat);
	 * 
	 * } else if (taskVO.getTaskType().equalsIgnoreCase("weekly")) { if
	 * (taskVO.getTaskExecutionDay() != null) { LocalDate nextDay =
	 * LocalDate.now().with(
	 * TemporalAdjusters.nextOrSame(DayOfWeek.valueOf(taskVO.getTaskExecutionDay
	 * ().toUpperCase()))); taskExecutionDate =
	 * dateFormat.format(java.sql.Date.valueOf(nextDay)); taskExecutionDate =
	 * getExecutionDate(taskExecutionDate, currentTime, currentDate, taskVO,
	 * localDateTime, nextDay, dateFormat);
	 * 
	 * } else { throw new BusinessException("Day can not be Null"); }
	 * 
	 * } else if (taskVO.getTaskType().equalsIgnoreCase("monthly")) {
	 * 
	 * if (taskVO.getTaskExecutionDate() != null) { int noOfDay =
	 * today.withDayOfMonth(today.lengthOfMonth()).getDayOfMonth(); if (noOfDay
	 * >= Integer.parseInt(taskVO.getTaskExecutionDate())) { LocalDate localDate
	 * = LocalDate.of(localDateTime.getYear(), localDateTime.getMonth(),
	 * Integer.parseInt(taskVO.getTaskExecutionDate())); taskExecutionDate =
	 * dateFormat.format(java.sql.Date.valueOf(localDate)); taskExecutionDate =
	 * getExecutionDate(taskExecutionDate, currentTime, currentDate, taskVO,
	 * localDateTime, localDate, dateFormat); } else { throw new
	 * BusinessException("Invalid TaskExecutionDate"); }
	 * 
	 * } else { throw new BusinessException("TaskExecutionDate can not be Null"
	 * ); } } else if (taskVO.getTaskType().equalsIgnoreCase("yearly")) { if
	 * (taskVO.getTaskExecutionDate() != null && taskVO.getTaskExecutionMonth()
	 * != null) { LocalDate localDate = LocalDate.of(localDateTime.getYear(),
	 * Integer.parseInt(taskVO.getTaskExecutionMonth()),
	 * Integer.parseInt(taskVO.getTaskExecutionDate()));
	 * 
	 * taskExecutionDate = dateFormat.format(java.sql.Date.valueOf(localDate));
	 * taskExecutionDate = getExecutionDate(taskExecutionDate, currentTime,
	 * currentDate, taskVO, localDateTime, localDate, dateFormat);
	 * 
	 * } else { throw new BusinessException("TaskExecutionDate can not be Null"
	 * ); } }
	 * 
	 * return taskExecutionDate; }
	 */

	/*
	 * private static String getExecutionDate(String taskExecutionDate,
	 * LocalTime currentTime, String currentDate, TaskVO taskVO, LocalDateTime
	 * localDateTime, LocalDate executionDate, DateFormat dateFormat) {
	 * LocalDate date = null;
	 * 
	 * if (currentDate.equals(taskExecutionDate)) { String[] timeArray =
	 * taskVO.getTaskExecutionTime().split(":"); LocalTime executionTime =
	 * LocalTime.of(Integer.parseInt(timeArray[0]),
	 * Integer.parseInt(timeArray[1]));
	 * 
	 * if (executionTime.isBefore(currentTime)) { if
	 * (taskVO.getTaskType().equalsIgnoreCase("Daily")) { date =
	 * LocalDate.now().plusDays(1); } else if
	 * (taskVO.getTaskType().equalsIgnoreCase("Weekly")) { date =
	 * executionDate.plusWeeks(1); } else if
	 * (taskVO.getTaskType().equalsIgnoreCase("Monthly")) { date =
	 * executionDate.plusMonths(1); } else if
	 * (taskVO.getTaskType().equalsIgnoreCase("Yearly")) { date =
	 * executionDate.plusYears(1); } } }
	 * 
	 * if (date != null) { taskExecutionDate =
	 * dateFormat.format(java.sql.Date.valueOf(date)); }
	 * 
	 * return taskExecutionDate; }
	 */
	public static DateTimeFormatter getDateTimeFormat() {
		return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	}

	public static String getDay(String executionDate) {
		String day = "";
		if (!executionDate.isEmpty() && null != executionDate) {
			String[] parts = executionDate.split("\\-");
			day = parts[0].substring(1);
		}
		return day;
	}

	public static String getWeekDay(String executionDate) throws ParseException {
		String weekDay = "";
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		if (!executionDate.isEmpty() && null != executionDate) {
			Date date = formatter.parse(executionDate);
			weekDay = new SimpleDateFormat("EEEE").format(date.getTime());
		}
		return weekDay;
	}

	public static Date getDateAccoringToInterval(long interval) {
		return new Date(System.currentTimeMillis() - interval);
	}

	public static DateFormat getTimeFormatHHMMSS() {
		return new SimpleDateFormat("HH:mm:ss");
	}

	public static String getCurrentTimeAsHHMMSS() {
		DateFormat timeFormat = getTimeFormatHHMMSS();
		return timeFormat.format(new Date());
	}

	public static String getDateTimeFormatFromMillisec(String milliSec) {
		if (null != milliSec) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Long milliSeconds = Long.parseLong(milliSec);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(milliSeconds);
			String dateTimeFormat = formatter.format(calendar.getTime());
			return dateTimeFormat;
		}
		return null;
	}

	public static String getTaskExecutionDate(TaskVO taskVO) throws Exception {

		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		// DateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
		LocalDateTime localDateTime = LocalDateTime.now();
		LocalTime currentTime = LocalTime.now();
		LocalDate today = LocalDate.now();
		String currentDate = dateFormat.format(java.sql.Date.valueOf(today));
		String taskExecutionDate = null;

		if (taskVO.getTaskType().equalsIgnoreCase("daily")) {
	
			taskExecutionDate = dateFormat.format(new Date());
			String dailytaskHourfrequency = taskVO.getDailytaskHourfrequency();
			LocalDateTime executionTime = null;
			executionTime = LocalDate.now().atStartOfDay();
			if(dailytaskHourfrequency == null || StringUtils.isEmpty(dailytaskHourfrequency)){
				throw new BusinessException("dailytaskHourfrequency can not be blank For daily task type.");
			}
			taskVO.setTaskExecutionTime(executionTime.toLocalTime().toString());
			taskExecutionDate = getExecutionDate(taskExecutionDate, currentTime, currentDate, taskVO, localDateTime,
					today, dateFormat);
			
			if (!dailytaskHourfrequency.equalsIgnoreCase(ScheduleFrequency.HOURS_24.getValue())) {
				String[] timeArr = taskVO.getDailytaskHourfrequency().split(" ");
				int currentHourValue = currentTime.getHour();
				int hourFrequency = Integer.parseInt(timeArr[0]);
				int executionhour = 0;
				if ((currentHourValue % hourFrequency) == 0 && currentTime.getMinute() == 0) {
					executionhour = currentHourValue;
				} else {
					executionhour = (hourFrequency - (currentHourValue % hourFrequency)) + currentHourValue;
					if(executionhour == 24)
					{
						executionhour=0;
						today=today.plusDays(1);
						taskExecutionDate = dateFormat.format(java.sql.Date.valueOf(today));
					}
				}
				taskVO.setTaskExecutionTime(executionTime.withHour(executionhour).toLocalTime().toString());
			}

		} else if (taskVO.getTaskType().equalsIgnoreCase("weekly")) {
				LocalDate nextWeekDay = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
				taskExecutionDate = dateFormat.format(java.sql.Date.valueOf(nextWeekDay));
				taskVO.setTaskExecutionTime(LocalDate.now().atStartOfDay().toLocalTime().toString());
				taskExecutionDate = getExecutionDate(taskExecutionDate, currentTime, currentDate, taskVO, localDateTime,
						nextWeekDay, dateFormat);
				

		} else if (taskVO.getTaskType().equalsIgnoreCase("monthly")) {

		  LocalDate nextmonthDay = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());
		  taskExecutionDate= dateFormat.format(java.sql.Date.valueOf(nextmonthDay));
		  taskVO.setTaskExecutionTime(LocalDate.now().atStartOfDay().toLocalTime().toString());
		}
		return taskExecutionDate;
	}

	private static String getExecutionDate(String taskExecutionDate, LocalTime currentTime, String currentDate,
			TaskVO taskVO, LocalDateTime localDateTime, LocalDate executionDate, DateFormat dateFormat) {
		LocalDate date = null;

		if (currentDate.equals(taskExecutionDate)) {
			String[] timeArray = taskVO.getTaskExecutionTime().split(":");
			LocalTime executionTime = LocalTime.of(Integer.parseInt(timeArray[0]), Integer.parseInt(timeArray[1]));

			if (executionTime.isBefore(currentTime)) {
				if (taskVO.getTaskType().equalsIgnoreCase("Daily")) {
					if (taskVO.getDailytaskHourfrequency().equalsIgnoreCase(ScheduleFrequency.HOURS_24.getValue())) {
						date = LocalDate.now().plusDays(1);
					}
				} else if (taskVO.getTaskType().equalsIgnoreCase("Weekly")) {
					date = executionDate.plusWeeks(1);
				} else if (taskVO.getTaskType().equalsIgnoreCase("Monthly")) {
					date = executionDate.plusMonths(1);
				} else if (taskVO.getTaskType().equalsIgnoreCase("Yearly")) {
					date = executionDate.plusYears(1);
				}
			}
		}

		if (date != null) {
			taskExecutionDate = dateFormat.format(java.sql.Date.valueOf(date));
		}

		return taskExecutionDate;
	}
}
