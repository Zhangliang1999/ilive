package com.jwzt.common;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.jwzt.common.Logger;

/**
 * @author Administrator
 * 清理日志的线程
 */
public class LogThread extends Thread
{
	private long CIRCLE_TIME = 60*60*1000;//1天
	private static final String FILE_EXTENTION_LOG = ".log";
	private Calendar delTime = Calendar.getInstance();
	private Calendar nowTime = Calendar.getInstance();
	private Calendar today = null;
	private String cms_directory = "";
	private String soms_directory = "";
	private int storeTime = 0;
	
	public void setDir(String cms_directory,String soms_directory,int circle,int storeTime){
		this.cms_directory = cms_directory;
		this.soms_directory = soms_directory;
		this.CIRCLE_TIME = CIRCLE_TIME*circle;//时间单位是小时
		this.storeTime = storeTime;
	}
	
	//public int getIsFirst(){
	//	return isFirst;
	//}
	
	public void run()
	{
		while (true)
		{
			try
			{
				today = Calendar.getInstance();
				//nowTime.set(nowTime.DATE, 1);
				//int dayOfMonth = today.get(today.DAY_OF_MONTH);
				//if(dayOfMonth == 1 || isFirst == 0){
				//	isFirst = 1;
				//System.out.println("我进线程了....");
					if(this.deleteLogFile(cms_directory)){
						Logger.log(today.getTime().toString()+":CMS日志删除成功!", 2);
						//System.out.println(today.getTime().toString()+":CMS日志删除成功!");
					}else{
						Logger.log(today.getTime().toString()+",CMS日志删除失败或没有日志可删除!", 2);
						//System.out.println(today.getTime().toString()+":CMS日志删除失败或没有日志可删除!");
					}
					if(this.deleteLogFile(soms_directory)){
						Logger.log(today.getTime().toString()+":SOMS日志删除成功!", 2);
						//System.out.println(today.getTime().toString()+":SOMS日志删除成功!");
					}else{
						Logger.log(today.getTime().toString()+",SOMS日志删除失败或没有日志可删除!", 2);
						//System.out.println(today.getTime().toString()+":SOMS日志删除失败或没有日志可删除!");
					}
				//}
				this.sleep(CIRCLE_TIME);
			}
			catch (InterruptedException e)
			{
				Logger.log(e.toString(), 2);
			}
		}
	}
	
	private FileFilter fileFilter = new FileFilter() {   
        public boolean accept(File file) {   
            // directory and log file   
            return file.isFile() && file.getName().endsWith(FILE_EXTENTION_LOG);   
        }   
    };
	
	public boolean deleteLogFile(String delDirectory){
		boolean flag = false;
		File directory = new File(delDirectory);
		if(!directory.exists()){
			//System.out.println("路径错误，文件夹不存在。");
			return true;
		}
		File[] files = directory.listFiles(fileFilter);
        for (File file : files) {   
            if (file.isFile()) {
            	//System.out.println("fileName = "+file.getName());
            	//long lastModifyTime = file.lastModified();
            	if(isDel(file.getName())){
            		file.delete();
            		flag = true;
            	}
            }
        }
		return flag;
	}
	
	public boolean isDel(Long time){
		boolean del = false;
		delTime.setTimeInMillis(time);
		//if(delTime.before(nowTime)){
		if(!(delTime.get(delTime.YEAR)>nowTime.get(nowTime.YEAR))&& (delTime.get(delTime.MONTH)<nowTime.get(nowTime.MONTH))){	
			del = true;
		}
		return del;
	}
	
	public boolean isDel(String fileName){
		boolean del = false;
		try
		{
			nowTime = Calendar.getInstance();
			fileName = fileName.substring(0,fileName.indexOf("."));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Long timeMills = nowTime.getTimeInMillis();
			timeMills = timeMills - storeTime*24*60*60*1000;
			nowTime.setTimeInMillis(timeMills);
			nowTime.set(nowTime.HOUR, 0);
			nowTime.set(nowTime.MINUTE, 0);
			nowTime.set(nowTime.SECOND, 0);
			
			Date d2 = nowTime.getTime();
			//String now = sdf.format(d2);
			//System.out.println("fileName=========="+fileName);
			Date d1 = sdf.parse(fileName);
			//System.out.println("d2=========="+d2.toString());
			//System.out.println("d1=========="+d1.toString());
			if(d1.before(d2)){
				del = true;
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			Logger.log(e, 3);
			del = false;
		}
		//System.out.println("del ==="+del);
		return del;
	}
	public static void  main (String args[])
	{
		LogThread lt=new LogThread();
		lt.isDel("2010-7-4.log");
		lt.isDel("2010-7-5.log");
		lt.isDel("2010-7-6.log");
		lt.isDel("2010-7-7.log");
	}
}
