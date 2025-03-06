package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveFileWhiteBill;

public interface ILiveFileWhiteBillDao {

	boolean checkUserInWhiteList(String phone, Long fileId);

	void deleteFromFileId(Long fileId);

	void batchSavePhoneNums(List<ILiveFileWhiteBill> bills);

	List<ILiveFileWhiteBill> getFileWhiteBills(Long fileId);
	Pagination getPage(String queryNum, Integer pageNo, Integer pageSize, Long fileId);
	void clearViewWhiteBill(Long fileId);
	void save(ILiveFileWhiteBill bill);
	void deleteById(Long whitebillId);
	List<ILiveFileWhiteBill> getAllViewWhiteBilll(String queryNum, Long fileId);

}
