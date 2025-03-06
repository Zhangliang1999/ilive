package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveLotteryParktakeDao;
import com.bvRadio.iLive.iLive.entity.ILiveLotteryParktake;

@Repository
public class ILiveLotteryParktakeDaoImpl extends HibernateBaseDao<ILiveLotteryParktake, Long> implements ILiveLotteryParktakeDao {

	@Override
	public Pagination getPage(Integer isPrize, String search,Long lotteryId, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from ILiveLotteryParktake where lotteryId =:lotteryId");
		finder.setParam("lotteryId", lotteryId);
		if(isPrize != null && isPrize==1) {
			finder.append(" and isPrize = :isPrize");
			finder.setParam("isPrize", isPrize);
		}
		if (search!=null&&!"".equals(search)) {
			finder.append(" and (userName like :search or phone like :search)");
			finder.setParam("search", "%"+search+"%");
		}
		finder.append(" order by id DESC");
		return find(finder, pageNo, pageSize);
	}

	@Override
	protected Class<ILiveLotteryParktake> getEntityClass() {
		return ILiveLotteryParktake.class;
	}

	@Override
	public List<ILiveLotteryParktake> getListByLotteryIdAndPhone(Long lotteryId, String phone) {
		String hql = "from ILiveLotteryParktake where lotteryId =:lotteryId and phone = :phone";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("lotteryId", lotteryId);
		query.setParameter("phone", phone);
		List<ILiveLotteryParktake> list = query.list();
		return list;
	}

	@Override
	public void saveUser(ILiveLotteryParktake iLiveLotteryParktake) {
		this.getSession().save(iLiveLotteryParktake);
	}

	@Override
	public List<ILiveLotteryParktake> getList(Integer isPrize, String search, Long lotteryId) {
		Finder finder = Finder.create("from ILiveLotteryParktake where lotteryId =:lotteryId");
		finder.setParam("lotteryId", lotteryId);
		if(isPrize != null && isPrize==1) {
			finder.append(" and isPrize = :isPrize");
			finder.setParam("isPrize", isPrize);
		}
		if (search!=null&&!"".equals(search)) {
			finder.append(" and (userName like :search or phone like :search)");
			finder.setParam("search", "%"+search+"%");
		}
		finder.append(" order by id DESC");
		return find(finder);
	}

}
