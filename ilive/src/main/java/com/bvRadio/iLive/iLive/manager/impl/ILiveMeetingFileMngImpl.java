package com.bvRadio.iLive.iLive.manager.impl;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveMeetingFileDao;
import com.bvRadio.iLive.iLive.entity.IliveMeetingFile;
import com.bvRadio.iLive.iLive.manager.ILiveMeetingFileMng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ILiveMeetingFileMngImpl implements ILiveMeetingFileMng {

    @Autowired
    private ILiveMeetingFileDao iLiveMeetingFileDao;


    @Override
    public Serializable saveILiveMeetingFileMng(IliveMeetingFile iliveMeetingFile) {
        return iLiveMeetingFileDao.saveMeetingFile(iliveMeetingFile);
    }

    @Override
    public Pagination selectMeetingFileListByPage(Map<String,Object> params){
        return iLiveMeetingFileDao.selectMeetingFileListByPage(params);
    }

    @Override
    public IliveMeetingFile selectMeetingFileListById(Long fileId) throws Exception {
        return iLiveMeetingFileDao.selectIliveMeetingFileByFileId(fileId);
    }

    @Override
    public void updateMeetingFile(IliveMeetingFile file) {
        iLiveMeetingFileDao.updateMeetingMediaFile(file);
    }

    @Override
    public Integer batchDeleteMeetingFiles(String fileIds) {
        return iLiveMeetingFileDao.batchDeleteMeetingFiles(fileIds);
    }

    @Override
    public void deleteMeetingAllFiles(Integer roomId) {
        iLiveMeetingFileDao.deleteMeetingAllFiles(roomId);
    }

    @Override
    public void deleteMeetingAllFilesByType(Integer roomId, Integer fileType) {
        iLiveMeetingFileDao.deleteMeetingAllFilesByType(roomId,fileType);
    }
}
