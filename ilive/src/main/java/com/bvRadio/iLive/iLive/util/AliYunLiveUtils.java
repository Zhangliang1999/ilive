package com.bvRadio.iLive.iLive.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.live.model.v20161101.AddLiveAppRecordConfigRequest;
import com.aliyuncs.live.model.v20161101.AddLiveAppRecordConfigRequest.RecordFormat;
import com.aliyuncs.live.model.v20161101.AddLiveAppSnapshotConfigRequest;
import com.aliyuncs.live.model.v20161101.AddLiveDetectNotifyConfigRequest;
import com.aliyuncs.live.model.v20161101.AddLiveSnapshotDetectPornConfigRequest;
import com.aliyuncs.live.model.v20161101.AddLiveStreamTranscodeRequest;
import com.aliyuncs.live.model.v20161101.AddMultipleStreamMixServiceRequest;
import com.aliyuncs.live.model.v20161101.CreateLiveStreamRecordIndexFilesRequest;
import com.aliyuncs.live.model.v20161101.DeleteLiveAppSnapshotConfigRequest;
import com.aliyuncs.live.model.v20161101.DeleteLiveStreamTranscodeRequest;
import com.aliyuncs.live.model.v20161101.DescribeLiveSnapshotConfigRequest;
import com.aliyuncs.live.model.v20161101.DescribeLiveStreamOnlineUserNumRequest;
import com.aliyuncs.live.model.v20161101.DescribeLiveStreamRecordContentRequest;
import com.aliyuncs.live.model.v20161101.DescribeLiveStreamRecordIndexFileRequest;
import com.aliyuncs.live.model.v20161101.DescribeLiveStreamRecordIndexFilesRequest;
import com.aliyuncs.live.model.v20161101.DescribeLiveStreamSnapshotInfoRequest;
import com.aliyuncs.live.model.v20161101.DescribeLiveStreamTranscodeInfoRequest;
import com.aliyuncs.live.model.v20161101.DescribeLiveStreamsBlockListRequest;
import com.aliyuncs.live.model.v20161101.DescribeLiveStreamsControlHistoryRequest;
import com.aliyuncs.live.model.v20161101.DescribeLiveStreamsFrameRateAndBitRateDataRequest;
import com.aliyuncs.live.model.v20161101.DescribeLiveStreamsOnlineListRequest;
import com.aliyuncs.live.model.v20161101.DescribeLiveStreamsPublishListRequest;
import com.aliyuncs.live.model.v20161101.ForbidLiveStreamRequest;
import com.aliyuncs.live.model.v20161101.RemoveMultipleStreamMixServiceRequest;
import com.aliyuncs.live.model.v20161101.ResumeLiveStreamRequest;
import com.aliyuncs.live.model.v20161101.SetLiveStreamsNotifyUrlConfigRequest;
import com.aliyuncs.live.model.v20161101.StartMultipleStreamMixServiceRequest;
import com.aliyuncs.live.model.v20161101.StopMultipleStreamMixServiceRequest;
import com.aliyuncs.live.model.v20161101.UpdateLiveAppSnapshotConfigRequest;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

/**
 * @author ysf
 */
public class AliYunLiveUtils {
	private static final Logger log = LoggerFactory.getLogger(AliYunLiveUtils.class);

	private static DefaultAcsClient client;

	/**
	 * 初始化Client
	 * 
	 * @return
	 * @throws ClientException
	 */
	private static DefaultAcsClient init() throws ClientException {
		if (null == client) {
			// IClientProfile profile = DefaultProfile.getProfile("cn-beijing",
			// "LTAIklG1gQSACRsA", "K3mHEP3XNsit5lGRXTywPkeRdqX9DL");//默认的地理信息，
			// DefaultProfile.addEndpoint("cn-shanghai", "cn-shanghai", "live",
			// "live.aliyuncs.com"); //添加自定义endpoint。
			String accessKeyId = ConfigUtils.get("aliyun_live_access_key_id");
			String secret = ConfigUtils.get("aliyun_live_secert");
			IClientProfile profile = DefaultProfile.getProfile("cn-shanghai", accessKeyId, secret);
			client = new DefaultAcsClient(profile);
			// System.setProperty("http.proxyHost", "127.0.0.1");
			// //此设置用于设置代理，可用fiddler拦截查看http请求，便于调试。
			// System.setProperty("http.proxyPort", "8888");
		}
		return client;
	}

	/**
	 * 查询摸个域名下的所有推流信息
	 * 
	 * @param domainName
	 * @param appName
	 * @param streamName
	 * @param startTime
	 * @param endTime
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	public static String describeLiveStreamsPublishListSample(String domainName, String appName, String streamName,
			String startTime, String endTime, Long pageSize, Long pageNumber) {

		try {
			DescribeLiveStreamsPublishListRequest describeLiveStreamsPublishListRequest = new DescribeLiveStreamsPublishListRequest();
			if (StringUtils.isBlank(domainName) || StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
				return "缺少必要参数";
			} else {
				describeLiveStreamsPublishListRequest.setActionName("DescribeLiveStreamsPublishList");
				describeLiveStreamsPublishListRequest.setDomainName(domainName);
				describeLiveStreamsPublishListRequest.setStartTime(startTime);
				describeLiveStreamsPublishListRequest.setEndTime(endTime);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				describeLiveStreamsPublishListRequest.setAcceptFormat(FormatType.JSON);
				describeLiveStreamsPublishListRequest.setVersion("2016-11-01");
			}
			if (!StringUtils.isBlank(appName)) {
				describeLiveStreamsPublishListRequest.setAppName(appName);
			}
			if (!StringUtils.isBlank(streamName)) {
				describeLiveStreamsPublishListRequest.setStreamName(streamName);
			}
			if (null != pageSize) {
				describeLiveStreamsPublishListRequest.setPageSize(pageSize);
			}
			if (null != pageNumber) {
				describeLiveStreamsPublishListRequest.setPageSize(pageNumber);
			}

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(describeLiveStreamsPublishListRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				log.info("describeLiveStreamsPublishListSample response :{}", str);
			} catch (UnsupportedEncodingException e) {
				log.warn("describeLiveStreamsPublishListSample error", e);
			}
			return str;
		} catch (ServerException e) {
			log.warn("describeLiveStreamsPublishListSample error", e);
			return null;
		} catch (ClientException e) {
			log.warn("describeLiveStreamsPublishListSample error", e);
			return null;
		}
	}

	/**
	 * 查看指定域名下（或者指定域名下某个应用）的所有正在推的流的信息
	 * 
	 * @param domainName
	 * @param appName
	 * @return
	 */
	public static String describeLiveStreamsOnlineListSample(String domainName, String appName) {

		try {
			DescribeLiveStreamsOnlineListRequest describeLiveStreamsOnlineListRequest = new DescribeLiveStreamsOnlineListRequest();
			if (StringUtils.isBlank(domainName)) {
				return "缺少必要参数";
			} else {
				describeLiveStreamsOnlineListRequest.setActionName("DescribeLiveStreamsOnlineList");
				describeLiveStreamsOnlineListRequest.setDomainName(domainName);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				describeLiveStreamsOnlineListRequest.setAcceptFormat(FormatType.JSON);
				describeLiveStreamsOnlineListRequest.setVersion("2016-11-01");
			}
			if (!StringUtils.isBlank(appName)) {
				describeLiveStreamsOnlineListRequest.setAppName(appName);
			}

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(describeLiveStreamsOnlineListRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				log.info("describeLiveStreamsOnlineListSample response :{}", str);
			} catch (UnsupportedEncodingException e) {
				log.warn("describeLiveStreamsOnlineListSample error", e);
			}
			return str;
		} catch (ServerException e) {
			log.warn("describeLiveStreamsOnlineListSample error", e);
			return null;
		} catch (ClientException e) {
			log.warn("describeLiveStreamsOnlineListSample error", e);
			return null;
		}
	}

	/**
	 * 获取流播放的黑名单
	 * 
	 * @param domainName
	 * @return
	 */
	public static String describeLiveStreamsBlockListSample(String domainName) {

		try {
			DescribeLiveStreamsBlockListRequest describeLiveStreamsBlockListRequest = new DescribeLiveStreamsBlockListRequest();
			if (StringUtils.isBlank(domainName)) {
				return "缺少必要参数";
			} else {
				describeLiveStreamsBlockListRequest.setActionName("DescribeLiveStreamsBlockList");
				describeLiveStreamsBlockListRequest.setDomainName(domainName);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				describeLiveStreamsBlockListRequest.setAcceptFormat(FormatType.JSON);
				describeLiveStreamsBlockListRequest.setVersion("2016-11-01");
			}

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(describeLiveStreamsBlockListRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				log.info("describeLiveStreamsBlockListSample response :{}", str);
			} catch (UnsupportedEncodingException e) {
				log.warn("describeLiveStreamsBlockListSample error", e);
			}
			return str;
		} catch (ServerException e) {
			log.warn("describeLiveStreamsBlockListSample error", e);
			return null;
		} catch (ClientException e) {
			log.warn("describeLiveStreamsBlockListSample error", e);
			return null;
		}
	}

	/**
	 * 查看某个域名下所有流控记录
	 * 
	 * @param domainName
	 * @param appName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static String describeLiveStreamsControlHistorySample(String domainName, String appName, String startTime,
			String endTime) {

		try {
			DescribeLiveStreamsControlHistoryRequest describeLiveStreamsControlHistoryRequest = new DescribeLiveStreamsControlHistoryRequest();
			if (StringUtils.isBlank(domainName) || StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
				return "缺少必要参数";
			} else {
				describeLiveStreamsControlHistoryRequest.setActionName("DescribeLiveStreamsControlHistory");
				describeLiveStreamsControlHistoryRequest.setDomainName(domainName);
				describeLiveStreamsControlHistoryRequest.setStartTime(startTime);
				describeLiveStreamsControlHistoryRequest.setEndTime(endTime);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				describeLiveStreamsControlHistoryRequest.setAcceptFormat(FormatType.JSON);
				describeLiveStreamsControlHistoryRequest.setVersion("2016-11-01");
			}
			if (!StringUtils.isBlank(appName)) {
				describeLiveStreamsControlHistoryRequest.setAppName(appName);
			}

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(describeLiveStreamsControlHistoryRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				log.info("describeLiveStreamsControlHistorySample response :{}", str);
			} catch (UnsupportedEncodingException e) {
				log.warn("describeLiveStreamsControlHistorySample error", e);
			}
			return str;
		} catch (ServerException e) {
			log.warn("describeLiveStreamsControlHistorySample error", e);
			return null;
		} catch (ClientException e) {
			log.warn("describeLiveStreamsControlHistorySample error", e);
			return null;
		}
	}

	/**
	 * 获取直播流的在线人数，支持基于域名和基于流的查询
	 * 
	 * @param domainName
	 * @param appName
	 * @param streamName
	 * @return
	 */
	public static String describeLiveStreamOnlineUserNumSample(String domainName, String appName, String streamName) {

		try {
			DescribeLiveStreamOnlineUserNumRequest describeLiveStreamOnlineUserNumRequest = new DescribeLiveStreamOnlineUserNumRequest();
			if (StringUtils.isBlank(domainName)) {
				return "缺少必要参数";
			} else {
				describeLiveStreamOnlineUserNumRequest.setActionName("DescribeLiveStreamOnlineUserNum");
				describeLiveStreamOnlineUserNumRequest.setDomainName(domainName);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				describeLiveStreamOnlineUserNumRequest.setAcceptFormat(FormatType.JSON);
				describeLiveStreamOnlineUserNumRequest.setVersion("2016-11-01");
			}
			if (!StringUtils.isBlank(appName)) {
				describeLiveStreamOnlineUserNumRequest.setAppName(appName);
			}
			if (!StringUtils.isBlank(streamName)) {
				describeLiveStreamOnlineUserNumRequest.setStreamName(streamName);
			}

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(describeLiveStreamOnlineUserNumRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				log.info("describeLiveStreamOnlineUserNumSample response :{}", str);
			} catch (UnsupportedEncodingException e) {
				log.warn("describeLiveStreamOnlineUserNumSample error", e);
			}
			return str;
		} catch (ServerException e) {
			log.warn("describeLiveStreamOnlineUserNumSample error", e);
			return null;
		} catch (ClientException e) {
			log.warn("describeLiveStreamOnlineUserNumSample error", e);
			return null;
		}
	}

	/**
	 * 停止直播流
	 * 
	 * @param domainName
	 * @param appName
	 * @param streamName
	 * @param resumeTime
	 * @return
	 */
	public static String forbidLiveStreamSample(String domainName, String appName, String streamName,
			String resumeTime) {

		try {
			ForbidLiveStreamRequest forbidLiveStreamRequest = new ForbidLiveStreamRequest();
			if (StringUtils.isBlank(domainName) || StringUtils.isBlank(appName)) {
				return "缺少必要参数";
			} else {
				forbidLiveStreamRequest.setActionName("ForbidLiveStream");
				forbidLiveStreamRequest.setDomainName(domainName);
				forbidLiveStreamRequest.setAppName(appName);
				forbidLiveStreamRequest.setLiveStreamType("publisher");
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				forbidLiveStreamRequest.setAcceptFormat(FormatType.JSON);
				forbidLiveStreamRequest.setVersion("2016-11-01");
			}
			if (!StringUtils.isBlank(resumeTime)) {
				forbidLiveStreamRequest.setResumeTime(resumeTime);
			}

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(forbidLiveStreamRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				log.info("forbidLiveStreamSample response :{}", str);
			} catch (UnsupportedEncodingException e) {
				log.warn("forbidLiveStreamSample error", e);
			}
			return str;
		} catch (ServerException e) {
			log.warn("forbidLiveStreamSample error", e);
			return null;
		} catch (ClientException e) {
			log.warn("forbidLiveStreamSample error", e);
			return null;
		}
	}

	/**
	 * 开始直播流
	 * 
	 * @param domainName
	 * @param appName
	 * @param streamName
	 * @return
	 */
	public static String resumeLiveStreamSample(String domainName, String appName, String streamName) {

		try {
			ResumeLiveStreamRequest resumeLiveStreamRequest = new ResumeLiveStreamRequest();
			if (StringUtils.isBlank(domainName) || StringUtils.isBlank(appName)) {
				return "缺少必要参数";
			} else {
				resumeLiveStreamRequest.setActionName("ResumeLiveStream");
				resumeLiveStreamRequest.setDomainName(domainName);
				resumeLiveStreamRequest.setAppName(appName);
				resumeLiveStreamRequest.setLiveStreamType("publisher");
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				resumeLiveStreamRequest.setAcceptFormat(FormatType.JSON);
				resumeLiveStreamRequest.setVersion("2016-11-01");
			}

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(resumeLiveStreamRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				log.info("resumeLiveStreamSample response :{}", str);
			} catch (UnsupportedEncodingException e) {
				log.warn("resumeLiveStreamSample error", e);
			}
			return str;
		} catch (ServerException e) {
			log.warn("resumeLiveStreamSample error", e);
			return null;
		} catch (ClientException e) {
			log.warn("resumeLiveStreamSample error", e);
			return null;
		}
	}

	/**
	 * 设置直播信息需要推送到的 URL 地址
	 * 
	 * @param domainName
	 * @param notifyUrl
	 * @return
	 */
	public static String setLiveStreamsNotifyUrlConfigSample(String domainName, String notifyUrl) {
		try {
			SetLiveStreamsNotifyUrlConfigRequest setLiveStreamsNotifyUrlConfigRequest = new SetLiveStreamsNotifyUrlConfigRequest();
			if (StringUtils.isBlank(domainName) || StringUtils.isBlank(notifyUrl)) {
				return "缺少必要参数";
			} else {
				setLiveStreamsNotifyUrlConfigRequest.setActionName("SetLiveStreamsNotifyUrlConfig");
				setLiveStreamsNotifyUrlConfigRequest.setDomainName(domainName);
				setLiveStreamsNotifyUrlConfigRequest.setNotifyUrl(notifyUrl);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				setLiveStreamsNotifyUrlConfigRequest.setAcceptFormat(FormatType.JSON);
				// setLiveStreamsNotifyUrlConfigRequest.setVersion("2016-11-01");
			}

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(setLiveStreamsNotifyUrlConfigRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				log.info("setLiveStreamsNotifyUrlConfigSample response :{}", str);
			} catch (UnsupportedEncodingException e) {
				log.warn("setLiveStreamsNotifyUrlConfigSample error", e);
			}
			return str;
		} catch (ServerException e) {
			log.warn("setLiveStreamsNotifyUrlConfigSample error", e);
			return null;
		} catch (ClientException e) {
			log.warn("setLiveStreamsNotifyUrlConfigSample error", e);
			return null;
		}
	}

	/**
	 * 配置 APP 录制，输出内容保存到 OSS 中
	 * 
	 * @param domainName
	 * @param appName
	 * @param ossEndpoint
	 * @param ossBucket
	 * @param recordFormatFormt
	 * @param recordFormatOssObjectPrefix
	 * @param recordFormatSliceOssObjectPrefix
	 * @param recordFormatCycleDuration
	 * @return
	 */
	public static String addLiveAppRecordConfigSample(String domainName, String appName, String ossEndpoint,
			String ossBucket, String recordFormatFormt, String recordFormatOssObjectPrefix,
			String recordFormatSliceOssObjectPrefix, Integer recordFormatCycleDuration) {
		List<RecordFormat> list = new ArrayList<RecordFormat>();
		RecordFormat rf = new RecordFormat();
		try {
			AddLiveAppRecordConfigRequest addLiveAppRecordConfigRequest = new AddLiveAppRecordConfigRequest();
			if (StringUtils.isBlank(domainName) || StringUtils.isBlank(appName) || StringUtils.isBlank(ossEndpoint)
					|| StringUtils.isBlank(ossBucket) || StringUtils.isBlank(recordFormatFormt)
					|| StringUtils.isBlank(recordFormatOssObjectPrefix)) {
				return "缺少必要参数";
			} else {
				rf.setOssObjectPrefix(recordFormatOssObjectPrefix);
				rf.setFormat(recordFormatFormt);
				addLiveAppRecordConfigRequest.setActionName("AddLiveAppRecordConfig");
				addLiveAppRecordConfigRequest.setDomainName(domainName);
				addLiveAppRecordConfigRequest.setAppName(appName);
				addLiveAppRecordConfigRequest.setOssEndpoint(ossEndpoint);
				addLiveAppRecordConfigRequest.setOssBucket(ossBucket);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				addLiveAppRecordConfigRequest.setAcceptFormat(FormatType.JSON);
				addLiveAppRecordConfigRequest.setVersion("2016-11-01");
			}
			if (!StringUtils.isBlank(recordFormatSliceOssObjectPrefix)) {
				rf.setSliceOssObjectPrefix(recordFormatSliceOssObjectPrefix);
			}
			if (null != recordFormatCycleDuration) {
				rf.setCycleDuration(recordFormatCycleDuration);
			}
			list.add(rf);
			addLiveAppRecordConfigRequest.setRecordFormats(list);

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(addLiveAppRecordConfigRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				log.info("addLiveAppRecordConfigSample response :{}", str);
			} catch (UnsupportedEncodingException e) {
				log.warn("addLiveAppRecordConfigSample error", e);
			}
			return str;
		} catch (ServerException e) {
			log.warn("addLiveAppRecordConfigSample error", e);
			return null;
		} catch (ClientException e) {
			log.warn("addLiveAppRecordConfigSample error", e);
			return null;
		}
	}

	/**
	 * 创建录制索引
	 * 
	 * @param domainName
	 * @param appName
	 * @param streamName
	 * @param startTime
	 * @param endTime
	 * @param ossBucket
	 * @param ossObject
	 * @param ossEndpoint
	 * @return
	 */
	public static String createLiveStreamRecordIndexFilesSample(String domainName, String appName, String streamName,
			String startTime, String endTime, String ossBucket, String ossObject, String ossEndpoint) {

		try {
			CreateLiveStreamRecordIndexFilesRequest createLiveStreamRecordIndexFilesRequest = new CreateLiveStreamRecordIndexFilesRequest();
			if (StringUtils.isBlank(domainName) || StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)
					|| StringUtils.isBlank(appName) || StringUtils.isBlank(streamName) || StringUtils.isBlank(ossBucket)
					|| StringUtils.isBlank(ossObject) || StringUtils.isBlank(ossEndpoint)) {
				return "缺少必要参数";
			} else {
				createLiveStreamRecordIndexFilesRequest.setActionName("CreateLiveStreamRecordIndexFiles");
				createLiveStreamRecordIndexFilesRequest.setDomainName(domainName);
				createLiveStreamRecordIndexFilesRequest.setStartTime(startTime);
				createLiveStreamRecordIndexFilesRequest.setEndTime(endTime);
				createLiveStreamRecordIndexFilesRequest.setAppName(appName);
				createLiveStreamRecordIndexFilesRequest.setStreamName(streamName);
				createLiveStreamRecordIndexFilesRequest.setOssBucket(ossBucket);
				createLiveStreamRecordIndexFilesRequest.setOssEndpoint(ossEndpoint);
				createLiveStreamRecordIndexFilesRequest.setOssObject(ossObject);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				createLiveStreamRecordIndexFilesRequest.setAcceptFormat(FormatType.JSON);
				createLiveStreamRecordIndexFilesRequest.setVersion("2016-11-01");
			}

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(createLiveStreamRecordIndexFilesRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				log.info("createLiveStreamRecordIndexFilesSample response :{}", str);
			} catch (UnsupportedEncodingException e) {
				log.warn("createLiveStreamRecordIndexFilesSample error", e);
			}
			return str;
		} catch (ServerException e) {
			log.warn("createLiveStreamRecordIndexFilesSample error", e);
			return null;
		} catch (ClientException e) {
			log.warn("createLiveStreamRecordIndexFilesSample error", e);
			return null;
		}
	}

	/**
	 * 查询某路直播流录制内容
	 * 
	 * @param domainName
	 * @param appName
	 * @param streamName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static String describeLiveStreamRecordContentSample(String domainName, String appName, String streamName,
			String startTime, String endTime) {

		try {
			DescribeLiveStreamRecordContentRequest describeLiveStreamRecordContentRequest = new DescribeLiveStreamRecordContentRequest();
			if (StringUtils.isBlank(domainName) || StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)
					|| StringUtils.isBlank(appName) || StringUtils.isBlank(streamName)) {
				return "缺少必要参数";
			} else {
				describeLiveStreamRecordContentRequest.setActionName("DescribeLiveStreamRecordContent");
				describeLiveStreamRecordContentRequest.setDomainName(domainName);
				describeLiveStreamRecordContentRequest.setStartTime(startTime);
				describeLiveStreamRecordContentRequest.setEndTime(endTime);
				describeLiveStreamRecordContentRequest.setAppName(appName);
				describeLiveStreamRecordContentRequest.setStreamName(streamName);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				describeLiveStreamRecordContentRequest.setAcceptFormat(FormatType.JSON);
				describeLiveStreamRecordContentRequest.setVersion("2016-11-01");
			}

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(describeLiveStreamRecordContentRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				log.info("describeLiveStreamRecordContentSample response :{}", str);
			} catch (UnsupportedEncodingException e) {
				log.warn("describeLiveStreamRecordContentSample error", e);
			}
			return str;
		} catch (ServerException e) {
			log.warn("describeLiveStreamRecordContentSample error", e);
			return null;
		} catch (ClientException e) {
			log.warn("describeLiveStreamRecordContentSample error", e);
			return null;
		}
	}

	/**
	 * 查询单个录制索引文件
	 * 
	 * @param domainName
	 * @param appName
	 * @param streamName
	 * @param recordID
	 * @return
	 */
	public static String describeLiveStreamRecordIndexFileSample(String domainName, String appName, String streamName,
			String recordID) {

		try {
			DescribeLiveStreamRecordIndexFileRequest describeLiveStreamRecordIndexFile = new DescribeLiveStreamRecordIndexFileRequest();
			if (StringUtils.isBlank(domainName) || StringUtils.isBlank(recordID) || StringUtils.isBlank(appName)
					|| StringUtils.isBlank(streamName)) {
				return "缺少必要参数";
			} else {
				describeLiveStreamRecordIndexFile.setActionName("DescribeLiveStreamRecordIndexFile");
				describeLiveStreamRecordIndexFile.setDomainName(domainName);
				describeLiveStreamRecordIndexFile.setRecordId(recordID);
				describeLiveStreamRecordIndexFile.setStreamName(streamName);
				describeLiveStreamRecordIndexFile.setAppName(appName);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				describeLiveStreamRecordIndexFile.setAcceptFormat(FormatType.JSON);
				describeLiveStreamRecordIndexFile.setVersion("2016-11-01");
			}

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(describeLiveStreamRecordIndexFile);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				log.info("describeLiveStreamRecordIndexFileSample response :{}", str);
			} catch (UnsupportedEncodingException e) {
				log.warn("describeLiveStreamRecordIndexFileSample error", e);
			}
			return str;
		} catch (ServerException e) {
			log.warn("describeLiveStreamRecordIndexFileSample error", e);
			return null;
		} catch (ClientException e) {
			log.warn("describeLiveStreamRecordIndexFileSample error", e);
			return null;
		}
	}

	/**
	 * 查询录制索引文件
	 * 
	 * @param domainName
	 * @param appName
	 * @param streamName
	 * @param startTime
	 * @param endTime
	 * @param pageSize
	 * @param pageNumber
	 * @param order
	 * @return
	 */
	public static String describeLiveStreamRecordIndexFilesSample(String domainName, String appName, String streamName,
			String startTime, String endTime, Integer pageSize, Integer pageNumber, String order) {
		try {
			DescribeLiveStreamRecordIndexFilesRequest describeLiveStreamRecordIndexFilesRequest = new DescribeLiveStreamRecordIndexFilesRequest();
			if (StringUtils.isBlank(domainName) || StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)
					|| StringUtils.isBlank(appName) || StringUtils.isBlank(streamName)) {
				return "缺少必要参数";
			} else {
				describeLiveStreamRecordIndexFilesRequest.setActionName("DescribeLiveStreamRecordIndexFiles");
				describeLiveStreamRecordIndexFilesRequest.setDomainName(domainName);
				describeLiveStreamRecordIndexFilesRequest.setStartTime(startTime);
				describeLiveStreamRecordIndexFilesRequest.setEndTime(endTime);
				describeLiveStreamRecordIndexFilesRequest.setAppName(appName);
				describeLiveStreamRecordIndexFilesRequest.setStreamName(streamName);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				describeLiveStreamRecordIndexFilesRequest.setAcceptFormat(FormatType.JSON);
				// describeLiveStreamRecordIndexFilesRequest.setVersion("2016-11-01");
			}
			if (null != pageSize) {
				describeLiveStreamRecordIndexFilesRequest.setPageSize(pageSize);
			}
			if (null != pageNumber) {
				describeLiveStreamRecordIndexFilesRequest.setPageSize(pageNumber);
			}
			if (StringUtils.isBlank(order)) {
				describeLiveStreamRecordIndexFilesRequest.setOrder(order);
			}

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(describeLiveStreamRecordIndexFilesRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				log.info("describeLiveStreamRecordIndexFilesSample response :{}", str);
			} catch (UnsupportedEncodingException e) {
				log.warn("describeLiveStreamRecordIndexFilesSample error", e);
			}
			return str;
		} catch (ServerException e) {
			log.warn("describeLiveStreamRecordIndexFilesSample error", e);
			return null;
		} catch (ClientException e) {
			log.warn("describeLiveStreamRecordIndexFilesSample error", e);
			return null;
		}
	}

	/**
	 * 获取直播流的帧率和码率，支持基于域名和基于流的查询
	 * 
	 * @param domainName
	 * @param appName
	 * @param streamName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static String describeLiveStreamsFrameRateAndBitRateDataSample(String domainName, String appName,
			String streamName, String startTime, String endTime) {
		try {
			DescribeLiveStreamsFrameRateAndBitRateDataRequest describeLiveStreamsFrameRateAndBitRateDataRequest = new DescribeLiveStreamsFrameRateAndBitRateDataRequest();
			if (StringUtils.isBlank(domainName) || StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
				return "缺少必要参数";
			} else {
				describeLiveStreamsFrameRateAndBitRateDataRequest.setActionName("DescribeLiveStreamBitRateData");
				describeLiveStreamsFrameRateAndBitRateDataRequest.setDomainName(domainName);
				describeLiveStreamsFrameRateAndBitRateDataRequest.setStartTime(startTime);
				describeLiveStreamsFrameRateAndBitRateDataRequest.setEndTime(endTime);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				describeLiveStreamsFrameRateAndBitRateDataRequest.setAcceptFormat(FormatType.JSON);
				describeLiveStreamsFrameRateAndBitRateDataRequest.setVersion("2016-11-01");
			}
			if (StringUtils.isBlank(appName)) {
				describeLiveStreamsFrameRateAndBitRateDataRequest.setAppName(appName);
			}
			if (StringUtils.isBlank(streamName)) {
				describeLiveStreamsFrameRateAndBitRateDataRequest.setStreamName(streamName);
			}

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(describeLiveStreamsFrameRateAndBitRateDataRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				log.info("describeLiveStreamsFrameRateAndBitRateDataSample response :{}", str);
			} catch (UnsupportedEncodingException e) {
				log.warn("describeLiveStreamsFrameRateAndBitRateDataSample error", e);
			}
			return str;
		} catch (ServerException e) {
			log.warn("describeLiveStreamsFrameRateAndBitRateDataSample error", e);
			return null;
		} catch (ClientException e) {
			log.warn("describeLiveStreamsFrameRateAndBitRateDataSample error", e);
			return null;
		}
	}

	/**
	 * 添加转码配置
	 * 
	 * @param domainName
	 * @param appName
	 * @param template
	 * @return
	 */
	public static String addLiveStreamTranscodeSample(String domainName, String appName, String template) {

		try {
			AddLiveStreamTranscodeRequest addLiveStreamTranscodeRequest = new AddLiveStreamTranscodeRequest();
			if (StringUtils.isBlank(domainName) || StringUtils.isBlank(appName) || StringUtils.isBlank(template)) {
				return "缺少必要参数";
			} else {
				addLiveStreamTranscodeRequest.setActionName("AddLiveStreamTranscode");
				addLiveStreamTranscodeRequest.setDomain(domainName);
				addLiveStreamTranscodeRequest.setApp(appName);
				addLiveStreamTranscodeRequest.setTemplate(template);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				addLiveStreamTranscodeRequest.setAcceptFormat(FormatType.JSON);
				addLiveStreamTranscodeRequest.setVersion("2016-11-01");
			}

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(addLiveStreamTranscodeRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				log.info("addLiveStreamTranscodeSample response :{}", str);
			} catch (UnsupportedEncodingException e) {
				log.warn("addLiveStreamTranscodeSample error", e);
			}
			return str;
		} catch (ServerException e) {
			log.warn("addLiveStreamTranscodeSample error", e);
			return null;
		} catch (ClientException e) {
			log.warn("addLiveStreamTranscodeSample error", e);
			return null;
		}
	}

	/**
	 * 删除转码配置
	 * 
	 * @param domainName
	 * @param appName
	 * @param template
	 * @return
	 */
	public static String deleteLiveStreamTranscodeSample(String domainName, String appName, String template) {

		try {
			DeleteLiveStreamTranscodeRequest deleteLiveStreamTranscode = new DeleteLiveStreamTranscodeRequest();
			if (StringUtils.isBlank(domainName) || StringUtils.isBlank(appName) || StringUtils.isBlank(template)) {
				return "缺少必要参数";
			} else {
				deleteLiveStreamTranscode.setActionName("DeleteLiveStreamTranscode");
				deleteLiveStreamTranscode.setDomain(domainName);
				deleteLiveStreamTranscode.setApp(appName);
				deleteLiveStreamTranscode.setTemplate(template);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				deleteLiveStreamTranscode.setAcceptFormat(FormatType.JSON);
				deleteLiveStreamTranscode.setVersion("2016-11-01");
			}

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(deleteLiveStreamTranscode);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				log.info("deleteLiveStreamTranscodeSample response :{}", str);
			} catch (UnsupportedEncodingException e) {
				log.warn("deleteLiveStreamTranscodeSample error", e);
			}
			return str;
		} catch (ServerException e) {
			log.warn("deleteLiveStreamTranscodeSample error", e);
			return null;
		} catch (ClientException e) {
			log.warn("deleteLiveStreamTranscodeSample error", e);
			return null;
		}
	}

	/**
	 * 查询转码配置
	 * 
	 * @param domainName
	 * @return
	 */
	public static String describeLiveStreamTranscodeInfoSample(String domainName) {

		try {
			DescribeLiveStreamTranscodeInfoRequest describeLiveStreamTranscodeInfo = new DescribeLiveStreamTranscodeInfoRequest();
			if (StringUtils.isBlank(domainName)) {
				return "缺少必要参数";
			} else {
				describeLiveStreamTranscodeInfo.setActionName("DescribeLiveStreamTranscodeInfo");
				describeLiveStreamTranscodeInfo.setDomainTranscodeName(domainName);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				describeLiveStreamTranscodeInfo.setAcceptFormat(FormatType.JSON);
				describeLiveStreamTranscodeInfo.setVersion("2016-11-01");
			}

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(describeLiveStreamTranscodeInfo);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				log.info("describeLiveStreamTranscodeInfoSample response :{}", str);
			} catch (UnsupportedEncodingException e) {
				log.warn("describeLiveStreamTranscodeInfoSample error", e);
			}
			return str;
		} catch (ServerException e) {
			log.warn("describeLiveStreamTranscodeInfoSample error", e);
			return null;
		} catch (ClientException e) {
			log.warn("describeLiveStreamTranscodeInfoSample error", e);
			return null;
		}
	}

	/**
	 * 添加截图配置
	 * 
	 * @param domainName
	 * @param appName
	 * @param timeInterval
	 * @param ossEndpoint
	 * @param ossBucket
	 * @param overwriteOSSObject
	 * @param sequenceOSSObject
	 * @return
	 */
	public static String addLiveAppSnapshotConfigSample(String domainName, String appName, Integer timeInterval,
			String ossEndpoint, String ossBucket, String overwriteOSSObject, String sequenceOSSObject) {

		try {
			AddLiveAppSnapshotConfigRequest addLiveAppSnapshotConfigRequest = new AddLiveAppSnapshotConfigRequest();
			if (StringUtils.isBlank(domainName) || StringUtils.isBlank(appName) || StringUtils.isBlank(ossEndpoint)
					|| StringUtils.isBlank(ossBucket) || null == timeInterval) {
				return "缺少必要参数";
			} else {
				addLiveAppSnapshotConfigRequest.setActionName("AddLiveAppSnapshotConfig");
				addLiveAppSnapshotConfigRequest.setDomainName(domainName);
				addLiveAppSnapshotConfigRequest.setAppName(appName);
				addLiveAppSnapshotConfigRequest.setTimeInterval(timeInterval);
				addLiveAppSnapshotConfigRequest.setOssEndpoint(ossEndpoint);
				addLiveAppSnapshotConfigRequest.setOssBucket(ossBucket);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				addLiveAppSnapshotConfigRequest.setAcceptFormat(FormatType.JSON);
				addLiveAppSnapshotConfigRequest.setVersion("2016-11-01");
			}
			if (!StringUtils.isBlank(overwriteOSSObject)) {
				addLiveAppSnapshotConfigRequest.setOverwriteOssObject(overwriteOSSObject);
			}
			if (!StringUtils.isBlank(sequenceOSSObject)) {
				addLiveAppSnapshotConfigRequest.setSequenceOssObject(sequenceOSSObject);
			}

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(addLiveAppSnapshotConfigRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				log.info("addLiveAppSnapshotConfigSample response :{}", str);
			} catch (UnsupportedEncodingException e) {
				log.warn("addLiveAppSnapshotConfigSample error", e);
			}
			return str;
		} catch (ServerException e) {
			log.warn("addLiveAppSnapshotConfigSample error", e);
			return null;
		} catch (ClientException e) {
			log.warn("addLiveAppSnapshotConfigSample error", e);
			return null;
		}
	}

	/**
	 * 删除截图配置
	 * 
	 * @param domainName
	 * @param appName
	 * @return
	 */
	public static String deleteLiveAppSnapshotConfigSample(String domainName, String appName) {

		try {
			DeleteLiveAppSnapshotConfigRequest deleteLiveAppSnapshotConfig = new DeleteLiveAppSnapshotConfigRequest();
			if (StringUtils.isBlank(domainName) || StringUtils.isBlank(appName)) {
				return "缺少必要参数";
			} else {
				deleteLiveAppSnapshotConfig.setActionName("DeleteLiveAppSnapshotConfig");
				deleteLiveAppSnapshotConfig.setDomainName(domainName);
				deleteLiveAppSnapshotConfig.setAppName(appName);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				deleteLiveAppSnapshotConfig.setAcceptFormat(FormatType.JSON);
				deleteLiveAppSnapshotConfig.setVersion("2016-11-01");
			}

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(deleteLiveAppSnapshotConfig);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				log.info("deleteLiveAppSnapshotConfigSample response :{}", str);
			} catch (UnsupportedEncodingException e) {
				log.warn("deleteLiveAppSnapshotConfigSample error", e);
			}
			return str;
		} catch (ServerException e) {
			log.warn("deleteLiveAppSnapshotConfigSample error", e);
			return null;
		} catch (ClientException e) {
			log.warn("deleteLiveAppSnapshotConfigSample error", e);
			return null;
		}
	}

	/**
	 * 更新截图配置
	 * 
	 * @param domainName
	 * @param appName
	 * @param timeInterval
	 * @param ossEndpoint
	 * @param ossBucket
	 * @param overwriteOSSObject
	 * @param sequenceOSSObject
	 * @return
	 */
	public static String updateLiveAppSnapshotConfigSample(String domainName, String appName, Integer timeInterval,
			String ossEndpoint, String ossBucket, String overwriteOSSObject, String sequenceOSSObject) {

		try {
			UpdateLiveAppSnapshotConfigRequest updateLiveAppSnapshotConfigRequest = new UpdateLiveAppSnapshotConfigRequest();
			if (StringUtils.isBlank(domainName) || StringUtils.isBlank(appName)) {
				return "缺少必要参数";
			} else {
				updateLiveAppSnapshotConfigRequest.setActionName("UpdateLiveAppSnapshotConfig");
				updateLiveAppSnapshotConfigRequest.setDomainName(domainName);
				updateLiveAppSnapshotConfigRequest.setAppName(appName);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				updateLiveAppSnapshotConfigRequest.setAcceptFormat(FormatType.JSON);
				updateLiveAppSnapshotConfigRequest.setVersion("2016-11-01");
			}
			if (null != timeInterval) {
				updateLiveAppSnapshotConfigRequest.setTimeInterval(timeInterval);
			}
			if (!StringUtils.isBlank(ossEndpoint)) {
				updateLiveAppSnapshotConfigRequest.setOssEndpoint(ossEndpoint);
			}
			if (!StringUtils.isBlank(ossBucket)) {
				updateLiveAppSnapshotConfigRequest.setOssBucket(ossBucket);
			}
			if (!StringUtils.isBlank(sequenceOSSObject)) {
				updateLiveAppSnapshotConfigRequest.setSequenceOssObject(sequenceOSSObject);
			}
			if (!StringUtils.isBlank(overwriteOSSObject)) {
				updateLiveAppSnapshotConfigRequest.setOverwriteOssObject(overwriteOSSObject);
			}

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(updateLiveAppSnapshotConfigRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				log.info("updateLiveAppSnapshotConfigSample response :{}", str);
			} catch (UnsupportedEncodingException e) {
				log.warn("updateLiveAppSnapshotConfigSample error", e);
			}
			return str;
		} catch (ServerException e) {
			log.warn("updateLiveAppSnapshotConfigSample error", e);
			return null;
		} catch (ClientException e) {
			log.warn("updateLiveAppSnapshotConfigSample error", e);
			return null;
		}
	}

	/**
	 * 查询域名截图配置
	 * 
	 * @param domainName
	 * @param appName
	 * @param pageSize
	 * @param pageNumber
	 * @param order
	 * @return
	 */
	public static String describeLiveSnapshotConfigSample(String domainName, String appName, Integer pageSize,
			Integer pageNumber, String order) {

		try {
			DescribeLiveSnapshotConfigRequest describeLiveSnapshotConfigRequest = new DescribeLiveSnapshotConfigRequest();
			if (StringUtils.isBlank(domainName)) {
				return "缺少必要参数";
			} else {
				describeLiveSnapshotConfigRequest.setActionName("DescribeLiveSnapshotConfig");
				describeLiveSnapshotConfigRequest.setDomainName(domainName);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				describeLiveSnapshotConfigRequest.setAcceptFormat(FormatType.JSON);
				describeLiveSnapshotConfigRequest.setVersion("2016-11-01");
			}
			if (StringUtils.isBlank(appName)) {
				describeLiveSnapshotConfigRequest.setAppName(appName);
			}
			if (null != pageSize) {
				describeLiveSnapshotConfigRequest.setPageSize(pageSize);
			}
			if (null != pageNumber) {
				describeLiveSnapshotConfigRequest.setPageSize(pageNumber);
			}
			if (StringUtils.isBlank(order)) {
				describeLiveSnapshotConfigRequest.setOrder(order);
			}

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(describeLiveSnapshotConfigRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				log.info("describeLiveSnapshotConfigSample response :{}", str);
			} catch (UnsupportedEncodingException e) {
				log.warn("describeLiveSnapshotConfigSample error", e);
			}
			return str;
		} catch (ServerException e) {
			log.warn("describeLiveSnapshotConfigSample error", e);
			return null;
		} catch (ClientException e) {
			log.warn("describeLiveSnapshotConfigSample error", e);
			return null;
		}
	}

	/**
	 * 查询截图信息
	 * 
	 * @param domainName
	 * @param appName
	 * @param streamName
	 * @param startTime
	 * @param endTime
	 * @param limit
	 * @return
	 */
	public static String describeLiveStreamSnapshotInfoSample(String domainName, String appName, String streamName,
			String startTime, String endTime, Integer limit) {

		try {
			DescribeLiveStreamSnapshotInfoRequest describeLiveStreamSnapshotInfoRequest = new DescribeLiveStreamSnapshotInfoRequest();
			if (StringUtils.isBlank(domainName) || StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)
					|| StringUtils.isBlank(appName) || StringUtils.isBlank(streamName)) {
				return "缺少必要参数";
			} else {
				describeLiveStreamSnapshotInfoRequest.setActionName("DescribeLiveStreamSnapshotInfo");
				describeLiveStreamSnapshotInfoRequest.setDomainName(domainName);
				describeLiveStreamSnapshotInfoRequest.setStartTime(startTime);
				describeLiveStreamSnapshotInfoRequest.setEndTime(endTime);
				describeLiveStreamSnapshotInfoRequest.setAppName(appName);
				describeLiveStreamSnapshotInfoRequest.setStreamName(streamName);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				describeLiveStreamSnapshotInfoRequest.setAcceptFormat(FormatType.JSON);
				describeLiveStreamSnapshotInfoRequest.setVersion("2016-11-01");
			}
			if (null != limit) {
				describeLiveStreamSnapshotInfoRequest.setLimit(limit);
			}

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(describeLiveStreamSnapshotInfoRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				log.info("describeLiveStreamSnapshotInfoSample response :{}", str);
			} catch (UnsupportedEncodingException e) {
				log.warn("describeLiveStreamSnapshotInfoSample error", e);
			}
			return str;
		} catch (ServerException e) {
			log.warn("describeLiveStreamSnapshotInfoSample error", e);
			return null;
		} catch (ClientException e) {
			log.warn("describeLiveStreamSnapshotInfoSample error", e);
			return null;
		}
	}

	/**
	 * 添加鉴黄配置
	 * 
	 * @param domainName
	 * @param appName
	 * @param ossEndpoint
	 * @param ossBucket
	 * @param ossObject
	 * @param interval
	 * @return
	 */
	public static String addLiveSnapshotDetectPornConfigSample(String domainName, String appName, String ossEndpoint,
			String ossBucket, String ossObject, Integer interval) {

		try {
			AddLiveSnapshotDetectPornConfigRequest addLiveSnapshotDetectPornConfig = new AddLiveSnapshotDetectPornConfigRequest();
			if (StringUtils.isBlank(domainName) || StringUtils.isBlank(appName) || StringUtils.isBlank(ossEndpoint)
					|| StringUtils.isBlank(ossBucket)) {
				return "缺少必要参数";
			} else {
				addLiveSnapshotDetectPornConfig.setDomainName(domainName);
				addLiveSnapshotDetectPornConfig.setOssEndpoint(ossEndpoint);
				addLiveSnapshotDetectPornConfig.setOssBucket(ossBucket);
				addLiveSnapshotDetectPornConfig.setAppName(appName);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				addLiveSnapshotDetectPornConfig.setAcceptFormat(FormatType.JSON);
				addLiveSnapshotDetectPornConfig.setVersion("2016-11-01");
			}
			if (null != interval) {
				addLiveSnapshotDetectPornConfig.setInterval(interval);
			}
			if (StringUtils.isBlank(ossObject)) {
				addLiveSnapshotDetectPornConfig.setOssObject(ossObject);
			}

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(addLiveSnapshotDetectPornConfig);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				log.info("addLiveSnapshotDetectPornConfigSample response :{}", str);
			} catch (UnsupportedEncodingException e) {
				log.warn("addLiveSnapshotDetectPornConfigSample error", e);
			}
			return str;
		} catch (ServerException e) {
			log.warn("addLiveSnapshotDetectPornConfigSample error", e);
			return null;
		} catch (ClientException e) {
			log.warn("addLiveSnapshotDetectPornConfigSample error", e);
			return null;
		}
	}

	/**
	 * 添加鉴黄回调
	 * 
	 * @param domainName
	 * @param notifyUrl
	 * @return
	 */
	public static String addLiveDetectNotifyConfigSample(String domainName, String notifyUrl) {

		try {
			AddLiveDetectNotifyConfigRequest addLiveDetectNotifyConfig = new AddLiveDetectNotifyConfigRequest();
			if (StringUtils.isBlank(domainName) || StringUtils.isBlank(notifyUrl)) {
				return "缺少必要参数";
			} else {
				addLiveDetectNotifyConfig.setDomainName(domainName);
				addLiveDetectNotifyConfig.setNotifyUrl(notifyUrl);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				addLiveDetectNotifyConfig.setAcceptFormat(FormatType.JSON);
				addLiveDetectNotifyConfig.setVersion("2016-11-01");
			}

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(addLiveDetectNotifyConfig);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				log.info("addLiveDetectNotifyConfigSample response :{}", str);
			} catch (UnsupportedEncodingException e) {
				log.warn("addLiveDetectNotifyConfigSample error", e);
			}
			return str;
		} catch (ServerException e) {
			log.warn("error", e);
			return null;
		} catch (ClientException e) {
			log.warn("error", e);
			return null;
		}
	}

	/**
	 * 开启混流服务
	 * 
	 * @param domainName
	 * @param appName
	 * @param roomId
	 * @return
	 */
	public static String startMultipleStreamMixService(String domainName, String appName, String roomId) {
		StartMultipleStreamMixServiceRequest request = new StartMultipleStreamMixServiceRequest();
		if (StringUtils.isBlank(domainName)) {
			return "缺少必要参数";
		} else {
			request.setActionName("StartMultipleStreamMixService");
			request.setDomainName(domainName);
			request.setAppName(appName);
			request.setStreamName(roomId);
			request.setMixTemplate("pip4a");
		}
		try {

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(request);
			String str = null;
			str = new String(httpResponse.getHttpContent(), "UTF-8");
			log.info("startMultipleStreamMixService response :{}", str);
		} catch (Exception e) {
			log.warn("startMultipleStreamMixService error", e);
		}
		return roomId;
	}

	/**
	 * 往主流中添加一路
	 * 
	 * @param domainName
	 * @param appName
	 * @param roomId
	 * @param mixRoomId
	 * @return
	 */
	public static String addMultipleStreamMixService(String domainName, String appName, String roomId,
			String mixRoomId) {
		AddMultipleStreamMixServiceRequest request = new AddMultipleStreamMixServiceRequest();
		if (StringUtils.isBlank(domainName)) {
			return "缺少必要参数";
		} else {
			request.setActionName("AddMultipleStreamMixService");
			request.setDomainName(domainName);
			request.setAppName(appName);
			request.setStreamName(roomId);
			request.setMixDomainName(domainName);
			request.setMixAppName(appName);
			request.setMixStreamName(mixRoomId);
		}
		try {

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(request);
			String str = null;
			str = new String(httpResponse.getHttpContent(), "UTF-8");
			log.info("addMultipleStreamMixService response :{}", str);
		} catch (UnsupportedEncodingException | ClientException e) {
			log.warn("addMultipleStreamMixService error", e);
		}
		return roomId;
	}

	/**
	 * 从主流中移除一路
	 * 
	 * @param domainName
	 * @param appName
	 * @param roomId
	 * @param mixRoomId
	 * @return
	 */
	public static String removeMultipleStreamMixService(String domainName, String appName, String roomId,
			String mixRoomId) {
		RemoveMultipleStreamMixServiceRequest request = new RemoveMultipleStreamMixServiceRequest();
		if (StringUtils.isBlank(domainName)) {
			return "缺少必要参数";
		} else {
			request.setActionName("RemoveMultipleStreamMixService");
			request.setDomainName(domainName);
			request.setAppName(appName);
			request.setStreamName(roomId);
			request.setMixDomainName(domainName);
			request.setMixAppName(appName);
			request.setMixStreamName(mixRoomId);
		}
		try {

			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(request);
			String str = null;
			str = new String(httpResponse.getHttpContent(), "UTF-8");
			log.info("removeMultipleStreamMixService response :{}", str);
		} catch (UnsupportedEncodingException | ClientException e) {
			log.warn("removeMultipleStreamMixService error", e);
		}
		return roomId;
	}

	/**
	 * 从停止混流服务
	 * 
	 * @param domainName
	 * @param appName
	 * @param roomId
	 * @return
	 */
	public static String stopMultipleStreamMixService(String domainName, String appName, String roomId) {
		StopMultipleStreamMixServiceRequest request = new StopMultipleStreamMixServiceRequest();
		if (StringUtils.isBlank(domainName)) {
			return "缺少必要参数";
		} else {
			request.setActionName("StopMultipleStreamMixService");
			request.setDomainName(domainName);
			request.setAppName(appName);
			request.setStreamName(roomId);
		}
		try {
			DefaultAcsClient client = init();
			HttpResponse httpResponse = client.doAction(request);
			String str = null;
			str = new String(httpResponse.getHttpContent(), "UTF-8");
			log.info("stopMultipleStreamMixService response :{}", str);
		} catch (UnsupportedEncodingException | ClientException e) {
			log.warn("stopMultipleStreamMixService error", e);
		}
		return roomId;
	}
}
