package com.harman.rtnm.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.harman.rtnm.common.constant.ScheduleFrequency;
import com.harman.rtnm.common.constant.ScheduleTaskType;
import com.harman.rtnm.common.helper.DateHelper;
import com.harman.rtnm.model.Counter;
import com.harman.rtnm.model.Task;
import com.harman.rtnm.service.CounterService;
import com.harman.rtnm.service.ScheduleTimeMetadataService;
import com.harman.rtnm.service.TaskService;
import com.harman.rtnm.service.UserDetailService;
import com.harman.rtnm.vo.CounterVO;
import com.harman.rtnm.vo.TaskVO;
import com.fasterxml.jackson.databind.ObjectMapper;

@EnableAspectJAutoProxy(proxyTargetClass = true)
@RestController
@RequestMapping(value = "/task")
public class TaskController {

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private CounterService counterService;
	
	
	@Autowired
	ScheduleTimeMetadataService scheduleTimeMetadataService;
	
	@Autowired
	UserDetailService userDetailService;
	
	@RequestMapping(value = "/insertCounter", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CounterVO> insertCounter(@RequestBody CounterVO counterVO) throws Exception {
		try {
			Counter counterObj=new Counter();
			//counterObj.setCounterDescription(counterVO.get);
			//counterService.createTask(counterVO);
		    //counterService.addCounter(counter)
		    
		} catch (Exception e) {
			return new ResponseEntity<CounterVO>(counterVO, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<CounterVO>(counterVO, HttpStatus.OK);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TaskVO> createTask(@RequestBody TaskVO taskVO) throws Exception {
		try {
			taskService.createTask(taskVO);
			taskVO.setMessage("Task created Succesfully.");
			taskVO.setMessagecode(200);
		} catch (Exception e) {

			taskVO = new TaskVO();
			taskVO.setMessage(e.getMessage());
			taskVO.setMessagecode(400);
			return new ResponseEntity<TaskVO>(taskVO, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<TaskVO>(taskVO, HttpStatus.OK);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TaskVO>> getTaskByUser(@RequestParam("username") String userName) {

		List<TaskVO> taskVOs = new ArrayList<>();
		try {
			taskVOs = taskService.getTasksByUser(userName);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<TaskVO>>(taskVOs, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<List<TaskVO>>(taskVOs, HttpStatus.OK);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TaskVO> updateTask(@RequestBody TaskVO taskVO) {
		try {
			Task task = new Task();
			ObjectMapper mapper = new ObjectMapper();
			BeanUtils.copyProperties(taskVO, task);
			if (taskVO.getTaskParamValue() != null) {
				task.setTaskParamValue(mapper.writeValueAsBytes(taskVO.getTaskParamValue()));
				task.setUserDetail(userDetailService.loadUserByName(taskVO.getTaskParamValue().getUserName()));
			}
			if (taskVO.getTaskType() != null) {
				task.setTaskType(taskVO.getTaskType());
				task.setTaskExecutionDate(DateHelper.getTaskExecutionDate(taskVO));
				task.setTaskStatus("wait");
				task.setTaskExecutionTime(taskVO.getTaskExecutionTime());
				if(taskVO.getDailytaskHourfrequency() == null || taskVO.getDailytaskHourfrequency().equalsIgnoreCase(ScheduleFrequency.HOURS_24.getValue()))
				{
					String scheduleTime = scheduleTimeMetadataService.getScheduleTimebyTaskType(taskVO.getTaskType().toLowerCase());
					if(null != scheduleTime)
					{
						task.setTaskExecutionTime(scheduleTime);
						taskVO.setTaskExecutionTime(scheduleTime);
					}
				}
				taskVO.setTaskExecutionDate(task.getTaskExecutionDate());
				if (taskVO.getTaskType().equalsIgnoreCase(ScheduleTaskType.DAILY.getValue()) && !taskVO
						.getDailytaskHourfrequency().equalsIgnoreCase(ScheduleFrequency.HOURS_24.getValue())) {
					task.setFrequency(taskVO.getDailytaskHourfrequency());

				} else {
					task.setFrequency(taskVO.getTaskType());
				}
				
			}
			taskService.updateTask(task);
			taskVO.setMessage("Task updated Succesfully.");
			taskVO.setMessagecode(200);
            
		} catch (Exception e) {
			e.printStackTrace();
			taskVO=new TaskVO();
			taskVO.setMessage(e.getMessage());
			return new ResponseEntity<TaskVO>(taskVO, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<TaskVO>(taskVO, HttpStatus.OK);
	}

	@RequestMapping(value = "/delete/{taskId}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteTask(@PathVariable int taskId) {
		String message = null;
		int returnCode = 0;
		try {
			returnCode = taskService.deleteTask(taskId);
			if (returnCode == 1) {
				message = "Task deleted SuccessFully";
			} else {
				message = "Task does not exist";
				return new ResponseEntity<String>(message, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Exception Occured during deletion of task", HttpStatus.NOT_MODIFIED);
		}
		return new ResponseEntity<String>(message, HttpStatus.OK);
	}
}
