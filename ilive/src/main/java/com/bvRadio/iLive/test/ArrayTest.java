package com.bvRadio.iLive.test;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

public class ArrayTest {
public static void main(String[] args) {

//Integer[] array = {10001,10004,10008,10010,10011,10012,10013,10029,10036,10038,10047,10048,10049,10050,10051,10052,10054,10056,10066,10067,10073};
//int index = Arrays.binarySearch(array,10047);
//System.out.println("index:" + index); //--- index:1
//index = Arrays.binarySearch(array,10030);
//System.out.println("index:" + index); //--- index:-1
//index = Arrays.binarySearch(array,10089);
//System.out.println("index:" + index);
	
//	Timestamp creatTime=new Timestamp(System.currentTimeMillis()-(1000*60*60*24));
//	Timestamp currTime=new Timestamp(System.currentTimeMillis());
//	Long validTime=(long) (1000*60*60*24*7);
//	Long diff=validTime-(currTime.getTime()-creatTime.getTime());
//	long days = diff / (1000 * 60 * 60 * 24);
//	long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);  
//    long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);  
//    System.out.println(""+days+"天"+hours+"小时"+minutes+"分");
//    String vail="0000-00-"+days+" "+hours+":"+minutes+":00";
//    Timestamp time = Timestamp.valueOf(vail);
//	System.out.println(time);
	
//	          /*测试合并两个类型相同的list*/
//	          List<String> list1 = new ArrayList<String>();
//	          List<String> list2 = new ArrayList<String>();
//	          //给list1赋值
//	          list1.add("测");
//	          list1.add("试");
//	          list1.add("一");
//	          list1.add("下");
//	          //给list2赋值
//	          list2.add("合");
//	          list2.add("并");
//	          list2.add("列");
//	          list2.add("表");
//	          //将list1.list2合并
//	          list1.addAll(list2);
//	          //循环输出list1 看看结果
//	          for (String s : list1) {
//	              System.out.print(s);
//	          }
//	 String token="8a3f5f6e14daffe988df14d8b5306d03";
//	 String access="8a3f5f6e14daffe988df14d8b5306d03";
//	 if(token.equals(access)){
//		 System.out.println("一致");
//	 }
	 
//	 String md5Hex = DigestUtils.md5Hex("2019-04-22 09:26:00"+"&"+"40b3257f-95ba-4374-8a7f-4c931196a370"+"&"+"TV189");
//	System.out.println(md5Hex);
	a(1,2,3,4,5,6,7,8,9,10);
}
public static void a(int num1,int num2,int num3,int num4,int num5,int num6,int num7,int num8,int num9,int num10){
	int[] arr=new int[]{num1,num2,num3,num4,num5,num6,num7,num8,num9,num10};
	int a;
	for(int i=0;i<10;i++){
		for(int j=0;j<9;j++){
			if(arr[j]>arr[j+1]){
				a=arr[j+1];
				arr[j+1]=arr[j];
				arr[j]=a;
			}
		}
	}
	for(int i=0;i<arr.length;i++){
		System.out.println(arr[i]);
	}
}
}
