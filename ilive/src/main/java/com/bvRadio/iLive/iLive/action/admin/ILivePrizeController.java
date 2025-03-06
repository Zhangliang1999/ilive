package com.bvRadio.iLive.iLive.action.admin;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.action.util.SubAccountCache;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveLottery;
import com.bvRadio.iLive.iLive.entity.ILiveLotteryList;
import com.bvRadio.iLive.iLive.entity.ILiveLotteryParktake;
import com.bvRadio.iLive.iLive.entity.ILiveLotteryPrize;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveLotteryListMng;
import com.bvRadio.iLive.iLive.manager.ILiveLotteryParktakeMng;
import com.bvRadio.iLive.iLive.manager.ILiveLotteryPrizeMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILivePrizeMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubLevelMng;
import com.bvRadio.iLive.iLive.util.Md5Util;
import com.bvRadio.iLive.iLive.util.RandomUtils;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="prize")
public class ILivePrizeController {

	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;// 直播间
	
	@Autowired
	private ILivePrizeMng iLivePrizeMng;	//抽奖内容
	
	@Autowired
	private ILiveLotteryParktakeMng iLiveLotteryParktakeMng;	//抽奖参与人
	
	@Autowired
	private ILiveLotteryPrizeMng iLiveLotteryPrizeMng;		//抽奖奖品设置
	
	@Autowired
	private ILiveLotteryListMng iLiveLotteryListMng;		//抽奖黑名单/白名单设置
	
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;
	
	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	
	@Autowired
	private ILiveSubLevelMng iLiveSubLevelMng;
	
	/**
	 * 抽奖列表
	 * @return
	 */
	@RequestMapping(value="prizelist.do")
	public String prizeList(Model model,String prizeName,Integer pageNo,Integer pageSize,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		Integer enterpriseId = user.getEnterpriseId();
		
		ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.valueOf(user.getUserId()));
		boolean per=iLiveSubLevelMng.selectIfCan(request, SubAccountCache.ENTERPRISE_FUNCTION_rewardActivity);
		Pagination page = iLivePrizeMng.getPageByEnterpriseId(prizeName,enterpriseId,pageNo==null?1:pageNo,10);
		if (iLiveManager.getLevel()!=null&&iLiveManager.getLevel()!=0&&!per) {
			page = iLivePrizeMng.getpageByUserId(Long.valueOf(user.getUserId()),prizeName,pageNo==null?1:pageNo,10);
		}else {
			page = iLivePrizeMng.getPageByEnterpriseId(prizeName,enterpriseId,pageNo==null?1:pageNo,10);
		}
		
		String  authString = getAuthString(request);
		String list_lottery = ConfigUtils.get("list_lottery");
		model.addAttribute("list_lottery",list_lottery);
		model.addAttribute("authString",authString);
		model.addAttribute("prizeName",prizeName);
		model.addAttribute("enterpriseId",enterpriseId);
		model.addAttribute("page",page);
		model.addAttribute("totalPage",page == null?0:page.getTotalPage());
		model.addAttribute("topActive", "2");
		model.addAttribute("leftActive", "4_2");
		return "prize/prizelist";
	}
	
	/**
	 * 创建抽奖页面
	 * @return
	 */
	@RequestMapping(value="createprize.do")
	public String createPrize(Model model, Long id,Integer detail,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		Integer enterpriseId = user.getEnterpriseId();
		ILiveLottery prize;
		List<ILiveLotteryPrize> list;
		if(id!=null) {
			prize = iLivePrizeMng.getPrize(id);
			list = iLiveLotteryPrizeMng.getListByLotteryId(id);
			Iterator<ILiveLotteryPrize> iterator = list.iterator();
			while (iterator.hasNext()) {
				ILiveLotteryPrize iLiveLotteryPrize = (ILiveLotteryPrize) iterator.next();
				List<ILiveLotteryList> whiteListByPrizeId = iLiveLotteryListMng.getWhiteListByPrizeId(iLiveLotteryPrize.getId());
				if (!whiteListByPrizeId.isEmpty()) {
					StringBuilder str = new StringBuilder();
					for(int i=0;i<whiteListByPrizeId.size();i++) {
						if (i==0) {
							str.append(whiteListByPrizeId.get(i).getPhone());
						}else {
							str.append(","+whiteListByPrizeId.get(i).getPhone());
						}
					}
					iLiveLotteryPrize.setWhiteList(str.toString());
				}
			}
		}else {
			prize = new ILiveLottery();
			list = new ArrayList<>();
		}
		Integer serverGroupId = this.selectServerGroup();
		model.addAttribute("prize", prize);
		model.addAttribute("list", list);
		model.addAttribute("enterpriseId", enterpriseId);
		model.addAttribute("topActive", "2");
		model.addAttribute("leftActive", "4_2");
		model.addAttribute("serverGroupId", serverGroupId);
		if(detail == null) {
			model.addAttribute("detail", 0);
		}else {
			model.addAttribute("detail", 1);
		}
		return "prize/createprize";
	}
	
	/**
	 * 抽奖详情
	 * @return
	 */
	@RequestMapping(value="prizedetail.do")
	public String prizedetail(Model model,Integer enterpriseId,Long lotteryId,Integer isPrize,String search,Integer pageNo,Integer pageSize) {
		ILiveLottery prize = iLivePrizeMng.getPrize(lotteryId);
		Pagination page = iLiveLotteryParktakeMng.getPage(isPrize,search,lotteryId,pageNo==null?1:pageNo,10);
		model.addAttribute("page", page);
		model.addAttribute("totalPage", page.getTotalPage());
		model.addAttribute("prize",prize);
		model.addAttribute("isPrize",isPrize);
		model.addAttribute("search",search);
		model.addAttribute("lotteryId",lotteryId);
		model.addAttribute("enterpriseId", enterpriseId);
		model.addAttribute("topActive", "2");
		model.addAttribute("leftActive", "4_2");
		return "prize/prizedetail";
	}
	
	/**
	 * 创建/修改一个抽奖活动
	 * @param lottery
	 */
	@ResponseBody
	@RequestMapping(value="savePrize.do",method = RequestMethod.POST)
	public void savePrize(ILiveLottery lottery,String lotteryPrize,HttpServletResponse response,HttpServletRequest request) {
		JSONObject res = new JSONObject();
		try {
			UserBean user = ILiveUtils.getUser(request);
			Integer enterpriseId = user.getEnterpriseId();
			lottery.setEnterpriseId(enterpriseId);
			lottery.setUserId(Long.valueOf(user.getUserId()));
			ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
			/*boolean b = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_Lottery,certStatus);
			if(b){
				res.put("status", 2);
				res.put("message", EnterpriseCache.ENTERPRISE_FUNCTION_Lottery_Content);
				ResponseUtils.renderJson(response, res.toString());
				return;
			}*/
			if (!blackHashMap.isEmpty()) {
				iLivePrizeMng.createOrUpdateLotteryPrize(lottery,lotteryPrize,blackHashMap);
				blackHashMap.clear();
			}else {
				iLivePrizeMng.createOrUpdateLotteryPrize(lottery,lotteryPrize,blackHashMap);
			}
			
			res.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			res.put("status", 2);
		}
		ResponseUtils.renderJson(response, res.toString());
	}
	
	/**
	 * 使一个抽奖活动结束
	 * @param lottery
	 */
	@ResponseBody
	@RequestMapping(value="letend.do",method = RequestMethod.POST)
	public void letend(Long id,HttpServletResponse response) {
		JSONObject res = new JSONObject();
		try {
			res.put("status", 1);
			ILiveLottery prize = iLivePrizeMng.getPrize(id);
			Timestamp now = new Timestamp(new Date().getTime());
			prize.setIsEnd(1);
			prize.setEndTime(now);
			prize.setIsSwitch(0);
			iLivePrizeMng.update(prize);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			res.put("date", format.format(new Date()));
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
			.getUserListMap();
			if(userListMap!=null) {
				ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap.get(prize.getEnterpriseId());
				if(concurrentHashMap!=null) {
					Iterator<String> userIterator = concurrentHashMap.keySet().iterator();
					ILiveMessage iLiveMessage = new ILiveMessage();
					iLiveMessage.setRoomType(5);
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
			}
		}catch (Exception e) {
			e.printStackTrace();
			res.put("status", 2);
		}
		ResponseUtils.renderJson(response, res.toString());
	}
	
	/**
	 * 关闭/开启一个抽奖活动
	 * @param lottery
	 */
	@ResponseBody
	@RequestMapping(value="letclose.do",method = RequestMethod.POST)
	public void letclose(Long id,Integer isSwitch,HttpServletResponse response) {
		JSONObject res = new JSONObject();
		try {
			iLivePrizeMng.closeOrStart(id,isSwitch);
			ILiveLottery prize = iLivePrizeMng.getPrize(id);
			if(prize!=null){
				Integer enterpriseId = prize.getEnterpriseId();
				ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
						.getUserListMap();
				if(userListMap!=null) {
					ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap.get(enterpriseId);
					Iterator<String> userIterator = concurrentHashMap.keySet().iterator();
					ILiveMessage iLiveMessage = new ILiveMessage();
					iLiveMessage.setRoomType(5);
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
			}
			res.put("status", 1);
		}catch (Exception e) {
			e.printStackTrace();
			res.put("status", 2);
		}
		ResponseUtils.renderJson(response, res.toString());
	}
	private Integer selectServerGroup() {
		return 100;
	}
	
	/**
	 * 查询是该直播间是否有已开启的抽奖活动
	 * @param roomId
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="isStartLottery.do")
	public void isStartLottery(Integer enterpriseId,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveLottery startPrize = iLivePrizeMng.isEnterpriseStartPrize(enterpriseId);
			if(startPrize==null) {
				resultJson.put("status", 0);
			}else {
				resultJson.put("status", 1);
			}
		} catch (Exception e) {
			resultJson.put("status", 3);
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	
	private static HashMap<String, String> blackHashMap = new HashMap<>();
	
	//读取Excel文件
	@ResponseBody
	@RequestMapping(value="saveBlackExcel.do")
	public void saveBlack(@RequestParam("file")MultipartFile file,HttpServletResponse response) {
		JSONObject res = new JSONObject();
		try {
		    try {
		    	@SuppressWarnings("resource")
				HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
		    	System.out.println("文件不为空");
				//获取当前工作表
				Sheet sheet = workbook.cloneSheet(0);
				readExcel(sheet);
		    }catch (Exception e) {
		    	@SuppressWarnings("resource")
				XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		    	if(workbook!=null) {
					System.out.println("文件不为空");
					//获取当前工作表
					Sheet sheet = workbook.cloneSheet(0);
					readExcel(sheet);
				}
		    }
			res.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			res.put("status", 2);
		}
		ResponseUtils.renderJson(response, res.toString());
	}
	private void readExcel(Sheet sheet ) {
		int firstRow = sheet.getFirstRowNum();
		int lastRow = sheet.getLastRowNum();
		for(int i=firstRow+1;i<=lastRow;i++) {
			Row row = sheet.getRow(i);
			Cell cell1 = row.getCell(0);
			Cell cell2 = row.getCell(1);
			blackHashMap.put(cell1.getStringCellValue(), String.valueOf(cell2.getNumericCellValue()).replace(".0", ""));
		}
	}
	
	@ResponseBody
	@RequestMapping(value="getprize.do")
	public void getprize(Long id,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			result.put("status", "1");
			ILiveLottery prize = iLivePrizeMng.getPrize(id);
			JSONObject res = JSONObject.fromObject(prize);
			result.put("data", res);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "2");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	@RequestMapping("exportData")
	public void exportData(HttpServletResponse response,Integer enterpriseId,Long lotteryId,Integer isPrize,String search) {
		try {
			
			//Pagination page = iLiveLotteryParktakeMng.getPage(isPrize,search,lotteryId,pageNo==null?1:pageNo,10);
			List<ILiveLotteryParktake> list = iLiveLotteryParktakeMng.getList(isPrize, search,lotteryId);
			
			@SuppressWarnings("resource")
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("企业列表");
			HSSFRow rowHead = sheet.createRow(0);
			rowHead.createCell(0).setCellValue("序号");
			rowHead.createCell(1).setCellValue("参与人");
			rowHead.createCell(2).setCellValue("联系方式");
			rowHead.createCell(3).setCellValue("是否中奖");
			rowHead.createCell(4).setCellValue("奖品名称");
			rowHead.createCell(5).setCellValue("参与时间");
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for(int i = 0;i<list.size();i++) {
				ILiveLotteryParktake lo = list.get(i);
				
				HSSFRow rowHead2 = sheet.createRow(i+1);
				rowHead2.createCell(0).setCellValue(lo.getId());
				rowHead2.createCell(1).setCellValue(lo.getUserName());
				rowHead2.createCell(2).setCellValue(lo.getPhone());
				rowHead2.createCell(3).setCellValue((lo.getIsPrize()!=null&&lo.getIsPrize()!=0)?"中奖":"未中奖");
				rowHead2.createCell(4).setCellValue(lo.getPrizepro());
				rowHead2.createCell(5).setCellValue(format.format(new Date(lo.getCreateTime().getTime())));
			}
			
			try {
				this.setResponse(response, "参与人名单.xls");
				OutputStream os = response.getOutputStream();
				workbook.write(os);
		        os.flush();
		        os.close();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void setResponse(HttpServletResponse response,String fileName) throws UnsupportedEncodingException {
		fileName = new String(fileName.getBytes(),"ISO8859-1");
		response.setContentType("application/octet-stream;charset=ISO8859-1");
		response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
		response.addHeader("Pargam", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
	}
	private String getAuthString(HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		String queryString = request.getQueryString();
		if (user != null) {
			Integer enterpriseId = user.getEnterpriseId();
			String userId =user.getUserId();
			queryString = "enterpriseId=" + enterpriseId + "&userId=" +userId;
		}

		String timeStr = System.currentTimeMillis() / 1000 + "";
		String encodeMd5Str = Md5Util.encode(timeStr + "_chinanet_2018_jwzt_" + queryString);
		String authString = RandomUtils.getRadomNum(4) + timeStr + RandomUtils.getRadomNum(4) + "@" + queryString + "@"
				+ encodeMd5Str;
		try {
			authString = URLEncoder.encode(authString, "utf-8");
		} catch (Exception e) {
		}
		return authString;
	}
}
