package com.bvRadio.iLive.iLive.util;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;

public class SystemMd5Util {

	private static final String hashAlgorithmName = "MD5";


	public static String md5(String pass, String saltSource) {
		Object salt = new Md5Hash(saltSource);
		int hashIterations = 1024;
		Object result = new SimpleHash(hashAlgorithmName, pass, salt, hashIterations);
		String password = result.toString();
		return password;
	}
}
