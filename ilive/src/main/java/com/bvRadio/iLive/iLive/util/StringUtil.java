package com.bvRadio.iLive.iLive.util;

public class StringUtil {
	private static final char digits[] = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	/**
     * 获取指定指标之间的内容
     * @param     sContent 要处理的字符串
     * @param     sTagName 指标名称
     * @return    指标的内容
     */
    public static String getTagValue(String sContent, String sTagName)
    {
        String sTagValue = "";
        String sTemp;
        if(sContent != null && sTagName!= null)
        {
            sTemp = "<" + sTagName + ">";
            int nPosBegin = sContent.indexOf(sTemp);
            if (nPosBegin >= 0)
            {
                sTemp  = "</" + sTagName + ">";
                int nPosEnd = sContent.indexOf(sTemp);
                if (nPosEnd >= 0)
                {
                    sTagValue = sContent.substring(nPosBegin + sTagName.length() + 2,nPosEnd);
                }
            }
        }
        return sTagValue;
	}

	public static String toHex(String data, String encode) {
		try {
			return toHex(data.getBytes(encode));
		} catch (Exception e) {
		}
		return "";
	}
	public static String toHex(byte byteData[]) {
		return toHex(byteData, 0, byteData.length);
	}
	public static String toHex(byte byteData[], int offset, int len) {
		char buf[] = new char[len * 2];
		int k = 0;
		for (int i = offset; i < len; i++) {
			buf[k++] = digits[(byteData[i] & 255) >> 4];
			buf[k++] = digits[byteData[i] & 15];
		}

		return new String(buf);
	}

	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}


}
