package com.bvRadio.iLive.test;

import java.math.RoundingMode;
import java.text.NumberFormat;

public class convertMemorySizeFromKb2MbTest {
public static void main(String[] args) {
	Long fileSize=123243254354L;
	if (fileSize == null)
		System.out.println("0.0");
	double size = fileSize / 1024 + (fileSize % 1024) / 1024.0;
	NumberFormat nf = NumberFormat.getNumberInstance();
	nf.setMaximumFractionDigits(2);
	nf.setRoundingMode(RoundingMode.UP);
	System.out.println(nf.format(size));
}
}
