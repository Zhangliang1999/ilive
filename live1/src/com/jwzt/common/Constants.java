package com.jwzt.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Constants {

	public static Map reviewHistoryMap = new HashMap();
	
	public static void main(String args[]){
		Map map = new HashMap();
		map.put("4", "123456");
		map.put("1", "234567");
		map.put("2", "345678");
		map.put("3", "456789");
		
		Set set  = map.keySet();
		Iterator it  = set.iterator();
		while(it.hasNext()){
			String temp  =(String)it.next();
			System.out.println(temp);
		}
	}
}
