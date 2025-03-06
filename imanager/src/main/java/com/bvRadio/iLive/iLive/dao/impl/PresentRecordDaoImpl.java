package com.bvRadio.iLive.iLive.dao.impl;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.PresentRecordDao;
import com.bvRadio.iLive.iLive.entity.PresentRecordBean;
import com.bvRadio.iLive.iLive.entity.UserBean;
@Repository
public class PresentRecordDaoImpl extends HibernateBaseDao<PresentRecordBean, Long> implements
		PresentRecordDao {

	@Override
	public Pagination getPagination(int pageNo, int pageSize,Integer present_type, Integer keyword) throws Exception {
		Finder finder = Finder.create("select bean from PresentRecordBean bean");
		finder.append(" where 1=1 ");
		if(present_type!=null){
			finder.append(" and bean.present_type="+present_type);
		}
		if(keyword!=null){
			finder.append(" and (bean.user_id like '%"+ keyword +"%' or  bean.payment_order_number like '%"+keyword+"%')" );
		}
		finder.append(" order by bean.present_id desc ");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public PresentRecordBean insertRechargeRecordBean(
			PresentRecordBean presentRecordBean) throws Exception {
		getSession().save(presentRecordBean);
		return presentRecordBean;
	}

	@Override
	protected Class<PresentRecordBean> getEntityClass() {
		return PresentRecordBean.class;
	}

	@Override
	public PresentRecordBean selectPresentRecordByPresentId(long present_id)
			throws Exception {
		PresentRecordBean presentRecordBean = get(present_id);
		return presentRecordBean;
	}

	@Override
	public void updatePresentRecord(PresentRecordBean presentRecordBean) {
		getSession().update(presentRecordBean);
	}

	@Override
	public Pagination getPersonalPagination(int pageNo, int pageSize,
			Integer present_type, UserBean userBean)
			throws Exception {
		Finder finder = Finder.create("select bean from PresentRecordBean bean");
		if(present_type!=null){
			finder.append(" where bean.user_id="+userBean.getUserId()+" and bean.present_type="+present_type);
		}else{
			finder.append(" where bean.user_id="+userBean.getUserId());
		}
		finder.append(" order by bean.present_id desc ");
		return find(finder, pageNo, pageSize);
	}

}
