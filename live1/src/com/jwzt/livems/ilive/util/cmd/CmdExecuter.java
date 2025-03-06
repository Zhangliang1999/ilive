package com.jwzt.livems.ilive.util.cmd;

import java.util.List;

import com.jwzt.common.RunExec;

/** 
 */
public class CmdExecuter {

	static public void exec(List<String> cmd, IStringGetter getter) throws RuntimeException{
		try {
			StringBuffer sb = new StringBuffer();
			for (String str : cmd) {
				sb.append(str).append(" ");
			}
			RunExec.SYRunFFMPEGAppend(sb.toString(), getter);
			// System.out.println(sb.toString());
			// ProcessBuilder builder = new ProcessBuilder();
			// builder.command(sb.toString());
			// builder.redirectErrorStream(true);
			// Process proc = builder.start();
			// BufferedReader stdout = new BufferedReader(new
			// InputStreamReader(proc.getInputStream()));
			// String line;
			// while ((line = stdout.readLine()) != null) {
			// if (getter != null)
			// getter.dealString(line);
			// }
			// proc.waitFor();
			// stdout.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}