package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.common.page.SimplePage.cpn;
import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveFCode;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveViewAuthBill;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveFCodeMng;
import com.bvRadio.iLive.iLive.manager.ILiveFileAuthenticationRecordMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewAuthBillMng;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

@Controller
@RequestMapping(value = "config")
public class ILiveFCodeAct {
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;
	@Autowired
	private ILiveFCodeMng iLiveFCodeMng;
	@Autowired
	private ILiveViewAuthBillMng iLiveViewAuthBillMng;
	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	@Autowired
	private ILiveFileAuthenticationRecordMng iLiveFileAuthenticationRecordMng;
	/**
	 * F码设置
	 * 
	 * @return
	 */
	@RequestMapping(value = "fset.do")
	public String iLiveConfigFSet(Model model, Integer roomId, Long fileId, Integer pageNo, String codeName, String code,
			Integer status) {
		Pagination pagination = iLiveFCodeMng.pageByParams(codeName, code, status, roomId, fileId,null,null, cpn(pageNo), 20);
		model.addAttribute("pagination", pagination);
		model.addAttribute("roomId", roomId);
		model.addAttribute("fileId", fileId);
		model.addAttribute("status", status);
		model.addAttribute("codeName", codeName);
		model.addAttribute("code", code);
		if (null != fileId) {
			model.addAttribute("topActive", "2");
			model.addAttribute("leftActive", "1");
		} else {
			ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
			ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
			model.addAttribute("liveEvent", liveEvent);
			model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
			model.addAttribute("topActive", "1");
			model.addAttribute("leftActive", "6_4");
		}
		return "liveconfig/fset";
	}
/**
 * 直播间观看授权列表显示
 */
	@ResponseBody
	@RequestMapping(value = "fpager.do")
	public void pager(Integer roomId,Long fileId,Integer pageNo,HttpServletRequest request, HttpServletResponse response,Integer creatType,String searchCode){
		//f码数据列表
	Integer type=1;
	if(creatType!=null){
		if(creatType==5){
			creatType=null;
		}
	}
	if("".equals(searchCode)&&searchCode!=null){
		searchCode=null;
	}
	Pagination pagination = iLiveFCodeMng.getBeanBySearchCode(roomId, fileId,type,creatType,searchCode,cpn(pageNo), 20);
		JSONObject resultJson = new JSONObject();
		resultJson.put("status", 1001);
		resultJson.put("pager", pagination.getList());
		resultJson.put("totalCount", pagination.getTotalCount());
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 左侧历史记录表
	 */
		@ResponseBody
		@RequestMapping(value = "hisfpager.do")
		public String hispager(Model model,Integer roomId,Long fileId,Integer pageNo,HttpServletRequest request, HttpServletResponse response){
			Integer type=2;
			Pagination pagination = iLiveFCodeMng.getBeanByCode(roomId, fileId,type,cpn(pageNo), 20);
			
			model.addAttribute("pagination", pagination);
			model.addAttribute("roomId", roomId);
			model.addAttribute("fileId", fileId);
			model.addAttribute("topActive", "1");
			model.addAttribute("leftActive", "6_4");
			return "liveconfig/frecode";
		}
	/**
	 * 添加F码
	 * 
	 * @param codeName
	 * @param startTime
	 * @param endTime
	 * @param codeNum
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "addFCode.do")
	public void addFCode(Integer liveRoomId, Long fileId, String codeName, Date startTime, Date endTime, Integer codeNum,HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			List<ILiveFCode> list = iLiveFCodeMng.save(liveRoomId, fileId, codeName, startTime, endTime, codeNum);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "新建观看码"+codeNum+"个成功！");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "新建观看码"+codeNum+"个失败！");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 根据id重置F码
	 */
	@ResponseBody
	@RequestMapping(value = "resetFCodeById.do")
	public void resetFCodeById(Integer roomId,Long fileId, Long codeId,HttpServletRequest request, HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			//F码置为失效
			ILiveFCode fCode = iLiveFCodeMng.getBeanById(codeId);
			fCode.setStatus(ILiveFCode.STATUS_CANCELED);
			fCode.setOutTime(new Timestamp(System.currentTimeMillis()));
			iLiveFCodeMng.update(fCode);
			//健全表中对应的F码鉴权记录也需要删除掉
			//健全表中对应的F码鉴权记录也需要删除掉
			if(fCode.getBindUserId()!=null){
				String userId =fCode.getBindUserId().toString();
				if(roomId!=null){
					iLiveViewAuthBillMng.deleteLiveViewAuthBillByLiveRoomId(roomId,userId);
				}else if(fileId!=null){
					iLiveViewAuthBillMng.deleteLiveViewAuthBillByfileId(userId,fileId);
					//同時把健全歷史表中相應記錄給刪掉
					iLiveFileAuthenticationRecordMng.deleteFileAuthenticationRecordByFileId(fileId);
				}
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "重置成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "重置失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 重置F码
	 */
	@ResponseBody
	@RequestMapping(value = "resetFCode.do")
	public void resetFCode(Integer roomId,Long fileId,@RequestParam(value = "codes[]")String[] codes,HttpServletRequest request, HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			for(int i=0;i<codes.length;i++){
				//F码置为失效
				ILiveFCode fCode = iLiveFCodeMng.getBeanByCode(roomId, fileId, codes[i]);
				fCode.setStatus(ILiveFCode.STATUS_CANCELED);
				fCode.setOutTime(new Timestamp(System.currentTimeMillis()));
				iLiveFCodeMng.update(fCode);
				//健全表中对应的F码鉴权记录也需要删除掉
				//健全表中对应的F码鉴权记录也需要删除掉
				if(fCode.getBindUserId()!=null){
					String userId =fCode.getBindUserId().toString();
					if(roomId!=null){
						iLiveViewAuthBillMng.deleteLiveViewAuthBillByLiveRoomId(roomId,userId);
					}else if(fileId!=null){
						iLiveViewAuthBillMng.deleteLiveViewAuthBillByfileId(userId,fileId);
						//同時把健全歷史表中相應記錄給刪掉
						iLiveFileAuthenticationRecordMng.deleteFileAuthenticationRecordByFileId(fileId);
					}
				}
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "重置成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "重置失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 重置F码
	 */
	@ResponseBody
	@RequestMapping(value = "resetAllFCode.do")
	public void resetAllFCode(Integer roomId,Long fileId,HttpServletRequest request, HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			//F码置为失效
			List<ILiveFCode> list = iLiveFCodeMng.getBeanByCode(roomId, fileId);
			for(ILiveFCode fCode:list){
				fCode.setStatus(ILiveFCode.STATUS_CANCELED);
				fCode.setOutTime(new Timestamp(System.currentTimeMillis()));
				iLiveFCodeMng.update(fCode);
				//健全表中对应的F码鉴权记录也需要删除掉
				if(fCode.getBindUserId()!=null){
					String userId =fCode.getBindUserId().toString();
					if(roomId!=null){
						iLiveViewAuthBillMng.deleteLiveViewAuthBillByLiveRoomId(roomId,userId);
					}else if(fileId!=null){
						iLiveViewAuthBillMng.deleteLiveViewAuthBillByfileId(userId,fileId);
						//同時把健全歷史表中相應記錄給刪掉
						iLiveFileAuthenticationRecordMng.deleteFileAuthenticationRecordByFileId(fileId);
					}
				}
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "重置成功");
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "重置失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 绑定F码
	 * 
	 * @param codeName
	 * @param startTime
	 * @param endTime
	 * @param codeNum
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "bindFCode.do")
	public void bindFCode(Integer roomId,Long fileId, String code,HttpServletRequest request, HttpServletResponse response) {
		ILiveFCode fCode = iLiveFCodeMng.getBeanByCode(roomId, fileId, code);
		JSONObject resultJson = new JSONObject();
		UserBean user = ILiveUtils.getUser(request);
		String userId = user.getUserId();
		if (null != fCode && null != userId) {
			Integer status = fCode.getStatus();
			if (!ILiveFCode.STATUS_NEW.equals(status)) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "F码已失效");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			Long bindId=fCode.getBindUserId();
			if(bindId!=null&&!userId.equals(bindId)){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "该F码已被其他用户绑定！");
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			//Long id = fCode.getId();
			//iLiveFCodeMng.useByCodeId(id, Long.parseLong(userId));
			ILiveManager manager= iLiveManagerMng.selectILiveManagerById(Long.parseLong(userId));
			try {
				fCode.setBindUserId(Long.parseLong(userId));
				fCode.setBindUserName(manager.getUserName());
				fCode.setBingNailName(manager.getNailName());
				iLiveFCodeMng.update(fCode);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ILiveLiveRoom liveRoom = iLiveLiveRoomMng.findById(roomId);
			ILiveEvent liveEvent = liveRoom.getLiveEvent();
			Long liveEventId = liveEvent.getLiveEventId();
			ILiveViewAuthBill authbill = new ILiveViewAuthBill();
			authbill.setAuthPassTime(new Timestamp(System.currentTimeMillis()));
			authbill.setAuthType(ILiveViewAuthBill.AUTH_TYPE_FCODE);
			authbill.setDeleteStatus(0);
			authbill.setLiveEventId(liveEventId);
			authbill.setLiveRoomId(roomId);
			authbill.setUserId(String.valueOf(userId));
			iLiveViewAuthBillMng.addILiveViewAuthBill(authbill);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "F码验证成功");
			ResponseUtils.renderJson(request, response, resultJson.toString());
		} else {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "F码验证失败");
			ResponseUtils.renderJson(request, response, resultJson.toString());
		}
	}
	
	/**
	 * 直播间观看授权列表显示
	 */
		@ResponseBody
		@RequestMapping(value = "fsearch.do")
		public void fsearch(Integer creatType,String searchCode,Integer roomId,Long fileId,Integer pageNo,HttpServletRequest request, HttpServletResponse response){
			Integer type=1;
			if(creatType==5){
				creatType=null;
			}
			if("".equals(searchCode)){
				searchCode=null;
			}
			Pagination pagination = iLiveFCodeMng.getBeanBySearchCode(roomId, fileId,type,creatType,searchCode,cpn(pageNo), 20);
			JSONObject resultJson = new JSONObject();
			resultJson.put("status", 1001);
			resultJson.put("code", 0);
			resultJson.put("pager", pagination);
			ResponseUtils.renderJson(request, response, resultJson.toString());
		}
	
	
	
	/**
	 * 删除F码
	 * 
	 * @param codeId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "cancelFCode.do")
	public String cancelFCode(@RequestParam("ids[]") Long[] ids) {
		iLiveFCodeMng.cancelByCodeIds(ids);
		return "success";
	}
	/**
	 * F码记录导出
	 * 
	 * @return
	 */
	@RequestMapping(value="excelexport.do")
	@ResponseBody
	public void excel(Integer creatType,String searchCode,Integer roomId,Long fileId,HttpServletRequest request,HttpServletResponse response) {
		if(creatType==5){
			creatType=null;
		}
		List<ILiveFCode> list = iLiveFCodeMng.listByParams(searchCode, null, creatType, roomId, null, null, null);
		if(searchCode!=null&&!"".equals(searchCode)){
			List<ILiveFCode> list1 = iLiveFCodeMng.listByParams(null, searchCode, creatType, roomId, null, null, null);
			list.addAll(list1);
		}
		String[] title = {"观看码","活动名称","状态","绑定账号","绑定昵称","绑定时间"};
		List<String[]> excelBody = new LinkedList<>();
		for(ILiveFCode fCode:list) {
			if(fCode.getStatus()!=3){
				String[] strings = new String[6];
				strings[0] =String.valueOf(fCode.getCode()==null?"":fCode.getCode());
				strings[1] =String.valueOf(fCode.getCodeName()==null?"":fCode.getCodeName());
				String Status="";
				if(fCode.getStatus()==1){
					Status="未使用";
				}else if(fCode.getStatus()==2){
					Status="已绑定";
				}
				strings[2] =String.valueOf(Status);
				strings[3] =String.valueOf(fCode.getBindUserName()==null?"":fCode.getBindUserName());
				strings[4] =String.valueOf(fCode.getBingNailName()==null?"":fCode.getBingNailName());
				strings[5] =String.valueOf(fCode.getBindTime()==null?"":fCode.getBindTime());
				excelBody.add(strings);
			}
		}
		HSSFWorkbook workbook = excelExport(title,excelBody);
		try {
			this.setResponse(response,"观看码记录.xls");
			OutputStream os = response.getOutputStream();
	        workbook.write(os);
	        os.flush();
	        os.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
	//excel导出
			public HSSFWorkbook excelExport(String[] title,List<String[]> body) {
				//1、创建HSSFWorkbook文件对应一个Excel文件
				HSSFWorkbook workbook = new HSSFWorkbook();
				//2、创建一个sheet
				HSSFSheet sheet = workbook.createSheet();
				//3、在sheet中添加表头第0行
				HSSFRow rowHead = sheet.createRow(0);
				//4、创建单元格 并设置表头格式
				HSSFCellStyle style = workbook.createCellStyle();
				//style.setAlignment(HSSFCellStyle.ALIGN_CENTER);	//创建一个居中格式
				
				//声明对象
				HSSFCell cell = null;
				
				//创建标题
				for(int i = 0;i<title.length;i++) {
					cell = rowHead.createCell(i);
					cell.setCellValue(title[i]);
					cell.setCellStyle(style);
				}
				
				//添加内容
				if (body.size()>0) {
					for (int i = 0; i < body.size(); i++) {
						String[] s = body.get(i);
						HSSFRow rowBody = sheet.createRow(i+1);
						for(int j=0;j<s.length;j++) {
							HSSFCell cell2 = rowBody.createCell(j);
							cell2.setCellValue(s[j]);
						}
					}
				}
				
				return workbook;
			}
}
