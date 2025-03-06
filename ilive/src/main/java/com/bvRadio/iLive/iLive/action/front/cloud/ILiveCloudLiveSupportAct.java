package com.bvRadio.iLive.iLive.action.front.cloud;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.web.ResponseUtils;

/**
 * 云直播提供服务
 * 
 * @author administrator
 *
 */

@Controller
@RequestMapping(value = "support/cloud")
public class ILiveCloudLiveSupportAct {

	
	/**
	 * 构建直播间
	 */
	@RequestMapping(value = "channelManage/createChannel.jhtml")
	public void createChannel(HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		resultJson.put("code", ERROR_STATUS);
		resultJson.put("message", "123123");
		resultJson.put("data", "{}");
		ResponseUtils.renderJson(response, resultJson.toString());
	}

}
