package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.action.front.vo.ILiveAppEnterprise;
import com.bvRadio.iLive.iLive.dao.ILiveEnterpriseDao;
import com.bvRadio.iLive.iLive.dao.ILiveEnterpriseFansDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseFans;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseFansMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

@Transactional
@Service
public class ILiveEnterpriseMngImpl implements ILiveEnterpriseMng {

	@Autowired
	private ILiveEnterpriseDao iLiveEnterpriseDao;

	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;

	@Autowired
	private ILiveManagerMng iLiveManagerMng;

	@Autowired
	private ILiveEnterpriseFansMng iLiveEnterpriseFansMng;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private ILiveServerAccessMethodMng serverAccessMng;
	@Autowired
	private ILiveEnterpriseFansDao fansDao;
	@Override
	@Transactional
	public boolean saveILiveEnterprise(ILiveEnterprise iLiveEnterprise) {
		long enterpriseId = fieldIdMng.getNextId("ilive_enterprise", "enterprise_id", 1);
		iLiveEnterprise.setEnterpriseId((int) enterpriseId);
		iLiveEnterprise.setApplyTime(new Timestamp(System.currentTimeMillis()));
		iLiveEnterprise.setEntype(1);
		UserBean user = ILiveUtils.getUser(request);
		Long userId = Long.valueOf(user.getUserId());
		ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
		iLiveManager.setEnterPrise(iLiveEnterprise);
		user.setEnterpriseId((int) enterpriseId);
		ILiveUtils.setUser(request, user);
		return iLiveEnterpriseDao.saveILiveEnterprise(iLiveEnterprise);
	}

	@Override
	@Transactional
	public ILiveEnterprise saveILiveEnterpriseForPhone(ILiveEnterprise iLiveEnterprise) {
		int enterpriseId = fieldIdMng.getNextId("ilive_enterprise", "enterprise_id", 1).intValue();
		iLiveEnterprise.setEnterpriseId((int) enterpriseId);
		iLiveEnterprise.setApplyTime(new Timestamp(System.currentTimeMillis()));
		return iLiveEnterprise;
	}
	@Override
	@Transactional
	public ILiveEnterprise saveILiveEnterpriseForyyt(ILiveEnterprise iLiveEnterprise) {
		int enterpriseId = fieldIdMng.getNextId("ilive_enterprise", "enterprise_id", 1).intValue();
		iLiveEnterprise.setEnterpriseId((int) enterpriseId);
		iLiveEnterprise.setApplyTime(new Timestamp(System.currentTimeMillis()));
		iLiveEnterpriseDao.saveILiveEnterprise(iLiveEnterprise);
		return iLiveEnterprise;
	}
	@Override
	@Transactional(readOnly = true)
	public ILiveEnterprise getILiveEnterPrise(Integer enterpriseId) {
		return iLiveEnterpriseDao.getILiveEnterpriseById(enterpriseId);
	}

	@Override
	public ILiveEnterprise getdefaultEnterprise() {
		return iLiveEnterpriseDao.getILiveEnterpriseById(Integer.parseInt(ConfigUtils.get("defaultEnterpriseId")));
	}

	@Override
	@Transactional(readOnly = true)
	public List<ILiveEnterprise> getEnterPriseByIds(List<Integer> myEnterprise) {
		return iLiveEnterpriseDao.getEnterPriseByIds(myEnterprise);
	}

	@Override
	public List<ILiveAppEnterprise> getPagerForView(String keyWord, Integer pageNo, Integer pageSize,
			Integer searchType, Long userId) {
		List<ILiveEnterprise> enterpriseList = iLiveEnterpriseDao.getPagerForView1(keyWord, pageNo, pageSize);
		List<ILiveAppEnterprise> appEnterpriseList = new ArrayList<>();
		if (enterpriseList != null && !enterpriseList.isEmpty()) {
			for (ILiveEnterprise enterprise : enterpriseList) {
				ILiveAppEnterprise appEnterprise = this.convertPo2Vo(enterprise);
				if (userId != 0) {
					boolean exist = iLiveEnterpriseFansMng.isExist(enterprise.getEnterpriseId(), userId);
					if (exist) {
						appEnterprise.setConcernStatus(1);
					} else {
						appEnterprise.setConcernStatus(0);
					}
				} else {
					appEnterprise.setConcernStatus(0);
				}
				appEnterpriseList.add(appEnterprise);
			}
		}
		return appEnterpriseList;
	}

	@Override
	public List<ILiveAppEnterprise> getTop4ForView(String keyWord, Long userId) {
		List<ILiveEnterprise> top4ForView = iLiveEnterpriseDao.getTop4ForView1(keyWord);
		List<ILiveAppEnterprise> appEnterpriseList = new ArrayList<>();
		if (top4ForView != null && !top4ForView.isEmpty()) {
			for (ILiveEnterprise enterprise : top4ForView) {
				ILiveAppEnterprise appEnterprise = this.convertPo2Vo(enterprise);
				if (userId != 0) {
					boolean exist = iLiveEnterpriseFansMng.isExist(enterprise.getEnterpriseId(), userId);
					if (exist) {
						appEnterprise.setConcernStatus(1);
					} else {
						appEnterprise.setConcernStatus(0);
					}
				} else {
					appEnterprise.setConcernStatus(0);
				}
				appEnterpriseList.add(appEnterprise);
			}
		}
		return appEnterpriseList;
	}

	private ILiveAppEnterprise convertPo2Vo(ILiveEnterprise enterprise) {
		ILiveAppEnterprise appEnterprise = new ILiveAppEnterprise();
		appEnterprise.setCertStatus(enterprise.getCertStatus());
		appEnterprise.setEnterpriseDesc(StringPatternUtil.convertEmpty(enterprise.getEnterpriseDesc()));
		appEnterprise.setConcernTotal(0L);
		appEnterprise.setEnterpriseId(enterprise.getEnterpriseId());
		appEnterprise.setEnterpriseName(StringPatternUtil.convertEmpty(enterprise.getEnterpriseName()));
		appEnterprise.setEnterpriseImg(StringPatternUtil.convertEmpty(enterprise.getEnterpriseImg()));
		String defaultEnterpriseServerId = ConfigUtils.get("defaultEnterpriseServerId");
		ILiveServerAccessMethod serverGroup = serverAccessMng
				.getAccessMethodBySeverGroupId(Integer.parseInt(defaultEnterpriseServerId));
		String homePageUrl = serverGroup.getH5HttpUrl() + "/home/index.html?enterpriseId="
				+ enterprise.getEnterpriseId();
		appEnterprise.setHomePageUrl(homePageUrl);
		appEnterprise.setContactPhone(enterprise.getContactPhone());
		appEnterprise.setEntype(enterprise.getEntype());
		appEnterprise.setBindPhone(enterprise.getUserPhone());
		appEnterprise.setStamp(enterprise.getStamp());
		return appEnterprise;
	}

	@Override
	public List<ILiveAppEnterprise> getBatchEnterpriseForStatics(Integer startId, Integer size) {
		List<ILiveEnterprise> searchList = iLiveEnterpriseDao.getBatchEnterpriseForStatics(startId, size);
		List<ILiveAppEnterprise> appEnterpriseList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (searchList != null && !searchList.isEmpty()) {
			for (ILiveEnterprise enterprise : searchList) {
				try {
					ILiveAppEnterprise convertPo2Vo = this.convertPo2Vo(enterprise);
					convertPo2Vo.setApplyTime(sdf.format(enterprise.getApplyTime()));
					convertPo2Vo.setCertTime(enterprise.getCertTime() == null ? "" : sdf.format(enterprise.getCertTime()));
					appEnterpriseList.add(convertPo2Vo);
				} catch (Exception e) {
				}
			}
		}
		return appEnterpriseList;
	}

	@Override
	public List<ILiveAppEnterprise> geSingleEnterpriseForStatics(Integer[] enterpriseIds) {
		List<Integer> asList = Arrays.asList(enterpriseIds);
		List<ILiveEnterprise> enterPriseByIds = iLiveEnterpriseDao.getEnterPriseByIds(asList);
		List<ILiveAppEnterprise> appEnterpriseList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (enterPriseByIds != null && !enterPriseByIds.isEmpty()) {
			for (ILiveEnterprise enterprise : enterPriseByIds) {
				ILiveAppEnterprise convertPo2Vo = this.convertPo2Vo(enterprise);
				convertPo2Vo.setApplyTime(sdf.format(enterprise.getApplyTime()));
				convertPo2Vo.setCertTime(enterprise.getCertTime() == null ? "" : sdf.format(enterprise.getCertTime()));
				appEnterpriseList.add(convertPo2Vo);
			}
		}
		return appEnterpriseList;
	}
	@Override
	@Transactional
	public void updateEnterpriseWithPerson(ILiveEnterprise enterprise, List<ILiveManager> managerList) {
		iLiveEnterpriseDao.updateByBean(enterprise);
		if (managerList != null && !managerList.isEmpty()) {
			for (ILiveManager manager : managerList) {
				manager.setCertStatus(enterprise.getCertStatus());
				iLiveManagerMng.updateLiveManager(manager);
			}
		}
	}

	@Override
	public void update(ILiveEnterprise enterprise) {
		// TODO Auto-generated method stub
		iLiveEnterpriseDao.updateByBean(enterprise);
	}

	@Override
	public List<ILiveAppEnterprise> getPagerForView1(String keyWord, Integer pageNo, Integer pageSize,
			Integer searchType, Long userId) {
		Pagination pagination = fansDao.getIliveEnterPriseByUserId1(userId, pageNo, pageSize,keyWord);
		if (pagination != null && !pagination.getList().isEmpty()) {
			@SuppressWarnings("unchecked")
			List<ILiveEnterpriseFans> list = (List<ILiveEnterpriseFans>) pagination.getList();
			List<ILiveAppEnterprise> applist=new ArrayList<ILiveAppEnterprise>();
			for (ILiveEnterpriseFans fans : list) {
				ILiveAppEnterprise enterpriseApp = new ILiveAppEnterprise();
				Integer enterpriseId = fans.getEnterpriseId();
				ILiveEnterprise enterprise = this.getILiveEnterPrise(enterpriseId);
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
				applist.add(enterpriseApp);
					}
			
			return applist;
		}
		return null;
	}

	@Override
	public ILiveEnterprise getILiveEnterPriseByAppId(String appId) {
		// TODO Auto-generated method stub
		return iLiveEnterpriseDao.getILiveEnterPriseByAppId(appId);
	}

}
