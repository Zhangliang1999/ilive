package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveFileWhiteBillDao;
import com.bvRadio.iLive.iLive.entity.ILiveFileWhiteBill;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveFileWhiteBillMng;

public class ILiveFileWhiteBillMngImpl implements ILiveFileWhiteBillMng {

	@Autowired
	private ILiveFileWhiteBillDao iLiveFileWhiteBillDao;

	@Autowired
	private ILiveFieldIdManagerMng iLiveFieldIdManagerMng;

	@Override
	public boolean checkUserInWhiteList(String phone, Long fileId) {
		return iLiveFileWhiteBillDao.checkUserInWhiteList(phone, fileId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ILiveFileWhiteBill> getFileWhiteBills(Long fileId) {
		return iLiveFileWhiteBillDao.getFileWhiteBills(fileId);
	}

	@Override
	public List<ILiveFileWhiteBill> distinctUserPhone(String[] phoneNums, Long fileId) {
		return null;
	}

	@Override
	@Transactional
	public void rebuildFileWhite(String[] phoneNums, Long fileId) {
		iLiveFileWhiteBillDao.deleteFromFileId(fileId);
		List<ILiveFileWhiteBill> bills = new ArrayList<>();
		ILiveFileWhiteBill bill = null;
		Long billIdEnd = iLiveFieldIdManagerMng.getNextId("ilive_file_whitebill", "bill_id", phoneNums.length);
		Long firstIdEnd = billIdEnd - phoneNums.length;
		for (String phoneNum : phoneNums) {
			bill = new ILiveFileWhiteBill();
			bill.setBillId(firstIdEnd);
			bill.setFileId(fileId);
			bill.setPhoneNum(phoneNum);
			firstIdEnd++;
			bills.add(bill);
		}
		iLiveFileWhiteBillDao.batchSavePhoneNums(bills);
	}

	@Override
	public Pagination getPage(String queryNum, Integer pageNo, Integer pageSize, Long fileId) {
		
		return iLiveFileWhiteBillDao.getPage(queryNum, pageNo, pageSize, fileId);
	}

	@Override
	public void clearViewWhiteBill(Long fileId) {
		iLiveFileWhiteBillDao.clearViewWhiteBill(fileId);
	}

	@Override
	@Transactional
	public void save(ILiveFileWhiteBill bill) {
		iLiveFileWhiteBillDao.save(bill);
	}

	@Override
	public void deleteById(Long whitebillId) {
		iLiveFileWhiteBillDao.deleteById(whitebillId);
	}

	@Override
	public List<ILiveFileWhiteBill> getAllViewWhiteBilll(String queryNum, Long fileId) {
		
		return iLiveFileWhiteBillDao.getAllViewWhiteBilll(queryNum, fileId);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void batchInsertUser(List<Object[]> readXlsx, UserBean user, Long fileId) {
        int count = 0;
		
		Map<String, ILiveFileWhiteBill> map=new HashMap<String, ILiveFileWhiteBill>();
		for (Object[] objArr : readXlsx) {
			count++;
			if (count == 1) {
				continue;
			}
			ILiveFileWhiteBill viewWhite = new ILiveFileWhiteBill();
			viewWhite.setExportTime(new Timestamp(System.currentTimeMillis()));
			long billId = iLiveFieldIdManagerMng.getNextId("ilive_file_whitebill", "bill_id", 1);
			viewWhite.setFileId(fileId);
			viewWhite.setBillId(billId);
			boolean exsit = true;
			for (int i = 0; i < objArr.length; i++) {
				Object object = objArr[i];
				if (i == 0) {
					String phoneNum = (String) object;
					boolean flag= checkUserInWhiteList(phoneNum, fileId);
					if (!flag) {
					} else {
						
							//exsit=false;
						
						List<ILiveFileWhiteBill> list=getAllViewWhiteBilll(phoneNum,fileId);
							iLiveFileWhiteBillDao.deleteById(list.get(0).getBillId());
						
						
					}
					viewWhite.setPhoneNum(phoneNum);
				} else if (i == 1) {
					viewWhite.setUserName((String) object);
				}
			}
			if (exsit) {
				map.put(viewWhite.getPhoneNum(), viewWhite);
				
			}

		}
		addWhite(map);
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void addWhite(Map<String, ILiveFileWhiteBill> map) {
		if (map!=null) {
			
			for (ILiveFileWhiteBill whiteBill : map.values()) { 
				iLiveFileWhiteBillDao.save(whiteBill);
				}
		}
	}

}
