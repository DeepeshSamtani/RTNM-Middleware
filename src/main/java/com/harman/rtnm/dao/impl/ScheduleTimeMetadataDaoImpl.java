package com.harman.rtnm.dao.impl;

import org.springframework.stereotype.Repository;

import com.harman.rtnm.dao.AbstractDAO;
import com.harman.rtnm.dao.ScheduleTimeMetadataDao;
import com.harman.rtnm.model.ScheduleTimeMetaData;

@SuppressWarnings("serial")
@Repository
public class ScheduleTimeMetadataDaoImpl extends AbstractDAO<ScheduleTimeMetaData> implements ScheduleTimeMetadataDao {

	@Override
	public String getScheduleTimebyTaskType(String taskType) {

		StringBuilder queryString = new StringBuilder();
		queryString.append(" from ScheduleTimeMetaData sm where sm.taskType='" + taskType + "'");
		ScheduleTimeMetaData scheduleTimeMetaData = executeQueryForUniqueRecord(queryString.toString());
		if (null != scheduleTimeMetaData) {
			return scheduleTimeMetaData.getScheduleTime();
		} else {
			return null;
		}
	}

}
