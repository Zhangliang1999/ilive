package com.bvRadio.iLive.iLive.manager;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.action.front.vo.AppMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;

/**
 * 媒资文件管理接口
 * 
 * @author administrator
 */
public interface ILiveMediaFileMng {

	/**
	 * 保存文件
	 * 
	 * @param iLiveMediaFile
	 * @return
	 */
	public boolean saveIliveMediaFile(ILiveMediaFile iLiveMediaFile);

	/**
	 * 删除单个视频信息
	 * 
	 * @param id
	 *            视频文件id
	 */
	public void deleteVedio(Long id);

	/**
	 * 删除多个视频信息
	 * 
	 * @param ids
	 *            视频文件id的数组
	 */
	public void deleteVediosByIds(Long[] ids);

	/**
	 * 更新单个视频上线状态
	 * 
	 * @param id
	 *            视频文件id
	 * @param state
	 *            上线状态 :1表示上线，0表示下线
	 */
	public void updateVedioLineState(Long id, Integer state);

	/**
	 * 更新多个视频上线状态
	 * 
	 * @param ids
	 *            视频文件id的数组
	 * @param state
	 *            上线状态 :1表示上线，0表示下线
	 */
	public void updateVediosLineStateByIds(Long[] ids, Integer state);

	/**
	 * 根据 场次ID
	 * 
	 * @param liveEventId
	 *            直播间ID
	 * @param pageNO
	 *            页码
	 * @param pageSize
	 *            每页数据条数
	 * @param delFlag
	 *            是否删除
	 * @param fileType
	 *            文件类型
	 * @return
	 */

	public Pagination selectILiveMediaFilePage(Long liveEventId, Integer pageNO, Integer pageSize, Integer delFlag,
			Integer fileType);

	/**
	 * 获取场次录制文件集
	 * 
	 * @param liveEventId
	 *            场次ID
	 * @return
	 */
	public List<ILiveMediaFile> selectILiveMediaFileListByEventId(Long liveEventId);

	/**
	 * 根据主键ID获取数据
	 * 
	 * @param fileId
	 *            主键
	 * @return
	 */
	public ILiveMediaFile selectILiveMediaFileByFileId(Long fileId);

	/**
	 * 修改对象
	 * 
	 * @param file
	 */
	public void updateEventFileOnlineFlag(ILiveMediaFile file);

	/**
	 * 根据直播间ID获取数据
	 * 
	 * @param roomId
	 *            直播间ID
	 * @param pageNO
	 *            页码
	 * @param pageSize
	 *            每页数据条数
	 * @param delFlag
	 *            是否删除
	 * @param fileType
	 *            文件类型
	 * @return
	 */
	public Pagination selectILiveMediaFilePageByRoomId(Integer roomId, Integer pageNO, Integer pageSize,
			Integer delFlag, Integer fileType);

	/**
	 * 查询文件分页
	 * 
	 * @param sqlParam
	 *            查询参数 （delFlag 删除标记参数， userId 用户id， mediaFileName 文件名称参数 ，
	 *            fileType file_type参数， createType create_type参数）
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            条数
	 * @return
	 */
	public Pagination getMediaFilePage(Map<String, Object> sqlParam, Integer pageNo, Integer pageSize);

	/**
	 * 查询所有的媒体文件的大小
	 * 
	 * @param userId
	 *            用户Id
	 * @param mediaName
	 *            文件资源名
	 * @return
	 */
	public String selectLiveRecordTotalSize(Long userId, String mediaName);

	/**
	 * 修改媒体文件信息
	 * 
	 * @param sqlParam
	 */
	public void updateMediaFileById(ILiveMediaFile sqlParam);

	/**
	 * 获取所有列表
	 * 
	 * @param type
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination getPager(Integer type, Integer pageNo, Integer pageSize);
	
	public Pagination getPager(Integer createType,Integer mediaState,Timestamp startTime,Timestamp endTime,String query,Integer type, Integer pageNo, Integer pageSize);

	// 根据文件类型获取列表
	public List<ILiveMediaFile> getListByType(Integer fileType, Integer enterpriseId);

	/**
	 * 获取媒体文件信息
	 * 
	 * @param sqlParam
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<AppMediaFile> getMediaFilePageByRoom(Map<String, Object> sqlParam, Integer pageNo, Integer pageSize);

	int getNum(Integer type,Integer state);
}
