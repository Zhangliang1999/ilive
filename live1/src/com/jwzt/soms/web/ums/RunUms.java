package com.jwzt.soms.web.ums;

/**
 * 启动ums
 */
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jwzt.common.Logger;

public class RunUms {
	public boolean RunUmss(String commandTxt) {
		return RunUmss(commandTxt, true);
	}
	

	/**
	 * 执行命令行
	 * @param commandTxt 命令行
	 * @param wait 是否等待命令结束
	 * @return
	 */
	public boolean RunUmss(String commandTxt, boolean wait) {
		boolean ret = false;

		Process process = null;
		try {
			String command = commandTxt;
			System.out.println(command);

			process = Runtime.getRuntime().exec(command);
			TheardOut t1 = new TheardOut(process.getErrorStream());
			TheardOut t2 = new TheardOut(process.getInputStream());
			t1.start();
			t2.start();
			ret = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (wait)
					process.waitFor();
			} catch (Exception e) {
				e.printStackTrace();
				Logger.log(e.getMessage(), 3);
			}
		}

		return ret;
	}

	/**
	 * 
	 * 线程允许程序
	 * @author jwzt
	 *
	 */
	private class TheardOut extends Thread {

		private InputStream input;

		public TheardOut(InputStream in) {
			this.input = in;

		}

		public void run() {
			BufferedReader reader = null;
			try {

				reader = new BufferedReader(new InputStreamReader(this.input));
				String line = "";
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (reader != null)
						reader.close();
					if (input != null)
						input.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

}
