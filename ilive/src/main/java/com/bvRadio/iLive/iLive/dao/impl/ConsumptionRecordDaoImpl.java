package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.Constants;
import com.bvRadio.iLive.iLive.dao.ConsumptionRecordDao;
import com.bvRadio.iLive.iLive.entity.ConsumptionRecordBean;
import com.bvRadio.iLive.iLive.entity.UserBean;
@Repository
public class ConsumptionRecordDaoImpl extends HibernateBaseDao<ConsumptionRecordBean, Long> implements ConsumptionRecordDao {

	@Override
	public Pagination selectComsumptionRecordByPage(Integer pageNo,
			Integer pageSize,Integer consumption_type, Integer user_id, Integer admin_id) throws Exception {
		Finder finder =Finder.create("select bean from ConsumptionRecordBean bean");
		finder.append(" where 1=1 ");
		if(consumption_type!=null){
			finder.append(" and bean.consumption_type="+consumption_type);
		}
		if(user_id!=null){
			finder.append(" and bean.user_id="+user_id);
		}
		if(admin_id!=null){
			finder.append(" and bean.admin_id="+admin_id);
		}
		finder.append(" order by bean.consumption_id desc ");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public ConsumptionRecordBean insertConsumptionRecordBean(
			ConsumptionRecordBean consumptionRecordBean) throws Exception {
		getSession().save(consumptionRecordBean);
		return consumptionRecordBean;
	}

	@Override
	protected Class<ConsumptionRecordBean> getEntityClass() {
		return ConsumptionRecordBean.class;
	}

	@Override
	public ConsumptionRecordBean selectConsumptionRecordBeanByID(
			long consumption_id) throws Exception {
		ConsumptionRecordBean consumptionRecordBean = get(consumption_id);
		return consumptionRecordBean;
	}

	@Override
	public void delectConsumptionRecordBean(
			ConsumptionRecordBean consumptionRecordBean) throws Exception {
		getSession().delete(consumptionRecordBean);
		
	}

	@Override
	public Pagination selectPersonalComsumptionRecordByPage(Integer pageNo,
			Integer pageSize, Integer consumption_type, UserBean userBean)
			throws Exception {
		Finder finder =Finder.create("select bean from ConsumptionRecordBean bean");
		if(consumption_type!=null){
			if(consumption_type.equals(Constants.CONSUMPTION_TYPE_IN)){
				finder.append(" where bean.admin_id="+userBean.getUserId());	
			}else{
				finder.append(" where bean.user_id="+userBean.getUserId());	
			}
		}else{
			finder.append(" where bean.user_id="+userBean.getUserId()+" or bean.admin_id="+userBean.getUserId());
		}
		finder.append(" order by bean.consumption_id desc ");
		return find(finder, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsumptionRecordBean> selectConsumptionRecordByOut(Integer user_id) throws Exception {
		Finder finder =Finder.create("select bean from ConsumptionRecordBean bean");
		finder.append(" where bean.user_id="+user_id);
		return find(finder);
	}

}
