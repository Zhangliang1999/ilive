package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.BBSDiyformDataDao;
import com.bvRadio.iLive.iLive.entity.BBSDiyformData;

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

}
