package com.bvRadio.iLive.iLive.util;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.beanutils.converters.SqlTimestampConverter;

/**
 * beanuitls扩展类
 * 
 * @author administrator
 *
 */
public class BeanUtilsExt extends BeanUtils {

	private BeanUtilsExt() {
	}

	static {
		// 注册sql.date的转换器，即允许BeanUtils.copyProperties时的源目标的sql类型的值允许为空
		ConvertUtils.register(new SqlDateConverter(null), java.util.Date.class);
		ConvertUtils.register(new SqlTimestampConverter(null), java.sql.Timestamp.class);
	}

	public static void copyProperties(Object target, Object source)
			throws InvocationTargetException, IllegalAccessException {

		org.apache.commons.beanutils.BeanUtils.copyProperties(target, source);

	}

}
