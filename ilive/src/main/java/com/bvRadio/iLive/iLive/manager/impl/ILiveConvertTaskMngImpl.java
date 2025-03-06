package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.*;
import com.bvRadio.iLive.iLive.entity.base.BaseILiveMediaFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveConvertTaskDao;
import com.bvRadio.iLive.iLive.dao.ILiveEnterpriseConvertDao;
import com.bvRadio.iLive.iLive.manager.ILiveConvertTaskMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.web.ConfigUtils;


@Service
@Transactional
public class ILiveConvertTaskMngImpl implements ILiveConvertTaskMng {

	@Autowired
	private ILiveConvertTaskDao iLiveConvertTaskDao;
	@Autowired
	private ILiveFieldIdManagerMng fieldIdManagerMng;
	@Autowired
	private ILiveEnterpriseConvertDao iLiveEnterpriseConvertDao;
	
	@Override
	public boolean addConvertTask(ILiveConvertTask iLiveConvertTask){
		
		Long taskId = fieldIdManagerMng.getNextId("ilive_convert_task", "task_id", 1);
		
		iLiveConvertTask.setTaskId(taskId);
		
		iLiveConvertTaskDao.saveConvertTask(iLiveConvertTask);
		
		return true;
		
	}
	
	@Override
	public void updateConvertTask(ILiveConvertTask iLiveConvertTask){
		iLiveConvertTaskDao.updateConvertTask(iLiveConvertTask);
	}
	
	@Override
	public List<ILiveConvertTask> getConvertTaskByState(Integer state){
		
		return iLiveConvertTaskDao.getConvertTaskByState(state);
		
	}
	
	@Override
	public ILiveConvertTask getConvertTask(Integer taskId){
		
		return iLiveConvertTaskDao.getConvertTask(taskId);
		
	}
	
	@Override
	public ILiveConvertTask createConvertTask(BaseILiveMediaFile selectFile , ILiveServerAccessMethod accessMethod ){
		
		ILiveConvertTask iLiveConvertTask = null;
		
		try{
			String convertServer = ConfigUtils.get(ConfigUtils.CONVERT_SERVER);
			String convertTemplet = ConfigUtils.get(ConfigUtils.CONVERT_TEMPLET);
			
			ILiveEnterpriseConvert ILiveEnterpriseConvert = iLiveEnterpriseConvertDao.getConvertTemplet(selectFile.getEnterpriseId());
			
			if(ILiveEnterpriseConvert != null){
				convertTemplet = ILiveEnterpriseConvert.getTempletId().toString();
			}
			
			
			String sPath = selectFile.getFilePath();
			if(sPath.startsWith("/"))sPath = sPath.substring(1);
			String dPath = sPath.substring(0,sPath.indexOf("."))+"_"+convertTemplet+".mp4";
			String sFtpUrl = "ftp://"+accessMethod.getFtp_user()+":"+accessMethod.getFtp_pwd()+"@"+accessMethod.getFtp_address_inner()+":"+accessMethod.getFtpPort()+accessMethod.getMountInfo().getFtp_path();
			
			iLiveConvertTask = new ILiveConvertTask();
			iLiveConvertTask.setCallBackId(-1);
			iLiveConvertTask.setConvertServerId(Integer.parseInt(convertServer));
			iLiveConvertTask.setsPath("/"+sPath);
			iLiveConvertTask.setdPath("/"+dPath);
			iLiveConvertTask.setFileId(selectFile.getFileId());
			iLiveConvertTask.setState(0);
			iLiveConvertTask.setTempletId(Integer.parseInt(convertTemplet));
			iLiveConvertTask.setsFtpPath(sFtpUrl+sPath);
			iLiveConvertTask.setdFtpPath(sFtpUrl+dPath);
			if(selectFile instanceof IliveMeetingFile){
				iLiveConvertTask.setFileSourceTable(1);
			}else if(selectFile instanceof ILiveMediaFile){
				iLiveConvertTask.setFileSourceTable(0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return iLiveConvertTask;
		
	}


	
}
