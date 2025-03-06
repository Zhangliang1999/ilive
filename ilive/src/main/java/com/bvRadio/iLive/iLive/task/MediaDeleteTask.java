package com.bvRadio.iLive.iLive.task;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.util.ApplicationContextUtil;

/**
 * 文件存储定时器
 * 
 * @author Administrator
 *
 */
public class MediaDeleteTask extends TimerTask {
	ExecutorService newFixedThreadPool = null;
	public MediaDeleteTask() {
		newFixedThreadPool = Executors.newFixedThreadPool(10);
	}
	@Override
	public void run() {
		try {
			ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
			ILiveMediaFileMng iLiveMediaFileMng = (ILiveMediaFileMng) applicationContext.getBean("iLiveMediaFileMng");
			List<ILiveMediaFile> list = iLiveMediaFileMng.selectILiveMediaFileListByIsMediaType(ILiveMediaFile.MEDIA_TYPE_Temporary,0);//删除标记:1表示yes已经删除，0或者空表示no未删除
			if(list.size()>0){
				runnableMedia(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static final Logger log = LoggerFactory.getLogger(MediaDeleteTask.class);
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private void runnableMedia(final List<ILiveMediaFile> list) {
		log.info("开始执行删除视频线程！");
		newFixedThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
					ILiveMediaFileMng iLiveMediaFileMng = (ILiveMediaFileMng) applicationContext.getBean("iLiveMediaFileMng");
					Date date = new Date();
					for (ILiveMediaFile iLiveMediaFile : list) {
						Timestamp temporaryTime = iLiveMediaFile.getTemporaryTime();
						if(temporaryTime==null){
							//获取视频创建时间
							Timestamp creatTime=iLiveMediaFile.getMediaCreateTime();
							if(creatTime==null){
								temporaryTime=new Timestamp(System.currentTimeMillis()+(1000*60*60*24));
							}else{
								Long validTime=(long) (1000*60*60*24*7);
								temporaryTime=new Timestamp(creatTime.getTime()+validTime);
							}
						}
						log.info("获取视频的有效截至时间"+temporaryTime);
						if(date.getTime()>temporaryTime.getTime()){
							log.info("获取到需要删除的视频id："+iLiveMediaFile.getFileId()+";开始执行删除操作！");
							iLiveMediaFileMng.deleteVedio(iLiveMediaFile.getFileId());
						}
					}
				} catch (Exception e) {
					log.error("文件处理异常 ："+e.toString());
				}
			}
		});
	}
}
