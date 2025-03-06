package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveRedPacketDao;
import com.bvRadio.iLive.iLive.entity.ILiveRedPacket;
import com.bvRadio.iLive.iLive.entity.DocumentPicture;
import com.bvRadio.iLive.iLive.manager.ILiveRedPacketMng;
import com.bvRadio.iLive.iLive.manager.DocumentPictureMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service
@Transactional
public class ILiveRedPacketMngImpl implements ILiveRedPacketMng {

	@Autowired
	private ILiveRedPacketDao managerDao;
	
	@Autowired
	private ILiveFieldIdManagerMng filedIdMng;
	

	

	@Override
	public Long save(ILiveRedPacket ILiveRedPacket) {
		Long nextId = filedIdMng.getNextId("ilive_red_packet", "packet_id", 1);
		ILiveRedPacket.setPacketId(nextId);
		Timestamp now = new Timestamp(new Date().getTime());
		ILiveRedPacket.setCreateTime(now);
		managerDao.save(ILiveRedPacket);
		return nextId;
	}

	@Override
	public void update(ILiveRedPacket ILiveRedPacket) {
		managerDao.update(ILiveRedPacket);
	}

	@Override
	public void delete(Long id) {
		managerDao.delete(id);
	}

	@Override
	public ILiveRedPacket getById(Long id) {
		ILiveRedPacket doc = managerDao.getById(id);
		if(doc==null) {
			return null;
		}
		
		return doc;
	}

	@Override
	public Pagination getPage(String name, Integer roomId, Integer pageNo,
			Integer pageSize) {
		// TODO Auto-generated method stub
		return managerDao.getPage(name, roomId, pageNo, pageSize);
	}

	@Override
	public List<ILiveRedPacket> getListByRoomId(Integer roomId) {
		// TODO Auto-generated method stub
		return managerDao.getListByRoomId(roomId);
	}

	@Override
	public ILiveRedPacket getIsStart(Integer roomId) {
		// TODO Auto-generated method stub
		return managerDao.getIsStart(roomId);
	}

	
	

}
