package com.bvRadio.iLive.core.dao;

import java.util.List;

import com.bvRadio.iLive.common.hibernate3.Updater;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.core.entity.UnifiedUser;

public interface UnifiedUserDao {
	public UnifiedUser getByUsername(String username);

	public List<UnifiedUser> getByEmail(String email);

	public int countByEmail(String email);

	public Pagination getPage(int pageNo, int pageSize);

	public UnifiedUser findById(Integer id);

	public UnifiedUser save(UnifiedUser bean);

	public UnifiedUser updateByUpdater(Updater<UnifiedUser> updater);

	public UnifiedUser deleteById(Integer id);

	public int countByMobile(String mobile);

	public UnifiedUser getByMobile(String mobile);

	public UnifiedUser getByThirdId(String thirdId);

	public UnifiedUser getByThirdIdAndType(String openId, int thirdTypeWeixin);
}