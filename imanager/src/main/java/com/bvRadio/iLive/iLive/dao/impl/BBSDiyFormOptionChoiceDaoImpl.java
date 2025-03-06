package com.bvRadio.iLive.iLive.dao.impl;


import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.BBSDiyFormOptionChoiceDao;
import com.bvRadio.iLive.iLive.entity.BBSDiyformOptionChoiceData;

@Repository
public class BBSDiyFormOptionChoiceDaoImpl extends HibernateBaseDao<BBSDiyformOptionChoiceData, Long> implements BBSDiyFormOptionChoiceDao {

	@Override
	protected Class<BBSDiyformOptionChoiceData> getEntityClass() {
		return BBSDiyformOptionChoiceData.class;
	}


	@Override
	public Pagination getMedialFilePage(Map<String, Object> sqlParam, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("select bBSDiyformOptionChoiceData from BBSDiyformOptionChoiceData bBSDiyformOptionChoiceData "
				+ " inner join fetch bBSDiyformOptionChoiceData.bbsDiyformOption option"
				+ "  inner join fetch option.bbsDiymodel model"
				+ "  inner join fetch model.bbsDiyform fiyform"
				+ "  inner join fetch bBSDiyformOptionChoiceData.user user"
				+ "  inner join fetch bBSDiyformOptionChoiceData.iLiveIpAddress iLiveIpAddress"
				+ " where fiyform.formType = 1");
		//diyFormId
		Integer diyFormId = (Integer)sqlParam.get("diyFormId");
		if(diyFormId!=null){
			finder.append(" and fiyform.diyformId = :diyFormId");
			finder.setParam("diyFormId", diyFormId.intValue());
		}
		//username
		String username = (String)sqlParam.get("username");
		if(StringUtils.isNotBlank(username)){
			finder.append(" and user.userName like :username");
			finder.setParam("username", "%"+username+"%");
		}

		finder.append(" order by bBSDiyformOptionChoiceData.createTime desc");
		return find(finder, pageNo==null?1:pageNo, pageSize==null?10:pageSize);
	}


	@Override
	public BigInteger selectUserCount(Map<String, Object> sqlParam) {
		SQLQuery sqlQuery = getSession().createSQLQuery("SELECT COUNT(DISTINCT a.user_id) "
				+ " FROM bbs_diyform_option_choice a,bbs_diyform_option b,bbs_diymodel c,bbs_diyform d,ilive_user e"
				+ " WHERE a.option_id = b.option_id AND b.model_id = c.diymodel_id AND c.diyform_id = d.diyform_id"
				+ " AND a.user_id = e.user_id and d.form_type=1"
				+ " and e.username LIKE :username AND d.diyform_id = :diyFormId");
		//diyFormId
		Integer diyFormId = (Integer)sqlParam.get("diyFormId");
		sqlQuery.setInteger("diyFormId", diyFormId);
		//username
		String username = (String)sqlParam.get("username");
		if(StringUtils.isBlank(username)){
			username = "";
		}
		sqlQuery.setString("username", "%"+username+"%");
		List<BigInteger> list = sqlQuery.list();
		return list.get(0);
	}




}
