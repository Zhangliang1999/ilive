package com.bvRadio.iLive.iLive.dao.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
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
import com.thoughtworks.xstream.mapper.Mapper.Null;

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
	public void deleteLiveMediaFile(Long id) {
		ILiveMediaFile iLiveMediaFile = get(id);
		iLiveMediaFile.setDelFlag(1);
		getSession().update(iLiveMediaFile);
		getSession().flush();
	}

	@Override
	public void deleteLiveMediaFilesByIds(Long[] ids) {
		if (ids != null && ids.length > 0) {
			for (Long id : ids) {
				deleteLiveMediaFile(id);
			}
		}
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
		Pagination find = find(finder, pageNO, pageSize);
		return find;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveMediaFile> selectILiveMediaFileListByEventId(Long liveEventId) throws Exception {
		Finder finder = Finder.create("select bean from ILiveMediaFile bean where bean.liveEventId=:liveEventId ");
		finder.setParam("liveEventId", liveEventId);
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
		Finder finder = Finder.create("select liveMediaFile from ILiveMediaFile liveMediaFile" + " where 1=1 ");
		// 删除标记参数
		Integer delFlag = (Integer) sqlParam.get("delFlag");
		if (delFlag != null) {
			finder.append(" and (liveMediaFile.delFlag is null or  liveMediaFile.delFlag != :delFlag)");
			finder.setParam("delFlag", delFlag);
		}
		// 用户参数
		String userId = (String) sqlParam.get("userId");
		if (StringUtils.isNotBlank(userId)) {
			Long userid = Long.parseLong(userId);
			finder.append(" and liveMediaFile.userId = :userId");
			finder.setParam("userId", userid);
		}
		// 文件名称参数
		String mediaFileName = (String) sqlParam.get("mediaFileName");
		if (StringUtils.isNotBlank(mediaFileName)) {
			finder.append(" and liveMediaFile.mediaFileName like :mediaFileName");
			finder.setParam("mediaFileName", "%" + mediaFileName + "%");
		}
		// file_type参数
		Integer fileType = (Integer) sqlParam.get("fileType");
		if (fileType != null) {
			finder.append(" and liveMediaFile.fileType = :fileType");
			finder.setParam("fileType", fileType);
		}
		// create_type参数
		Integer createType = (Integer) sqlParam.get("createType");
		if (createType != null) {
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
		return find(finder, pageNo == null ? 1 : pageNo, pageSize);
	}
	
	@Override
	public Pagination getPager(Integer createType,Integer mediaState,Timestamp startTime,Timestamp endTime,String query,Integer type, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from ILiveMediaFile where fileType = :fileType");
		finder.setParam("fileType", type);
		if(createType!=null) {
			finder.append(" and createType = :createType");
			finder.setParam("createType",createType);
		}
		if(mediaState!=null) {
			
		}
		if(query!=null && !query.trim().equals("")) {
			finder.append(" and mediaFileName like :query");
			finder.setParam("query", "%"+query+"%");
		}
		return find(finder, pageNo == null ? 1 : pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveMediaFile> getListByType(Integer fileType,Integer enterpriseId) {
		String hql = "from ILiveMediaFile where fileType = :fileType and enterpriseId = :enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("fileType", fileType);
		long en = (long)enterpriseId;
		query.setParameter("enterpriseId", en);
		List<ILiveMediaFile> list = query.list();
		return list;
	}

	@Override
	public Pagination getMediaFilePageByRoom(Map<String, Object> sqlParam, Integer pageNo, Integer pageSize) {
		String hql = " from ILiveMediaFile where createType=0 and mediaInfoStatus =1 and onlineFlag=1 and delFlag=0   ";
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
		String hql = " from ILiveMediaFile where createType=0 and mediaInfoStatus =1 and onlineFlag=1 and delFlag=0   ";
		Finder finder = Finder.create(hql);
		if (sqlParam.get("roomId") != null) {
			finder.append(" and liveRoomId= :liveRoomId");
			finder.setParam("liveRoomId", sqlParam.get("roomId"));
		}
		finder.append(" and mediaInfoDealState=1 ");
		finder.append(" order by fileId desc ");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public int getNum(Integer type,Integer state) {
		StringBuilder hql= new StringBuilder("select count(*) from ILiveMediaFile where fileType = :fileType");
		/*if(state!=null) {
			hql.append(" and ")
		}*/
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("fileType", type);
		long num = (Long)query.uniqueResult();
		return (int)num;
	}

}
