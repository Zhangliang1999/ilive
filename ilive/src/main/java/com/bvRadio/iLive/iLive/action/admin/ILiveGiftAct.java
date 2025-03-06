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
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveGift;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveUserGift;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveAPIGateWayRouterMng;
import com.bvRadio.iLive.iLive.manager.ILiveGiftMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveUserGiftMng;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
/**
 * 礼物
 * @author YanXL
 *
 */
@Controller
@RequestMapping(value="gift")
public class ILiveGiftAct {
	
	@Autowired
	private ILiveGiftMng iLiveGiftMng;
	
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;
	
	@Autowired
	private ILiveAPIGateWayRouterMng iLiveAPIGateWayRouterMng;
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 礼物列表
	 * @param map
	 * @param roomId 直播间
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/selectGift.do")
	public String selectILiveGiftList(ModelMap map,Integer roomId,HttpServletRequest request, HttpServletResponse response){
		UserBean user = ILiveUtils.getUser(request);
		List<ILiveGift> iLiveGifts = iLiveGiftMng.selectIliveGiftByUserId(Long.parseLong(user.getUserId()),roomId);
		map.addAttribute("iLiveGifts", iLiveGifts);
		List<ILiveGift> systemGifts = iLiveGiftMng.selectIliveGiftByUserId(0l,0);
		map.addAttribute("systemGifts", systemGifts);
		ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
		map.addAttribute("iLiveLiveRoom", iliveRoom);
		Integer isSystemGift = iliveRoom.getIsSystemGift();
		map.addAttribute("isSystemGift", isSystemGift==null ? 0 : isSystemGift);
		Integer isUserGift = iliveRoom.getIsUserGift();
		map.addAttribute("isUserGift", isUserGift==null ? 0 : isUserGift);
		ILiveEvent liveEvent = iliveRoom.getLiveEvent();
		map.addAttribute("ILiveEvent", liveEvent);
		map.addAttribute("topActive", "1");
		map.addAttribute("leftActive", "4_4");
		String apiRouterUrl = "";
		List<ILiveAPIGateWayRouter> routerList = iLiveAPIGateWayRouterMng.getRouterList();
		if (routerList != null && !routerList.isEmpty()) {
			ILiveAPIGateWayRouter iLiveAPIGateWayRouter = routerList.get(0);
			apiRouterUrl = iLiveAPIGateWayRouter.getRouterUrl();
		}
		map.addAttribute("apiRouterUrl", apiRouterUrl);
		return "gift/gift_list";
	}
	
	@Autowired
	private ILiveUserGiftMng iLiveUserGiftMng;
	/**
	 * 收取礼物列表
	 * @param userContent
	 * @param roomContent
	 * @param map
	 * @param roomId
	 * @param pageNo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/room/giftUserList.do")
	public String updateRoomUserGift(String userContent,String roomContent,ModelMap map,Integer roomId,Integer pageNo,HttpServletRequest request, HttpServletResponse response){
		ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
		map.addAttribute("iLiveLiveRoom", iliveRoom);
		ILiveEvent liveEvent = iliveRoom.getLiveEvent();
		map.addAttribute("ILiveEvent", liveEvent);
		Pagination pagination = new Pagination(cpn(pageNo), 20, 0);
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
		try {
			pagination = iLiveUserGiftMng.selectILiveUserGiftPage(userContent,roomContent,roomId,cpn(pageNo),20);
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.addAttribute("pagination", pagination);
		map.addAttribute("userContent", userContent);
		map.addAttribute("roomContent", roomContent);
		map.addAttribute("topActive", "1");
		map.addAttribute("leftActive", "4_4");
		return "gift/gift_user";
	}
	
	@RequestMapping(value="excelexport.do")
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
			List<ILiveUserGift>  iLiveUserGifts = iLiveUserGiftMng.selectILiveUserGiftExcel(userContent,roomContent,roomId);
			String[] title = {"序号","直播场次ID","直播标题","用户ID","用户名","赠送时间","赠送礼物","道具价格","道具数量"};
			List<String[]> excelBody = new ArrayList<String[]>();
			for (ILiveUserGift iLiveUserGift : iLiveUserGifts) {
				String id = String.valueOf(iLiveUserGift.getId()==null?0:iLiveUserGift.getId());
				String evenId = String.valueOf(iLiveUserGift.getEvenId()==null?0:iLiveUserGift.getEvenId());
				String roomTitle = iLiveUserGift.getRoomTitle()==null?"":iLiveUserGift.getRoomTitle();
				String userId = String.valueOf(iLiveUserGift.getUserId()==null?0:iLiveUserGift.getUserId());
				String userName = iLiveUserGift.getUserName()==null?"":iLiveUserGift.getUserName();
				String time = format.format(iLiveUserGift.getGiveTime()==null?new Date():iLiveUserGift.getGiveTime());
				String giftName = iLiveUserGift.getGiftName()==null?"":iLiveUserGift.getGiftName();
				String giftPrice = iLiveUserGift.getGiftPrice()==null?"0.00":iLiveUserGift.getGiftPrice();
				String giftNumber = String.valueOf(iLiveUserGift.getGiftNumber()==null?0:iLiveUserGift.getGiftNumber());
				String[] str = {id,evenId,roomTitle,userId,userName,time,giftName,giftPrice,giftNumber};
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
