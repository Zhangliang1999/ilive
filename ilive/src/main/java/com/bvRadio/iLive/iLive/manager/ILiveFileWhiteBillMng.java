package com.bvRadio.iLive.iLive.manager;

import java.util.List;
import java.util.Map;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveFileWhiteBill;
import com.bvRadio.iLive.iLive.entity.UserBean;

public interface ILiveFileWhiteBillMng {

	
	/**
	 * 
	 * @param username
	 * @param fileId
	 * @return
	 */
	boolean checkUserInWhiteList(String username, Long fileId);

	/**
	 * 
	 * @param phoneNums
	 * @param fileId
	 * @return
	 */
	List<ILiveFileWhiteBill> distinctUserPhone(String[] phoneNums, Long fileId);

	
	/**
	 * 
	 * @param phoneNums
	 * @param fileId
	 */
	void rebuildFileWhite(String[] phoneNums, Long fileId);
	
	
	/**
	 * 
	 * @param fileId
	 * @return
	 */
	public List<ILiveFileWhiteBill> getFileWhiteBills(Long fileId);

	Pagination getPage(String queryNum, Integer pageNo, Integer pageSize, Long fileId);

	void clearViewWhiteBill(Long fileId);

	void save(ILiveFileWhiteBill bill);

	void deleteById(Long whitebillId);

	List<ILiveFileWhiteBill> getAllViewWhiteBilll(String queryNum, Long fileId);

	void batchInsertUser(List<Object[]> readXlsx, UserBean user, Long fileId);
	void addWhite(Map<String, ILiveFileWhiteBill> map);

}
