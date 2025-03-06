package com.jwzt.statistic.dao.impl;

import java.util.Date;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.common.page.Pagination;
import com.jwzt.statistic.dao.WorkingLogDao;
import com.jwzt.statistic.entity.WorkingLog;
@Repository
public class WorkingLogDaoImpl extends BaseHibernateDao<WorkingLog, String> implements
		WorkingLogDao {

	@Override
	protected Class<WorkingLog> getEntityClass() {
		return WorkingLog.class;
	}

	@Override
	public void insertWorkingLog(WorkingLog workingLog) throws Exception {
		Session session = getSession();
		session.save(workingLog);
	}

	@Override
	public Pagination findWorkingLogAll(Integer systemId,
			Integer modelId, String contentId, String content,
			String contentName, Integer userId, String userName,
			String terminal, Date startTime, Date endTime,Integer pageNo, Integer pageSize) throws Exception {
		Finder finder = Finder.create("select bean from WorkingLog bean");
		finder.append(" where 1=1 ");
		if(systemId!=null){
			finder.append(" and bean.systemId=:systemId");
			finder.setParam("systemId", systemId);
		}
		if(modelId!=null){
			finder.append(" and bean.modelId=:modelId");
			finder.setParam("modelId", modelId);
		}
		if(contentId!=null){
			finder.append(" and bean.contentId=:contentId");
			finder.setParam("contentId", contentId);
		}
		if(content!=null){
			finder.append(" and bean.content like :content");
			finder.setParam("content", "%"+content+"%");
		}
		if(contentName!=null){
			finder.append(" and bean.contentName=:contentName");
			finder.setParam("contentName", contentName);
		}
		if(userId!=null){
			finder.append(" and bean.userId=:userId");
			finder.setParam("userId", userId);
		}
		if(userName!=null){
			finder.append(" and bean.userName=:userName");
			finder.setParam("userName", userName);
		}
		if(terminal!=null){
			finder.append(" and bean.terminal=:terminal");
			finder.setParam("terminal", terminal);
		}
		if(startTime==null&&endTime!=null){
			finder.append(" and bean.createTime<:endTime ");
			finder.setParam("endTime", endTime);
		}else if(startTime!=null&&endTime==null){
			finder.append(" and bean.createTime>:startTime and bean.createTime<:endTime ");
			finder.setParam("startTime", startTime);
			finder.setParam("endTime", new Date());
		}else if(startTime!=null&&endTime!=null){
			finder.append(" and bean.createTime>:startTime and bean.createTime<:endTime ");
			finder.setParam("startTime", startTime);
			finder.setParam("endTime", endTime);
		}
		finder.append(" order by bean.createTime desc");
		Pagination find = find(finder, pageNo, pageSize);
		return find;
	}

}
