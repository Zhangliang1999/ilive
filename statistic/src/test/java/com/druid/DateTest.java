package com.druid;

import java.util.Calendar;
import java.util.Date;

public class DateTest {
public static void main(String[] args) {
	 Calendar calendar=Calendar.getInstance();
	 calendar.set(2019, 5, 12);  //年月日  也可以具体到时分秒如calendar.set(2015, 10, 12,11,32,52); 
     Date date=calendar.getTime();//date就是你需要的时间
    // System.out.println(date);
     Date date1=new Date();
     Integer num=differentDays(date,date1);
    // System.out.println(num);
     for(int i=0;i<=num;i++){
    	  Calendar calendar1 = Calendar.getInstance();
    	  calendar1.setTime(date);
    	  calendar1.add(calendar1.DAY_OF_YEAR,i);
    	  Date date2=calendar1.getTime();
    	  Calendar calendar2 = Calendar.getInstance();
  		calendar2.setTime(date2);
    	  calendar2.set(Calendar.HOUR_OF_DAY, 0);
  		calendar2.set(Calendar.MINUTE, 0);
  		calendar2.set(Calendar.SECOND, 0);
  		calendar2.set(Calendar.MILLISECOND, 0);
  		Date viewTimeHourStartTime = calendar2.getTime();
  		calendar2.set(Calendar.HOUR_OF_DAY, 0);
  		calendar2.set(Calendar.MINUTE, 59);
  		calendar2.set(Calendar.SECOND, 59);
  		calendar2.set(Calendar.MILLISECOND, 999);
  		Date viewTimeHourEndTime = calendar2.getTime();
  		System.out.println(viewTimeHourStartTime+"至"+viewTimeHourEndTime);
     }
}
/**
 * date2比date1多的天数
 * @param date1    
 * @param date2
 * @return    
 */
public static int differentDays(Date date1,Date date2)
{
    Calendar cal1 = Calendar.getInstance();
    cal1.setTime(date1);
    
    Calendar cal2 = Calendar.getInstance();
    cal2.setTime(date2);
   int day1= cal1.get(Calendar.DAY_OF_YEAR);
    int day2 = cal2.get(Calendar.DAY_OF_YEAR);
    
    int year1 = cal1.get(Calendar.YEAR);
    int year2 = cal2.get(Calendar.YEAR);
    if(year1 != year2)   //同一年
    {
        int timeDistance = 0 ;
        for(int i = year1 ; i < year2 ; i ++)
        {
            if(i%4==0 && i%100!=0 || i%400==0)    //闰年            
            {
                timeDistance += 366;
            }
            else    //不是闰年
            {
                timeDistance += 365;
            }
        }
        
        return timeDistance + (day2-day1) ;
    }
    else    //不同年
    {
        System.out.println("判断day2 - day1 : " + (day2-day1));
        return day2-day1;
    }
}
}
