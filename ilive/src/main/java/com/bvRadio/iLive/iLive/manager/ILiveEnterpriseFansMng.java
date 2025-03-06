package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.action.front.vo.AppConcernVo;
import com.bvRadio.iLive.iLive.action.front.vo.AppILiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseFans;

public interface ILiveEnterpriseFansMng {

	public List<Long> getListByEnterpriseId(Integer enterpriseId);
	
	public Pagination getPage(String queryNum, Integer pageNo, Integer pageSize);

	public Pagination getPageBlack(String queryNum, Integer pageNo, Integer pageSize);

	public boolean addEnterpriseConcern(Integer enterpriseId, Long userId);

	public ILiveEnterpriseFans findEnterpriseFans(Integer enterpriseId, Long userId);

	public boolean isExist(Integer enterpriseId, Long userId);

	public void delFans(Long id);

	public void letblack(Long id);

	public int getFansNum(Integer enterpriseId);

	public void deleteEnterpriseConcern(Integer enterpriseId, Long userId);

	public void deleteEnterpriseConcern(ILiveEnterpriseFans fans);

	public List<Integer> getMyEnterprise(Long userId);

	public List<AppConcernVo> getMyConvernLive(Long userId, int pageNo, int pageSize);

	public void updateEnterFans(ILiveEnterpriseFans entFans);

	public boolean isblack(long userId, Integer enterpriseId);

	public Pagination getPage(String queryNum, Integer enterpriseId, Integer pageNo, int i);

	public Pagination getPageBlack(String queryNum, Integer enterpriseId, Integer pageNo, Integer pageSize);


}
