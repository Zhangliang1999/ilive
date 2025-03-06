package com.bvRadio.iLive.iLive.action.admin;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.action.util.SubAccountCache;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.PictureInfo;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveSubLevelMng;
import com.bvRadio.iLive.iLive.manager.PhotoGalleryMng;
import com.bvRadio.iLive.iLive.manager.PictureInfoMng;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
/**
 * 子账户  图片库管理
 * @author YanXL
 *
 */
@Controller
@RequestMapping(value="/collaborative/picture")
public class CollaborativePhotoGalleryController {

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
		model.addAttribute("leftActive", "4_1");
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
	public String editPicture(Model model,Long id) {
		PictureInfo picture = pictureInfoMng.getById(id);
		
		model.addAttribute("picture", picture);
		model.addAttribute("leftActive", "4_1");
		model.addAttribute("topActive", "2");
		return "picture/editpicture";
	}
}
