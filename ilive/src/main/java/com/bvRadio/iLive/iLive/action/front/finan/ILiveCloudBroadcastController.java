package com.bvRadio.iLive.iLive.action.front.finan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.cache.CacheManager;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.front.vo.cloud.Content;
import com.bvRadio.iLive.iLive.action.front.vo.cloud.Output;
import com.bvRadio.iLive.iLive.action.front.vo.cloud.XmlRoot;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.util.Md5Util;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.Annotations;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 云导播API控制器
 * 
 * @author administrator
 */

@Controller
@RequestMapping(value = "/cloudbroad")
public class ILiveCloudBroadcastController {

	private static final String RTMP_PROTOACAL = "rtmp://";

	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;

	@Autowired
	private ILiveServerAccessMethodMng iLiveServerAccessMethodMng;

	@RequestMapping(value = "auth.jspx")
	public void generatorAuth(Integer roomId, HttpServletResponse response) {
		// 生存客户端登录token
		UUID uuid = UUID.randomUUID();
		String token = Md5Util.encode(uuid.toString() + System.currentTimeMillis());
		CacheManager.putCacheInfo(CacheManager.cloudbroad_token_ + token, roomId, 5 * 60 * 1000);
		Map<String, Object> map = new HashMap<>();
		map.put("auth", token);
		map.put("code", 1);
		ResponseUtils.renderJson(response, new JSONObject(map).toString());
	}

	@RequestMapping(value = "init.jspx")
	public void broadCast(String auth, Integer roomId, HttpServletResponse response) throws IOException {
		if (auth != null) {
			String[] splitArr = auth.split("@");
			String timeStamp = splitArr[0];
			String substring = timeStamp.substring(4, timeStamp.length() - 4);
			long time = generatorTimeStr();
			long distance = 5 * 60;
			if (time - Long.parseLong(substring) > distance) {
				response.getWriter().print("");
				return;
			}
			String sequence = splitArr[1];
			String orgPass = substring + "jwzt" + sequence;
			String encodeStr = Md5Util.encode(orgPass);
			if (!encodeStr.toUpperCase().equals(splitArr[2])) {
				response.getWriter().print("");
				return;
			}
		}
		ILiveLiveRoom room = iLiveLiveRoomMng.findById(roomId);
		ILiveServerAccessMethod accessMethod = iLiveServerAccessMethodMng
				.getAccessMethodBySeverGroupId(room.getServerGroupId());
		String pushStreamAddr = RTMP_PROTOACAL + accessMethod.getOrgLiveHttpUrl() + ":" + accessMethod.getUmsport()
				+ "/live/live" + roomId + "_tzwj_5000k";
		String retXml = "";
		try {
			retXml = this.buildXml(pushStreamAddr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ResponseUtils.renderXml(response, retXml);
	}

	public long generatorTimeStr() {
		Calendar calendar = Calendar.getInstance();
		int nMonth = calendar.get(Calendar.MONTH) + 1;
		int nDay = calendar.get(Calendar.DAY_OF_MONTH);
		int nHour = calendar.get(Calendar.HOUR_OF_DAY);
		int nMin = calendar.get(Calendar.MINUTE);
		int nSecond = calendar.get(Calendar.SECOND);
		long lTime = ((nMonth * 30) + nDay) * 24 * 3600 + nHour * 3600 + nMin * 60 + nSecond;
		return lTime;
	}

	/**
	 * 构建xml返回给客户端调用
	 * 
	 * @param pushStreamAddr
	 * @return
	 * @throws IOException
	 */
	private String buildXml(String pushStreamAddr) throws IOException {
		XStream xstream = new XStream(new DomDriver());
		Annotations.configureAliases(xstream, XmlRoot.class);
		XmlRoot xmlRoot = new XmlRoot();
		Content content = new Content();
		xmlRoot.setContent(content);
//		List<Input> inputs = new ArrayList<>();
//		Input input = new Input();
//		input.setRemoteAddr("rtmp://live.hkstv.hk.lxdns.com/live/hks");
//		inputs.add(input);
//		content.setInputs(inputs);
		List<Output> outputs = new ArrayList<>();
		Output ot = new Output();
		ot.setOutRTMP(pushStreamAddr);
		ot.setCodecInfo(ot.getDefaultCodeInfo());
		outputs.add(ot);
		content.setOutputs(outputs);
		String obj_xml = xstream.toXML(xmlRoot);
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?>\r\n");
		sb.append(obj_xml);
		return obj_xml;
	}

}
