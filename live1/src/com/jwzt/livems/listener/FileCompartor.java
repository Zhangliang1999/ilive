package com.jwzt.livems.listener;

import java.util.Comparator;

public class FileCompartor  implements Comparator{  
	  
	 public int compare(Object arg0, Object arg1) {  
	  FileJson file0=(FileJson)arg0;  
	  FileJson file1=(FileJson)arg1;  
	  //首先比较年龄，如果年龄相同，则比较名字  
	  int flag=file1.getBandWidth().compareTo(file0.getBandWidth());  
	  if(flag==0){  
	   return file1.getBandWidth().compareTo(file0.getBandWidth());  
	  }else{  
	   return flag;  
	  }    
	 }  
	   
	}  
