package com.harman.rtnm.dao.impl;

import java.util.List;

import javax.management.MXBean;

import org.springframework.stereotype.Repository;

import com.harman.rtnm.dao.AbstractDAO;
import com.harman.rtnm.dao.TableIdTestDao;
import com.harman.rtnm.model.Aggregation;
import com.harman.rtnm.model.Counter;
import com.harman.rtnm.model.CounterGroup;
import com.harman.rtnm.model.TableIdTest;

@Repository
public class TableIdTestDaoImpl extends AbstractDAO<TableIdTest> implements TableIdTestDao{

	@Override
	public String maxId (String deviceType) throws Exception {	
		
			String subStr = deviceType + "_C_%";
		
		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("SELECT max(cast(substring(c.id, "+subStr.length()+", "
				+ "(length(c.id)-"+(subStr.length()-1)+")) as int)) from TableIdTest c ");

		StringBuilder whereQuery = new StringBuilder();
		selectQuery.append(" where c.id like '"+subStr+"'");
		
		List<TableIdTest> rows = (List<TableIdTest>) executeQuery(selectQuery.toString());
		System.out.println(rows.get(0));
		String maxCount = "";		
		if(null!=rows && !rows.isEmpty())
			maxCount = String.valueOf(rows.get(0));
		else
			maxCount = 0+"";
		return maxCount;
	}

}
