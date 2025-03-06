package com.bvRadio.iLive.iLive.action.admin;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.util.SubAccountCache;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.PictureInfo;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveSubLevelMng;
import com.bvRadio.iLive.iLive.manager.PhotoGalleryMng;
import com.bvRadio.iLive.iLive.manager.PictureInfoMng;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="picture")
public class PhotoGalleryController {

	@Autowired
	private PhotoGalleryMng photoGalleryMng;
	
	@Autowired
	private PictureInfoMng pictureInfoMng;
	@Autowired
	private ILiveSubLevelMng iLiveSubLevelMng;
	/**
	 * 图片库主页
	 * @param photoName
	 * @param model
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="list.do")
	public String list(String photoName,Model model,Integer pageNo,Integer pageSize,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		Pagination page = photoGalleryMng.getPage(user.getEnterpriseId(),pageNo==null?1:pageNo, 19);
		model.addAttribute("page", page);
		model.addAttribute("leftActive", "1_4");
		model.addAttribute("topActive", "2");
		return "picture/list";
	}
	
	/**
	 * 一个图片库内的图片
	 * @param photoName
	 * @param galleryId
	 * @param model
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="picturelist.do")
	public String picturelist(String photoName,Model model,Integer pageNo,Integer pageSize,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		Pagination page = new Pagination( pageNo==null?1:pageNo, 19, 0, new ArrayList<>());
		Integer level = user.getLevel();
		level = level==null?ILiveManager.USER_LEVEL_ADMIN:level;
		//查询子账户是否具有图片查看全部
		boolean per=iLiveSubLevelMng.selectIfCan(request, SubAccountCache.ENTERPRISE_FUNCTION_picture);
		if(level.equals(ILiveManager.USER_LEVEL_SUB)&&!per){
			long userId = Long.parseLong(user.getUserId());
			page = pictureInfoMng.getCollaborativePage(photoName, pageNo==null?1:pageNo, 19, userId);
		}else{
			Integer enterpriseId = user.getEnterpriseId();
			page = pictureInfoMng.getPage(photoName,enterpriseId, pageNo==null?1:pageNo, 19);
		}
		Integer serverGroupId = this.selectServerGroup();
		model.addAttribute("page", page.getList());
		model.addAttribute("number", page.getTotalCount());
		model.addAttribute("pageNum", page.getTotalPage());
		model.addAttribute("leftActive", "1_4");
		model.addAttribute("topActive", "2");
		model.addAttribute("serverGroupId", serverGroupId);
		return "picture/picturelist";
	}
	private Integer selectServerGroup() {
		return 100;
	}
	
	/**
	 * 修改图片信息页面
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="editpicturepage.do")
	public String editPicture(Model model,Long id,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		PictureInfo picture = pictureInfoMng.getById(id);
		if(picture.getEnterpriseId().equals(user.getEnterpriseId())) {
			model.addAttribute("picture", picture);
			model.addAttribute("leftActive", "1_4");
			model.addAttribute("topActive", "2");
			return "picture/editpicture";
		}else {
			return "redirect:/admin/login.do";
		}
		
	}
	
	/**
	 * 保存一个图片
	 * @param model
	 * @param id
	 */
	@ResponseBody
	@RequestMapping(value="savepicture.do")
	public void savepicture(PictureInfo pictureInfo,HttpServletResponse response,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			UserBean user = ILiveUtils.getUser(request);
			pictureInfo.setUserName(user.getUsername());
			pictureInfo.setEditName(user.getUsername());
			pictureInfo.setEnterpriseId(user.getEnterpriseId());
			pictureInfo.setUserId(Long.parseLong(user.getUserId()));
			Long id = pictureInfoMng.save(pictureInfo);
			PictureInfo byId = pictureInfoMng.getById(id);
			jsonObject.put("status", 1);
			jsonObject.put("pictureInfo", byId);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("status", 2);
		}
		ResponseUtils.renderJson(response, jsonObject.toString());
	}
	
	/**
	 * 修改一个图片
	 * @param model
	 * @param id
	 */
	@ResponseBody
	@RequestMapping(value="editpicture.do",method=RequestMethod.POST)
	public void editpicture(Long id,String name,String descript,HttpServletResponse response,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			UserBean user = ILiveUtils.getUser(request);
			PictureInfo pictureInfo = pictureInfoMng.getById(id);
			pictureInfo.setName(name);
			pictureInfo.setDescript(descript);
			pictureInfo.setEditName(user.getUsername());
			pictureInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			pictureInfoMng.update(pictureInfo);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String updateTime = format.format(new Date());
			jsonObject.put("status", 1);
			jsonObject.put("pictureInfo", pictureInfo);
			jsonObject.put("updateTime", updateTime);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("status", 2);
		}
		ResponseUtils.renderJson(response, jsonObject.toString());
	}
	
	/**
	 * 删除一个图片
	 * @param response
	 * @param id
	 */
	@ResponseBody
	@RequestMapping(value="delete.do")
	public void deletePicture(HttpServletResponse response,Long id) {
		JSONObject result = new JSONObject();
		try {
			pictureInfoMng.delete(id);
			result.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 搜索
	 * @param name
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="search.do",method=RequestMethod.GET)
	public void search(String name,HttpServletResponse response,HttpServletRequest request) {
		JSONObject result = new JSONObject();
		try {
			UserBean user = ILiveUtils.getUser(request);
			Pagination page = pictureInfoMng.getPage(name,user.getEnterpriseId(), 1, 20);
			List<PictureInfo> list = (List<PictureInfo>) page.getList();
			JSONArray array = new JSONArray();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(PictureInfo info:list) {
				JSONObject object = JSONObject.fromObject(info);
				object.put("createTime", format.format(info.getCreateTime()));
				array.add(object);
			}
			result.put("data", array);
			result.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	@ResponseBody
	@RequestMapping(value="download.do")
	public void downLoadImage(Long id,HttpServletResponse response) throws UnsupportedEncodingException {
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
				System.out.println(id);
				PictureInfo info = pictureInfoMng.getById(id);
				String url = info.getUrl();
//				String b = string.replaceAll("//", "\\\\\\\\");
//				File file = new File(b);
//				FileImageInputStream imageInputStream = new FileImageInputStream(file);
//				int length = (int)imageInputStream.length();
//				byte[] bytes = new byte[length];
//				imageInputStream.read(bytes,0,length);
//				imageInputStream.close();
				
				URL url2 = new URL(url);
				DataInputStream dataInputStream = new DataInputStream(url2.openStream());
				byteArrayOutputStream = new ByteArrayOutputStream();
				byte[] bytes = new byte[1024];
				int length ;
				while((length = dataInputStream.read(bytes))!=-1) {
					System.out.println(length);
					byteArrayOutputStream.write(bytes, 0, length);
				}
				
//				String fileName = new String(info.getName().getBytes(),"ISO8859-1");
//				response.setContentType("application/octet-stream;charset=ISO8859-1");
//				response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
//				response.addHeader("Pargam", "no-cache");
//				response.addHeader("Cache-Control", "no-cache");
				System.out.println("response开始响应");
				response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(info.getName(), "utf-8"));
				response.setHeader("Content-Type",info.getType());
				ServletOutputStream outputStream = response.getOutputStream();
				outputStream.write(byteArrayOutputStream.toByteArray());
				outputStream.flush();
				outputStream.close();
				System.out.println("response返回完毕");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
