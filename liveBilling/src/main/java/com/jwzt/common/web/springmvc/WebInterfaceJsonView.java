package com.jwzt.common.web.springmvc;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.AbstractView;

import com.jwzt.common.exception.WrongRenderJSONException;
import com.jwzt.common.utils.JsonUtils;

/**
 * @author ysf
 */
public class WebInterfaceJsonView extends AbstractView {

	private final static Logger log = LogManager.getLogger();

	/**
	 * Default content type. Overridable as bean property.
	 */
	public static final String DEFAULT_CONTENT_TYPE = "application/json";

	/**
	 * Construct a new {@code JacksonJsonView}, setting the content type to
	 * {@code application/json}.
	 */
	public WebInterfaceJsonView() {
		setContentType(DEFAULT_CONTENT_TYPE);
	}

	private Set<String> renderedAttributes;

	private boolean disableCaching = true;

	/**
	 * Returns the attributes in the model that should be rendered by this view.
	 */
	public Set<String> getRenderedAttributes() {
		return renderedAttributes;
	}

	/**
	 * Sets the attributes in the model that should be rendered by this view.
	 * When set, all other model attributes will be ignored.
	 */
	public void setRenderedAttributes(Set<String> renderedAttributes) {
		this.renderedAttributes = renderedAttributes;
	}

	/**
	 * Disables caching of the generated JSON.
	 *
	 * <p>
	 * Default is {@code true}, which will prevent the client from caching the
	 * generated JSON.
	 */
	public void setDisableCaching(boolean disableCaching) {
		this.disableCaching = disableCaching;
	}

	@Override
	protected void prepareResponse(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType(getContentType());
		response.setCharacterEncoding("UTF-8");
		if (disableCaching) {
			response.addHeader("Pragma", "no-cache");
			response.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
			response.addDateHeader("Expires", 1L);
		}
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Object value = filterModel(model);
		String json = JsonUtils.objToJson(value);
		log.trace(json);
		PrintWriter respWriter = response.getWriter();
		String jsoncallback = request.getParameter("jsoncallback");
		if (!StringUtils.isBlank(jsoncallback)) {
			respWriter.print(jsoncallback + "(" + json + ");");
		} else {
			respWriter.print(json);
		}
	}

	/**
	 * Filters out undesired attributes from the given model. The return value
	 * can be either another {@link Map}, or a single value object.
	 *
	 * <p>
	 * Default implementation removes {@link BindingResult} instances and
	 * entries not included in the {@link #setRenderedAttributes(Set)
	 * renderedAttributes} property.
	 *
	 * @param model
	 *            the model, as passed on to {@link #renderMergedOutputModel}
	 * @return the object to be rendered
	 * @throws WrongRenderJSONException
	 */
	protected Object filterModel(Map<String, Object> model) throws WrongRenderJSONException {
		Map<String, Object> result = new HashMap<String, Object>(model.size());
		Set<String> renderedAttributes = !CollectionUtils.isEmpty(this.renderedAttributes) ? this.renderedAttributes
				: model.keySet();

		for (String key : renderedAttributes) {
			Object value = model.get(key);
			if (null != value) {
				// 过滤有绑定的数据
				if (!(value instanceof BindingResult)) {
					result.put(key, value);
				}
			}
		}
		return result;
	}
}
