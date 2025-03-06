package com.bvRadio.iLive.iLive.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveSubLevel;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;

/**
 * ILive上下文信息拦截器
 * 
 * 包括登录信息、权限信息、站点信息
 */
public class AdminContextInterceptor extends HandlerInterceptorAdapter {

	public static final String PERMISSION_MODEL = "_permission_key";

	private ConcurrentHashMap<Integer, Map<String, Integer>> roomEnterpriseMap = new ConcurrentHashMap<>();

	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		@SuppressWarnings("static-access")
		String requestURI = this.getURI(request);
		if (exclude(requestURI)) {
			if (requestURI.indexOf("opentrial.do") > -1) {
				UserBean user = ILiveUtils.getUser(request);
				if (user != null) {
					return true;
				} else {
					response.sendRedirect("/ilive/admin/login.do");
					return false;
				}
			}
			return true;
		}
		if(requestURI.indexOf("logout.do")>-1) {
			response.sendRedirect("/ilive/admin/login.do");
			return false;
		}
		if(requestURI.indexOf("/cert/enterprise.do")>-1) {
			return true;
		}
		
		UserBean user = ILiveUtils.getUser(request);
		if (user != null) {
			Integer certStatus = user.getCertStatus();
			certStatus = certStatus == null ? 0 : certStatus;
			if (certStatus == 0) {
				response.sendRedirect("/ilive/admin/opentrial.do");
				return false;
			}
			String roomIdstr = request.getParameter("roomId");
			if (roomIdstr != null && !roomIdstr.equals("")) {
				Integer roomId = Integer.parseInt(roomIdstr);
				Map<String, Integer> map = roomEnterpriseMap.get(roomId);
				if (map == null) {
					ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
					if (iliveRoom != null) {
						Map<String, Integer> recordMap = new HashMap<>();
						recordMap.put("enterpriseId", iliveRoom.getEnterpriseId());
						recordMap.put("managerId", iliveRoom.getManagerId().intValue());
						roomEnterpriseMap.put(roomId, recordMap);
						Integer managerId = iliveRoom.getManagerId().intValue();
						Integer enterpriseId = iliveRoom.getEnterpriseId();
						if (managerId.intValue() != Integer.parseInt(user.getUserId())
								&& !enterpriseId.equals(user.getEnterpriseId())) {
							response.sendRedirect("/ilive/admin/login.do");
							return false;
						}
					} else {
						response.sendRedirect("/ilive/admin/login.do");
						return false;
					}
				} else {
					Integer enterpriseId = map.get("enterpriseId");
					Integer managerId = map.get("managerId");
					if (managerId.intValue() != Integer.parseInt(user.getUserId()) && !enterpriseId.equals(user.getEnterpriseId())) {
						response.sendRedirect("/ilive/admin/login.do");
						return false;
					}
				}
			}
			return true;
		} else {
			response.sendRedirect("/ilive/admin/login.do");
		}
		return false;
	}

	private boolean exclude(String uri) {
		if (excludeUrls != null) {
			for (String exc : excludeUrls) {
				if (uri.endsWith(exc)) {
					return true;
				}
			}
		}
		return false;
	}

	private String[] excludeUrls;

	public void setExcludeUrls(String[] excludeUrls) {
		this.excludeUrls = excludeUrls;
	}
	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav)
			throws Exception {
		if (null != mav) {
			ModelMap model = mav.getModelMap();
			/**
			 * 处理顶部框架用户信息显示的通用信息
			 */
			UserBean topUser = ILiveUtils.getUser(request);
			if (null != topUser) {
				Long UserId=Long.parseLong(topUser.getUserId());
				//获取昵称
				ILiveManager manager=iLiveManagerMng.selectILiveManagerById(UserId);
				topUser.setNickname(manager.getNailName());
				//获取子账户的权限
				List<ILiveSubLevel> iLiveSubLevel = iLiveManagerMng.selectILiveSubById(UserId);
				
				java.util.List list=new ArrayList();
				if(iLiveSubLevel!=null&&iLiveSubLevel.size()>0) {
					for(int i=0;i<iLiveSubLevel.size();i++) {
						list.add(iLiveSubLevel.get(i).getSubTop());
					}
				}
				topUser.setSubTop(list);
				Integer enterpriseId = topUser.getEnterpriseId();
				ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
				Integer certStatus = iLiveEnterprise.getCertStatus();
				boolean b = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_CloudGuidedSeeding,certStatus);
				if(b){
					topUser.setCloudGuidedSeeding(0);
					}else{
					topUser.setCloudGuidedSeeding(1);
				}
				//查询是否开通拉流直播
				boolean b1 = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_lahueLive,certStatus);
				if(b1){
					topUser.setLahueLive(0);
				}else{
					topUser.setLahueLive(1);
				}
				//查询是否开通红包
				boolean b20 = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_WechatRed,certStatus);
				if(b20){
					topUser.setRedPackage(0);
				}else{
					topUser.setRedPackage(1);
				}
				//查询是否开通垫片
				boolean b2 = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_Gasket,certStatus);
				if(b2){
					topUser.setGasket(0);
				}else{
					topUser.setGasket(1);
				}
				//查询是否开通第三方推流
				boolean b3 = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_PushStream,certStatus);
				if(b3){
					topUser.setPushStream(0);
				}else{
					topUser.setPushStream(1);
				}
				//查询是否开通桌面直播工具
				boolean b4 = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_DeskTop,certStatus);
				if(b4){
					topUser.setDeskTop(0);
					}else{
					topUser.setDeskTop(1);
				}
				//检查是否开通直播助手
				boolean b5 = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_LiveAssistant,certStatus);
				if(b5){
					topUser.setLiveAssistant(0);
				}else{
					topUser.setLiveAssistant(1);
				}
				//检查是否开通子账号管理
				Integer b6 = EnterpriseUtil.checkSubcount(enterpriseId, certStatus);
				topUser.setSubAccountManagement(b6);
				
				//检查是否开通直播自服务平台LOGO自定义
				boolean b7 = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_LiveSelfServiceLogo,certStatus);
				if(b7){
					topUser.setLiveSelfServiceLogo(0);
				}else{
					topUser.setLiveSelfServiceLogo(1);
				}
				//检查是否开通播放画面台标自定义
				boolean b17 = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_PlayTableMark,certStatus);
				if(b17){
					topUser.setPlayTableMark(0);
				}else{
					topUser.setPlayTableMark(1);
				}
				//检查是否开通矩阵监控
				boolean b8 = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_LiveMatrixMonitoring,certStatus);
				if(b8){
					topUser.setLiveMatrixMonitoring(0);
				}else{
					topUser.setLiveMatrixMonitoring(1);
				}
				//检查是否开通收益账户
				Integer getIfPassRevenue=EnterpriseUtil.getIfPassRevenue(enterpriseId, certStatus);
				if(getIfPassRevenue!=null) {
					topUser.setGetIfPassRevenue(getIfPassRevenue);
				}
				boolean b11 = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_CorporateHome,certStatus);
				boolean b21 = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_CorporateHomePublicnumber,certStatus);
				if(b11&&b21){
					topUser.setCorporateHome(0);
				}else{
					topUser.setCorporateHome(1);
				}
				boolean b31 = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_TopicPage,certStatus);
				boolean b41 = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_TopicPagePublicnumber,certStatus);
				if(b31&&b41){
					topUser.setTopicPage(0);
				}else{
					topUser.setTopicPage(1);
				}
				if(b11&&b21&&b31&&b41) {
					topUser.setCorporateTopicPage(0);
				}else {
					topUser.setCorporateTopicPage(1);
				}
				boolean ifPackage=EnterpriseUtil.getEnterprisePackage(enterpriseId, certStatus);
				if(ifPackage) {
					topUser.setIfHasPackage(1);
				}else {
					topUser.setIfHasPackage(0);
				}
				boolean delayedLive=EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_DelayedLive,certStatus);
				if(delayedLive) {
					topUser.setDelayedLive(0);
				}else {
					topUser.setDelayedLive(1);
				}
				model.put("topUser", topUser);
				
			}
		}
	}

	/**
	 * 获得第三个路径分隔符的位置
	 * 
	 * @param request
	 * @throws IllegalStateException
	 *             访问路径错误，没有三(四)个'/'
	 */
	private static String getURI(HttpServletRequest request) throws IllegalStateException {
		UrlPathHelper helper = new UrlPathHelper();
		String uri = helper.getOriginatingRequestUri(request);
		String ctxPath = helper.getOriginatingContextPath(request);
		int start = 0, i = 0, count = 1;
		if (!StringUtils.isBlank(ctxPath)) {
			count++;
		}
		while (i < count && start != -1) {
			start = uri.indexOf('/', start + 1);
			i++;
		}
		if (start <= 0) {
			throw new IllegalStateException("admin access path not like '/admin/...' pattern: " + uri);
		}
		return uri.substring(start);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}