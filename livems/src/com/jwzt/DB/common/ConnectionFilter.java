package com.jwzt.DB.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


public class ConnectionFilter implements Filter
{
	protected FilterConfig filterConfig = null;

	public void init(FilterConfig filterConfig) throws ServletException
	{
		this.filterConfig = filterConfig;
	}

	public void destroy()
	{
		this.filterConfig = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException
	{
		try
		{
			request.setCharacterEncoding("GBK");
            chain.doFilter(request, response);
            HibernateSessionUtil.commitTransaction();
		}
		finally
		{
			HibernateSessionUtil.closeSession();
		}

	}

}
