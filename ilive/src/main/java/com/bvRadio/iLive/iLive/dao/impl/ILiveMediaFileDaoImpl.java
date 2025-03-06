package com.bvRadio.iLive.iLive.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveMediaFileDao;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;

@Repository
public class ILiveMediaFileDaoImpl extends HibernateBaseDao<ILiveMediaFile, Long> implements ILiveMediaFileDao {

	@Override
	protected Class<ILiveMediaFile> getEntityClass() {
		return ILiveMediaFile.class;
	}

	@Override
	public boolean saveILiveMediaFile(ILiveMediaFile iLiveMediaFile) {
		this.getSession().save(iLiveMediaFile);
		return true;
	}

	@Override
	public ILiveMediaFile deleteLiveMediaFile(Long id) {
		ILiveMediaFile iLiveMediaFile = get(id);
		if(null!=iLiveMediaFile) {
			iLiveMediaFile.setDelFlag(1);
			getSession().update(iLiveMediaFile);
			getSession().flush();
		}
		return iLiveMediaFile;
	}

	@Override
	public Pagination selectILiveMediaFilePage(Long liveEventId, Integer pageNO, Integer pageSize, Integer delFlag,
			Integer fileType) throws Exception {
		Finder finder = Finder.create("select bean from ILiveMediaFile bean ");
		finder.append(" where 1=1 ");
		if (liveEventId != null) {
			finder.append(" and bean.liveEventId=:liveEventId");
			finder.setParam("liveEventId", liveEventId);
		}
		finder.append(" and bean.delFlag=:delFlag ");
		finder.setParam("delFlag", delFlag);
		finder.append(" and bean.fileType=:fileType ");
		finder.setParam("fileType", fileType);
		finder.append(" order by bean.fileId desc ");
		Pagination find = find(finder, pageNO, pageSize);
		return find;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveMediaFile> selectILiveMediaFileListByEventId(Long liveEventId) throws Exception {
		Finder finder = Finder
				.create("select bean from ILiveMediaFile bean where bean.liveEventId=:liveEventId and delFlag=0 ");
		finder.setParam("liveEventId", liveEventId);
		finder.append(" order by bean.fileId desc ");
		List<ILiveMediaFile> find = find(finder);
		return find;
	}

	@Override
	public ILiveMediaFile selectILiveMediaFileByFileId(Long fileId) throws Exception {
		ILiveMediaFile iLiveMediaFile = get(fileId);
		return iLiveMediaFile;
	}

	@Override
	public void updateEventFileOnlineFlag(ILiveMediaFile file) throws Exception {
		getSession().update(file);
	}

	@Override
	public Pagination selectILiveMediaFilePageByRoomId(Integer roomId, Integer pageNO, Integer pageSize,
			Integer delFlag, Integer fileType) throws Exception {
		Finder finder = Finder.create("select bean from ILiveMediaFile bean ");
		finder.append(" where 1=1 ");
		if (roomId != null) {
			finder.append(" and bean.liveRoomId=:liveRoomId");
			finder.setParam("liveRoomId", roomId);
		}
		finder.append(" and bean.delFlag=:delFlag ");
		finder.setParam("delFlag", delFlag);
		finder.append(" and bean.fileType=:fileType ");
		finder.setParam("fileType", fileType);
		finder.append(" order by bean.fileId desc ");
		Pagination find = find(finder, pageNO, pageSize);
		return find;
	}

	@Override
	public void updateLiveMediaFileLineState(Long id, Integer state) {
		ILiveMediaFile iLiveMediaFile = get(id);
		if (!state.equals(iLiveMediaFile.getOnlineFlag())) {
			iLiveMediaFile.setOnlineFlag(state);
			getSession().update(iLiveMediaFile);
			getSession().flush();
		}
	}

	@Override
	public void updateLiveMediaFilesLineStateByIds(Long[] ids, Integer state) {
		if (ids != null && ids.length > 0) {
			for (Long id : ids) {
				updateLiveMediaFileLineState(id, state);
			}
		}
	}

	@Override
	public Pagination getMedialFilePage(Map<String, Object> sqlParam, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("select liveMediaFile from ILiveMediaFile liveMediaFile where 1=1 ");
		// 删除标记参数
		Integer delFlag = (Integer) sqlParam.get("delFlag");
		if (delFlag != null) {
			finder.append(" and liveMediaFile.delFlag = :delFlag");
			finder.setParam("delFlag", delFlag);
		}
		// 用户参数

		Long fileId=(Long) sqlParam.get("fileId");
		if (fileId != null) {
			finder.append(" and liveMediaFile.fileId != :fileId");
			finder.setParam("fileId", fileId);
		}
		String userId = (String) sqlParam.get("userId");
		String roomIds=(String) sqlParam.get("roomIds");
		if (StringUtils.isNotBlank(userId)) {
			Long userid = Long.parseLong(userId);
			if(roomIds!=null&&!"".equals(roomIds)) {
			finder.append(" and (liveMediaFile.userId = :userId or liveMediaFile.liveRoomId in"+roomIds+")");
			}else {
				finder.append(" and liveMediaFile.userId = :userId ");	
			}
			finder.setParam("userId", userid);
		}

		Long enterpriseId = (Long) sqlParam.get("enterpriseId");
		if (enterpriseId != null) {
			finder.append(" and liveMediaFile.enterpriseId = :enterpriseId");
			finder.setParam("enterpriseId", enterpriseId);
		}

		try {
			String mediaFileName = (String) sqlParam.get("mediaFileName");
			String liveRoomId = (String) sqlParam.get("mediaFileName");
			Integer roomId=Integer.parseInt(liveRoomId);
			if (StringUtils.isNotBlank(mediaFileName)) {
				finder.append(" and (liveMediaFile.liveRoomId = :liveRoomId  or liveMediaFile.mediaFileName like :mediaFileName) ");
				finder.setParam("mediaFileName", "%" + mediaFileName + "%");
				finder.setParam("liveRoomId", roomId);
			}
		} catch (Exception e) {
			String mediaFileName = (String) sqlParam.get("mediaFileName");
			if (StringUtils.isNotBlank(mediaFileName)) {
				finder.append(" and  liveMediaFile.mediaFileName like :mediaFileName ");
				finder.setParam("mediaFileName", "%" + mediaFileName + "%");
			}
		}
			
		
		// file_type参数
		Integer fileType = (Integer) sqlParam.get("fileType");
		if (fileType != null) {
			finder.append(" and liveMediaFile.fileType = :fileType");
			finder.setParam("fileType", fileType);
		}
		// create_type参数
		Integer createType = (Integer) sqlParam.get("createType");
		if (createType != null && createType.intValue() != 5) {
			finder.append(" and liveMediaFile.createType = :createType");
			finder.setParam("createType", createType);
		}

		finder.append(" order by liveMediaFile.mediaCreateTime desc");
		return find(finder, pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long selectLiveRecordTotalSize(Long userId, String mediaName) {
		List<BigDecimal> list = (List<BigDecimal>) getSession().createSQLQuery(
				"SELECT SUM(file_size) FROM ilive_media_file" + " where ( del_flag is null or  del_flag != 1) "
						+ " and create_type = 0" + " and user_id = " + userId
						+ (StringUtils.isNotBlank(mediaName) ? (" and file_name like '%" + mediaName + "%'") : ""))
				.list();
		if (list != null && list.size() > 0 && list.get(0) != null) {
			return list.get(0).longValue();
		}
		return 0l;
	}

	@Override
	public void updateMediaFileById(ILiveMediaFile sqlParam) {
		ILiveMediaFile iLiveMediaFile = get(sqlParam.getFileId());
		if (StringUtils.isNotBlank(sqlParam.getMediaFileName())) {
			iLiveMediaFile.setMediaFileName(sqlParam.getMediaFileName());
		}
		if (StringUtils.isNotBlank(sqlParam.getMediaFileDesc())) {
			iLiveMediaFile.setMediaFileDesc(sqlParam.getMediaFileDesc());
		}
		getSession().update(iLiveMediaFile);
		getSession().flush();
	}

	@Override
	public Pagination getPager(Integer type, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from ILiveMediaFile where fileType = :fileType");
		finder.setParam("fileType", type);
		finder.append(" order by fileId desc ");
		return find(finder, pageNo == null ? 1 : pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveMediaFile> getListByType(Integer fileType, Integer enterpriseId) {
		String hql = "from ILiveMediaFile where delFlag=0 and fileType = :fileType and enterpriseId = :enterpriseId and onlineFlag = :onlineFlag order by fileId desc";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("fileType", fileType);
		query.setParameter("onlineFlag", 1);
		long en = (long) enterpriseId;
		query.setParameter("enterpriseId", en);
		List<ILiveMediaFile> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveMediaFile> getListByTypeAndSize(Integer fileType, Integer enterpriseId, Integer num) {
		String hql = "from ILiveMediaFile where delFlag=0 and fileType = :fileType and enterpriseId = :enterpriseId and onlineFlag = :onlineFlag order by mediaCreateTime desc";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("fileType", fileType);
		query.setParameter("onlineFlag", 1);
		long en = (long) enterpriseId;
		query.setParameter("enterpriseId", en);
		query.setFirstResult(0);
		query.setMaxResults(num);
		List<ILiveMediaFile> list = query.list();
		return list;
	}

	@Override
	public Pagination getMediaFilePageByRoom(Map<String, Object> sqlParam, Integer pageNo, Integer pageSize) {
		String hql = " from ILiveMediaFile where mediaInfoStatus =1 and onlineFlag=1 and delFlag=0   ";
		Finder finder = Finder.create(hql);
		if (sqlParam.get("roomId") != null) {
			finder.append(" and liveRoomId= :liveRoomId");
			finder.setParam("liveRoomId", sqlParam.get("roomId"));
		}
		finder.append(" order by fileId desc ");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public Pagination getMediaFilePageByRoomForView(Map<String, Object> sqlParam, Integer pageNo, Integer pageSize) {
		String hql = " from ILiveMediaFile where mediaInfoStatus =1 and onlineFlag=1 and delFlag=0   ";
		Finder finder = Finder.create(hql);
		if (sqlParam.get("roomId") != null) {
			finder.append(" and liveRoomId= :liveRoomId");
			finder.setParam("liveRoomId", sqlParam.get("roomId"));
		}
		finder.append(" and mediaInfoDealState=1 ");
		finder.append(" order by fileId desc ");
		return find(finder, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveMediaFile> getTop4ForView(String keyWord) {
		String hql = "select file from ILiveMediaFile file,ILiveEnterprise enterprise where enterprise.enterpriseId=file.enterpriseId and enterprise.certStatus=4 and  enterprise.isDel=0 and file.mediaInfoStatus =1 and file.onlineFlag=1 and file.delFlag=0 ";
		Finder finder = Finder.create(hql);
		List<ILiveMediaFile> viewFiles = new ArrayList<>();
		int pageNo = 1;
		if (keyWord != null) {
			int pageSize = 4;
			if (pageNo == 1) {
				boolean roomNameInt = StringPatternUtil.isInteger(keyWord);
				if (roomNameInt) {
					finder.append(" and file.fileId =:fileId");
					finder.setParam("fileId", Long.parseLong(keyWord));
					List<ILiveMediaFile> find = this.find(finder);
					if (find != null && !find.isEmpty()) {
						viewFiles.addAll(find);
						finder = Finder.create(
								"select file from ILiveMediaFile file,ILiveEnterprise enterprise where enterprise.enterpriseId=file.enterpriseId and enterprise.certStatus=4 and  enterprise.isDel=0 and  file.mediaInfoStatus =1 and file.onlineFlag=1 and file.delFlag=0 and file.mediaFileName like :mediaFileName ");
						finder.append(" order by file.fileId desc ");
						finder.setParam("mediaFileName", "%" + keyWord + "%");
						Pagination find2 = find(finder, 1, pageSize - find.size());
						if (find2 != null) {
							List<ILiveMediaFile> list = (List<ILiveMediaFile>) find2.getList();
							if (list != null && !list.isEmpty()) {
								viewFiles.addAll(list);
							}
						}
					} else {
						finder = Finder.create(
								"select file from ILiveMediaFile file,ILiveEnterprise enterprise where enterprise.enterpriseId=file.enterpriseId and enterprise.certStatus=4 and  enterprise.isDel=0 and file.mediaInfoStatus =1 and file.onlineFlag=1 and file.delFlag=0 and file.mediaFileName like :mediaFileName ");
						finder.append(" order by file.fileId desc ");
						finder.setParam("mediaFileName", "%" + keyWord + "%");
						Pagination find2 = find(finder, pageNo, pageSize);
						if (find2 != null) {
							List<ILiveMediaFile> list = (List<ILiveMediaFile>) find2.getList();
							if (list != null && !list.isEmpty()) {
								viewFiles.addAll(list);
							}
						}
					}
				} else {
					finder = Finder.create(
							"select file from ILiveMediaFile file,ILiveEnterprise enterprise where enterprise.enterpriseId=file.enterpriseId and enterprise.certStatus=4 and  enterprise.isDel=0 and  file.mediaInfoStatus =1 and file.onlineFlag=1 and file.delFlag=0 and file.mediaFileName like :mediaFileName ");
					finder.append(" order by file.fileId desc ");
					finder.setParam("mediaFileName", "%" + keyWord + "%");
					Pagination find2 = find(finder, pageNo, pageSize);
					if (find2 != null) {
						List<ILiveMediaFile> list = (List<ILiveMediaFile>) find2.getList();
						if (list != null && !list.isEmpty()) {
							viewFiles.addAll(list);
						}
					}
				}

			}
		}
		return viewFiles;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveMediaFile> getPagerForView(String keyWord, Integer pageNo, Integer pageSize) {
		String hql = " select file from ILiveMediaFile file,ILiveEnterprise enterprise where enterprise.enterpriseId=file.enterpriseId and enterprise.certStatus=4 and  enterprise.isDel=0 and file.mediaInfoStatus =1 and file.onlineFlag=1 and file.delFlag=0 ";
		Finder finder = Finder.create(hql);
		List<ILiveMediaFile> viewFiles = new ArrayList<>();
		if (keyWord != null) {
			if (pageNo == 1) {
				boolean roomNameInt = StringPatternUtil.isInteger(keyWord);
				if (roomNameInt) {
					finder.append(" and file.fileId =:fileId");
					finder.setParam("fileId", Long.parseLong(keyWord));
					List<ILiveMediaFile> find = this.find(finder);
					if (find != null && !find.isEmpty()) {
						viewFiles.addAll(find);
						finder = Finder.create(
								"select file from ILiveMediaFile file ,ILiveEnterprise enterprise where enterprise.enterpriseId=file.enterpriseId  and enterprise.certStatus=4 and  enterprise.isDel=0 and file.mediaInfoStatus =1 and file.onlineFlag=1 and file.delFlag=0 and file.mediaFileName like :mediaFileName ");
						finder.append(" order by file.fileId desc ");
						finder.setParam("mediaFileName", "%" + keyWord + "%");
						Pagination find2 = find(finder, 1, pageSize - find.size());
						if (find2 != null) {
							List<ILiveMediaFile> list = (List<ILiveMediaFile>) find2.getList();
							if (list != null && !list.isEmpty()) {
								viewFiles.addAll(list);
							}
						}
					} else {
						finder = Finder.create(
								"select file from ILiveMediaFile file ,ILiveEnterprise enterprise where enterprise.enterpriseId=file.enterpriseId and enterprise.certStatus=4 and  enterprise.isDel=0 and file.mediaInfoStatus =1 and file.onlineFlag=1 and file.delFlag=0 and file.mediaFileName like :mediaFileName ");
						finder.append(" order by file.fileId desc ");
						finder.setParam("mediaFileName", "%" + keyWord + "%");
						Pagination find2 = find(finder, pageNo, pageSize);
						if (find2 != null) {
							List<ILiveMediaFile> list = (List<ILiveMediaFile>) find2.getList();
							if (list != null && !list.isEmpty()) {
								viewFiles.addAll(list);
							}
						}
					}
				} else {
					finder = Finder.create(
							"select file from ILiveMediaFile file ,ILiveEnterprise enterprise where enterprise.enterpriseId=file.enterpriseId and enterprise.certStatus=4 and  enterprise.isDel=0 and file.mediaInfoStatus =1 and file.onlineFlag=1 and file.delFlag=0 and file.mediaFileName like :mediaFileName ");
					finder.append(" order by file.fileId desc ");
					finder.setParam("mediaFileName", "%" + keyWord + "%");
					Pagination find2 = find(finder, pageNo, pageSize);
					if (find2 != null) {
						List<ILiveMediaFile> list = (List<ILiveMediaFile>) find2.getList();
						if (list != null && !list.isEmpty()) {
							viewFiles.addAll(list);
						}
					}
				}
			} else {
				finder = Finder.create(
						"select file from ILiveMediaFile file ,ILiveEnterprise enterprise where enterprise.enterpriseId=file.enterpriseId and enterprise.certStatus=4 and  enterprise.isDel=0 and file.mediaInfoStatus =1 and file.onlineFlag=1 and file.delFlag=0 and file.mediaFileName like :mediaFileName ");
				finder.append(" order by file.fileId desc ");
				finder.setParam("mediaFileName", "%" + keyWord + "%");
				Pagination find2 = find(finder, pageNo, pageSize);
				if (find2 != null) {
					List<ILiveMediaFile> list = (List<ILiveMediaFile>) find2.getList();
					if (list != null && !list.isEmpty()) {
						viewFiles.addAll(list);
					}
				}
			}
		}
		return viewFiles;
	}

	@Override
	public Pagination getPersonalFileList(Long userId, Integer pageNo, Integer pageSize) {
		Finder finder = Finder
				.create(" from ILiveMediaFile where mediaInfoDealState =1 and delFlag=0 and userId=:userId ");
		finder.append(" order by mediaCreateTime desc ");
		finder.setParam("userId", userId);
		Pagination pager = find(finder, pageNo, pageSize);
		return pager;
	}

	@Override
	public void updateMediaFile(ILiveMediaFile iLiveMediaFile) {
		this.getSession().update(iLiveMediaFile);
	}

	@Override
	public Pagination getAppMediaFileByEnterprise(String search, Integer enterpriseId, Integer pageNo,
			Integer pageSize,String userId,String roomIds) {
		Finder create = Finder
				.create("from ILiveMediaFile where mediaInfoDealState =1 and delFlag=0 and enterpriseId=:enterpriseId");
		if (search != null && !search.equals("")) {
			create.append(" and mediaFileName like :mediaFileName");
			create.setParam("mediaFileName", "%" + search + "%");
		}
		if(userId!=null) {
			create.append("and liveRoomId in:liveRoomId");
			create.setParam("liveRoomId", roomIds);
		}
		create.append(" order by mediaCreateTime desc ");
		create.setParam("enterpriseId", (long) enterpriseId);
		return this.find(create, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveMediaFile> getBatchVodListForStatics(Long startId, Integer size) {
		String hql = "from ILiveMediaFile where delFlag=0";
		Finder finder = Finder.create(hql);
		if(null!=startId) {
			finder.append(" and fileId > :startId");
			finder.setParam("startId", startId);
		}
		Query query = getSession().createQuery(finder.getOrigHql());
		finder.setParamsToQuery(query);
		query.setMaxResults(size);
		List<ILiveMediaFile> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveMediaFile> getListByIds(Long[] ids) {
		String hql = "from ILiveMediaFile where delFlag=0 and  fileId in (:fileIds)";
		Finder finder = Finder.create(hql);
		finder.setParamList("fileIds", ids);
		List<ILiveMediaFile> find = this.find(finder);
		return find;
	}

	@Override
	public Pagination getCollaborativeMediaFilePage(
			Map<String, Object> sqlParam, Integer pageNo, Integer pageSize,
			Long userId, Integer roomId) {
		Finder finder = Finder.create("select liveMediaFile from ILiveMediaFile liveMediaFile where 1=1 ");
		// 删除标记参数
		Integer delFlag = (Integer) sqlParam.get("delFlag");
		if (delFlag != null) {
			finder.append(" and liveMediaFile.delFlag = :delFlag");
			finder.setParam("delFlag", delFlag);
		}
		String mediaFileName = (String) sqlParam.get("mediaFileName");
		if (StringUtils.isNotBlank(mediaFileName)) {
			finder.append(" and liveMediaFile.mediaFileName like :mediaFileName");
			finder.setParam("mediaFileName", "%" + mediaFileName + "%");
		}
		// create_type参数
		Integer createType = (Integer) sqlParam.get("createType");
		if (createType != null && createType.intValue() != 5) {
			finder.append(" and liveMediaFile.createType = :createType");
			finder.setParam("createType", createType);
		}
		finder.append(" and ( liveMediaFile.liveRoomId = :liveRoomId or liveMediaFile.userId = :userId )");
		finder.setParam("liveRoomId", roomId);
		finder.setParam("userId", userId);
		finder.append(" order by liveMediaFile.mediaCreateTime desc");
		return find(finder, pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveMediaFile> selectILiveMediaFileListByEnterprieId(
			Integer enterpriseId, Integer fileType) {
		String hql = "from ILiveMediaFile bean where bean.delFlag=0";
		Finder finder = Finder.create(hql);
		finder.append(" and bean.enterpriseId =:enterpriseId");
		finder.setParam("enterpriseId", Long.parseLong(enterpriseId.toString()));
		finder.append(" and bean.fileType =:fileType");
		finder.setParam("fileType", fileType);
		finder.append(" order by bean.fileId");
		List<ILiveMediaFile> find = this.find(finder);
		return find;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveMediaFile> selectILiveMediaFileListByIsMediaType(
			Integer mediaTypeTemporary, Integer delFlag) {
		String hql = "from ILiveMediaFile bean where bean.delFlag=:delFlag";
		Finder finder = Finder.create(hql);
		finder.setParam("delFlag", delFlag);
		finder.append(" and bean.isMediaType =:isMediaType");
		finder.setParam("isMediaType", mediaTypeTemporary);
		finder.append(" order by bean.fileId");
		List<ILiveMediaFile> find = this.find(finder);
		return find;
	}

	@Override
	public List<ILiveMediaFile> getListByTypeAndName(String name, Integer fileType, Integer enterpriseId) {
		String hql = "from ILiveMediaFile where delFlag=0 and fileType = :fileType and enterpriseId = :enterpriseId and onlineFlag = :onlineFlag and mediaFileName like :name order by fileId desc";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("fileType", fileType);
		query.setParameter("onlineFlag", 1);
		query.setParameter("name", "%"+name+"%");
		long en = (long) enterpriseId;
		query.setParameter("enterpriseId", en);
		@SuppressWarnings("unchecked")
		List<ILiveMediaFile> list = query.list();
		return list;
	}

	@Override
	public Pagination getPersonalFileList(Long userId, int pageNo, int pageSize, String keyWord) {
		Finder finder = Finder
				.create(" from ILiveMediaFile where mediaInfoDealState =1 and delFlag=0 and userId=:userId and mediaFileName like:mediaFileName");
		finder.append(" order by mediaCreateTime desc ");
		finder.setParam("userId", userId);
		finder.setParam("mediaFileName", "%"+keyWord+"%");
		Pagination pager = find(finder, pageNo, pageSize);
		return pager;
	}

	@Override
	public Pagination getMedialFilePage1(Map<String, Object> sqlParam, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from ILiveMediaFile liveMediaFile where 1=1 ");
		// 用户参数
		if (sqlParam.containsKey("isSync")) {
			finder.append(" and liveMediaFile.isSync = :isSync");
			finder.setParam("isSync",sqlParam.get("isSync"));
		}
		if (sqlParam.containsKey("enterpriseId")) {
			finder.append(" and liveMediaFile.enterpriseId = :enterpriseId");
			finder.setParam("enterpriseId", sqlParam.get("enterpriseId"));
		}
		if (sqlParam.containsKey("userId")) {
			Object roomIds= sqlParam.get("roomIds");
			if(roomIds != null && !"".equals(roomIds)) {
				finder.append(" and (liveMediaFile.userId = :userId or liveMediaFile.liveRoomId in"+roomIds+")");
			}else {
				finder.append(" and liveMediaFile.userId = :userId ");
			}
			finder.setParam("userId", sqlParam.get("userId"));
		}
		if (sqlParam.containsKey("enterpriseId")) {
			finder.append(" and liveMediaFile.enterpriseId = :enterpriseId");
			finder.setParam("enterpriseId", sqlParam.get("enterpriseId"));
		}
		if (sqlParam.containsKey("mediaFileName")) {
			String mediaFileName = sqlParam.get("mediaFileName").toString();
			finder.append(" and liveMediaFile.mediaFileName like :mediaFileName ");
			finder.setParam("mediaFileName","%" + mediaFileName + "%");
		}
		finder.append(" order by liveMediaFile.mediaCreateTime desc");
		return find(finder, pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
	}

	@Override
	public List<ILiveMediaFile> selectILiveMediaFileBycloudDiskFileIds(String[] cloudDiskFileId) {
		String hql = "from ILiveMediaFile where cloudDiskFileId in (:cloudDiskFileId) ";
		Finder finder = Finder.create(hql);
		finder.setParamList("cloudDiskFileId", cloudDiskFileId);
		List<ILiveMediaFile> find = this.find(finder);
		return find;
	}
}
