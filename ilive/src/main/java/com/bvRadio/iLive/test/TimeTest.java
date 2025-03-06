package com.bvRadio.iLive.test;

import java.sql.Timestamp;
import java.util.TimeZone;

public class TimeTest {
public static void main(String[] args) {
//	Timestamp startTime=Timestamp.valueOf("2019-05-05 18:52:00");
//	Timestamp endTime=Timestamp.valueOf("2019-5-20 15:15:00");
//	Long useValue=(endTime.getTime()-startTime.getTime())/1000;
//	System.out.println(useValue);
	 long current = System.currentTimeMillis();
     long zero = current/(1000*3600*24)*(1000*3600*24) - TimeZone.getDefault().getRawOffset()+(1000*3600*24);
     System.out.println(zero);
}
}
