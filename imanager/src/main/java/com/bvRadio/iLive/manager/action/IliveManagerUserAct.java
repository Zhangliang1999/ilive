package com.bvRadio.iLive.manager.action;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.VoiceStatus;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveComments;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFileComments;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveCommentsMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileCommentsMng;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.google.gson.JsonObject;

import net.sf.json.JSONArray;

/**
 * 
 * @author administrator 平台用户控制器 包含web用户 管理用户 等
 */
@Controller
@RequestMapping(value = "/user")
public class IliveManagerUserAct {

	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	
	@Autowired
	private ILiveMediaFileCommentsMng iLiveMediaFileCommentsMng;
	
	@Autowired
	private ILiveCommentsMng iLiveCommentsMng;
	
	/**
	 * 用户概览
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/overview.do")
	public String userOverView(Model model) {
		model.addAttribute("topActive", "4");
		model.addAttribute("leftActive", "1");
		return "user/overview";
	}
	
	/**
	 * web用户管理
	 * @param model
	 * @return
	 */
	@RequestMapping(value="userlist.do")
	public String userList(Model model,Integer pageSize,Integer pageNo,ILiveManager user) {
		Pagination page = iLiveManagerMng.getPage(user, 10, pageNo);
		model.addAttribute("user", user);
		model.addAttribute("page", page);
		model.addAttribute("topActive", "4");
		model.addAttribute("leftActive", "2");
		return "user/userlist";
	}
	
	/**
	 * 企业用户管理
	 * @param model
	 * @return
	 */
	@RequestMapping(value="enterpriseuserlist.do")
	public String enterpriseuserlist(Model model,Integer pageSize,Integer pageNo,ILiveManager user) {
		if (user==null) {
			user = new ILiveManager();
		}
		user.setCertStatus(4);
		Pagination page = iLiveManagerMng.getPage(user, 10, pageNo);
		model.addAttribute("user", user);
		model.addAttribute("page", page);
		model.addAttribute("topActive", "4");
		model.addAttribute("leftActive", "3");
		return "user/enterpriseuserlist";
	}
	
	/**
	 * 用户详情页
	 * @param model
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="userdetail.do")
	public String userDetails(Model model,Long userId) {
		ILiveManager user = iLiveManagerMng.getILiveManager(userId);
		//回看评论
		ILiveMediaFileComments filecommons = new ILiveMediaFileComments();
		filecommons.setUserId(userId);
		Pagination fileComments = iLiveMediaFileCommentsMng.getFileCommentsById(filecommons, 1, 10);
		//话题评论
		ILiveComments iLiveComments = new ILiveComments();
		iLiveComments.setUserId(userId);
		Pagination topicComments = iLiveCommentsMng.getByUserId(iLiveComments, 1, 10);
		model.addAttribute("user", user);
		model.addAttribute("topActive", "4");
		model.addAttribute("filecomments", fileComments.getList());
		model.addAttribute("filecommentsPage", fileComments.getTotalPage());
		model.addAttribute("topicComments", topicComments.getList());
		model.addAttribute("topicCommentsPage", topicComments.getTotalPage());
		if (user.getCertStatus()!=null && user.getCertStatus()==4) {
			model.addAttribute("leftActive", "3");
		}else {
			model.addAttribute("leftActive", "2");
		}
		return "user/userdetail";
	}
	
	/**
	 * 黑名单管理
	 * @param model
	 * @return
	 */
	@RequestMapping(value="blacklist.do")
	public String blacklist(Model model,Integer pageNo,Integer pageSize,ILiveManager user) {
		if (user==null) {
			user = new ILiveManager();
		}
		user.setIsBlack(1);
		
		Pagination page = iLiveManagerMng.getPage(user, 10, pageNo);
		model.addAttribute("user", user);
		model.addAttribute("page", page);
		model.addAttribute("topActive", "4");
		model.addAttribute("leftActive", "4");
		return "user/blacklist";
	}
	
	/**
	 * 黑名单详情
	 * @param model
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="blackdetail.do")
	public String blackDetail(Model model,Long userId) {
		ILiveManager user = iLiveManagerMng.getILiveManager(userId);
		model.addAttribute("user", user);
		model.addAttribute("topActive", "4");
		model.addAttribute("leftActive", "4");
		return "user/blackdetail";
	}
	//拉黑用户
	@RequestMapping(value="toblack.do")
	public void toblack(Long userId,String blackReason,HttpServletResponse response) {
		JSONObject res = new JSONObject();
		try {
			ILiveManager user = iLiveManagerMng.getILiveManager(userId);
			user.setIsBlack(1);
			user.setBlackReason(blackReason);
			iLiveManagerMng.updateLiveManager(user);
			res.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			res.put("status", 2);
		}
		ResponseUtils.renderJson(response, res.toString());
	}
	//拉黑多个用户
	@RequestMapping(value="toblackmany.do")
	public void toblackmany(String userIds,String blackReason,HttpServletResponse response) {
		JSONObject res = new JSONObject();
		try {
			@SuppressWarnings("static-access")
			JSONArray jsonArray = new JSONArray().fromObject(userIds);
			if(jsonArray.size()>0) {
				Long[] arr = new Long[jsonArray.size()];
				for(int i=0;i<jsonArray.size();i++) {
					arr[i] = jsonArray.getLong(i);
					ILiveManager user = iLiveManagerMng.getILiveManager(jsonArray.getLong(i));
					user.setIsBlack(1);
					user.setBlackReason(blackReason);
					iLiveManagerMng.updateLiveManager(user);
				}
			}
			res.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			res.put("status", 2);
		}
		ResponseUtils.renderJson(response, res.toString());
	}
	//释放拉黑用户
	@RequestMapping(value="release.do")
	public void release(Long userId,HttpServletResponse response) {
		JSONObject res = new JSONObject();
		try {
			ILiveManager user = iLiveManagerMng.getILiveManager(userId);
			user.setIsBlack(0);
			user.setBlackReason(null);
			iLiveManagerMng.updateLiveManager(user);
			res.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			res.put("status", 2);
		}
		ResponseUtils.renderJson(response, res.toString());
	}
	
	//获取拉黑原因
	@RequestMapping(value="getUser.do")
	public void getUser(Long userId,HttpServletResponse response) {
		JSONObject res = new JSONObject();
		try {
			ILiveManager user = iLiveManagerMng.getILiveManager(userId);
			res.put("data", user.getBlackReason());
			res.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			res.put("status", 2);
			res.put("data", "");
		}
		ResponseUtils.renderJson(response, res.toString());
	}

	//修改用户资料
	@RequestMapping(value="updateUser")
	public void updateUser(ILiveManager user,HttpServletResponse response,HttpServletRequest request) {
		JSONObject res = new JSONObject();
		try {
			ILiveManager userOld = iLiveManagerMng.getILiveManager(user.getUserId());
			userOld.setBeizhu(user.getBeizhu());
			userOld.setNailName(user.getNailName());
			userOld.setUpdateTime(new Timestamp(new Date().getTime()));
			
			try {
				UserBean userBean = ILiveUtils.getUser(request);
				if(userBean!=null) {
					userOld.setUpdateUserName(userBean.getUsername());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			iLiveManagerMng.updateLiveManager(userOld);
			res.put("status", 1);
			res.put("data", new JSONObject(userOld));
		} catch (Exception e) {
			e.printStackTrace();
			res.put("status", 2);
		}
		ResponseUtils.renderJson(response, res.toString());
	}
	
	//删除回看评论
	@RequestMapping(value="deletefilecomment.do")
	public void deletefilecomment(Long commentsId,HttpServletResponse response) {
		JSONObject res = new JSONObject();
		try {
			ILiveMediaFileComments fileComment = iLiveMediaFileCommentsMng.getFileCommentById(commentsId);
			fileComment.setDelFlag(1);
			iLiveMediaFileCommentsMng.update(fileComment);
			res.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			res.put("status", 2);
		}
		ResponseUtils.renderJson(response, res.toString());
	}
	//删除话题评论
	@RequestMapping(value="deletemsgcomment.do")
	public void deletemsgcomment(Long commentsId,HttpServletResponse response) {
		JSONObject res = new JSONObject();
		try {
			ILiveComments comments = iLiveCommentsMng.queryById(commentsId);
			comments.setIsDelete(true);
			iLiveCommentsMng.update(comments);
			res.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			res.put("status", 2);
		}
		ResponseUtils.renderJson(response, res.toString());
	}
	//查询回看评论
	@RequestMapping(value="searchfilecomment.do")
	public void searchfilecomment(ILiveMediaFileComments filecommons,String comments,Long fileId,Integer pageNo,Integer pageSize,HttpServletResponse response) {
		JSONObject res = new JSONObject();
		filecommons.setComments(comments);
		try {
			ILiveMediaFile file = new ILiveMediaFile();
			file.setFileId(fileId);
			filecommons.setiLiveMediaFile(file);
			Pagination fileComments = iLiveMediaFileCommentsMng.getFileCommentsById(filecommons, pageNo == null ?1:pageNo, 10);
			//Iterator<ILiveMediaFileComments> iterator = (Iterator<ILiveMediaFileComments>) fileComments.getList().iterator();
			res.put("fileComments", fileComments.getList());
			res.put("pageSize", fileComments.getTotalPage());
			res.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			res.put("status", 2);
		}
		ResponseUtils.renderJson(response, res.toString());
	}
	//查询话题评论
	@RequestMapping(value="searchmsgcomment.do",method=RequestMethod.POST)
	public void searchmsgcomment(ILiveComments iLivecomments,String comments,Integer pageNo,Integer pageSize,HttpServletResponse response) {
		JSONObject res = new JSONObject();
		iLivecomments.setComments(comments);
		try {
			Pagination msgComments = iLiveCommentsMng.getByUserId(iLivecomments, pageNo==null?1:pageNo, 10);
			res.put("msgComments", msgComments.getList());
			res.put("pageSize", msgComments.getTotalPage());
			res.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			res.put("status", 2);
		}
		ResponseUtils.renderJson(response, res.toString());
	}
	
	//删除多条回看评论
	@RequestMapping(value="delmanyfilecomment.do")
	public void delmanyfilecomment(String ids,HttpServletResponse response) {
		JSONObject res = new JSONObject();
		try {
			JSONArray array = JSONArray.fromObject(ids);
			if(array.size()>0) {
				for(int i=0;i<array.size();i++) {
					ILiveMediaFileComments fileComment = iLiveMediaFileCommentsMng.getFileCommentById(array.getLong(i));
					fileComment.setDelFlag(1);
					iLiveMediaFileCommentsMng.update(fileComment);
				}
			}
			res.put("status", 1);
		}catch (Exception e) {
			e.printStackTrace();
			res.put("status", 2);
		}
		ResponseUtils.renderJson(response, res.toString());
	}
	//删除多条话题评论
	@RequestMapping(value="delmanymsgcomment.do")
	public void delmanymsgcomment(String ids,HttpServletResponse response) {
		JSONObject res = new JSONObject();
		try {
			JSONArray array = JSONArray.fromObject(ids);
			if(array.size()>0) {
				for(int i=0;i<array.size();i++) {
					ILiveComments comments = iLiveCommentsMng.queryById(array.getLong(i));
					comments.setIsDelete(true);
					iLiveCommentsMng.update(comments);
				}
			}
			res.put("status", 1);
		}catch (Exception e) {
			e.printStackTrace();
			res.put("status", 2);
		}
		ResponseUtils.renderJson(response, res.toString());
	}
	
}
