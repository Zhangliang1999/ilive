package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveSendMsg;

/**
 * 礼物管理
 * @author YanXL
 *
 */
public interface ILiveSendMsgDao {
	
	public List<ILiveSendMsg> selectILiveSubById(Long roomId);

	public void save(ILiveSendMsg iLiveSendMsg);
	
	public Long selectMaxId();

	public void delete(Long roomId);

	public void update(ILiveSendMsg iLiveSendMsg);

	public ILiveSendMsg getRecode(Long roomId);

	public Pagination selectILiveRoomThirdPage(Integer pageNo, Integer pageSize, Integer roomId) throws Exception;
}
