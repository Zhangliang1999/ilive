package com.jwzt.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jwzt.livems.ilive.util.cmd.IStringGetter;

public class RunExec {

	public synchronized static boolean SYRunBVAppend(String commandTxt) {
		return new RunExec().RunBVAppend(commandTxt, true);
	}

	public synchronized static boolean SYRunFFMPEGAppend(String commandTxt, IStringGetter getter) {
		return new RunExec().RunFFMPEGAppend(commandTxt, true, getter);
	}

	public boolean RunBVAppend(String commandTxt) {
		return RunBVAppend(commandTxt, true);
	}

	/**
	 * 执行命令行
	 * 
	 * @param commandTxt
	 *            命令行
	 * @param wait
	 *            是否等待命令结束
	 * @return
	 */
	public boolean RunFFMPEGAppend(String commandTxt, boolean wait, IStringGetter getter) {
		boolean ret = false;
		Logger.log("开始执行：" + commandTxt, 3);
		Process process = null;
		try {
			String command = commandTxt;
			System.out.println(command);

			process = Runtime.getRuntime().exec(command);
			TheardOutFFMpeg t1 = new TheardOutFFMpeg(process.getErrorStream(), getter);
			TheardOutFFMpeg t2 = new TheardOutFFMpeg(process.getInputStream(), getter);
			t1.start();
			t2.start();
			ret = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (wait) {
					Logger.log("等待程序结束", 3);
					process.waitFor();

				}
			} catch (Exception e) {
				e.printStackTrace();
				Logger.log(e.getMessage(), 3);
			}
		}
		Logger.log("执行结束：" + commandTxt, 3);
		return ret;
	}

	/**
	 * 执行命令行
	 * 
	 * @param commandTxt
	 *            命令行
	 * @param wait
	 *            是否等待命令结束
	 * @return
	 */
	public boolean RunBVAppend(String commandTxt, boolean wait) {
		boolean ret = false;
		Logger.log("开始执行：" + commandTxt, 3);
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
				if (wait) {
					Logger.log("等待程序结束", 3);
					process.waitFor();

				}
			} catch (Exception e) {
				e.printStackTrace();
				Logger.log(e.getMessage(), 3);
			}
		}
		Logger.log("执行结束：" + commandTxt, 3);
		return ret;
	}

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
					Logger.log(e.getMessage(), 3);
				}
			}

		}
	}

	private class TheardOutFFMpeg extends Thread {

		private InputStream input;

		private IStringGetter getter;

		public TheardOutFFMpeg(InputStream in, IStringGetter getter) {
			this.input = in;
			this.getter = getter;
		}

		public void run() {
			BufferedReader reader = null;
			try {

				reader = new BufferedReader(new InputStreamReader(this.input));
				String line = "";
				while ((line = reader.readLine()) != null) {
					getter.dealString(line);
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
					Logger.log(e.getMessage(), 3);
				}
			}

		}
	}
}
