package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveSendMsg;
import com.bvRadio.iLive.iLive.entity.ILiveSubLevel;

public interface ILiveSendMsgMng {
	public List<ILiveSendMsg> selectILiveSubById(Long roomId);

	public void save(ILiveSendMsg iLiveSendMsg);
	
	public Long selectMaxId();

	public void delete(Long roomId);

	public void update(ILiveSendMsg iLiveSendMsg);

	public ILiveSendMsg  getRecode(Long roomId);

	public Pagination selectILiveRoomThirdPage(Integer pageNo, Integer pageSize,
			Integer roomId);
	
}
