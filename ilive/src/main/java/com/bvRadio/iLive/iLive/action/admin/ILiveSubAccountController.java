package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.common.page.SimplePage.cpn;
import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.formula.functions.T;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.action.util.SubAccountCache;
import com.bvRadio.iLive.iLive.dao.ILiveSubLevelDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveSubLevel;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubAccountMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubLevelMng;
import com.bvRadio.iLive.iLive.util.SystemMd5Util;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.google.gson.Gson;

import antlr.collections.List;
/**
 * 子账户管理
 * @author Administrator
 *
 */
@Controller
public class ILiveSubAccountController {
	@Autowired
	private ILiveSubAccountMng iLiveSubAccountMng;
	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;
	/**
	 * 子账户数据
	 * @param pageNo
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/sub/pageSub.do")
	public String selectILiveManagerPage(Integer pageNo,HttpServletRequest request, ModelMap map){
		UserBean userBean = ILiveUtils.getUser(request);
		Integer enterpriseId = userBean.getEnterpriseId();
		Pagination pagination = iLiveSubAccountMng.selectILiveManagerPage( cpn(pageNo),20,enterpriseId);
		java.util.List<ILiveManager> list = iLiveSubAccountMng.selectILiveManagerPage(enterpriseId);
		//添加查询子账户现有个数
		Integer num=list.size();
		map.addAttribute("subNum", num);
		map.addAttribute("pager", pagination);
		map.addAttribute("topActive", "6");
		map.addAttribute("leftActive", "8_2");
		return "sub/page";
	}

	/**
	 * 协同管理添加子账户获取列表
	 * @param pageNo
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/sub/getSub.do")
	public void getILiveManagerPage(String roomId,Integer pageNo,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		UserBean userBean = ILiveUtils.getUser(request);
		Integer enterpriseId = userBean.getEnterpriseId();
		java.util.List<ILiveManager> iLiveManagerPage = iLiveSubAccountMng.getILiveManagerPage(enterpriseId,roomId,userBean.getUserId());
		resultJson.put("date", iLiveManagerPage);
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	/**
	 * 子账户修改信息获取列表
	 * @param pageNo
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/sub/getSubMsg.do")
	public void getILiveManagerPageMsg(Long userId,Integer pageNo,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
//		ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(userId);
		java.util.List<ILiveManager> manager=iLiveManagerMng.getILiveManagerById(userId);
		resultJson.put("code", SUCCESS_STATUS);
		resultJson.put("date", manager);
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	@Autowired 
	private ILiveSubLevelMng iLiveSubLevelMng;
	/**
	 * 协同管理获取子账户已有权限
	 * @param pageNo
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/sub/getuseredlevel.do")
	public String getuseredlevel(Long userId,Integer pageNo,HttpServletRequest request,HttpServletResponse response, ModelMap map){
		UserBean userBean = ILiveUtils.getUser(request);
		java.util.List<ILiveManager> manager=iLiveManagerMng.getILiveManagerById(userId);
		map.addAttribute("manager", manager.get(0));
		java.util.List<ILiveSubLevel> list=iLiveSubLevelMng.selectILiveSubById(userId);
		java.util.List set=new ArrayList();
		if(list!=null&&list.size()>0) {
			for(int i=0;i<list.size();i++) {
				set.add(list.get(i).getSubTop());
			}
		}
		map.addAttribute("sublevel", set);
		map.addAttribute("topActive", "6");
		map.addAttribute("leftActive", "8_2");
		return "sub/Right";
	}
	/**
	 * 删除用户
	 * @param userId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/sub/deleteSub.do")
	public void deleteILiveManager(Long userId,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(userId);
			iLiveManager.setIsDel(1);
			iLiveManager.setMobile("");
			iLiveManagerMng.updateLiveManager(iLiveManager);
			//将子账户权限表中相应的uesrid删除 
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}

	@Autowired
	private ILiveFieldIdManagerMng fieldManagerMng;
	/**
	 * 新增子账户
	 * @param phoneNum 手机号码
	 * @param nailName 昵称
	 * @param password 密码
	 * @param realName 真实姓名
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/sub/addSub.do")
	public void addILiveManager(String userName,String phoneNum,String nailName,String password,String realName,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			ILiveManager iLiveMessage;
			if(phoneNum==null) {
				iLiveMessage=null;
			}else {
				iLiveMessage= iLiveManagerMng.getILiveMangerByMobile(phoneNum);
			}
			ILiveManager iLiveManager = iLiveManagerMng.getILiveManagerByUserName(userName);
			iLiveManager = iLiveManagerMng.getILiveManagerByUserName(userName);
			if(iLiveManager!=null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "新增账户已存在");
			}else if(iLiveMessage!=null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户手机号已注册");
				ResponseUtils.renderJson(response, resultJson.toString());
				return;
			}else{
				UserBean userBean = ILiveUtils.getUser(request);
				String userId = userBean.getUserId();
				//子账户权限表插入
				ILiveSubLevel iLiveSubLevel=new ILiveSubLevel();
				Long id=iLiveSubLevelMng.selectMaxId();
				if(id==null) {
					id=1L;
				}
				iLiveSubLevel.setId(id);
				long UserId = fieldManagerMng.getNextId("ilive_manager", "user_id", 0);
				iLiveSubLevel.setUserId(UserId+1L);
				iLiveSubLevel.setSubTop("0");
				iLiveSubLevelMng.save(iLiveSubLevel);
				iLiveMessage = iLiveManagerMng.selectILiveManagerById(Long.valueOf(userId));
				ILiveManager manager = new ILiveManager();
				manager.setAppSecret(iLiveMessage.getAppSecret());
				manager.setBirthday(iLiveMessage.getBirthday());
				manager.setCalendar(iLiveMessage.getCalendar());
				manager.setCertStatus(4);
				manager.setConstellatory(iLiveMessage.getConstellatory());
				manager.setEditUserNameIdentify(iLiveMessage.getEditUserNameIdentify());
				manager.setEduLevel(iLiveMessage.getEduLevel());
				manager.setEmail(iLiveMessage.getEmail());
				manager.setEnterPrise(iLiveMessage.getEnterPrise());
				manager.setEnterpriseId(iLiveMessage.getEnterpriseId());
				manager.setErrorNum(iLiveMessage.getErrorNum());
				manager.setIncomeLevel(iLiveMessage.getIncomeLevel());
				manager.setIsDel(iLiveMessage.getIsDel());
				manager.setJpushId(phoneNum);
				manager.setLastLoginTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
				manager.setLoginToken(iLiveMessage.getLoginToken());
				manager.setLoginType(iLiveMessage.getLoginType());
				manager.setOccupation(iLiveMessage.getOccupation());
				manager.setParentId(iLiveMessage.getParentId());
				manager.setRoomId(iLiveMessage.getRoomId());
				manager.setRootId(iLiveMessage.getRootId());
				manager.setSex(iLiveMessage.getSex());
				manager.setSimpleEnterpriseName(iLiveMessage.getSimpleEnterpriseName());
				manager.setTerminalType(iLiveMessage.getTerminalType());
				manager.setUpdatePasswordTime(iLiveMessage.getUpdatePasswordTime());
				manager.setUserName(userName);
				manager.setWxOpenId(iLiveMessage.getWxOpenId());
				manager.setWxUnionId(iLiveMessage.getWxUnionId());
				manager.setMobile(phoneNum);
				manager.setNailName(nailName);
				manager.setSubType(ILiveManager.SUB_TYPE_ON);
				manager.setSaltPwd(password);
				manager.setSubTop("0");
				manager.setSubLeft("0");
				String salt = String.valueOf(new Random().nextInt(2000));
				manager.setSalt(salt);
				String md5Pwd = SystemMd5Util.md5(password, salt);
				manager.setUserPwd(md5Pwd);
				manager.setLevel(ILiveManager.USER_LEVEL_SUB);
				String userImage = ConfigUtils.get("defaultTerminalUserImg");
				manager.setUserImg(userImage);
				manager.setCreateTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
				manager.setRealName(realName);
				boolean b = iLiveSubAccountMng.addILiveSubAccountMng(manager);
				if(b){
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "成功");
					
				}else{
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "添加失败");
				}
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	/**
	 * 修改子账户密码
	 * @param userId 用户ID
	 * @param password 新密码
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/sub/updatePwd.do",method=RequestMethod.POST)
	public void updatePassword(Long userId,String password,String phoneNum,String nailName,String realName,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			
			ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(userId);
			iLiveManager.setSaltPwd(password);
			String salt = String.valueOf(new Random().nextInt(2000));
			iLiveManager.setSalt(salt);
			String md5Pwd = SystemMd5Util.md5(password, salt);
			iLiveManager.setUserPwd(md5Pwd);
			iLiveManager.setMobile(phoneNum);
			iLiveManager.setNailName(nailName);
			iLiveManager.setRealName(realName);
			iLiveManagerMng.updateLiveManager(iLiveManager);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
			
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	/**
	 * 修改子账户权限
	 * @param userId 用户ID
	 * @param password 新密码
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/sub/updatePermission.do",method=RequestMethod.POST)
	public void updatePermission(Long userId,String creatRoom,String plateFigure,String topicWrite,String thematicPageWrite,String matrixMonitoring,
			String enterpriseInformationManagement,String subAccountManageMent,String enterpriseFansManagement,String blackListManagement,String enterpriseAccountManagement,String liveRoom,
			String recall,String picture,String document,String voteActivity,
			String rewardActivity,String pageActivity,String signActivity,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			//首先删除权限表中该子用户的所有权限
			if(userId!=null) {
				iLiveSubLevelMng.delete(userId);
				if("1".equals(creatRoom)) {
					this.savePermission(userId, SubAccountCache.ENTERPRISE_FUNCTION_creatRoom);
				}
				if("1".equals(plateFigure)) {
					this.savePermission(userId, SubAccountCache.ENTERPRISE_FUNCTION_plateFigure);
				}
				if("1".equals(topicWrite)) {
					this.savePermission(userId, SubAccountCache.ENTERPRISE_FUNCTION_topicWrite);
				}
				if("1".equals(thematicPageWrite)) {
					this.savePermission(userId, SubAccountCache.ENTERPRISE_FUNCTION_thematicPageWrite);
				}
				if("1".equals(matrixMonitoring)) {
					this.savePermission(userId, SubAccountCache.ENTERPRISE_FUNCTION_matrixMonitoring);
				}
				if("1".equals(enterpriseInformationManagement)) {
					this.savePermission(userId, SubAccountCache.ENTERPRISE_FUNCTION_enterpriseInformationManagement);
				}
				if("1".equals(subAccountManageMent)) {
					this.savePermission(userId, SubAccountCache.ENTERPRISE_FUNCTION_subAccountManageMent);
				}
				if("1".equals(enterpriseFansManagement)) {
					this.savePermission(userId, SubAccountCache.ENTERPRISE_FUNCTION_enterpriseFansManagement);
				}
				if("1".equals(blackListManagement)) {
					this.savePermission(userId, SubAccountCache.ENTERPRISE_FUNCTION_blackListManagement);
				}
				if("1".equals(enterpriseAccountManagement)) {
					this.savePermission(userId, SubAccountCache.ENTERPRISE_FUNCTION_enterpriseAccountManagement);
				}
				if("1".equals(liveRoom)) {
					this.savePermission(userId, SubAccountCache.ENTERPRISE_FUNCTION_liveRoom);
				}
				if("1".equals(recall)) {
					this.savePermission(userId, SubAccountCache.ENTERPRISE_FUNCTION_recall);
				}
				if("1".equals(picture)) {
					this.savePermission(userId, SubAccountCache.ENTERPRISE_FUNCTION_picture);
				}
				if("1".equals(document)) {
					this.savePermission(userId, SubAccountCache.ENTERPRISE_FUNCTION_document);
				}
				if("1".equals(voteActivity)) {
					this.savePermission(userId, SubAccountCache.ENTERPRISE_FUNCTION_voteActivity);
				}
				if("1".equals(rewardActivity)) {
					this.savePermission(userId, SubAccountCache.ENTERPRISE_FUNCTION_rewardActivity);
				}
				if("1".equals(pageActivity)) {
					this.savePermission(userId, SubAccountCache.ENTERPRISE_FUNCTION_pageActivity);
				}
				if("1".equals(signActivity)) {
					this.savePermission(userId, SubAccountCache.ENTERPRISE_FUNCTION_signActivity);
				}
			}
			
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	public void savePermission(Long userId,String permission) {
		ILiveSubLevel iLiveSubLevel=new ILiveSubLevel();
			Long id=iLiveSubLevelMng.selectMaxId();
			if(id==null) {
				id=1L;
			}
			iLiveSubLevel.setId(id);
			iLiveSubLevel.setUserId(userId);
			iLiveSubLevel.setSubTop(permission);
			iLiveSubLevelMng.save(iLiveSubLevel);
		
	}
	/**
	 * 子账户 启用和关闭
	 * @param userId 用户ID
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/sub/updateSubType.do",method=RequestMethod.POST)
	public void updateSubType(Long userId,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(userId);
			Integer subType = iLiveManager.getSubType();
			subType = subType==null?ILiveManager.SUB_TYPE_ON:subType;
			if(subType.equals(ILiveManager.SUB_TYPE_OFF)){
				iLiveManager.setSubType(ILiveManager.SUB_TYPE_ON);
			}else{
				iLiveManager.setSubType(ILiveManager.SUB_TYPE_OFF);
			}
			iLiveManagerMng.updateLiveManager(iLiveManager);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}

}
