package com.bvRadio.iLive.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
//判断 是否为靓号

public class Regu {

        public boolean isLiang(String mc) {
        	boolean flag=false;
                //匹配6位顺增
        	/*	String pattern = "(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){5}\\d";
                Pattern pa = Pattern.compile(pattern);
                Matcher ma = pa.matcher(mc);
                // System.out.println("6位顺增 ：" + ma.matches());
                // System.out.println("*******分割线*******");
                if(ma.matches()){
                	flag=true;
                }
        		
                //匹配6位顺降
                pattern = "(?:9(?=8)|8(?=7)|7(?=6)|6(?=5)|5(?=4)|4(?=3)|3(?=2)|2(?=1)|1(?=0)){5}\\d";
                pa = Pattern.compile(pattern);
                mc = "654321";
                ma = pa.matcher(mc);
                // System.out.println("6位顺降 ：" + ma.matches());
                // System.out.println("*******分割线*******");
                if(ma.matches()){
                	flag=true;
                }*/
               
                //匹配6位顺增或顺降
        	    String pattern = "(?:(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){5}|(?:9(?=8)|8(?=7)|7(?=6)|6(?=5)|5(?=4)|4(?=3)|3(?=2)|2(?=1)|1(?=0)){5})\\d";
                Pattern pa = Pattern.compile(pattern);
                Matcher ma = pa.matcher(mc);
               /* // System.out.println("6位顺增或顺降 ：" + ma.matches());
                // System.out.println("*******分割线*******");*/
                if(ma.matches()){
                	flag=true;
                }
               
        /*        //匹配4-9位连续的数字
                pattern = "(?:(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){3,}|(?:9(?=8)|8(?=7)|7(?=6)|6(?=5)|5(?=4)|4(?=3)|3(?=2)|2(?=1)|1(?=0)){3,})\\d";
                pa = Pattern.compile(pattern);
                mc = "123456789";
                ma = pa.matcher(mc);
                // System.out.println("4-9位连续的数字 ：" + ma.matches());
                // System.out.println("*******分割线*******");
               */
                //匹配3位以上的重复数字
               /* (\d)\1{2}*/
                pattern = "^.*(.)\\1{2}.*$";
               /* pattern = "([\\d])\\1{2,}";*/
                pa = Pattern.compile(pattern);
                ma = pa.matcher(mc);
               /* // System.out.println("3位以上的重复数字 ：" + ma.matches());
                // System.out.println("*******分割线*******");*/
                if(ma.matches()){
                	flag=true;
                }
               
                //匹配2233类型
                pattern = "([\\d])\\1{1,}([\\d])\\2{1,}";
                pa = Pattern.compile(pattern);
                ma = pa.matcher(mc);
               /* // System.out.println("2233类型 ：" + ma.matches());
                // System.out.println("*******分割线*******");*/
                if(ma.matches()){
                	flag=true;
                }
                
                if(flag){
                	return true;
                }else{
                	return false;
                }
                
        }
        public static void main(String[] args) {
			String str="222233";
			Regu rt=new Regu();
			boolean b = rt.isLiang(str);
			// System.out.println(b);
		}
}