package com.bvRadio.iLive.iLive.action.admin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.action.util.SubAccountCache;
import com.bvRadio.iLive.iLive.entity.DocumentManager;
import com.bvRadio.iLive.iLive.entity.DocumentPicture;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.DocumentManagerMng;
import com.bvRadio.iLive.iLive.manager.DocumentPictureMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubLevelMng;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

/**
 * 子账户   文档库
 * @author YanXL
 *
 */
@Controller
@RequestMapping(value="/collaborative/document")
public class CollaborativeDocumentController {

	
	@Autowired
	private DocumentManagerMng documentManage;	//文档库
	
	@Autowired	
	private DocumentPictureMng documentPictureMng;	//文档图片
	@Autowired
	private ILiveSubLevelMng iLiveSubLevelMng;
	/**
	 * 文档库主页列表
	 * @param model
	 * @param name
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="list.do")
	public String list(Model model,String name,Integer pageNo,Integer pageSize,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		Pagination page = new Pagination( pageNo==null?1:pageNo, 10, 0, new ArrayList<>());
		Integer level = user.getLevel();
		level = level==null?ILiveManager.USER_LEVEL_ADMIN:level;
		//查询子账户是否具有图片查看全部
		boolean per=iLiveSubLevelMng.selectIfCan(request, SubAccountCache.ENTERPRISE_FUNCTION_document);
		if(level.equals(ILiveManager.USER_LEVEL_SUB)&&!per){
			long userId = Long.parseLong(user.getUserId());
			page = documentManage.getCollaborativePage(name, pageNo==null?1:pageNo, 10, userId);
		}else{
			Integer enterpriseId = user.getEnterpriseId();
			page = documentManage.getPage(name,enterpriseId, pageNo==null?1:pageNo, 10);
		}
		Integer serverGroupId = this.selectServerGroup();
		model.addAttribute("serverGroupId", serverGroupId);
		model.addAttribute("page", page);
		model.addAttribute("name", name);
		model.addAttribute("num", page.getTotalCount());
		model.addAttribute("leftActive", "3_1");
		model.addAttribute("topActive", "2");
		return "document/list";
	}
	private Integer selectServerGroup() {
		return 100;
	}
	/**
	 * 一个文档中的所有图片
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="documentpicture")
	public String documentPicture(Model model,Long docId) {
		System.out.println(docId);
		DocumentManager doc = documentManage.getById(docId);
		
		Pagination page = documentPictureMng.getPage(docId,1,10);
		@SuppressWarnings("unchecked")
		List<DocumentPicture> listpicu = (List<DocumentPicture>) page.getList();
		if(listpicu==null) {
			listpicu = new ArrayList<>();
		}
		model.addAttribute("doc", doc);
		model.addAttribute("listpicu", listpicu);
		model.addAttribute("page", page.getTotalPage());
		model.addAttribute("leftActive", "3_1");
		model.addAttribute("topActive", "2");
		return "document/documentpicture";
	}
	
}
