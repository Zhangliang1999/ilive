package com.jwzt.common.web.springmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * springMVC 通用异常处理
 * 
 * @author ysf
 */
public class CommonSimpleMappingExceptionResolver extends SimpleMappingExceptionResolver {

	private final static Logger log = LogManager.getLogger();

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		String viewName = determineViewName(ex, request);
		if (viewName != null) {
			log.debug("viewName={}", viewName);
			Integer statusCode = determineStatusCode(request, viewName);
			if (statusCode != null) {
				applyStatusCodeIfPossible(request, response, statusCode);
			}
			return getModelAndView(viewName, ex, request);
		} else {
			return null;
		}
	}
}
