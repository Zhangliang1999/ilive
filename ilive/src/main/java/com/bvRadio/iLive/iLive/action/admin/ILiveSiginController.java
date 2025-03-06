package com.bvRadio.iLive.iLive.action.admin;





import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.util.ExcelUtil;
import com.bvRadio.iLive.iLive.action.util.SubAccountCache;
import com.bvRadio.iLive.iLive.entity.ILiveComment;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveRedPacket;
import com.bvRadio.iLive.iLive.entity.ILiveSignin;
import com.bvRadio.iLive.iLive.entity.ILiveSigninUser;
import com.bvRadio.iLive.iLive.entity.ILiveUploadServer;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveCommentMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveRedPacketMng;
import com.bvRadio.iLive.iLive.manager.ILiveSignRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveSigninMng;
import com.bvRadio.iLive.iLive.manager.ILiveSigninUserMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubLevelMng;
import com.bvRadio.iLive.iLive.manager.ILiveUploadServerMng;
import com.bvRadio.iLive.iLive.util.FileUtils;
import com.bvRadio.iLive.iLive.util.TwoDimensionCode;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="siginin")
public class ILiveSiginController {

	
	
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;// 直播间
	
	@Autowired
	private ILiveSigninUserMng signinUserMng;	//人员管理
	@Autowired
	private ILiveSigninMng signinMng;
	@Autowired
	private ILiveUploadServerMng iLiveUploadServerMng;
	@Autowired
	private ILiveCommentMng commentMng;
	@Autowired
	private ILiveRedPacketMng packetMng;
	@Autowired
	private ILiveSubLevelMng iLiveSubLevelMng;
	
	
	/**
	 * 口令红包列表页
	 * @param roomId
	 * @param model
	 * @param name
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="redPacketlist.do")
	public String redPacketlist(Integer roomId,Model model,String name,Integer pageNo,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		Pagination page =packetMng.getPage(name,roomId,pageNo==null?1:pageNo, 10);
		model.addAttribute("page", page);
		model.addAttribute("name", name);
		
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		model.addAttribute("topActive", "1");
		model.addAttribute("leftActive", "4_5");
		return "siginIn/redPacketList";
	}
	/**
	 * 口令红包新增
	 * @param roomId
	 * @param model
	 * @param name
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="redPacketAdd.do")
	public String redPacketAdd(Integer roomId,Model model) {
		
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		model.addAttribute("topActive", "2");
		model.addAttribute("leftActive", "4_5");
		return "siginIn/redPacketAdd";
	}
	
	/**
	 * 口令红包新增保存
	 * @param roomId
	 * @param model
	 * @param name
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="redPacketSave.do")
	public String redPacketSave(Integer roomId,ILiveRedPacket redPacket,Model model,HttpServletRequest request) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		UserBean user = ILiveUtils.getUser(request);
		Timestamp now = new Timestamp(new Date().getTime());
		redPacket.setCreateTime(now);
		redPacket.setIsAllow(0);
		redPacket.setUserName(user.getUsername());
		redPacket.setUserId(Long.parseLong(user.getUserId()));
		redPacket.setRoom(iLiveLiveRoom);
		packetMng.save(redPacket);
		return "redirect:redPacketlist.do?roomId="+roomId;
	}
	
	/**
	 * 评论上墙
	 * @param roomId
	 * @param model
	 * @param name
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="comment.do")
	public String comment(Integer roomId,Model model,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveComment comment =commentMng.getIsStart(roomId);
		String requestPath = ConfigUtils.get("free_login_review");
		if(comment==null) {
			model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
			model.addAttribute("topActive", "1");
			model.addAttribute("leftActive", "3_4");
			return "siginIn/commentAdd";
		}else {
			model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
			model.addAttribute("topActive", "1");
			model.addAttribute("leftActive", "3_4");
			model.addAttribute("comment", comment);
			model.addAttribute("requestPath", requestPath);
			return "siginIn/commentEdit";
		}
	}
	
	/**
	 * 评论上墙修改
	 * @param roomId
	 * @param model
	 * @param name
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="commentUpdate.do")
	public String commentUpdate(String imgUrl,Integer roomId,String commentId,ILiveComment comment,Model model,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		Timestamp now = new Timestamp(new Date().getTime());
		ILiveComment ilivecomment =commentMng.getById(commentId);
		ilivecomment.setImgUrl(imgUrl);
		ilivecomment.setIsAllow(comment.getIsAllow());
		ilivecomment.setStartTime(comment.getStartTime());
		ilivecomment.setEndTime(comment.getEndTime());
		ilivecomment.setUpdateUserName(user.getUsername());
		ilivecomment.setUpdateTime(now);
		commentMng.update(ilivecomment);
		return "redirect:comment.do?roomId="+roomId;
	}
	
	/**
	 * 评论上墙新增
	 * @param roomId
	 * @param model
	 * @param name
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws FileNotFoundException 
	 */
	@RequestMapping(value="commentAdd.do")
	public String commentAdd(String imgUrl,Integer roomId,ILiveComment comment,Model model,HttpServletRequest request) throws FileNotFoundException {
		UserBean user = ILiveUtils.getUser(request);
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		comment.setCommentId(UUID.randomUUID().toString());
		comment.setImgUrl(imgUrl);
		comment.setUserId(Long.parseLong(user.getUserId()));
		comment.setUserName(user.getUsername());
		comment.setRoom(iLiveLiveRoom);
		
		String ext = "jpg";
		String tempFileName = System.currentTimeMillis() + "." + ext;
		String realPath = request.getSession().getServletContext().getRealPath("/temp");
		File tempFile = new File(realPath + "/" + tempFileName);
		TwoDimensionCode.encoderQRCode(ConfigUtils.get("comment_in_phone")+"?roomId="+roomId,  realPath + "/" + tempFileName,"jpg");
		
		String filePath = FileUtils.getTimeFilePath(tempFileName);
		
			boolean result = false;
			ILiveUploadServer uploadServer = iLiveUploadServerMng.getDefaultServer();
			if (uploadServer != null) {
				FileInputStream in = new FileInputStream(tempFile);
				result = uploadServer.upload(filePath, in);
			}
			
			if(tempFile.exists()) {
				tempFile.delete();
			}
			String httpUrl = uploadServer.getHttpUrl() + uploadServer.getFtpPath() + "/" + filePath;
		comment.setCommentUrl(ConfigUtils.get("comment_in_phone")+"?roomId="+roomId);
		comment.setEwImg(httpUrl);
		commentMng.save(comment);
		return "redirect:comment.do?roomId="+roomId;
	}
	
	/**
	 * 签到活动列表页
	 * @param roomId
	 * @param model
	 * @param name
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="siginlist.do")
	public String signList(Model model,String name,Integer state,Integer pageNo,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		if(state==null) {
			state=0;
		}
		Pagination page =null;
		Integer enterpriseId=user.getEnterpriseId();
		Integer level = user.getLevel();
		level = level==null?ILiveManager.USER_LEVEL_ADMIN:level;
		//查询子账户是否具有
		boolean per=iLiveSubLevelMng.selectIfCan(request, SubAccountCache.ENTERPRISE_FUNCTION_signActivity);
		if(level.equals(ILiveManager.USER_LEVEL_SUB)&&!per) {
			page =signinMng.getPageByUserId(name,user.getUserId(), state, pageNo==null?1:pageNo, 10);
		}else {
			page =signinMng.getPageByEnterpriseId(enterpriseId.toString(),name,enterpriseId, state, pageNo==null?1:pageNo, 10);
		}
		
		
		model.addAttribute("page", page);
		model.addAttribute("totalPage", page.getTotalPage());
		model.addAttribute("name", name);
		model.addAttribute("state", state);
		model.addAttribute("topActive", "2");
		model.addAttribute("leftActive", "4_9");
		return "siginIn/siginList";
	}
	
	/**
	 * 签到名单列表页
	 * @param roomId
	 * @param model
	 * @param name
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="siginUserlist.do")
	public String siginUserlist(Integer roomId,Long signId,Model model,String name,String roomName,Date startTime,Date endTime,Integer pageNo) {
		ILiveSignin sign = signinMng.getById(signId.longValue());
		Pagination page =signinUserMng.getPage(signId+"",name,roomName, startTime,endTime, pageNo==null?1:pageNo, 10);
		model.addAttribute("page", page);
		model.addAttribute("totalPage", page.getTotalPage());
		model.addAttribute("name", name);
		model.addAttribute("sign", sign);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("roomId", roomId);
		model.addAttribute("topActive", "2");
		model.addAttribute("leftActive", "4_9");
		return "siginIn/siginUserList";
	}
	
	/**
	 * 导出签到名单
	 * @param roomId
	 * @param model
	 * @param name
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="downsiginUser.do")
	public void downsiginUser(Integer roomId,Long signId,String name,String roomName,Date startTime,Date endTime,HttpServletRequest request,HttpServletResponse response) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveSignin sign = signinMng.getById(signId.longValue());
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<ILiveSigninUser>  ILiveSigninUser = signinUserMng.getList(signId+"",name,roomName, startTime,endTime);
			String[] title = {"序号","用户账号","用户昵称","用户ID","直播ID","直播名称","签到时间","用户头像"};
			List<String[]> excelBody = new ArrayList<String[]>();
			int i=1;
			for (ILiveSigninUser signUser : ILiveSigninUser) {
				String id=i+"";
				String userPhone = String.valueOf(signUser.getUserPhone()==null?0:signUser.getUserPhone());
				String userName = String.valueOf(signUser.getUserName()==null?0:signUser.getUserName());
				String userId = String.valueOf(signUser.getUserId()==null?0:signUser.getUserId());
				String roomIds = String.valueOf(signUser.getRoom().getRoomId()==null?0:signUser.getRoom().getRoomId());
				String roomNames = signUser.getRoom().getLiveEvent().getLiveTitle()==null?"":signUser.getRoom().getLiveEvent().getLiveTitle();
				String time = format.format(signUser.getCreateTime()==null?new Date():signUser.getCreateTime());
				String userPhoto = String.valueOf(signUser.getUserPhoto()==null?0:signUser.getUserPhoto());
				String[] str = {id,userPhone,userName,userId,roomIds,roomNames,time,userPhoto};
				excelBody.add(str);
				i++;
			}
			HSSFWorkbook workbook = ExcelUtil.excelExport(title,excelBody);
			this.setResponse(response, new Date().getTime()+".xls");
			OutputStream os = response.getOutputStream();
	        workbook.write(os);
	        os.flush();
	        os.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
	/**
	 * 新增
	 */
	@RequestMapping(value="add.do",method=RequestMethod.GET)
	public String add(Model model) {
		
		
		model.addAttribute("topActive", "2");
		model.addAttribute("leftActive", "4_9");
		return "siginIn/add";
	}
	/**
	 * 新增保存
	 */
	@RequestMapping(value="save.do")
	public String save(String imgUrl,ILiveSignin sigin,Model model,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		sigin.setImgUrl(imgUrl);
		sigin.setIsAllow(0);
		sigin.setUserId(Long.parseLong(user.getUserId()));
		sigin.setUserName(user.getUsername());
		sigin.setEnterpriseId(user.getEnterpriseId());
		signinMng.save(sigin);
		return "redirect:siginlist.do";
	}
	
	
	/**
	 * 编辑
	 */
	@RequestMapping(value="edit.do",method=RequestMethod.GET)
	public String add(Integer roomId,Long siginId,Model model,HttpServletRequest request) {
		
		ILiveSignin sigin =signinMng.getById(siginId);
		String requestPath = ConfigUtils.get("free_login_review");
		model.addAttribute("requestPath", requestPath);
		model.addAttribute("sigin", sigin);
		model.addAttribute("topActive", "2");
		model.addAttribute("leftActive", "4_9");
		return "siginIn/edit";
	}
	/**
	 * 只读
	 */
	@RequestMapping(value="preview.do",method=RequestMethod.GET)
	public String preview(Integer roomId,Long siginId,Model model,HttpServletRequest request) {
		
		
		ILiveSignin sigin =signinMng.getById(siginId);
		String requestPath = ConfigUtils.get("free_login_review");
		model.addAttribute("requestPath", requestPath);
		model.addAttribute("sigin", sigin);
		model.addAttribute("topActive", "2");
		model.addAttribute("leftActive", "4_9");
		return "siginIn/preview";
	}
	/**
	 * 修改保存
	 */
	@RequestMapping(value="update.do")
	public String update(Integer iLiveRoomId,Long siginId,String imgUrl,ILiveSignin sigin,Model model,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		
		ILiveSignin sign =signinMng.getById(siginId);
		Timestamp now = new Timestamp(new Date().getTime());
		
		
		sign.setUpdateTime(now);
		sign.setUpdateUserName(user.getUsername());
		sign.setImgUrl(imgUrl);
		sign.setName(sigin.getName());
		sign.setStartTime(sigin.getStartTime());
		sign.setEndTime(sigin.getEndTime());
		sign.setState(sigin.getState());
		sign.setName(sigin.getName());;
		signinMng.update(sign);
		return "redirect:siginlist.do";
	}
	
	/**
	 * 活动开关
	 */
	@RequestMapping(value="letclose.do",method=RequestMethod.POST)
	public @ResponseBody String letclose(Integer id,Integer isSwitch,Model model) {
		
		try {
			ILiveSignin singin =signinMng.getById(id.longValue());
			singin.setIsAllow(isSwitch);
			signinMng.update(singin);
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
		
	}
	
	
	/**
	 * 签到二维码
	 * @throws FileNotFoundException 
	 */
	@RequestMapping(value="signew.do",method=RequestMethod.POST)
	public void userSign(Long id, HttpServletResponse response,Integer type,HttpServletRequest request) throws FileNotFoundException {
		ILiveSignin signin =signinMng.getById(id);
		JSONObject json = new JSONObject();
		try {
			if(signin.getEwImg()!=null&&!"".equals(signin.getEwImg())) {
				json.put("code", 1);
				json.put("imgUrl", signin.getEwImg());
				json.put("url", ConfigUtils.get("sign_in_phone_sign")+"?signId="+id);
			}else {

				String ext = "jpg";
				String tempFileName = System.currentTimeMillis() + "." + ext;
				String realPath = request.getSession().getServletContext().getRealPath("/temp");
				File tempFile = new File(realPath + "/" + tempFileName);
				TwoDimensionCode.encoderQRCode(ConfigUtils.get("sign_in_phone_sign")+"?signId="+id,  realPath + "/" + tempFileName,"jpg");
				
				String filePath = FileUtils.getTimeFilePath(tempFileName);
				
					boolean result = false;
					ILiveUploadServer uploadServer = iLiveUploadServerMng.getDefaultServer();
					if (uploadServer != null) {
						FileInputStream in = new FileInputStream(tempFile);
						result = uploadServer.upload(filePath, in);
					}
					
					if(tempFile.exists()) {
						tempFile.delete();
					}
					String httpUrl = uploadServer.getHttpUrl() + uploadServer.getFtpPath() + "/" + filePath;
					if (result) {
						json.put("code", 2);
						json.put("imgUrl", httpUrl);
						json.put("url", ConfigUtils.get("sign_in_phone_sign")+"?signId="+id);
						
					} else {
						json.put("code", 0);
					}
					signin.setEwImg(httpUrl);
					signinMng.update(signin);
				
			}
			
		} catch (Exception e) {
			json.put("code", 0);
			
		}
		
		ResponseUtils.renderJson(response, json.toString());
		
	}
}
