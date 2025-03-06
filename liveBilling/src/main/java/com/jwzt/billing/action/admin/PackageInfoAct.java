package com.jwzt.billing.action.admin;

import static com.jwzt.common.page.SimplePage.checkPageNo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.aliyun.oss.OSSClient;
import com.jwzt.billing.entity.PackageInfo;
import com.jwzt.billing.entity.bo.UploadServer;
import com.jwzt.billing.entity.bo.UserBO;
import com.jwzt.billing.entity.bo.WorkLog;
import com.jwzt.billing.manager.PackageInfoMng;
import com.jwzt.billing.manager.WorkLogMng;
import com.jwzt.billing.utils.SessionUtils;
import com.jwzt.common.entity.Config;
import com.jwzt.common.manager.ConfigMng;
import com.jwzt.common.page.Pagination;
import com.jwzt.common.utils.FileUtils;
import com.jwzt.common.web.CookieUtils;
import com.jwzt.common.web.springmvc.RenderJsonUtils;

@Controller
public class PackageInfoAct {

	@RequestMapping(value = "/package/page", method = RequestMethod.GET)
	public String page(String queryContent, Integer packageType, Integer status, Integer channelType, Date startTime,
			Date endTime, ModelMap mp, HttpServletRequest request, Integer pageNo) {
		String channelTypes = null;
		String packageName = null;
		try {
			if (null != queryContent) {
				packageName = new String(queryContent.getBytes("iso-8859-1"), "utf-8");
			}
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
			if (null != channelType) {
				channelTypes = String.valueOf(channelType);
			}
		} catch (Exception e) {
		}
		Integer[] packageTypes = null;
		if (null != packageType) {
			packageTypes = new Integer[1];
			packageTypes[0] = packageType;
		}
		Pagination page = packageMng.pageByParams(packageName, packageTypes, status, channelTypes, startTime, endTime,
				checkPageNo(pageNo), CookieUtils.getPageSize(request), true, false);
		RenderJsonUtils.addSuccess(mp, page);
		return "renderJson";
	}

	@RequestMapping(value = "/package/list", method = RequestMethod.GET)
	public String list(String queryContent, Integer packageType, Integer status, Integer channelType, Date startTime,
			Date endTime, ModelMap mp, HttpServletRequest request, Integer pageNo) {
		String channelTypes = null;
		String packageName = null;
		try {
			if (null != queryContent) {
				packageName = new String(queryContent.getBytes("iso-8859-1"), "utf-8");
			}
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
			if (null != channelType) {
				channelTypes = String.valueOf(channelType);
			}
		} catch (Exception e) {
		}
		Integer[] packageTypes = null;
		if (null != packageType) {
			packageTypes = new Integer[1];
			packageTypes[0] = packageType;
		}
		List<PackageInfo> list = packageMng.listByParams(packageName, packageTypes, status, channelTypes, startTime,
				endTime, true,false);
		RenderJsonUtils.addSuccess(mp, list);
		return "renderJson";
	}

	@RequestMapping(value = "/package/info", method = RequestMethod.GET)
	public String info(Integer id, ModelMap mp) {
		if (null == id) {
			RenderJsonUtils.addError(mp, "参数id不能为空");
			return "renderJson";
		}
		PackageInfo bean = packageMng.getBeanById(id, true);
		if (null != bean) {
			try {
				List<WorkLog> workLogList = workLogMng.listByParams(WorkLog.CURRENT_SYSTEM_ID, WorkLog.MODEL_ID_PACKAGE,
						String.valueOf(id), null, null, null);
				bean.setWorkLogList(workLogList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		RenderJsonUtils.addSuccess(mp, bean);
		return "renderJson";
	}

	@RequestMapping(value = "/package/save", method = RequestMethod.POST)
	public String save(PackageInfo bean, @RequestParam(value = "productIds[]", required = false) Integer[] productIds,
			ModelMap mp, HttpServletRequest request) {
		if (null == bean) {
			RenderJsonUtils.addError(mp, "内容不能为空");
			return "renderJson";
		}
		String packageName = bean.getPackageName();
		if (null == packageName) {
			RenderJsonUtils.addError(mp, "套餐名称不能为空");
			return "renderJson";
		}
		Integer vaildDurationValue = bean.getVaildDurationValue();
		Integer vaildDurationUnit = bean.getVaildDurationUnit();
		if (null == vaildDurationValue || null == vaildDurationUnit) {
			RenderJsonUtils.addError(mp, "套餐周期不能为空");
			return "renderJson";
		}
		if (vaildDurationValue < 0) {
			RenderJsonUtils.addError(mp, "套餐周期不能为负值");
			return "renderJson";
		}
		Double orginalPrice = bean.getOrginalPrice();
		if (null == orginalPrice) {
			RenderJsonUtils.addError(mp, "套餐原价不能为空");
			return "renderJson";
		}
		if (orginalPrice < 0) {
			RenderJsonUtils.addError(mp, "套餐原价不能为负值");
			return "renderJson";
		}
		Double costPrice = bean.getCostPrice();
		if (null == costPrice) {
			RenderJsonUtils.addError(mp, "成本价不能为空");
			return "renderJson";
		}
		if (costPrice < 0) {
			RenderJsonUtils.addError(mp, "成本价不能为负值");
			return "renderJson";
		}
		String channelTypes = bean.getChannelTypes();
		if (StringUtils.isBlank(channelTypes)) {
			RenderJsonUtils.addError(mp, "投放渠道不能为空");
			return "renderJson";
		}
		String[] channelTypeArray = channelTypes.split(",");
		for (int i = 0; i < channelTypeArray.length; i++) {
			if (String.valueOf(PackageInfo.CHANNEL_TYPE_OFFICIAL_PLATFORM).equals(channelTypeArray[i])) {
				Double officialPlatformPrice = bean.getOfficialPlatformPrice();
				if (null == officialPlatformPrice) {
					RenderJsonUtils.addError(mp, "平台销售价格不能为空");
					return "renderJson";
				}
				if (officialPlatformPrice < 0) {
					RenderJsonUtils.addError(mp, "平台销售价格不能为负值");
					return "renderJson";
				}
			} else if (String.valueOf(PackageInfo.CHANNEL_TYPE_CHANNEL_AGENT).equals(channelTypeArray[i])) {
				Double channelAgentPrice = bean.getChannelAgentPrice();
				if (null == channelAgentPrice) {
					RenderJsonUtils.addError(mp, "渠道扣率基准价不能为空");
					return "renderJson";
				}
				if (channelAgentPrice < 0) {
					RenderJsonUtils.addError(mp, "渠道扣率基准价不能为负值");
					return "renderJson";
				}
			} else if (String.valueOf(PackageInfo.CHANNEL_TYPE_GROUP_PRODUCT_LIBRARY).equals(channelTypeArray[i])) {
				Double groupProductLibraryPrice = bean.getGroupProductLibraryPrice();
				if (null == groupProductLibraryPrice) {
					RenderJsonUtils.addError(mp, "产品库销售价格不能为空");
					return "renderJson";
				}
				if (groupProductLibraryPrice < 0) {
					RenderJsonUtils.addError(mp, "产品库销售价格不能为负值");
					return "renderJson";
				}
			}
		}
		if (null == productIds || productIds.length == 0) {
			RenderJsonUtils.addError(mp, "至少选择一个资源包");
			return "renderJson";
		}
		Map<String, String> uploadFileMap = commonUpload(request);
		String introduceImagePath = uploadFileMap.get("introduceImageFile");
		bean.setIntroduceImagePath(introduceImagePath);
		packageMng.save(bean, productIds);
		try {
			Integer id = bean.getId();
			UserBO currentUser = SessionUtils.getUser(request);
			Long userId = currentUser.getId();
			String username = currentUser.getUsername();
			String contentName = "新增套餐";
			String content = bean.toString();
			WorkLog workLog = new WorkLog(WorkLog.MODEL_ID_PACKAGE, String.valueOf(id), content, contentName, userId,
					username, null);
			workLogMng.save(workLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		RenderJsonUtils.addSuccess(mp, bean);
		return "renderJson";
	}

	@RequestMapping(value = "/package/update", method = RequestMethod.POST)
	public String update(PackageInfo bean, ModelMap mp, HttpServletRequest request) {
		if (null == bean) {
			RenderJsonUtils.addError(mp, "参数id不能为空");
			return "renderJson";
		}
		Map<String, String> uploadFileMap = commonUpload(request);
		String introduceImagePath = uploadFileMap.get("introduceImageFile");
		String httpServerHomeUrl = configMng.getValue(Config.HTTP_SERVER_HOME_URL);
		introduceImagePath=httpServerHomeUrl+"/"+introduceImagePath;
		bean.setIntroduceImagePath(introduceImagePath);
		packageMng.update(bean);
		try {
			Integer id = bean.getId();
			UserBO currentUser = SessionUtils.getUser(request);
			Long userId = currentUser.getId();
			String username = currentUser.getUsername();
			String contentName = "修改套餐";
			String content = bean.toString();
			WorkLog workLog = new WorkLog(WorkLog.MODEL_ID_PACKAGE, String.valueOf(id), content, contentName, userId,
					username, null);
			workLogMng.save(workLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		RenderJsonUtils.addSuccess(mp, bean);
		return "renderJson";
	}

	@RequestMapping(value = "/package/online", method = RequestMethod.POST)
	public String online(Integer id, ModelMap mp, HttpServletRequest request) {
		if (null == id) {
			RenderJsonUtils.addError(mp, "参数id不能为空");
			return "renderJson";
		}
		PackageInfo bean = packageMng.getBeanById(id, false);
		if (null == bean) {
			RenderJsonUtils.addError(mp, "套餐不存在");
			return "renderJson";
		}
		Integer status = bean.getStatus();
		if (!(PackageInfo.STATUS_NEW.equals(status) || PackageInfo.STATUS_OFFLINE.equals(status))) {
			RenderJsonUtils.addError(mp, "套餐状态非法，不能发售");
			return "renderJson";
		}
		bean.setStatus(PackageInfo.STATUS_ONLINE);
		packageMng.update(bean);
		try {
			UserBO currentUser = SessionUtils.getUser(request);
			Long userId = currentUser.getId();
			String username = currentUser.getUsername();
			String contentName = "发售套餐";
			String content = bean.toString();
			WorkLog workLog = new WorkLog(WorkLog.MODEL_ID_PACKAGE, String.valueOf(id), content, contentName, userId,
					username, null);
			workLogMng.save(workLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		RenderJsonUtils.addSuccess(mp, bean);
		return "renderJson";
	}

	@RequestMapping(value = "/package/offline", method = RequestMethod.POST)
	public String offline(Integer id, ModelMap mp, HttpServletRequest request) {
		if (null == id) {
			RenderJsonUtils.addError(mp, "参数id不能为空");
			return "renderJson";
		}
		PackageInfo bean = packageMng.getBeanById(id, false);
		if (null == bean) {
			RenderJsonUtils.addError(mp, "套餐不存在");
			return "renderJson";
		}
		Integer status = bean.getStatus();
		if (!PackageInfo.STATUS_ONLINE.equals(status)) {
			RenderJsonUtils.addError(mp, "套餐状态非法，不能停售");
			return "renderJson";
		}
		bean.setStatus(PackageInfo.STATUS_OFFLINE);
		packageMng.update(bean);
		try {
			UserBO currentUser = SessionUtils.getUser(request);
			Long userId = currentUser.getId();
			String username = currentUser.getUsername();
			String contentName = "停售套餐";
			String content = bean.toString();
			WorkLog workLog = new WorkLog(WorkLog.MODEL_ID_PACKAGE, String.valueOf(id), content, contentName, userId,
					username, null);
			workLogMng.save(workLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		RenderJsonUtils.addSuccess(mp, bean);
		return "renderJson";
	}

	@RequestMapping(value = "/package/order", method = RequestMethod.POST)
	public String order(Integer id, Integer orderNum, ModelMap mp, HttpServletRequest request) {
		if (null == id) {
			RenderJsonUtils.addError(mp, "参数id不能为空");
			return "renderJson";
		}
		PackageInfo bean = packageMng.getBeanById(id, false);
		if (null == bean) {
			RenderJsonUtils.addError(mp, "套餐不存在");
			return "renderJson";
		}
		bean.setOrderNum(orderNum);
		packageMng.update(bean);
		RenderJsonUtils.addSuccess(mp, bean);
		return "renderJson";
	}

	private Map<String, String> commonUpload(HttpServletRequest request) {
		// 创建存放上传文件存放路径的map
		Map<String, String> uploadVodFileMap = new HashMap<String, String>();
		// 创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 判断 request 是否有文件上传,即多部分请求
		if (multipartResolver.isMultipart(request)) {
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				try {
					// 取得上传文件
					MultipartFile myfile = multiRequest.getFile(iter.next());
					if (myfile != null) {
						String paramName = myfile.getName();
						// 取得当前上传文件的文件名称
						String fileName = myfile.getOriginalFilename();
						// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
						if (!StringUtils.isBlank(fileName)) {
							// 重命名上传后的文件名
							String filePath = FileUtils.getTimeFilePath(fileName);
							
							String ftpIp = configMng.getValue(Config.FTP_SERVER_IP);
							Integer ftpPort;
							try {
								ftpPort = Integer.parseInt(configMng.getValue(Config.FTP_SERVER_PORT));
							} catch (Exception e) {
								ftpPort = 21;
							}
							String ftpUsername = configMng.getValue(Config.FTP_SERVER_USERNAME);
							String ftpPassword = configMng.getValue(Config.FTP_SERVER_PASSWORD);
							String ftpPath = configMng.getValue(Config.FTP_SERVER_PATH);
							String ftpEncoding = configMng.getValue(Config.FTP_SERVER_ENCODING);
							// 定义上传路径
							UploadServer uploadServer = new UploadServer(ftpIp, ftpPort, ftpUsername, ftpPassword,
									ftpPath, ftpEncoding);
							InputStream in = myfile.getInputStream();
							boolean result = uploadServer.upload(filePath, in);
							if (result) {
								uploadVodFileMap.put(paramName, filePath);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return uploadVodFileMap;
	}

	@Autowired
	private PackageInfoMng packageMng;
	@Autowired
	private ConfigMng configMng;
	@Autowired
	private WorkLogMng workLogMng;
}
