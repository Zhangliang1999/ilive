package com.bvRadio.iLive.iLive.action.front.phone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.live.model.v20161101.DescribeLiveSnapshotConfigRequest;
import com.aliyuncs.live.model.v20161101.DescribeLiveSnapshotConfigResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * 连麦
 * 
 * @author administrator
 */
@Controller
@RequestMapping(value = "videocall")
public class ILiveVideocallAct {

	IAcsClient client = null;

	public void init() throws ClientException {
		IClientProfile profile = DefaultProfile.getProfile("cn-shanghai", "<your accessKey>", "<your accessSecret>");
		// DefaultProfile.addEndpoint("cn-shanghai", "cn-shanghai", "live",
		// "live.aliyuncs.com"); //添加自定义endpoint。
		client = new DefaultAcsClient(profile);
		// System.setProperty("http.proxyHost", "127.0.0.1");
		// //此设置用于设置代理，可用fiddler拦截查看http请求，便于调试。
		// System.setProperty("http.proxyPort", "8888");
	}

	public void requestInitSample() {
		DescribeLiveSnapshotConfigRequest describeLiveSnapshotConfigRequest = new DescribeLiveSnapshotConfigRequest();
		describeLiveSnapshotConfigRequest.setDomainName("live.aliyunlive.com");
		// describeLiveSnapshotConfigRequest.setProtocol(ProtocolType.HTTPS);
		// //指定访问协议
		// describeLiveSnapshotConfigRequest.setAcceptFormat(FormatType.JSON);
		// //指定api返回格式
		// describeLiveSnapshotConfigRequest.setMethod(MethodType.POST);
		// //指定请求方法
		// describeLiveSnapshotConfigRequest.setRegionId("cn-shanghai");//指定要访问的Region,仅对当前请求生效，不改变client的默认设置。
		try {
			HttpResponse httpResponse = client.doAction(describeLiveSnapshotConfigRequest);
			System.out.println(httpResponse.getUrl());
			System.out.println(new String(httpResponse.getHttpContent()));
			DescribeLiveSnapshotConfigResponse describeLiveSnapshotConfigResponse = client
					.getAcsResponse(describeLiveSnapshotConfigRequest);
			// todo something.
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 如果是连麦直播间 先推送到阿里云服务器上 生成主流和观众流地址 主流
	 */
	/**
	 * STEP0 获取在线观众人数[Socket的，即手机端的]
	 */
	public void getOnlineAudienceList(HttpServletRequest request, HttpServletResponse response) {

	}

	/**
	 * STEP1 邀请某个观众进行连麦 inviterType 邀请是否为观众 #1是观众 2 主播
	 */
	@RequestMapping(value = "invite.jspx")
	public void inviteAudience(String inviterUid, String inviteeUids, Integer liveRoomId, Integer inviterType) {
		// 推送消息给某个用户让其同意，并将主流地址推送给观众

	}

	/**
	 * STEP2 被邀请的人是否同意连麦 status #1同意 2 不同意 inviterType 邀请是否为观众 #1是观众 2 主播
	 * inviteeType 被邀请是否为观众 #1是观众 2 主播
	 */
	@RequestMapping(value = "feedback.jspx")
	public void feedback(String inviterUid, String inviteeUid, Integer status, Integer inviterType,
			Integer inviteeType) {
		// 如果同意了生成副流地址给客户端,同时通知邀请人副流地址
		// 邀请端照常进行主流推流，但是拿到的副流进行拉流播放
		// 被邀请端进行副流推流，主流拉流播放

	}

	/**
	 * 结束连麦
	 */
	@RequestMapping(value = "close.jspx")
	public void close() {

	}

}
