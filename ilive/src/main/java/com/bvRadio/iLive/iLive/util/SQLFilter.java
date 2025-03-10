package com.bvRadio.iLive.iLive.util;

import org.apache.commons.lang.StringUtils;

/**
 * SQL过滤
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-01 16:16
 */
public class SQLFilter {

    /**
     * SQL注入过滤
     * @param str  待验证的字符串
     */
    public static String sqlInject(String str){
        if(StringUtils.isBlank(str)){
            return "";
        }
        //去掉'|"|;|\字符
        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, "\"", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, "\\", "");

        //转换成小写
        str = str.toLowerCase();

        //非法字符
//        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alert", "create", "drop"};
//
//        //判断是否包含非法字符
//        for(String keyword : keywords){
//            if(str.indexOf(keyword) != -1){
//                // throw new RuntimeException("包含非法字符");
//            	 // str = StringUtils.replace(str, "ｍａｓｔｅｒ","");
//            }
//        }

        return str;
    }
}
