package com.bvRadio.iLive.iLive.util;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * XSS过滤处理
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-01 11:29
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
	// 没被包装过的HttpServletRequest（特殊场景，需求自己过滤）

	public XssHttpServletRequestWrapper(HttpServletRequest servletRequest) {
		super(servletRequest);
	}

	@Override
	public String[] getParameterValues(String parameter) {
		String[] values = super.getParameterValues(parameter);
		if (values == null) {
			return null;
		}
		int count = values.length;
		String[] encodedValues = new String[count];
		for (int i = 0; i < count; i++) {
			encodedValues[i] = stripXSS(values[i]);
		}
		return encodedValues;
	}

	@Override
	public String getParameter(String parameter) {
		String value = super.getParameter(parameter);
		return stripXSS(value);
	}

	@Override
	public String getHeader(String name) {
		String value = super.getHeader(name);
		return stripXSS(value);
	}

	private String stripXSS(String value) {
		String valueDe="";
		try {
			if(!"".equals(value)&&value!=null) {
				valueDe = java.net.URLDecoder.decode(value,"utf-8");
			}
			
			
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}
		Integer length=valueDe.length();
		if (valueDe != null) {
			// NOTE: It's highly recommended to use the ESAPI library and
			// uncomment the following line to
			// avoid encoded attacks.
			// value = ESAPI.encoder().canonicalize(value);
			// Avoid null characters
			valueDe = valueDe.replaceAll("", "");
			// Avoid anything between script tags
			Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(valueDe).replaceAll("");
			// Avoid anything in a src="..." type of e­xpression
			scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			valueDe = scriptPattern.matcher(valueDe).replaceAll("");
			scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			valueDe = scriptPattern.matcher(valueDe).replaceAll("");
			scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*(.*?)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			valueDe = scriptPattern.matcher(valueDe).replaceAll("");
			scriptPattern = Pattern.compile("on(.*?)[\r\n]*=[\r\n]*\\\"(.*?)\\\"",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			valueDe = scriptPattern.matcher(valueDe).replaceAll("");
			scriptPattern = Pattern.compile("on(.*?)[\r\n]*=[\r\n]*(.*?)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			valueDe = scriptPattern.matcher(valueDe).replaceAll("");
			// Remove any lonesome </script> tag
			scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
			valueDe = scriptPattern.matcher(valueDe).replaceAll("");
			// Remove any lonesome <script ...> tag
			scriptPattern = Pattern.compile("<script(.*?)>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			valueDe = scriptPattern.matcher(valueDe).replaceAll("");
			// Avoid eval(...) e­xpressions
			scriptPattern = Pattern.compile("eval\\((.*?)\\)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			valueDe = scriptPattern.matcher(valueDe).replaceAll("");
			// Avoid e­xpression(...) e­xpressions
			scriptPattern = Pattern.compile("e­xpression\\((.*?)\\)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			valueDe = scriptPattern.matcher(valueDe).replaceAll("");
			// Avoid javascript:... e­xpressions
			scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
			valueDe = scriptPattern.matcher(valueDe).replaceAll("");
			// Avoid vbscript:... e­xpressions
			scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
			valueDe = scriptPattern.matcher(valueDe).replaceAll("");
			// Avoid onload= e­xpressions
			scriptPattern = Pattern.compile("onload(.*?)=",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			valueDe = scriptPattern.matcher(valueDe).replaceAll("");
		}
		Integer length1=valueDe.length();
		if(length1==length) {
			
			return value;
			
		}else {
				System.out.println("xss--------------------------------value:"+valueDe);
				return valueDe;
			
		}
		
		
	}

}
