/**
 * 
 */
package com.harman.rtnm.dao;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;



@Repository
public class AbstractDAO<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6708077422646467948L;

	@Autowired
	SessionFactory sessionFactory;

	private Session getSession() {

		return sessionFactory.getCurrentSession();

	}

	/**
	 * 
	 * @param baseentity
	 */
	public void save(T baseentity) {
		getSession().save(baseentity);
	}

	/**
	 * 
	 * @param baseentity
	 */
	public void saveOrUpdate(T baseentity) {
		getSession().saveOrUpdate(baseentity);
	}

	/**
	 * 
	 * @param baseentity
	 */
	public void update(T baseentity) {
		getSession().update(baseentity);
	}

	/**
	 * 
	 * @param baseentity
	 */
	public void delete(T baseentity) {
		getSession().delete(baseentity);
	}

	/**
	 * 
	 * @param baseentity
	 */
	public void deleteList(List<T> baseentityList) {
		if (!baseentityList.isEmpty()) {
			baseentityList.forEach((baseentity) -> {
				getSession().delete(baseentity);
			});
		}
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public List<?> loadClass(Class clazz) {
		Query qry = getSession().createQuery("from " + clazz.getName());
		return qry.list();
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public List<T> executeQuery(String sqlquery) {
		Query query = getSession().createQuery(sqlquery);
		return (List<T>) query.list();
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public T executeQueryForUniqueRecord(String sqlquery) {
		Query query = getSession().createQuery(sqlquery);
		return (T) query.uniqueResult();
	}

	/**
	 * 
	 * @param queryString
	 * @return
	 */
	public List<?> execcuteSQLQuery(String queryString) {
		Query query = getSession().createSQLQuery(queryString);
		return query.list();
	}

	/**
	 * This method returns database record based on provided unique id.
	 * 
	 */
	public T getRecordById(Class clazz, Object id) {
		Serializable ids = (Serializable) id;
		T t = (T) getSession().get(clazz, ids);
		return t;
	}

	/**
	 * This method executes mysql smart alarm procedures based on configured
	 * policy.
	 */
	public int executeProcedure(Class clazz, String queryString) {
		Query query = getSession().createSQLQuery(queryString).addEntity(clazz);
		return query.executeUpdate();
	}

	/**
	 * Unused method : This select will fetch record according to page size and
	 * page number.
	 * 
	 * @param sqlquery
	 * @param currentpage
	 * @param pagesize
	 * @return
	 */
	public List<?> selectWithPagination(String sqlquery, int currentpage, int pagesize) {
		Query query = getSession().createQuery(sqlquery);
		query.setFirstResult((currentpage - 1) * pagesize);
		query.setMaxResults(pagesize);
		return query.list();
	}

	/**
	 * Unused method : This Will return total record count for class.
	 */
	public Long getTotalRecordCount(Class claz) {
		Criteria criteriaCount = getSession().createCriteria(claz);
		criteriaCount.setProjection(Projections.rowCount());
		return (Long) criteriaCount.uniqueResult();
	}

	/**
	 * Unused method :
	 */
	public long sum(String hqlQuery) {
		String sum = (String) getSession().createQuery(hqlQuery).uniqueResult();
		long sumValue = Long.valueOf(sum);
		return sumValue;
	}

	/**
	 * Unused method :
	 */
	public List<?> getResultWithPagenation(Class claz, int pageNumber, int pageSize) {
		Query query = getSession().createQuery("from " + claz.getName());
		query.setFirstResult((pageNumber * pageSize) - pageSize);
		query.setMaxResults(pageSize);
		return query.list();
	}

	/**
	 * Unused method :
	 */
	public List<?> getResultForCustomiseRequest(Class claz, int pageNumber, int pageSize, long fromField, long toFeild,
			List<String> colomns, Map<String, Object> filters, String sortColomnName, String sortType,
			String betweenField) {
		Order order = null;
		if (sortType.equalsIgnoreCase("desc")) {
			order = Order.desc(sortColomnName);
		} else if (sortType.equalsIgnoreCase("asc")) {
			order = Order.asc(sortColomnName);
		}
		ProjectionList projectionList = Projections.projectionList();
		for (String columnName : colomns) {
			projectionList.add(Projections.property(columnName), columnName);
		}
		Criteria cr = getSession().createCriteria(claz).setProjection(projectionList).add(Restrictions.allEq(filters))
				.add(Restrictions.between(betweenField, fromField, toFeild))
				.setFirstResult((pageNumber * pageSize) - pageSize).setMaxResults(pageSize).addOrder(order)
				.setResultTransformer(Transformers.aliasToBean(claz));
		return cr.list();
	}

	/**
	 * Unused method :
	 */
	public Long getTotalCountWithFilter(Class claz,  Map<String, Object> filters) {
		Criteria criteriaCount = getSession().createCriteria(claz).add(Restrictions.allEq(filters));
		criteriaCount.setProjection(Projections.rowCount());
		return (Long) criteriaCount.uniqueResult();
	}

	/**
	 * Unused method :
	 */
	public List<?> selectPaginationWithTransformers(String sqlquery, int currentpage, int pagesize, Class claz) {
		Query query = getSession().createQuery(sqlquery);
		query.setFirstResult((currentpage - 1) * pagesize);
		query.setMaxResults(pagesize);
		query.setResultTransformer(Transformers.aliasToBean(claz));
		return query.list();
	}

	/**
	 * Unused method :
	 */
	public List<?> executeQueryTransformers(String sqlquery, Class claz) {
		Query query = getSession().createQuery(sqlquery);
		query.setResultTransformer(Transformers.aliasToBean(claz));
		return query.list();
	}

	public void updateList(String sqlquery, List<?> list) {
		Query query = getSession().createQuery(sqlquery);
		query.setParameterList("list", list);
		query.executeUpdate();
	}

	public void updateSet(String sqlquery, Set<?> set) {
		Query query = getSession().createQuery(sqlquery);
		query.setParameterList("set", set);
		query.executeUpdate();
	}

	public int executeUpdate(String sqlquery) {
		Query query = getSession().createQuery(sqlquery);
		return query.executeUpdate();
	}

	public List<?> executeQueryWithList(String sqlquery, List<?> list) {
		Query query = getSession().createQuery(sqlquery);
		query.setParameterList("list", list);
		return query.list();
	}

	public List<T> selectWithInClause(Map<String, Set> values, Class claz) throws Exception {
		Criteria cr = getSession().createCriteria(claz);
		values.forEach((k, v) -> {
			cr.add(Restrictions.in(k, v));
		});
		return cr.list();
	}

	public void updateList(List<T> baseentityList) {
		if (!baseentityList.isEmpty()) {
			baseentityList.forEach((baseentity) -> {
				getSession().update(baseentity);
			});
		}
	}
	
	public long count(String sqlQuery) {
		BigInteger countValue= (BigInteger) getSession().createSQLQuery(sqlQuery).uniqueResult();
		return countValue.longValue();
	}

}