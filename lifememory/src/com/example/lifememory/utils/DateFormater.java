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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd�� HHʱmm��ss��");
		String dateStr = sdf.format(date);
		return dateStr;
	}
	//��ȡ������
	public String getYMD() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd��");
		String dateStr = sdf.format(date); 
		return dateStr;
	}
	//�������
	public String getYM() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��");
		String dateStr = sdf.format(date); 
		return dateStr;
	}
}
