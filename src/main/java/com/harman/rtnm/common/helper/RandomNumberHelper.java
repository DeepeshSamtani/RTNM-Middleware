package com.harman.rtnm.common.helper;

import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

public class RandomNumberHelper {
	
	
	
	private static Random randomCount=new Random();

	public static int generateRandomNumber()
	{
		int userTempleateCount= randomCount.nextInt(99);
		return userTempleateCount;
	}
	
	
	public static String generateRandomAlphaNumericString()
	{
	    return RandomStringUtils.randomAlphanumeric(6);
	}
	
	public static String generateRandomAlphaNumericStringWithSevendigit()
	{
	    return RandomStringUtils.randomAlphanumeric(7);
	}
}
