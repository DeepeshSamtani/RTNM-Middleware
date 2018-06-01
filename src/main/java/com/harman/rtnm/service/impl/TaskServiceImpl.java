package com.harman.rtnm.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.harman.rtnm.common.constant.ScheduleFrequency;
import com.harman.rtnm.common.constant.ScheduleTaskType;
import com.harman.rtnm.common.exception.BusinessException;
import com.harman.rtnm.common.helper.DateHelper;
import com.harman.rtnm.common.helper.ReportDownloadHelper;
import com.harman.rtnm.common.helper.SchedulerPropertyHelper;
import com.harman.rtnm.dao.TaskDao;
import com.harman.rtnm.dao.UserDetailDao;
import com.harman.rtnm.model.StorageDetails;
import com.harman.rtnm.model.Task;
import com.harman.rtnm.model.TaskStatus;
import com.harman.rtnm.model.UserDetail;
import com.harman.rtnm.model.response.ExportDetails;
import com.harman.rtnm.model.response.UserReport;
import com.harman.rtnm.service.ReportService;
import com.harman.rtnm.service.ScheduleTimeMetadataService;
import com.harman.rtnm.service.StorageDetailsService;
import com.harman.rtnm.service.TaskService;
import com.harman.rtnm.service.TaskStatusService;
import com.harman.rtnm.service.UserDetailService;
import com.harman.rtnm.util.ObjectFactory;
import com.harman.rtnm.vo.ReportDetailVO;
import com.harman.rtnm.vo.TaskVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TaskServiceImpl implements TaskService {
	@Autowired
	TaskDao taskDao;

	@Autowired
	UserDetailDao userDetailDao;

	@Autowired
	SchedulerPropertyHelper helper;

	@Autowired
	ReportDownloadHelper reportDownloadHelper;

	@Autowired
	ReportService reportService;

	@Autowired
	TaskStatusService taskStatusService;

	@Autowired
	StorageDetailsService storageDetailsService;

	@Autowired
	UserDetailService userDetailService;

	@Autowired
	ScheduleTimeMetadataService scheduleTimeMetadataService;

	private ObjectMapper mapper = ObjectFactory.getObjectMapper();

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public void createTask(TaskVO taskVO) throws Exception {
		Task task = prepareTask(taskVO);
		taskDao.createTask(task);

	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public void updateTask(Task task) {
		taskDao.updateTask(task);

	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<TaskVO> getTasksByUser(String userName) throws Exception {
		UserDetail userDetail = userDetailService.loadUserByName(userName);
		List<Task> tasks = null;
		List<TaskVO> taskVOs = new ArrayList<>();
		if (null != userDetail) {
			tasks = taskDao.getTasksByUser(userDetail);
			if (!tasks.isEmpty()) {
				tasks.forEach(task -> {
					try {
						TaskVO taskVO = new TaskVO();
						BeanUtils.copyProperties(task, taskVO);
						if (task.getTaskType().equalsIgnoreCase(ScheduleTaskType.DAILY.getValue())) {
							if (task.getFrequency().equalsIgnoreCase(ScheduleTaskType.DAILY.getValue())) {
								taskVO.setDailytaskHourfrequency(ScheduleFrequency.HOURS_24.getValue());
							} else {
								taskVO.setDailytaskHourfrequency(task.getFrequency());
							}
						}
						taskVO.setTaskParamValue(mapper.readValue(task.getTaskParamValue(), ReportDetailVO.class));
						taskVOs.add(taskVO);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}
		}
		return taskVOs;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public int deleteTask(int taskId) throws Exception {
		return taskDao.deleteTask(taskId);
	}

	private Task prepareTask(TaskVO taskVO) throws Exception {
		Task task = new Task();

		ReportDetailVO reportDetailVO = taskVO.getTaskParamValue();
		String userName = reportDetailVO.getUserName();

		task.setUserDetail(userDetailDao.loadUserByName(userName));

		if (taskVO.getTaskName() != null) {
			task.setTaskName(taskVO.getTaskName());

		} else {
			throw new BusinessException("TaskName can not be Null");
		}

		if (taskVO.getUserTemplateId() != null) {
			task.setUserTemplateId(taskVO.getUserTemplateId());
			reportDetailVO.setUserTemplateId(taskVO.getUserTemplateId());
		} else {
			throw new BusinessException("UserTemplateId can not be Null");
		}

		if (taskVO.getTaskType() != null) {
			task.setTaskType(taskVO.getTaskType());
			task.setTaskExecutionDate(DateHelper.getTaskExecutionDate(taskVO));
			taskVO.setTaskExecutionDate(task.getTaskExecutionDate());
			task.setFrequency(setTaskFrequency(taskVO));

		} else {
			throw new BusinessException("TaskType can not be Null");
		}

		if (taskVO.getTaskParamValue() != null) {
			task.setTaskParamValue(mapper.writeValueAsBytes(taskVO.getTaskParamValue()));
			if (taskVO.getDailytaskHourfrequency() == null
					|| taskVO.getDailytaskHourfrequency().equalsIgnoreCase(ScheduleFrequency.HOURS_24.getValue())) {
				String scheduleTime = scheduleTimeMetadataService
						.getScheduleTimebyTaskType(taskVO.getTaskType().toLowerCase());
				if (null != scheduleTime) {
					taskVO.setTaskExecutionTime(scheduleTime);
				}
			}
			task.setTaskExecutionTime(taskVO.getTaskExecutionTime());
		} else {
			throw new BusinessException("TaskParamValue can not be Null");
		}

		task.setTaskStatus("wait");
		return task;
	}

	@Scheduled(fixedDelayString = "${scheduled.cron.time.seconds}000")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public void executeReportScheduler() throws Exception {
		DateFormat timeFormat = new SimpleDateFormat(DateHelper.getCurrentTimeAsHHMMSS());
		Long seconds = Long.parseLong(helper.getTimer());
		List<Task> tasks = taskDao.getTaskDetails(seconds);
		ReportDetailVO reportDetailVO = null;
		for (Task task : tasks) {
			TaskStatus taskStatus = new TaskStatus();
			taskStatus.setTaskPickedTime(timeFormat.format(new Date()));
			task.setTaskStatus("picked");
			StorageDetails storageDetails = new StorageDetails();
			try {
				reportDetailVO = mapper.readValue(new String(task.getTaskParamValue()),
						new TypeReference<ReportDetailVO>() {
						});
				reportDetailVO.setScheduledReport(true);
				setJsonStringForReport(task, reportDetailVO);
				storageDetails.setUserTemplateId(reportDetailVO.getUserTemplateId());
				exportReports(reportDetailVO, storageDetails);
				taskStatus.setTaskExecutionStatus("Success");
				taskStatus.setComments(task.getTaskType() + " task successfully completed");
				task.setTaskStatus("wait");
				storageDetails.setUserId(task.getUserDetail().getUserId());

			} catch (Exception ex) {
				task.setTaskStatus("wait");
				ex.printStackTrace();
				taskStatus.setTaskExecutionStatus("Failure");
				taskStatus.setComments(task.getTaskType() + " Task failure..." + "Reason " + ex.getMessage());
				// reportDownloadHelper.sendMail(null, null,
				// reportDetailVO.getUserName(), null, reportDetailVO,
				// taskStatus.getComments());
			}
			taskStatus.setTaskEndTime(timeFormat.format(new Date()));
			taskStatus.setTaskExecutionDateTime(task.getTaskExecutionDate() + " " + task.getTaskExecutionTime());
			taskStatus.setTask(task);
			updateTaskDetails(task);
			updateTask(task);
			taskStatusService.createTaskStatus(taskStatus);
			storageDetailsService.saveStorageDetails(storageDetails);

		}
	}

	private void exportReports(ReportDetailVO reportDetailVO, StorageDetails storageDetails) throws Exception {

		ExportDetails exportDetails = new ExportDetails();
		exportDetails.setUserReport(reportService.getUserReport(reportDetailVO));
		exportDetails.setFileHeaderList(new ArrayList<>());
		exportDetails.setFileContent(reportDownloadHelper.processDruidResponseData(exportDetails, reportDetailVO));
		exportDetails.setReportType(reportDetailVO.getFileType());
		exportDetails.setReportFlag(true);
		reportDownloadHelper.generateReport(exportDetails);
		UserReport usertemplate = exportDetails.getUserReport();
		String fileName = reportDownloadHelper
				.exctractReportName(usertemplate.getReportConfiguration().getConfiguration());
		String pathURL = exportDetails.getFilePath();
		storageDetails.setFilePath(exportDetails.getFilePath());
		storageDetails.setFilePathUrl(exportDetails.getFilePathURL());
		reportDownloadHelper.sendMail(pathURL, reportDetailVO.getFileType(), reportDetailVO.getUserName(), fileName,
				reportDetailVO, null);

	}

	public void updateTaskDetails(Task task) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Calendar calendar = Calendar.getInstance();
		if (null != task.getTaskType() && !StringUtils.isEmpty(task.getTaskType())) {
			String taskExecutionDate = task.getTaskExecutionDate();

			try {

				calendar.setTime(simpleDateFormat.parse(taskExecutionDate));
				LocalTime time = LocalTime.parse(task.getTaskExecutionTime());
				if (task.getTaskType().equalsIgnoreCase(ScheduleTaskType.DAILY.getValue())) {

					if (!task.getFrequency().equalsIgnoreCase(ScheduleTaskType.DAILY.getValue())) {
						if (time.getHour() == 0) {
							calendar.add(Calendar.DATE, 1);
							task.setTaskExecutionDate(simpleDateFormat.format(calendar.getTime()));
						} else {
							time = time.plusHours(Integer.parseInt(task.getFrequency().split(" ")[0]));
							task.setTaskExecutionTime(time.toString());
							if (time.getHour() == 0) {
								calendar.add(Calendar.DATE, 1);
								task.setTaskExecutionDate(simpleDateFormat.format(calendar.getTime()));
								System.out.println(task.getTaskExecutionDate());
							}
						}

					} else {
						calendar.add(Calendar.DATE, 1);
						task.setTaskExecutionDate(simpleDateFormat.format(calendar.getTime()));
					}
				} else if (task.getTaskType().equalsIgnoreCase(ScheduleTaskType.WEEKLY.getValue())) {
					calendar.add(Calendar.DATE, 7);
					task.setTaskExecutionDate(simpleDateFormat.format(calendar.getTime()));
				} else if (task.getTaskType().equalsIgnoreCase(ScheduleTaskType.MONTHLY.getValue())) {
					calendar.add(Calendar.MONTH, 1);
					task.setTaskExecutionDate(simpleDateFormat.format(calendar.getTime()));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public String setTaskFrequency(TaskVO taskVO) throws Exception{
		String frequency = taskVO.getTaskType();
		if (taskVO.getTaskType().equalsIgnoreCase(ScheduleTaskType.DAILY.getValue())
				&& !taskVO.getDailytaskHourfrequency().equalsIgnoreCase(ScheduleFrequency.HOURS_24.getValue())) {
			frequency = taskVO.getDailytaskHourfrequency();
		}
		return frequency;
	}

	public void setJsonStringForReport(Task task, ReportDetailVO reportDetailVO) throws ParseException {

		Map<String, Object> jsonString = reportDetailVO.getJsonString();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate executionDate = LocalDate.parse(task.getTaskExecutionDate(), formatter);
		LocalDateTime endDate = LocalDateTime.of(executionDate, LocalTime.MIN);
		LocalDateTime starteDate = LocalDateTime.of(executionDate, LocalTime.MIN);
		String granularity = "DAILY";
		ZoneId utcZoneID = ZoneId.of("Europe/London");
		if (null == jsonString) {
			jsonString = new HashMap<String, Object>();

		}
		if (task.getFrequency().toLowerCase().contains("hours")) {
			LocalTime executiontime = LocalTime.parse(task.getTaskExecutionTime());
			endDate = endDate.withHour(executiontime.getHour());
			executiontime = executiontime.minusHours(Long.valueOf(task.getFrequency().split(" ")[0]));
			starteDate = starteDate.withHour(executiontime.getHour());
			granularity = "HOURLY";
		} else if (task.getFrequency().equalsIgnoreCase(ScheduleTaskType.DAILY.getValue())) {
			starteDate = starteDate.minusDays(1);
			granularity = "HOURLY";
		} else if (task.getFrequency().equalsIgnoreCase(ScheduleTaskType.WEEKLY.getValue())) {
			starteDate = starteDate.minusWeeks(1);

		} else if (task.getFrequency().equalsIgnoreCase(ScheduleTaskType.MONTHLY.getValue())) {
			starteDate = starteDate.minusMonths(1);
		}

		ZonedDateTime startUTCdate = starteDate.atZone(utcZoneID);
		ZonedDateTime endUTCdate = endDate.atZone(utcZoneID);

		jsonString.put("intervals",
				(startUTCdate.toString()).split("Z")[0] + "Z/" + endUTCdate.toString().split("Z")[0].concat("Z"));
		jsonString.put("granularity", granularity);
		reportDetailVO.setJsonString(jsonString);
	}

}
