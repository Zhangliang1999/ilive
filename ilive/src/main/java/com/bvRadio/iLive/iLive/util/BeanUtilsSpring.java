package com.bvRadio.iLive.iLive.util;

import org.springframework.beans.BeansException;

public class BeanUtilsSpring {
	public static void copyProperties(Object target, Object source)
			throws BeansException {
		org.springframework.beans.BeanUtils.copyProperties(source , target);
	}
}
