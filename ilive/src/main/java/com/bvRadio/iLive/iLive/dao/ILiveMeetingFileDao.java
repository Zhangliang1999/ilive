package com.bvRadio.iLive.iLive.dao;


import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.IliveMeetingFile;
import java.io.Serializable;
import java.util.Map;

public interface ILiveMeetingFileDao {

	/**
	 * 根据主键ID获取数据
	 * @param fileId 主键
	 * @return
	 * @throws Exception
	 */
	IliveMeetingFile selectIliveMeetingFileByFileId(Long fileId) throws Exception;

	/**
	 * 修改会议文件信息
	 * @param iliveMeetingFile
	 */
	void updateMeetingMediaFile(IliveMeetingFile iliveMeetingFile);

	/**
	 * 保存文件
	 */
	Serializable saveMeetingFile(IliveMeetingFile iliveMeetingFile);

	/**
	 * 分页列表
	 * @param params
	 * @return
	 */
	Pagination selectMeetingFileListByPage(Map<String,Object> params);

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
