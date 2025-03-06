package com.bvRadio.iLive.iLive.action.front.finan;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.cache.Cache;
import com.bvRadio.iLive.common.cache.CacheManager;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.admin.ILiveLoginAct;
import com.bvRadio.iLive.iLive.constants.ILiveCertStatus;
import com.bvRadio.iLive.iLive.entity.ILiveCertTopic;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveManagerState;
import com.bvRadio.iLive.iLive.entity.ILivePackageAndProduct;
import com.bvRadio.iLive.iLive.entity.WorkLog;
import com.bvRadio.iLive.iLive.manager.ILiveCertTopicMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerStateMng;
import com.bvRadio.iLive.iLive.manager.ILivePackageAndProductMng;
import com.bvRadio.iLive.iLive.manager.WorkLogMng;
import com.bvRadio.iLive.iLive.realm.UsernamePasswordLoginTypeToken;
import com.bvRadio.iLive.iLive.util.HttpUtils;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

@Controller
@RequestMapping(value = "/yyt")
public class IliveYytLoginApiController {
	/**
	 * 日志记录
	 */
	private static final Logger log = LoggerFactory.getLogger(ILiveLoginAct.class);
	
	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;
	@Autowired
	private WorkLogMng workLogMng;
	@Autowired
	private ILiveCertTopicMng iLiveCertTopicMng;
	@Autowired
	private com.bvRadio.iLive.manager.manager.ILiveEnterpriseMng ILiveEenteriseMng;
	@Autowired
	private ILivePackageAndProductMng iLivePackageAndProductMng;
	@Autowired
	private ILiveManagerStateMng iLiveManagerStateMng;
	
	@Autowired
	private ILiveFieldIdManagerMng fieldManagerMng;
	
	/**
	 * 通过营业厅新增账户
	 * @param phoneNum 手机号码
	 * @param nailName 昵称
	 * @param password 密码
	 * @param realName 真实姓名
	 * @param request
	 * @param response
	 */
	
	@RequestMapping(value="/yytregistered.jspx")
	public void addILiveManager(HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		String enterpriseName=null;
		String enterpriseRegName=null;
		String enterpriseRegNo=null;
		String contactName=null;
		String contactPhone=null;
		String packagename=null;
		String productname=null;
		String packagenum=null;
		String productnum=null;
		String phoneNum=null;
		Double paymentPrice=0.0;
		Double paymentPrice1=0.0;
		String CustID=null;
		String userName=null;
		String productId=null;
		String startTime=null;
//		if(true) {
//			resultJson.put("status", "0");
//			ResponseUtils.renderJson(response, resultJson.toString());
//			return;
//		}
		 Calendar now = Calendar.getInstance();  
		 Integer year=now.get(Calendar.YEAR);
		try {
			String msg=IOUtils.toString(request.getInputStream(), "utf-8");
	//		String msg="{'StreamingNo':'Y29ycDMwMDUzMjI$','OPFlag':'0104','ProductID':'145000180000000000000003','OfferID':'14100112','BizID':'master@22160991','AreaCode':'59403','CustID':'FJ040080082','CustAccount':'22160991','CustName':'\u8386\u7530\u9f9f\u5c71\u798f\u6e05\u5bfa','ProductInfo':{'Product':[{'ProductInstID':'1','ProductType':'enterpriseName','ProductValue':'\u8386\u7530\u9f9f\u5c71\u798f\u6e05\u5bfa','ParentType':[],'ProductParentInstID':[]},{'ProductInstID':'2','ProductType':'enterpriseRegName','ProductValue':'\u8386\u7530\u9f9f\u5c71\u798f\u6e05\u5bfa','ParentType':[],'ProductParentInstID':[]},{'ProductInstID':'3','ProductType':'enterpriseRegNo','ProductValue':'71350302782191656G','ParentType':[],'ProductParentInstID':[]},{'ProductInstID':'4','ProductType':'contactName','ProductValue':'\u91ca\u5b8f\u653f','ParentType':[],'ProductParentInstID':[]},{'ProductInstID':'5','ProductType':'contactPhone','ProductValue':'18059511288','ParentType':[],'ProductParentInstID':[]},{'ProductInstID':'6','ProductType':'package','ProductValue':'2','ParentType':[],'ProductParentInstID':[]},{'ProductInstID':'7','ProductType':'userPhone','ProductValue':'18059511288','ParentType':[],'ProductParentInstID':[]},{'ProductInstID':'8','ProductType':'contractNbr','ProductValue':'610548094','ParentType':[],'ProductParentInstID':[]}]},'ReturnStatus':'00000','Summary':'\u5ba2\u6237\u4fe1\u606f\u67e5\u8be2\u6210\u529f','TimeStamp':'20190612152106'}";
			log.info("接收到的yyt发来的字符串："+msg);
			JSONObject jsonObject = new JSONObject(msg);
			CustID=jsonObject.getString("CustID");
			userName=jsonObject.getString("CustAccount");
			productId=jsonObject.getString("ProductID");
			JSONObject jsonInfo=jsonObject.getJSONObject("ProductInfo");
		    JSONArray Product = jsonInfo.getJSONArray("Product");
//			System.out.println(Product);
			for(int i=0;i<Product.length();i++) {
//				System.out.println("第"+i+"个："+Product.get(i).toString());
				JSONObject msgObject = new JSONObject(Product.get(i).toString());
//				System.out.println("key:"+msgObject.getString("ProductType")+"======》value："+msgObject.getString("ProductValue"));
//			    log.info("key:"+msgObject.getString("ProductType")+"======》value："+msgObject.getString("ProductValue"));
				try {
					if("enterpriseName".equals(msgObject.getString("ProductType"))) {
						enterpriseName=msgObject.getString("ProductValue");
						if(enterpriseName==null||enterpriseName==""){
							resultJson.put("status", "0");
							resultJson.put("msg", "请输入有效的enterpriseName！");
							ResponseUtils.renderJson(response, resultJson.toString());
						}
				    	
				    }else if("enterpriseRegName".equals(msgObject.getString("ProductType"))) {
				    	enterpriseRegName=msgObject.getString("ProductValue");
				    	if(enterpriseRegName==null||enterpriseRegName==""){
							resultJson.put("status", "0");
							resultJson.put("msg", "请输入有效的enterpriseRegName！");
							ResponseUtils.renderJson(response, resultJson.toString());
						}
				    }else if("enterpriseRegNo".equals(msgObject.getString("ProductType"))) {
				    	enterpriseRegNo=msgObject.getString("ProductValue");
				    	if(enterpriseRegNo==null||enterpriseRegNo==""){
							resultJson.put("status", "0");
							resultJson.put("msg", "请输入有效的enterpriseRegNo！");
							ResponseUtils.renderJson(response, resultJson.toString());
						}
				    }else if("contactName".equals(msgObject.getString("ProductType"))) {
				    	contactName=msgObject.getString("ProductValue");
				    	if(contactName==null||contactName==""){
							resultJson.put("status", "0");
							resultJson.put("msg", "请输入有效的contactName！");
							ResponseUtils.renderJson(response, resultJson.toString());
						}
				    }else if("contactPhone".equals(msgObject.getString("ProductType"))) {
				    	contactPhone=msgObject.getString("ProductValue");
				    	if(contactPhone==null||contactPhone==""){
							resultJson.put("status", "0");
							resultJson.put("msg", "请输入有效的contactPhone！");
							ResponseUtils.renderJson(response, resultJson.toString());
						}
				    }else if("package".equals(msgObject.getString("ProductType"))) {
				    	packagename=msgObject.getString("ProductValue");
				    	if(packagename==null||packagename==""){
				    		packagename="0";
						}
				    	//根据套餐种类去查找相对应的相关平台数据
				    	ILivePackageAndProduct iLivePackageAndProduct=iLivePackageAndProductMng.getMsgBypackageId(Integer.parseInt(packagename));
				    	if(iLivePackageAndProduct==null){
				    		resultJson.put("status", "0");
							resultJson.put("msg", "无此套餐");
							ResponseUtils.renderJson(response, resultJson.toString());
				    	}else{
				    		packagename=iLivePackageAndProduct.getPlapackageId().toString();
				    		paymentPrice=iLivePackageAndProduct.getPrice();
				    		packagenum="1";
				    		productnum="0";
				    	}
				    }else if("package1".equals(msgObject.getString("ProductType"))) {
				    	productnum=msgObject.getString("ProductValue");
				    	if(productnum==null||productnum==""){
				    		productnum="0";
						}
				    	//根据套餐种类去查找相对应的相关平台数据
				    	ILivePackageAndProduct iLivePackageAndProduct=iLivePackageAndProductMng.getMsgByProductId(1);
				    	if(iLivePackageAndProduct==null){
				    		resultJson.put("status", "0");
							resultJson.put("msg", "无此套餐");
							ResponseUtils.renderJson(response, resultJson.toString());
				    	}else{
				    		productname=iLivePackageAndProduct.getPlapackageId().toString();
				    		paymentPrice1=iLivePackageAndProduct.getPrice();
				    	}
				    }else if("package2".equals(msgObject.getString("ProductType"))) {
				    	productnum=msgObject.getString("ProductValue");
				    	if(productnum==null||productnum==""){
				    		productnum="0";
						}
				    	//根据套餐种类去查找相对应的相关平台数据
				    	ILivePackageAndProduct iLivePackageAndProduct=iLivePackageAndProductMng.getMsgByProductId(2);
				    	if(iLivePackageAndProduct==null){
				    		resultJson.put("status", "0");
							resultJson.put("msg", "无此套餐");
							ResponseUtils.renderJson(response, resultJson.toString());
				    	}else{
				    		productname=iLivePackageAndProduct.getPlapackageId().toString();
				    		paymentPrice1=iLivePackageAndProduct.getPrice();
				    	}
				    }else  if("userPhone".equals(msgObject.getString("ProductType"))) {
				    	phoneNum=msgObject.getString("ProductValue");
				    	if(phoneNum.length()!=11&&phoneNum!=null) {
							resultJson.put("status", "0");
							resultJson.put("msg", "请输入有效的手机号！");
							ResponseUtils.renderJson(response, resultJson.toString());
						}
				    	
				    }
				
				
				} catch (Exception e) {
					resultJson.put("status", "0");
					resultJson.putOnce("msg", e.getMessage());
					ResponseUtils.renderJson(response, resultJson.toString());
				}
			    
			}
			
			
			
			
			
			ILiveManager iLiveMessage= iLiveManagerMng.getILiveMangerByPhoneNumber(phoneNum);
			boolean ret=false;
				if(iLiveMessage!=null){//用户已存在,查看是否存在企业
				Integer enterpriseId=iLiveMessage.getEnterpriseId().intValue();
				if(enterpriseId>100) {
					//已存在 查看是否认证
					Integer cer=iLiveMessage.getCertStatus();
					if(cer==4) {
						//已经认证
						ILiveEnterprise enterprise=iLiveMessage.getEnterPrise();
						enterprise.setAutoBuy(1);
						iLiveManagerMng.updateLiveManager(iLiveMessage);
						Integer num=Integer.parseInt(productnum);
						if("145000180000000000000004".equals(productId)||"145000180000000000000005".equals(productId)){
							for(int i=0;i<num;i++){
								ret=this.yytbilling(enterpriseId,Integer.parseInt(productname),paymentPrice1,contactName,phoneNum,CustID,startTime,request, response);	
							}
						}else if("145000180000000000000002".equals(productId)||"145000180000000000000003".equals(productId)){
							ret=this.yytbilling(enterpriseId,Integer.parseInt(packagename),paymentPrice,contactName,phoneNum,CustID,startTime,request, response);
						}
						if(ret==true) {
						resultJson.put("status", "1");
						resultJson.put("msg", "订购成功");
						}else {
							resultJson.put("status", "0");
							resultJson.put("msg", "订购失败");
						}
					}else {
						//通知播管通过认证
						ILiveEnterprise enterprise1 = iLiveEnterpriseMng.getILiveEnterPrise(iLiveMessage.getEnterpriseId().intValue());
						enterprise1.setCertStatus(4);
						enterprise1.setAutoBuy(1);
						enterprise1.setCertTime(new Timestamp(System.currentTimeMillis()));
						enterprise1.setCheckPerson("admin");
						enterprise1.setAuthUserId(Long.parseLong("1263"));
						enterprise1.setAutoUserName("admin");
						enterprise1.setStamp(0);
						List<ILiveManager> managerList = iLiveManagerMng
								.getILiveManagerByEnterpriseId(enterprise1.getEnterpriseId());
						iLiveEnterpriseMng.updateEnterpriseWithPerson(enterprise1, managerList);
						ILiveCertTopic certTopic = new ILiveCertTopic();
						certTopic.setUserId(enterprise1.getUserId());
							// 审核通过增加一条记录
							certTopic.setCommentTime(new Timestamp(System.currentTimeMillis()));
							certTopic.setEnterpriseId(iLiveMessage.getEnterpriseId().intValue());
							certTopic.setManagerId(iLiveMessage.getEnterpriseId().longValue());
							certTopic.setManagerName(iLiveMessage.getUserName());
							certTopic.setTopicComment("企业认证通过");
							certTopic.setCertStatus(4);
							if (enterprise1.getStamp() == 1) {
								workLogMng.save(new WorkLog(WorkLog.MODEL_ENTEXAMINE, certTopic.getEnterpriseId()+"", net.sf.json.JSONObject.fromObject(certTopic).toString(), WorkLog.MODEL_ENTEXAMINE_NAME_PASS, Long.parseLong("1263"),"admin", ""));
							}else {
								workLogMng.save(new WorkLog(WorkLog.MODEL_PEREXAMINE, certTopic.getEnterpriseId()+"", net.sf.json.JSONObject.fromObject(certTopic).toString(), WorkLog.MODEL_PEREXAMINE_NAME_PASS, Long.parseLong("1263"),"admin", ""));
							}
						iLiveCertTopicMng.addCertTopic(certTopic);
						//购买套餐
						Integer num=Integer.parseInt(productnum);
						if("145000180000000000000004".equals(productId)||"145000180000000000000005".equals(productId)){
							for(int i=0;i<num;i++){
								ret=this.yytbilling(enterpriseId,Integer.parseInt(productname),paymentPrice1,contactName,phoneNum,CustID,startTime,request, response);	
							}
						}else if("145000180000000000000002".equals(productId)||"145000180000000000000003".equals(productId)){
							ret=this.yytbilling(enterpriseId,Integer.parseInt(packagename),paymentPrice,contactName,phoneNum,CustID,startTime,request, response);
						}
						if(ret==true) {
							resultJson.put("status", "1");
							resultJson.put("msg", "订购成功");
							}else {
								resultJson.put("status", "0");
								resultJson.put("msg", "订购失败");
							}
					}
				}else {//用户存在，但是不存在企业
					// 构建新的企业
					ILiveEnterprise enterprise = this.buildRawEnterprise(phoneNum);
					ILiveEnterprise newEnterprise = iLiveEnterpriseMng.saveILiveEnterpriseForyyt(enterprise);
					iLiveMessage.setCertStatus(2);
					iLiveMessage.setEnterPrise(newEnterprise);
					iLiveManagerMng.updateLiveManager(iLiveMessage);
					//申请认证
					ILiveEnterprise iliveEnterprise = new ILiveEnterprise();
					iliveEnterprise.setStamp(1);
					iliveEnterprise.setContactPhone(phoneNum);
					iliveEnterprise.setContactName(userName);
					iliveEnterprise.setApplyTime(new Timestamp(System.currentTimeMillis()));
					iliveEnterprise.setEnterpriseName(enterpriseName);
					iliveEnterprise.setEnterpriseRegName(enterpriseRegName);
					iliveEnterprise.setEnterpriseRegNo(enterpriseRegNo);
					iliveEnterprise.setUserId(iLiveMessage.getUserId());
					iliveEnterprise.setAutoBuy(1);
					try {
						String userId = iLiveMessage.getUserId().toString();
						ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.parseLong(userId));
						iliveEnterprise.setUserPhone(iLiveManager.getMobile());
						Integer saveEnterpriseForWeb = ILiveEenteriseMng.saveEnterpriseForWeb(iliveEnterprise, iLiveManager);
						if (saveEnterpriseForWeb > -1) {
							
							ILiveManagerState iliveManagerState = iLiveManagerStateMng
									.getIliveManagerState(iLiveManager.getUserId());
							if (iliveManagerState == null) {
								iliveManagerState = new ILiveManagerState();
								iliveManagerState.setCertStatus(4);
								iliveManagerState.setManagerId(iLiveManager.getUserId());
								iLiveManagerStateMng.saveIliveManagerState(iliveManagerState);
							} else {
								iliveManagerState.setCertStatus(4);
								iLiveManagerStateMng.updateIliveManagerState(iliveManagerState);
							}
						}
						//通知播管通过认证
						ILiveEnterprise enterprise1 = iLiveEnterpriseMng.getILiveEnterPrise(iLiveMessage.getEnterpriseId().intValue());
						enterprise1.setCertStatus(4);
						enterprise1.setCertTime(new Timestamp(System.currentTimeMillis()));
						enterprise1.setCheckPerson("admin");
						enterprise1.setAuthUserId(Long.parseLong("1263"));
						enterprise1.setAutoUserName("admin");
						enterprise1.setStamp(0);
						enterprise1.setAutoBuy(1);
						List<ILiveManager> managerList = iLiveManagerMng
								.getILiveManagerByEnterpriseId(enterprise1.getEnterpriseId());
						iLiveEnterpriseMng.updateEnterpriseWithPerson(enterprise1, managerList);
						ILiveCertTopic certTopic = new ILiveCertTopic();
						certTopic.setUserId(enterprise1.getUserId());
							// 审核通过增加一条记录
							certTopic.setCommentTime(new Timestamp(System.currentTimeMillis()));
							certTopic.setEnterpriseId(iLiveMessage.getEnterpriseId().intValue());
							certTopic.setManagerId(iLiveMessage.getUserId());
							certTopic.setManagerName(iLiveMessage.getUserName());
							certTopic.setTopicComment("企业认证通过");
							certTopic.setCertStatus(4);
							if (enterprise1.getStamp() == 1) {
								workLogMng.save(new WorkLog(WorkLog.MODEL_ENTEXAMINE, certTopic.getEnterpriseId()+"", net.sf.json.JSONObject.fromObject(certTopic).toString(), WorkLog.MODEL_ENTEXAMINE_NAME_PASS, Long.parseLong("1263"),"admin", ""));
							}else {
								workLogMng.save(new WorkLog(WorkLog.MODEL_PEREXAMINE, certTopic.getEnterpriseId()+"", net.sf.json.JSONObject.fromObject(certTopic).toString(), WorkLog.MODEL_PEREXAMINE_NAME_PASS, Long.parseLong("1263"),"admin", ""));
							}
						iLiveCertTopicMng.addCertTopic(certTopic);
						Integer num=Integer.parseInt(productnum);
						if("145000180000000000000004".equals(productId)||"145000180000000000000005".equals(productId)){
							for(int i=0;i<num;i++){
								ret=this.yytbilling(enterpriseId,Integer.parseInt(productname),paymentPrice1,contactName,phoneNum,CustID,startTime,request, response);	
							}
						}else if("145000180000000000000002".equals(productId)||"145000180000000000000003".equals(productId)){
							ret=this.yytbilling(enterpriseId,Integer.parseInt(packagename),paymentPrice,contactName,phoneNum,CustID,startTime,request, response);
						}
						if(ret==true) {
							resultJson.put("status", "1");
							resultJson.put("msg", "订购成功");
							}else {
								resultJson.put("status", "0");
								resultJson.put("msg", "订购失败");
							}
					} catch (Exception e) {
						resultJson.put("status", "0");
						resultJson.put("msg", e.getMessage());
						e.printStackTrace();
					
					}
				}
			}else{
				 String nailName = phoneNum.substring(0, 3) + "****" + phoneNum.substring(7, phoneNum.length());
				long registeredManagerId = this.addyytILiveManager(userName, phoneNum, nailName,productId);
				Cache cacheInfo = CacheManager.getCacheInfo(CacheManager.mobile_token_ + "reg_" + phoneNum);
				if (registeredManagerId > 0) {
					Subject subject = SecurityUtils.getSubject();
					UsernamePasswordLoginTypeToken utt = new UsernamePasswordLoginTypeToken(phoneNum,
							"jwztadmin", false, null, "1", null, null);
					ILiveManager iLiveMessage1=iLiveManagerMng.getILiveMangerByMobile(phoneNum);
					try {
						// 执行认证操作
						subject.login(utt);
						CacheManager.cacheExpired(cacheInfo);
						ILiveManager user = iLiveManagerMng.getILiveMangerByMobile(userName);
						// 构建新的企业
						ILiveEnterprise enterprise = this.buildRawEnterprise(phoneNum);
						ILiveEnterprise newEnterprise = iLiveEnterpriseMng.saveILiveEnterpriseForyyt(enterprise);
						iLiveMessage1.setCertStatus(2);
						iLiveMessage1.setEnterpriseId(newEnterprise.getEnterpriseId().longValue());
						iLiveManagerMng.updateLiveManager(iLiveMessage1);
						//申请认证
						ILiveEnterprise iliveEnterprise = new ILiveEnterprise();
						iliveEnterprise.setStamp(1);
						iliveEnterprise.setContactPhone(phoneNum);
						iliveEnterprise.setContactName(userName);
						iliveEnterprise.setApplyTime(new Timestamp(System.currentTimeMillis()));
						iliveEnterprise.setEnterpriseName(enterpriseName);
						iliveEnterprise.setEnterpriseRegName(enterpriseRegName);
						iliveEnterprise.setEnterpriseRegNo(enterpriseRegNo);
						iliveEnterprise.setUserId(iLiveMessage1.getUserId());
						iliveEnterprise.setAutoBuy(1);
						try {
							String userId = iLiveMessage1.getUserId().toString();
							ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.parseLong(userId));
							iliveEnterprise.setUserPhone(iLiveManager.getMobile());
							iLiveManager.setUserName(userName);
							iLiveManagerMng.updateLiveManager(iLiveManager);
							Integer saveEnterpriseForWeb = ILiveEenteriseMng.saveEnterpriseForWeb(iliveEnterprise, iLiveManager);
							if (saveEnterpriseForWeb > -1) {
								
								ILiveManagerState iliveManagerState = iLiveManagerStateMng
										.getIliveManagerState(iLiveManager.getUserId());
								if (iliveManagerState == null) {
									iliveManagerState = new ILiveManagerState();
									iliveManagerState.setCertStatus(ILiveCertStatus.CERT_ING);
									iliveManagerState.setManagerId(iLiveManager.getUserId());
									iLiveManagerStateMng.saveIliveManagerState(iliveManagerState);
								} else {
									iliveManagerState.setCertStatus(ILiveCertStatus.CERT_ING);
									iLiveManagerStateMng.updateIliveManagerState(iliveManagerState);
								}
								
							}
							
							//通知播管通过认证
							ILiveEnterprise enterprise1 = iLiveEnterpriseMng.getILiveEnterPrise(iLiveMessage1.getEnterpriseId().intValue());
							enterprise1.setCertStatus(4);
							enterprise1.setCertTime(new Timestamp(System.currentTimeMillis()));
							enterprise1.setCheckPerson("admin");
							enterprise1.setAuthUserId(Long.parseLong("1263"));
							enterprise1.setAutoUserName("admin");
							enterprise1.setStamp(0);
							enterprise1.setAutoBuy(1);
							List<ILiveManager> managerList = iLiveManagerMng
									.getILiveManagerByEnterpriseId(enterprise1.getEnterpriseId());
							iLiveEnterpriseMng.updateEnterpriseWithPerson(enterprise1, managerList);
							ILiveCertTopic certTopic = new ILiveCertTopic();
							certTopic.setUserId(enterprise1.getUserId());
								// 审核通过增加一条记录
								certTopic.setCommentTime(new Timestamp(System.currentTimeMillis()));
								certTopic.setEnterpriseId(iLiveMessage1.getEnterpriseId().intValue());
								certTopic.setManagerId(iLiveMessage1.getUserId());
								certTopic.setManagerName(iLiveMessage1.getUserName());
								certTopic.setTopicComment("企业认证通过");
								certTopic.setCertStatus(4);
								if (enterprise1.getStamp() == 1) {
									workLogMng.save(new WorkLog(WorkLog.MODEL_ENTEXAMINE, certTopic.getEnterpriseId()+"", net.sf.json.JSONObject.fromObject(certTopic).toString(), WorkLog.MODEL_ENTEXAMINE_NAME_PASS, Long.parseLong("1263"),"admin", ""));
								}else {
									workLogMng.save(new WorkLog(WorkLog.MODEL_PEREXAMINE, certTopic.getEnterpriseId()+"", net.sf.json.JSONObject.fromObject(certTopic).toString(), WorkLog.MODEL_PEREXAMINE_NAME_PASS, Long.parseLong("1263"),"admin", ""));
								}
							iLiveCertTopicMng.addCertTopic(certTopic);
							Integer num=Integer.parseInt(productnum);
							if("145000180000000000000004".equals(productId)||"145000180000000000000005".equals(productId)){
								for(int i=0;i<num;i++){
									ret=this.yytbilling(iLiveMessage1.getEnterpriseId().intValue(),Integer.parseInt(productname),paymentPrice1,contactName,phoneNum,CustID,startTime,request, response);	
								}
							}else if("145000180000000000000002".equals(productId)||"145000180000000000000003".equals(productId)){
								ret=this.yytbilling(iLiveMessage1.getEnterpriseId().intValue(),Integer.parseInt(packagename),paymentPrice,contactName,phoneNum,CustID,startTime,request, response);
							}
							if(ret==true) {
								resultJson.put("status", "1");
								resultJson.put("msg", "订购成功");
								}else {
									resultJson.put("status", "0");
									resultJson.put("msg", "订购失败");
								}
						} catch (Exception e) {
							resultJson.put("status", "0");
							e.printStackTrace();
							return;
						}
					}  catch (Exception e) {
						e.printStackTrace();
						// 其他错误，比如锁定，如果想单独处理请单独catch处理
						resultJson.put("status", "0");
						resultJson.put("msg", "失败");
					}
				}
			}
		} catch (Exception e) {
			resultJson.put("status", ERROR_STATUS);
			resultJson.put("msg", "失败,原因："+e.getMessage());
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	
	
	
	private ILiveEnterprise buildRawEnterprise(String enterpriseName) {
		ILiveEnterprise rawEnterprise = new ILiveEnterprise();
		// 为提交认证
		rawEnterprise.setCertStatus(0);
		String defaultImg = ConfigUtils.get("defaultEnterpriseImg");
		rawEnterprise.setEnterpriseImg(defaultImg);
		rawEnterprise.setStamp(1);
		rawEnterprise.setEnterpriseName(enterpriseName);
		rawEnterprise.setAutoBuy(1);
		return rawEnterprise;
	}

	
	public boolean yytbilling(Integer enterpriseId,Integer packageId,Double paymentPrice,String contactName,String contactNumber,String CustID,String startTime,HttpServletRequest request, HttpServletResponse response) {
		boolean ret=false;
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("enterpriseId", enterpriseId.toString());
			map.put("packageId", packageId.toString());
			map.put("paymentPrice", paymentPrice.toString());
			map.put("contactName", contactName);
			map.put("contactNumber", contactNumber);
			map.put("CustID", CustID);
			if(startTime!=null){
				map.put("startTime", startTime);
			}
			String url =ConfigUtils.get("liveBilling_yyt");
			String postJson = HttpUtils.sendPost(url, map, "UTF-8");
			JSONObject jsonObject = new JSONObject(postJson);
			System.out.println(jsonObject);
			Integer code=jsonObject.getInt("code");
			if(code==1) {
				ret=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return ret;
	}
	/**
	 * 
	 * @param userName
	 * @param phoneNum
	 * @param nailName
	 * @param productId
	 * @return
	 */
	public Long addyytILiveManager(String userName,String phoneNum,String nailName,String productId){
		JSONObject resultJson = new JSONObject();
		Long msg=-1L;
			ILiveManager iLiveMessage;
			if(phoneNum==null) {
				iLiveMessage=null;
			}else {
				iLiveMessage= iLiveManagerMng.getILiveMangerByMobile(phoneNum);
			}
				if(iLiveMessage!=null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户手机号已注册");
				return msg;
			}else{
				ILiveManager manager = new ILiveManager();
				long userId = fieldManagerMng.getNextId("ilive_manager", "user_id", 1);
				manager.setUserId(userId);
				manager.setCertStatus(0);
				manager.setJpushId(phoneNum);
				manager.setLastLoginTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
				manager.setUserName(userName);
				manager.setMobile(phoneNum);
				manager.setNailName(nailName);
				manager.setPlatformId(productId);
				String salt = String.valueOf(new Random().nextInt(2000));
				manager.setSalt(salt);
				ILiveEnterprise enterPrise = iLiveEnterpriseMng.getdefaultEnterprise();
				enterPrise.setAutoBuy(1);
				manager.setEnterPrise(enterPrise);
				String userImage = ConfigUtils.get("defaultTerminalUserImg");
				manager.setUserImg(userImage);
				manager.setCreateTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
				
				boolean b = iLiveManagerMng.saveIliveManager(manager);
				if(b){
					
					msg= userId;
				}
			}
				return msg;
		
	}
	/**
	 * 修改领航用户是否自动续购
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/yytupdateEnterPriseAutoBuy.jspx")
	private void updateEnterPriseAutoBuy(HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		String phoneNum=null;
		Integer orderDays=0;
		try {
			String msg=IOUtils.toString(request.getInputStream(), "utf-8");
			log.info("接收到的yyt发来的开关字符串："+msg);
			JSONObject jsonObject = new JSONObject(msg);
			JSONObject jsonInfo=jsonObject.getJSONObject("ProductInfo");
		    JSONArray Product = jsonInfo.getJSONArray("Product");
		    for(int i=0;i<Product.length();i++) {
		    	JSONObject msgObject = new JSONObject(Product.get(i).toString());
		    	if("userPhone".equals(msgObject.getString("ProductType"))) {
			    	phoneNum=msgObject.getString("ProductValue").trim();
			    	if(phoneNum.length()!=11&&phoneNum!=null) {
						resultJson.put("status", "0");
						resultJson.put("msg", "请输入有效的手机号！");
						ResponseUtils.renderJson(response, resultJson.toString());
					}
			    	
			    }
		    }
		    ILiveManager iLiveMessage= iLiveManagerMng.getILiveMangerByPhoneNumber(phoneNum);
		    if(iLiveMessage==null){
		    	resultJson.put("status", "0");
				resultJson.put("msg", "用户不存在！");
				ResponseUtils.renderJson(response, resultJson.toString());
		    }else{
		    	ILiveEnterprise enterprise= iLiveMessage.getEnterPrise();
		    	enterprise.setAutoBuy(0);
		    	iLiveManagerMng.updateLiveManager(iLiveMessage);
		    	//通知计费系统
		    	//如果关闭了计费功能则不执行
				if("1".equals(ConfigUtils.get("open_liveBilling"))){
					Map<String, String> map = new HashMap<String, String>();
					map.put("enterpriseId", enterprise.getEnterpriseId()+"");
					String url =ConfigUtils.get("liveBilling_yyt_update");
					String postJson = HttpUtils.sendPost(url, map, "UTF-8");
					JSONObject resultObject = new JSONObject(postJson);
					Integer code = (Integer) jsonObject.get("code");
					if(code==1){
						resultJson.put("status", "1");
						resultJson.put("msg", "修改成功！");
						workLogMng.save(new WorkLog(WorkLog.MODEL_LHTD, enterprise.getEnterpriseId()+"", net.sf.json.JSONObject.fromObject(enterprise).toString(), WorkLog.MODEL_LHTD_NAME, Long.parseLong("1263"),"admin", ""));
					}else{
						String message=resultObject.getString("message");
						resultJson.put("status", "1");
						resultJson.put("msg", message);
					}
				}else{
					resultJson.put("status", "1");
					resultJson.put("msg", "修改成功！");
					workLogMng.save(new WorkLog(WorkLog.MODEL_LHTD, enterprise.getEnterpriseId()+"", net.sf.json.JSONObject.fromObject(enterprise).toString(), WorkLog.MODEL_LHTD_NAME, Long.parseLong("1263"),"admin", ""));
				
				}
				ResponseUtils.renderJson(response, resultJson.toString());
		    }
		} catch (Exception e) {
			resultJson.put("status", "0");
			resultJson.put("msg", e.getMessage());
			ResponseUtils.renderJson(response, resultJson.toString());
		}
	}
}
