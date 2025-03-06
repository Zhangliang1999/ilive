package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.CommissionDao;
import com.bvRadio.iLive.iLive.entity.CommissionBean;
@Repository
public class CommissionDaoImpl extends HibernateBaseDao<CommissionBean, Integer> implements CommissionDao {

	@Override
	public Pagination selectCommissionPagination(Integer pageNo,
			Integer pageSize) throws Exception {
		Finder finder = Finder.create("select bean from CommissionBean bean ");
		finder.append(" order by bean.commission_level");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public void insertCommissionBean(CommissionBean commissionBean)
			throws Exception {
		getSession().save(commissionBean);
	}

	@Override
	public void deleteCommissionBean(CommissionBean commissionBean) throws Exception {
		getSession().delete(commissionBean);
	}

	@Override
	protected Class<CommissionBean> getEntityClass() {
		return CommissionBean.class;
	}

	@Override
	public CommissionBean selectCommissionBeanByCommissionId(Integer commission_id) throws Exception {
		return get(commission_id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommissionBean> selectCommissionBeanByMax() throws Exception {
		Finder finder = Finder.create("SELECT bean FROM CommissionBean bean WHERE bean.max_currency=(SELECT MAX(bean2.max_currency) FROM CommissionBean bean2)");
		List<CommissionBean> list = find(finder);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommissionBean> selectCommissionBeanAll() throws Exception {
		Finder finder = Finder.create("SELECT bean FROM CommissionBean bean");
		List<CommissionBean> list = find(finder);
		return list;
	}

}
