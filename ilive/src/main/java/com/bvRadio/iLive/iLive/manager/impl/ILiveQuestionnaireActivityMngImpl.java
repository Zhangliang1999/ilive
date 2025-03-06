package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveQuestionnaireActivityDao;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireActivity;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireActivityStatistic;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireOption;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnairePeople;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireProblem;
import com.bvRadio.iLive.iLive.entity.vo.ILiveWenJuanResult;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveQuestionnaireActivityMng;
import com.bvRadio.iLive.iLive.manager.ILiveQuestionnaireOptionMng;
import com.bvRadio.iLive.iLive.manager.ILiveQuestionnairePeopleMng;
import com.bvRadio.iLive.iLive.manager.ILiveQuestionnaireProblemMng;
import com.bvRadio.iLive.iLive.manager.ILiveQuestionnaireStatisticMng;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class ILiveQuestionnaireActivityMngImpl implements ILiveQuestionnaireActivityMng {

	@Autowired
	private ILiveQuestionnaireActivityDao iLiveQuestionnaireActivityDao;	//问卷活动
	
	@Autowired
	private ILiveQuestionnaireOptionMng iLiveQuestionnaireOptionMng;		//问卷问题选项
	
	@Autowired
	private ILiveQuestionnaireProblemMng iLiveQuestionnaireProblemMng;		//投票问题
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	@Autowired
	private ILiveQuestionnairePeopleMng iLiveQuestionnairePeopleMng;		//用户答卷记录
	
	@Autowired
	private ILiveQuestionnaireStatisticMng iLiveQuestionnaireStatisticMng; //用户作答或浏览记录
	
	@Override
	public Pagination getPage(Integer roomId,String name, Integer pageNo, Integer pageSize) {
		iLiveQuestionnaireActivityDao.checkEndIsClose();
		Pagination page = iLiveQuestionnaireActivityDao.getPage(roomId,name,pageNo,pageSize);
		@SuppressWarnings("unchecked")
		List<ILiveQuestionnaireActivity> list = (List<ILiveQuestionnaireActivity>) page.getList();
		Iterator<ILiveQuestionnaireActivity> iterator = list.listIterator();
		Long now = new Date().getTime();
		while (iterator.hasNext()) {
			ILiveQuestionnaireActivity iLiveQuestionnaireActivity = (ILiveQuestionnaireActivity) iterator.next();
			if(now>iLiveQuestionnaireActivity.getEndTime().getTime()) {
				iLiveQuestionnaireActivity.setIsSwitch(0);
				iLiveQuestionnaireActivity.setIsEnd(1);
				iLiveQuestionnaireActivityDao.update(iLiveQuestionnaireActivity);
			}
		}
		return page;
	}
	@Override
	public Pagination getPageByEnterpriseId(Integer enterpriseId,String name, String time,Integer pageNo, Integer pageSize) {
		iLiveQuestionnaireActivityDao.checkEndIsClose();
		Pagination page = iLiveQuestionnaireActivityDao.getPageByEnterpriseId(enterpriseId,name,time,pageNo,pageSize);
		@SuppressWarnings("unchecked")
		List<ILiveQuestionnaireActivity> list = (List<ILiveQuestionnaireActivity>) page.getList();
		Iterator<ILiveQuestionnaireActivity> iterator = list.listIterator();
		Long now = new Date().getTime();
		while (iterator.hasNext()) {
			ILiveQuestionnaireActivity iLiveQuestionnaireActivity = (ILiveQuestionnaireActivity) iterator.next();
			if(now>iLiveQuestionnaireActivity.getEndTime().getTime()) {
				iLiveQuestionnaireActivity.setIsSwitch(0);
				iLiveQuestionnaireActivity.setIsEnd(1);
				iLiveQuestionnaireActivityDao.update(iLiveQuestionnaireActivity);
			}
		}
		return page;
	}

	@Override
	public ILiveQuestionnaireActivity getById(Long QuestionnaireId) {
		//获取问卷内容信息
		ILiveQuestionnaireActivity activity = iLiveQuestionnaireActivityDao.getById(QuestionnaireId);
		//获取问卷问题
		List<ILiveQuestionnaireProblem> listByQuestionnaireId = iLiveQuestionnaireProblemMng.getListByQuestionnaireId(QuestionnaireId);
		for(ILiveQuestionnaireProblem p:listByQuestionnaireId) {
			List<ILiveQuestionnaireOption> listOption = iLiveQuestionnaireOptionMng.getListByProblemId(p.getId());
			for(ILiveQuestionnaireOption option:listOption) {
				Integer num = iLiveQuestionnairePeopleMng.getPeopleByOptionId(option.getId());
				option.setNum(num);
			}
			p.setListOption(listOption);
		}
		activity.setList(listByQuestionnaireId);
		return activity;
	}

	@Override
	public Long save(ILiveQuestionnaireActivity QuestionnaireActivity) {
		Long nextId = fieldIdMng.getNextId("ilive_Questionnaire_activity", "id", 1);
		Timestamp now = new Timestamp(new Date().getTime());
		QuestionnaireActivity.setId(nextId);
		QuestionnaireActivity.setIsSwitch(0);
		QuestionnaireActivity.setIsBeforeSwitch(0);
		QuestionnaireActivity.setIsEnd(0);
		QuestionnaireActivity.setNumber(0);
		QuestionnaireActivity.setCreateTime(now);
		QuestionnaireActivity.setUpdateTime(now);
		iLiveQuestionnaireActivityDao.save(QuestionnaireActivity);
		return nextId;
	}

	@Override
	public void update(ILiveQuestionnaireActivity QuestionnaireActivity) {
		Timestamp now = new Timestamp(new Date().getTime());
		QuestionnaireActivity.setUpdateTime(now);
		iLiveQuestionnaireActivityDao.update(QuestionnaireActivity);
	}

	@Override
	@Transactional
	public void createQuestionnaire(ILiveQuestionnaireActivity iLiveQuestionnaireActivity, String strList) {
		Long id = this.save(iLiveQuestionnaireActivity);
		JSONArray jsonArray = JSONArray.fromObject(strList);
		for(int i=0;i<jsonArray.size();i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			ILiveQuestionnaireProblem iLiveQuestionnaireProblem = new ILiveQuestionnaireProblem();
			iLiveQuestionnaireProblem.setQuestionnaireId(id);
			iLiveQuestionnaireProblem.setProblemName(obj.getString("problemName"));
			iLiveQuestionnaireProblem.setStyle(obj.getInt("style"));
			Integer index=i+1;
			iLiveQuestionnaireProblem.setProblemIndex(index.longValue());
			iLiveQuestionnaireProblem.setMaxQuestionnaireNum(Integer.parseInt(obj.getString("maxNum")));
			Long problemId = iLiveQuestionnaireProblemMng.save(iLiveQuestionnaireProblem);
			if(obj.getInt("style")!=3){
				JSONArray arrOption = obj.getJSONArray("list");
				for(int j=0;j<arrOption.size();j++) {
					JSONObject objOption = arrOption.getJSONObject(j);
					ILiveQuestionnaireOption option = new ILiveQuestionnaireOption();
					option.setQuestionnaireId(id);
					option.setQuestionnaireProblemId(problemId);
					option.setContent(objOption.getString("content"));
					option.setContentImg(objOption.getString("contentImg"));
					iLiveQuestionnaireOptionMng.save(option);
				}
			}
			
		}
	}

	@Override
	@Transactional
	public void updateQuestionnaire(ILiveQuestionnaireActivity iLiveQuestionnaireActivity, String strList) {
		//更新活动
		ILiveQuestionnaireActivity activity = iLiveQuestionnaireActivityDao.getById(iLiveQuestionnaireActivity.getId());

		activity.setQuestionnaireName(iLiveQuestionnaireActivity.getQuestionnaireName());
		activity.setStartTime(iLiveQuestionnaireActivity.getStartTime());
		activity.setEndTime(iLiveQuestionnaireActivity.getEndTime());
		activity.setIsOpen(iLiveQuestionnaireActivity.getIsOpen());
		activity.setAuthentication(iLiveQuestionnaireActivity.getAuthentication());
		activity.setIdentity(iLiveQuestionnaireActivity.getIdentity());
		this.update(activity);
		
		
		List<ILiveQuestionnaireProblem> listByQuestionnaireId = iLiveQuestionnaireProblemMng.getListByQuestionnaireId(iLiveQuestionnaireActivity.getId());
		for(ILiveQuestionnaireProblem p:listByQuestionnaireId) {
			iLiveQuestionnaireOptionMng.deleteAllByProblemId(p.getId());
		}
		iLiveQuestionnaireProblemMng.deleteAllByQuestionnaireId(iLiveQuestionnaireActivity.getId());
		//遍历选项问题
		JSONArray jsonArray = JSONArray.fromObject(strList);
		Long id = iLiveQuestionnaireActivity.getId();
		for(int i=0;i<jsonArray.size();i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			ILiveQuestionnaireProblem iLiveQuestionnaireProblem = new ILiveQuestionnaireProblem();
			iLiveQuestionnaireProblem.setQuestionnaireId(id);
			iLiveQuestionnaireProblem.setProblemName(obj.getString("problemName"));
			iLiveQuestionnaireProblem.setStyle(obj.getInt("style"));
			Integer index=i+1;
			iLiveQuestionnaireProblem.setProblemIndex(index.longValue());
			iLiveQuestionnaireProblem.setMaxQuestionnaireNum(Integer.parseInt(obj.getString("maxNum")));
			Long problemId = iLiveQuestionnaireProblemMng.save(iLiveQuestionnaireProblem);
			if(obj.getInt("style")!=3){
				JSONArray arrOption = obj.getJSONArray("list");
				for(int j=0;j<arrOption.size();j++) {
					JSONObject objOption = arrOption.getJSONObject(j);
					ILiveQuestionnaireOption option = new ILiveQuestionnaireOption();
					option.setQuestionnaireId(id);
					option.setQuestionnaireProblemId(problemId);
					option.setContent(objOption.getString("content"));
					option.setContentImg(objOption.getString("contentImg"));
					iLiveQuestionnaireOptionMng.save(option);
				}
			}
		} 
	}

	@Override
	public ILiveQuestionnaireActivity getActivityByRoomId(Integer roomId) {
		return iLiveQuestionnaireActivityDao.getActivityByRoomId(roomId);
	}
	@Override
	public ILiveQuestionnaireActivity getActivityByEnterpriseId(Integer enterpriseId) {
		return iLiveQuestionnaireActivityDao.getActivityByEnterpriseId(enterpriseId);
	}

	@Override
	public List<ILiveQuestionnaireActivity> getH5QuestionnaireList(Integer roomId) {
		List<ILiveQuestionnaireActivity> h5QuestionnaireList = iLiveQuestionnaireActivityDao.getH5QuestionnaireList(roomId);
		Long now = new Date().getTime();
		Iterator<ILiveQuestionnaireActivity> iterator = h5QuestionnaireList.iterator();
		while (iterator.hasNext()) {
			ILiveQuestionnaireActivity activity = (ILiveQuestionnaireActivity) iterator.next();
			if (activity.getEndTime().getTime()<now) {
				activity.setIsEnd(1);
				activity.setIsSwitch(0);
				update(activity);
				iterator.remove();
			}
			
			
			//获取投票问题
			List<ILiveQuestionnaireProblem> listByQuestionnaireId = iLiveQuestionnaireProblemMng.getListByQuestionnaireId(activity.getId());
			for(ILiveQuestionnaireProblem p:listByQuestionnaireId) {
				List<ILiveQuestionnaireOption> listOption = iLiveQuestionnaireOptionMng.getListByProblemId(p.getId());
				p.setListOption(listOption);
			}
			activity.setList(listByQuestionnaireId);
			
		}
		
		return h5QuestionnaireList;
	}

	@Override
	public ILiveQuestionnaireActivity getH5Questionnaire(Integer roomId) {
		ILiveQuestionnaireActivity activity = iLiveQuestionnaireActivityDao.getH5Questionnaire(roomId);
		List<ILiveQuestionnaireProblem> listByQuestionnaireId = iLiveQuestionnaireProblemMng.getListByQuestionnaireId(activity.getId());
		for(ILiveQuestionnaireProblem p:listByQuestionnaireId) {
			List<ILiveQuestionnaireOption> listOption = iLiveQuestionnaireOptionMng.getListByProblemId(p.getId());
			p.setListOption(listOption);
		}
		activity.setList(listByQuestionnaireId);
		return activity;
	}
	
	@Override
	public ILiveQuestionnaireActivity getH5Questionnaire2(Integer enterpriseId) {
		ILiveQuestionnaireActivity activity = iLiveQuestionnaireActivityDao.getH5Questionnaire2(enterpriseId);
		List<ILiveQuestionnaireProblem> listByQuestionnaireId = iLiveQuestionnaireProblemMng.getListByQuestionnaireId(activity.getId());
		for(ILiveQuestionnaireProblem p:listByQuestionnaireId) {
			List<ILiveQuestionnaireOption> listOption = iLiveQuestionnaireOptionMng.getListByProblemId(p.getId());
			p.setListOption(listOption);
		}
		activity.setList(listByQuestionnaireId);
		return activity;
	}

	@Override
	@Transactional
	public void Questionnaire(Long QuestionnaireId,Long userId, String string, String questionAnswers) {
		//答卷记录加一
		ILiveQuestionnaireActivity activity = iLiveQuestionnaireActivityDao.getById(QuestionnaireId);
		activity.setNumber(activity.getNumber()+1);
		iLiveQuestionnaireActivityDao.update(activity);
		
		//保存用户答卷记录
		String[] idStrings = string.split(",");
		for(String a:idStrings) {
			String[] ids = a.split("_");
			ILiveQuestionnairePeople iLiveQuestionnairePeople = new ILiveQuestionnairePeople();
			iLiveQuestionnairePeople.setQuestionnaireId(QuestionnaireId);
			iLiveQuestionnairePeople.setQuestionnaireProblemId(Long.parseLong(ids[0]));
			iLiveQuestionnairePeople.setQuestionnaireOptionId(Long.parseLong(ids[1]));
			iLiveQuestionnairePeople.setUserId(userId);
			iLiveQuestionnairePeopleMng.save(iLiveQuestionnairePeople);
		}
		String[] answers = questionAnswers.split(",");
		for(String b:answers) {
			String[] answer = b.split("_");
			ILiveQuestionnairePeople iLiveQuestionnairePeople = new ILiveQuestionnairePeople();
			iLiveQuestionnairePeople.setQuestionnaireId(QuestionnaireId);
			iLiveQuestionnairePeople.setQuestionnaireProblemId(Long.parseLong(answer[0]));
			iLiveQuestionnairePeople.setAnswer(answer[1]);
			iLiveQuestionnairePeople.setUserId(userId);
			iLiveQuestionnairePeopleMng.save(iLiveQuestionnairePeople);
		}
		//更新答卷结束时间
		ILiveQuestionnaireActivityStatistic statistic=iLiveQuestionnaireStatisticMng.getListByUserId(userId, QuestionnaireId);
		statistic.setEndTime(new Timestamp(System.currentTimeMillis()));
		iLiveQuestionnaireStatisticMng.update(statistic);
	}

	@Override
	public ILiveQuestionnaireActivity getResult(Long QuestionnaireId) {
		//获取答卷内容信息
		ILiveQuestionnaireActivity activity = iLiveQuestionnaireActivityDao.getById(QuestionnaireId);
		
		//获取有多少人答卷
		Integer number = iLiveQuestionnairePeopleMng.getAllPeopleByQuestionnaireId(QuestionnaireId);
		if(number==0) {
			number = 1;
		}
		DecimalFormat df = new DecimalFormat("######0.00");  
		//获取问卷问题
		List<ILiveQuestionnaireProblem> listByQuestionnaireId = iLiveQuestionnaireProblemMng.getListByQuestionnaireId(QuestionnaireId);
		for(ILiveQuestionnaireProblem p:listByQuestionnaireId) {
			//如果是选择题
			if(p.getStyle()!=3){
				//获取这个问题有多少选项
				List<ILiveQuestionnaireOption> listOption = iLiveQuestionnaireOptionMng.getListByProblemId(p.getId());
				//获取一共有多少人选择这个问题
				Integer optionRecord = iLiveQuestionnairePeopleMng.getPeopleByProblemId(p.getId());
				
				//查询每个选项有多少人投票 占百分比多少
				for(ILiveQuestionnaireOption option:listOption) {
					Integer optionNum = iLiveQuestionnairePeopleMng.getPeopleByOptionId(option.getId());
					double d = (double)optionNum/(double)optionRecord * 100;
					String percen = df.format(d);
					option.setNum(optionNum);
					option.setPercen(percen);
				}
				p.setListOption(listOption);
			}else{
				//获取这个问题对应的答案
				List<ILiveQuestionnairePeople> listPeople=iLiveQuestionnairePeopleMng.getListByQuestionnaireId(QuestionnaireId);
				p.setListPeople(listPeople);
			}
		}
		activity.setList(listByQuestionnaireId);
		return activity;
	}
	@Override
	public ILiveQuestionnaireActivity getActivityByenterpriseId(Integer enterpriseId) {
		return iLiveQuestionnaireActivityDao.getActivityByenterpriseId(enterpriseId);
	}
	@Override
	public List<ILiveQuestionnaireActivity> getH5QuestionnaireListByEnterpriseId(Integer enterpriseId) {
		List<ILiveQuestionnaireActivity> h5QuestionnaireList = iLiveQuestionnaireActivityDao.getH5QuestionnaireListByEnterpriseId(enterpriseId);
		if (h5QuestionnaireList!=null&&!h5QuestionnaireList.isEmpty()) {
			Long now = new Date().getTime();
			Iterator<ILiveQuestionnaireActivity> iterator = h5QuestionnaireList.iterator();
			while (iterator.hasNext()) {
				ILiveQuestionnaireActivity activity = (ILiveQuestionnaireActivity) iterator.next();
				if (activity.getEndTime().getTime()<now) {
					activity.setIsEnd(1);
					activity.setIsSwitch(0);
					update(activity);
					iterator.remove();
				}
				
				
				//获取投票问题
				List<ILiveQuestionnaireProblem> listByQuestionnaireId = iLiveQuestionnaireProblemMng.getListByQuestionnaireId(activity.getId());
				for(ILiveQuestionnaireProblem p:listByQuestionnaireId) {
					List<ILiveQuestionnaireOption> listOption = iLiveQuestionnaireOptionMng.getListByProblemId(p.getId());
					p.setListOption(listOption);
				}
				activity.setList(listByQuestionnaireId);
				
			}
		}
		
		return h5QuestionnaireList;
	}
	@Override
	public List<ILiveQuestionnaireActivity> getH5QuestionnaireListByUserId(Long userId) {
		return iLiveQuestionnaireActivityDao.getH5QuestionnaireListByUserId(userId);
	}
	@Override
	public Pagination getpageByUserId(Long userId,String Questionnairename,Integer pageNo,Integer pageSize) {
		return iLiveQuestionnaireActivityDao.getpageByUserId(userId, Questionnairename, pageNo, pageSize);
	}
	@Override
	public ILiveQuestionnaireActivity getById1(Long questionnaireId,Long problemId,Integer pageNo) {
			//获取问卷内容信息
			ILiveQuestionnaireActivity activity = iLiveQuestionnaireActivityDao.getById(questionnaireId);
			//获取问卷问题
			List<ILiveQuestionnaireProblem> listByQuestionnaireId = iLiveQuestionnaireProblemMng.getListByQuestionnaireId(questionnaireId);
			for(ILiveQuestionnaireProblem p:listByQuestionnaireId) {
				if(p.getId()==problemId){
					
					if(p.getStyle()!=3){
						List<ILiveQuestionnaireOption> listOption = iLiveQuestionnaireOptionMng.getListByProblemId(p.getId());
						for(ILiveQuestionnaireOption option:listOption) {
							Integer num = iLiveQuestionnairePeopleMng.getPeopleByOptionId(option.getId());
							option.setNum(num);
						}
						p.setListOption(listOption);
					}else{
						//获取这个问题对应的答案
						Pagination pagination=iLiveQuestionnairePeopleMng.getByQuestionnaireId(questionnaireId, problemId, pageNo, 15);
						List<ILiveQuestionnairePeople> listPeople=(List<ILiveQuestionnairePeople>) pagination.getList();
						p.setListPeople(listPeople);
					}
				}
				
			}
			activity.setList(listByQuestionnaireId);
			return activity;
	}
	@Override
	public List<ILiveWenJuanResult> getByuserId(Long questionnaireId, Long userId) {
		List<ILiveWenJuanResult> list=new ArrayList<ILiveWenJuanResult>();
		//获取问卷内容信息
		ILiveQuestionnaireActivity activity = iLiveQuestionnaireActivityDao.getById(questionnaireId);
		//获取问卷问题
		List<ILiveQuestionnaireProblem> listByQuestionnaireId = iLiveQuestionnaireProblemMng.getListByQuestionnaireId(questionnaireId);
		for(ILiveQuestionnaireProblem p:listByQuestionnaireId) {
					//获取这个问题对应的答案
					List<ILiveQuestionnairePeople> listPeople=iLiveQuestionnairePeopleMng.getListByUserId(userId,"",questionnaireId);
				    for(int i=0;i<listPeople.size();i++){
				    	if(listPeople.get(i).getQuestionnaireProblemId().equals(p.getId())){
				    		ILiveWenJuanResult result=new ILiveWenJuanResult();
				    		result.setQuestionnaireId(questionnaireId);
				    		result.setQuestionnaireName(activity.getQuestionnaireName());
				    		result.setQuestionnaireProblemIndex((i+1)+"");
				    		result.setQuestionnaireProblem(p.getProblemName());
				    		String answer=null;
				    		String imgUrl=null;
				    		if(p.getStyle()==1){
				    		answer=	iLiveQuestionnaireOptionMng.getById(listPeople.get(i).getQuestionnaireOptionId()).getContent();
				    		}else if(p.getStyle()==2){
				    		answer=	iLiveQuestionnaireOptionMng.getById(listPeople.get(i).getQuestionnaireOptionId()).getContent();
				    		imgUrl=iLiveQuestionnaireOptionMng.getById(listPeople.get(i).getQuestionnaireOptionId()).getContentImg();
				    		}else if(p.getStyle()==3){
				    			answer=listPeople.get(i).getAnswer();
				    		}
				    		result.setImgUrl(imgUrl);
				    		result.setQuestionnaireProblemAnswer(answer);
				    		list.add(result);
				    	}
				    	}
			}
		
		activity.setList(listByQuestionnaireId);
		return list;
	}
	@Override
	public Map<Long, Object> getByquestionnaireId(Long questionnaireId) {
		Map<Long, Object> map = new HashMap<Long, Object>();
		//获取问卷问题
		List<ILiveQuestionnaireProblem> listByQuestionnaireId = iLiveQuestionnaireProblemMng.getListByQuestionnaireId(questionnaireId);
		for(ILiveQuestionnaireProblem p:listByQuestionnaireId) {
			         //获取回答这个问题的人员
			List<ILiveQuestionnairePeople> answerPeople=iLiveQuestionnairePeopleMng.getListByQuestionnaireId(questionnaireId);
			for(ILiveQuestionnairePeople allpeople : answerPeople){
				//获取这个问题对应的答案
				List<ILiveWenJuanResult> list=this.getByuserId(questionnaireId, allpeople.getUserId());
			             map.put(allpeople.getUserId(), list);
				    	}
			}
		
		
		return map;
	}
	

}
