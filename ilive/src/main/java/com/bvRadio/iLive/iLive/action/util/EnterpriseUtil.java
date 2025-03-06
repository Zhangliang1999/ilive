package com.bvRadio.iLive.iLive.action.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.bvRadio.iLive.iLive.util.HttpUtils;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

public class EnterpriseUtil {
	/**
	 * 存储
	 */
	public static final Integer ENTERPRISE_USE_TYPE_Store=2;	
	/**
	 * 短信
	 */
	public static final Integer ENTERPRISE_USE_TYPE_Sms=3;
	/**
	 * 时长
	 */
	public static final Integer ENTERPRISE_USE_TYPE_Duration=4;
	/**
	 * 子账号数量
	 */
	public static final Integer ENTERPRISE_USE_TYPE_Subcount=5;
	/**
	 * 产品使用   通知
	 * @param enterpriseId 企业ID
	 * @param useType 使用类型    2：存储/kb  3：短信/条 4：时长/s
	 * @param useValue 使用数量 
	 * 					存储时 单位： KB
	 *   				短信     单位：条
	 *   				时长     单位：秒
	 *   				并发数 单位：个
	 * @return false 数据发送成功      true 数据发送失败
	 */
	public static boolean openEnterprise(Integer enterpriseId,Integer useType,String useValue,Integer certStatus){
		boolean ret = true;
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("enterpriseId", enterpriseId+"");
			map.put("useType", useType+"");
			map.put("useValue", useValue);
			certStatus = certStatus==null?0:certStatus;
			map.put("certStatus", certStatus+"");
			String url =ConfigUtils.get("enterprise_function_open");
			String postJson = HttpUtils.sendPost(url, map, "UTF-8");
			JSONObject jsonObject = new JSONObject(postJson);
			Hashtable<Integer, List<Integer>> enterpriseMap = EnterpriseCache.getEnterpriseMap();
			List<Integer> list = new ArrayList<Integer>();
			Integer code = (Integer) jsonObject.get("code");
			if(code==1){
				//解析功能码
				JSONObject data = (JSONObject) jsonObject.get("data");
				String functionCode = (String) data.get("functionCode");
				if(!functionCode.equals("")){
					String[] split = functionCode.split(",");
					for (int i = 0; i < split.length; i++) {
						try {
							list.add(Integer.valueOf(split[i]));
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
					}
				}
				Hashtable<Integer, Hashtable<String, String>> enterpriseStrMap = EnterpriseCache.getEnterpriseStrMap();
				Hashtable<String, String> strMap = enterpriseStrMap.get(enterpriseId);
				if(strMap==null){
					strMap = new Hashtable<String, String>();
				}
				//存储  已使用   usedStorageProduct  kb   总共 storageProduct kb
				String usedStorageProduct =data.get("usedStorageProduct").toString();
				strMap.put(EnterpriseCache.ENTERPRISE_USE_Store, usedStorageProduct);
				String storageProduct =data.get("storageProduct").toString();
				strMap.put(EnterpriseCache.ENTERPRISE_Store, storageProduct);
				//短信  已使用  usedShortMessageProduct  总共 shortMessageProduct
				String usedShortMessageProduct =data.get("usedShortMessageProduct").toString();
				strMap.put(EnterpriseCache.ENTERPRISE_USE_Sms, usedShortMessageProduct);
				String shortMessageProduct =data.get("shortMessageProduct").toString();
				strMap.put(EnterpriseCache.ENTERPRISE_Sms, shortMessageProduct);
				//时长 已使用 usedDurationProduct 秒             总共 durationProduct
				String usedDurationProduct =data.get("usedDurationProduct").toString();
				strMap.put(EnterpriseCache.ENTERPRISE_USE_Duration, usedDurationProduct);
				String durationProduct =data.get("durationProduct").toString();
				strMap.put(EnterpriseCache.ENTERPRISE_Duration, durationProduct);
				//并发数 已使用 usedConcurrenceProduct   总共 concurrenceProduct
				String usedConcurrenceProduct =data.get("usedConcurrenceProduct").toString();
				strMap.put(EnterpriseCache.ENTERPRISE_USE_Concurrent, usedConcurrenceProduct);
				String concurrenceProduct =data.get("concurrenceProduct").toString();
				strMap.put(EnterpriseCache.ENTERPRISE_Concurrent, concurrenceProduct);
				enterpriseStrMap.put(enterpriseId, strMap);
				ret = false;
			}else if(code==500){
				ret = true;
			}else{
				ret = true;
			}
			enterpriseMap.put(enterpriseId, list);
		} catch (Exception e) {
			e.printStackTrace();
			ret = false;
		}
		return ret;
	}
	/**
	 * 更新企业功能缓存数据
	 * @param enterpriseId
	 * @return
	 * @throws Exception
	 */
	public static Hashtable<Integer, List<Integer>> selectEnterpriseCache(Integer enterpriseId,Integer certStatus) throws Exception{
		Map<String, String> map = new HashMap<String, String>();
		map.put("enterpriseId", enterpriseId+"");
		certStatus = certStatus==null?0:certStatus;
		map.put("certStatus", certStatus+"");
		String url =ConfigUtils.get("enterprise_function");
		String postJson = HttpUtils.sendPost(url, map, "UTF-8");
		System.out.println("企业ID："+enterpriseId);
		System.out.println("更新企业套餐："+postJson);
		JSONObject jsonObject = new JSONObject(postJson);
		Hashtable<Integer, List<Integer>> enterpriseMap = EnterpriseCache.getEnterpriseMap();
		List<Integer> list = new ArrayList<Integer>();
		Integer code = (Integer) jsonObject.get("code");
		if(code==1){
			//解析功能码
			JSONObject data = (JSONObject) jsonObject.get("data");
			String functionCode = (String) data.get("functionCode");
			System.out.println(functionCode);
			System.out.println(functionCode+"  判空:"+functionCode.equals(""));
			if(!functionCode.equals("")){
				String[] split = functionCode.split(",");
				for (int i = 0; i < split.length; i++) {
					try {
						Integer valueOf = Integer.valueOf(split[i]);
						list.add(valueOf);
					} catch (Exception e) {
						System.out.println("类型转换异常："+split[i]);
					}
				}
			}
			Hashtable<Integer, Hashtable<String, String>> enterpriseStrMap = EnterpriseCache.getEnterpriseStrMap();
			Hashtable<String, String> strMap = enterpriseStrMap.get(enterpriseId);
			if(strMap==null){
				strMap = new Hashtable<String, String>();
			}
			//存储  已使用   usedStorageProduct  kb   总共 storageProduct kb
			String usedStorageProduct = data.get("usedStorageProduct").toString();
			strMap.put(EnterpriseCache.ENTERPRISE_USE_Store, usedStorageProduct);
			String storageProduct = data.get("storageProduct").toString();
			strMap.put(EnterpriseCache.ENTERPRISE_Store, storageProduct);
			//短信  已使用  usedShortMessageProduct  总共 shortMessageProduct
			String usedShortMessageProduct =  data.get("usedShortMessageProduct").toString();
			strMap.put(EnterpriseCache.ENTERPRISE_USE_Sms, usedShortMessageProduct);
			String shortMessageProduct =  data.get("shortMessageProduct").toString();
			strMap.put(EnterpriseCache.ENTERPRISE_Sms, shortMessageProduct);
			//时长 已使用 usedDurationProduct 秒             总共 durationProduct
			String usedDurationProduct =  data.get("usedDurationProduct").toString();
			strMap.put(EnterpriseCache.ENTERPRISE_USE_Duration, usedDurationProduct);
			String durationProduct =  data.get("durationProduct").toString();
			strMap.put(EnterpriseCache.ENTERPRISE_Duration, durationProduct);
			//并发数 已使用 usedConcurrenceProduct   总共 concurrenceProduct
			String usedConcurrenceProduct =  data.get("usedConcurrenceProduct").toString();
			strMap.put(EnterpriseCache.ENTERPRISE_USE_Concurrent, usedConcurrenceProduct);
			String concurrenceProduct =  data.get("concurrenceProduct").toString();
			strMap.put(EnterpriseCache.ENTERPRISE_Concurrent, concurrenceProduct);
			enterpriseStrMap.put(enterpriseId, strMap);
		}
		enterpriseMap.put(enterpriseId, list);
		return enterpriseMap;
	}
	/**
	 * 登录获取用户套餐信息
	 * @param enterpriseId
	 * @return
	 * @throws Exception
	 */
	public static String getEnterpriseMsg(Integer enterpriseId,Integer certStatus) throws Exception{
		
		//如果关闭了计费功能则不执行
				if("0".equals(ConfigUtils.get("open_liveBilling"))){
					return "-1";
				}
		String functionCode=null;
		Map<String, String> map = new HashMap<String, String>();
		map.put("enterpriseId", enterpriseId+"");
		certStatus = certStatus==null?0:certStatus;
		map.put("certStatus", certStatus+"");
		String url =ConfigUtils.get("enterprise_function");
		String postJson = HttpUtils.sendPost(url, map, "UTF-8");
		System.out.println("企业ID："+enterpriseId);
		System.out.println("更新企业套餐："+postJson);
		JSONObject jsonObject = new JSONObject(postJson);
		Integer code = (Integer) jsonObject.get("code");
		if(code==1){
			//解析功能码
			JSONObject data = (JSONObject) jsonObject.get("data");
			functionCode = (String) data.get("functionCode");
			System.out.println(functionCode+"  判空:"+functionCode.equals(""));
		}
		return functionCode;
	}
	/**
	 * 登录获取用户套餐id
	 * @param enterpriseId
	 * @return
	 * @throws Exception
	 */
	public static boolean getEnterprisePackage(Integer enterpriseId,Integer certStatus) throws Exception{
		
		//如果关闭了计费功能则不执行
				if("0".equals(ConfigUtils.get("open_liveBilling"))){
					return true;
				}
		boolean ret=true;
		Integer packageId=null;
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("enterpriseId", enterpriseId+"");
			certStatus = certStatus==null?0:certStatus;
			map.put("certStatus", certStatus+"");
			String url =ConfigUtils.get("enterprise_function");
			String postJson = HttpUtils.sendPost(url, map, "UTF-8");
			System.out.println("企业ID："+enterpriseId);
			System.out.println("更新企业套餐："+postJson);
			JSONObject jsonObject = new JSONObject(postJson);
			Integer code = (Integer) jsonObject.get("code");
			if(code==1){
				//解析功能码
				JSONObject data = (JSONObject) jsonObject.get("data");
				packageId = Integer.parseInt(data.get("packageId").toString());
				if(packageId==708||packageId==709||packageId==710) {
					ret=false;
				}else {
					ret=true;
				}
			}
		} catch (Exception e) {
			e.getStackTrace();
			ret=true;
		}
		return ret;
	}
	/**
	 * 查看是否开通功能
	 * @param enterpriseId 企业ID
	 * @param code 功能编码
	 * @return false 已购买 true  未购买
	 */
	public static boolean selectIfEn(Integer enterpriseId,Integer code,Integer certStatus){

		//如果关闭了计费功能则不执行
		if("0".equals(ConfigUtils.get("open_liveBilling"))){
			return false;
		}
		Integer[] array = {10001,10004,10008,10010,10011,10012,10013,10020,10029,10036,10038,10047,10048,10049,10050,10051,10052,10054,10056,10066,10067,10073};
		int index = Arrays.binarySearch(array,code);
		if(index<0){
			return false;
		}else{
		boolean ret = true;
		Hashtable<Integer, List<Integer>> enterpriseMap = EnterpriseCache.getEnterpriseMap();
		List<Integer> list = enterpriseMap.get(enterpriseId);
		if(list==null){
			list = new ArrayList<Integer>();
		}
		if(list.size()==0){
			try {
				enterpriseMap = selectEnterpriseCache(enterpriseId,certStatus);
				list = enterpriseMap.get(enterpriseId);
			} catch (Exception e) {
				list = new ArrayList<Integer>();
			}
		}
		for (Integer codeId : list) {
			if(code.equals(codeId)){
				ret = false;
			}
		}
		return ret;
		}
	}
	/**
	 * 处理购买内容是否超标
	 * @param key 功能字符标示
	 * @param content 已使用数据
	 * 			存储时 单位： KB
	 *   		短信     单位：条
	 *   		时长     单位：秒
	 *   		并发数 单位：个
	 * @param enterpriseId 企业ID
	 * @return false 未超标  true  超标
	 */
	public static boolean selectIfContent(String key,Long content,Integer enterpriseId,Integer certStatus){
		
		
		//如果关闭了计费功能则不执行
		if("0".equals(ConfigUtils.get("open_liveBilling"))){
			return false;
		}
		
		boolean ret = true;
		Hashtable<Integer, Hashtable<String, String>> enterpriseStrMap = EnterpriseCache.getEnterpriseStrMap();
		Hashtable<String, String> map = enterpriseStrMap.get(enterpriseId);
			try {
				selectEnterpriseCache(enterpriseId,certStatus);
				enterpriseStrMap = EnterpriseCache.getEnterpriseStrMap();
				map = enterpriseStrMap.get(enterpriseId);
			} catch (Exception e) {
				map =new Hashtable<String,String>();
				e.printStackTrace();
			}
		
		String value = map.get(key);
		System.out.println("标示："+key+"   现有数："+content+"  总数："+value);
		value = value==null?"0":value;
		if(!value.equals("0")){
			if(content<=Long.valueOf(value)){
				ret = false;
			}else{
				ret = true;
			}
		}
		return ret;
	}
	/**
	 * 查询是否还有足够空间，或者套餐是否到期
	 */
	public static boolean getIfCan(Integer enterpriseId,String useType,Integer certStatus) {
		
		//如果关闭了计费功能则不执行
				if("0".equals(ConfigUtils.get("open_liveBilling"))){
					return false;
				}
				
		boolean ret=true;
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("enterpriseId", enterpriseId+"");
			map.put("useType", useType);
			certStatus = certStatus==null?0:certStatus;
			map.put("certStatus", certStatus+"");
			String url =ConfigUtils.get("enterprise_function_msg");
			String postJson = HttpUtils.sendPost(url, map, "UTF-8");
			JSONObject jsonObject = new JSONObject(postJson);
			Integer code = (Integer) jsonObject.get("code");
			if(code==1) {
				ret=false;
			}else {
				ret=true;
			}
		} catch (Exception e) {
			ret=false;
		}
		return ret;
	}
	/**
	 * 查询存储是否还有足够空间
	 */
	public static boolean getStoreIfCan(Integer enterpriseId,String useType,Integer certStatus,Long useValue) {
		
		//如果关闭了计费功能则不执行
				if("0".equals(ConfigUtils.get("open_liveBilling"))){
					return false;
				}
				
		boolean ret=true;
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("enterpriseId", enterpriseId+"");
			map.put("useType", useType);
			certStatus = certStatus==null?0:certStatus;
			map.put("certStatus", certStatus+"");
			map.put("useValue", useValue+"");
			String url =ConfigUtils.get("enterprise_function_storemsg");
			String postJson = HttpUtils.sendPost(url, map, "UTF-8");
			JSONObject jsonObject = new JSONObject(postJson);
			Integer code = (Integer) jsonObject.get("code");
			if(code==1) {
				ret=false;
			}else {
				ret=true;
			}
		} catch (Exception e) {
			ret=false;
		}
		return ret;
	}
	/**
	 * 查询是否开通收益账户/红包账户
	 */
	public static Integer getIfPassRevenue(Integer enterpriseId,Integer certStatus) {
		
		//如果关闭了计费功能则不执行
				if("0".equals(ConfigUtils.get("open_liveBilling"))){
					return 1;
				}
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("enterpriseId", enterpriseId+"");
			String url =ConfigUtils.get("enterprise_function_revenue");
			String postJson = HttpUtils.sendPost(url, map, "UTF-8");
			JSONObject jsonObject = new JSONObject(postJson);
			Integer code = (Integer) jsonObject.get("code");
			return code;
		} catch (Exception e) {
			return 0;
		}
		
	}
	/**
	 * 
	 * @param enterpriseId
	 * @param useType
	 * @param certStatus 
	 * @param Total 总用量
	 * @return
	 */
public static boolean totalUpdate(Integer enterpriseId,String useType,Integer certStatus,String Total) {
		
		//如果关闭了计费功能则不执行
				if("0".equals(ConfigUtils.get("open_liveBilling"))){
					return false;
				}
				
		boolean ret=true;
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("enterpriseId", enterpriseId+"");
			map.put("useType", useType);
			certStatus = certStatus==null?0:certStatus;
			map.put("certStatus", certStatus+"");
			map.put("total", Total);
			String url =ConfigUtils.get("enterprise_function_total");
			String postJson = HttpUtils.sendPost(url, map, "UTF-8");
			JSONObject jsonObject = new JSONObject(postJson);
			Integer code = (Integer) jsonObject.get("code");
			if(code==1) {
				ret=false;
			}else {
				ret=true;
			}
		} catch (Exception e) {
			ret=true;
		}
		return ret;
	}
/**
 * 获取当前用户的账号是否到期
 * @param mobile
 * @return
 */
	public static Integer checkValiteTime(Integer enterpriseId,Integer certStatus) throws Exception{
		//如果关闭了计费功能则不执行
		if("0".equals(ConfigUtils.get("open_liveBilling"))){
			return 365;
		}
	Integer remainingDays=null;
	try {
		Map<String, String> map = new HashMap<String, String>();
		map.put("enterpriseId", enterpriseId+"");
		certStatus = certStatus==null?0:certStatus;
		map.put("certStatus", certStatus+"");
		String url =ConfigUtils.get("enterprise_function");
		String postJson = HttpUtils.sendPost(url, map, "UTF-8");
		System.out.println("企业ID："+enterpriseId);
		System.out.println("更新企业套餐："+postJson);
		JSONObject jsonObject = new JSONObject(postJson);
		Integer code = (Integer) jsonObject.get("code");
		if(code==1){
			//解析功能码
			JSONObject data = (JSONObject) jsonObject.get("data");
			remainingDays = (Integer) data.get("remainingDays");
			System.out.println("remainingDays===="+remainingDays);
		}
	} catch (Exception e) {
		remainingDays=365;
	}
	return remainingDays;
		}
	
	/**
	 * 获取当前用户的账号子账号总数
	 * @param mobile
	 * @return
	 */
		public static Integer checkSubcount(Integer enterpriseId,Integer certStatus) throws Exception{
			//如果关闭了计费功能则不执行
			if("0".equals(ConfigUtils.get("open_liveBilling"))){
				return 5;
			}
		Integer subCountProduct=null;
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("enterpriseId", enterpriseId+"");
			certStatus = certStatus==null?0:certStatus;
			map.put("certStatus", certStatus+"");
			String url =ConfigUtils.get("enterprise_function");
			String postJson = HttpUtils.sendPost(url, map, "UTF-8");
			System.out.println("企业ID："+enterpriseId);
			System.out.println("更新企业套餐："+postJson);
			JSONObject jsonObject = new JSONObject(postJson);
			Integer code = (Integer) jsonObject.get("code");
			if(code==1){
				//解析功能码
				JSONObject data = (JSONObject) jsonObject.get("data");
				subCountProduct = (Integer) data.get("subCountProduct");
				System.out.println("subCountProduct===="+subCountProduct);
			}
		} catch (Exception e) {
			subCountProduct=5;
		}
		return subCountProduct;
			}
}
