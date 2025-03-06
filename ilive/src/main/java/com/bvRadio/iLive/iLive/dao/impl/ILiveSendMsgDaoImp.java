package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveSendMsgDao;
import com.bvRadio.iLive.iLive.dao.ILiveSubLevelDao;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveSendMsg;
import com.bvRadio.iLive.iLive.entity.ILiveSubLevel;
@Repository
public class ILiveSendMsgDaoImp  extends HibernateBaseDao<ILiveSendMsg, Long> implements  ILiveSendMsgDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveSendMsg> selectILiveSubById(Long roomId) {
		Finder finder = Finder.create("from ILiveSendMsg where roomId ="+roomId);
		return this.find(finder);
	}

	@Override
	protected Class<ILiveSendMsg> getEntityClass() {
		// TODO Auto-generated method stub
		return ILiveSendMsg.class;
	}

	@Override
	public void save(ILiveSendMsg iLiveSubLevel) {
		this.getSession().save(iLiveSubLevel);
		
	}

	@Override
	public Long selectMaxId() {
		String hql = "from ILiveSendMsg  order by id desc";
		Finder finder = Finder.create(hql);
		List<ILiveSendMsg> find = this.find(finder);
		if (find != null && !find.isEmpty()) {
			Long id=find.get(0).getId();
			return find.get(0).getId()+1;
		}
		return null;
	}

	@Override
	public void delete(Long roomId) {
		List iLiveSendMsg = this.find("from ILiveSendMsg where roomId=?", roomId);
		if(iLiveSendMsg!=null||iLiveSendMsg.size()>0) {
			for(int i=0;i<iLiveSendMsg.size();i++) {
				ILiveSendMsg level=(ILiveSendMsg)iLiveSendMsg.get(i);
				getSession().delete(level);
			}
		}
		
		
	}

	@Override
	public void update(ILiveSendMsg iLiveSendMsg) {
		// TODO Auto-generated method stub
		this.getSession().update(iLiveSendMsg);
	}

	@Override
	public ILiveSendMsg getRecode(Long roomId) {
		if (roomId == null) {
			return null;
		}
		return (ILiveSendMsg) this.findUnique("from ILiveSendMsg where roomId=?", roomId);
	
	}

	@Override
	public Pagination selectILiveRoomThirdPage(Integer pageNo, Integer pageSize, Integer roomId)throws Exception {
		Finder finder = Finder.create("from ILiveSendMsg bean where bean.roomId=:roomId");
		finder.setParam("roomId", roomId.longValue());
		return find(finder, pageNo, pageSize);
	}

	
	
}
