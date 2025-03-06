package com.bvRadio.iLive.iLive.action.front.phone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.Topic;
import com.bvRadio.iLive.iLive.manager.TopicMng;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="topic")
public class TopicPhoneController {

	@Autowired
	private TopicMng topicMng;	//专题
	
	@RequestMapping(value="getById.jspx")
	public void getTopicById(Long id,HttpServletResponse response,HttpServletRequest request) {
		JSONObject result = new JSONObject();
		try {
			Topic topic = topicMng.getById(id);
			if(topic==null || topic.getIsPut()==0) {
				result.put("code", "2");
				result.put("message", "该专题不存在或者已下架");
				result.put("data", new JSONObject());
			}else {
				result.put("code", "1");
				result.put("message", "获取成功");
				result.put("data", JSONObject.fromObject(topic));
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			result.put("code", "0");
			result.put("message", "获取页面失败");
			result.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request, response, result.toString());
	}
	
}
