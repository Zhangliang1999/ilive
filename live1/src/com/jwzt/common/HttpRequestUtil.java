/**
 * 由于不同的服务器对request.getParamter的实现不同，常常出现乱码这里用一个类来
 * 单独处理和request有关的操作
 */

package com.jwzt.common;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class HttpRequestUtil
{
    /** request的请求串 */
    private String queryString = null;

    /** 内部保存的reqeust对象 */
    private HttpServletRequest request = null;

    private String zstr = "";

    /**
     * 获取内部的request对象
     * @return request对象
     */
    public HttpServletRequest getHttpRequest()
    {
        return request;
    }
    /** 构造函数，初始化成员request */
    public HttpRequestUtil(HttpServletRequest request)
    {
        this.request = request;
    }

    /**
     * 获取递交的请求串
     * @return	        请求串
     */
    private void getQueryString()
    {
        if ((queryString == null) && (request != null))
        {
            if (request.getMethod().equalsIgnoreCase("get"))
            {
                queryString = request.getQueryString();
            }
            else
            {
                try
                {
                    ServletInputStream is = request.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuffer buffer = new StringBuffer(512);
                    String line = reader.readLine();
                    while (line != null)
                    {
                        buffer.append(line);
                        line = reader.readLine();
                    }
                    queryString = buffer.toString();
                }
                catch (Exception ex)
                {
                    Logger.log(ex, 3);
                }
            }
        }
    }

    /**
     * 获取递交的参数的值
     * @param           paraName          参数值
     * @param           encoded      true包含中文，false只是英文
     * @return	    返回指定参数的值
     */
    public String getParameter(String paraName, boolean encoded)
    {
        getQueryString();
        if (queryString == null)
        {
            return null;
        }
        String strRet = null;
        String strFind = paraName + "=";

        int strlen = queryString.length();
        int beginindex = queryString.indexOf(strFind);
        if(beginindex > 0)
        {
            char tmp_char = queryString.charAt(beginindex - 1);
            while(tmp_char != '&')
            {
                beginindex = queryString.indexOf(strFind, beginindex + strFind.length());
                if (beginindex > 0)
                {
                    tmp_char = queryString.charAt(beginindex - 1);
                }
                else
                {
                    break;
                }
            }
        }
        if(beginindex > -1)
        {
            beginindex += strFind.length();
            int endindex = queryString.indexOf("&", beginindex);
            if(endindex == -1)
            {
                endindex = strlen;
            }
            strRet = queryString.substring(beginindex, endindex);
            if(encoded)
            {
                strRet = unescapestring(strRet);
            }
        }

        if(encoded && strRet != null)
        {
            strRet = parseName(strRet);
        }
        return strRet;
    }
    
    public String getParameterGBK(String paraName, boolean encoded)
    {
        getQueryString();
        if (queryString == null)
        {
            return null;
        }
        String strRet = null;
        String strFind = paraName + "=";

        int strlen = queryString.length();
        int beginindex = queryString.indexOf(strFind);
        if(beginindex > 0)
        {
            char tmp_char = queryString.charAt(beginindex - 1);
            while(tmp_char != '&')
            {
                beginindex = queryString.indexOf(strFind, beginindex + strFind.length());
                if (beginindex > 0)
                {
                    tmp_char = queryString.charAt(beginindex - 1);
                }
                else
                {
                    break;
                }
            }
        }
        if(beginindex > -1)
        {
            beginindex += strFind.length();
            int endindex = queryString.indexOf("&", beginindex);
            if(endindex == -1)
            {
                endindex = strlen;
            }
            strRet = queryString.substring(beginindex, endindex);
            if(encoded)
            {
                strRet = unescapestringGBK(strRet);
            }
        }

        if(encoded && strRet != null)
        {
            strRet = parseNameGBK(strRet);
        }
        return strRet;
    }

    /**
     * 参数值进行处理，防止乱码的出现
     * @param           strin   需要处理的字符串
     * @return	        返回处理后的字符串值
     */
    public static String unescapestring(String strin)
    {
        if(strin == null)
            return "";

        int length = strin.length();
        if(length == 0)
            return "";

        int bytelen = 0;
        byte[] bytes = new byte[length];

        int tempB1,tempB2;
        char tempChar;
        for(int i=0; i<length; i++)
        {
            tempChar = strin.charAt(i);
            if(tempChar == '+')
            {
                //加号必须变为空格
                bytes[bytelen++] = ' ';
                continue;
            }
            if(tempChar == '%')
            {
                tempChar = strin.charAt(++i);
                if(tempChar != 'u')
                {
                    //国标码
                    tempB1 = Character.digit(tempChar,16);
                    tempChar = strin.charAt(++i);
                    tempB2 = Character.digit(tempChar,16);
                    bytes[bytelen++] = (byte)(tempB1*16 + tempB2);
                }
                else
                {
                    //UNICODE码
                    tempChar = strin.charAt(++i);
                    tempB1 = Character.digit(tempChar,16);

                    tempChar = strin.charAt(++i);
                    tempB1 = tempB1*16 + Character.digit(tempChar,16);

                    tempChar = strin.charAt(++i);
                    tempB1 = tempB1*16 + Character.digit(tempChar,16);

                    tempChar = strin.charAt(++i);
                    tempB1 = tempB1*16 + Character.digit(tempChar,16);

                    tempChar = (char)tempB1;
                    byte[] tempbytes = null;

                    try
                    {
                        tempbytes = ("" + tempChar).getBytes("UTF-8");
                    }
                    catch(java.io.UnsupportedEncodingException f)
                    {
                        tempbytes = ("" + tempChar).getBytes();
                    }

                    bytes[bytelen++] = tempbytes[0];
                    bytes[bytelen++] = tempbytes[1];
                }
            }
            else
            {
                bytes[bytelen++] = (byte)tempChar;
            }
        }

        if(bytelen < length)
        {
            byte[] trimValue = new byte[bytelen];
            System.arraycopy(bytes, 0, trimValue, 0, bytelen);
            bytes = trimValue;
        }

        try
        {
            return new String(bytes, "UTF-8");
        }
        catch(java.io.UnsupportedEncodingException ex)
        {
            return  new String(bytes);
        }
    }
    
    
    static String unescapestringGBK(String strin)
    {
        if(strin == null)
            return "";

        int length = strin.length();
        if(length == 0)
            return "";

        int bytelen = 0;
        byte[] bytes = new byte[length];

        int tempB1,tempB2;
        char tempChar;
        for(int i=0; i<length; i++)
        {
            tempChar = strin.charAt(i);
            if(tempChar == '+')
            {
                //加号必须变为空格
                bytes[bytelen++] = ' ';
                continue;
            }
            if(tempChar == '%')
            {
                tempChar = strin.charAt(++i);
                if(tempChar != 'u')
                {
                    //国标码
                    tempB1 = Character.digit(tempChar,16);
                    tempChar = strin.charAt(++i);
                    tempB2 = Character.digit(tempChar,16);
                    bytes[bytelen++] = (byte)(tempB1*16 + tempB2);
                }
                else
                {
                    //UNICODE码
                    tempChar = strin.charAt(++i);
                    tempB1 = Character.digit(tempChar,16);

                    tempChar = strin.charAt(++i);
                    tempB1 = tempB1*16 + Character.digit(tempChar,16);

                    tempChar = strin.charAt(++i);
                    tempB1 = tempB1*16 + Character.digit(tempChar,16);

                    tempChar = strin.charAt(++i);
                    tempB1 = tempB1*16 + Character.digit(tempChar,16);

                    tempChar = (char)tempB1;
                    byte[] tempbytes = null;

                    try
                    {
                        tempbytes = ("" + tempChar).getBytes("GBK");
                    }
                    catch(java.io.UnsupportedEncodingException f)
                    {
                        tempbytes = ("" + tempChar).getBytes();
                    }

                    bytes[bytelen++] = tempbytes[0];
                    bytes[bytelen++] = tempbytes[1];
                }
            }
            else
            {
                bytes[bytelen++] = (byte)tempChar;
            }
        }

        if(bytelen < length)
        {
            byte[] trimValue = new byte[bytelen];
            System.arraycopy(bytes, 0, trimValue, 0, bytelen);
            bytes = trimValue;
        }

        try
        {
            return new String(bytes, "GBK");
        }
        catch(java.io.UnsupportedEncodingException ex)
        {
            return  new String(bytes);
        }
    }


    private String parseNameRecursion(String value)
    {
            String temps, ftemps=null, charcol="", tempstr, temphan;

            //System.out.println(value);
            int last= value.lastIndexOf("*");
            int first= value.indexOf("*");
            if (last!=-1){
                    temps=value.substring(first);
                    if (first>=1)
                            ftemps=value.substring(0,first);

                    temphan=temps.substring(2,6);
                    tempstr=temps.substring(6);
                    char ch=(char)Integer.parseInt(temphan,16);
                    charcol+=ch;
                    if(ftemps!=null)
                            zstr=zstr+ftemps+charcol;
                    else
                            zstr+=charcol;
                    zstr= parseNameRecursion(tempstr);
            }
            else{
                    if(zstr!=null)
                            zstr+=value;
                    else
                            zstr=value;
            }
            try{
             //   CharToByteConverter   convert=  CharToByteConverter.getConverter("gb2312");
               // byte [] b=convert.convertAll(zstr.toCharArray());
               byte[] b= zstr.getBytes("UTF-8");
                    zstr=new String (b,"UTF-8");
            }catch (Exception e){
            }
            return zstr;
    }
    
    
    private String parseNameRecursionGBK(String value)
    {
            String temps, ftemps=null, charcol="", tempstr, temphan;

            //System.out.println(value);
            int last= value.lastIndexOf("*");
            int first= value.indexOf("*");
            if (last!=-1){
                    temps=value.substring(first);
                    if (first>=1)
                            ftemps=value.substring(0,first);

                    temphan=temps.substring(2,6);
                    tempstr=temps.substring(6);
                    char ch=(char)Integer.parseInt(temphan,16);
                    charcol+=ch;
                    if(ftemps!=null)
                            zstr=zstr+ftemps+charcol;
                    else
                            zstr+=charcol;
                    zstr= parseNameRecursion(tempstr);
            }
            else{
                    if(zstr!=null)
                            zstr+=value;
                    else
                            zstr=value;
            }
            try{
             //   CharToByteConverter   convert=  CharToByteConverter.getConverter("gb2312");
               // byte [] b=convert.convertAll(zstr.toCharArray());
               byte[] b= zstr.getBytes("GBK");
                    zstr=new String (b,"GBK");
            }catch (Exception e){
            }
            return zstr;
    }
    
    private String parseName(String value){
            zstr = "";
            return parseNameRecursion(value);
    }
    
    private String parseNameGBK(String value){
        zstr = "";
        return parseNameRecursionGBK(value);
}


}