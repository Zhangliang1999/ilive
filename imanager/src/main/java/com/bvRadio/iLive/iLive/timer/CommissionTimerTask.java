package com.bvRadio.iLive.iLive.timer;

import java.util.Calendar;
import java.util.TimerTask;

import com.bvRadio.iLive.iLive.config.SystemXMLTomcatUrl;
import com.bvRadio.iLive.iLive.manager.UserBalancesMng;
import com.bvRadio.iLive.iLive.util.ApplicationContextUtil;

public class CommissionTimerTask extends TimerTask {

	@Override
	public void run() {
		Calendar cal = Calendar.getInstance();
		int day_of_month = cal.get(Calendar.DAY_OF_MONTH);
		int day=1;
		try {
			day = Integer.valueOf(SystemXMLTomcatUrl.getUrl("DAY_OF_MONTH"));
		} catch (NumberFormatException e) {
			System.out.println("佣金计算时间设置不合法！启用默认时间（每月1号）进行周期计算");
			day=1;
		}
		if (day_of_month == day) {
			UserBalancesMng userBalancesMng = (UserBalancesMng) ApplicationContextUtil.getBean("userBalancesMng");
			userBalancesMng.selectUserBalanceAllCommission();
		}else{
			System.out.println("*****************************************************************************");
			System.out.println("*********************今天是本月："+day_of_month+"号**********************************");
			System.out.println("*****************************************************************************");
		}
	}

}
