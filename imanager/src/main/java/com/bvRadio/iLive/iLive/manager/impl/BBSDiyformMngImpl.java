package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.hibernate3.Updater;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.BBSDiyOptionDao;
import com.bvRadio.iLive.iLive.dao.BBSDiyformDao;
import com.bvRadio.iLive.iLive.dao.BBSDiymodelDao;
import com.bvRadio.iLive.iLive.dao.ILiveFieldIdManagerDao;
import com.bvRadio.iLive.iLive.entity.BBSDiyform;
import com.bvRadio.iLive.iLive.entity.BBSDiyformOption;
import com.bvRadio.iLive.iLive.entity.BBSDiymodel;
import com.bvRadio.iLive.iLive.manager.BBSDiyformMng;

@Service
@Transactional
public class BBSDiyformMngImpl implements BBSDiyformMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public Pagination getaVotePage(String voteName, Integer pageNo, Integer pageSize) {
		Pagination page = dao.getPageByParam(voteName, pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public List<BBSDiyform> getList() {
		List<BBSDiyform> list = dao.getList();
		return list;
	}

	@Transactional(readOnly = true)
	public BBSDiyform findById(Integer id) {
		BBSDiyform entity = dao.findById(id);
		return entity;
	}

	public BBSDiyform save(BBSDiyform bbsDiyform) {
		long nextIdLong = iLiveFieldIdManagerDao.getNextId("bbs_diyform", "diyform_id", 1);
		int nextId = (int) nextIdLong;
		if (nextId != -1) {
			bbsDiyform.setDiyformId(nextId);
			dao.save(bbsDiyform);
		}
		return bbsDiyform;
	}

	public BBSDiyform save(BBSDiyform bbsDiyform, List<BBSDiymodel> bbsDiymodelList) {
		long nextIdLong = iLiveFieldIdManagerDao.getNextId("bbs_diyform", "diyform_id", 1);
		int nextId = (int) nextIdLong;
		if (nextId != -1) {
			bbsDiyform.setDiyformId(nextId);
			bbsDiyform.setCreateTime(new Timestamp(new Date().getTime()));
			dao.save(bbsDiyform);
			if (null != bbsDiymodelList && bbsDiymodelList.size() > 0) {
				long nextIdLong2 = iLiveFieldIdManagerDao.getNextId("bbs_diymodel", "diymodel_id",
						bbsDiymodelList.size());
				nextId = (int) nextIdLong2;
				if (nextId != -1) {
					Integer diymodelId = nextId - bbsDiymodelList.size();
					for (int i = 0; i < bbsDiymodelList.size(); i++) {
						try {
							BBSDiymodel bbsDiymodel = bbsDiymodelList.get(i);
							// 过滤 html标签
							String diymodelTitle = bbsDiymodel.getDiymodelTitle();
							String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
							Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
							Matcher m_html = p_html.matcher(diymodelTitle);
							diymodelTitle = m_html.replaceAll(""); // 过滤html标签
							bbsDiymodel.setDiymodelId(diymodelId);
							bbsDiymodel.setBbsDiyform(bbsDiyform);
							bbsDiymodel.setCreateTime(new Timestamp(new Date().getTime()));
							diymodelDao.save(bbsDiymodel);
//							if (StringUtils.isNotBlank(bbsDiymodel.getOptValue())) {
//								String[] optionArr = bbsDiymodel.getOptValue().split("@;@");
//								if (optionArr != null) {
//									for (int j = 0; j < optionArr.length; j++) {
//										long optionId = iLiveFieldIdManagerDao.getNextId("bbs_diyform_option",
//												"option_id", 1);
//										String optionStr = optionArr[j];
//										BBSDiyformOption bbsDiyformOption = new BBSDiyformOption();
//										bbsDiyformOption.setOptionId(optionId);
//										bbsDiyformOption.setOptionOrder(j);
//										bbsDiyformOption.setOptionName(optionStr);
//										bbsDiyformOption.setBbsDiymodel(bbsDiymodel);
//										bBSDiyOptionDao.save(bbsDiyformOption);
//									}
//								}
//							}
							diymodelId++;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return bbsDiyform;
	}

	public BBSDiyform update(BBSDiyform bbsDiyform, List<BBSDiymodel> bbsDiymodelList) {
		if (null != bbsDiyform && null != bbsDiyform.getDiyformId()) {
			dao.update(bbsDiyform);
			long nextId = iLiveFieldIdManagerDao.getNextId("bbs_diymodel", "diymodel_id", bbsDiymodelList.size());
			if (nextId != -1) {
				Integer diymodelId = (int) nextId - bbsDiymodelList.size();
				diymodelDao.deleteByDiyformId(bbsDiyform.getDiyformId());
				for (int i = 0; i < bbsDiymodelList.size(); i++) {
					try {
						BBSDiymodel bbsDiymodel = bbsDiymodelList.get(i);
						bbsDiymodel.setDiymodelId(diymodelId);
						bbsDiymodel.setBbsDiyform(bbsDiyform);
						bbsDiymodel.setCreateTime(new Timestamp(new Date().getTime()));
						diymodelDao.save(bbsDiymodel);
						diymodelId++;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return bbsDiyform;
	}

	public void updateUser(BBSDiyform bbsDiyform) {
		Updater<BBSDiyform> updater = new Updater<BBSDiyform>(bbsDiyform);
		dao.updateByUpdater(updater);
	}

	public BBSDiyform deleteById(Integer id) {
		BBSDiyform bean = dao.deleteById(id);
		return bean;
	}

	@Transactional
	public BBSDiyform deleteVoteById(Integer id) {
		BBSDiyform bBSDiyform = findById(id);
		bBSDiyform.setDelFlag(1);
		dao.update(bBSDiyform);
		return bBSDiyform;
	}

	public BBSDiyform[] deleteByIds(Integer[] ids) {
		BBSDiyform[] beans = new BBSDiyform[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BBSDiyformDao dao;
	private BBSDiymodelDao diymodelDao;
	private ILiveFieldIdManagerDao iLiveFieldIdManagerDao;
	private BBSDiyOptionDao bBSDiyOptionDao;

	@Autowired
	public void setDao(BBSDiyformDao dao) {
		this.dao = dao;
	}

	@Autowired
	public void setDiymodelDao(BBSDiymodelDao diymodelDao) {
		this.diymodelDao = diymodelDao;
	}

	@Autowired
	public void setiLiveFieldIdManagerDao(ILiveFieldIdManagerDao iLiveFieldIdManagerDao) {
		this.iLiveFieldIdManagerDao = iLiveFieldIdManagerDao;
	}

	@Override
	public BBSDiyform getDiyfromById(int formId) {
		return dao.findById(formId);
	}

	@Autowired
	public void setBBSDiyOptionDao(BBSDiyOptionDao bBSDiyOptionDao) {
		this.bBSDiyOptionDao = bBSDiyOptionDao;
	}

}
