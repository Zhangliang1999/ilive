package com.bvRadio.iLive.iLive.factory;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.FactoryBean;

public class MsgIdFactoryBean implements FactoryBean<String> {

	private static long counter = 0;

	public synchronized String getObject() {
		String date = new SimpleDateFormat("MMddHHmm").format(new Date());
		String sequ = new DecimalFormat("000000").format(counter++);
		return date + sequ;
	}

	public Class<String> getObjectType() {
		return String.class;
	}

	public boolean isSingleton() {
		return false;
	}

	public static void reset() {
		MsgIdFactoryBean.counter = 0;
	}
}