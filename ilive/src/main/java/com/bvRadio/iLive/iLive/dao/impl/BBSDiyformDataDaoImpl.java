package com.bvRadio.iLive.iLive.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.BBSDiyformDataDao;
import com.bvRadio.iLive.iLive.entity.BBSDiyformData;
import com.bvRadio.iLive.iLive.entity.ILiveManager;

import freemarker.template.SimpleDate;

/**
 * @author administrator 自定义表单数据
 */
@Repository
public class BBSDiyformDataDaoImpl extends HibernateBaseDao<BBSDiyformData, Integer> implements BBSDiyformDataDao {

	@Override
	protected Class<BBSDiyformData> getEntityClass() {
		return BBSDiyformData.class;
	}

	@Override
	public void saveDiyformData(BBSDiyformData formData) {
		this.getSession().save(formData);
	}

	@Override
	public boolean checkUserSignUp(String userId, Integer formId) {
		String hql = "from BBSDiyformData where managerId=? and bbsDiyform.diyformId=?";
		List find = this.find(hql, Long.parseLong(userId), formId);
		if (find != null && !find.isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	public void saveDiyFormList(List<BBSDiyformData> diyformDatas) {
		for (BBSDiyformData data : diyformDatas) {
			super.getSession().save(data);
		}
	}

	@Override
	public Pagination distinctDiyformSunmitId(Integer formId, Date startTime, Date endTime, Integer pageNo,
			Integer pageSize) {
		Finder finder = Finder.create("select submitId from BBSDiyformData");
		finder.append(" where bbsDiyform.diyformId=:diyformId");
		finder.append(
				" and modelId in (select diymodelId from BBSDiymodel bean where bean.bbsDiyform.diyformId = :diyformId)");
		if (null != startTime) {
			finder.append(" and createTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and createTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		finder.append(" group by submitId order by createTime asc");
		finder.setParam("diyformId", formId);
		Pagination find = this.find(finder, pageNo, pageSize);
		return find;
	}
	
	@Override
	public List<Long> distinctDiyformSunmitId(Integer formId, Date startTime, Date endTime){
		Finder finder = Finder.create("select submitId from BBSDiyformData");
		finder.append(" where bbsDiyform.diyformId=:diyformId");
		finder.append(
				" and modelId in (select diymodelId from BBSDiymodel bean where bean.bbsDiyform.diyformId = :diyformId)");
		if (null != startTime) {
			finder.append(" and createTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and createTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		finder.append(" group by submitId order by createTime asc");
		finder.setParam("diyformId", formId);
		List<Long> find = this.find(finder);
		return find;
	}
	
	@Override
	public Pagination distinctDiyformUser(Integer formId, Integer pageNo, Integer pageSize) {
		String hql = "select managerId from BBSDiyformData where bbsDiyform.diyformId=:diyformId group by managerId order by createTime asc ";
		Finder finder = Finder.create(hql);
		finder.setParam("diyformId", formId);
		Pagination find = this.find(finder, pageNo, pageSize);
		return find;
	}

	@Override
	public String[] getFormStatic(ILiveManager manager, Integer[] questionIds) {
		String hql = "from BBSDiyformData where managerId=:managerId and modelId in(:questionIds) order by dataOrder asc";
		Finder finder = Finder.create(hql);
		finder.setParam("managerId", manager.getUserId());
		finder.setParamList("questionIds", questionIds);
		List<BBSDiyformData> find = this.find(finder);
		if (find != null && !find.isEmpty()) {
			Map<Integer, BBSDiyformData> dataMap = new HashMap<>();
			for (BBSDiyformData data : find) {
				dataMap.put(data.getModelId(), data);
			}
			int orgLength = questionIds.length;
			String[] outLists = new String[orgLength + 3];
			int nowList = outLists.length;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = null;
			for (int i = 0; i < nowList; i++) {
				if (i == 0) {
					outLists[i] = String.valueOf(manager.getUserId());
				} else if (i == 1) {
					outLists[i] = manager.getMobile();
				} else if (i == nowList - 1) {
					outLists[i] = time;
				} else {
					BBSDiyformData bbsDiyformData = dataMap.get(questionIds[i - 2]);
					if (time == null && bbsDiyformData!=null &&bbsDiyformData.getCreateTime()!=null) {
						time = simpleDateFormat.format(bbsDiyformData.getCreateTime());
					}
					if (bbsDiyformData == null) {
						outLists[i] = "";
					} else if (bbsDiyformData.getDataValue() == null) {
						outLists[i] = "";
					} else {
						outLists[i] = bbsDiyformData.getDataValue();
					}
				}
			}
			return outLists;
		}
		return null;
	}
	
	
	@Override
	public String[] getFormStaticBySubmitId(Long submitId, Integer[] questionIds) {
		String hql = "from BBSDiyformData where submitId=:submitId and modelId in(:questionIds) order by dataOrder asc";
		Finder finder = Finder.create(hql);
		finder.setParam("submitId", submitId);
		finder.setParamList("questionIds", questionIds);
		List<BBSDiyformData> find = this.find(finder);
		if (find != null && !find.isEmpty()) {
			Map<Integer, BBSDiyformData> dataMap = new HashMap<>();
			for (BBSDiyformData data : find) {
				dataMap.put(data.getModelId(), data);
			}
			int orgLength = questionIds.length;
			String[] outLists = new String[orgLength + 1];
			int nowList = outLists.length;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = null;
			for (int i = 0; i < nowList; i++) {
				if (i == 0) {
					
				} else {
					BBSDiyformData bbsDiyformData = dataMap.get(questionIds[i - 1]);
					if (time == null && bbsDiyformData!=null &&bbsDiyformData.getCreateTime()!=null) {
						time = simpleDateFormat.format(bbsDiyformData.getCreateTime());
					}
					if (bbsDiyformData == null) {
						outLists[i] = "";
					} else if (bbsDiyformData.getDataValue() == null) {
						outLists[i] = "";
					} else {
						outLists[i] = bbsDiyformData.getDataValue();
					}
				}
			}
			//第一个是时间
			outLists[0] = time;
			return outLists;
		}
		return null;
	}

}
