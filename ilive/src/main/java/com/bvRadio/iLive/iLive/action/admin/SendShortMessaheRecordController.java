package com.bvRadio.iLive.iLive.action.admin;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
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
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveSendMsg;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.SendShortMessaheRecord;
import com.bvRadio.iLive.iLive.entity.SendShortUser;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.entity.WorkLog;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveSendMsgMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.SendShortMessaheRecordMng;
import com.bvRadio.iLive.iLive.manager.SendShortUserMng;
import com.bvRadio.iLive.iLive.manager.WorkLogMng;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RequestMapping(value="sendshortmessage")
@Controller
public class SendShortMessaheRecordController {

	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;// 直播间
	
	@Autowired
	private SendShortUserMng sendShortUserMng;	//群发短信记录
	
	@Autowired
	private ILiveManagerMng iLiveManagerMng;	//用户
	
	@Autowired
	private SendShortMessaheRecordMng sendShortMessaheRecordMng;	//群发短信详细到个人记录
	
	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;
	
	@Autowired
	private ILiveSendMsgMng iLiveSendMsgMng;
	@Autowired
	private WorkLogMng workLogMng;
	
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;
	private final static String RTMP_PROTOACAL = "rtmp://";

	private static final String HTTP_PROTOCAL = "http://";
	/**
	 * 发送短信页面
	 * @param roomId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="shortmessage.do")
	public String sendMessage(Integer roomId,Model model,Integer pageNo) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		Timestamp liveStartTime = liveEvent.getLiveStartTime();
		/**
		 * 获取直播间推流地址
		 */
		ILiveServerAccessMethod accessMethodBySeverGroupId = null;
		try {
			accessMethodBySeverGroupId = accessMethodMng
					.getAccessMethodBySeverGroupId(iLiveLiveRoom.getServerGroupId());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		String push =  RTMP_PROTOACAL + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
//				+ accessMethodBySeverGroupId.getUmsport()+ "/live/live" + roomId + "_tzwj_5000k";
		String liveAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone" + "/live.html?roomId="
				+ roomId;
		userMessage.clear();
		SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm");
		model.addAttribute("startTime", format.format(new Date(liveStartTime.getTime())));
		model.addAttribute("url", liveAddr);
		model.addAttribute("leftActive", "2_9");
		ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(iLiveLiveRoom.getEnterpriseId());
		Integer certStatus = iLiveEnterprise.getCertStatus();
		boolean b = EnterpriseUtil.selectIfEn(iLiveLiveRoom.getEnterpriseId(), EnterpriseCache.ENTERPRISE_FUNCTION_InvitedToWatch,certStatus);
//		if(b){
//			model.addAttribute("enterpriseType", 0);
//			model.addAttribute("enterpriseContent", EnterpriseCache.ENTERPRISE_FUNCTION_InvitedToWatch_Content);
//		}else{
//			model.addAttribute("enterpriseType", 1);
//			model.addAttribute("enterpriseContent", "");
//		}
//		List<ILiveSendMsg> iLiveSendMsg=iLiveSendMsgMng.selectILiveSubById(roomId.longValue());
		if(pageNo==null){
			pageNo=1;
		}
		Pagination pagination = iLiveSendMsgMng.selectILiveRoomThirdPage( pageNo,10,roomId);
		model.addAttribute("pager", pagination ==null?"":pagination);
		model.addAttribute("topActive", "1");
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		model.addAttribute("liveEvent", liveEvent);
		return "liveconfig/shortmessage";
	}
	
	/**
	 * 查看发送记录页
	 * @param roomId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="shortmessagerecord.do")
	public String sendMessageRecord(Integer roomId,Long id,String sendUser,Timestamp startTime,Timestamp endTime,Integer pageNo,Integer pageSize,Model model) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		
		Pagination page = sendShortUserMng.getPage(roomId, id, sendUser, pageNo==null?1:pageNo, 10,startTime,endTime);
		
		model.addAttribute("page", page);
		model.addAttribute("messageid", id);
		model.addAttribute("sendUser", sendUser);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("leftActive", "2_9");
		model.addAttribute("topActive", "1");
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		model.addAttribute("liveEvent", liveEvent);
		return "liveconfig/shortmessagerecord";
	}
	
	/**
	 * 根据id查询群发给的所有人
	 * @param id
	 * @param response
	 */
	@RequestMapping(value="getRecordById.do")
	public void getRecordById(Long id,String mobile,HttpServletResponse response) {
		JSONObject res = new JSONObject();
		try {
			List<SendShortMessaheRecord> listByRecordId = sendShortMessaheRecordMng.getListByRecordId(id,mobile);
			res.put("data", JSONArray.fromObject(listByRecordId));
			res.put("status", 1);
		} catch (Exception e) {
			res.put("status", 2);
		}
		ResponseUtils.renderJson(response, res.toString());
	}
	/**
	 * 根据查询条件查询群发给的所有人
	 * @param id
	 * @param response
	 */
	@RequestMapping(value="getRecord.do")
	public void getRecord(Long id,String mobile,String start,String stop,HttpServletResponse response) {
		JSONObject res = new JSONObject();
		try {
			List<SendShortMessaheRecord> listByRecordId = sendShortMessaheRecordMng.getListByRecord(id,mobile,start,stop);
			res.put("data", JSONArray.fromObject(listByRecordId));
			res.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			res.put("status", 2);
		}
		ResponseUtils.renderJson(response, res.toString());
	}
	/**
	 * 根据id查询一次群发记录
	 * @param id
	 * @param response
	 */
	@RequestMapping(value="getUserddById.do")
	public void getUserddById(Long id,HttpServletResponse response) {
		JSONObject res = new JSONObject();
		try {
			SendShortUser byId = sendShortUserMng.getById(id);
			res.put("data", JSONObject.fromObject(byId));
			res.put("status", 1);
		} catch (Exception e) {
			res.put("status", 2);
		}
		ResponseUtils.renderJson(response, res.toString());
	}
	
	/**
	 * 发送一次短信
	 * @param id
	 * @param response
	 */
	@RequestMapping(value="sendMessage.do")
	public void sendMessage(Integer roomId,String shortMessage,String url,HttpServletResponse response,HttpServletRequest request) {
		JSONObject res = new JSONObject();
		try {
			UserBean user = ILiveUtils.getUser(request);
			ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.parseLong(user.getUserId()));
			SendShortUser sendMessage = new SendShortUser();
			ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
			ILiveEvent liveEvent = iliveRoom.getLiveEvent();
			ILiveServerAccessMethod accessMethodBySeverGroupId = null;
			try {
				accessMethodBySeverGroupId = accessMethodMng
						.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			url=accessMethodBySeverGroupId.getH5HttpUrl() + "/phone" + "/live.html?roomId="
					+ roomId;
			shortMessage = "亲,天翼直播邀请您观看将于"+liveEvent.getLiveStartTime()+"开始的直播"+liveEvent.getLiveTitle()+".直播地址:"+url;
			Integer enterpriseId = iliveRoom.getEnterpriseId();
			sendMessage.setRoomId(roomId);
			sendMessage.setEnterpriseId(enterpriseId);
			sendMessage.setShortMessage(shortMessage);
			sendMessage.setUserId(Long.parseLong(user.getUserId()));
			sendMessage.setUserName(iLiveManager.getNailName());
			sendMessage.setMobile(iLiveManager.getMobile());
			ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
			Integer certStatus = iLiveEnterprise.getCertStatus();
			boolean b = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_InvitedToWatch,certStatus);
//			if(b){
//				res.put("status", 2);
//				res.put("message", EnterpriseCache.ENTERPRISE_FUNCTION_InvitedToWatch_Content);
//				ResponseUtils.renderJson(response, res.toString());
//				return;
//			}else{
			   int size = userMessage.size();
				Integer numberByEnterpriseId = sendShortMessaheRecordMng.getNumberByEnterpriseId(enterpriseId);
				int number = size+numberByEnterpriseId;
				boolean c = EnterpriseUtil.selectIfContent(EnterpriseCache.ENTERPRISE_Sms, Long.valueOf(String.valueOf(number)), enterpriseId,certStatus);
				if(c){
					res.put("status", 2);
					res.put("message", "短信发送剩余不足");
					ResponseUtils.renderJson(response, res.toString());
					return;
				}
				boolean d = EnterpriseUtil.openEnterprise(enterpriseId, EnterpriseUtil.ENTERPRISE_USE_TYPE_Sms, String.valueOf(size),certStatus);
				if(d){
					res.put("status", 2);
					res.put("message", "更新企业产品信息失败");
					ResponseUtils.renderJson(response, res.toString());
					return;
				}
				
				
				Long id = sendShortUserMng.operatorSend(sendMessage,userMessage,url);
				workLogMng.save(new WorkLog(WorkLog.MODEL_INVITEMESSAGE, id+"", net.sf.json.JSONObject.fromObject(sendMessage).toString(), WorkLog.MODEL_INVITEMESSAGE_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
				userMessage.clear();
				res.put("status", 1);
//			}
		} catch (Exception e) {
			res.put("status", 2);
			res.put("message", "发送出现异常，请稍后重试");
		}
		ResponseUtils.renderJson(response, res.toString());
	}
	/**
	 * 发送一次短信
	 * @param id
	 * @param response
	 */
	@RequestMapping(value="sendMessage1.do")
	public void sendMessage1(Integer roomId,String shortMessage,String url,HttpServletResponse response,HttpServletRequest request) {
		JSONObject res = new JSONObject();
		try {
			UserBean user = ILiveUtils.getUser(request);
			ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.parseLong(user.getUserId()));
			SendShortUser sendMessage = new SendShortUser();
			ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
			Integer enterpriseId = iliveRoom.getEnterpriseId();
			sendMessage.setRoomId(roomId);
			sendMessage.setEnterpriseId(enterpriseId);
			sendMessage.setShortMessage(shortMessage);
			sendMessage.setUserId(Long.parseLong(user.getUserId()));
			sendMessage.setUserName(iLiveManager.getNailName());
			sendMessage.setMobile(iLiveManager.getMobile());
			ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
			Integer certStatus = iLiveEnterprise.getCertStatus();
			boolean b = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_InvitedToWatch,certStatus);
//			if(b){
//				res.put("status", 2);
//				res.put("message", EnterpriseCache.ENTERPRISE_FUNCTION_InvitedToWatch_Content);
//				ResponseUtils.renderJson(response, res.toString());
//				return;
//			}else{
			 List<ILiveSendMsg> iLiveSendMsg=iLiveSendMsgMng.selectILiveSubById(roomId.longValue());
			  Integer size = iLiveSendMsg.size();
			       for(int i=0;i<size;i++){
					userMessage.put(i+"",iLiveSendMsg.get(i).getMobile() );
				}
				Integer numberByEnterpriseId = sendShortMessaheRecordMng.getNumberByEnterpriseId(enterpriseId);
				Integer number = size+numberByEnterpriseId;
				boolean c = EnterpriseUtil.selectIfContent(EnterpriseCache.ENTERPRISE_Sms, Long.valueOf(String.valueOf(number)), enterpriseId,certStatus);
				if(c){
					res.put("status", 2);
					res.put("message", "短信发送剩余不足");
					ResponseUtils.renderJson(response, res.toString());
					return;
				}
				boolean d = EnterpriseUtil.openEnterprise(enterpriseId, EnterpriseUtil.ENTERPRISE_USE_TYPE_Sms, String.valueOf(size),certStatus);
				if(d){
					res.put("status", 2);
					res.put("message", "更新企业产品信息失败");
					ResponseUtils.renderJson(response, res.toString());
					return;
				}
				ILiveServerAccessMethod accessMethodBySeverGroupId = null;
				try {
					accessMethodBySeverGroupId = accessMethodMng
							.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				url=accessMethodBySeverGroupId.getH5HttpUrl() + "/phone" + "/live.html?roomId="
						+ roomId;
				Long id = sendShortUserMng.operatorSend1(sendMessage,userMessage,url);
				workLogMng.save(new WorkLog(WorkLog.MODEL_INVITEMESSAGE, id+"", net.sf.json.JSONObject.fromObject(sendMessage).toString(), WorkLog.MODEL_INVITEMESSAGE_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
				iLiveSendMsgMng.delete(roomId.longValue());
				res.put("status", 1);
//			}
		} catch (Exception e) {
			res.put("status", 2);
			res.put("message", "发送出现异常，请稍后重试");
		}
		ResponseUtils.renderJson(response, res.toString());
	}
	/**
	 * <name,phone>
	 */
	private static HashMap<String, String> userMessage = new HashMap<>();
	private ThreadLocal<HashMap<String, String>> messageLocal = new ThreadLocal<HashMap<String, String>>() {
		protected java.util.HashMap<String,String> initialValue() {
			return new HashMap<String, String>();
		};
	};
	
	/**
	 * 读取excel表
	 * @param id
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="readExcel.do")
	public void readExcel(@RequestParam("file")MultipartFile file,Long roomId,HttpServletResponse response,HttpServletRequest request) {
		JSONObject res = new JSONObject();
		UserBean user = ILiveUtils.getUser(request);
		iLiveSendMsgMng.delete(roomId);
		try {
			//Workbook workbook = getWorkBook(f);
		    try {
		    	HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
		    	System.out.println("文件不为空");
				//获取当前工作表
				Sheet sheet = workbook.cloneSheet(0);
				readExcel(sheet,roomId,user.getUsername());
		    }catch (Exception e) {
		    	XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		    	if(workbook!=null) {
					System.out.println("文件不为空");
					//获取当前工作表
					Sheet sheet = workbook.cloneSheet(0);
					readExcel(sheet,roomId,user.getUsername());
				}
		    }
			res.put("status", 1);
			JSONArray array = new JSONArray();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = format.format(new Date());
			for(Map.Entry<String, String> entry:userMessage.entrySet()) {
				JSONObject object = new JSONObject();
				object.put("name", entry.getKey());
				object.put("phone", entry.getValue());
				object.put("time", time);
				object.put("user", user.getUsername());
				array.add(object);
			}
			res.put("data", array);
		} catch (Exception e) {
			e.printStackTrace();
			res.put("status", 2);
		}
		ResponseUtils.renderJson(response, res.toString());
	}
	private void readExcel(Sheet sheet,Long roomId,String nailName) {
		int firstRow = sheet.getFirstRowNum();
		int lastRow = sheet.getLastRowNum();
		for(int i=firstRow+1;i<=lastRow;i++) {
			try {
				Row row = sheet.getRow(i);
//				row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
//				row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
				String name =(String) getCellFormatValue(row.getCell(1));
				String phone = (String) getCellFormatValue(row.getCell(0));
				System.out.println(name);
				System.out.println(phone);
				boolean ret =true;
				if(phone!=null||phone!=""){
					//查询表里面是不是有这条数据了
					List<ILiveSendMsg> sendMsgReode=iLiveSendMsgMng.selectILiveSubById(roomId);
					if(sendMsgReode!=null&&sendMsgReode.size()>0){
						for(ILiveSendMsg msg:sendMsgReode){
							if(phone.equals(msg.getMobile())){
								ret=false;
								break;
							}
						}
					}
					if(ret){
						ILiveSendMsg sendMsgMng=new ILiveSendMsg();
						Long id=iLiveSendMsgMng.selectMaxId();
						if(id==null){
							id=1L;
						}
						sendMsgMng.setId(id);
						sendMsgMng.setCreatTime(new Timestamp(System.currentTimeMillis()));
						sendMsgMng.setMobile(phone);
						sendMsgMng.setRemark(name);
						sendMsgMng.setRoomId(roomId);
						sendMsgMng.setLoadManager(nailName);
						iLiveSendMsgMng.save(sendMsgMng);
					}
				}
				
				userMessage.put(name, phone);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	 public static Object getCellFormatValue(Cell cell){
	        Object cellValue = null;
	        if(cell!=null){
	            //判断cell类型
	            switch(cell.getCellType()){
	            case Cell.CELL_TYPE_NUMERIC:{
	               // cellValue = String.valueOf(cell.getNumericCellValue());
	            	 DecimalFormat df = new DecimalFormat("#");
	            	 cellValue =df.format(cell.getNumericCellValue());
	                break;
	            }
	            case Cell.CELL_TYPE_FORMULA:{
	                //判断cell是否为日期格式
	                if(DateUtil.isCellDateFormatted(cell)){
	                    //转换为日期格式YYYY-mm-dd
	                    cellValue = cell.getDateCellValue();
	                }else{
	                    //数字
	                    cellValue = String.valueOf(cell.getNumericCellValue());
	                }
	                break;
	            }
	            case Cell.CELL_TYPE_STRING:{
	                cellValue = cell.getRichStringCellValue().getString().trim();
	                break;
	            }
	            default:
	                cellValue = "";
	            }
	        }else{
	            cellValue = "";
	        }
	       
	        return cellValue;
	    }
	@ResponseBody
	@RequestMapping(value="loadExcel")
	public void retExcel(HttpServletResponse response) throws Exception {
		String name =new String("短信模板.xls".getBytes(), "ISO8859-1");
		@SuppressWarnings("resource")
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
		HSSFSheet sheet = hssfWorkbook.createSheet("模板");
		HSSFRow row = sheet.createRow(0);
		HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
		HSSFCell cell1 = row.createCell(0);
		cell1.setCellStyle(cellStyle);
		cell1.setCellValue("手机号码");
		HSSFCell cell2 = row.createCell(1);
		cell2.setCellStyle(cellStyle);
		cell2.setCellValue("备注");
		try {
			
			this.setResponse(response, "短信模板.xls");
			
			OutputStream os = response.getOutputStream();
			hssfWorkbook.write(os);
	        os.flush();
	        os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setResponse(HttpServletResponse response,String fileName) throws UnsupportedEncodingException {
		fileName = new String(fileName.getBytes(),"ISO8859-1");
		response.setContentType("application/octet-stream;charset=ISO8859-1");
		response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
		response.addHeader("Pargam", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
	}
	
}
