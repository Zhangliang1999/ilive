package com.jwzt.common.log4j2.appenders;

import java.io.Serializable;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

/**
 * @author ysf
 */
@Plugin(name = "NotifyAppender", category = "Core", elementType = "appender", printObject = true)
public class NotifyAppender extends AbstractAppender {

	private Level level = null;

	protected NotifyAppender(String name, Filter filter, Layout<? extends Serializable> layout,
			boolean ignoreExceptions, Level level) {
		super(name, filter, layout, ignoreExceptions);
		this.level = level;
	}

	@Override
	public void append(LogEvent event) {
		Level eventLevel = event.getLevel();
		if (null != eventLevel) {
			if (eventLevel.isMoreSpecificThan(level)) {
				// TODO 需要通知监控系统
				System.out.println("通知监控系统*********");
			}
		}
	}

	/**
	 * 接收配置文件中的参数信息
	 * 
	 * @param name
	 * @param filter
	 * @param layout
	 * @param ignoreExceptions
	 * @return
	 */
	@PluginFactory
	public static NotifyAppender createAppender(@PluginAttribute("name") String name,
			@PluginElement("Filter") final Filter filter,
			@PluginElement("Layout") Layout<? extends Serializable> layout,
			@PluginAttribute("ignoreExceptions") boolean ignoreExceptions, @PluginAttribute("level") Level level) {
		if (name == null) {
			LOGGER.error("No name provided for MyCustomAppenderImpl");
			return null;
		}
		if (layout == null) {
			layout = PatternLayout.createDefaultLayout();
		}
		return new NotifyAppender(name, filter, layout, ignoreExceptions, level);
	}

}
