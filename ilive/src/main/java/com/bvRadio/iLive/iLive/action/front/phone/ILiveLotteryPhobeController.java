package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bvRadio.iLive.common.cache.Cache;
import com.bvRadio.iLive.common.cache.CacheManager;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.util.SubAccountCache;
import com.bvRadio.iLive.iLive.entity.AuthBean;
import com.bvRadio.iLive.iLive.entity.ILiveComment;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveLottery;
import com.bvRadio.iLive.iLive.entity.ILiveLotteryParktake;
import com.bvRadio.iLive.iLive.entity.ILiveLotteryPrize;
import com.bvRadio.iLive.iLive.entity.ILiveLotteryShare;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.ILivePlayRewards;
import com.bvRadio.iLive.iLive.entity.ILivePrizeRoom;
import com.bvRadio.iLive.iLive.entity.ILiveRedPacket;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.ILiveSignRoom;
import com.bvRadio.iLive.iLive.entity.ILiveSignin;
import com.bvRadio.iLive.iLive.entity.ILiveSigninUser;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveCommentMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveLotteryParktakeMng;
import com.bvRadio.iLive.iLive.manager.ILiveLotteryPrizeMng;
import com.bvRadio.iLive.iLive.manager.ILiveLotteryShareMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILivePlayRewardsMng;
import com.bvRadio.iLive.iLive.manager.ILivePrizeMng;
import com.bvRadio.iLive.iLive.manager.ILivePrizeRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveQuestionnaireRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveRedPacketMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveSignRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveSigninMng;
import com.bvRadio.iLive.iLive.manager.ILiveSigninUserMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubLevelMng;
import com.bvRadio.iLive.iLive.manager.ILiveVoteActivityMng;
import com.bvRadio.iLive.iLive.manager.ILiveVoteRoomMng;
import com.bvRadio.iLive.iLive.util.JedisUtils;
import com.bvRadio.iLive.iLive.util.Md5Util;
import com.bvRadio.iLive.iLive.util.SerializeUtil;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="/prize")
public class ILiveLotteryPhobeController {

	@Autowired
	private ILivePrizeMng iLivePrizeMng;	//抽奖内容
	
	@Autowired
	private ILiveLotteryPrizeMng iLiveLotteryPrizeMng;		//抽奖奖品设置
	
	@Autowired
	private ILiveManagerMng iLiveManagerMng;		//用户Manager
	
	@Autowired
	private ILiveLotteryParktakeMng iLiveLotteryParktakeMng;		//抽奖用户
	
	@Autowired
	private ILiveVoteActivityMng iLiveVoteActivityMng;	//投票活动
	
	@Autowired
	private ILiveLiveRoomMng iLiveRoomMng;
	
	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;
	
	@Autowired
	private ILivePlayRewardsMng iLivePlayRewardsMng;
	
	@Autowired
	private ILiveLotteryShareMng iLiveLotteryShareMng;
	@Autowired
	private ILiveSigninMng signinMng;
	@Autowired
	private ILiveSigninUserMng signinUserMng;	//人员管理
	@Autowired
	private ILiveCommentMng commentMng;
	@Autowired
	private ILiveRedPacketMng packetMng;
	
	@Autowired
	private ILiveVoteRoomMng iLiveVoteRoomMng;
	
	@Autowired
	private ILivePrizeRoomMng iLivePrizeRoomMng;
	@Autowired
	private ILiveSignRoomMng  iLiveSignRoomMng;
	@Autowired
	private ILiveQuestionnaireRoomMng iLiveQuestionnaireRoomMng;
	@Autowired
	private ILiveSubLevelMng iLiveSubLevelMng;
	
	public static final String USER_SIGN_USERID = "_user_sign_userid";
	public static final String USER_SIGN_USERIMG = "_user_sign_userimg";
	
	@RequestMapping(value="userInfo.jspx")
	public void userInfo(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String string=IOUtils.toString(request.getInputStream(), "utf-8");
		JSONObject json_result = JSONObject.fromObject(string);
		String authString=json_result.get("authString").toString();
		JSONObject resultJson = new JSONObject();
		try {
			if (StringUtils.isNotBlank(authString)) {
				AuthBean authBean = checkAuthString(authString);
				if (null != authBean && null != authBean.getResult() && authBean.getResult()) {
					String paramString = authBean.getParamString();
				    String userId=	getQueryString(paramString, "userId");
				    ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.parseLong(userId));
				    if(iLiveManager!=null) {
				    	resultJson.put("status", 1001);
				    	resultJson.put("enterprise", iLiveManager.getEnterPrise().getEnterpriseId());
						resultJson.put("msg", "获取用户信息成功");
				    }else {
				    	resultJson.put("status", 1004);
						resultJson.put("msg", "获取用户信息失败");
				    }
				}else {
					resultJson.put("status", 1003);
					resultJson.put("msg", "解析authString参数错误");
				}
			}else {
				resultJson.put("status", 1002);
				resultJson.put("msg", "获取authString参数错误");
			}
			
			
				
				
		} catch (Exception e) {
			resultJson.put("status", 1005);
			resultJson.put("msg", "方法异常");
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	
	
	/**
	 * 是否重复签到
	 */
	@RequestMapping(value="siginPhone.jspx")
	public void siginPhone(Long signId,String userPhone,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveSigninUser signinUser =signinUserMng.getListByEnterpriseId(signId,userPhone);
			if(signinUser!=null) {
				resultJson.put("status", 1);
			}else {
				resultJson.put("status", 0);
			}
			
		} catch (Exception e) {
             
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	
	
	/**
	 * 关闭其他签到活动
	 */
	@RequestMapping(value="closeOtherSign.jspx",method=RequestMethod.GET)
	public void closeOtherSign(Integer roomId,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveLiveRoom room = iLiveRoomMng.findById(roomId);
			List<ILiveSignin> list = null;
			if (room!=null&&room.getEnterpriseId()!=null) {
				list = signinMng.getByEnterpriseId(room.getEnterpriseId());
			}
			for (ILiveSignin iLiveSignin : list) {
				iLiveSignin.setIsAllow(0);
				signinMng.update(iLiveSignin);
			}
			List<ILiveSignRoom> signList =iLiveSignRoomMng.selectAllSign(roomId);
			if(signList!=null&&signList.size()!=0) {
				for (ILiveSignRoom iLiveSignRoom : signList) {
					iLiveSignRoom.setIsOpen(0);;
					iLiveSignRoomMng.update(iLiveSignRoom);
				}
			}
			resultJson.put("code", 1);
		} catch (Exception e) {
             resultJson.put("code", 0);
		}	
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	
	/**
	 * 关闭其他红包活动
	 */
	@RequestMapping(value="closeOtherRedPacket.jspx",method=RequestMethod.GET)
	public void closeOtherRedPacket(Integer roomId,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			List<ILiveRedPacket> list =packetMng.getListByRoomId(roomId);
			for (ILiveRedPacket packet : list) {
				packet.setIsAllow(0);
				packetMng.update(packet);
			}
			resultJson.put("code", 1);
		} catch (Exception e) {
             resultJson.put("code", 0);
		}	
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	
	/**
	 * 签到活动信息
	 */
	@RequestMapping(value="isSign.jspx")
	public void isSign(Integer roomId,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		ILiveSignRoom signin = iLiveSignRoomMng.selectStartSign(roomId);
		
		if(signin==null) {
			resultJson.put("code", 0);
			resultJson.put("message","活动已结束");
		}else {
			if(signin.getIsOpen()==0) {
				resultJson.put("code", 0);
				resultJson.put("message","活动已结束");
			}else {
				resultJson.put("code", 1);
				
				resultJson.put("signId",signin.getSignId());
			}
			
			
		}
			
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	
	/**
	 * 红包信息
	 */
	@RequestMapping(value="isRedPacket.jspx")
	public void isRedPacket(Integer roomId,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		ILiveRedPacket packet =packetMng.getIsStart(roomId);
		if(packet==null) {
			resultJson.put("code", 0);
			resultJson.put("message","活动已结束");
		}else {
			
			resultJson.put("code", 1);
			
			JSONObject jsonArray =JSONObject.fromObject(packet);
			resultJson.put("packet", jsonArray.toString());
		}
	    
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	/**
	 * 签到活动信息
	 */
	@RequestMapping(value="isOverSign.jspx")
	public void isOverSign(Integer signId,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		ILiveSignin signin =signinMng.getById(signId.longValue());
		if(signin!=null) {
			if(signin.getEndTime().getTime()>=new Date().getTime()) {
				resultJson.put("code", 1);
			}else {
				resultJson.put("code", 0);
			}
		}else {
			resultJson.put("code", 0);
		}
		
			
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	
	/**
	 * 评论活动信息
	 */
	@RequestMapping(value="isOverComment.jspx")
	public void isOverComment(Integer roomId,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		ILiveComment comment =commentMng.getIsStart(roomId);
		if(comment!=null) {
			resultJson.put("isAllow", comment.getIsAllow());
			if(comment.getEndTime().getTime()>=new Date().getTime()) {
				resultJson.put("code", 1);
				
			}else {
				resultJson.put("code", 0);
				
			}
		}else {
			resultJson.put("code", 0);
		}
		
		JSONObject jsonArray =JSONObject.fromObject(comment);
		resultJson.put("comment", jsonArray.toString());
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	
	/**
	 * 签到名单列表页
	 * @param roomId
	 * @param model
	 * @param name
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="siginUserlist.jspx")
	public void siginUserlist(Integer roomId,Long signId,Integer pageNo,Integer pageSize, HttpServletResponse response,Integer type,HttpServletRequest request) {
		JSONObject json = new JSONObject();
		if(pageNo==null) {
			pageNo=1;
		}
		if(pageSize==null) {
			pageSize=24;
		}
		try {
			if(roomId!=null) {
				ILiveSignin sign = signinMng.getById(signId.longValue());
				JSONObject res = JSONObject.fromObject(sign);
				json.put("msg", "获取信息成功");
				json.put("status", 1);
				json.put("res", res);
			}else {
				ILiveSignin sign = signinMng.getById(signId.longValue());
				JSONObject res = JSONObject.fromObject(sign);
				Pagination page =signinUserMng.getPage(signId+"",null,null, null,null, pageNo==null?1:pageNo, pageSize);
				JSONArray jsonArray = JSONArray.fromObject(page.getList());
				json.put("status", 1);
				json.put("pageNo",page.getPageNo());
				json.put("pageSize",page.getPageSize());
				json.put("totalPage",page.getTotalPage());
				json.put("data", jsonArray);
				json.put("res", res);
			}
			
		} catch (Exception e) {
			json.put("status", 0);
			json.put("msg", "获取列表失败");
		}
		
		ResponseUtils.renderJson(request,response, json.toString());
		
	}
	
	
	
	
	/**
	 * 人员签到
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="userSign.jspx")
	public void userSign(String vPassword,String userphone,Long signId,
			HttpServletResponse response,Integer type,HttpServletRequest request) throws UnsupportedEncodingException {
		String username=URLDecoder.decode(request.getParameter("username"),"UTF-8");
		HttpSession session = request.getSession();
		 String userId=(String) session.getAttribute(USER_SIGN_USERID);
		 String userImg=(String) session.getAttribute(USER_SIGN_USERIMG);
		
		JSONObject json = new JSONObject();
		boolean ret = true;
		if (username == null) {
			ret = false;
			json.put("status", "0");
			json.put("msg", "手机号不能为空");
		} else if (vPassword == null) {
			ret = false;
			json.put("status", "0");
			json.put("msg", "验证码不能为空");
		} else {
			Cache cacheInfo = CacheManager.getCacheInfo(CacheManager.mobile_token_ + "reg_" + userphone);
			String defaultVpassword = ConfigUtils.get("defaultVpassword");
			
			if (defaultVpassword.equals(vPassword)) {
				ret = true;
			} else if (cacheInfo == null) {
				json.put("status", "0");
				json.put("msg", "输入的验证码不正确");
				ret = false;
			} else if (cacheInfo.isExpired()) {
				json.put("status", "0");
				json.put("msg", "输入的验证码已经过期,请重新获取");
				ret = false;
			} else if (!vPassword.equals(cacheInfo.getValue())) {
				json.put("status", "0");
				json.put("msg", "输入的验证码不正确请重新获取验证码");
				CacheManager.clearOnly(CacheManager.mobile_token_ + "reg_" + username);
				ret = false;
			}
		}
		if(ret) {
			try {
				ILiveSigninUser signinUser =new ILiveSigninUser();
				ILiveSignin sign = signinMng.getById(signId.longValue());
				if(sign.getEndTime().getTime()>=new Date().getTime()) {
					String uuid = UUID.randomUUID().toString();
					signinUser.setCreateTime(new Date());
					if(userId!=null&&!"".equals(userId)) {
						signinUser.setUserId(Long.parseLong(userId));
					}
					signinUser.setUserName(username);
					signinUser.setUserPhoto(userImg);
					signinUser.setUserPhone(userphone);
					signinUser.setSign(sign);
					signinUser.setSignUserId(uuid);
					signinUserMng.save(signinUser);
					json.put("status", 1000);
					json.put("msg", "签到成功");
				}else {
					json.put("status", 1001);
					json.put("msg", "签到已过期");
				}
				
				
			} catch (Exception e) {
				json.put("status", 1002);
				json.put("msg", "签到出错");
			}
			
		}
		ResponseUtils.renderJson(request,response, json.toString());
		
	}
	
	
	/**
	 * 是否登录
	 */
	@RequestMapping(value="isLogin.jspx",method=RequestMethod.GET)
	public void letclose(HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		
			UserBean user = ILiveUtils.getAppUser(request);
			if(user==null) {
				resultJson.put("code", 0);
				
				resultJson.put("data","");
			}else {
				JSONObject jsonArray =JSONObject.fromObject(user);
				resultJson.put("code", 1);
				
				resultJson.put("data",jsonArray.toString() );
			}
			
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	
	
	/**
	 * 获取已开始活动和未开始的活动
	 * @param roomId
	 * @param response
	 * @param request
	 */
	@RequestMapping(value="getList.jspx")
	public void getListByRoomId(Integer roomId,HttpServletResponse response,HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			UserBean user = ILiveUtils.getUser(request);
			ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
			
			ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.valueOf(user.getUserId()));
			iLiveVoteRoomMng.clearEnd(roomId,iliveRoom.getEnterpriseId());
			iLivePrizeRoomMng.clearEnd(roomId,iliveRoom.getEnterpriseId());
			
			//查询子账户是否具有图片查看权限
			boolean per=iLiveSubLevelMng.selectIfCan(request, SubAccountCache.ENTERPRISE_FUNCTION_rewardActivity);
			List<ILiveLottery> list = null;
			if (iLiveManager.getLevel()!=null&&iLiveManager.getLevel()!=0&&!per) {
				list = iLivePrizeMng.getListUserH5ByUserId(Long.valueOf(user.getUserId()));
			}else {
				if (iliveRoom!=null && iliveRoom.getEnterpriseId()!=null) {
					list = iLivePrizeMng.getListUserH5ByEnterpriseId(iliveRoom.getEnterpriseId());
				}
			}
			
			
			boolean startPrize = iLivePrizeRoomMng.isStartPrize(roomId);
			if (startPrize) {
				ILivePrizeRoom selectStartPrize = iLivePrizeRoomMng.selectStartPrize(roomId);
				if (list!=null&&list.size()>0) {
					Iterator<ILiveLottery> iterator = list.iterator();
					while (iterator.hasNext()) {
						ILiveLottery iLiveLottery = (ILiveLottery) iterator.next();
						if (iLiveLottery.getId().equals(selectStartPrize.getPrizeId())) {
							iLiveLottery.setIsSwitch(1);
						}
					}
				}
			}
			
			JSONArray jsonArray = JSONArray.fromObject(list);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取抽奖列表成功");
			resultJson.put("data",jsonArray.toString() );
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取抽奖列表失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	
	/**
	 * 获取活动签到
	 * @param roomId
	 * @param model
	 * @param name
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="getSignList.jspx")
	public void getList(Integer roomId,HttpServletResponse response,HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
		ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		List<ILiveSignin> list = null;
		if(iliveRoom!=null&&iliveRoom.getEnterpriseId()!=null) {
			list =signinMng.getByEnterpriseId(iliveRoom.getEnterpriseId());
		}
		iLiveSignRoomMng.selectAllSign(roomId);
		boolean startSign = iLiveSignRoomMng.isStartSign(roomId);
		if (startSign) {
			ILiveSignRoom selectStartSign = iLiveSignRoomMng.selectStartSign(roomId);
			if (list!=null&&list.size()>0) {
				Iterator<ILiveSignin> iterator = list.iterator();
				while (iterator.hasNext()) {
					ILiveSignin iLiveSignin = (ILiveSignin) iterator.next();
					if (iLiveSignin.getSignId().equals(selectStartSign.getSignId())) {
						iLiveSignin.setIsAllow(1);
					}
				}
			}
		}
		
		
		JSONArray jsonArray = JSONArray.fromObject(list);
		resultJson.put("code", SUCCESS_STATUS);
		resultJson.put("message", "获取活动签到列表成功");
		resultJson.put("data",jsonArray.toString() );
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取活动签到列表失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	
	/**
	 * 获取口令红包
	 * @param roomId
	 * @param model
	 * @param name
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="getRedPacketList.jspx")
	public void getRedPacketList(Integer roomId,HttpServletResponse response,HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
		List<ILiveRedPacket> list =packetMng.getListByRoomId(roomId);
		JSONArray jsonArray = JSONArray.fromObject(list);
		resultJson.put("code", 1);
		resultJson.put("message", "获取红包列表成功");
		resultJson.put("data",jsonArray.toString() );
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", 2);
			resultJson.put("message", "获取红包列表失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	
	/**
	 * 修改抽奖活动
	 * @param id
	 * @param isSwitch
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="closeorstart.jspx")
	public void closeorstart(Integer roomId,Long id,Integer isSwitch,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			iLivePrizeMng.closeOrStart(id,isSwitch);
			ILivePrizeRoom selectPrize = iLivePrizeRoomMng.selectPrize(roomId, id);
			if (selectPrize!=null) {
				selectPrize.setIsOpen(isSwitch);
				iLivePrizeRoomMng.update(selectPrize);
			}else {
				selectPrize = new ILivePrizeRoom();
				selectPrize.setRoomId(roomId);
				selectPrize.setPrizeId(id);
				selectPrize.setIsOpen(isSwitch);
				iLivePrizeRoomMng.save(selectPrize);
			}
			ILiveLottery prize = iLivePrizeMng.getPrize(id);
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap.get(roomId);
			Iterator<String> userIterator = concurrentHashMap.keySet().iterator();
			ILiveMessage iLiveMessage = new ILiveMessage();
			iLiveMessage.setRoomType(5);
			if("open".equals(ConfigUtils.get("redis_service"))) {
				iLiveMessage.setMsgId(Long.parseLong("-"+roomId+""+roomId));
				JedisUtils.delObject("msg:"+iLiveMessage.getMsgId());
				JedisUtils.setByte(("msg:"+iLiveMessage.getMsgId()).getBytes(), SerializeUtil.serialize(iLiveMessage), 0);
				Set<String> userIdList =JedisUtils.getSet("userIdList"+roomId);
				if(userIdList!=null&&userIdList.size()!=0) {
					for(String userId:userIdList) {
						boolean flag=true;
						while (flag) {
							String requestionIdString=UUID.randomUUID().toString();
							if(JedisUtils.tryGetDistributedLock(userId+"lock", requestionIdString, 1)) {
								JedisUtils.listAdd(roomId+":"+userId, iLiveMessage.getMsgId()+"");
								flag=false;
								JedisUtils.releaseDistributedLock(userId+"lock", requestionIdString);
							}else {
								try {
									Thread.sleep(100);
								} catch (Exception e) {
									e.printStackTrace();
								}
								
							}
						}
					
					}
				}
				
			}else {
				while (userIterator.hasNext()) {
					String key = userIterator.next();
					UserBean user = concurrentHashMap.get(key);
					
						List<ILiveMessage> msgList = user.getMsgList();
						if (msgList == null) {
							msgList = new ArrayList<ILiveMessage>();
						}
						msgList.add(iLiveMessage);
				}
			}
			
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "修改成功");
			resultJson.put("data",new JSONObject() );
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "修改失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	
	/**
	 * 修改签到活动
	 * @param id
	 * @param isSwitch
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="closeorstartsign.jspx")
	public void closeorstartsign(Integer roomId,Long id,Integer isSwitch,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			
			ILiveSignRoom selectSign = iLiveSignRoomMng.selectSign(roomId, id);
			if (selectSign!=null) {
				selectSign.setIsOpen(isSwitch);
				iLiveSignRoomMng.update(selectSign);
			}else {
				selectSign = new ILiveSignRoom();
				selectSign.setRoomId(roomId);
				selectSign.setSignId(id);
				selectSign.setIsOpen(isSwitch);
				iLiveSignRoomMng.save(selectSign);
			}
			
			
			
			ILiveSignin singin =signinMng.getById(id.longValue());
			
			
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap.get(roomId);
			Iterator<String> userIterator = concurrentHashMap.keySet().iterator();
			ILiveMessage iLiveMessage = new ILiveMessage();
			iLiveMessage.setRoomType(15);
			if("open".equals(ConfigUtils.get("redis_service"))) {
				iLiveMessage.setMsgId(Long.parseLong("-"+roomId+""+roomId));
				
				JedisUtils.delObject("msg:"+iLiveMessage.getMsgId());
				JedisUtils.setByte(("msg:"+iLiveMessage.getMsgId()).getBytes(), SerializeUtil.serialize(iLiveMessage), 0);
				Set<String> userIdList =JedisUtils.getSet("userIdList"+roomId);
				if(userIdList!=null&&userIdList.size()!=0) {
					for(String userId:userIdList) {
						boolean flag=true;
						while (flag) {
							String requestionIdString=UUID.randomUUID().toString();
							if(JedisUtils.tryGetDistributedLock(userId+"lock", requestionIdString, 1)) {
								JedisUtils.listAdd(roomId+":"+userId, iLiveMessage.getMsgId()+"");
								flag=false;
								JedisUtils.releaseDistributedLock(userId+"lock", requestionIdString);
							}else {
								try {
									Thread.sleep(100);
								} catch (Exception e) {
									e.printStackTrace();
								}
								
							}
						}
					}
				}
				
			}else {
				while (userIterator.hasNext()) {
					String key = userIterator.next();
					UserBean user = concurrentHashMap.get(key);
						List<ILiveMessage> msgList = user.getMsgList();
						if (msgList == null) {
							msgList = new ArrayList<ILiveMessage>();
						}
						msgList.add(iLiveMessage);
					
				}
			}
			
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "修改成功");
			resultJson.put("data",new JSONObject() );
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "修改失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	/**
	 * 关闭红包活动
	 * @param id
	 * @param isSwitch
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="closeredpacket.jspx")
	public void closeredpacket(Long id,Integer isSwitch,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveRedPacket packet =packetMng.getById(id);
			packet.setIsAllow(isSwitch);
			packetMng.update(packet);
			
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap.get(packet.getRoom().getRoomId());
			Iterator<String> userIterator = concurrentHashMap.keySet().iterator();
			ILiveMessage iLiveMessage = new ILiveMessage();
			iLiveMessage.setRoomType(16);
			if("open".equals(ConfigUtils.get("redis_service"))) {
				iLiveMessage.setMsgId(Long.parseLong("-"+packet.getRoom().getRoomId()+""+packet.getRoom().getRoomId()));
				
				JedisUtils.delObject("msg:"+iLiveMessage.getMsgId());
				JedisUtils.setByte(("msg:"+iLiveMessage.getMsgId()).getBytes(), SerializeUtil.serialize(iLiveMessage), 0);
				Set<String> userIdList =JedisUtils.getSet("userIdList"+packet.getRoom().getRoomId());
				if(userIdList!=null&&userIdList.size()!=0) {
					for(String userId:userIdList) {
						boolean flag=true;
						while (flag) {
							String requestionIdString=UUID.randomUUID().toString();
							if(JedisUtils.tryGetDistributedLock(userId+"lock", requestionIdString, 1)) {
								
								JedisUtils.listAdd(packet.getRoom().getRoomId()+":"+userId, iLiveMessage.getMsgId()+"");
								flag=false;
								JedisUtils.releaseDistributedLock(userId+"lock", requestionIdString);
							}else {
								try {
									Thread.sleep(100);
								} catch (Exception e) {
									e.printStackTrace();
								}
								
							}
						}
					}
				}
				
			}else {
				while (userIterator.hasNext()) {
					String key = userIterator.next();
					UserBean user = concurrentHashMap.get(key);
					
						List<ILiveMessage> msgList = user.getMsgList();
						if (msgList == null) {
							msgList = new ArrayList<ILiveMessage>();
						}
						msgList.add(iLiveMessage);
					
					
				}
			}
			
			resultJson.put("code", 1);
			resultJson.put("message", "修改成功");
			resultJson.put("data",new JSONObject() );
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", 2);
			resultJson.put("message", "修改失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	/**
	 * 关闭评论活动
	 * @param id
	 * @param isSwitch
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="closeComment.jspx")
	public void closeComment(Integer id,Integer isSwitch,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveComment comment =commentMng.getIsStart(id);
			comment.setIsAllow(isSwitch);
			commentMng.update(comment);
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap.get(comment.getRoom().getRoomId());
			Iterator<String> userIterator = concurrentHashMap.keySet().iterator();
			ILiveMessage iLiveMessage = new ILiveMessage();
			iLiveMessage.setRoomType(17);
			if("open".equals(ConfigUtils.get("redis_service"))) {
				iLiveMessage.setMsgId(Long.parseLong("-"+comment.getRoom().getRoomId()+""+comment.getRoom().getRoomId()));
				
				JedisUtils.delObject("msg:"+iLiveMessage.getMsgId());
				JedisUtils.setByte(("msg:"+iLiveMessage.getMsgId()).getBytes(), SerializeUtil.serialize(iLiveMessage), 0);
				Set<String> userIdList =JedisUtils.getSet("userIdList"+comment.getRoom().getRoomId());
				if(userIdList!=null&&userIdList.size()!=0) {
					for(String userId:userIdList) {
						boolean flag=true;
						while (flag) {
							String requestionIdString=UUID.randomUUID().toString();
							if(JedisUtils.tryGetDistributedLock(userId+"lock", requestionIdString, 1)) {
								
								JedisUtils.listAdd(comment.getRoom().getRoomId()+":"+userId,iLiveMessage.getMsgId()+"");
								flag=false;
								JedisUtils.releaseDistributedLock(userId+"lock", requestionIdString);
							}else {
								try {
									Thread.sleep(100);
								} catch (Exception e) {
									e.printStackTrace();
								}
								
							}
						}
					}
				}
				
			}else {
				while (userIterator.hasNext()) {
					String key = userIterator.next();
					UserBean user = concurrentHashMap.get(key);
					
						List<ILiveMessage> msgList = user.getMsgList();
						if (msgList == null) {
							msgList = new ArrayList<ILiveMessage>();
						}
						msgList.add(iLiveMessage);
					
					
				}
			}
			
			resultJson.put("code", 1);
			resultJson.put("message", "修改成功");
			resultJson.put("data",new JSONObject() );
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", 2);
			resultJson.put("message", "修改失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	
	/**
	 * 判断该直播间是否有开启的抽奖活动、投票活动
	 * @param request
	 * @param response
	 * @param roomId
	 */
	@RequestMapping(value="isStartPrizr.jspx")
	public void isStartPrize(HttpServletRequest request,HttpServletResponse response,Integer roomId) {
		JSONObject resultJson = new JSONObject();
		try {
			if(JedisUtils.exists("isStartPrizr:"+roomId)) {
				
				ResponseUtils.renderJson(request, response, JedisUtils.get("isStartPrizr:"+roomId));
				return;
			}else {
				ILiveLiveRoom live = iLiveRoomMng.findById(roomId);
				if (live == null) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "直播不存在");
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}
				// 查询开启的抽奖
				//ILiveLottery startPrize = iLivePrizeMng.isStartPrize2(live.getEnterpriseId());
				// 查询开启的投票
				//ILiveVoteActivity activity = iLiveVoteActivityMng.getActivityByRoomId(roomId);
				//ILiveVoteActivity activity = iLiveVoteActivityMng.getActivityByenterpriseId(live.getEnterpriseId());
	            //ILiveSignin signin =signinMng.getIsStart(roomId);
	            ILiveRedPacket packet=packetMng.getIsStart(roomId);
				JSONObject res = new JSONObject();
				boolean startPrize1 = iLivePrizeRoomMng.isStartPrize(roomId);
				boolean startSign=iLiveSignRoomMng.isStartSign(roomId);
				boolean startQuestionnaire=iLiveQuestionnaireRoomMng.isStartQuestionnaire(roomId);
				if (!startPrize1) {
					res.put("isStartPrizr", 0);
				} else {
					res.put("isStartPrizr", 1);
				}
				boolean vote = iLiveVoteRoomMng.isStartVote(roomId);
				if (!vote) {
					res.put("isStartVote", 0);
				} else {
					res.put("isStartVote", 1);
				}
				if (!startSign) {
					res.put("isStartSign", 0);
				} else {
					res.put("isStartSign", 1);
				}
				if (packet == null) {
					res.put("isStartRedPacket", 0);
				} else {
					res.put("isStartRedPacket", 1);
				}
				if (!startQuestionnaire) {
					res.put("isStartQuestionnaire", 0);
				} else {
					res.put("isStartQuestionnaire", 1);
				}
				//礼物是否开启
				res.put("isStartQuestionAnswer", live.getIsSystemGift()==null?0:live.getIsSystemGift());
				//打赏是否开启
				ILivePlayRewards iLivePlayRewards = iLivePlayRewardsMng.selectILivePlayRewardsById(roomId);
				if(iLivePlayRewards==null) {
					res.put("isPlayRewards", ILivePlayRewards.ROOM_REWARDS_SWITCH_OFF);
				}else {
					res.put("isPlayRewards", iLivePlayRewards.getRewardsSwitch()==null?ILivePlayRewards.ROOM_REWARDS_SWITCH_OFF:iLivePlayRewards.getRewardsSwitch());
				}
				// 添加抽奖页面地址
				String appLotteryAddr;
				try {
					ILiveServerAccessMethod accessMethodBySever = accessMethodMng
							.getAccessMethodBySeverGroupId(live.getServerGroupId());
					appLotteryAddr = accessMethodBySever.getH5HttpUrl() + "/phone" + "/appLottery.html?roomId=" + roomId;
				} catch (Exception e) {
					appLotteryAddr = "";
				}
				res.put("appLotteryAddr", appLotteryAddr);
				// 添加投票页面地址
				String appVoteAddr;
				try {
					ILiveServerAccessMethod accessMethodBySever = accessMethodMng
							.getAccessMethodBySeverGroupId(live.getServerGroupId());
					appVoteAddr = accessMethodBySever.getH5HttpUrl() + "/phone" + "/appVote.html?roomId=" + roomId;
				} catch (Exception e) {
					appVoteAddr = "";
				}
				res.put("appVoteAddr", appVoteAddr);
				// 添加问答页面地址
				String appQuestionAnswerAddr;
				try {
					ILiveServerAccessMethod accessMethodBySever = accessMethodMng
							.getAccessMethodBySeverGroupId(live.getServerGroupId());
					appQuestionAnswerAddr = accessMethodBySever.getH5HttpUrl() + "/phone"
							+ "/appQuestionAnswer.html?roomId=" + roomId;
				} catch (Exception e) {
					appQuestionAnswerAddr = "";
				}
				res.put("appQuestionAnswerAddr", appQuestionAnswerAddr);

				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "查询成功");
				resultJson.put("data", res);
				JedisUtils.set("isStartPrizr:"+roomId, resultJson.toString(), 120);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "查询失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}

	/**
	 * 查询开启的抽奖活动
	 * @param roomId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="getPrize.jspx")
	public void getPrize(Integer roomId,Long userId,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		JSONObject res;
		try {
			ILiveLiveRoom room = iLiveRoomMng.findById(roomId);
			Integer enterpriseId = room.getEnterpriseId();
			
			//获取抽奖内容
			//ILiveLottery prize = iLivePrizeMng.isStartPrize(roomId);
			ILiveLottery prize = null;
			ILivePrizeRoom selectStartPrize = iLivePrizeRoomMng.selectStartPrize(roomId);
			if (selectStartPrize!=null) {
				prize = iLivePrizeMng.getPrize(selectStartPrize.getPrizeId());
			}
			if (prize!=null) {
				//获取抽奖奖品
				List<ILiveLotteryPrize> list = iLiveLotteryPrizeMng.getListByLotteryId(prize.getId());
				if(list.size()<8) {
					for(int i=list.size();i<8;i++) {
						ILiveLotteryPrize p = new ILiveLotteryPrize();
						p.setPrizeName("谢谢惠顾");
						p.setPrizeTypeName("谢谢惠顾");
						list.add(p);
					}
					
				}
				List<ILiveLotteryPrize> list2 = new ArrayList<ILiveLotteryPrize>();
				for(int i=0;i<8;i++) {
					ILiveLotteryPrize p = new ILiveLotteryPrize();
					p.setPrizeName("谢谢惠顾");
					p.setPrizeTypeName("谢谢惠顾");
					list2.add(p);
				}
				switch (list.size()) {
					case 1:
						list2.set(0, list.get(0));
					break;
					case 2:
						list2.set(0, list.get(0));
						list2.set(2, list.get(1));
					break;
					case 3:
						list2.set(0, list.get(0));
						list2.set(2, list.get(1));
						list2.set(4, list.get(2));
					break;
					case 4:
						list2.set(0, list.get(0));
						list2.set(2, list.get(1));
						list2.set(4, list.get(2));
						list2.set(6, list.get(3));
					break;
					case 5:
						list2.set(0, list.get(0));
						list2.set(2, list.get(1));
						list2.set(4, list.get(2));
						list2.set(6, list.get(3));
						list2.set(7, list.get(4));
					break;
					case 6:
						list2.set(0, list.get(0));
						list2.set(2, list.get(1));
						list2.set(4, list.get(2));
						list2.set(6, list.get(3));
						list2.set(7, list.get(4));
						list2.set(1, list.get(5));
					break;
					case 7:
						list2.set(0, list.get(0));
						list2.set(2, list.get(1));
						list2.set(4, list.get(2));
						list2.set(6, list.get(3));
						list2.set(7, list.get(4));
						list2.set(1, list.get(5));
						list2.set(3, list.get(6));
					break;
					case 8:
						list2.set(0, list.get(0));
						list2.set(2, list.get(1));
						list2.set(4, list.get(2));
						list2.set(6, list.get(3));
						list2.set(7, list.get(4));
						list2.set(1, list.get(5));
						list2.set(3, list.get(6));
						list2.set(5, list.get(7));
					break;
				default:
					break;
				}
				prize.setList(list2);
				//格式化时间
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String startTime = format.format(prize.getStartTime());
				String endTime = format.format(prize.getEndTime());
				String updateTime = format.format(prize.getUpdateTime());
				String createTime = format.format(prize.getCreateTime());
				res = JSONObject.fromObject(prize);
				res.put("startTime", startTime);
				res.put("endTime", endTime);
				res.put("updateTime", updateTime);
				res.put("createTime", createTime);
				//获取该userId抽奖详情
				ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
				//额外的抽奖机会
				int extra = 0;
				ILiveLotteryShare record = iLiveLotteryShareMng.getRecordByUserIdAndLotteryId(userId, prize.getId());
				if(record!=null) {
					extra = 1;
				}
				//总共能抽多少次奖
				int userNum = prize.getLotteryNum()+extra;
				if (iLiveManager!=null && iLiveManager.getMobile()!=null) {
					List<ILiveLotteryParktake> userList = iLiveLotteryParktakeMng.getListByLotteryIdAndPhone(prize.getId(),iLiveManager.getMobile());
					if(!userList.isEmpty()) {
						userNum-=userList.size();
					}
				}
				if (userNum<=0) {
					res.put("userNum", 0);
				}else {
					res.put("userNum", userNum);
				}
				
			}else {
				res = new JSONObject();
			}
			Timestamp startTime = prize.getStartTime();
			Timestamp endTime = prize.getEndTime();
			Date date = new Date();
			if(date.getTime()>startTime.getTime()&&date.getTime()<endTime.getTime()) {
				//进行中
				resultJson.put("prizeStatus", 0);
			}else if(date.getTime()<startTime.getTime()) {
				//未开始
				resultJson.put("prizeStatus", 1);
			}else if(date.getTime()>endTime.getTime()) {
				//抽奖结束
				resultJson.put("prizeStatus", 2);
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "查询成功");
			resultJson.put("data", res.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "查询失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	
	/**
	 * 根据奖品id获取奖品信息
	 * @param id
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="getLotteryPrize.jspx")
	public void getLotteryPrize(Long id,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveLotteryPrize lotteryPrize = iLiveLotteryPrizeMng.getById(id);
			
			if(lotteryPrize!=null) {
				JSONObject res = JSONObject.fromObject(lotteryPrize);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
				String createTime = format.format(lotteryPrize.getCreateTime());
				res.put("createTime", createTime);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "查询成功");
				resultJson.put("data", res.toString());
			}else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "该奖品不存在");
				resultJson.put("data", new JSONObject());
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "查询失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	
	/**
	 * 根据抽奖活动id获取奖品列表
	 * @param lotteryId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="getPrizeList.jspx")
	public void getPrizeList(Long lotteryId,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			List<ILiveLotteryPrize> list = iLiveLotteryPrizeMng.getListByLotteryId(lotteryId);
			JSONArray jsonArray = JSONArray.fromObject(list);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "查询成功");
			resultJson.put("data", jsonArray.toString());
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "查询失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	
	/**
	 * 抽奖 根据概率获取奖品
	 * @param userId
	 * @param lotteryId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="userLottery.jspx")
	public void userLottery(Long userId,Integer roomId,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		JSONObject res = new JSONObject();
		try {
			ILiveLiveRoom room = iLiveRoomMng.findById(roomId);
			ILiveLottery prize = null;
			if (room!=null) {
				ILivePrizeRoom selectStartPrize = iLivePrizeRoomMng.selectStartPrize(roomId);
				if (selectStartPrize!=null) {
					prize = iLivePrizeMng.getPrize(selectStartPrize.getPrizeId());
				}
			}
			Long lottery = iLivePrizeMng.lottery(userId,prize.getId());
			
			if (lottery == -1) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户未设置手机号");
				resultJson.put("data", res.toString());
			}else if (lottery == -2) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户不存在");
				resultJson.put("data", res.toString());
			}else {
				res.put("prize", lottery);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "抽奖成功");
				resultJson.put("data", res.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "抽奖失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	
	/**
	 * 分享后抽奖次数加一
	 * @param roomId
	 * @param userId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="share.jspx")
	public void share(Integer roomId, Long userId,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			iLiveLotteryShareMng.share(roomId, userId);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "添加抽奖次数成功");
			resultJson.put("data", new JSONObject());
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "添加抽奖次数失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	
	private String getQueryString(String parmas, String name) {
		String value = null;
		if (null != parmas && null != name) {
			String str;
			if (parmas.indexOf("?") != -1) {
				str = parmas.substring(parmas.indexOf("?") + 1);
			} else {
				str = parmas;
			}
			String[] strs = str.split("&");
			for (int i = 0; i < strs.length; i++) {
				if (name.equals(strs[i].split("=")[0])) {
					value = strs[i].split("=")[1];
					break;
				}
			}
		}
		return value;
	}
	
	private AuthBean checkAuthString(String authString) {
		AuthBean authBean = new AuthBean();
		if (StringUtils.isNotBlank(authString)) {
			try {
				String[] array = authString.split("@");
				String timeStr = array[0].trim().substring(4, array[0].length() - 4);
				long warpnum = 5000;
				long time = Long.parseLong(timeStr);
				authBean.setTime(time);
				long currentTimeMillis = System.currentTimeMillis() / 1000;
				if (time < (currentTimeMillis - warpnum) || time > (currentTimeMillis + warpnum)) {
					authBean.setResult(false);
					return authBean;
				}
				String paramString = array[1].trim();
				authBean.setParamString(paramString);
				String md5Str = array[2].trim();
				String encodeMd5Str = Md5Util.encode(timeStr + "_chinanet_2018_jwzt_" + paramString);
				if (encodeMd5Str.equalsIgnoreCase(md5Str)) {
					authBean.setResult(true);
					return authBean;
				}
			} catch (Exception e) {
				e.printStackTrace();
				authBean.setResult(false);
				return authBean;
			}
		}
		authBean.setResult(false);
		return authBean;
	}
	
}
