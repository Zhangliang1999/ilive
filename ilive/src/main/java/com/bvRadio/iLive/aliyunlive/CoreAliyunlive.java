package com.bvRadio.iLive.aliyunlive;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

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
import com.aliyuncs.live.model.v20161101.DescribeLiveSnapshotDetectPornConfigRequest;
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

public class CoreAliyunlive {
	/* private static DefaultAcsClient client; */
	// 初始化Client
	public DefaultAcsClient init() throws ClientException {
		// IClientProfile profile = DefaultProfile.getProfile("cn-beijing",
		// "LTAIklG1gQSACRsA", "K3mHEP3XNsit5lGRXTywPkeRdqX9DL");//默认的地理信息，
		IClientProfile profile = DefaultProfile.getProfile("cn-shanghai", "",
				"");
		// DefaultProfile.addEndpoint("cn-shanghai", "cn-shanghai", "live",
		// "live.aliyuncs.com"); //添加自定义endpoint。
		DefaultAcsClient client = new DefaultAcsClient(profile);
		return client;
		// System.setProperty("http.proxyHost", "127.0.0.1");
		// //此设置用于设置代理，可用fiddler拦截查看http请求，便于调试。
		// System.setProperty("http.proxyPort", "8888");
	}

	// 查询摸个域名下的所有推流信息
	public String DescribeLiveStreamsPublishListSample(String DomainName, String AppName, String StreamName,
			String StartTime, String EndTime, Long PageSize, Long PageNumber) {

		try {
			DescribeLiveStreamsPublishListRequest describeLiveStreamsPublishListRequest = new DescribeLiveStreamsPublishListRequest();
			if (StringUtils.isBlank(DomainName) || StringUtils.isBlank(StartTime) || StringUtils.isBlank(EndTime)) {
				return "缺少必要参数";
			} else {
				describeLiveStreamsPublishListRequest.setActionName("DescribeLiveStreamsPublishList");
				describeLiveStreamsPublishListRequest.setDomainName(DomainName);
				describeLiveStreamsPublishListRequest.setStartTime(StartTime);
				describeLiveStreamsPublishListRequest.setEndTime(EndTime);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				describeLiveStreamsPublishListRequest.setAcceptFormat(FormatType.JSON);
				describeLiveStreamsPublishListRequest.setVersion("2016-11-01");
			}
			if (!StringUtils.isBlank(AppName)) {
				describeLiveStreamsPublishListRequest.setAppName(AppName);
			}
			if (!StringUtils.isBlank(StreamName)) {
				describeLiveStreamsPublishListRequest.setStreamName(StreamName);
			}
			if (null != PageSize) {
				describeLiveStreamsPublishListRequest.setPageSize(PageSize);
			}
			if (null != PageNumber) {
				describeLiveStreamsPublishListRequest.setPageSize(PageNumber);
			}

			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(describeLiveStreamsPublishListRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				System.out.println(str);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return str;
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 查看指定域名下（或者指定域名下某个应用）的所有正在推的流的信息
	public String DescribeLiveStreamsOnlineListSample(String DomainName, String AppName) {

		try {
			DescribeLiveStreamsOnlineListRequest describeLiveStreamsOnlineListRequest = new DescribeLiveStreamsOnlineListRequest();
			if (StringUtils.isBlank(DomainName)) {
				return "缺少必要参数";
			} else {
				describeLiveStreamsOnlineListRequest.setActionName("DescribeLiveStreamsOnlineList");
				describeLiveStreamsOnlineListRequest.setDomainName(DomainName);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				describeLiveStreamsOnlineListRequest.setAcceptFormat(FormatType.JSON);
				describeLiveStreamsOnlineListRequest.setVersion("2016-11-01");
			}
			if (!StringUtils.isBlank(AppName)) {
				describeLiveStreamsOnlineListRequest.setAppName(AppName);
			}
			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(describeLiveStreamsOnlineListRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				System.out.println(str);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return str;
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 获取流播放的黑名单
	public String DescribeLiveStreamsBlockListSample(String DomainName) {

		try {
			DescribeLiveStreamsBlockListRequest describeLiveStreamsBlockListRequest = new DescribeLiveStreamsBlockListRequest();
			if (StringUtils.isBlank(DomainName)) {
				return "缺少必要参数";
			} else {
				describeLiveStreamsBlockListRequest.setActionName("DescribeLiveStreamsBlockList");
				describeLiveStreamsBlockListRequest.setDomainName(DomainName);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				describeLiveStreamsBlockListRequest.setAcceptFormat(FormatType.JSON);
				describeLiveStreamsBlockListRequest.setVersion("2016-11-01");
			}
			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(describeLiveStreamsBlockListRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				System.out.println(str);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return str;
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 查看某个域名下所有流控记录
	public String DescribeLiveStreamsControlHistorySample(String DomainName, String AppName, String StartTime,
			String EndTime) {

		try {
			DescribeLiveStreamsControlHistoryRequest describeLiveStreamsControlHistoryRequest = new DescribeLiveStreamsControlHistoryRequest();
			if (StringUtils.isBlank(DomainName) || StringUtils.isBlank(StartTime) || StringUtils.isBlank(EndTime)) {
				return "缺少必要参数";
			} else {
				describeLiveStreamsControlHistoryRequest.setActionName("DescribeLiveStreamsControlHistory");
				describeLiveStreamsControlHistoryRequest.setDomainName(DomainName);
				describeLiveStreamsControlHistoryRequest.setStartTime(StartTime);
				describeLiveStreamsControlHistoryRequest.setEndTime(EndTime);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				describeLiveStreamsControlHistoryRequest.setAcceptFormat(FormatType.JSON);
				describeLiveStreamsControlHistoryRequest.setVersion("2016-11-01");
			}
			if (!StringUtils.isBlank(AppName)) {
				describeLiveStreamsControlHistoryRequest.setAppName(AppName);
			}
			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(describeLiveStreamsControlHistoryRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				System.out.println(str);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return str;
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 获取直播流的在线人数，支持基于域名和基于流的查询
	public String DescribeLiveStreamOnlineUserNumSample(String DomainName, String AppName, String StreamName) {

		try {
			DescribeLiveStreamOnlineUserNumRequest describeLiveStreamOnlineUserNumRequest = new DescribeLiveStreamOnlineUserNumRequest();
			if (StringUtils.isBlank(DomainName)) {
				return "缺少必要参数";
			} else {
				describeLiveStreamOnlineUserNumRequest.setActionName("DescribeLiveStreamOnlineUserNum");
				describeLiveStreamOnlineUserNumRequest.setDomainName(DomainName);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				describeLiveStreamOnlineUserNumRequest.setAcceptFormat(FormatType.JSON);
				describeLiveStreamOnlineUserNumRequest.setVersion("2016-11-01");
			}
			if (!StringUtils.isBlank(AppName)) {
				describeLiveStreamOnlineUserNumRequest.setAppName(AppName);
			}
			if (!StringUtils.isBlank(StreamName)) {
				describeLiveStreamOnlineUserNumRequest.setStreamName(StreamName);
			}
			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(describeLiveStreamOnlineUserNumRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				System.out.println(str);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return str;
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 停止直播流
	public static String ForbidLiveStreamSample(String DomainName, String AppName, String StreamName,
			String ResumeTime) {

		try {
			ForbidLiveStreamRequest forbidLiveStreamRequest = new ForbidLiveStreamRequest();
			if (StringUtils.isBlank(DomainName) || StringUtils.isBlank(AppName)) {
				return "缺少必要参数";
			} else {
				forbidLiveStreamRequest.setActionName("ForbidLiveStream");
				forbidLiveStreamRequest.setDomainName(DomainName);
				forbidLiveStreamRequest.setAppName(AppName);
				forbidLiveStreamRequest.setLiveStreamType("publisher");
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				forbidLiveStreamRequest.setAcceptFormat(FormatType.JSON);
				forbidLiveStreamRequest.setVersion("2016-11-01");
			}
			if (!StringUtils.isBlank(ResumeTime)) {
				forbidLiveStreamRequest.setResumeTime(ResumeTime);
			}
			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(forbidLiveStreamRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				System.out.println(str);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return str;
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 开始直播流
	public String ResumeLiveStreamSample(String DomainName, String AppName, String StreamName) {

		try {
			ResumeLiveStreamRequest resumeLiveStreamRequest = new ResumeLiveStreamRequest();
			if (StringUtils.isBlank(DomainName) || StringUtils.isBlank(AppName)) {
				return "缺少必要参数";
			} else {
				resumeLiveStreamRequest.setActionName("ResumeLiveStream");
				resumeLiveStreamRequest.setDomainName(DomainName);
				resumeLiveStreamRequest.setAppName(AppName);
				resumeLiveStreamRequest.setLiveStreamType("publisher");
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				resumeLiveStreamRequest.setAcceptFormat(FormatType.JSON);
				resumeLiveStreamRequest.setVersion("2016-11-01");
			}
			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(resumeLiveStreamRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				System.out.println(str);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return str;
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 设置直播信息需要推送到的 URL 地址
	public String SetLiveStreamsNotifyUrlConfigSample(String DomainName, String NotifyUrl) {
		System.out.println(NotifyUrl);
		try {
			SetLiveStreamsNotifyUrlConfigRequest setLiveStreamsNotifyUrlConfigRequest = new SetLiveStreamsNotifyUrlConfigRequest();
			if (StringUtils.isBlank(DomainName) || StringUtils.isBlank(NotifyUrl)) {
				return "缺少必要参数";
			} else {
				setLiveStreamsNotifyUrlConfigRequest.setActionName("SetLiveStreamsNotifyUrlConfig");
				setLiveStreamsNotifyUrlConfigRequest.setDomainName(DomainName);
				setLiveStreamsNotifyUrlConfigRequest.setNotifyUrl(NotifyUrl);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				setLiveStreamsNotifyUrlConfigRequest.setAcceptFormat(FormatType.JSON);
				// setLiveStreamsNotifyUrlConfigRequest.setVersion("2016-11-01");
			}

			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(setLiveStreamsNotifyUrlConfigRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				System.out.println(str);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return str;
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 配置 APP 录制，输出内容保存到 OSS 中
	public String AddLiveAppRecordConfigSample(String DomainName, String AppName, String OssEndpoint, String OssBucket,
			String RecordFormat_formt, String RecordFormat_OssObjectPrefix, String RecordFormat_SliceOssObjectPrefix,
			Integer RecordFormat_CycleDuration) {
		List<RecordFormat> list = new ArrayList<RecordFormat>();
		RecordFormat rf = new RecordFormat();
		try {
			AddLiveAppRecordConfigRequest addLiveAppRecordConfigRequest = new AddLiveAppRecordConfigRequest();
			if (StringUtils.isBlank(DomainName) || StringUtils.isBlank(AppName) || StringUtils.isBlank(OssEndpoint)
					|| StringUtils.isBlank(OssBucket) || StringUtils.isBlank(RecordFormat_formt)
					|| StringUtils.isBlank(RecordFormat_OssObjectPrefix)) {
				return "缺少必要参数";
			} else {
				rf.setOssObjectPrefix(RecordFormat_OssObjectPrefix);
				rf.setFormat(RecordFormat_formt);
				addLiveAppRecordConfigRequest.setActionName("AddLiveAppRecordConfig");
				addLiveAppRecordConfigRequest.setDomainName(DomainName);
				addLiveAppRecordConfigRequest.setAppName(AppName);
				addLiveAppRecordConfigRequest.setOssEndpoint(OssEndpoint);
				addLiveAppRecordConfigRequest.setOssBucket(OssBucket);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				addLiveAppRecordConfigRequest.setAcceptFormat(FormatType.JSON);
				addLiveAppRecordConfigRequest.setVersion("2016-11-01");
			}
			if (!StringUtils.isBlank(RecordFormat_SliceOssObjectPrefix)) {
				rf.setSliceOssObjectPrefix(RecordFormat_SliceOssObjectPrefix);
			}
			if (null != RecordFormat_CycleDuration) {
				rf.setCycleDuration(RecordFormat_CycleDuration);
			}
			list.add(rf);
			addLiveAppRecordConfigRequest.setRecordFormats(list);
			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(addLiveAppRecordConfigRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				System.out.println(str);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return str;
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 创建录制索引
	public String CreateLiveStreamRecordIndexFilesSample(String DomainName, String AppName, String StreamName,
			String StartTime, String EndTime, String OssBucket, String OssObject, String OssEndpoint) {

		try {
			CreateLiveStreamRecordIndexFilesRequest createLiveStreamRecordIndexFilesRequest = new CreateLiveStreamRecordIndexFilesRequest();
			if (StringUtils.isBlank(DomainName) || StringUtils.isBlank(StartTime) || StringUtils.isBlank(EndTime)
					|| StringUtils.isBlank(AppName) || StringUtils.isBlank(StreamName) || StringUtils.isBlank(OssBucket)
					|| StringUtils.isBlank(OssObject) || StringUtils.isBlank(OssEndpoint)) {
				return "缺少必要参数";
			} else {
				createLiveStreamRecordIndexFilesRequest.setActionName("CreateLiveStreamRecordIndexFiles");
				createLiveStreamRecordIndexFilesRequest.setDomainName(DomainName);
				createLiveStreamRecordIndexFilesRequest.setStartTime(StartTime);
				createLiveStreamRecordIndexFilesRequest.setEndTime(EndTime);
				createLiveStreamRecordIndexFilesRequest.setAppName(AppName);
				createLiveStreamRecordIndexFilesRequest.setStreamName(StreamName);
				createLiveStreamRecordIndexFilesRequest.setOssBucket(OssBucket);
				createLiveStreamRecordIndexFilesRequest.setOssEndpoint(OssEndpoint);
				createLiveStreamRecordIndexFilesRequest.setOssObject(OssObject);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				createLiveStreamRecordIndexFilesRequest.setAcceptFormat(FormatType.JSON);
				createLiveStreamRecordIndexFilesRequest.setVersion("2016-11-01");
			}

			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(createLiveStreamRecordIndexFilesRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				System.out.println(str);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return str;
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 查询某路直播流录制内容
	public String DescribeLiveStreamRecordContentSample(String DomainName, String AppName, String StreamName,
			String StartTime, String EndTime) {

		try {
			DescribeLiveStreamRecordContentRequest describeLiveStreamRecordContentRequest = new DescribeLiveStreamRecordContentRequest();
			if (StringUtils.isBlank(DomainName) || StringUtils.isBlank(StartTime) || StringUtils.isBlank(EndTime)
					|| StringUtils.isBlank(AppName) || StringUtils.isBlank(StreamName)) {
				return "缺少必要参数";
			} else {
				describeLiveStreamRecordContentRequest.setActionName("DescribeLiveStreamRecordContent");
				describeLiveStreamRecordContentRequest.setDomainName(DomainName);
				describeLiveStreamRecordContentRequest.setStartTime(StartTime);
				describeLiveStreamRecordContentRequest.setEndTime(EndTime);
				describeLiveStreamRecordContentRequest.setAppName(AppName);
				describeLiveStreamRecordContentRequest.setStreamName(StreamName);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				describeLiveStreamRecordContentRequest.setAcceptFormat(FormatType.JSON);
				describeLiveStreamRecordContentRequest.setVersion("2016-11-01");
			}

			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(describeLiveStreamRecordContentRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				System.out.println(str);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return str;
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 查询单个录制索引文件
	public String DescribeLiveStreamRecordIndexFileSample(String DomainName, String AppName, String StreamName,
			String RecordID) {

		try {
			DescribeLiveStreamRecordIndexFileRequest describeLiveStreamRecordIndexFile = new DescribeLiveStreamRecordIndexFileRequest();
			if (StringUtils.isBlank(DomainName) || StringUtils.isBlank(RecordID) || StringUtils.isBlank(AppName)
					|| StringUtils.isBlank(StreamName)) {
				return "缺少必要参数";
			} else {
				describeLiveStreamRecordIndexFile.setActionName("DescribeLiveStreamRecordIndexFile");
				describeLiveStreamRecordIndexFile.setDomainName(DomainName);
				describeLiveStreamRecordIndexFile.setRecordId(RecordID);
				describeLiveStreamRecordIndexFile.setStreamName(StreamName);
				describeLiveStreamRecordIndexFile.setAppName(AppName);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				describeLiveStreamRecordIndexFile.setAcceptFormat(FormatType.JSON);
				describeLiveStreamRecordIndexFile.setVersion("2016-11-01");
			}

			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(describeLiveStreamRecordIndexFile);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				System.out.println(str);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return str;
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 查询录制索引文件
	public String DescribeLiveStreamRecordIndexFilesSample(String DomainName, String AppName, String StreamName,
			String StartTime, String EndTime, Integer PageSize, Integer PageNumber, String Order) {

		try {
			DescribeLiveStreamRecordIndexFilesRequest describeLiveStreamRecordIndexFilesRequest = new DescribeLiveStreamRecordIndexFilesRequest();
			if (StringUtils.isBlank(DomainName) || StringUtils.isBlank(StartTime) || StringUtils.isBlank(EndTime)
					|| StringUtils.isBlank(AppName) || StringUtils.isBlank(StreamName)) {
				return "缺少必要参数";
			} else {
				describeLiveStreamRecordIndexFilesRequest.setActionName("DescribeLiveStreamRecordIndexFiles");
				describeLiveStreamRecordIndexFilesRequest.setDomainName(DomainName);
				describeLiveStreamRecordIndexFilesRequest.setStartTime(StartTime);
				describeLiveStreamRecordIndexFilesRequest.setEndTime(EndTime);
				describeLiveStreamRecordIndexFilesRequest.setAppName(AppName);
				describeLiveStreamRecordIndexFilesRequest.setStreamName(StreamName);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				describeLiveStreamRecordIndexFilesRequest.setAcceptFormat(FormatType.JSON);
				// describeLiveStreamRecordIndexFilesRequest.setVersion("2016-11-01");
			}
			if (null != PageSize) {
				describeLiveStreamRecordIndexFilesRequest.setPageSize(PageSize);
			}
			if (null != PageNumber) {
				describeLiveStreamRecordIndexFilesRequest.setPageSize(PageNumber);
			}
			if (StringUtils.isBlank(Order)) {
				describeLiveStreamRecordIndexFilesRequest.setOrder(Order);
			}
			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(describeLiveStreamRecordIndexFilesRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				System.out.println(str);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return str;
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 获取直播流的帧率和码率，支持基于域名和基于流的查询
	public String DescribeLiveStreamsFrameRateAndBitRateDataSample(String DomainName, String AppName, String StreamName,
			String StartTime, String EndTime) {

		try {
			DescribeLiveStreamsFrameRateAndBitRateDataRequest describeLiveStreamsFrameRateAndBitRateDataRequest = new DescribeLiveStreamsFrameRateAndBitRateDataRequest();
			if (StringUtils.isBlank(DomainName) || StringUtils.isBlank(StartTime) || StringUtils.isBlank(EndTime)) {
				return "缺少必要参数";
			} else {
				describeLiveStreamsFrameRateAndBitRateDataRequest.setActionName("DescribeLiveStreamBitRateData");
				describeLiveStreamsFrameRateAndBitRateDataRequest.setDomainName(DomainName);
				describeLiveStreamsFrameRateAndBitRateDataRequest.setStartTime(StartTime);
				describeLiveStreamsFrameRateAndBitRateDataRequest.setEndTime(EndTime);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				describeLiveStreamsFrameRateAndBitRateDataRequest.setAcceptFormat(FormatType.JSON);
				describeLiveStreamsFrameRateAndBitRateDataRequest.setVersion("2016-11-01");
			}
			if (StringUtils.isBlank(AppName)) {
				describeLiveStreamsFrameRateAndBitRateDataRequest.setAppName(AppName);
			}
			if (StringUtils.isBlank(StreamName)) {
				describeLiveStreamsFrameRateAndBitRateDataRequest.setStreamName(StreamName);
			}
			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(describeLiveStreamsFrameRateAndBitRateDataRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				System.out.println(str);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return str;
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 添加转码配置
	public String AddLiveStreamTranscodeSample(String DomainName, String AppName, String Template) {

		try {
			AddLiveStreamTranscodeRequest addLiveStreamTranscodeRequest = new AddLiveStreamTranscodeRequest();
			if (StringUtils.isBlank(DomainName) || StringUtils.isBlank(AppName) || StringUtils.isBlank(Template)) {
				return "缺少必要参数";
			} else {
				addLiveStreamTranscodeRequest.setActionName("AddLiveStreamTranscode");
				addLiveStreamTranscodeRequest.setDomain(DomainName);
				addLiveStreamTranscodeRequest.setApp(AppName);
				addLiveStreamTranscodeRequest.setTemplate(Template);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				addLiveStreamTranscodeRequest.setAcceptFormat(FormatType.JSON);
				addLiveStreamTranscodeRequest.setVersion("2016-11-01");
			}
			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(addLiveStreamTranscodeRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				System.out.println(str);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return str;
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 删除转码配置
	public String DeleteLiveStreamTranscodeSample(String DomainName, String AppName, String Template) {

		try {
			DeleteLiveStreamTranscodeRequest deleteLiveStreamTranscode = new DeleteLiveStreamTranscodeRequest();
			if (StringUtils.isBlank(DomainName) || StringUtils.isBlank(AppName) || StringUtils.isBlank(Template)) {
				return "缺少必要参数";
			} else {
				deleteLiveStreamTranscode.setActionName("DeleteLiveStreamTranscode");
				deleteLiveStreamTranscode.setDomain(DomainName);
				deleteLiveStreamTranscode.setApp(AppName);
				deleteLiveStreamTranscode.setTemplate(Template);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				deleteLiveStreamTranscode.setAcceptFormat(FormatType.JSON);
				deleteLiveStreamTranscode.setVersion("2016-11-01");
			}
			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(deleteLiveStreamTranscode);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				System.out.println(str);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return str;
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 查询转码配置
	public String DescribeLiveStreamTranscodeInfoSample(String DomainName) {

		try {
			DescribeLiveStreamTranscodeInfoRequest describeLiveStreamTranscodeInfo = new DescribeLiveStreamTranscodeInfoRequest();
			if (StringUtils.isBlank(DomainName)) {
				return "缺少必要参数";
			} else {
				describeLiveStreamTranscodeInfo.setActionName("DescribeLiveStreamTranscodeInfo");
				describeLiveStreamTranscodeInfo.setDomainTranscodeName(DomainName);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				describeLiveStreamTranscodeInfo.setAcceptFormat(FormatType.JSON);
				describeLiveStreamTranscodeInfo.setVersion("2016-11-01");
			}
			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(describeLiveStreamTranscodeInfo);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				System.out.println(str);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return str;
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 添加截图配置
	public String AddLiveAppSnapshotConfigSample(String DomainName, String AppName, Integer TimeInterval,
			String OSSEndpoint, String OSSBucket, String OverwriteOSSObject, String SequenceOSSObject) {

		try {
			AddLiveAppSnapshotConfigRequest addLiveAppSnapshotConfigRequest = new AddLiveAppSnapshotConfigRequest();
			if (StringUtils.isBlank(DomainName) || StringUtils.isBlank(AppName) || StringUtils.isBlank(OSSEndpoint)
					|| StringUtils.isBlank(OSSBucket) || null == TimeInterval) {
				return "缺少必要参数";
			} else {
				addLiveAppSnapshotConfigRequest.setActionName("AddLiveAppSnapshotConfig");
				addLiveAppSnapshotConfigRequest.setDomainName(DomainName);
				addLiveAppSnapshotConfigRequest.setAppName(AppName);
				addLiveAppSnapshotConfigRequest.setTimeInterval(TimeInterval);
				addLiveAppSnapshotConfigRequest.setOssEndpoint(OSSEndpoint);
				addLiveAppSnapshotConfigRequest.setOssBucket(OSSBucket);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				addLiveAppSnapshotConfigRequest.setAcceptFormat(FormatType.JSON);
				addLiveAppSnapshotConfigRequest.setVersion("2016-11-01");
			}
			if (!StringUtils.isBlank(OverwriteOSSObject)) {
				addLiveAppSnapshotConfigRequest.setOverwriteOssObject(OverwriteOSSObject);
			}
			if (!StringUtils.isBlank(SequenceOSSObject)) {
				addLiveAppSnapshotConfigRequest.setSequenceOssObject(SequenceOSSObject);
			}
			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(addLiveAppSnapshotConfigRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				System.out.println(str);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return str;
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 删除截图配置
	public String DeleteLiveAppSnapshotConfigSample(String DomainName, String AppName) {

		try {
			DeleteLiveAppSnapshotConfigRequest deleteLiveAppSnapshotConfig = new DeleteLiveAppSnapshotConfigRequest();
			if (StringUtils.isBlank(DomainName) || StringUtils.isBlank(AppName)) {
				return "缺少必要参数";
			} else {
				deleteLiveAppSnapshotConfig.setActionName("DeleteLiveAppSnapshotConfig");
				deleteLiveAppSnapshotConfig.setDomainName(DomainName);
				deleteLiveAppSnapshotConfig.setAppName(AppName);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				deleteLiveAppSnapshotConfig.setAcceptFormat(FormatType.JSON);
				deleteLiveAppSnapshotConfig.setVersion("2016-11-01");
			}
			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(deleteLiveAppSnapshotConfig);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				System.out.println(str);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return str;
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 更新截图配置
	public String UpdateLiveAppSnapshotConfigSample(String DomainName, String AppName, Integer TimeInterval,
			String OSSEndpoint, String OSSBucket, String OverwriteOSSObject, String SequenceOSSObject) {

		try {
			UpdateLiveAppSnapshotConfigRequest updateLiveAppSnapshotConfigRequest = new UpdateLiveAppSnapshotConfigRequest();
			if (StringUtils.isBlank(DomainName) || StringUtils.isBlank(AppName)) {
				return "缺少必要参数";
			} else {
				updateLiveAppSnapshotConfigRequest.setActionName("UpdateLiveAppSnapshotConfig");
				updateLiveAppSnapshotConfigRequest.setDomainName(DomainName);
				updateLiveAppSnapshotConfigRequest.setAppName(AppName);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				updateLiveAppSnapshotConfigRequest.setAcceptFormat(FormatType.JSON);
				updateLiveAppSnapshotConfigRequest.setVersion("2016-11-01");
			}
			if (null != TimeInterval) {
				updateLiveAppSnapshotConfigRequest.setTimeInterval(TimeInterval);
			}
			if (!StringUtils.isBlank(OSSEndpoint)) {
				updateLiveAppSnapshotConfigRequest.setOssEndpoint(OSSEndpoint);
			}
			if (!StringUtils.isBlank(OSSBucket)) {
				updateLiveAppSnapshotConfigRequest.setOssBucket(OSSBucket);
			}
			if (!StringUtils.isBlank(SequenceOSSObject)) {
				updateLiveAppSnapshotConfigRequest.setSequenceOssObject(SequenceOSSObject);
			}
			if (!StringUtils.isBlank(OverwriteOSSObject)) {
				updateLiveAppSnapshotConfigRequest.setOverwriteOssObject(OverwriteOSSObject);
			}
			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(updateLiveAppSnapshotConfigRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				System.out.println(str);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return str;
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 查询域名截图配置
	public String DescribeLiveSnapshotConfigSample(String DomainName, String AppName, Integer PageSize,
			Integer PageNumber, String Order) {

		try {
			DescribeLiveSnapshotConfigRequest describeLiveSnapshotConfigRequest = new DescribeLiveSnapshotConfigRequest();
			if (StringUtils.isBlank(DomainName)) {
				return "缺少必要参数";
			} else {
				describeLiveSnapshotConfigRequest.setActionName("DescribeLiveSnapshotConfig");
				describeLiveSnapshotConfigRequest.setDomainName(DomainName);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				describeLiveSnapshotConfigRequest.setAcceptFormat(FormatType.JSON);
				describeLiveSnapshotConfigRequest.setVersion("2016-11-01");
			}
			if (StringUtils.isBlank(AppName)) {
				describeLiveSnapshotConfigRequest.setAppName(AppName);
			}
			if (null != PageSize) {
				describeLiveSnapshotConfigRequest.setPageSize(PageSize);
			}
			if (null != PageNumber) {
				describeLiveSnapshotConfigRequest.setPageSize(PageNumber);
			}
			if (StringUtils.isBlank(Order)) {
				describeLiveSnapshotConfigRequest.setOrder(Order);
			}
			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(describeLiveSnapshotConfigRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				System.out.println(str);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return str;
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 查询截图信息
	public String DescribeLiveStreamSnapshotInfoSample(String DomainName, String AppName, String StreamName,
			String StartTime, String EndTime, Integer Limit) {

		try {
			DescribeLiveStreamSnapshotInfoRequest describeLiveStreamSnapshotInfoRequest = new DescribeLiveStreamSnapshotInfoRequest();
			if (StringUtils.isBlank(DomainName) || StringUtils.isBlank(StartTime) || StringUtils.isBlank(EndTime)
					|| StringUtils.isBlank(AppName) || StringUtils.isBlank(StreamName)) {
				return "缺少必要参数";
			} else {
				describeLiveStreamSnapshotInfoRequest.setActionName("DescribeLiveStreamSnapshotInfo");
				describeLiveStreamSnapshotInfoRequest.setDomainName(DomainName);
				describeLiveStreamSnapshotInfoRequest.setStartTime(StartTime);
				describeLiveStreamSnapshotInfoRequest.setEndTime(EndTime);
				describeLiveStreamSnapshotInfoRequest.setAppName(AppName);
				describeLiveStreamSnapshotInfoRequest.setStreamName(StreamName);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				describeLiveStreamSnapshotInfoRequest.setAcceptFormat(FormatType.JSON);
				describeLiveStreamSnapshotInfoRequest.setVersion("2016-11-01");
			}
			if (null != Limit) {
				describeLiveStreamSnapshotInfoRequest.setLimit(Limit);
			}
			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(describeLiveStreamSnapshotInfoRequest);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				System.out.println(str);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return str;
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 添加鉴黄配置
	public String AddLiveSnapshotDetectPornConfigSample(String DomainName, String AppName, String OssEndpoint,
			String OssBucket, String OssObject, Integer Interval) {

		try {
			AddLiveSnapshotDetectPornConfigRequest addLiveSnapshotDetectPornConfig = new AddLiveSnapshotDetectPornConfigRequest();
			if (StringUtils.isBlank(DomainName) || StringUtils.isBlank(AppName) || StringUtils.isBlank(OssEndpoint)
					|| StringUtils.isBlank(OssBucket)) {
				return "缺少必要参数";
			} else {
				addLiveSnapshotDetectPornConfig.setDomainName(DomainName);
				addLiveSnapshotDetectPornConfig.setOssEndpoint(OssEndpoint);
				addLiveSnapshotDetectPornConfig.setOssBucket(OssBucket);
				addLiveSnapshotDetectPornConfig.setAppName(AppName);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				addLiveSnapshotDetectPornConfig.setAcceptFormat(FormatType.JSON);
				addLiveSnapshotDetectPornConfig.setVersion("2016-11-01");
			}
			if (null != Interval) {
				addLiveSnapshotDetectPornConfig.setInterval(Interval);
			}
			if (StringUtils.isBlank(OssObject)) {
				addLiveSnapshotDetectPornConfig.setOssObject(OssObject);
			}
			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(addLiveSnapshotDetectPornConfig);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				System.out.println(str);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return str;
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 添加鉴黄回调
	public String AddLiveDetectNotifyConfigSample(String DomainName, String NotifyUrl) {

		try {
			AddLiveDetectNotifyConfigRequest addLiveDetectNotifyConfig = new AddLiveDetectNotifyConfigRequest();
			if (StringUtils.isBlank(DomainName) || StringUtils.isBlank(NotifyUrl)) {
				return "缺少必要参数";
			} else {
				addLiveDetectNotifyConfig.setDomainName(DomainName);
				addLiveDetectNotifyConfig.setNotifyUrl(NotifyUrl);
				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
				// //指定请求方法
				addLiveDetectNotifyConfig.setAcceptFormat(FormatType.JSON);
				addLiveDetectNotifyConfig.setVersion("2016-11-01");
			}
			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(addLiveDetectNotifyConfig);
			String str = null;
			try {
				str = new String(httpResponse.getHttpContent(), "UTF-8");
				System.out.println(str);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return str;
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

//	// 查询鉴黄配置
//	public String DescribeLiveSnapshotDetectPornConfigSample(String DomainName, String AppName, Integer PageSize,
//			Integer PageNumber, String Order) {
//
//		try {
//			DescribeLiveSnapshotDetectPornConfig describeLiveSnapshotDetectPornConfigRequest = new DescribeLiveSnapshotDetectPornConfigRequest();
//			if (StringUtils.isBlank(DomainName)) {
//				return "缺少必要参数";
//			} else {
//				describeLiveSnapshotDetectPornConfigRequest.setDomainName(DomainName);
//				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
//				// //指定请求方法
//				describeLiveSnapshotDetectPornConfigRequest.setAcceptFormat(FormatType.JSON);
//				describeLiveSnapshotDetectPornConfigRequest.setVersion("2016-11-01");
//			}
//			if (StringUtils.isBlank(AppName)) {
//				describeLiveSnapshotDetectPornConfigRequest.setAppName(AppName);
//			}
//			if (null != PageSize) {
//				describeLiveSnapshotDetectPornConfigRequest.setPageSize(PageSize);
//			}
//			if (null != PageNumber) {
//				describeLiveSnapshotDetectPornConfigRequest.setPageSize(PageNumber);
//			}
//			if (StringUtils.isBlank(Order)) {
//				describeLiveSnapshotDetectPornConfigRequest.setOrder(Order);
//			}
//			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
//			DefaultAcsClient client = coreAliyunlive.init();
//			HttpResponse httpResponse = client.doAction(describeLiveSnapshotDetectPornConfigRequest);
//			String str = null;
//			try {
//				str = new String(httpResponse.getHttpContent(), "UTF-8");
//				System.out.println(str);
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//			return str;
//		} catch (ServerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		} catch (ClientException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}

//	// 查询鉴黄回调
//	public String DescribeLiveDetectNotifyConfigSample(String DomainName) {
//
//		try {
//			DescribeLiveDetectNotifyConfigRequest describeLiveDetectNotifyConfigRequest = new DescribeLiveDetectNotifyConfigRequest();
//			if (StringUtils.isBlank(DomainName)) {
//				return "缺少必要参数";
//			} else {
//				describeLiveDetectNotifyConfigRequest.setDomainName(DomainName);
//				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
//				// //指定请求方法
//				describeLiveDetectNotifyConfigRequest.setAcceptFormat(FormatType.JSON);
//				describeLiveDetectNotifyConfigRequest.setVersion("2016-11-01");
//			}
//			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
//			DefaultAcsClient client = coreAliyunlive.init();
//			HttpResponse httpResponse = client.doAction(describeLiveDetectNotifyConfigRequest);
//			String str = null;
//			try {
//				str = new String(httpResponse.getHttpContent(), "UTF-8");
//				System.out.println(str);
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//			return str;
//		} catch (ServerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		} catch (ClientException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	// 更新鉴黄配置
//	public String UpdateLiveSnapshotDetectPornConfigSample(String DomainName, String AppName, String OssEndpoint,
//			String OssBucket, String OssObject, Integer Interval) {
//
//		try {
//			UpdateLiveSnapshotDetectPornConfigRequest updateLiveSnapshotDetectPornConfig = new UpdateLiveSnapshotDetectPornConfigRequest();
//			if (StringUtils.isBlank(DomainName) || StringUtils.isBlank(AppName)) {
//				return "缺少必要参数";
//			} else {
//				updateLiveSnapshotDetectPornConfig.setDomainName(DomainName);
//				updateLiveSnapshotDetectPornConfig.setAppName(AppName);
//				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
//				// //指定请求方法
//				updateLiveSnapshotDetectPornConfig.setAcceptFormat(FormatType.JSON);
//				updateLiveSnapshotDetectPornConfig.setVersion("2016-11-01");
//			}
//			if (null != Interval) {
//				updateLiveSnapshotDetectPornConfig.setInterval(Interval);
//			}
//			if (StringUtils.isBlank(OssEndpoint)) {
//				updateLiveSnapshotDetectPornConfig.setOssEndpoint(OssEndpoint);
//			}
//			if (StringUtils.isBlank(OssBucket)) {
//				updateLiveSnapshotDetectPornConfig.setOssBucket(OssBucket);
//			}
//			if (StringUtils.isBlank(OssObject)) {
//				updateLiveSnapshotDetectPornConfig.setOssObject(OssObject);
//			}
//			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
//			DefaultAcsClient client = coreAliyunlive.init();
//			HttpResponse httpResponse = client.doAction(updateLiveSnapshotDetectPornConfig);
//			String str = null;
//			try {
//				str = new String(httpResponse.getHttpContent(), "UTF-8");
//				System.out.println(str);
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//			return str;
//		} catch (ServerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		} catch (ClientException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	// 更新鉴黄回调
//	public String UpdateLiveDetectNotifyConfigSample(String DomainName, String NotifyUrl) {
//
//		try {
//			UpdateLiveDetectNotifyConfigRequest updateLiveDetectNotifyConfig = new UpdateLiveDetectNotifyConfigRequest();
//			if (StringUtils.isBlank(DomainName)) {
//				return "缺少必要参数";
//			} else {
//				updateLiveDetectNotifyConfig.setDomainName(DomainName);
//				updateLiveDetectNotifyConfig.setNotifyUrl(NotifyUrl);
//				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
//				// //指定请求方法
//				updateLiveDetectNotifyConfig.setAcceptFormat(FormatType.JSON);
//				updateLiveDetectNotifyConfig.setVersion("2016-11-01");
//			}
//			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
//			DefaultAcsClient client = coreAliyunlive.init();
//			HttpResponse httpResponse = client.doAction(updateLiveDetectNotifyConfig);
//			String str = null;
//			try {
//				str = new String(httpResponse.getHttpContent(), "UTF-8");
//				System.out.println(str);
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//			return str;
//		} catch (ServerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		} catch (ClientException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	// 删除鉴黄配置
//	public String DeleteLiveSnapshotDetectPornConfigSample(String DomainName, String AppName) {
//
//		try {
//			DeleteLiveSnapshotDetectPornConfigRequest deleteLiveSnapshotDetectPornConfig = new DeleteLiveSnapshotDetectPornConfigRequest();
//			if (StringUtils.isBlank(DomainName) || StringUtils.isBlank(AppName)) {
//				return "缺少必要参数";
//			} else {
//				deleteLiveSnapshotDetectPornConfig.setDomainName(DomainName);
//				deleteLiveSnapshotDetectPornConfig.setAppName(AppName);
//				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
//				// //指定请求方法
//				deleteLiveSnapshotDetectPornConfig.setAcceptFormat(FormatType.JSON);
//				deleteLiveSnapshotDetectPornConfig.setVersion("2016-11-01");
//			}
//			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
//			DefaultAcsClient client = coreAliyunlive.init();
//			HttpResponse httpResponse = client.doAction(deleteLiveSnapshotDetectPornConfig);
//			String str = null;
//			try {
//				str = new String(httpResponse.getHttpContent(), "UTF-8");
//				System.out.println(str);
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//			return str;
//		} catch (ServerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		} catch (ClientException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	// 删除鉴黄回调
//	public String DeleteLiveDetectNotifyConfigSample(String DomainName) {
//
//		try {
//			DeleteLiveDetectNotifyConfigRequest deleteLiveDetectNotifyConfig = new DeleteLiveDetectNotifyConfigRequest();
//			if (StringUtils.isBlank(DomainName)) {
//				return "缺少必要参数";
//			} else {
//				deleteLiveDetectNotifyConfig.setDomainName(DomainName);
//				// describeLiveStreamsPublishListRequest.setMethod(MethodType.GET);
//				// //指定请求方法
//				deleteLiveDetectNotifyConfig.setAcceptFormat(FormatType.JSON);
//				deleteLiveDetectNotifyConfig.setVersion("2016-11-01");
//			}
//			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
//			DefaultAcsClient client = coreAliyunlive.init();
//			HttpResponse httpResponse = client.doAction(deleteLiveDetectNotifyConfig);
//			String str = null;
//			try {
//				str = new String(httpResponse.getHttpContent(), "UTF-8");
//				System.out.println(str);
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//			return str;
//		} catch (ServerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		} catch (ClientException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}

	// 开启混流服务
	public static String StartMultipleStreamMixService(String DomainName, String appName, String roomId) {
		StartMultipleStreamMixServiceRequest request = new StartMultipleStreamMixServiceRequest();
		if (StringUtils.isBlank(DomainName)) {
			return "缺少必要参数";
		} else {
			request.setActionName("StartMultipleStreamMixService");
			request.setDomainName(DomainName);
			request.setAppName(appName);
			request.setStreamName(roomId);
			request.setMixTemplate("pip4a");
		}
		try {
			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(request);
			String str = null;

			str = new String(httpResponse.getHttpContent(), "UTF-8");
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return roomId;
	}

	// 往主流中添加一路
	public static String AddMultipleStreamMixService(String DomainName, String appName, String roomId,
			String MixRoomId) {
		AddMultipleStreamMixServiceRequest request = new AddMultipleStreamMixServiceRequest();
		if (StringUtils.isBlank(DomainName)) {
			return "缺少必要参数";
		} else {
			request.setActionName("AddMultipleStreamMixService");
			request.setDomainName(DomainName);
			request.setAppName(appName);
			request.setStreamName(roomId);
			request.setMixDomainName(DomainName);
			request.setMixAppName(appName);
			request.setMixStreamName(MixRoomId);
		}
		try {
			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(request);
			String str = null;

			str = new String(httpResponse.getHttpContent(), "UTF-8");
			System.out.println(str);
		} catch (UnsupportedEncodingException | ClientException e) {
			e.printStackTrace();
		}
		return roomId;
	}

	// 从主流中移除一路
	public static String RemoveMultipleStreamMixService(String DomainName, String appName, String roomId,
			String MixRoomId) {
		RemoveMultipleStreamMixServiceRequest request = new RemoveMultipleStreamMixServiceRequest();
		if (StringUtils.isBlank(DomainName)) {
			return "缺少必要参数";
		} else {
			request.setActionName("RemoveMultipleStreamMixService");
			request.setDomainName(DomainName);
			request.setAppName(appName);
			request.setStreamName(roomId);
			request.setMixDomainName(DomainName);
			request.setMixAppName(appName);
			request.setMixStreamName(MixRoomId);
		}
		try {
			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(request);
			String str = null;

			str = new String(httpResponse.getHttpContent(), "UTF-8");
			System.out.println(str);
		} catch (UnsupportedEncodingException | ClientException e) {
			e.printStackTrace();
		}
		return roomId;
	}

	// 从停止混流服务
	public static String StopMultipleStreamMixService(String DomainName, String appName, String roomId) {
		StopMultipleStreamMixServiceRequest request = new StopMultipleStreamMixServiceRequest();
		if (StringUtils.isBlank(DomainName)) {
			return "缺少必要参数";
		} else {
			request.setActionName("StopMultipleStreamMixService");
			request.setDomainName(DomainName);
			request.setAppName(appName);
			request.setStreamName(roomId);
		}
		try {
			CoreAliyunlive coreAliyunlive = new CoreAliyunlive();
			DefaultAcsClient client = coreAliyunlive.init();
			HttpResponse httpResponse = client.doAction(request);
			String str = null;

			str = new String(httpResponse.getHttpContent(), "UTF-8");
			System.out.println(str);
		} catch (UnsupportedEncodingException | ClientException e) {
			e.printStackTrace();
		}
		return roomId;
	}

	public static void main(String[] args) {
		// rtmp://source.live01.zb.tv189.com:1935/live/live1173_tzwj_5000k
//		 StartMultipleStreamMixService("alplay1.qz123.com", "jwzt",
//		 "live1173_tzwj_5000k");
		AddMultipleStreamMixService("alplay1.qz123.com", "jwzt", "live1173_tzwj_5000k", "live1174_tzwj_5000k");
		// StopMultipleStreamMixService("live1.iptv-soft.com", "jwzt", "101");
	}

}
