package com.bvRadio.iLive.iLive.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveViewWhiteBillDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseWhiteBill;
import com.bvRadio.iLive.iLive.entity.ILiveViewWhiteBill;

public class ILiveViewWhiteBillDaoImpl extends HibernateBaseDao<ILiveViewWhiteBill, Long>
		implements ILiveViewWhiteBillDao {

	@Override
	public boolean checkUserInWhiteList(String userId, Long liveEventId) {
		String hql = "from ILiveViewWhiteBill where phoneNum=? and liveEventId=?";
		List find = this.find(hql, userId, liveEventId);
		if (find != null && !find.isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	protected Class<ILiveViewWhiteBill> getEntityClass() {
		return ILiveViewWhiteBill.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveViewWhiteBill> getAllViewWhiteBill(Long liveEventId) {
		String hql = " from ILiveViewWhiteBill where liveEventId=" + liveEventId;
		Query createQuery = this.getSession().createQuery(hql);
		return createQuery.list();
	}

	@Override
	public List<String> distinctUserPhone(String[] phoneNums, Long liveEventId) {
		List<String> list = new ArrayList<>();
		String hql = " from ILiveViewWhiteBill where liveEventId=:liveEventId and phoneNum in(:phoneNums)";
		Finder finder = Finder.create(hql);
		finder.setParam("liveEventId", liveEventId);
		finder.setParamList("phoneNums", phoneNums);
		List<ILiveViewWhiteBill> findList = this.find(finder);
		if (findList != null && !findList.isEmpty()) {
			for (String outerPhone : phoneNums) {
				boolean ret = true;
				for (ILiveViewWhiteBill whiteBill : findList) {
					String phoneNum = whiteBill.getPhoneNum();
					if (outerPhone.equals(phoneNum)) {
						ret = false;
					}
				}
				if (ret) {
					list.add(outerPhone);
				}
			}
		} else {
			for (String outerPhone : phoneNums) {
				list.add(outerPhone);
			}
		}
		return list;
	}

	@Override
	public void batchInsertUserPhone(List<ILiveViewWhiteBill> distinctUserPhone) {
		for(ILiveViewWhiteBill bill : distinctUserPhone ) {
			// System.out.println("bill------>"+bill.getBillId());
		}
	}
	
	@Override
	public void clearViewWhiteBill(Long liveEventId) {
		String hql = "delete from ILiveViewWhiteBill where liveEventId=:liveEventId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("liveEventId", liveEventId);
		query.executeUpdate();
	}

	@Override
	public void saveIliveViewWhiteBill(ILiveViewWhiteBill bill) {
		this.getSession().save(bill);
	}

	@Override
	public Pagination getMyInviteLive(Long userId, String phoneNum,int pageNo, int pageSize) {
		Finder create = Finder.create(" select room from ILiveLiveRoom room,ILiveViewWhiteBill bill where bill.phoneNum=:phoneNum and room.liveEvent.liveStatus in (0,1) and room.liveEvent.liveEventId=bill.liveEventId and room.liveEvent.viewAuthorized=4 order by room.liveEvent.liveStatus desc,room.liveEvent.liveEventId desc ");
		create.setParam("phoneNum", phoneNum);
		Pagination find = this.find(create, pageNo, pageSize);
		return find;
	}

	@Override
	public void deleteById(String phoneNum, Long liveEventId) {
		String hql = "delete from ILiveViewWhiteBill where liveEventId=:liveEventId and phoneNum=:phoneNum ";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("liveEventId", liveEventId);
		query.setParameter("phoneNum", phoneNum);
		query.executeUpdate();
	}

	@Override
	
	public Pagination getPage(String queryName, Integer pageNo, Integer pageSize, Long iLiveEventId) {
		this.getSession().clear();
		Finder finder = Finder.create("select bean from ILiveViewWhiteBill bean where 1=1 ");
		if (queryName != null) {
			finder.append(" and (bean.phoneNum like :phoneNum or bean.userName like:phoneNum)");
			finder.setParam("phoneNum", "%" + queryName + "%");
		}
		finder.append(" and bean.liveEventId=:iLiveEventId");
		finder.setParam("iLiveEventId", iLiveEventId);
		finder.append(" order by exportTime desc ");
		return find(finder, pageNo == null ? 1 : pageNo, pageSize);
	}

	  @Override
      public List<ILiveViewWhiteBill> getAllViewWhiteBilll(String queryNum, Long liveEventId) {
		
		this.getSession().clear();
		Finder finder = Finder.create("select bean from ILiveViewWhiteBill bean where 1 = 1");
		
		if(queryNum!=null&&!"".endsWith(queryNum)) {
			finder.append(" and (bean.phoneNum like :phoneNum or bean.userName like:phoneNum)");
			finder.setParam("phoneNum", "%" + queryNum + "%");
		}
		finder.append(" and bean.liveEventId=:iLiveEventId");
		finder.setParam("iLiveEventId", liveEventId);
		finder.append(" order by exportTime desc ");
		return find(finder);
	}

	@Override
	public void updateEventId(long oldLiveEventId, Long saveIliveMng) {
		String hql = "update ILiveViewWhiteBill set liveEventId=:saveIliveMng where liveEventId=:oldLiveEventId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("saveIliveMng", saveIliveMng);
		query.setParameter("oldLiveEventId", oldLiveEventId);
		query.executeUpdate();
	}

}
