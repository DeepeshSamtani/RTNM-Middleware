package com.harman.rtnm.dao;

import com.harman.rtnm.model.Message;

public interface MessageDao {
	Message getMessage(int code);
}