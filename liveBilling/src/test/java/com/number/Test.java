package com.number;

import java.math.BigDecimal;

/**
 * @author ysf
 */
public class Test {
	public static void main(String[] args) {
		Double platformAmount = 0.974925124;
		BigDecimal b = new BigDecimal(platformAmount);
		platformAmount = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		System.out.println(platformAmount);
	}
}
