package com.bvRadio.iLive.iLive.action.admin;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.dao.ILiveEnterpriseMemberDao;
import com.bvRadio.iLive.iLive.dao.ILiveViewWhiteBillDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseWhiteBill;
import com.bvRadio.iLive.iLive.entity.ILiveViewWhiteBill;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMemberMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewWhiteBillMng;
import com.bvRadio.iLive.iLive.util.DownloadUtil;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

import net.sf.json.JSONObject;

/**
 * @author administrator 会员管理
 */
@Controller
@RequestMapping(value = "member")
public class ILiveEnterpriseMemberAct {

	@Autowired
	private ILiveEnterpriseMemberMng iliveEnterproseMng;
	@Autowired
	private ILiveViewWhiteBillMng viewWhiteMng;
	@Autowired
	private ILiveFieldIdManagerMng iLivefieldIdManagerMng;
	@Autowired
	private ILiveViewWhiteBillDao iLiveViewWhiteBillDao;
	@Autowired
	private ILiveEnterpriseMemberDao iliveEnterpriseDao;

	/**
	 * 白名单
	 */
	@RequestMapping(value = "whitelist.do")
	public void whiteListRouter(String queryNum, Integer pageNo, Integer pageSize,Long iLiveEventId,
			HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			
			Pagination page = viewWhiteMng.getPage(queryNum, pageNo, 20, iLiveEventId);
			resultJson.put("status", 1001);
	    	resultJson.put("page", page);
		} catch (Exception e) {
			resultJson.put("status", 1002);
	    	
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	/**
	 * 白名单
	 */
	@RequestMapping(value = "delAll.do")
	public void delAll( HttpServletRequest request,HttpServletResponse response,Long iLiveEventId) {
		JSONObject resultJson = new JSONObject();
		try {
			
				iLiveViewWhiteBillDao.clearViewWhiteBill(iLiveEventId);
				resultJson.put("status", 1001);
		} catch (Exception e) {
			resultJson.put("status", 1002);
		}
		
			
			
			ResponseUtils.renderJson(request,response, resultJson.toString());
		
	}
	
	/**
	 * 白名单模板下载
	 */
	@RequestMapping(value="/download/excel.do")
	public void downloadExcel(HttpServletRequest request,HttpServletResponse response) {
		String downExcel = DownloadUtil.downExcel(null, "白名单模板.xlsx", "", request, response);
	}
	
	/**
	 * 白名单模板下载2
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/download/excel2.do")
	public void downloadExcel2(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
//		String downExcel = DownloadUtil.downExcel(null, "白名单模板.xlsx", "", request, response);
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow head = sheet.createRow(0);
		head.createCell(0).setCellValue("手机号");
		head.createCell(1).setCellValue("姓名");
		response.setContentType("application/octet-stream;charset=ISO8859-1");
		response.setHeader("Content-Disposition", "attachment;filename="+ new String("白名单模板.xls".getBytes(),"ISO8859-1"));
		response.addHeader("Pargam", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
		try {
			ServletOutputStream outputStream = response.getOutputStream();
			workbook.write(outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 终端用户
	 */
	@RequestMapping(value = "terminalUser.do")
	public String terminalUser(Model model, String queryNum, Integer pageNo, Integer pageSize,
			HttpServletRequest request) {
		UserBean userBean = ILiveUtils.getUser(request);
		Integer enterpriseId = userBean.getEnterpriseId();
		if (enterpriseId == null) {
			enterpriseId = 100;
		}
		Pagination page = iliveEnterproseMng.getPage(queryNum, pageNo, 20, enterpriseId);
		model.addAttribute("page", page);
		return "whitelist/whitebill";
	}

	/**
	 * 添加白名单
	 */
	@RequestMapping(value = "addwhite.do")
	public void addWhiteListRouter(Model model, ILiveEnterpriseWhiteBill white, Long iLiveEventId, 
			HttpServletRequest request,HttpServletResponse response ) {
		JSONObject resultJson = new JSONObject();
		try {
			    List<ILiveViewWhiteBill>  list=viewWhiteMng.getAllViewWhiteBilll(white.getPhoneNum(), iLiveEventId);
			    if(list.size()!=0&&list!=null) {
			    	
			    	viewWhiteMng.deleteById(white.getPhoneNum(), iLiveEventId);
			    }
			    	ILiveViewWhiteBill whiteBill = new ILiveViewWhiteBill();
					long billId = iLivefieldIdManagerMng.getNextId("ilive_view_whitebill", "bill_id", 1);
					whiteBill.setBillId(billId);
					whiteBill.setPhoneNum(white.getPhoneNum());
					whiteBill.setLiveEventId(iLiveEventId);
					whiteBill.setUserName(white.getUserName());
					whiteBill.setExportTime(new Timestamp(System.currentTimeMillis()));
					viewWhiteMng.saveIliveViewWhiteBill(whiteBill);
			    
			    
			
			resultJson.put("status", 1001);
		} catch (Exception e) {
			resultJson.put("status", 1002);
		}
		
		
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}

	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping(value = "deletewhite.do")
	public void deleteWhite(Integer whitebillId,String phoneNum,Long iLiveEventId, 
			HttpServletRequest request,HttpServletResponse response ) {
		JSONObject resultJson = new JSONObject();
		try {
			viewWhiteMng.deleteById(phoneNum, iLiveEventId);
			resultJson.put("status", 1001);
		} catch (Exception e) {
			resultJson.put("status", 1002);
		}
		
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}

	/**
	 * 白名单导出
	 * 
	 * @return
	 */
	@RequestMapping(value="excelexport.do")
	@ResponseBody
	public void excel(Long liveEventId,String queryNum,HttpServletRequest request,HttpServletResponse response) {
		List<ILiveViewWhiteBill> list =viewWhiteMng.getAllViewWhiteBilll(queryNum, liveEventId);
		String[] title = {"场次ID","用户","姓名"};
		List<String[]> excelBody = new LinkedList<>();
		for(ILiveViewWhiteBill user:list) {
			String[] strings = new String[3];
			strings[0] =String.valueOf(user.getLiveEventId());
			strings[1] =String.valueOf(user.getPhoneNum());
			strings[2] =String.valueOf(user.getUserName());
			
			excelBody.add(strings);
		}
		HSSFWorkbook workbook = excelExport(title,excelBody);
		try {
			this.setResponse(response,"白名单用户.xls");
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
