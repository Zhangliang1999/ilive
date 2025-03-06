package com.bvRadio.iLive.iLive.util;

import java.io.UnsupportedEncodingException;

import com.alipay.api.main;

public class FullCharConverter {

	/**
	 * 半角转全角
	 * 
	 * @Methods Name half2Fullchange
	 * @Create In 2012-8-24 By v-jiangwei
	 * @param QJstr
	 * @return String
	 */
	public static final String half2Fullchange(String input) {
		 char c[] = input.toCharArray();
         for (int i = 0; i < c.length; i++) {
           if (c[i] == ' ') {
             c[i] = '\u3000';
           } else if (c[i] < '\177') {
             c[i] = (char) (c[i] + 65248);
           }
         }
         return new String(c);
	}
	
	public static void main(String[] args) {
		String name = "<div> * from ";
		String half2Fullchange = half2Fullchange(name);
		// System.out.println(half2Fullchange);
		
	}

}
