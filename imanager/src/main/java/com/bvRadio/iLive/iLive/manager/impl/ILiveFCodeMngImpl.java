package com.bvRadio.iLive.iLive.manager.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveFCodeDao;
import com.bvRadio.iLive.iLive.dao.ILiveFCodeDetailDao;
import com.bvRadio.iLive.iLive.entity.ILiveFCode;
import com.bvRadio.iLive.iLive.entity.ILiveFCodeDetail;
import com.bvRadio.iLive.iLive.manager.ILiveFCodeMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;

@Service
@Transactional
public class ILiveFCodeMngImpl implements ILiveFCodeMng {
	
	@Autowired
	private ILiveFCodeDao liveFCodeDao;
	@Autowired
	private ILiveFieldIdManagerMng filedIdMng;
	@Autowired
	private ILiveFCodeDetailDao liveFCodeDetail;

	@Override
	public Long saveFCode(ILiveFCode liveFCode) {
		//XX公司F码
		Long nextId = filedIdMng.getNextId("ilive_fcode", "code_id", 1);
		liveFCode.setCodeId(nextId);
		liveFCode.setIsDelete(0);
		liveFCode.setUseNum(0);
		//添加详细的F码
		int num = liveFCode.getCodeNum();
		//产生随机F码
		String F="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		List<ILiveFCodeDetail> list = new LinkedList<>();
		for(int i=0;i<num;i++) {
			ILiveFCodeDetail codeDetail = new ILiveFCodeDetail();
			Long nextFCodeId = filedIdMng.getNextId("ilive_fcode_detail", "id", 1);
			codeDetail.setId(nextFCodeId);
			codeDetail.setCodeId(nextId);
			codeDetail.setIsUsed(0);
			StringBuffer buffer = new StringBuffer();
			for(int j=0;j<12;j++) {
				int ran = random.nextInt(35);
				buffer.append(F.charAt(ran));
			}
			codeDetail.setCode(buffer.toString());
			list.add(codeDetail);
		}
		liveFCodeDao.saveFCode(liveFCode);
		liveFCodeDetail.saveListFCode(list);
		return nextId;
	}

	@Override
	public List<ILiveFCode> getAllFCode() {
		return liveFCodeDao.getAllFCode();
	}

	@Override
	public void deleteByCodeId(Long codeId) {
		ILiveFCode liveFCode = liveFCodeDao.getById(codeId);
		liveFCode.setIsDelete(1);
		liveFCodeDao.deleteByCodeId(liveFCode);
	}

	@Override
	public ILiveFCode getFCodeById(Long codeId) {
		return liveFCodeDao.getById(codeId);
	}

	@Override
	public List<ILiveFCodeDetail> getDetailByCodeId(Long codeId) {
		return liveFCodeDao.getDetailByCodeId(codeId);
	}

	@Override
	public Pagination getMainPager(String userName, int cpn, int pageSize) {
		return liveFCodeDao.getMainPager( userName,  cpn,  pageSize);
	}

}
