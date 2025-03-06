package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.common.page.SimplePage.cpn;
import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveSubLevel;
import com.bvRadio.iLive.iLive.entity.IliveSubRoom;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.AssistentLoginMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubAccountMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubLevelMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubRoomMng;
import com.bvRadio.iLive.iLive.util.SystemMd5Util;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
/**
 * 协调管理
 * @author YanXL
 *
 */
@Controller
@RequestMapping(value="collaborative")
public class AssistentLoginController {
	@Autowired
	private AssistentLoginMng assistentLoginMng;
	@Autowired
	private ILiveManagerMng iLiveManagerMng; 
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;
	/**
	 * 协同助手
	 * @param map
	 * @param roomId 直播间ID
	 * @return
	 */
	@RequestMapping(value="assistant/open.do")
	public String selectOpenAssistent(ModelMap map,Integer roomId,HttpServletRequest request){
		try {
			List<ILiveManager> list = iLiveManagerMng.selectILiveManagerByRoomId(roomId,ILiveManager.USER_LEVEL_Assistant);
			ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
			map.put("iLiveManagers", list);
			Integer isAssistant = iLiveLiveRoom.getIsAssistant();
			isAssistant = isAssistant==null?0:isAssistant;
			iLiveLiveRoom.setIsAssistant(isAssistant);
			map.put("iLiveLiveRoom", iLiveLiveRoom);
			UserBean userBean = ILiveUtils.getUser(request);
			map.addAttribute("userBean", userBean);
			map.addAttribute("leftActive", "7_2");
			map.addAttribute("topActive", "1");
			map.put("loginUrl", ConfigUtils.get("mpSystemAddr")+"/admin/assistant/login.do?roomId="+roomId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "assistant/assistant_open";
	}
	/**
	 * 修改助手启动状态
	 * @param map
	 * @param roomId 直播间ID
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="assistant/update.do")
	public void updateIsAssistent(ModelMap map,Integer roomId,Integer isAssistant,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
			iLiveLiveRoom.setIsAssistant(isAssistant);
			iLiveLiveRoomMng.update(iLiveLiveRoom);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "修改成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "新增管理失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 新增邀请码
	 * @param roomId 直播加ID
	 * @param invitationCode 邀请码
	 * @param request	
	 * @param response
	 */
	@RequestMapping(value="assistant/save.do")
	public void addAssistent(Integer roomId,String invitationCode,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			if(invitationCode==null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "助手邀请码不能为空");
			}
			ILiveManager iLiveManager = iLiveManagerMng.getILiveMangerByMobile(invitationCode);
			if(iLiveManager!=null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "邀请码已被注册");
			}else{
				UserBean userBean = ILiveUtils.getUser(request);
				String userId = userBean.getUserId();
				ILiveManager manager = iLiveManagerMng.selectILiveManagerById(Long.valueOf(userId));
				iLiveManager = new ILiveManager();
				iLiveManager.setAppSecret(manager.getAppSecret());
				iLiveManager.setBirthday(manager.getBirthday());
				iLiveManager.setCalendar(manager.getCalendar());
				iLiveManager.setCertStatus(manager.getCertStatus());
				iLiveManager.setConstellatory(manager.getConstellatory());
				iLiveManager.setEditUserNameIdentify(manager.getEditUserNameIdentify());
				iLiveManager.setEduLevel(manager.getEduLevel());
				iLiveManager.setEmail(manager.getEmail());
				iLiveManager.setEnterPrise(manager.getEnterPrise());
				iLiveManager.setEnterpriseId(manager.getEnterpriseId());
				iLiveManager.setErrorNum(manager.getErrorNum());
				iLiveManager.setIncomeLevel(manager.getIncomeLevel());
				iLiveManager.setIsDel(manager.getIsDel());
				iLiveManager.setJpushId(invitationCode);
				iLiveManager.setMobile(invitationCode);
				iLiveManager.setLastLoginTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
				iLiveManager.setLoginToken(manager.getLoginToken());
				iLiveManager.setLoginType(manager.getLoginType());
				iLiveManager.setOccupation(manager.getOccupation());
				iLiveManager.setParentId(manager.getParentId());
				iLiveManager.setRoomId(manager.getRoomId());
				iLiveManager.setRootId(manager.getRootId());
				iLiveManager.setSex(manager.getSex());
				iLiveManager.setSimpleEnterpriseName(manager.getSimpleEnterpriseName());
				iLiveManager.setTerminalType("web");
				iLiveManager.setUpdatePasswordTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
				iLiveManager.setUserName(invitationCode);
				iLiveManager.setNailName(invitationCode);
				iLiveManager.setSaltPwd("123456");
				String salt = String.valueOf(new Random().nextInt(2000));
				iLiveManager.setSalt(salt);
				String md5Pwd = SystemMd5Util.md5("123456", salt);
				iLiveManager.setUserPwd(md5Pwd);
				iLiveManager.setLevel(ILiveManager.USER_LEVEL_Assistant);
				String userImage = ConfigUtils.get("defaultTerminalUserImg");
				iLiveManager.setUserImg(userImage);
				iLiveManager.setCreateTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
				iLiveManager.setRoomId(roomId);
				boolean b = iLiveSubAccountMng.addILiveSubAccountMng(iLiveManager);
				if(b){
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "成功");
				}else{
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "添加失败");
				}
				
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "添加成功");
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "新增管理失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 删除邀请码
	 * @param map
	 * @param userId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="assistant/delete.do")
	public void addAssistent(ModelMap map,Long userId,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			iLiveManagerMng.deleteILiveManager(userId);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "删除成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "删除失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 协调登录记录
	 * @param map
	 * @param pageNo
	 * @param roomId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="assistant/page.do")
	public String selectAssistentByPage(ModelMap map,Integer pageNo,Integer roomId,HttpServletRequest request,HttpServletResponse response){
		try {
			Pagination pagination = assistentLoginMng.selectAssistentLoginByPage(roomId,cpn(pageNo), 20);
			map.put("pagination", pagination);
			ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
			map.put("iLiveLiveRoom", iLiveLiveRoom);
			UserBean userBean = ILiveUtils.getUser(request);
			map.addAttribute("userBean", userBean);
			map.addAttribute("leftActive", "7_2");
			map.addAttribute("topActive", "1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "assistant/assistant_login_page";
	}
	
	/**
	 * 协调管理 用户 列表
	 * @param roomId 直播加ID
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="list.do")
	public String selectCollaborative(ModelMap map,Integer roomId,HttpServletRequest request,HttpServletResponse response){
		try {
			List<ILiveManager> list = iLiveManagerMng.selectILiveManagerByRoomId(roomId,ILiveManager.USER_LEVEL_SUB);
			map.put("iLiveManagers", list);
			
			ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
			map.put("iLiveLiveRoom", iLiveLiveRoom);
			UserBean userBean = ILiveUtils.getUser(request);
			java.util.List<ILiveManager> sublist = iLiveSubAccountMng.selectILiveManagerPage(userBean.getEnterpriseId());
			//添加查询子账户现有个数
			Integer num=sublist.size();
			map.addAttribute("subNum", num);
			map.put("loginUrl", ConfigUtils.get("mpSystemAddr")+"/admin/login.do?logintype=1");
			map.addAttribute("userBean", userBean);
			map.addAttribute("leftActive", "7_3");
			map.addAttribute("topActive", "1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "assistant/assistant_list";
	}
	@Autowired
	private ILiveSubAccountMng iLiveSubAccountMng;
	@Autowired
	private ILiveSubRoomMng iLiveSubRoomMng;
	@RequestMapping(value="subsave.do")
	public void addsubCollaborative(ModelMap map,Integer roomId,String userName,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		//通过username去获取password
		try {
			
			ILiveManager iLiveManager=iLiveManagerMng.getILiveManagerByUserName(userName);
			//将iliveroom设置中的子账号id添加进去
			
			iLiveManager.setJpushId(userName);
			iLiveManager.setTerminalType("web");
			iLiveManager.setUserName(userName);
			iLiveManager.setNailName(userName);
			iLiveManager.setRoomId(roomId);
			iLiveManagerMng.updateLiveManager(iLiveManager);
			//在房间号和子账号的关联表中添加一条数据
			IliveSubRoom iLiveSubRoom=new IliveSubRoom();
			Integer id=iLiveSubRoomMng.selectMaxId();
			if(id==null) {
				id=1;
			}else {
				id=id+1;
			}
			iLiveSubRoom.setId(id);
			iLiveSubRoom.setSubAccountId(iLiveManager.getUserId());
			iLiveSubRoom.setLiveRoomId(roomId);
			iLiveSubRoomMng.save(iLiveSubRoom);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "从子账户新增管理失败");
			e.printStackTrace();
		}
		
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	@Autowired 
	private ILiveSubLevelMng iLiveSubLevelMng;
	@Autowired
	private ILiveFieldIdManagerMng fieldManagerMng;
	
	/**
	 * 新增子用户
	 * @param map
	 * @param roomId
	 * @param userName
	 * @param password
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="save.do")
	public void addCollaborative(ModelMap map,Integer roomId,String userName,String phoneNum,String nailName,String password,String realName,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			if(userName==null||roomId==null||password==null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "新增信息错误");
			}
			ILiveManager iLiveManager = iLiveManagerMng.getILiveManagerByUserName(userName);
			iLiveManager = iLiveManagerMng.getILiveManagerByUserName(userName);
			if(iLiveManager!=null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "新增账户已存在");
			}else{
				ILiveManager iLiveMessage;
				if(phoneNum==null) {
					iLiveMessage=null;
				}else {
					iLiveMessage= iLiveManagerMng.getILiveMangerByMobile(phoneNum);
				}
				
				if(iLiveMessage!=null){
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "用户手机号已注册");
					ResponseUtils.renderJson(response, resultJson.toString());
					return;
				}else{
				UserBean userBean = ILiveUtils.getUser(request);
				String userId = userBean.getUserId();
				//子账户权限表插入一条数据
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
				//在房间号和子账号的关联表中添加一条数据
				IliveSubRoom iLiveSubRoom=new IliveSubRoom();
				
				Integer id1=iLiveSubRoomMng.selectMaxId();
				if(id1==null) {
					id1=1;
				}else {
					id1=id1+1;
				}
				iLiveSubRoom.setId(id1);
				iLiveSubRoom.setSubAccountId(UserId+1);
				iLiveSubRoom.setLiveRoomId(roomId);
				iLiveSubRoomMng.save(iLiveSubRoom);
				ILiveManager manager = iLiveManagerMng.selectILiveManagerById(Long.valueOf(userId));
				iLiveManager = new ILiveManager();
				iLiveManager.setAppSecret(manager.getAppSecret());
				iLiveManager.setBirthday(manager.getBirthday());
				iLiveManager.setCalendar(manager.getCalendar());
				iLiveManager.setCertStatus(manager.getCertStatus());
				iLiveManager.setConstellatory(manager.getConstellatory());
				iLiveManager.setEditUserNameIdentify(manager.getEditUserNameIdentify());
				iLiveManager.setEduLevel(manager.getEduLevel());
				iLiveManager.setEmail(manager.getEmail());
				iLiveManager.setEnterPrise(manager.getEnterPrise());
				iLiveManager.setEnterpriseId(manager.getEnterpriseId());
				iLiveManager.setErrorNum(manager.getErrorNum());
				iLiveManager.setIncomeLevel(manager.getIncomeLevel());
				iLiveManager.setIsDel(manager.getIsDel());
				iLiveManager.setJpushId(userName);
				iLiveManager.setLastLoginTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
				iLiveManager.setLoginToken(manager.getLoginToken());
				iLiveManager.setLoginType(manager.getLoginType());
				iLiveManager.setOccupation(manager.getOccupation());
				iLiveManager.setParentId(manager.getParentId());
				iLiveManager.setRoomId(roomId);
				iLiveManager.setRootId(manager.getRootId());
				iLiveManager.setSex(manager.getSex());
				iLiveManager.setSimpleEnterpriseName(manager.getSimpleEnterpriseName());
				iLiveManager.setTerminalType("web");
				iLiveManager.setUpdatePasswordTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
				iLiveManager.setUserName(userName);
				iLiveManager.setNailName(nailName);
				iLiveManager.setSaltPwd(password);
				iLiveManager.setMobile(phoneNum);
				iLiveManager.setRealName(realName);
				manager.setSubTop("0");
				manager.setSubLeft("0");
				String salt = String.valueOf(new Random().nextInt(2000));
				iLiveManager.setSalt(salt);
				String md5Pwd = SystemMd5Util.md5(password, salt);
				iLiveManager.setUserPwd(md5Pwd);
				iLiveManager.setLevel(ILiveManager.USER_LEVEL_SUB);
				String userImage = ConfigUtils.get("defaultTerminalUserImg");
				iLiveManager.setUserImg(userImage);
				iLiveManager.setCreateTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
				iLiveManager.setSubRoomIds(iLiveManager.getSubRoomIds()+"&"+roomId);
				boolean b = iLiveSubAccountMng.addILiveSubAccountMng(iLiveManager);
				if(b){
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "成功");
				}else{
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "添加失败");
				}
			}
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "新增管理失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 修改协调管理员密码
	 * @param userId 用户ID
	 * @param password 用户密码
	 * @param request 
	 * @param response
	 */
	@RequestMapping(value="update/password.do")
	public void updateILiveManagerPassword(Long userId,String password,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(userId);
			String salt = iLiveManager.getSalt();
			String md5Pwd = SystemMd5Util.md5(password, salt);
			iLiveManager.setUserPwd(md5Pwd);
			iLiveManager.setSaltPwd(password);
			iLiveManagerMng.updateLiveManager(iLiveManager);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "重置密码成功");
			resultJson.put("loginUrl", ConfigUtils.get("mpSystemAddr")+"/admin/login.do?logintype=1");
			resultJson.put("userName", iLiveManager.getUserName());
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "修改失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 删除协调管理员密码
	 * @param userId 用户ID
	 * @param request 
	 * @param response
	 */
	@RequestMapping(value="delete.do")
	public void deleteILiveManagerPassword(String roomId,Long userId,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			//删除关联表中相应的记录
			iLiveSubRoomMng.delete(userId,roomId);
			
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "操作成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "修改失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 解除子账户和直播间的关联
	 * @param userId 用户ID
	 * @param request 
	 * @param response
	 */
	@RequestMapping(value="relieve.do")
	public void relieveILiveManagerRoom(String roomId,Long userId,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			//删除关联表中相应的记录
			iLiveSubRoomMng.delete(userId,roomId);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "解除成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "修改失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
}
