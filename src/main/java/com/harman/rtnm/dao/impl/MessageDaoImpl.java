package com.harman.rtnm.dao.impl;

import org.springframework.stereotype.Repository;

import com.harman.rtnm.dao.AbstractDAO;
import com.harman.rtnm.dao.MessageDao;
import com.harman.rtnm.model.Message;

@Repository
public class MessageDaoImpl extends AbstractDAO<Message> implements MessageDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4364916123038960299L;

	@Override
	public Message getMessage(int code) {
		return getRecordById(Message.class, code);
	}
}