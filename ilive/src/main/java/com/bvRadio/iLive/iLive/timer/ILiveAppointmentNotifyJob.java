package com.bvRadio.iLive.iLive.timer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.manager.ILiveAppointmentMng;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.service.PushJson;
import com.bvRadio.iLive.iLive.util.ApplicationContextUtil;
import com.bvRadio.iLive.sms.ILiveSmgServerCenter;

/**
 * 定时获取10分钟内开始的直播并发短信通知预约人
 * 
 * @author Wei
 *
 */
@Component
public class ILiveAppointmentNotifyJob {

	@Autowired
	private ILiveEventMng iLiveEventMng;
	@Autowired
	private ILiveAppointmentMng iLiveAppointmentMng;
	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;
	
	@Autowired
	private ThreadPoolTaskExecutor execute;

	private static final Logger LOGGER = LoggerFactory.getLogger(ILiveAppointmentNotifyJob.class);
	
	public void execute() {
		try {
			List<ILiveEvent> list = iLiveEventMng.getLiveEventByStartTime(10);
			HashMap<String, Object> sendParam = new HashMap<>();
			sendParam.put("biztype", 246634);
			if (!list.isEmpty()) {
				Iterator<ILiveEvent> iterator = list.iterator();
				
				// 遍历所有十分钟内开始的场次
				while (iterator.hasNext()) {
					ILiveEvent iLiveEvent = (ILiveEvent) iterator.next();
					// 获取直播间信息
					ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.findByEventId(iLiveEvent.getLiveEventId());
					if (iliveRoom != null) {
						String liveName = iLiveEvent.getLiveTitle();
						SimpleDateFormat format = new SimpleDateFormat("HH:mm");
						String startTime = format.format(iLiveEvent.getLiveStartTime());
						ILiveServerAccessMethod accessMethodBySeverGroupId = accessMethodMng
								.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
						String liveAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone" + "/live.html?roomId="
								+ iliveRoom.getRoomId();
						List<String> userList = iLiveAppointmentMng.getUserListByEventId(iLiveEvent.getLiveEventId());
						if (userList==null) {
							LOGGER.info("没有人预约这个场次");
						}else {
							for (final String userId : userList) {
								ILiveManager user = iLiveManagerMng.getILiveManager(Long.parseLong(userId));
								if (user != null && user.getMobile() != null) {
									LOGGER.info("有人预约");
									List<Object> paramslist=new ArrayList<Object>();
									final String string = "您预约的直播:"+liveName+",将于:"+startTime+"开始,观看地址"+liveAddr;
									paramslist.add(string);
									sendParam.put("params",paramslist);
									List<String> phonelist=new ArrayList<String>();
									phonelist.add(user.getMobile());
									ILiveSmgServerCenter.sendMsgNew(phonelist, sendParam);
									LOGGER.info("发送成功,接收者："+user.getMobile());
									final Integer roomId = iliveRoom.getRoomId();
									execute.execute(new Runnable() {
										@Override
										public void run() {
											PushJson.pushOne(Long.parseLong(userId), string,1,roomId);
										}
									});
								}
							}
							// 设置场次为已发送短信 isNotify设为1
							iLiveEvent.setIsNotify(1);
							iLiveEventMng.updateILiveEvent(iLiveEvent);
							
						}
					}else {
						LOGGER.info("没有找到场次对应的直播间");
					}
				}
			}else {
				LOGGER.info("没有找到将要开始的直播间");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("执行发送短信操作失败");
		}
	}
	
	
	
	
}
