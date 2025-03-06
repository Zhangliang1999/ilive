package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveVoteActivityDao;
import com.bvRadio.iLive.iLive.entity.ILiveVoteActivity;
import com.bvRadio.iLive.iLive.entity.ILiveVoteOption;
import com.bvRadio.iLive.iLive.entity.ILiveVotePeople;
import com.bvRadio.iLive.iLive.entity.ILiveVoteProblem;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveVoteActivityMng;
import com.bvRadio.iLive.iLive.manager.ILiveVoteOptionMng;
import com.bvRadio.iLive.iLive.manager.ILiveVotePeopleMng;
import com.bvRadio.iLive.iLive.manager.ILiveVoteProblemMng;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class ILiveVoteActivityMngImpl implements ILiveVoteActivityMng {

	@Autowired
	private ILiveVoteActivityDao iLiveVoteActivityDao;	//投票活动
	
	@Autowired
	private ILiveVoteOptionMng iLiveVoteOptionMng;		//投票问题选项
	
	@Autowired
	private ILiveVoteProblemMng iLiveVoteProblemMng;		//投票问题
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	@Autowired
	private ILiveVotePeopleMng iLiveVotePeopleMng;		//用户投票记录
	
	@Override
	public Pagination getPage(Integer roomId,String name, Integer pageNo, Integer pageSize) {
		iLiveVoteActivityDao.checkEndIsClose();
		Pagination page = iLiveVoteActivityDao.getPage(roomId,name,pageNo,pageSize);
		@SuppressWarnings("unchecked")
		List<ILiveVoteActivity> list = (List<ILiveVoteActivity>) page.getList();
		Iterator<ILiveVoteActivity> iterator = list.listIterator();
		Long now = new Date().getTime();
		while (iterator.hasNext()) {
			ILiveVoteActivity iLiveVoteActivity = (ILiveVoteActivity) iterator.next();
			if(now>iLiveVoteActivity.getEndTime().getTime()) {
				iLiveVoteActivity.setIsSwitch(0);
				iLiveVoteActivity.setIsEnd(1);
				iLiveVoteActivityDao.update(iLiveVoteActivity);
			}
		}
		return page;
	}
	@Override
	public Pagination getPageByEnterpriseId(Integer enterpriseId,String name, Integer pageNo, Integer pageSize) {
		iLiveVoteActivityDao.checkEndIsClose();
		Pagination page = iLiveVoteActivityDao.getPageByEnterpriseId(enterpriseId,name,pageNo,pageSize);
		@SuppressWarnings("unchecked")
		List<ILiveVoteActivity> list = (List<ILiveVoteActivity>) page.getList();
		Iterator<ILiveVoteActivity> iterator = list.listIterator();
		Long now = new Date().getTime();
		while (iterator.hasNext()) {
			ILiveVoteActivity iLiveVoteActivity = (ILiveVoteActivity) iterator.next();
			if(now>iLiveVoteActivity.getEndTime().getTime()) {
				iLiveVoteActivity.setIsSwitch(0);
				iLiveVoteActivity.setIsEnd(1);
				iLiveVoteActivityDao.update(iLiveVoteActivity);
			}
		}
		return page;
	}

	@Override
	public ILiveVoteActivity getById(Long voteId) {
		//获取投票内容信息
		ILiveVoteActivity activity = iLiveVoteActivityDao.getById(voteId);
		//获取投票问题
		List<ILiveVoteProblem> listByVoteId = iLiveVoteProblemMng.getListByVoteId(voteId);
		for(ILiveVoteProblem p:listByVoteId) {
			List<ILiveVoteOption> listOption = iLiveVoteOptionMng.getListByProblemId(p.getId());
			for(ILiveVoteOption option:listOption) {
				Integer num = iLiveVotePeopleMng.getPeopleByOptionId(option.getId());
				option.setNum(num);
			}
			p.setListOption(listOption);
		}
		activity.setList(listByVoteId);
		return activity;
	}

	@Override
	public Long save(ILiveVoteActivity voteActivity) {
		Long nextId = fieldIdMng.getNextId("ilive_vote_activity", "id", 1);
		Timestamp now = new Timestamp(new Date().getTime());
		voteActivity.setId(nextId);
		voteActivity.setIsSwitch(0);
		voteActivity.setIsBeforeSwitch(0);
		voteActivity.setIsEnd(0);
		voteActivity.setNumber(0);
		voteActivity.setCreateTime(now);
		voteActivity.setUpdateTime(now);
		iLiveVoteActivityDao.save(voteActivity);
		return nextId;
	}

	@Override
	public void update(ILiveVoteActivity voteActivity) {
		Timestamp now = new Timestamp(new Date().getTime());
		voteActivity.setUpdateTime(now);
		iLiveVoteActivityDao.update(voteActivity);
	}

	@Override
	@Transactional
	public void createVote(ILiveVoteActivity iLiveVoteActivity, String strList) {
		Long id = this.save(iLiveVoteActivity);
		JSONArray jsonArray = JSONArray.fromObject(strList);
		for(int i=0;i<jsonArray.size();i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			ILiveVoteProblem iLiveVoteProblem = new ILiveVoteProblem();
			iLiveVoteProblem.setVoteId(id);
			iLiveVoteProblem.setProblemName(obj.getString("problemName"));
			iLiveVoteProblem.setStyle(obj.getInt("style"));
			iLiveVoteProblem.setMaxVoteNum(obj.getInt("maxNum"));
			Long problemId = iLiveVoteProblemMng.save(iLiveVoteProblem);
			JSONArray arrOption = obj.getJSONArray("list");
			for(int j=0;j<arrOption.size();j++) {
				JSONObject objOption = arrOption.getJSONObject(j);
				ILiveVoteOption option = new ILiveVoteOption();
				option.setVoteId(id);
				option.setVoteProblemId(problemId);
				option.setContent(objOption.getString("content"));
				option.setContentImg(objOption.getString("contentImg"));
				iLiveVoteOptionMng.save(option);
			}
		}
	}

	@Override
	@Transactional
	public void updateVote(ILiveVoteActivity iLiveVoteActivity, String strList) {
		//更新活动
		ILiveVoteActivity activity = iLiveVoteActivityDao.getById(iLiveVoteActivity.getId());

		activity.setVoteName(iLiveVoteActivity.getVoteName());
		activity.setStartTime(iLiveVoteActivity.getStartTime());
		activity.setEndTime(iLiveVoteActivity.getEndTime());
		activity.setIsOpen(iLiveVoteActivity.getIsOpen());
		this.update(activity);
		
		
		List<ILiveVoteProblem> listByVoteId = iLiveVoteProblemMng.getListByVoteId(iLiveVoteActivity.getId());
		for(ILiveVoteProblem p:listByVoteId) {
			iLiveVoteOptionMng.deleteAllByProblemId(p.getId());
		}
		iLiveVoteProblemMng.deleteAllByVoteId(iLiveVoteActivity.getId());
		//遍历问题
		JSONArray jsonArray = JSONArray.fromObject(strList);
		Long id = iLiveVoteActivity.getId();
		for(int i=0;i<jsonArray.size();i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			
			//Long problemId = obj.getLong("id");
			Long problemId;
			ILiveVoteProblem iLiveVoteProblem;
			//if(problemId == 0) {
				iLiveVoteProblem = new ILiveVoteProblem();
			//}else {
			//	iLiveVoteProblem = iLiveVoteProblemMng.getById(problemId);
			//}
			
			//ILiveVoteProblem iLiveVoteProblem = new ILiveVoteProblem();
			iLiveVoteProblem.setVoteId(id);
			iLiveVoteProblem.setProblemName(obj.getString("problemName"));
			iLiveVoteProblem.setStyle(obj.getInt("style"));
			iLiveVoteProblem.setMaxVoteNum(obj.getInt("maxNum"));
			//if(problemId == 0) {
				problemId = iLiveVoteProblemMng.save(iLiveVoteProblem);
			//}else {
			//	iLiveVoteProblemMng.update(iLiveVoteProblem);
			//}
			//遍历选项
			JSONArray arrOption = obj.getJSONArray("list");
			for(int j=0;j<arrOption.size();j++) {
				JSONObject objOption = arrOption.getJSONObject(j);
				
				//Long optionId = objOption.getLong("id");
				ILiveVoteOption option;
				//if (optionId == 0) {
					option = new ILiveVoteOption();
				//}else {
				//	option = iLiveVoteOptionMng.getById(optionId);
				//}
				option.setVoteId(id);
				option.setVoteProblemId(problemId);
				option.setContent(objOption.getString("content"));
				option.setContentImg(objOption.getString("contentImg"));
				//if (optionId == 0) {
					iLiveVoteOptionMng.save(option);
				//}else {
				//	iLiveVoteOptionMng.update(option);
				//}
			}
		}
	}

	@Override
	public ILiveVoteActivity getActivityByRoomId(Integer roomId) {
		return iLiveVoteActivityDao.getActivityByRoomId(roomId);
	}
	@Override
	public ILiveVoteActivity getActivityByEnterpriseId(Integer enterpriseId) {
		return iLiveVoteActivityDao.getActivityByEnterpriseId(enterpriseId);
	}

	@Override
	public List<ILiveVoteActivity> getH5VoteList(Integer roomId) {
		List<ILiveVoteActivity> h5VoteList = iLiveVoteActivityDao.getH5VoteList(roomId);
		Long now = new Date().getTime();
		Iterator<ILiveVoteActivity> iterator = h5VoteList.iterator();
		while (iterator.hasNext()) {
			ILiveVoteActivity activity = (ILiveVoteActivity) iterator.next();
			if (activity.getEndTime().getTime()<now) {
				activity.setIsEnd(1);
				activity.setIsSwitch(0);
				update(activity);
				iterator.remove();
			}
			
			
			//获取投票问题
			List<ILiveVoteProblem> listByVoteId = iLiveVoteProblemMng.getListByVoteId(activity.getId());
			for(ILiveVoteProblem p:listByVoteId) {
				List<ILiveVoteOption> listOption = iLiveVoteOptionMng.getListByProblemId(p.getId());
				p.setListOption(listOption);
			}
			activity.setList(listByVoteId);
			
		}
		
		return h5VoteList;
	}

	@Override
	public ILiveVoteActivity getH5Vote(Integer roomId) {
		ILiveVoteActivity activity = iLiveVoteActivityDao.getH5Vote(roomId);
		List<ILiveVoteProblem> listByVoteId = iLiveVoteProblemMng.getListByVoteId(activity.getId());
		for(ILiveVoteProblem p:listByVoteId) {
			List<ILiveVoteOption> listOption = iLiveVoteOptionMng.getListByProblemId(p.getId());
			p.setListOption(listOption);
		}
		activity.setList(listByVoteId);
		return activity;
	}
	
	@Override
	public ILiveVoteActivity getH5Vote2(Integer enterpriseId) {
		ILiveVoteActivity activity = iLiveVoteActivityDao.getH5Vote2(enterpriseId);
		List<ILiveVoteProblem> listByVoteId = iLiveVoteProblemMng.getListByVoteId(activity.getId());
		for(ILiveVoteProblem p:listByVoteId) {
			List<ILiveVoteOption> listOption = iLiveVoteOptionMng.getListByProblemId(p.getId());
			p.setListOption(listOption);
		}
		activity.setList(listByVoteId);
		return activity;
	}

	@Override
	@Transactional
	public void vote(Long voteId,Long userId, String string) {
		//投票总数加一
		ILiveVoteActivity activity = iLiveVoteActivityDao.getById(voteId);
		activity.setNumber(activity.getNumber()+1);
		iLiveVoteActivityDao.update(activity);
		
		//保存用户投票记录
		String[] idStrings = string.split(",");
		for(String a:idStrings) {
			String[] ids = a.split("_");
			ILiveVotePeople iLiveVotePeople = new ILiveVotePeople();
			iLiveVotePeople.setVoteId(voteId);
			iLiveVotePeople.setVoteProblemId(Long.parseLong(ids[0]));
			iLiveVotePeople.setVoteOptionId(Long.parseLong(ids[1]));
			iLiveVotePeople.setUserId(userId);
			iLiveVotePeopleMng.save(iLiveVotePeople);
		}
		
	}

	@Override
	public ILiveVoteActivity getResult(Long voteId) {
		//获取投票内容信息
		ILiveVoteActivity activity = iLiveVoteActivityDao.getById(voteId);
		
		//获取有多少人投过票
		Integer number = iLiveVotePeopleMng.getAllPeopleByVoteId(voteId);
		if(number==0) {
			number = 1;
		}
		DecimalFormat df = new DecimalFormat("######0.00");  
		//获取投票问题
		List<ILiveVoteProblem> listByVoteId = iLiveVoteProblemMng.getListByVoteId(voteId);
		for(ILiveVoteProblem p:listByVoteId) {
			//获取这个问题有多少选项
			List<ILiveVoteOption> listOption = iLiveVoteOptionMng.getListByProblemId(p.getId());
			//获取一共有多少人选择这个问题
			Integer optionRecord = iLiveVotePeopleMng.getPeopleByProblemId(p.getId());
			
			//查询每个选项有多少人投票 占百分比多少
			for(ILiveVoteOption option:listOption) {
				Integer optionNum = iLiveVotePeopleMng.getPeopleByOptionId(option.getId());
				double d = (double)optionNum/(double)optionRecord * 100;
				String percen = df.format(d);
				option.setNum(optionNum);
				option.setPercen(percen);
			}
			p.setListOption(listOption);
		}
		activity.setList(listByVoteId);
		return activity;
	}
	@Override
	public ILiveVoteActivity getActivityByenterpriseId(Integer enterpriseId) {
		return iLiveVoteActivityDao.getActivityByenterpriseId(enterpriseId);
	}
	@Override
	public List<ILiveVoteActivity> getH5VoteListByEnterpriseId(Integer enterpriseId) {
		List<ILiveVoteActivity> h5VoteList = iLiveVoteActivityDao.getH5VoteListByEnterpriseId(enterpriseId);
		if (h5VoteList!=null&&!h5VoteList.isEmpty()) {
			Long now = new Date().getTime();
			Iterator<ILiveVoteActivity> iterator = h5VoteList.iterator();
			while (iterator.hasNext()) {
				ILiveVoteActivity activity = (ILiveVoteActivity) iterator.next();
				if (activity.getEndTime().getTime()<now) {
					activity.setIsEnd(1);
					activity.setIsSwitch(0);
					update(activity);
					iterator.remove();
				}
				
				
				//获取投票问题
				List<ILiveVoteProblem> listByVoteId = iLiveVoteProblemMng.getListByVoteId(activity.getId());
				for(ILiveVoteProblem p:listByVoteId) {
					List<ILiveVoteOption> listOption = iLiveVoteOptionMng.getListByProblemId(p.getId());
					p.setListOption(listOption);
				}
				activity.setList(listByVoteId);
				
			}
		}
		
		return h5VoteList;
	}
	@Override
	public List<ILiveVoteActivity> getH5VoteListByUserId(Long userId) {
		return iLiveVoteActivityDao.getH5VoteListByUserId(userId);
	}
	@Override
	public Pagination getpageByUserId(Long userId,String votename,Integer pageNo,Integer pageSize) {
		return iLiveVoteActivityDao.getpageByUserId(userId, votename, pageNo, pageSize);
	}

}
