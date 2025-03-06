package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.common.page.SimplePage.cpn;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.BBSDiyform;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.manager.BBSDiyFormOptionChoiceMng;
import com.bvRadio.iLive.iLive.manager.BBSDiyformMng;
import com.bvRadio.iLive.iLive.manager.BBSDiymodelMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveMessageMng;
import com.bvRadio.iLive.iLive.util.ExpressionUtil;

@Controller
@RequestMapping(value="interactmanager")
public class ILiveRoomInteractController {

	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;// 直播间
	
	@Autowired
	private ILiveMessageMng iLiveMessageMng;//消息管理
		
	@Autowired
	private ILiveLiveRoomMng iLiveRoomMng;
	
	@Autowired
	private BBSDiyformMng bbsDiyformMng;
	
	@Autowired
	private BBSDiymodelMng bbsDiymodelMng;
	@Autowired
	private BBSDiyFormOptionChoiceMng bBSDiyFormOptionChoiceMng;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="interact.do")
	public String interact(Integer roomId,Integer interactType,Model model,String searchContent,Integer pageNo) {
		if(interactType==null) {
			interactType = 2;
		}
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		Pagination pagination = iLiveMessageMng.getPage(roomId, interactType ,searchContent,cpn(pageNo), 20 );
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		List<ILiveMessage> list = (List<ILiveMessage>) pagination.getList();
		for (ILiveMessage iLiveMessage : list) {
			String msgContent = iLiveMessage.getMsgContent();
			msgContent = ExpressionUtil.replaceKeyToImg(msgContent);
			iLiveMessage.setMsgContent(msgContent);
		}
		model.addAttribute("pagination", pagination);
		model.addAttribute("interactType", interactType);
		model.addAttribute("searchContent", searchContent);
		model.addAttribute("leftActive", "4_1");
		model.addAttribute("topActive", "1");
		return "interactManager/interact";
	}
	
	@RequestMapping("entervote")
	public String enterVote(String voteTitle,Integer roomId,Integer pageNo,Integer pageSize,
			HttpServletRequest request, HttpServletResponse response, ModelMap model ){
		Map<String, Object> sqlParam = new HashMap<>();
		sqlParam.put("voteTitle", voteTitle);
		sqlParam.put("delFlag", 1);
		sqlParam.put("roomId",roomId);
		Pagination pagination = bbsDiyformMng.getaVotePage(voteTitle, pageNo, pageSize);
		if(pagination!=null&&pagination.getList()!=null&&pagination.getList().size()>0){
			for (BBSDiyform bBSDiyform : (List<BBSDiyform>)pagination.getList()) {
				sqlParam.put("diyFormId", bBSDiyform.getDiyformId());
				bBSDiyform.setVoteCount(bBSDiyFormOptionChoiceMng.selectUserCount(sqlParam).intValue());
			}
		}
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		model.addAttribute("pagination", pagination);
		model.addAttribute("voteTitle", voteTitle);
		model.addAttribute("leftActive", "4_3");
		model.addAttribute("topActive", "1");
		return "interactManager/vote/vote";
	}
	
	@RequestMapping("deletevote")
	public void deleteVote(Integer voteId,
			HttpServletRequest request, HttpServletResponse response, ModelMap model ){
		bbsDiyformMng.deleteVoteById(voteId);
		ResponseUtils.renderJson(response, "{\"status\":\"success\"}");
	}
	@RequestMapping("entervotecount")
	public String enterVoteCount(String voteTitle,Integer voteId,Integer roomId,
			HttpServletRequest request, HttpServletResponse response, ModelMap model ){
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		Map<String, Object> sqlParam = new HashMap<>();
		sqlParam.put("diyFormId", voteId);
		BigInteger count = bBSDiyFormOptionChoiceMng.selectUserCount(sqlParam);
		model.addAttribute("voteTitle", voteTitle);
		model.addAttribute("count", count);
		model.addAttribute("voteId", voteId);
		return "interactManager/vote/votecount";
	}
	@RequestMapping("entervotedetails")
	public String enterVoteDetails(String voteTitle,Integer voteId,String username,Integer roomId,Integer pageNo,Integer pageSize,
			HttpServletRequest request, HttpServletResponse response, ModelMap model ){
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		Map<String, Object> sqlParam = new HashMap<>();
		sqlParam.put("diyFormId", voteId);
		sqlParam.put("username", username);
		Pagination pagination = bBSDiyFormOptionChoiceMng.getMedialFilePage(sqlParam, pageNo, pageSize);
		BigInteger count = bBSDiyFormOptionChoiceMng.selectUserCount(sqlParam);
		model.addAttribute("pagination", pagination);
		model.addAttribute("voteId", voteId);
		model.addAttribute("username", username);
		model.addAttribute("voteTitle", voteTitle);
		model.addAttribute("count", count);
		return "interactManager/vote/votedetails";
	}
	@RequestMapping("entervoteedit")
	public String enterVoteEdit(Long roomId,Integer pageNo,Integer pageSize,
			HttpServletRequest request, HttpServletResponse response, ModelMap model ){
		ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId.intValue());
		model.addAttribute("iLiveLiveRoom", iliveRoom);
		model.addAttribute("leftActive", "2_4");
		model.addAttribute("topActive", "1");
		BBSDiyform diyform = bbsDiyformMng.findById(iliveRoom.getLiveEvent().getFormId());
		if(diyform!=null){
			model.addAttribute("bbsDiyform", diyform);
			model.addAttribute("bbsDiymodelList", bbsDiymodelMng.getListByDiyformId(diyform.getDiyformId()));			
		}
		return "interactManager/vote/voteadd";
	}

}
