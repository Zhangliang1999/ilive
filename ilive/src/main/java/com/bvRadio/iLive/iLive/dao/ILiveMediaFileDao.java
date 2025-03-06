package com.bvRadio.iLive.iLive.dao;


import java.util.List;
import java.util.Map;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;

public interface ILiveMediaFileDao {
	
	/**
	 * 保存媒资文件
	 * @param iLiveMediaFile
	 * @return
	 */
	public boolean saveILiveMediaFile(ILiveMediaFile iLiveMediaFile);
	/**
	 * 根据 场次ID
	 * @param liveEventId 直播间ID
	 * @param pageNO 页码
	 * @param pageSize 每页数据条数
	 * @param delFlag  是否删除
	 * @param fileType 文件类型
	 * @return
	 */
	public Pagination selectILiveMediaFilePage(Long liveEventId,
			Integer pageNO, Integer pageSize,Integer delFlag,Integer fileType) throws Exception;
	/**
	 * 获取场次录制文件集
	 * @param liveEventId 场次ID
	 * @return
	 */
	public List<ILiveMediaFile> selectILiveMediaFileListByEventId(
			Long liveEventId) throws Exception;
	/**
	 * 根据主键ID获取数据
	 * @param fileId 主键
	 * @return
	 * @throws Exception
	 */
	public ILiveMediaFile selectILiveMediaFileByFileId(Long fileId) throws Exception;
		
	/**
	 * 删除单个媒体文件（把delflag更新为1，并不是实际删除）
	 * @param id fileId
	 */
	public ILiveMediaFile deleteLiveMediaFile(Long id);
	/**
	 * 根据对象修改数据
	 * @param file
	 * @throws Exception
	 */
	public void updateEventFileOnlineFlag(ILiveMediaFile file) throws Exception;
	/**
	 * 根据直播间ID获取数据
	 * @param roomId 直播间ID
	 * @param pageNO 页码
	 * @param pageSize 每页数据条数
	 * @param delFlag  是否删除
	 * @param fileType 文件类型
	 * @return
	 */
	public Pagination selectILiveMediaFilePageByRoomId(Integer roomId,
			Integer pageNO, Integer pageSize, Integer delFlag, Integer fileType) throws Exception;
	
	/**
	 * 更新视频文件的上线状态
	 * @param id 媒体文件的fileId
	 * @param state 要变成的状态，1代表online上线、0代表offline下线
	 */
	public void updateLiveMediaFileLineState(Long id,Integer state);
	
	/**
	 * 更新视频文件的上线状态
	 * @param ids 多个媒体文件的fileId的数组
	 * @param state 要变成的状态，1代表online上线、0代表offline下线
	 */
	public void updateLiveMediaFilesLineStateByIds(Long[] ids,Integer state);
	
	/**
	 * 查询文件分页
	 * @param sqlParam 查询参数 （delFlag 删除标记参数，  userId 用户id，  
	 * mediaFileName 文件名称参数 ， fileType ，file_type参数， createType create_type参数）
	 * @param pageNo 页码
	 * @param pageSize 条数
	 * @return
	 */
	public Pagination getMedialFilePage(Map<String, Object> sqlParam, Integer pageNo, Integer pageSize);
	
	/**
	 *  查询所有的媒体文件的大小
	 * @param userId 用户Id
	 * @param mediaName	文件资源名
	 * @return
	 */
	public Long selectLiveRecordTotalSize(Long userId, String mediaName);
	
	
	public void updateMediaFileById(ILiveMediaFile sqlParam);
	
	/**
	 * 获取所有列表
	 * @param type
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination getPager(Integer type, Integer pageNo, Integer pageSize);
	
	//根据文档类型获取列表
	public List<ILiveMediaFile> getListByType(Integer fileType,Integer enterpriseId);
	
	//根据文档类型获取列表和数量
	public List<ILiveMediaFile> getListByTypeAndSize(Integer fileType,Integer enterpriseId,Integer num);
	
	/**
	 * 根据直播间获取关联的回看文件
	 * @param sqlParam
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination getMediaFilePageByRoom(Map<String, Object> sqlParam, Integer pageNo, Integer pageSize);
	
	/**
	 * 为观看端提供的文件地址
	 * @param sqlParam
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination getMediaFilePageByRoomForView(Map<String, Object> sqlParam, Integer pageNo, Integer pageSize);
	
	/**
	 * 搜索top4
	 * @param keyword
	 * @return
	 */
	public List<ILiveMediaFile> getTop4ForView(String keyword);
	
	
	/**
	 * 回看文件
	 * @param keyWord
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<ILiveMediaFile> getPagerForView(String keyWord, Integer pageNo, Integer pageSize);
	
	/**
	 * 获取个人直播分页列表
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination getPersonalFileList(Long userId, Integer pageNo, Integer pageSize);
	
	/**
	 * 修改文件信息
	 * @param iLiveMediaFile
	 */
	public void updateMediaFile(ILiveMediaFile iLiveMediaFile);
	
	
	/**
	 * 根据企业获取资源
	 * @param enterpriseId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination getAppMediaFileByEnterprise(String search,Integer enterpriseId, Integer pageNo, Integer pageSize,String userId,String roomIds);
	
	
	/**
	 * 回看的
	 * @param startId
	 * @param size
	 * @return
	 */
	public List<ILiveMediaFile> getBatchVodListForStatics(Long startId, Integer size);
	
	/**
	 * 根据文件带下获取文件
	 * @param ids
	 * @return
	 */
	public List<ILiveMediaFile> getListByIds(Long[] ids);
	/**
	 * 子用户 媒体列表
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
	 * 根据企业ID 获取企业文件
	 * @param enterpriseId
	 * @param fileType 文件类型 1视频 2文档 3图片
	 * @return
	 */
	public List<ILiveMediaFile> selectILiveMediaFileListByEnterprieId(
			Integer enterpriseId, Integer fileType);
	/**
	 * 获取暂存文件
	 * @param mediaTypeTemporary 暂存类型
	 * @param delFlag 删除标示
	 * @return
	 */
	public List<ILiveMediaFile> selectILiveMediaFileListByIsMediaType(
			Integer mediaTypeTemporary, Integer delFlag);

	List<ILiveMediaFile> getListByTypeAndName(String name, Integer fileType, Integer enterpriseId);
	public Pagination getPersonalFileList(Long userId, int pageNo, int pageSize, String keyWord);

	Pagination getMedialFilePage1(Map<String,Object> sqlParam, Integer pageNo, Integer pageSize);

    List<ILiveMediaFile> selectILiveMediaFileBycloudDiskFileIds(String[] cloudDiskFileId);
}
