package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.common.page.SimplePage.cpn;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.action.util.ExcelUtil;
import com.bvRadio.iLive.iLive.entity.ILiveAPIGateWayRouter;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILivePlayRewards;
import com.bvRadio.iLive.iLive.entity.ILiveUserPlayRewards;
import com.bvRadio.iLive.iLive.manager.ILiveAPIGateWayRouterMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILivePlayRewardsMng;
import com.bvRadio.iLive.iLive.manager.ILiveUserPlayRewardsMng;

/**
 * 打赏  
 * @author YanXL
 *
 */
@Controller
@RequestMapping(value="play")
public class ILivePlayRewardsAct {
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;
	
	@Autowired
	private ILivePlayRewardsMng iLivePlayRewardsMng;
	
	@Autowired
	private ILiveUserPlayRewardsMng iLiveUserPlayRewardsMng;
	@Autowired
	private ILiveAPIGateWayRouterMng iLiveAPIGateWayRouterMng;
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 打赏设置
	 * @param roomId 直播间ID 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/selectRewards.do")
	public String selectILivePlayRewards(Integer roomId,ModelMap map,HttpServletRequest request, HttpServletResponse response){
		ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
		map.addAttribute("iLiveLiveRoom", iliveRoom);
		try {
			ILivePlayRewards iLivePlayRewards =  iLivePlayRewardsMng.selectILivePlayRewardsById(roomId);
			if(iLivePlayRewards==null){
				iLivePlayRewards = new ILivePlayRewards();
				iLivePlayRewards.setRoomId(roomId);
				iLivePlayRewards.setRewardsSwitch(ILivePlayRewards.ROOM_REWARDS_SWITCH_OFF);
				iLivePlayRewards.setRewardsTitleType(ILivePlayRewards.ROOM_REWARDS_TITLE_Language);
				iLivePlayRewardsMng.addILivePlayRewards(iLivePlayRewards);
			}
			map.addAttribute("iLivePlayRewards", iLivePlayRewards);
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.addAttribute("topActive", "1");
		map.addAttribute("leftActive", "4_5");
		String apiRouterUrl = "";
		List<ILiveAPIGateWayRouter> routerList = iLiveAPIGateWayRouterMng.getRouterList();
		if (routerList != null && !routerList.isEmpty()) {
			ILiveAPIGateWayRouter iLiveAPIGateWayRouter = routerList.get(0);
			apiRouterUrl = iLiveAPIGateWayRouter.getRouterUrl();
		}
		map.addAttribute("apiRouterUrl", apiRouterUrl);
		return "rewards/play_rewards";
	}
	
	/**
	 * 获取打赏记录
	 * @param userContent 检索用户信息
	 * @param roomContent 检索直播间信息
	 * @param roomId 直播间ID
	 * @param pageNo 页码
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/user/selectRewardsList.do")
	public String selectILiveUserPlayRewards(String userContent,String roomContent,Integer roomId,Integer pageNo,ModelMap map,HttpServletRequest request, HttpServletResponse response){
		ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
		map.addAttribute("iLiveLiveRoom", iliveRoom);
		Pagination pagination = new Pagination(cpn(pageNo), 20, 0);
		try {
			if(userContent!=null){
				if(userContent.equals("")){
					userContent=null;
				}
			}
			if(roomContent!=null){
				if(roomContent.equals("")){
					roomContent=null;
				}
			}
			pagination = iLiveUserPlayRewardsMng.selectILiveUserPlayRewardsByPage(userContent,roomContent,roomId,cpn(pageNo), 20);
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.addAttribute("pagination", pagination);
		map.addAttribute("userContent", userContent);
		map.addAttribute("roomContent", roomContent);
		map.addAttribute("topActive", "1");
		map.addAttribute("leftActive", "4_5");
		return "rewards/play_rewards_list";
	}
	/**
	 * 导出
	 * @param userContent
	 * @param roomContent
	 * @param map
	 * @param roomId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/user/excelexport.do")
	@ResponseBody
	public void updateRoomUserGift(String userContent,String roomContent,ModelMap map,Integer roomId,HttpServletRequest request, HttpServletResponse response){
		try {
			if(userContent!=null){
				if(userContent.equals("")){
					userContent=null;
				}
			}
			if(roomContent!=null){
				if(roomContent.equals("")){
					roomContent=null;
				}
			}
			List<ILiveUserPlayRewards>  iLiveUserPlayRewardsList = iLiveUserPlayRewardsMng.selectILiveUserPlayRewardsByExcel(userContent,roomContent,roomId);
			String[] title = {"序号","直播场次ID","直播标题","用户ID","用户名","打赏时间","打赏金额"};
			List<String[]> excelBody = new ArrayList<String[]>();
			for (ILiveUserPlayRewards iLiveUserPlayRewards : iLiveUserPlayRewardsList) {
				String id = String.valueOf(iLiveUserPlayRewards.getId()==null?0:iLiveUserPlayRewards.getId());
				String evenId = String.valueOf(iLiveUserPlayRewards.getEvenId()==null?0:iLiveUserPlayRewards.getEvenId());
				String roomTitle = iLiveUserPlayRewards.getRoomTitle()==null?"":iLiveUserPlayRewards.getRoomTitle();
				String userId = String.valueOf(iLiveUserPlayRewards.getUserId()==null?0:iLiveUserPlayRewards.getUserId());
				String userName = iLiveUserPlayRewards.getUserName()==null?"":iLiveUserPlayRewards.getUserName();
				String time = format.format(iLiveUserPlayRewards.getPlayRewardsTime()==null?new Date():iLiveUserPlayRewards.getPlayRewardsTime());
				String playRewardsAmount = String.valueOf(iLiveUserPlayRewards.getPlayRewardsAmount()==null?0:iLiveUserPlayRewards.getPlayRewardsAmount());
				String[] str = {id,evenId,roomTitle,userId,userName,time,playRewardsAmount};
				excelBody.add(str);
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
}
