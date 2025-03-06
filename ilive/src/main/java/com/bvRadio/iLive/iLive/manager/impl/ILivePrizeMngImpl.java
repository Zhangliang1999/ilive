package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILivePrizeDao;
import com.bvRadio.iLive.iLive.entity.ILiveLottery;
import com.bvRadio.iLive.iLive.entity.ILiveLotteryList;
import com.bvRadio.iLive.iLive.entity.ILiveLotteryParktake;
import com.bvRadio.iLive.iLive.entity.ILiveLotteryPrize;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveLotteryListMng;
import com.bvRadio.iLive.iLive.manager.ILiveLotteryParktakeMng;
import com.bvRadio.iLive.iLive.manager.ILiveLotteryPrizeMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILivePrizeMng;
import com.bvRadio.iLive.iLive.manager.ILivePrizeRoomMng;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class ILivePrizeMngImpl implements ILivePrizeMng{

	@Autowired
	private ILivePrizeDao iLivePrizeDao;		//抽奖内容
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	@Autowired
	private ILiveLotteryPrizeMng iLiveLotteryPrizeMng;		//抽奖奖品设置
	
	@Autowired
	private ILiveManagerMng iLiveManagerMng;		//用户Manager
	
	@Autowired
	private ILiveLotteryParktakeMng iLiveLotteryParktakeMng;		//抽奖用户

	@Autowired
	private ILiveLotteryListMng iLiveLotteryListMng;		//白名单/黑名单
	
	@Autowired
	private ILivePrizeRoomMng iLivePrizeRoomMng;
	
	@Override
	@Transactional
	public Pagination getPage(String prizeName,Integer roomId, Integer pageNo, Integer pageSize) {
		//将所有结束时间超过现在的抽奖设置为已结束
		//iLivePrizeDao.setLetEnd();
		//查询
		Pagination page = iLivePrizeDao.getPage(prizeName,roomId,pageNo,pageSize);
		@SuppressWarnings("unchecked")
		List<ILiveLottery> list = (List<ILiveLottery>) page.getList();
		Iterator<ILiveLottery> iterator = list.iterator();
		Long now = new Date().getTime();
		while (iterator.hasNext()) {
			ILiveLottery iLiveLottery = (ILiveLottery) iterator.next();
			if (now>iLiveLottery.getEndTime().getTime()) {
				iLiveLottery.setIsEnd(1);
				iLiveLottery.setIsSwitch(0);
				iLivePrizeDao.update(iLiveLottery);
			}
		}
		return page;
	}

	@Override
	@Transactional
	public Long createPrizeContent(ILiveLottery lottery) {
		Long nextId = fieldIdMng.getNextId("ilive_lottery", "id", 1);
		lottery.setId(nextId);
		Timestamp now = new Timestamp(new Date().getTime());
		lottery.setCreateTime(now);
		lottery.setUpdateTime(now);
		lottery.setNumber(0);
		lottery.setIsSwitch(0);
		lottery.setIsEnd(0);
		lottery.setIsOpen(0);
		iLivePrizeDao.save(lottery);
		return nextId;
	}

	@Override
	public ILiveLottery getPrize(Long id) {
		return iLivePrizeDao.getPrize(id);
	}

	@Override
	@Transactional
	public void update(ILiveLottery lottery) {
		lottery.setUpdateTime(new Timestamp(new Date().getTime()));
		iLivePrizeDao.update(lottery);
	}

	@Override
	public List<ILiveLottery> getListByRoomId(Integer roomId) {
		return iLivePrizeDao.getListByRoomId(roomId);
	}

	@Override
	public List<ILiveLottery> getListUserH5ByRoomId(Integer roomId) {
		return iLivePrizeDao.getListUserH5ByRoomId(roomId);
	}
	

	@Override
	@Transactional
	public void closeOrStart(Long id, Integer isSwitch) {
		ILiveLottery prize = iLivePrizeDao.getPrize(id);
		prize.setIsSwitch(isSwitch);
		prize.setIsOpen(1);
		iLivePrizeDao.update(prize);
	}

	@Override
	public ILiveLottery isStartPrize(Integer roomId) {
		return iLivePrizeDao.isStartPrize(roomId);
	}

	/**
	 * 抽奖
	 */
	@Override
	@Transactional
	public Long lottery(Long userId, Long lotteryId) {
		//查询该userId的手机号
		ILiveManager user = iLiveManagerMng.getILiveManager(userId);
		if(user==null) {
			//用户不存在
			return -2l;
		}
		String mobile = user.getMobile();
		if(mobile==null) {
			//未设置手机号
			return -1l;
		}
		//抽奖参与人加一
		ILiveLottery prize = iLivePrizeDao.getPrize(lotteryId);
		prize.setNumber(prize.getNumber()+1);
		iLivePrizeDao.update(prize);
		//保存用户抽奖信息
		ILiveLotteryParktake iLiveLotteryParktake = new ILiveLotteryParktake();
		iLiveLotteryParktake.setLotteryId(lotteryId);
		iLiveLotteryParktake.setUserId(userId);
		iLiveLotteryParktake.setUserName(user.getNailName());
		iLiveLotteryParktake.setPhone(mobile);
		iLiveLotteryParktake.setCreateTime(new Timestamp(new Date().getTime()));
		
		
		//查询是否在黑名单中  如果在直接返回0
		boolean blackList = iLiveLotteryListMng.isBlackList(lotteryId, mobile);
		if (blackList) {
			iLiveLotteryParktake.setIsPrize(0);
			iLiveLotteryParktake.setPrizeId(0l);
			iLiveLotteryParktakeMng.saveUser(iLiveLotteryParktake);
			return 0l;
		}
		//获取所有奖品
		List<ILiveLotteryPrize> prizeList = iLiveLotteryPrizeMng.getListByLotteryId(lotteryId);
		ArrayList<Long> arrayList = new ArrayList<>();
		
		//根据中奖概率计算是否可以中奖
		Long[] prizeId = new Long[prizeList.size()];
		int[] proba = new int[prizeList.size()];
		int temp = 0;
		for(int i=0;i<prizeList.size();i++) {
			//设置中奖概率条
			//获取这个奖品
			ILiveLotteryPrize prize2 = prizeList.get(i);
			prizeId[i] = prize2.getId();
			proba[i] = (int)(temp + prize2.getProbability()*100);
			temp = proba[i];
			//查看是否在白名单中
			boolean iswhite = iLiveLotteryListMng.isWhiteList(prize2.getId(), mobile);
			if (iswhite) {
				arrayList.add(prize2.getId());
			}
		}
		
		//生成随机数  小公鸡点到谁我就选谁
		Random random = new Random();
		
		//是否在奖品白名单中
		if (arrayList.size()!=0) {
			int t = random.nextInt(arrayList.size());
			
			//根据奖品id获取该奖品
			ILiveLotteryPrize prizeN = iLiveLotteryPrizeMng.getById(prizeId[t]);
			if(prizeN.getNum()>0) {
				iLiveLotteryParktake.setIsPrize(1);
				iLiveLotteryParktake.setPrizeId(prizeId[t]);
				iLiveLotteryParktake.setPrize(prizeN.getPrizeName());
				iLiveLotteryParktake.setPrizepro(prizeN.getPrizeTypeName());
				//插入用户抽奖记录
				iLiveLotteryParktakeMng.saveUser(iLiveLotteryParktake);
				//减库存
				prizeN.setNum(prizeN.getNum()-1);
				iLiveLotteryPrizeMng.update(prizeN);
				//返回奖品id
				return arrayList.get(t);
			}
		}
		
		//不在黑名单也不在白名单中
		int pro = random.nextInt(10000);
		int prizeIdTemp = -1;
		for(int i=0;i<proba.length;i++) {
			if(pro<proba[i]) {
				prizeIdTemp = i;
				break;
			}
		}
		//未中奖
		if(prizeIdTemp == -1) {
			//保存抽奖信息
			iLiveLotteryParktake.setIsPrize(0);
			iLiveLotteryParktake.setPrizeId(0l);
			iLiveLotteryParktakeMng.saveUser(iLiveLotteryParktake);
			return 0l;
		}else {
			//中奖
			ILiveLotteryPrize prizeN = iLiveLotteryPrizeMng.getById(prizeId[prizeIdTemp]);
			
			//是否有库存
			if(prizeN.getNum()>0) {
				iLiveLotteryParktake.setIsPrize(1);
				iLiveLotteryParktake.setPrizeId(prizeId[prizeIdTemp]);
				iLiveLotteryParktake.setPrize(prizeN.getPrizeName());
				iLiveLotteryParktake.setPrizepro(prizeN.getPrizeTypeName());
				//插入抽奖记录
				iLiveLotteryParktakeMng.saveUser(iLiveLotteryParktake);
				//减库存
				prizeN.setNum(prizeN.getNum()-1);
				iLiveLotteryPrizeMng.update(prizeN);
				//返回奖品id
				return prizeId[prizeIdTemp];
			}else {
				iLiveLotteryParktake.setIsPrize(0);
				iLiveLotteryParktake.setPrizeId(0l);
				//插入抽奖记录
				iLiveLotteryParktakeMng.saveUser(iLiveLotteryParktake);
				return 0l;
			}
		}
	}

	@Override
	@Transactional
	public boolean createOrUpdateLotteryPrize(ILiveLottery lottery, String lotteryPrize,HashMap<String, String> hashMap) {
		Long id;
		if(lottery.getId()==null) {
			id = this.createPrizeContent(lottery);
		}else {
			id = lottery.getId();
			ILiveLottery prize = this.getPrize(id);
			prize.setName(lottery.getName());
			prize.setStartTime(lottery.getStartTime());
			prize.setEndTime(lottery.getEndTime());
			prize.setLotteryType(lottery.getLotteryType());
			prize.setLotteryNum(lottery.getLotteryNum());
			prize.setIsRepeat(lottery.getIsRepeat());
			prize.setIsLotteryRulePhone(lottery.getIsLotteryRulePhone());
			prize.setIsLotteryRuleShare(lottery.getIsLotteryRuleShare());
			this.update(prize);
		}
		Timestamp now = new Timestamp(new Date().getTime());
		if(lotteryPrize!=null) {
			JSONArray jsonArray = JSONArray.fromObject(lotteryPrize);
			iLiveLotteryPrizeMng.deleteAllByLotteryId(id);
			for(int i=0;i<jsonArray.size();i++) {
				//保存奖品
				net.sf.json.JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				ILiveLotteryPrize prize;
				//奖品id
				//long long1 = jsonObject.getLong("id");
				long long1;
				//奖品id为0则新增否则为修改
				//if(long1==0l) {
					prize = new ILiveLotteryPrize();
				//}else {
				//	prize = iLiveLotteryPrizeMng.getById(long1);
				//}
				prize.setLotteryId(id);
				prize.setPrizeTypeName(jsonObject.getString("prizeTypeName"));
				prize.setPrizeName(jsonObject.getString("prizeName"));
				prize.setNum(jsonObject.getInt("num"));
				prize.setProbability(jsonObject.getDouble("probability"));
				prize.setPrizeImg(jsonObject.getString("prizeImg"));
				prize.setCreateTime(now);
				//if(long1==0l) {
					long1 = iLiveLotteryPrizeMng.save(prize);
				//}else {
				//	iLiveLotteryPrizeMng.update(prize);
				//}
				
				//如果白名单存在则添加白名单
				String whitelist = jsonObject.getString("whitelist");
				if(whitelist != null && !whitelist.equals("")) {
					String[] str = whitelist.split(",");
					for(String string:str) {
						System.out.println(string);
						ILiveLotteryList iLiveLotteryList = new ILiveLotteryList();
						iLiveLotteryList.setListType(1);
						iLiveLotteryList.setCreateTime(now);
						iLiveLotteryList.setLotteryId(id);
						iLiveLotteryList.setLotteryPrizeId(long1);
						iLiveLotteryList.setPhone(string);
						iLiveLotteryListMng.save(iLiveLotteryList);
					}
				}
			}
		}
		try {
			if(!hashMap.isEmpty()) {
				for(Map.Entry<String, String> entry:hashMap.entrySet()) {
					ILiveLotteryList iLiveLotteryList = new ILiveLotteryList();
					iLiveLotteryList.setListType(2);
					iLiveLotteryList.setPhone(entry.getValue());
					iLiveLotteryList.setLotteryId(id);
					iLiveLotteryList.setCreateTime(now);
					iLiveLotteryListMng.save(iLiveLotteryList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	public Pagination getPageByEnterpriseId(String prizeName, Integer enterpriseId, Integer pageNo, Integer pageSize) {
		//将所有结束时间超过现在的抽奖设置为已结束
				//iLivePrizeDao.setLetEnd();
				//查询
				Pagination page = iLivePrizeDao.getPageByEnterpriseId(prizeName,enterpriseId,pageNo,pageSize);
				@SuppressWarnings("unchecked")
				List<ILiveLottery> list = (List<ILiveLottery>) page.getList();
				Iterator<ILiveLottery> iterator = list.iterator();
				Long now = new Date().getTime();
				while (iterator.hasNext()) {
					ILiveLottery iLiveLottery = (ILiveLottery) iterator.next();
					if (now>iLiveLottery.getEndTime().getTime()) {
						iLiveLottery.setIsEnd(1);
						iLiveLottery.setIsSwitch(0);
						iLivePrizeDao.update(iLiveLottery);
					}
				}
				return page;
	}

	@Override
	public ILiveLottery isEnterpriseStartPrize(Integer enterpriseId) {
		return iLivePrizeDao.isEnterpriseStartPrize(enterpriseId);
	}

	@Override
	public List<ILiveLottery> getListUserH5ByEnterpriseId(Integer enterpriseId) {
		return iLivePrizeDao.getListUserH5ByEnterpriseId(enterpriseId);
	}

	@Override
	public ILiveLottery isStartPrize2(Integer enterpriseId) {
		return iLivePrizeDao.isStartPrize2(enterpriseId);
	}

	@Override
	public List<ILiveLottery> getListUserH5ByUserId(Long userId) {
		return iLivePrizeDao.getListUserH5ByUserId(userId);
	}

	@Override
	public Pagination getpageByUserId(Long userId, String prizeName, Integer pageNo, Integer pageSize) {
		return iLivePrizeDao.getpageByUserId(userId,prizeName,pageNo,pageSize);
	}
}
