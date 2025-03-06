package com.bvRadio.iLive.iLive.action.admin;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseFans;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseTerminalUser;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseFansMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseTerminalUserMng;
import com.bvRadio.iLive.iLive.manager.TerminalUserMng;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

@Controller
@RequestMapping(value = "terminal")
public class TerminalUserController {
	
	@Autowired
	private TerminalUserMng userMng;
	@Autowired
	private ILiveEnterpriseFansMng fansMng;
	@Autowired
	private ILiveEnterpriseTerminalUserMng TerminalUserMng;
	
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;
	/**
	 * 终端粉丝管理
	 * @return
	 */
	@RequestMapping(value = "usermanager.do")
	public String uersmanager(Model model, String queryNum, Integer pageNo,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		UserBean userBean = user;
		Integer enterpriseId = userBean.getEnterpriseId();
		Pagination page = fansMng.getPage(queryNum,enterpriseId, pageNo, 20);
		ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
		if(iLiveEnterprise!=null) {
			Integer certStatus = iLiveEnterprise.getCertStatus();
			boolean b = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_EnterpriseFanImport,certStatus);
			if(b){
				model.addAttribute("enterpriseCode", 0);
				model.addAttribute("enterpriseMessage", EnterpriseCache.ENTERPRISE_FUNCTION_EnterpriseFanImport_Content);
			}else{
				model.addAttribute("enterpriseCode", 1);
				model.addAttribute("enterpriseMessage", "");
			}
			//检查是否开通收益账户
			Integer getIfPassRevenue=EnterpriseUtil.getIfPassRevenue(enterpriseId, certStatus);
			if(getIfPassRevenue!=null) {
				model.addAttribute("getIfPassRevenue", getIfPassRevenue);
			}
		}
		model.addAttribute("page", page);
		model.addAttribute("fanstype",-1);
		model.addAttribute("totalPage", page.getTotalPage());
		model.addAttribute("topActive", "6");
		model.addAttribute("leftActive", "7_1");
		return "user/terminal_user_list";
	}
	/**
	 * 黑名单管理
	 * @return
	 */
	@RequestMapping(value = "blacklist.do")
	public String blacklist(Model model, String queryNum, Integer pageNo,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		Integer enterpriseId = user.getEnterpriseId();
		Pagination page = fansMng.getPageBlack(queryNum,enterpriseId, pageNo, 20);
		model.addAttribute("page", page);
		model.addAttribute("totalPage", page.getTotalPage());
		model.addAttribute("topActive", "6");
		model.addAttribute("leftActive", "7_3");
		try {
			Long userId = Long.valueOf(user.getUserId());
			model.addAttribute("operationUserId", userId);
		} catch (Exception e) {
			model.addAttribute("operationUserId", 0);
		}
		return "user/blacklist";
	}

	/**
	 * 终端用户详情  修改
	 * 
	 * @return
	 */
	@RequestMapping(value = "userdetail.do")
	public String uersdetail(Model model,Long userId) {
		ILiveManager manager = userMng.getById(userId);
		model.addAttribute("user", manager);
		model.addAttribute("topActive", "3");
		model.addAttribute("leftActive", "4");
		return "user/terminal_user_detail";
	}
	
	/**
	 * 终端用户详情 查看
	 * 
	 * @return
	 */
	@RequestMapping(value = "seeuser.do")
	public String seeuser(Model model,Long userId) {
		ILiveManager manager = userMng.getById(userId);
		model.addAttribute("user", manager);
		model.addAttribute("topActive", "3");
		model.addAttribute("leftActive", "4");
		return "user/terminal_user_see";
	}

	
	/**
	 * 拉黑粉丝用户
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "letblack.do")
	public String letblack(Long id) {
		fansMng.letblack(id);
		return "";
	}
	
	/**
	 * 保存终端用户
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveUser.do")
	public String saveUser(ILiveManager user) {
		ILiveManager primaryUser = userMng.getById(user.getUserId());
		userMng.updateUser(user);
		return "";
	}
	
	/**
	 * 终端用户列表
	 * @return
	 */
	@RequestMapping(value = "terminaluser.do")
	public String terminaluser(Model model, String queryNum, Integer pageNo,Integer fanstype,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		Pagination page = TerminalUserMng.getPage(queryNum, fanstype,pageNo, 10,user.getEnterpriseId());
		if(queryNum == null) {
			queryNum = "";
		}
		if(fanstype == null) {
			fanstype = -1;
		}
		UserBean userBean = user;
		Integer enterpriseId = userBean.getEnterpriseId();
		ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
		if(iLiveEnterprise!=null) {
			Integer certStatus = iLiveEnterprise.getCertStatus();
			boolean b = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_EnterpriseFanImport,certStatus);
			if(b){
				model.addAttribute("enterpriseCode", 0);
				model.addAttribute("enterpriseMessage", EnterpriseCache.ENTERPRISE_FUNCTION_EnterpriseFanImport_Content);
			}else{
				model.addAttribute("enterpriseCode", 1);
				model.addAttribute("enterpriseMessage", "");
			}
			//检查是否开通收益账户
			Integer getIfPassRevenue=EnterpriseUtil.getIfPassRevenue(enterpriseId, certStatus);
			if(getIfPassRevenue!=null) {
				model.addAttribute("getIfPassRevenue", getIfPassRevenue);
			}
		}
		model.addAttribute("queryNum",queryNum);
		model.addAttribute("fanstype",fanstype);
		model.addAttribute("page", page);
		model.addAttribute("totalPage", page.getTotalPage());
		model.addAttribute("topActive", "6");
		model.addAttribute("leftActive", "7_1");
		return "user/terminal_user_list";
	}
	/**
	 * 拉黑终端用户
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "letbuserblack.do")
	public void letbuserlack(Long id) {
		TerminalUserMng.letbuserblack(id);
	}
	/**
	 * 删除终端用户
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "removeTerminaluser.do")
	public void removeTerminaluser(Long id) {
		TerminalUserMng.removeTerminaluser(id);
	}
	/**
	 * 删除粉丝
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "removeuser.do")
	public void removeuser(Long id) {
		fansMng.delFans(id);
	}
	/**
	 * 删除黑名单用户
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "removeBlackuser.do")
	public void removeBlackuser(Long id) {
		TerminalUserMng.removeBlackuser(id);
	}
	@RequestMapping(value="excelexport.do")
	@ResponseBody
	public void excel(String queryNum,Integer fanstype,HttpServletRequest request,HttpServletResponse response) {
		String[] title = {"ID","用户ID","昵称"};
		UserBean userBean = ILiveUtils.getUser(request);
		Pagination page = fansMng.getPage(queryNum,userBean.getEnterpriseId(), 1, 20);
		//List<ILiveEnterpriseTerminalUser> list = (List<ILiveEnterpriseTerminalUser>) page.getList();
		//List<ILiveEnterpriseTerminalUser> list =TerminalUserMng.queryList(queryNum,fanstype);
//		List<ILiveEnterpriseTerminalUser> list =TerminalUserMng.queryList(queryNum,fanstype,userBean.getEnterpriseId());
		 List<ILiveEnterpriseFans> list= (List<ILiveEnterpriseFans>) page.getList();
		List<String[]> excelBody = new LinkedList<>();
		for(ILiveEnterpriseFans user:list) {
			String[] strings = new String[5];
			strings[0] =String.valueOf(user.getId());
			strings[1] =String.valueOf(user.getUserId());
			strings[2] =String.valueOf(user.getNailName());
			excelBody.add(strings);
		}
		HSSFWorkbook workbook = excelExport(title,excelBody);
		try {
			this.setResponse(response,"ceshi.xls");
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
