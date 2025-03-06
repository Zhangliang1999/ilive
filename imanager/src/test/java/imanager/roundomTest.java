package imanager;

import java.util.Random;

public class roundomTest {
	public static String getRandomString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		 Random random = new Random();
		 StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
		 int number = random.nextInt(base.length());
		 sb.append(base.charAt(number));
		}
		 return sb.toString();
	}
	public static void main(String[] args) {
		String rundom=getRandomString(16);
		System.out.println(rundom);
	}
}
