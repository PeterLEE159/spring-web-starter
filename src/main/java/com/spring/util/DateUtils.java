package com.spring.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static SimpleDateFormat datesdf = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat timestampsdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	
	public static Date dateParse(String date) {
		try {
			return datesdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String dateFormat(Date date) {
		return datesdf.format(date);
	}
	
	public static Date timestampParse(String date) {
		try {
			return timestampsdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String timestampFormat(Date date) {
		return timestampsdf.format(date);
	}
}
