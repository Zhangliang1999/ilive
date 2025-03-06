package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.action.front.vo.AppConcernVo;
import com.bvRadio.iLive.iLive.action.front.vo.AppILiveRoom;
import com.bvRadio.iLive.iLive.action.front.vo.ILiveAppEnterprise;
import com.bvRadio.iLive.iLive.dao.ILiveEnterpriseFansDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseFans;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseFansMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

@Service
public class ILiveEnterpriseFansMngImpl implements ILiveEnterpriseFansMng {

	@Autowired
	private ILiveEnterpriseFansDao fansDao;

	@Autowired
	private ILiveFieldIdManagerMng filedIdMng;

	@Autowired
	private ILiveLiveRoomMng iLiveRoomMng;

	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;

	@Autowired
	private ILiveServerAccessMethodMng serverAccessMng;
	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	@Transactional
	@Override
	public Pagination getPage(String queryNum, Integer pageNo, Integer pageSize) {
		Pagination pagination =fansDao.getPage(queryNum, pageNo, pageSize);
		try {
			List<ILiveEnterpriseFans> iLiveEnterpriseFans=(List<ILiveEnterpriseFans>)pagination.getList();
			for(ILiveEnterpriseFans fans:iLiveEnterpriseFans){
				Long userId=fans.getUserId();
				ILiveManager iLiveManager=iLiveManagerMng.selectILiveManagerById(userId);
				if(iLiveManager==null){
					fans.setNailName("");
				}else{
				fans.setNailName(iLiveManager.getNailName());
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return pagination;
	}

	@Transactional
	@Override
	public Pagination getPageBlack(String queryNum, Integer pageNo, Integer pageSize) {
		return fansDao.getPageBlack(queryNum, pageNo, pageSize);
	}

	@Transactional
	@Override
	public boolean addEnterpriseConcern(Integer enterpriseId, Long userId) {
		ILiveEnterpriseFans fans = this.findEnterpriseFans(enterpriseId, userId);
		if (fans == null) {
			fans = new ILiveEnterpriseFans();
			Long nextId = filedIdMng.getNextId("ilive_enterprise_fans", "id", 1);
			fans.setId(nextId);
			fans.setEnterpriseId(enterpriseId);
			fans.setUserId(userId);
			fans.setIsDel(0);
			fans.setTopFlag(0);
			fans.setForbidState(0);
			fans.setConcernTime(new Timestamp(System.currentTimeMillis()));
			fansDao.addEnterpriseConcern(fans);
			return true;
		} else {
			// if(fans.getIsDel()!=null && fans.getIsDel()==1) {
			// fans.setIsDel(0);
			// fansDao.updateEnterFans(fans);
			// }
			return true;
		}
	}

	@Transactional(readOnly = true)
	@Override
	public ILiveEnterpriseFans findEnterpriseFans(Integer enterpriseId, Long userId) {
		return fansDao.findEnterpriseFans(enterpriseId, userId);
	}

	@Transactional(readOnly = true)
	@Override
	public boolean isExist(Integer enterpriseId, Long userId) {
		return fansDao.isExist(enterpriseId, userId);
	}

	@Transactional
	@Override
	public void delFans(Long id) {
		fansDao.delFans(id);
	}

	@Transactional
	@Override
	public void letblack(Long id) {
		fansDao.letblack(id);
	}

	@Override
	public int getFansNum(Integer enterpriseId) {
		return fansDao.getFansNum(enterpriseId);
	}

	@Override
	@Transactional
	public void deleteEnterpriseConcern(Integer enterpriseId, Long userId) {
		ILiveEnterpriseFans fans = this.findEnterpriseFans(enterpriseId, userId);
		// System.out.println("enterpriseId:" + enterpriseId + "userId:" +
		// userId);
		if (fans != null) {
			// System.out.println("删除了一个fans" + fans.getId());
			this.deleteEnterpriseConcern(fans);
		}
	}

	@Override
	@Transactional
	public void deleteEnterpriseConcern(ILiveEnterpriseFans fans) {
		fansDao.deleteEnterpriseConcern(fans);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Integer> getMyEnterprise(Long userId) {
		return fansDao.getMyEnterprise(userId);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AppConcernVo> getMyConvernLive(Long userId, int pageNo, int pageSize) {
		List<AppConcernVo> concernList = new ArrayList<>();
		Pagination pagination = fansDao.getIliveEnterPriseByUserId(userId, pageNo, pageSize);
		if (pagination != null && !pagination.getList().isEmpty()) {
			AppConcernVo appConcernVo = null;
			List<ILiveEnterpriseFans> list = (List<ILiveEnterpriseFans>) pagination.getList();
			for (ILiveEnterpriseFans fans : list) {
				appConcernVo = new AppConcernVo();
				ILiveAppEnterprise enterpriseApp = new ILiveAppEnterprise();
				Integer enterpriseId = fans.getEnterpriseId();
				ILiveEnterprise enterprise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
				enterpriseApp
						.setEnterpriseImg(enterprise.getEnterpriseImg() == null ? "" : enterprise.getEnterpriseImg());
				enterpriseApp.setEnterpriseName(
						enterprise.getEnterpriseName() == null ? "" : enterprise.getEnterpriseName());
				enterpriseApp.setEnterpriseDesc(
						enterprise.getEnterpriseDesc() == null ? "" : enterprise.getEnterpriseDesc());
				String defaultEnterpriseServerId = ConfigUtils.get("defaultEnterpriseServerId");
				ILiveServerAccessMethod serverGroup = serverAccessMng
						.getAccessMethodBySeverGroupId(Integer.parseInt(defaultEnterpriseServerId));
				String homePageUrl = serverGroup.getH5HttpUrl() + "/home/index.html?enterpriseId="
						+ enterprise.getEnterpriseId();
				enterpriseApp.setHomePageUrl(homePageUrl);
				enterpriseApp.setEnterpriseId(enterpriseId);
				enterpriseApp.setConcernStatus(1);
				Long concernTotal = fansDao.getTotalConcernNum(enterpriseId);
				enterpriseApp.setConcernTotal(concernTotal == null ? 0 : concernTotal);
				appConcernVo.setEnterprise(enterpriseApp);
				Pagination pagination2 = iLiveRoomMng.getTop1LiveByEnterpriseId(enterpriseId);
				if (pagination2 != null) {
					List<ILiveLiveRoom> list2 = (List<ILiveLiveRoom>) pagination2.getList();
					if (list2 != null && !list2.isEmpty()) {
						for (ILiveLiveRoom room : list2) {
							appConcernVo.setRoomNum(1);
							ILiveEvent liveEvent = room.getLiveEvent();
							AppILiveRoom appRoom = new AppILiveRoom();
							appRoom.setRoomId(room.getRoomId());
							appRoom.setLiveStatus(liveEvent.getLiveStatus());
							appRoom.setRoomDesc(liveEvent.getLiveDesc());
							appRoom.setRoomImg(liveEvent.getConverAddr());
							appRoom.setRoomName(liveEvent.getLiveTitle());
							appConcernVo.setIliveRoom(appRoom);
							break;
						}
					} else {
						appConcernVo.setRoomNum(0);
					}
				} else {
					appConcernVo.setRoomNum(0);
				}
				concernList.add(appConcernVo);
			}
		}
		return concernList;
	}

	@Override
	@Transactional
	public void updateEnterFans(ILiveEnterpriseFans entFans) {
		List<ILiveEnterpriseFans> fansList = fansDao.getEnterpriseListByTop(entFans.getUserId());
		if (fansList != null && !fansList.isEmpty()) {
			for (ILiveEnterpriseFans fans : fansList) {
				fans.setTopFlag(0);
				fansDao.updateEnterFans(entFans);
			}
		}
		entFans.setTopFlag(1);
		fansDao.updateEnterFans(entFans);
	}

	@Override
	public List<Long> getListByEnterpriseId(Integer enterpriseId) {
		return fansDao.getListByEnterpriseId(enterpriseId);
	}

	@Override
	public boolean isblack(long userId, Integer enterpriseId) {
		// TODO Auto-generated method stub
		return fansDao.isblack(userId,enterpriseId);
	}

	@Override
	public Pagination getPage(String queryNum, Integer enterpriseId, Integer pageNo, int i) {
		Pagination pagination =fansDao.getPage(queryNum,enterpriseId, pageNo, i);
		try {
			List<ILiveEnterpriseFans> iLiveEnterpriseFans=(List<ILiveEnterpriseFans>)pagination.getList();
			for(ILiveEnterpriseFans fans:iLiveEnterpriseFans){
				Long userId=fans.getUserId();
				ILiveManager iLiveManager=iLiveManagerMng.selectILiveManagerById(userId);
				if(iLiveManager==null){
					fans.setNailName("");
				}else{
				fans.setNailName(iLiveManager.getNailName());
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return pagination;
	}

	@Override
	public Pagination getPageBlack(String queryNum, Integer enterpriseId, Integer pageNo, Integer pageSize) {
		Pagination pagination =fansDao.getPageBlack(queryNum,enterpriseId, pageNo, pageSize);
		try {
			List<ILiveEnterpriseFans> iLiveEnterpriseFans=(List<ILiveEnterpriseFans>)pagination.getList();
			for(ILiveEnterpriseFans fans:iLiveEnterpriseFans){
				Long userId=fans.getUserId();
				ILiveManager iLiveManager=iLiveManagerMng.selectILiveManagerById(userId);
				if(iLiveManager==null){
					fans.setNailName("");
				}else{
				fans.setNailName(iLiveManager.getNailName());
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return pagination;
	}

}
