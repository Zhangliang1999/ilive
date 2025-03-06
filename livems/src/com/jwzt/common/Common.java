package com.jwzt.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.StringCharacterIterator;
import java.util.Collection;
import java.util.Map;



public class Common {

	public static int strlen(String text) {
		return strlen(text, "UTF-8");
	}

	public static int strlen(String text, String charsetName) {
		if (text == null || text.length() == 0) {
			return 0;
		}
		int length = 0;
		try {
			length = text.getBytes(charsetName).length;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return length;
	}
	
	public static String addSlashes(String text) {
		if (text == null || text.equals("")) {
			return "";
		}
		StringBuffer sb = new StringBuffer(text.length() * 2);
		StringCharacterIterator iterator = new StringCharacterIterator(text);
		char character = iterator.current();
		while (character != StringCharacterIterator.DONE) {
			switch (character) {
			case '\'':
			case '"':
			case '\\':
				sb.append("\\");
			default:
				sb.append(character);
				break;
			}
			character = iterator.next();
		}
		return sb.toString();
	}

	public static String urlEncode(String s) {
		return urlEncode(s, "UTF-8");
	}

	public static String urlEncode(String s, String enc) {
		if (!empty(s)) {
			try {
				return URLEncoder.encode(s, enc);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return s;
	}

	public static String urlDecode(String s) {
		return urlDecode(s, "UTF-8");
	}

	public static String urlDecode(String s, String enc) {
		if (!empty(s)) {
			try {
				return URLDecoder.decode(s, enc);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return s;
	}
	@SuppressWarnings("unchecked")
	public static boolean empty(Object obj) {
		if (obj == null) {
			return true;
		} else if (obj instanceof String && (obj.equals("") || obj.equals("0"))) {
			return true;
		} else if (obj instanceof Number && ((Number) obj).doubleValue() == 0) {
			return true;
		} else if (obj instanceof Boolean && !((Boolean) obj)) {
			return true;
		} else if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
			return true;
		} else if (obj instanceof Map && ((Map) obj).isEmpty()) {
			return true;
		} else if (obj instanceof Object[] && ((Object[]) obj).length == 0) {
			return true;
		}
		return false;
	}
	public static String authCode(String string, String operation, String key,
			int expiry) {
		String oldstring = string;
		String oldoperation = operation;
		String oldkey = key;
		int oldexpiry = expiry;
		long currentTime = System.currentTimeMillis();
		int time = (int) (currentTime / 1000);
		try {
			int ckey_length = 4;
			key = md5((key == null ? "mobile"
					: key));
			String keya = md5(key.substring(0, 16));
			String keyb = md5(key.substring(16, 32));
			String keyc = ckey_length > 0 ? ("DECODE".equals(operation) ? string
					.substring(0, ckey_length)
					: md5(String.valueOf(currentTime)).substring(
							32 - ckey_length))
					: "";
			String cryptkey = keya + md5(keya + keyc);
			int key_length = cryptkey.length();
			string = "DECODE".equals(operation) ? Base64.decode(string
					.substring(ckey_length), "UTF-8")
					: (expiry > 0 ? expiry + time : "0000000000")
							+ md5(string + keyb).substring(0, 16) + string;
			int string_length = string.length();
			StringBuffer result = new StringBuffer(string_length);
			int range = 128;
			int[] rndkey = new int[range];
			for (int i = 0; i < range; i++) {
				rndkey[i] = cryptkey.charAt(i % key_length);
			}
			int tmp;
			int[] box = new int[range];
			for (int i = 0; i < range; i++) {
				box[i] = i;
			}
			for (int i = 0, j = 0; i < range; i++) {
				j = (j + box[i] + rndkey[i]) % range;
				tmp = box[i];
				box[i] = box[j];
				box[j] = tmp;
			}
			for (int a = 0, i = 0, j = 0; i < string_length; i++) {
				a = (a + 1) % range;
				j = (j + box[a]) % range;
				tmp = box[a];
				box[a] = box[j];
				box[j] = tmp;
				result
						.append((char) ((int) string.charAt(i) ^ (box[(box[a] + box[j])
								% range])));
			}
			if ("DECODE".equals(operation)) {
				int resulttime = Common.intval(result.substring(0, 10));
				if ((resulttime == 0 || resulttime - time > 0)
						&& result.substring(10, 26).equals(
								md5(result.substring(26) + keyb).substring(0,
										16))) {
					return result.substring(26);
				} else {
					return "";
				}
			} else {
				String auth = keyc
						+ (Base64.encode(result.toString(),"UTF-8"))
								.replaceAll("=", "");
				if (canDecode(auth)) {
					return auth;
				} else {
					return authCode(oldstring, oldoperation, oldkey, oldexpiry);
				}
			}
		} catch (Exception e) {
			return "";
		}
	}
	
	public static boolean canDecode(String auth) {
		boolean bet = false;
		auth = Common.authCode(auth, "DECODE", null, 0);

		String[] values = auth.split("\t");
		if (values.length > 1) {
			bet = true;
		}
		return bet;
	}
	public static String md5(String arg0) {
		return Md5Util.encode(arg0);
	}
	
	public static int intval(String s) {
		return intval(s, 10);
	}

	public static int intval(String s, int radix) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		if (radix == 0) {
			radix = 10;
		} else if (radix < Character.MIN_RADIX) {
			return 0;
		} else if (radix > Character.MAX_RADIX) {
			return 0;
		}
		int result = 0;
		int i = 0, max = s.length();
		int limit;
		int multmin;
		int digit;
		boolean negative = false;
		if (s.charAt(0) == '-') {
			negative = true;
			limit = Integer.MIN_VALUE;
			i++;
		} else {
			limit = -Integer.MAX_VALUE;
		}
		if (i < max) {
			digit = Character.digit(s.charAt(i++), radix);
			if (digit < 0) {
				return 0;
			} else {
				result = -digit;
			}
		}
		multmin = limit / radix;
		while (i < max) {
			digit = Character.digit(s.charAt(i++), radix);
			if (digit < 0) {
				break;
			}
			if (result < multmin) {
				result = limit;
				break;
			}
			result *= radix;
			if (result < limit + digit) {
				result = limit;
				break;
			}
			result -= digit;
		}
		if (negative) {
			if (i > 1) {
				return result;
			} else {
				return 0;
			}
		} else {
			return -result;
		}
	}
}