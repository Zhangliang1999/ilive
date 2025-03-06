package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.IliveMeetingFile;
import java.io.Serializable;
import java.util.Map;

/**
 * 会议文件管理接口
 */
public interface ILiveMeetingFileMng {

    /**
     * 保存文件
     */
    Serializable saveILiveMeetingFileMng(IliveMeetingFile iliveMeetingFile);

    /**
     * 分页列表
     * @param params
     * @return
     */
    Pagination selectMeetingFileListByPage(Map<String,Object> params)throws Exception;

    /**
     * 主键查询实体对象
     * @param fileId
     * @return
     */
    IliveMeetingFile selectMeetingFileListById(Long fileId) throws Exception;

    /**
     * 更新实体对象
     * @param file
     */
    void updateMeetingFile(IliveMeetingFile file);

    /**
     * 批量删除会议文件
     * @param fileIds
     */
    Integer batchDeleteMeetingFiles(String fileIds);

    /**
     * 删除会议室所有文件
     * @param roomId
     */
    void deleteMeetingAllFiles(Integer roomId);

    /**
     * 按文件类型删除会议室文件
     * @param roomId
     * @param fileType
     */
    void deleteMeetingAllFilesByType(Integer roomId, Integer fileType);
}
