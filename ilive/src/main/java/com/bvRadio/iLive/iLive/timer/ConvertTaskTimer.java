package com.bvRadio.iLive.iLive.timer;

import java.util.List;
import java.util.TimerTask;

import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.IliveMeetingFile;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bvRadio.iLive.iLive.entity.ILiveConvertTask;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.manager.ILiveConvertTaskMng;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;

/***
 * 当转码完成是自动处理转码任务，将数据库内的文件进行替换
 * @author zpn
 *
 */
@Component
public class ConvertTaskTimer extends TimerTask{
	

	private ILiveConvertTaskMng iLiveConvertTaskMng;

	private ILiveMediaFileMng iLiveMediaFileMng;

	private ILiveServerAccessMethodMng iLiveServerAccessMethodMng;
	
	public ConvertTaskTimer(ILiveConvertTaskMng iLiveConvertTaskMng , ILiveMediaFileMng iLiveMediaFileMng, ILiveServerAccessMethodMng iLiveServerAccessMethodMng){
		this.iLiveConvertTaskMng = iLiveConvertTaskMng;
		this.iLiveMediaFileMng = iLiveMediaFileMng;
		this.iLiveServerAccessMethodMng = iLiveServerAccessMethodMng;
	}
	
	public void run()
    {
		try{
			String defaultVodServerGroupId = ConfigUtils.get("defaultVodServerGroupId");
			ILiveServerAccessMethod accessMethod = iLiveServerAccessMethodMng
					.getAccessMethodBySeverGroupId(Integer.parseInt(defaultVodServerGroupId));
			List<ILiveConvertTask> list = iLiveConvertTaskMng.getConvertTaskByState(3);
			
			for(ILiveConvertTask task : list){
				Long fileId = task.getFileId();
				if(task.getFileSourceTable() != null && task.getFileSourceTable().intValue() == 1){
					IliveMeetingFile iliveMeetingFile = iLiveMediaFileMng.selectIliveMeetingFileByFileId(fileId);
					if(iliveMeetingFile != null){
						iliveMeetingFile.setFilePath(task.getdPath());
						String httpUrl = "http://" + accessMethod.getHttp_address() + ":" + accessMethod.getUmsport()
								+ accessMethod.getMountInfo().getBase_path() + task.getdPath();
						iliveMeetingFile.setFileUrl(httpUrl);
						iLiveMediaFileMng.updateMeetingMediaFile(iliveMeetingFile);
						task.setState(4);
						iLiveConvertTaskMng.updateConvertTask(task);
					}
				}else{
					ILiveMediaFile fileInfo = iLiveMediaFileMng.selectILiveMediaFileByFileId(fileId);
					fileInfo.setFilePath(task.getdPath());
					iLiveMediaFileMng.updateMediaFile(fileInfo);
					task.setState(4);
					iLiveConvertTaskMng.updateConvertTask(task);
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}
				
				
				
    }
	
	
}
