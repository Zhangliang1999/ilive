package com.bvRadio.iLive.iLive.util;

import com.alipay.api.internal.util.StringUtils;

/**
 * 终端
 * 
 * @author administrator
 *
 */
public final class TerminalUtil {

	/**
	 * 检测APP
	 * @param terminalType
	 * @return
	 */
	public static boolean checkApp(String terminalType) {
		if (!StringUtils.isEmpty(terminalType)) {
			if (terminalType.equals("android") || terminalType.equals("ios")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检测PC
	 * 
	 * @param terminalType
	 * @return
	 */
	public static boolean checkPc(String terminalType) {
		if (!StringUtils.isEmpty(terminalType)) {
			if (terminalType.equals("pc")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检测H5
	 * 
	 * @param terminalType
	 * @return
	 */
	public static boolean checkH5(String terminalType) {
		if (!StringUtils.isEmpty(terminalType)) {
			if (terminalType.equals("h5")) {
				return true;
			}
		}
		return false;
	}

}
