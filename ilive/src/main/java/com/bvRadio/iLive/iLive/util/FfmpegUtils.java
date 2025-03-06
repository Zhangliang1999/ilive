package com.bvRadio.iLive.iLive.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bvRadio.iLive.iLive.web.ConfigUtils;

@SuppressWarnings("unused")
public class FfmpegUtils {
	private static final Logger log = LoggerFactory.getLogger(FfmpegUtils.class);

	public static boolean transcodeForAudio(String inFile, String outFile) {
		String command = ConfigUtils.get(ConfigUtils.TRANSCODE_COMMAND_VIDEO);
		command = command.replaceAll(":inFile", inFile).replaceAll(":outFile", outFile);
		log.info("开始音频转码 command = {}" , command);
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(command);
			InputStream stderr = proc.getErrorStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
				;
		} catch (Throwable t) {
			t.printStackTrace();
			return false;
		}
		return true;
	}

	public static  boolean transcodeForVideo(String inFile, String outFile) {
		/*StringBuffer command = new StringBuffer();
		command.append("E:/i_Workspace/bvRadioBBS/ffmpeg/ffmpeg.exe "); 

		command.append(" -i  ");
		command.append(inFile);
		command.append(" -r 29.97 -b:v 384k -s 540x304 -ar 24000 ");
		
		command.append(outFile);*/
		String command = ConfigUtils.get(ConfigUtils.TRANSCODE_COMMAND_VIDEO);
		command = command.replaceAll(":inFile", inFile).replaceAll(":outFile", outFile);
		log.info("开始视频转码 command = {}" , command);
		try {
			// System.out.println("inFile==================="+inFile);
			// System.out.println("outFile===================="+outFile);
			// System.out.println("command======================="+command);
			// System.out.println("正式转码==========111");
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(command.toString());
			InputStream stderr = proc.getErrorStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null){
				// System.out.println(line);
			};
		} catch (Throwable t) {
			// System.out.println("出现错误");
			t.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) throws IOException {
		String command = "E:/i_Workspace/bvRadioBBS/ffmpeg/ffmpeg.exe  -i  E:\\RJAZ\\apache-tomcat-7.0.75-windows-x64\\apache-tomcat-7.0.75\\webapps\\bvRadioILive\\temp\\1517900468253.mp4 -r 29.97 -b:v 384k -s 540x304 -ar 24000 E:\\RJAZ\\apache-tomcat-7.0.75-windows-x64\\apache-tomcat-7.0.75\\webapps\\bvRadioILive\\temp/1518058758.mp4";
		Runtime rt = Runtime.getRuntime();
		Process proc = rt.exec(command);
		InputStream stderr = proc.getErrorStream();
		InputStreamReader isr = new InputStreamReader(stderr);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		while ((line = br.readLine()) != null){
			// System.out.println(line);
		};
	}

	public static boolean transfer(String inFile, String outFile) {
		String command = "ffmpeg -i " + inFile + " -y -f image2 -ss 00:00:16  -s 176x144 " + outFile;
		// System.out.println(" command = " + command);
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(command);
			InputStream stderr = proc.getErrorStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
				;
		} catch (Throwable t) {
			t.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 获取时长（单位：毫秒）
	 * 
	 * @param filePath
	 * @return
	 */
	public static Integer getDuration(String filePath) {
		String command = "ffmpeg -i " + filePath + " 2>&1 | grep 'Duration' | cut -d ' ' -f 4 | sed s/,//";
		log.info("音频文件获取时长 command = {}", command);
		try {
			List<String> strList = new ArrayList<String>();
			Process process;
			process = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", command }, null, null);
			InputStreamReader ir = new InputStreamReader(process.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			String line;
			process.waitFor();
			while ((line = input.readLine()) != null) {
				strList.add(line);
			}
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < strList.size(); i++) {
				sb.append(strList.get(i));
			}
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss.SS");
			Date date1 = sdf.parse(sb.toString());
			Date date2 = sdf.parse("00:00:00.00");
			long length = date1.getTime() - date2.getTime();
			int s = (int) (length / 1000);
			log.info("音频文件获取时长成功  s= {}", s);
			return s;
		} catch (Throwable t) {
			log.error("音频文件获取时长出错 command = {}", command);
		}
		return -1;
	}

}
