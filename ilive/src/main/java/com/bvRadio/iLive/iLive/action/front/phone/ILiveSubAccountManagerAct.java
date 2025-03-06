package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.bvRadio.iLive.common.web.RequestUtils;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveRoomRegister;
import com.bvRadio.iLive.iLive.entity.ILiveSubAccountManager;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILiveRoomRegisterService;
import com.bvRadio.iLive.iLive.manager.ILiveSubAccountManagerMng;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUserViewStatics;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.bvRadio.iLive.iLive.web.PostMan;
import com.bvRadio.iLive.iLive.web.vo.UserBaseInfo;
import com.thoughtworks.xstream.mapper.Mapper.Null;

@Controller
@RequestMapping(value = "/subAccount")
public class ILiveSubAccountManagerAct {

	@Autowired
	private ILiveSubAccountManagerMng managerMng; // 签到、点赞
	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	

	// 子账户申请
	@RequestMapping(value = "add.jspx")
	public void register(Long userId, Integer enterpriseId, String  reson,String  contactPhone,String  name, HttpServletRequest request,
			HttpServletResponse response) {
		UserBean userBean = ILiveUtils.getAppUser(request);
		
		JSONObject resultJson = new JSONObject();
		if (userBean == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户未登录");
			resultJson.put("data", new JSONObject());
		} else {
			ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(userId);
			Integer cer=iLiveManager.getCertStatus();
			if(cer==4){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "认证通过用户不能申请");
				resultJson.put("data", new JSONObject());
			}else{
				//更新状态为申请中
				iLiveManager.setCertStatus(3);
				iLiveManagerMng.updateLiveManager(iLiveManager);
				ILiveSubAccountManager list=managerMng.getILiveSubAccountManager(iLiveManager.getMobile());
				if(list==null){
					try {
						ILiveSubAccountManager manager=new ILiveSubAccountManager();
						Long id=managerMng.selectMaxId();
						if(id==null){
							id=1L;
						}
						manager.setId(id);
						manager.setEnterpriseId(enterpriseId);
						manager.setUser(iLiveManager.getMobile());
						manager.setContactPhone(contactPhone);
						manager.setCreatTime(new Timestamp(System.currentTimeMillis()));
						manager.setProcess(1);
						manager.setResult(2);
						manager.setReason(reson);
						manager.setName(name);
						managerMng.addILiveSubAccountMng(manager);
						resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "申请成功");
						resultJson.put("data", new JSONObject());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "申请失败");
						resultJson.put("data", new JSONObject());
					}
				}else{
					try {
						list.setEnterpriseId(enterpriseId);
						list.setUser(iLiveManager.getMobile());
						list.setContactPhone(contactPhone);
						list.setCreatTime(new Timestamp(System.currentTimeMillis()));
						list.setProcess(1);
						list.setResult(2);
						list.setName(name);
						list.setReason(reson);
						managerMng.updateILiveSubAccountMng(list);
						resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "申请成功");
						resultJson.put("data", new JSONObject());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "申请失败");
						resultJson.put("data", new JSONObject());
					}
				}
			}
			
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
/**
 * 查询用户申请状态
 */
	@RequestMapping(value = "checkSubAcountStatus.jspx")
	public void checkSubAcountStatus(Long userId,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		UserBean userBean = ILiveUtils.getAppUser(request);
		if (userBean == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户未登录");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		try {
			ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(userId);
			//查询用户申请表
			Integer data=null;
			ILiveSubAccountManager list=managerMng.getILiveSubAccountManager(iLiveManager.getMobile());
			if(list==null){
				data=0;
			}else{
				Integer status=list.getResult();
				if(status==0){
					data=1;
				}else if(status==1){
					data=2;
				}else if(status==2){
					data=3;
				}
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", list.getRefuse()==null?"":list.getRefuse());
			resultJson.put("data", data.toString());
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "内部异常");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	

}
