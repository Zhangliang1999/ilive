package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.Constants;
import com.bvRadio.iLive.iLive.config.AlipayConfig;
import com.bvRadio.iLive.iLive.dao.ILiveFieldIdManagerDao;
import com.bvRadio.iLive.iLive.dao.PresentRecordDao;
import com.bvRadio.iLive.iLive.dao.UserBalancesDao;
import com.bvRadio.iLive.iLive.entity.PresentRecordBean;
import com.bvRadio.iLive.iLive.entity.UserBalancesBean;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.PresentRecordMng;

@Service
@Transactional
public class PresentRecordMngImpl implements PresentRecordMng {
	
	@Autowired
	private PresentRecordDao presentRecordDao;
	
	@Autowired
	private UserBalancesDao userBalancesDao;
	
	@Autowired
	private ILiveFieldIdManagerDao iLiveFieldIdManagerDao;

	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(Integer pageNo, Integer pageSize,Integer present_type, Integer keyword) {
		Pagination pagination = new Pagination();
		try {
			pagination = presentRecordDao.getPagination(pageNo, pageSize,present_type,keyword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagination;
	}

	@Override
	@Transactional(readOnly = true)
	public PresentRecordBean findPresentRecordByPresentId(long present_id) {
		PresentRecordBean presentRecordBean =  new PresentRecordBean();
		try {
			presentRecordBean = presentRecordDao.selectPresentRecordByPresentId(present_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return presentRecordBean;
	}

	@Override
	public String updatePresentRecordBeanBypresent_type(Integer present_type,
			long present_id) {
		String msg = "null";
		try {
			PresentRecordBean presentRecordBean = presentRecordDao.selectPresentRecordByPresentId(present_id);
			if(present_type==Constants.PRESENT_YES){
				Integer present_income = presentRecordBean.getPresent_income();
				Integer user_id = presentRecordBean.getUser_id();
				UserBalancesBean userBalancesBean = userBalancesDao.selectUserBalancesBean(user_id);
				if(present_income.equals(Constants.PRESENT_Income_red)){
					System.out.println("无法红包提现");
				}else if(present_income.equals(Constants.PRESENT_Income_user)){
					//收益提现
					double present_rmb = presentRecordBean.getPresent_rmb();
					double commission_income = userBalancesBean.getCommission_income();
					if(commission_income>=present_rmb){
						//提现开始
						String replace = UUID.randomUUID().toString().replace("-", "");
						AlipayFundTransToaccountTransferResponse response = AlipayConfig.transToaccountTransfer(replace,  presentRecordBean.getPresent_account(), String.valueOf(present_rmb),presentRecordBean.getReal_name());
						boolean b = false;
						if(response.isSuccess()){
							b=true;
							String orderId = response.getOrderId();
							presentRecordBean.setPayment_order_number(orderId);
							System.out.println("调用成功");
						} else {
							b=false;
							System.out.println("调用失败");
						}
						if(b){
							userBalancesBean.setCommission_income(commission_income-present_rmb);
							userBalancesDao.updateUserBalancesBean(userBalancesBean);
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String str= format.format(new Date());
							presentRecordBean.setPresent_endTime(Timestamp.valueOf(str));
							presentRecordBean.setPresent_type(Constants.PRESENT_YES);
							presentRecordDao.updatePresentRecord(presentRecordBean);
							msg="success";
						}else{
							msg="error";
						}
					}else{
						msg="error";
					}
				}else{
					System.out.println("提现方式未知");
				}
			}else{
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String str= format.format(new Date());
				presentRecordBean.setPresent_type(Constants.PRESENT_NO);
				presentRecordBean.setPresent_endTime(Timestamp.valueOf(str));
				presentRecordDao.updatePresentRecord(presentRecordBean);
				msg="success";
			}
		} catch (Exception e) {
			System.out.println("操作异常：");
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public Pagination getPersonalPage(Integer pageNo, Integer pageSize,
			Integer present_type, UserBean userBean) {
		Pagination pagination = new Pagination();
		try {
			pagination = presentRecordDao.getPersonalPagination(pageNo, pageSize, present_type, userBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagination;
	}

	@Override
	public String addPresentRecordBean(PresentRecordBean presentRecordBean) {
		String msg = "";
		try {
			long present_id = iLiveFieldIdManagerDao.getNextId("iLive_present_record", "present_id", 1);
			presentRecordBean.setPresent_id(present_id);
			presentRecordDao.insertRechargeRecordBean(presentRecordBean);
			msg="success";
		} catch (Exception e) {
			msg="error";
			System.out.println("提现申请失败");
			e.printStackTrace();
		}
		return msg;
	}

}
