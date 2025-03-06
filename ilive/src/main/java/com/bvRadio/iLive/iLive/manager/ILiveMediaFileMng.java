package com.bvRadio.iLive.iLive.manager;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.action.front.vo.AppMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.IliveMeetingFile;

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
	public Long saveIliveMediaFile(ILiveMediaFile iLiveMediaFile);

	/**
	 * 删除单个视频信息
	 * 
	 * @param id
	 *            视频文件id
	 */
	public ILiveMediaFile deleteVedio(Long id);

	/**
	 * 删除多个视频信息
	 * 
	 * @param ids
	 *            视频文件id的数组
	 */
	public ILiveMediaFile[] deleteVediosByIds(Long[] ids);

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
	 * 根据主键ID获取数据
	 *
	 * @param fileId
	 *            主键
	 * @return
	 */
	public IliveMeetingFile selectIliveMeetingFileByFileId(Long fileId);

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

	// 根据文件类型获取列表
	public List<ILiveMediaFile> getListByType(Integer fileType, Integer enterpriseId);
	
	List<ILiveMediaFile> getListByTypeAndName(String name,Integer fileType, Integer enterpriseId);

	// 根据文件类型获取列表和数量
	public List<ILiveMediaFile> getListByTypeAndSize(Integer fileType, Integer enterpriseId, Integer num);

	/**
	 * 获取媒体文件信息
	 * 
	 * @param sqlParam
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<AppMediaFile> getMediaFilePageByRoom(Map<String, Object> sqlParam, Integer pageNo, Integer pageSize);

	public List<AppMediaFile> getTop4ForView(String keyWord);

	public List<AppMediaFile> getPagerForView(String keyWord, Integer pageNo, Integer pageSize, Integer searchType);

	/**
	 * 获取个人所直播的列表详情
	 * 
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<AppMediaFile> getPersonalFileList(Long userId, Integer pageNo, Integer pageSize,HttpServletRequest request);

	/**
	 * 获取未web端展示的页面数据
	 * 
	 * @param fileId
	 * @return
	 */
	public ILiveMediaFile selectILiveMediaForWeb(Long fileId);

	public void updateMediaFile(ILiveMediaFile iLiveMediaFile);

	public void updateMeetingMediaFile(IliveMeetingFile iliveMeetingFile);

	public Pagination getAppMediaFile(Map<String, Object> sqlParam, Integer pageNo, Integer pageSize);

	/**
	 * 
	 * @param startId
	 * @param size
	 * @return
	 */
	public List<AppMediaFile> getBatchVodListForStatics(Long startId, Integer size);

	public List<AppMediaFile> getAppMediaFileForStatics(Long[] ids);
	
	/**
	 * 将ILiveMediaFile类型转换为AppMediaFile类型
	 * @param mediaFile
	 * @return
	 */
	public AppMediaFile convertILiveMediaFile2AppMediaFile(ILiveMediaFile mediaFile);
	
	/**
	 * 子用户媒体列表
	 * @param sqlParam
	 * @param pageNo
	 * @param pageSize
	 * @param userId
	 * @param roomId
	 * @return
	 */
	public Pagination getCollaborativeMediaFilePage(
			Map<String, Object> sqlParam, Integer pageNo, Integer pageSize,
			Long userId, Integer roomId);
	/**
	 * 根据企业ID 获取企业已使用内存文件大小
	 * @param enterpriseId
	 * @param fileType  文件类型 1视频 2文档 3图片
	 * @return
	 */
	public List<ILiveMediaFile> selectILiveMediaFileListByEnterprieId(Integer enterpriseId,Integer fileType);
	/**
	 * 获取暂存文件
	 * @param mediaTypeTemporary 暂存类型
	 * @param delFlag 删除标示
	 * @return
	 */
	public List<ILiveMediaFile> selectILiveMediaFileListByIsMediaType(Integer mediaTypeTemporary, Integer delFlag);

	public List<AppMediaFile> getPagerForView(String keyWord, Integer pageNo, Integer pageSize, Integer searchType,
			boolean per, Integer enterpriseId, Long userId, Integer level);

	Pagination getMediaFilePage1(Map<String,Object> sqlParam, Integer pageNo, Integer pageSize);

    List<ILiveMediaFile> selectILiveMediaFileBycloudDiskFileIds(String[] cloudDiskFileId);
}
