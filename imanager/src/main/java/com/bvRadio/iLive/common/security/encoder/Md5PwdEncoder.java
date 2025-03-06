package com.bvRadio.iLive.common.security.encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5密码加密
 */
public class Md5PwdEncoder implements PwdEncoder {
	private static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public String encodePassword(String rawPass) {
		return encodePassword(rawPass, defaultSalt);
	}

	private static String toHex(byte[] bytes) {
		StringBuffer str = new StringBuffer(32);
		for (byte b : bytes) {
			str.append(hexDigits[(b & 0xf0) >> 4]);
			str.append(hexDigits[(b & 0x0f)]);
		}
		return str.toString();
	}

	protected String encode(String rawPass) {
		return encode(rawPass, defaultSalt);
	}

	public String encodePassword(String rawPass, String salt) {
		return encode(encode(rawPass) + salt);
	}

	protected String encode(String rawPass, String salt) {
		String saltedPass = mergePasswordAndSalt(rawPass, salt, false);
		if (saltedPass == null) {
			saltedPass = "";
		}
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			md5.update(saltedPass.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toHex(md5.digest());
	}

	protected final MessageDigest getMessageDigest() {
		String algorithm = "MD5";
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("No such algorithm [" + algorithm + "]");
		}
	}

	protected String mergePasswordAndSalt(String password, Object salt, boolean strict) {
		if (password == null) {
			password = "";
		}
		if (strict && (salt != null)) {

		}
		if ((salt == null) || "".equals(salt)) {
			return password;
		} else {
			return password + salt.toString();
		}
	}

	public boolean isPasswordValid(String encPass, String rawPass) {
		return isPasswordValid(encPass, rawPass, defaultSalt);
	}

	/**
	 * @param encPass
	 *            加密之后的字符
	 * @param rawPass
	 *            明文传递字符串、
	 * @param salt
	 *            干扰码
	 * @return boolean
	 */
	public boolean isPasswordValid(String encPass, String rawPass, String salt) {
		if (encPass == null) {
			return false;
		}

		String pass2 = encodePassword(rawPass, salt);
		return encPass.equals(pass2);
	}

	/**
	 * 混淆码。防止破解。
	 */
	private String defaultSalt = "jwzt_HB";

	/**
	 * 获得混淆码
	 * 
	 * @return
	 */
	public String getDefaultSalt() {
		return defaultSalt;
	}

	/**
	 * 设置混淆码
	 * 
	 * @param defaultSalt
	 */
	public void setDefaultSalt(String defaultSalt) {
		this.defaultSalt = defaultSalt;
	}

	public static void main(String[] args) {
		// eea573f6cda9b2266a9d6ab2f2759405
		// 登陆
		String password = "12345678";
		String salt = "";
		Md5PwdEncoder encoder = new Md5PwdEncoder();
		password = encoder.encodePassword(password, salt);
		// password = encoder.encodePassword((password)+ salt);
		System.out.println(password);
		System.out.println(encoder.isPasswordValid(password, "12345678", ""));
	}
}
