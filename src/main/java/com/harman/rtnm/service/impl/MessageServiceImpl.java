package com.harman.rtnm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.harman.rtnm.dao.MessageDao;
import com.harman.rtnm.model.Message;
import com.harman.rtnm.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	MessageDao messageDao;

	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Message getMessage(int code) {
		return messageDao.getMessage(code);
	}
}