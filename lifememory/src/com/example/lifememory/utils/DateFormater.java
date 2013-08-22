package com.example.lifememory.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormater {
	public static DateFormater instance = new DateFormater();

	public DateFormater() {
	};

	public static DateFormater getInstatnce() {
		return instance;
	}

	public String getCurrentDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		String dateStr = sdf.format(date);
		return dateStr;
	}
	//获取年月日
	public String getYMD() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		String dateStr = sdf.format(date); 
		return dateStr;
	}
	//获得年月
	public String getYM() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
		String dateStr = sdf.format(date); 
		return dateStr;
	}
}
