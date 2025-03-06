package com.bvRadio.iLive.iLive.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveSubLevelDao;
import com.bvRadio.iLive.iLive.entity.ILiveSubLevel;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubLevelMng;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

@Transactional
@Service
public class ILiveSubLevelMngImp implements ILiveSubLevelMng {
   @Autowired ILiveSubLevelDao iLiveSubLevelDao;
   @Autowired
	private ILiveManagerMng iLiveManagerMng;
	@Override
	public List<ILiveSubLevel> selectILiveSubById(Long userId) {
		// TODO Auto-generated method stub
		return iLiveSubLevelDao.selectILiveSubById(userId);
	}

	@Override
	public void save(ILiveSubLevel iLiveSubLevel) {
		// TODO Auto-generated method stub
		iLiveSubLevelDao.save(iLiveSubLevel);
	}

	@Override
	public Long selectMaxId() {
		// TODO Auto-generated method stub
		return iLiveSubLevelDao.selectMaxId();
	}

	@Override
	public void delete(Long userId) {
		// TODO Auto-generated method stub
		iLiveSubLevelDao.delete(userId);
	}

	@Override
	public void update(ILiveSubLevel iLiveSubLevel) {
		// TODO Auto-generated method stub
		iLiveSubLevelDao.update(iLiveSubLevel);
	}

	@Override
	public ILiveSubLevel getSubLevel(Long userId) {
		// TODO Auto-generated method stub
		return iLiveSubLevelDao.getSubLevel(userId);
	}

	@Override
	@Transactional(readOnly=true)
	public boolean selectIfCan(HttpServletRequest request, String permission) {
		boolean ret=false;
		UserBean user = ILiveUtils.getUser(request);
		Integer level=user.getLevel();
		List<ILiveSubLevel> iLiveSubLevel = iLiveManagerMng.selectILiveSubById(Long.parseLong(user.getUserId()));
		List list=new ArrayList();
		if(iLiveSubLevel!=null&&iLiveSubLevel.size()>0) {
			for(int i=0;i<iLiveSubLevel.size();i++) {
				list.add(iLiveSubLevel.get(i).getSubTop());
			}
		}
		if(level==0) {
			ret=true;
		}else {
			if(list==null||list.size()==0) {
				ret=false;
			}else if(list!=null&&list.size()>0) {
				for(int i=0;i<list.size();i++) {
					String sub=list.get(i).toString();
					if(permission.equals(sub)) {
						ret=true;
						break;
					}
				}
			}
		}
		
		return ret;
	}

	@Override
	@Transactional(readOnly=true)
	public boolean selectAppIfCan(HttpServletRequest request, String permission) {
		boolean ret=false;
		UserBean user = ILiveUtils.getAppUser(request);
		Integer level=user.getLevel();
		List<ILiveSubLevel> iLiveSubLevel = iLiveManagerMng.selectILiveSubById(Long.parseLong(user.getUserId()));
		List list=new ArrayList();
		if(iLiveSubLevel!=null&&iLiveSubLevel.size()>0) {
			for(int i=0;i<iLiveSubLevel.size();i++) {
				list.add(iLiveSubLevel.get(i).getSubTop());
			}
		}
		if(level==0) {
			ret=true;
		}else {
			if(list==null||list.size()==0) {
				ret=false;
			}else if(list!=null&&list.size()>0) {
				for(int i=0;i<list.size();i++) {
					String sub=list.get(i).toString();
					if(permission.equals(sub)) {
						ret=true;
						break;
					}
				}
			}
		}
		
		return ret;
	}
	

}
