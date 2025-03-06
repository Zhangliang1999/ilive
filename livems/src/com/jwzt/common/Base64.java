package com.jwzt.common;

/*
 * Base64.java Author: Ma Bingyao <andot@ujn.edu.cn> Copyright: CoolCode.CN
 * Version: 1.5 LastModified: 2006-08-09 This library is free. You can
 * redistribute it and/or modify it. http://www.coolcode.cn/?p=203
 */

import java.io.*;

public class Base64
{
	private static char[] base64EncodeChars = new char[]
	{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a',
			'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
			'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0',
			'1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };

	private static byte[] base64DecodeChars = new byte[]
	{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55,
			56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4,
			5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22,
			23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33,
			34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
			51, -1, -1, -1, -1, -1 };

	private Base64()
	{
	}

	public static String encode(byte[] data)
	{
		StringBuffer sb = new StringBuffer();
		int len = data.length;
		int i = 0;
		int b1, b2, b3;

		while (i < len)
		{
			b1 = data[i++] & 0xff;
			if (i == len)
			{
				sb.append(base64EncodeChars[b1 >>> 2]);
				sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
				sb.append("==");
				break;
			}
			b2 = data[i++] & 0xff;
			if (i == len)
			{
				sb.append(base64EncodeChars[b1 >>> 2]);
				sb.append(base64EncodeChars[((b1 & 0x03) << 4)
						| ((b2 & 0xf0) >>> 4)]);
				sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
				sb.append("=");
				break;
			}
			b3 = data[i++] & 0xff;
			sb.append(base64EncodeChars[b1 >>> 2]);
			sb.append(base64EncodeChars[((b1 & 0x03) << 4)
					| ((b2 & 0xf0) >>> 4)]);
			sb.append(base64EncodeChars[((b2 & 0x0f) << 2)
					| ((b3 & 0xc0) >>> 6)]);
			sb.append(base64EncodeChars[b3 & 0x3f]);
		}
		return sb.toString();
	}

	public static byte[] decode(String str)
	{
		byte[] data = str.getBytes();
		int len = data.length;
		ByteArrayOutputStream buf = new ByteArrayOutputStream(len);
		int i = 0;
		int b1, b2, b3, b4;

		while (i < len)
		{

			/* b1 */
			do
			{
				b1 = base64DecodeChars[data[i++]];
			}
			while (i < len && b1 == -1);
			if (b1 == -1)
			{
				break;
			}

			/* b2 */
			do
			{
				b2 = base64DecodeChars[data[i++]];
			}
			while (i < len && b2 == -1);
			if (b2 == -1)
			{
				break;
			}
			buf.write((int) ((b1 << 2) | ((b2 & 0x30) >>> 4)));

			/* b3 */
			do
			{
				b3 = data[i++];
				if (b3 == 61)
				{
					return buf.toByteArray();
				}
				b3 = base64DecodeChars[b3];
			}
			while (i < len && b3 == -1);
			if (b3 == -1)
			{
				break;
			}
			buf.write((int) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));

			/* b4 */
			do
			{
				b4 = data[i++];
				if (b4 == 61)
				{
					return buf.toByteArray();
				}
				b4 = base64DecodeChars[b4];
			}
			while (i < len && b4 == -1);
			if (b4 == -1)
			{
				break;
			}
			buf.write((int) (((b3 & 0x03) << 6) | b4));
		}
		return buf.toByteArray();
	}
	
	public static final String encode(String str, String charsetName) {
		return encode(str, charsetName, 0);
	}
	public static final String encode(String str, String charsetName, int width) {
		byte[] data = null;
		try {
			data = str.getBytes(charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		int length = data.length;
		int size = (int) Math.ceil(length * 1.36);
		int splitsize = width > 0 ? size / width : 0;
		StringBuffer sb = new StringBuffer(size + splitsize);
		int r = length % 3;
		int len = length - r;
		int i = 0;
		int c;
		while (i < len) {
			c = (0x000000ff & data[i++]) << 16 | (0x000000ff & data[i++]) << 8 | (0x000000ff & data[i++]);
			sb.append(base64EncodeChars[c >> 18]);
			sb.append(base64EncodeChars[c >> 12 & 0x3f]);
			sb.append(base64EncodeChars[c >> 6 & 0x3f]);
			sb.append(base64EncodeChars[c & 0x3f]);
		}
		if (r == 1) {
			c = 0x000000ff & data[i++];
			sb.append(base64EncodeChars[c >> 2]);
			sb.append(base64EncodeChars[(c & 0x03) << 4]);
			sb.append("==");
		} else if (r == 2) {
			c = (0x000000ff & data[i++]) << 8 | (0x000000ff & data[i++]);
			sb.append(base64EncodeChars[c >> 10]);
			sb.append(base64EncodeChars[c >> 4 & 0x3f]);
			sb.append(base64EncodeChars[(c & 0x0f) << 2]);
			sb.append("=");
		}
		if (splitsize > 0) {
			char split = '\n';
			for (i = width; i < sb.length(); i++) {
				sb.insert(i, split);
				i += width;
			}
		}
		return sb.toString();
	}
	
	public static final String decode(String str, String charsetName) {
		byte[] data = null;
		try {
			data = str.getBytes(charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		int len = data.length;
		ByteArrayOutputStream buf = new ByteArrayOutputStream((int) (len * 0.67));
		int i = 0;
		int b1, b2, b3, b4;
		while (i < len) {
			do {
				if (i >= len) {
					b1 = -1;
					break;
				}
				b1 = base64DecodeChars[data[i++]];
			} while (i < len && b1 == -1);
			if (b1 == -1) {
				break;
			}
			do {
				if (i >= len) {
					b2 = -1;
					break;
				}
				b2 = base64DecodeChars[data[i++]];
			} while (i < len && b2 == -1);
			if (b2 == -1) {
				break;
			}
			buf.write((int) ((b1 << 2) | ((b2 & 0x30) >>> 4)));
			do {
				if (i >= len) {
					b3 = -1;
					break;
				}
				b3 = data[i++];
				if (b3 == 61) {
					b3 = -1;
					break;
				}
				b3 = base64DecodeChars[b3];
			} while (i < len && b3 == -1);
			if (b3 == -1) {
				break;
			}
			buf.write((int) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));
			do {
				if (i >= len) {
					b4 = -1;
					break;
				}
				b4 = data[i++];
				if (b4 == 61) {
					b4 = -1;
					break;
				}
				b4 = base64DecodeChars[b4];
			} while (b4 == -1);
			if (b4 == -1) {
				break;
			}
			buf.write((int) (((b3 & 0x03) << 6) | b4));
		}
		try {
			return buf.toString(charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
