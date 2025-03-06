package com.druid;

import java.util.Calendar;
import java.util.Date;

import com.alibaba.druid.filter.config.ConfigTools;

/**
 * @author ysf
 */
public class PasswordEncoder {
	public static void main(String[] args) throws Exception {
		String password = "Jwzt@tysx2018";
		String[] arr = ConfigTools.genKeyPair(512);
		System.out.println("privateKey:" + arr[0]);
		System.out.println("publicKey:" + arr[1]);
		System.out.println("password:" + ConfigTools.encrypt(arr[0], password));
		
		Calendar calendar = Calendar.getInstance();  
        calendar.set(Calendar.HOUR_OF_DAY, 3); //凌晨1点  
        calendar.set(Calendar.MINUTE, 0);  
        calendar.set(Calendar.SECOND, 0);  
        Date date=calendar.getTime(); //第一次执行定时任务的时间  
        //如果第一次执行定时任务的时间 小于当前的时间  
        //此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。  
        if (date.before(new Date())) {  
            Calendar startDT = Calendar.getInstance();  
            startDT.setTime(date);  
            startDT.add(Calendar.DAY_OF_MONTH, 3);  
            date =  startDT.getTime();  
        }  
        System.out.println(date);
	}
}
