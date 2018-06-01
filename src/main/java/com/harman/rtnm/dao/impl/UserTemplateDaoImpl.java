package com.harman.rtnm.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.harman.rtnm.dao.AbstractDAO;
import com.harman.rtnm.dao.UserTemplateDao;
import com.harman.rtnm.model.UserTemplate;
import com.harman.rtnm.vo.ReportDetailVO;

@Repository
public class UserTemplateDaoImpl extends AbstractDAO<UserTemplate> implements UserTemplateDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2201098889801064778L;
	private static final Logger logger = LoggerFactory.getLogger(UserTemplateDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public void saveUserTemplate(UserTemplate userTemplate) {
		save(userTemplate);
	}

	@Override
	public UserTemplate searchUserTemplate(ReportDetailVO reportDetailVO,int userId) throws Exception {
		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("from UserTemplate ut ");
		StringBuilder whereQuery = new StringBuilder();

		if (null != reportDetailVO) {
			whereQuery.append("where ");
				whereQuery.append("ut.userTemplateKey.userDetail.id='").append(userId + "'");
			
			if (!StringUtils.isEmpty(reportDetailVO.getUserTemplateId())) {
				whereQuery.append("and ut.userTemplateKey.usertemplateId='")
						.append(reportDetailVO.getUserTemplateId() + "'");
			}else
				throw new Exception("User Template Id can not be Null");
			selectQuery.append(whereQuery);
		}
		return executeQueryForUniqueRecord(selectQuery.toString());
	}

	@Override
	public List<UserTemplate> getUserTemplateByUserId(int userId) throws Exception {
		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("from UserTemplate ut ");
		StringBuilder whereQuery = new StringBuilder();

		whereQuery.append("where ");
		whereQuery.append("ut.userTemplateKey.userDetail.userId='").append(userId + "'");
		selectQuery.append(whereQuery);
		
		List<UserTemplate> userTemplatesList = new ArrayList();
		List<UserTemplate> userTemplates = (List<UserTemplate>) executeQuery(selectQuery.toString());
		for (Object row : userTemplates) {
			if (row instanceof UserTemplate) {
				userTemplatesList.add((UserTemplate)row);
			}
		}		
		return userTemplatesList;
	}
	
	@Override
	public int updatUserTemplate(UserTemplate userTemplate) throws Exception{
		
		// sessionFactory.getCurrentSession().saveOrUpdate(userTemplate);
		
		StringBuilder selectQuery = new StringBuilder();
		StringBuilder whereQuery = new StringBuilder();
		
		selectQuery.append("update UserTemplate as u set u.reportName =:reportName"
					+ ", u.reportDetail =:reportDetail");

		
			whereQuery.append(" where u.userTemplateKey.usertemplateId =:usertemplateId "
    				+ "and u.userTemplateKey.userDetail.userId =:userId ");
    	selectQuery.append(whereQuery);
		
		
		Query query = sessionFactory.getCurrentSession().createQuery(selectQuery.toString());
		query.setParameter("reportName", userTemplate.getReportName());
		query.setParameter("reportDetail",userTemplate.getReportDetail());

		query.setParameter("usertemplateId", userTemplate.getUserTemplateKey().getUsertemplateId());
		query.setParameter("userId", userTemplate.getUserTemplateKey().getUserDetail().getUserId());
		
		int count = query.executeUpdate();
		logger.debug(count+":Rows of UserTemplate updated successfully." );
		return count;
	}
	
	@Override
	public void deleteUserTemplate(UserTemplate userTemplate) throws Exception
	{
		
		UserTemplate template=getRecordById(UserTemplate.class, userTemplate.getUserTemplateKey());	
		delete(template);
	}
	
	public int getUserTemplateCountByUserId(int userId) throws Exception
	{
		  Map<String, Object> paramMap = new HashMap<>();
		  paramMap.put("userTemplateKey.userDetail.userId", userId);
		  Long count = getTotalCountWithFilter(UserTemplate.class , paramMap);
		return count.intValue();
	}
}
