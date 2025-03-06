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
import com.bvRadio.iLive.iLive.entity.ILiveSubAccountManager;
import com.bvRadio.iLive.iLive.entity.ILiveSubLevel;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubAccountManagerMng;
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
public class ILiveSubAccountManagerController {
	@Autowired
	private ILiveSubAccountManagerMng iLiveSubAccountManagerMng;
	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	@Autowired
	private ILiveSubAccountMng iLiveSubAccountMng;
	@Autowired 
	private ILiveSubLevelMng iLiveSubLevelMng;
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;
	/**
	 * 子账户数据
	 * @param pageNo
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/subAccount/submanagerPage.do")
	public String selectILiveManagerPage(Integer pageNo,HttpServletRequest request, ModelMap map){
		UserBean userBean = ILiveUtils.getUser(request);
		Integer enterpriseId = userBean.getEnterpriseId();
		Pagination pagination=null;
		try {
			pagination = iLiveSubAccountManagerMng.selectSubAccountManagerPage( cpn(pageNo),20,enterpriseId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		java.util.List<ILiveManager> list = iLiveSubAccountMng.selectILiveManagerPage(enterpriseId);
		//添加查询子账户现有个数
		Integer num=list.size();
		map.addAttribute("subNum", num);
		map.addAttribute("pager", pagination == null ?"":pagination);
		map.addAttribute("topActive", "6");
		map.addAttribute("leftActive", "8_4");
		return "sub/managerpage";
	}

	/**
	 * 审核
	 * @param pageNo
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/subAccount/result.do")
	public void getILiveManagerPage(String user,Integer result,String refuse,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		ILiveSubAccountManager manager=iLiveSubAccountManagerMng.getILiveSubAccountManager(user);
		if(result==0){//未通过
			if(refuse==null||refuse.equals("")){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "拒绝理由不能为空");
			}else{
				try {
					manager.setResult(result);
					manager.setRefuse(refuse);
					iLiveSubAccountManagerMng.updateILiveSubAccountMng(manager);
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "成功");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "失败");
				}
			}
			
		}else if(result==1){
			try {
				manager.setResult(result);
				iLiveSubAccountManagerMng.updateILiveSubAccountMng(manager);
				ILiveManager mnger=iLiveManagerMng.getILiveMangerByMobile(manager.getUser());
				//获得企业信息
				ILiveEnterprise enterprise=iLiveEnterpriseMng.getILiveEnterPrise(manager.getEnterpriseId());
				//子账户权限表插入
				ILiveSubLevel iLiveSubLevel=new ILiveSubLevel();
				Long id=iLiveSubLevelMng.selectMaxId();
				if(id==null) {
					id=1L;
				}
				iLiveSubLevel.setId(id);
				iLiveSubLevel.setUserId(mnger.getUserId());
				iLiveSubLevel.setSubTop("0");
				iLiveSubLevelMng.save(iLiveSubLevel);
				mnger.setEnterpriseId(manager.getEnterpriseId().longValue());
				mnger.setEnterPrise(enterprise);
				mnger.setLevel(ILiveManager.USER_LEVEL_SUB);
				mnger.setCertStatus(4);
				mnger.setSubType(ILiveManager.SUB_TYPE_ON);
				mnger.setSubTop("0");
				mnger.setSubLeft("0");
				iLiveManagerMng.updateLiveManager(mnger);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "成功");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "失败");
			}
		}
		resultJson.put("code", SUCCESS_STATUS);
		resultJson.put("date", manager);
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	
	

	

}
