package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveEnterpriseMemberDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseWhiteBill;

/**
 * 企业白名单管理
 * 
 * @author administrator
 *
 */
@Repository
public class ILiveEnterpriseMemberDaoImpl extends HibernateBaseDao<ILiveEnterpriseWhiteBill, Integer>
		implements ILiveEnterpriseMemberDao {

	@Override
	public Pagination getPage(String queryName, Integer pageNo, Integer pageSize, Integer enterpriseId) {
		this.getSession().clear();
		Finder finder = Finder.create("select bean from ILiveEnterpriseWhiteBill bean where bean.isDel = 0");
		if (queryName != null) {
			finder.append(" and (bean.phoneNum like :phoneNum or bean.userName like:phoneNum)");
			finder.setParam("phoneNum", "%" + queryName + "%");
		}
		finder.append(" and bean.enterpriseId=:enterpriseId");
		finder.setParam("enterpriseId", enterpriseId);
		finder.append(" order by exportTime desc ");
		return find(finder, pageNo == null ? 1 : pageNo, pageSize);
	}

	@Override
	public List<ILiveEnterpriseWhiteBill> getList(Integer enterpriseId) {
		this.getSession().clear();
		Finder finder = Finder.create("select bean from ILiveEnterpriseWhiteBill bean where bean.isDel = 0");
		
		finder.append(" and bean.enterpriseId=:enterpriseId");
		finder.setParam("enterpriseId", enterpriseId);
		finder.append(" order by exportTime desc ");
		return find(finder);
	}
	@Override
	protected Class<ILiveEnterpriseWhiteBill> getEntityClass() {
		return ILiveEnterpriseWhiteBill.class;
	}

	@Override
	public void deleteWhite(Integer whitebillId) {
		ILiveEnterpriseWhiteBill bean=this.get(whitebillId);
		bean.setIsDel(1);
		this.getSession().update(bean);
	}

	@Override
	public void addWhite(ILiveEnterpriseWhiteBill white) {
		this.getSession().save(white);
	}

	@Override
	public List<ILiveEnterpriseWhiteBill> getEnterpriseWhite(Integer enterpriseId, String phoneNum) {
		
		this.getSession().clear();
		Finder finder = Finder.create("select bean from ILiveEnterpriseWhiteBill bean where bean.isDel = 0");
		if(enterpriseId!=null) {
			finder.append("and bean.enterpriseId=:enterpriseId");
			finder.setParam("enterpriseId", enterpriseId);
		}
		if(phoneNum!=null&&!"".endsWith(phoneNum)) {
			finder.append(" and (bean.phoneNum like :phoneNum or bean.userName like:phoneNum)");
			finder.setParam("phoneNum", "%" + phoneNum + "%");
		}
		return find(finder);
	}
	
	@Override
	public List<ILiveEnterpriseWhiteBill> getEnterpriseWhite(String phoneNum) {
		List find = this.find(" from ILiveEnterpriseWhiteBill where phoneNum=? ", phoneNum);
		return find;
	}

	@Override
	public void deleteAll(Integer enterpriseId) {
		String hql = "update ILiveEnterpriseWhiteBill  set isDel = 1 where enterpriseId = :enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId",enterpriseId);
		query.executeUpdate();
				
	}

}
