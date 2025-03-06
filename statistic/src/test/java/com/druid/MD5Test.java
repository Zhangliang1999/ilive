package com.druid;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;

public class MD5Test {
	public static void main(String[] args) {
//		String pw=MD5Utils.getSaltMD5("huxin");
//		String pw1=MD5Utils.getStrMD5("zl445321");
//		System.out.println("pw:"+pw);
//		System.out.println("pw1:"+pw1);
		 String saltSource = "test";    
	        String hashAlgorithmName = "MD5";
	        String credentials = "123456";
	        Object salt = new Md5Hash(saltSource);
	        int hashIterations = 1024;            
	        Object result = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
	        System.out.println(result);
	}
}
