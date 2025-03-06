package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveFileWhiteBillDao;
import com.bvRadio.iLive.iLive.entity.ILiveFileWhiteBill;
import com.bvRadio.iLive.iLive.entity.ILiveViewWhiteBill;

public class ILiveFileWhiteBillDaoImpl extends HibernateBaseDao<ILiveFileWhiteBill, Long>
		implements ILiveFileWhiteBillDao {

	@Override
	protected Class<ILiveFileWhiteBill> getEntityClass() {
		return ILiveFileWhiteBill.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean checkUserInWhiteList(String phone, Long fileId) {
		Finder finder = Finder.create("from ILiveFileWhiteBill where fileId=:fileId and phoneNum=:phoneNum");
		finder.setParam("fileId", fileId);
		finder.setParam("phoneNum", phone);
		List<ILiveFileWhiteBill> find = this.find(finder);
		if (find != null && !find.isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	public void deleteFromFileId(Long fileId) {
		Query createQuery = this.getSession().createQuery("delete from ILiveFileWhiteBill where fileId=:fileId");
		createQuery.setLong("fileId", fileId);
		createQuery.executeUpdate();
	}

	@Override
	public void batchSavePhoneNums(List<ILiveFileWhiteBill> bills) {
		for (ILiveFileWhiteBill bill : bills) {
			getSession().save(bill);
			getSession().flush();
		}
	}

	@Override
	public List<ILiveFileWhiteBill> getFileWhiteBills(Long fileId) {
		String hql = "from ILiveFileWhiteBill where fileId=:fileId";
		Finder finder = Finder.create(hql);
		finder.setParam("fileId", fileId);
		List<ILiveFileWhiteBill> find = this.find(finder);
		return find;
	}

	@Override
	public Pagination getPage(String queryNum, Integer pageNo, Integer pageSize, Long fileId) {
		this.getSession().clear();
		Finder finder = Finder.create("select bean from ILiveFileWhiteBill bean where 1=1 ");
		if (queryNum != null) {
			finder.append(" and (bean.phoneNum like :phoneNum or bean.userName like:phoneNum)");
			finder.setParam("phoneNum", "%" + queryNum + "%");
		}
		finder.append(" and bean.fileId=:fileId");
		finder.setParam("fileId", fileId);
		finder.append(" order by exportTime desc ");
		return find(finder, pageNo == null ? 1 : pageNo, pageSize);
	}

	@Override
	public void clearViewWhiteBill(Long fileId) {
		Query createQuery = this.getSession().createQuery("delete from ILiveFileWhiteBill where fileId=:fileId");
		createQuery.setLong("fileId", fileId);
		createQuery.executeUpdate();
	}

	@Override
	public void save(ILiveFileWhiteBill bill) {
		this.getSession().save(bill);
	}

	@Override
	public void deleteById(Long whitebillId) {
		Query createQuery = this.getSession().createQuery("delete from ILiveFileWhiteBill where billId=:billId");
		createQuery.setLong("billId", whitebillId);
		createQuery.executeUpdate();
	}

	@Override
	public List<ILiveFileWhiteBill> getAllViewWhiteBilll(String queryNum, Long fileId) {
		this.getSession().clear();
		Finder finder = Finder.create("select bean from ILiveFileWhiteBill bean where 1 = 1");
		
		if(queryNum!=null&&!"".endsWith(queryNum)) {
			finder.append(" and (bean.phoneNum like :phoneNum or bean.userName like:phoneNum)");
			finder.setParam("phoneNum", "%" + queryNum + "%");
		}
		finder.append(" and bean.fileId=:fileId");
		finder.setParam("fileId", fileId);
		finder.append(" order by exportTime desc ");
		return find(finder);
	}

}
