package com.bvRadio.iLive.iLive.util;

public class StringUtil {
	
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

}
