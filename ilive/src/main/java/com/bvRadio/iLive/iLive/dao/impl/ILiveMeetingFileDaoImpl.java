package com.bvRadio.iLive.iLive.dao.impl;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveMeetingFileDao;
import com.bvRadio.iLive.iLive.entity.IliveMeetingFile;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

@Repository
public class ILiveMeetingFileDaoImpl extends HibernateBaseDao<IliveMeetingFile,Long> implements ILiveMeetingFileDao {
    @Override
    protected Class<IliveMeetingFile> getEntityClass() {
        return null;
    }

    @Override
    public IliveMeetingFile selectIliveMeetingFileByFileId(Long fileId){
        Query query = this.getSession().createQuery("from IliveMeetingFile bean where 1=1 and bean.fileId=:fileId and bean.delFlag <> 1 ");
        query.setParameter("fileId",fileId);
        Object obj = query.uniqueResult();
        if(obj != null && obj instanceof IliveMeetingFile){
            return  (IliveMeetingFile) obj;
        }
        return null;
    }

    @Override
    public void updateMeetingMediaFile(IliveMeetingFile iliveMeetingFile) {
        this.getSession().update(iliveMeetingFile);
    }

    @Override
    public Serializable saveMeetingFile(IliveMeetingFile iliveMeetingFile) {
        iliveMeetingFile.setMediaUpdateTime(new Timestamp(System.currentTimeMillis()));
        return this.getSession().save(iliveMeetingFile);
    }

    @Override
    public Pagination selectMeetingFileListByPage(Map<String,Object> params) {

        Finder finder = Finder.create(" from IliveMeetingFile bean where 1=1 and bean.liveRoomId=:roomId and bean.delFlag <> 1 ");
        finder.setParam("roomId", params.get("roomId"));
        if(params.get("fileName") != null && StringUtils.isNotBlank(params.get("fileName").toString())){
            finder.append(" and bean.mediaFileName like :mediaFileName");
            finder.setParam("mediaFileName","%"+params.get("fileName")+"%");
        }
        if(params.get("fileType") != null){
            finder.append(" and bean.fileType in (:fileType) ");
            String fileType = (String) params.get("fileType");
            String[] split = fileType.split(",");
            Integer[] typs = new Integer[split.length];
            for (int i = 0; i < split.length; i++) {
                typs[i] = Integer.valueOf(split[i]);
            }
            finder.setParamList("fileType",typs);
        }
        finder.append(" order by bean.mediaCreateTime desc");
        Pagination pagination = find(finder, (Integer) params.get("pageNo"), (Integer) params.get("pageSize"));
        return pagination;
    }

    @Override
    public Integer batchDeleteMeetingFiles(String fileIds) {
        Query query = this.getSession().createQuery("update IliveMeetingFile bean set bean.delFlag = 1 where 1=1 and bean.fileId in ("+fileIds+") ");
        return query.executeUpdate();
    }

    @Override
    public void deleteMeetingAllFiles(Integer roomId) {
        Query query = this.getSession().createQuery("update IliveMeetingFile bean set bean.delFlag = 1 where 1=1 and bean.liveRoomId =:liveRoomId ");
        query.setParameter("liveRoomId",roomId);
        query.executeUpdate();
    }

    @Override
    public void deleteMeetingAllFilesByType(Integer roomId, Integer fileType) {
        String hql = null;
        if(fileType == null){
            hql = "update IliveMeetingFile bean set bean.delFlag = 1 where bean.liveRoomId =:liveRoomId ";
        }else{
            hql = "update IliveMeetingFile bean set bean.delFlag = 1 where bean.fileType = :fileType and bean.liveRoomId =:liveRoomId ";
        }
        Query query = this.getSession().createQuery(hql);
        query.setParameter("liveRoomId",roomId);
        if(fileType != null){
            query.setParameter("fileType",fileType);
        }
        query.executeUpdate();
    }
}
