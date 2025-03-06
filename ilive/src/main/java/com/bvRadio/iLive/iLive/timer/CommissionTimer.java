package com.bvRadio.iLive.iLive.timer;

import java.util.Timer;

public class CommissionTimer {

	public static void timerStart() {
		Timer timer = new Timer();
		timer.schedule(new CommissionTimerTask(), 5000, 3600 * 24 * 1000);
	}
}
