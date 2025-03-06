package com.bvRadio.iLive.iLive.util;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;

import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.WorkLog;
import com.bvRadio.iLive.iLive.manager.WorkLogMng;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

import net.sf.json.JSONObject;

public class IPUtils {
	/**
	 * 将127.0.0.1形式的IP地址转换成十进制整数，这里没有进行任何错误处理
	 * 
	 * @param strIp
	 * @return
	 */
	public static long ipToLong(String strIp) {
		long[] ip = new long[4];
		// 先找到IP地址字符串中.的位置
		int position1 = strIp.indexOf(".");
		int position2 = strIp.indexOf(".", position1 + 1);
		int position3 = strIp.indexOf(".", position2 + 1);
		// 将每个.之间的字符串转换成整型
		ip[0] = Long.parseLong(strIp.substring(0, position1));
		ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));
		ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));
		ip[3] = Long.parseLong(strIp.substring(position3 + 1));
		return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
	}

	/**
	 * 将十进制整数形式转换成127.0.0.1形式的ip地址
	 * 
	 * @param longIp
	 * @return
	 */
	public static String longToIP(long longIp) {
		StringBuffer sb = new StringBuffer("");
		// 直接右移24位
		sb.append(String.valueOf((longIp >>> 24)));
		sb.append(".");
		// 将高8位置0，然后右移16位
		sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
		sb.append(".");
		// 将高16位置0，然后右移8位
		sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
		sb.append(".");
		// 将高24位置0
		sb.append(String.valueOf((longIp & 0x000000FF)));
		return sb.toString();
	}

	// public static void main(String[] args) {
	// String ipStr = "58.50.24.78";
	// long longIp = IPUtils.ipToLong(ipStr);
	// // System.out.println("整数形式为：" + longIp);
	// // System.out.println("整数" + longIp + "转化成字符串IP地址：" +
	// IPUtils.longToIP(longIp));
	// // ip地址转化成二进制形式输出
	// // System.out.println("二进制形式为：" + Long.toBinaryString(longIp));
	// }

	
	public static String getRealIpAddr(HttpServletRequest req) {
		String ip = req.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getRemoteAddr();
		}
		if (ip != null) {
			if (ip.indexOf(",") > -1) {
				String[] split = ip.split(",");
				if (split.length > 0) {
					return split[0].trim();
				} else {
					return ip;
				}
			} else {
				return ip;
			}
		} else {
			return "";
		}
	}
	
	/**
	 * 根据ip获取地区
	 * @param ip
	 * @return
	 */
	public static String getRegion(String ip) {
		String url = "http://freeapi.ipip.net/";
		String remoteUrl = url+ip;
		try {
			String sendPost = HttpUtils.sendGet(remoteUrl, "utf-8");
			System.out.println(sendPost);
			if (sendPost!=null&&!sendPost.equals("")) {
				String substring = sendPost.substring(1, sendPost.length()-1);
				System.out.println(sendPost);
				substring = substring.replaceAll("\"", "");
				System.out.println(substring);
				String[] split = substring.split(",");
				StringBuilder sBuilder = new StringBuilder();
				for(int i=0;i<split.length;i++){
					if (i<=2) {
						sBuilder.append(split[i]);
					}
				}
				return sBuilder.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 根据ip获取地区
	 * @param ip
	 * @return
	 */
	public static String getRegion2(String ip) {
		String url = ConfigUtils.get("StaticsNofityUrl")+"/service/ip/getAddress?ip="+ip;
		try {
			String sendPost = HttpUtils.sendGet(url, "utf-8");
			System.out.println(sendPost);
			JSONObject result = JSONObject.fromObject(sendPost);
			String code = result.getString("code");
			if (code!=null&&code.equals("0")) {
				return result.getString("data");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "未知";
		}
		return "未知";
	}
	
	
	private static final int queueMax = 100;
	private static final BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(queueMax);
	private static final ThreadPoolExecutor EXECUTOR = 
			new ThreadPoolExecutor(10, queueMax, 0, TimeUnit.SECONDS, workQueue);
	
	public static void asyncSaveWork(ILiveManager user,WorkLogMng workLogMng) {
		EXECUTOR.execute(new AsyncGetIp(user, workLogMng));
	}
	
}
class AsyncGetIp implements Runnable{

	private ILiveManager user;
	private WorkLogMng workLogMng;
	
	public AsyncGetIp(ILiveManager user, WorkLogMng workLogMng) {
		super();
		this.user = user;
		this.workLogMng = workLogMng;
	}

	@Override
	public void run() {
		try {
			if (user.getLastIP()!=null&&!user.getLastIP().equals("")) {
				String region = IPUtils.getRegion2(user.getLastIP());
				user.setLastRegion(region);
				System.out.println(region);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			String url2 = ConfigUtils.get("StaticsNofityUrl")+"/service/ip/saveRecord?ip="+user.getLastIP()+"&userId="+user.getUserId();
			net.sf.json.JSONObject fromObject = net.sf.json.JSONObject.fromObject(user);
			try {
				String sendPost = HttpUtils.sendGet(url2, "utf-8");
				System.out.println(sendPost);
			} catch (IOException e) {
				e.printStackTrace();
			}
			workLogMng.save(new WorkLog(WorkLog.MODEL_LOGIN, user.getUserId()+"", fromObject.toString(), WorkLog.MODEL_LOGIN_NAME, user.getUserId(), user.getMobile(), ""));
			System.out.println("日志保存成功");
		}
	}
	
}