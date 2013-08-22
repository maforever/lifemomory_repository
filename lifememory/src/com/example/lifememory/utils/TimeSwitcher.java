package com.example.lifememory.utils;

public class TimeSwitcher {

	public static TimeSwitcher instance = null;
	public TimeSwitcher() {};
	public static TimeSwitcher getInstance() {
		if(instance == null) {
			instance = new TimeSwitcher();
		}
		return instance;
	}
	
	public  String formatLongToTimeStr(Long l) {        
		   int hour = 0;        
		   int minute = 0;       
		   int second = 0;        
		   second = l.intValue() / 1000;        
		   if (second > 60) {            
		    minute = second / 60;            
		    second = second % 60;        
		   }       

		   if (minute > 60) {            
		    hour = minute / 60;            
		    minute = minute % 60;        
		   }        
		   String strtime = hour+"Ð¡Ê±"+minute+"·ÖÖÓ"+second+"Ãë";
		   return strtime;   

		}
	
	public String formatLongToTimeStrEn(Long l) {
		   int hour = 0;        
		   int minute = 0;       
		   int second = 0;        
		   second = l.intValue() / 1000;        
		   if (second > 60) {            
		    minute = second / 60;            
		    second = second % 60;        
		   }       

		   if (minute > 60) {            
		    hour = minute / 60;            
		    minute = minute % 60;        
		   }        
		   
		   String secondStr = second / 10 > 0 ? String.valueOf(second) : "0" + second;
		   String minuteStr = second / 10 > 0 ? String.valueOf(minute) : "0" + minute;
		   String hourStr = second / 10 > 0 ? String.valueOf(hour) : "0" + hour;
		   
		   String strtime = hourStr + ":" + minuteStr + ":" + secondStr;
		   return strtime;  
	}

}







