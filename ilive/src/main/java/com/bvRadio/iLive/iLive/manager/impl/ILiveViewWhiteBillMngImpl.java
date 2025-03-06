package com.bvRadio.iLive.iLive.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.action.front.vo.AppILiveRoom;
import com.bvRadio.iLive.iLive.action.front.vo.ILiveAppEnterprise;
import com.bvRadio.iLive.iLive.dao.ILiveEnterpriseMemberDao;
import com.bvRadio.iLive.iLive.dao.ILiveViewWhiteBillDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseWhiteBill;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.ILiveViewWhiteBill;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewWhiteBillMng;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

@Transactional
public class ILiveViewWhiteBillMngImpl implements ILiveViewWhiteBillMng {

	@Autowired
	private ILiveViewWhiteBillDao iLiveViewWhiteBillDao;

	@Autowired
	private ILiveFieldIdManagerMng iLivefieldIdManagerMng;

	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;

	@Autowired
	private ILiveServerAccessMethodMng iLiveServerAccessMethodMng;

	@Autowired
	private ILiveEnterpriseMemberDao iLiveEnterpriseMemberDao;
	
	@Override
	@Transactional(readOnly = true)
	public boolean checkUserInWhiteList(String userId, Long liveEventId) {
		return iLiveViewWhiteBillDao.checkUserInWhiteList(userId, liveEventId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ILiveViewWhiteBill> getAllViewWhiteBill(Long liveEventId) {
		return iLiveViewWhiteBillDao.getAllViewWhiteBill(liveEventId);
	}

	@Override
	@Transactional
	public List<ILiveViewWhiteBill> distinctUserPhone(String[] phoneNums, Long iLiveEventId) {
		try{
			int length = phoneNums.length;
			//删除此直播的历史白名单
			iLiveViewWhiteBillDao.clearViewWhiteBill(iLiveEventId);
			
			// 循环次数
			/*if (length < 5000) {*/
				List<String> distinctUserPhone = iLiveViewWhiteBillDao.distinctUserPhone(phoneNums, iLiveEventId);
				if (distinctUserPhone != null && !distinctUserPhone.isEmpty()) {
					List<ILiveViewWhiteBill> billList = new ArrayList<>();
					ILiveViewWhiteBill whiteBill = null;
					for (String phone : distinctUserPhone) {
						whiteBill = new ILiveViewWhiteBill();
						long billId = iLivefieldIdManagerMng.getNextId("ilive_view_whitebill", "bill_id", 1);
						whiteBill.setBillId(billId);
						whiteBill.setPhoneNum(phone);
						whiteBill.setLiveEventId(iLiveEventId);
						List<ILiveEnterpriseWhiteBill> list = iLiveEnterpriseMemberDao.getEnterpriseWhite(phone);
						if(list != null && list.size()>0){
							whiteBill.setUserName(list.get(0).getUserName());
						}
						
						billList.add(whiteBill);
					}
					return billList;
				}
			/*} else {
				// 次数剩余
				int circleAfford = length % 30;
				if (circleAfford == 0) {
					int circleTime = length / 30;
					List<ILiveViewWhiteBill> bigMapList = new ArrayList<>();
					for (int i = 0; i < circleTime; i++) {
						List<String> distinctUserPhone = iLiveViewWhiteBillDao.distinctUserPhone(phoneNums, iLiveEventId);
						List<ILiveViewWhiteBill> billList = new ArrayList<>();
						ILiveViewWhiteBill whiteBill = null;
						for (String phone : distinctUserPhone) {
							whiteBill = new ILiveViewWhiteBill();
							whiteBill.setPhoneNum(phone);
							whiteBill.setLiveEventId(iLiveEventId);
							List<ILiveEnterpriseWhiteBill> list = iLiveEnterpriseMemberDao.getEnterpriseWhite(phone);
							if(list != null && list.size()>0){
								whiteBill.setUserName(list.get(0).getUserName());
							}
							billList.add(whiteBill);
						}
						bigMapList.addAll(billList);
					}
					return bigMapList;
				} else {
					int circleTime = length / 30;
					List<ILiveViewWhiteBill> bigMapList = new ArrayList<>();
					for (int i = 0; i < circleTime; i++) {
						List<String> distinctUserPhone = iLiveViewWhiteBillDao.distinctUserPhone(phoneNums, iLiveEventId);
						List<ILiveViewWhiteBill> billList = new ArrayList<>();
						ILiveViewWhiteBill whiteBill = null;
						for (String phone : distinctUserPhone) {
							whiteBill = new ILiveViewWhiteBill();
							whiteBill.setPhoneNum(phone);
							whiteBill.setLiveEventId(iLiveEventId);
							List<ILiveEnterpriseWhiteBill> list = iLiveEnterpriseMemberDao.getEnterpriseWhite(phone);
							if(list != null && list.size()>0){
								whiteBill.setUserName(list.get(0).getUserName());
							}
							billList.add(whiteBill);
						}
						bigMapList.addAll(billList);
					}
					String[] affordArr = new String[circleAfford];
					int lenght = phoneNums.length;
					for (int i = 0; i < lenght; i++) {
						try {
							affordArr[i] = phoneNums[lenght - circleAfford];
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					List<String> distinctUserPhone = iLiveViewWhiteBillDao.distinctUserPhone(affordArr, iLiveEventId);
					List<ILiveViewWhiteBill> billList = new ArrayList<>();
					ILiveViewWhiteBill whiteBill = null;
					for (String phone : distinctUserPhone) {
						whiteBill = new ILiveViewWhiteBill();
						whiteBill.setPhoneNum(phone);
						whiteBill.setLiveEventId(iLiveEventId);
						List<ILiveEnterpriseWhiteBill> list = iLiveEnterpriseMemberDao.getEnterpriseWhite(phone);
						if(list != null && list.size()>0){
							whiteBill.setUserName(list.get(0).getUserName());
						}
						billList.add(whiteBill);
					}
					bigMapList.addAll(billList);
					return bigMapList;
					// if (bigMapList != null && !bigMapList.isEmpty()) {
					// this.batchInsertUserPhone(bigMapList);
					// }
				}
			}*/
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	@Transactional
	public void batchInsertUserPhone(List<ILiveViewWhiteBill> billList) {
		iLiveViewWhiteBillDao.batchInsertUserPhone(billList);
	}

	@Override
	public void saveIliveViewWhiteBill(ILiveViewWhiteBill bill) {
		iLiveViewWhiteBillDao.saveIliveViewWhiteBill(bill);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AppILiveRoom> getMyInviteLive(Long userId, String phoneNum, int pageNo, int pageSize) {
		Pagination pagination = iLiveViewWhiteBillDao.getMyInviteLive(userId, phoneNum, pageNo, pageSize);
		List<AppILiveRoom> liveRooms = new ArrayList<>();
		if (pagination != null) {
			List<ILiveLiveRoom> list = (List<ILiveLiveRoom>) pagination.getList();
			if (list != null && !list.isEmpty()) {
				AppILiveRoom iLiveRoom = null;
				for (ILiveLiveRoom room : list) {
					ILiveEvent event = room.getLiveEvent();
					Integer enterpriseId = room.getEnterpriseId();
					ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
					ILiveAppEnterprise appEnterprise = this.convertEnprise2AppEnterprise(iLiveEnterPrise);
					iLiveRoom = new AppILiveRoom();
					iLiveRoom = this.convertEvent2AppRoom(event, iLiveRoom);
					iLiveRoom.setAppEnterprise(appEnterprise);
					liveRooms.add(iLiveRoom);
				}
			}
		}
		return liveRooms;
	}

	private ILiveAppEnterprise convertEnprise2AppEnterprise(ILiveEnterprise enterprise) {
		ILiveAppEnterprise appEnterprise = new ILiveAppEnterprise();
		appEnterprise.setCertStatus(enterprise.getCertStatus());
		appEnterprise.setEnterpriseDesc(StringPatternUtil.convertEmpty(enterprise.getEnterpriseDesc()));
		appEnterprise.setConcernTotal(0L);
		appEnterprise.setEnterpriseId(enterprise.getEnterpriseId());
		appEnterprise.setEnterpriseName(StringPatternUtil.convertEmpty(enterprise.getEnterpriseName()));
		appEnterprise.setEnterpriseImg(StringPatternUtil.convertEmpty(enterprise.getEnterpriseImg()));
		String defaultEnterpriseServerId = ConfigUtils.get("defaultEnterpriseServerId");
		ILiveServerAccessMethod serverGroup = iLiveServerAccessMethodMng
				.getAccessMethodBySeverGroupId(Integer.parseInt(defaultEnterpriseServerId));
		String homePageUrl = serverGroup.getH5HttpUrl() + "/home/index.html?enterpriseId="
				+ enterprise.getEnterpriseId();
		appEnterprise.setHomePageUrl(homePageUrl);
		return appEnterprise;
	}

	private AppILiveRoom convertEvent2AppRoom(ILiveEvent event, AppILiveRoom iLiveRoom) {
		iLiveRoom.setLiveStatus(event.getLiveStatus());
		iLiveRoom.setRoomDesc(event.getLiveDesc());
		iLiveRoom.setRoomId(event.getRoomId());
		iLiveRoom.setRoomImg(event.getConverAddr());
		iLiveRoom.setRoomName(event.getLiveTitle());
		return iLiveRoom;
	}

	@Override
	@Transactional
	public void batchInsertUser(List<Object[]> readXlsx, Long iliveEvent) {

	}

	@Override
	public void deleteById(String phoneNum, Long liveEventId) {
		iLiveViewWhiteBillDao.deleteById(phoneNum, liveEventId);
	}

	@Override
	public List<ILiveViewWhiteBill> getAllViewWhiteBilll(String queryNum, Long liveEventId) {
		return iLiveViewWhiteBillDao.getAllViewWhiteBilll(queryNum, liveEventId);
	}

	@Override
	public Pagination getPage(String queryNum, Integer pageNo, Integer pageSize, Long iLiveEventId) {
		return iLiveViewWhiteBillDao.getPage(queryNum, pageNo, pageSize, iLiveEventId);
	}

	@Override
	public void updateEventId(long oldLiveEventId, Long saveIliveMng) {
		iLiveViewWhiteBillDao.updateEventId(oldLiveEventId, saveIliveMng);
	}

	

}
