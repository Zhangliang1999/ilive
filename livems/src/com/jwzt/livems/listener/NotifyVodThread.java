package com.jwzt.livems.listener;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.jwzt.DB.cdn.mount.MountInfo;
import com.jwzt.DB.cdn.mount.MountMgr;
import com.jwzt.DB.common.HibernateUtil;
import com.jwzt.common.SomsConfigInfo;
import com.jwzt.filemonitor.FileDirMonitor;
import com.jwzt.soms.web.ums.OpenUmsThread;

/**
 * 监听发布点 线程
 * @author 郭云飞
 *
 */
public class NotifyVodThread extends Thread{
	FileDirMonitor fileDirMonitor = new FileDirMonitor();
	 MountMgr mountMgr = new MountMgr();
	@Override
	public void run() {
		Session session = null;
    	try {
    		while(session==null){
    			 session = HibernateUtil.currentSession();
    			Thread .sleep(3000);
    		}
    		//服务器启动时启动配置ums
    		new OpenUmsThread().start();
			List<String> pathList = new ArrayList<String>();
			List<MountInfo> mountList= mountMgr.getMountList(Integer.parseInt(SomsConfigInfo.get("vodServerGroupId")));
			for (MountInfo mountInfo : mountList) {
				String path = mountInfo.getBase_path();
				//if(mountInfo.getIscut()==1){
					pathList.add(path);
				//}
			}
			fileDirMonitor.tnotify(pathList);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
