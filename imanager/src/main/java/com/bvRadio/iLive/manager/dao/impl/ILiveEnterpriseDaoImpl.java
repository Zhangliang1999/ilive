package com.bvRadio.iLive.manager.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.impl.ILiveManagerMngImpl;
import com.bvRadio.iLive.manager.dao.ILiveEnterpriseDao;

@Repository
public class ILiveEnterpriseDaoImpl extends HibernateBaseDao<ILiveEnterprise, Integer> implements ILiveEnterpriseDao {

	@Override
	protected Class<ILiveEnterprise> getEntityClass() {
		return ILiveEnterprise.class;
	}

	@Override
	public ILiveEnterprise getILiveEnterpriseById(Integer enterpriseId) {
		return this.get(enterpriseId);
	}

	@Override
	public boolean saveILiveEnterprise(ILiveEnterprise iLiveEnterprise) {
		this.getSession().save(iLiveEnterprise);
		return true;
	}

	@Override
	public boolean deleteILiveEnterprise(Integer enterpriseId) {
		Session session = this.getSession();
		ILiveEnterprise enterprise = (ILiveEnterprise) session.get(ILiveEnterprise.class, enterpriseId);
		session.delete(enterprise);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveEnterprise> getList() {
		String hql = "from ILiveEnterprise where isDel ==0 ";
		Query query = this.getSession().createQuery(hql);
		List<ILiveEnterprise> list = query.list();
		return list;
	}

	@Override
	public int getUserNum() {
		String hql = "select count(*) from ILiveEnterprise where isDel =0 ";
		Query query = this.getSession().createQuery(hql.toString());
		if((Long)query.uniqueResult() == null) {
			return 0;
		}
		Long num = (Long)query.uniqueResult();
		return num.intValue();
	}

	@Override
	public int getContractUserNum() {
		String hql = "select count(*) from ILiveEnterprise where isDel =0 and entype = 3";
		Query query = this.getSession().createQuery(hql.toString());
		if((Long)query.uniqueResult() == null) {
			return 0;
		}
		Long num = (Long)query.uniqueResult();
		return num.intValue();
	}

	@Override
	public Pagination getPage(String enterprisetype, String content, int pass, Integer pageNo, int pageSize) {
		Finder finder = null;
		finder = Finder
				.create("select bean from ILiveEnterprise bean where bean.certStatus =:certStatus and bean.isDel = 0");
		finder.setParam("certStatus", pass);
		if (content != null) {
			finder.append(" and bean.enterpriseName like :enterpriseName");
			finder.setParam("enterpriseName", "%" + content + "%");
		}
		if ((enterprisetype != null) && (!("0").equals(enterprisetype))) {
			finder.append(" and bean.enterpriseType = :enterpriseType");
			finder.setParam("enterpriseType", enterprisetype);
		}
		finder.append(" order by certTime desc ");
		return find(finder, pageNo == null ? 1 : pageNo, pageSize);
	}

	@Override
	public Pagination getPage(String content, int pass, Integer pageNo, int pageSize) {
		Finder finder = null;
		if (pass == 1) {
			finder = Finder
					.create("select bean from ILiveEnterprise bean where bean.certStatus = 2 and bean.isDel = 0");
		} else {
			finder = Finder
					.create("select bean from ILiveEnterprise bean where bean.certStatus = 0 and bean.isDel = 0");
		}
		if (content != null) {
			finder.append(" and bean.enterpriseRegName like :enterpriseRegName");
			finder.setParam("enterpriseRegName", "%" + content + "%");
		}
		return find(finder, pageNo == null ? 1 : pageNo, pageSize);
	}

	@Override
	public void update(ILiveEnterprise iLiveEnterprise) {
		/*String hql = "update ILiveEnterprise set certStatus = :certStatus where enterpriseId = :enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("certStatus", iLiveEnterprise.getCertStatus());
		query.setParameter("enterpriseId", iLiveEnterprise.getEnterpriseId());
		query.executeUpdate();*/
		this.getSession().update(iLiveEnterprise);
	}

	@Override
	public void updateByBean(ILiveEnterprise iLiveEnterprise) {
		this.getSession().update(iLiveEnterprise);
	}

	@Override
	public void del(Integer enterpriseId) {
		String hql = "update ILiveEnterprise set isDel = 1 where enterpriseId = :enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		query.executeUpdate();
	}

	@Override
	public Pagination getILiveEnterprisesByCertStatus(Integer certStauts, Integer pageNo, Integer pageSize) {
		Finder finder = Finder
				.create("from ILiveEnterprise bean where bean.certStatus =:certStatus and bean.isDel = 0");
		finder.setParam("certStatus", certStauts);
		finder.append(" order by bean.enterpriseId desc");
		return this.find(finder, pageNo == null ? 1 : pageNo, pageSize == null ? 20 : pageSize);
	}
	
	@Override
	public Pagination getILiveEnterprisesByCertStatusByList(List<Integer> certStatusList,String enterpriseType,String content, Integer pageNo, Integer pageSize) {
		Finder finder = Finder
				.create("from ILiveEnterprise bean where bean.certStatus in (:certStatusList) and bean.isDel = 0");
		Integer[] array = new Integer[certStatusList.size()];
		for(int i=0;i<array.length;i++) {
			array[i] = certStatusList.get(i);
		}
		finder.setParamList("certStatusList", array);
//		if(enterpriseType!=null && !enterpriseType.equals("0")) {
//			finder.append(" and bean.enterpriseType = :enterpriseType");
//			finder.setParam("enterpriseType", enterpriseType);
//		}
		if(content!=null&&!"".equals(content)) {
			finder.append(" and bean.enterpriseName like :enterpriseName");
			finder.setParam("enterpriseName", "%"+content+"%");
		}
		finder.append(" order by bean.enterpriseId desc");
		return this.find(finder, pageNo == null ? 1 : pageNo, pageSize == null ? 20 : pageSize);
	}
	@Override
	public Pagination getILiveEnterprisesByCertStatusByList(List<Integer> certStatusList,String enterpriseType,String content, Integer pageNo, Integer pageSize,Integer stamp) {
		Finder finder = Finder
				.create("from ILiveEnterprise bean where bean.certStatus in (:certStatusList) and bean.isDel = 0 and bean.stamp = :stamp ");
		Integer[] array = new Integer[certStatusList.size()];
		for(int i=0;i<array.length;i++) {
			array[i] = certStatusList.get(i);
		}
		finder.setParamList("certStatusList", array);
		finder.setParam("stamp",stamp);
		/*if(enterpriseType!=null && !enterpriseType.equals("0")) {
			finder.append(" and bean.enterpriseType = :enterpriseType");
			finder.setParam("enterpriseType", enterpriseType);
		}*/
		if(content!=null&&!"".equals(content)) {
			finder.append(" and bean.userPhone like :userPhone");
			finder.setParam("userPhone", "%"+content+"%");
		}
		finder.append(" order by bean.applyTime desc");
		return this.find(finder, pageNo == null ? 1 : pageNo, pageSize == null ? 20 : pageSize);
	}

	@Override
	public Pagination getPage(ILiveEnterprise iLiveEnterprise, Integer pageNo, int pageSize) {
		Finder finder = null;
		finder = Finder
				.create("select bean from ILiveEnterprise bean where bean.isDel = 0 and bean.certStatus = 4");
		if (iLiveEnterprise.getEnterpriseId()!= null) {
			finder.append(" and bean.enterpriseId = :enterpriseId");
			finder.setParam("enterpriseId", iLiveEnterprise.getEnterpriseId());
		}
		if ((iLiveEnterprise.getEnterpriseName() != null) && (!("").equals(iLiveEnterprise.getEnterpriseName()))) {
			finder.append(" and bean.enterpriseName like :enterpriseName");
			finder.setParam("enterpriseName", "%"+iLiveEnterprise.getEnterpriseName()+"%");
		}
		if ((iLiveEnterprise.getContactName() != null) && (!("").equals(iLiveEnterprise.getContactName()))) {
			finder.append(" and bean.contactName like :contactName");
			finder.setParam("contactName","%" + iLiveEnterprise.getContactName()+"%");
		}
		if (iLiveEnterprise.getUserPhone()!=null && !iLiveEnterprise.getUserPhone().equals("")) {
			finder.append(" and bean.userPhone like :userPhone");
			finder.setParam("userPhone", "%"+iLiveEnterprise.getUserPhone()+"%");
		}
		if (iLiveEnterprise.getDisabled()!=null && iLiveEnterprise.getDisabled()!=-1) {
			finder.append(" and bean.disabled = :disabled");
			finder.setParam("disabled", iLiveEnterprise.getDisabled());
		}
		if (iLiveEnterprise.getEntype()!=null && iLiveEnterprise.getEntype()!=-1) {
			finder.append(" and bean.entype = :entype");
			finder.setParam("entype", iLiveEnterprise.getEntype());
		}
		if (iLiveEnterprise.getStamp()!=null && iLiveEnterprise.getStamp()!=-1) {
			finder.append(" and bean.stamp = :stamp");
			finder.setParam("stamp", iLiveEnterprise.getStamp());
		}
		if (iLiveEnterprise.getAutoUserName()!=null && !iLiveEnterprise.getAutoUserName().equals("")) {
			finder.append(" and bean.autoUserName like :autoUserName");
			finder.setParam("autoUserName", "%"+iLiveEnterprise.getAutoUserName()+"%");
		}
		finder.append(" order by certTime desc ");
		
		if(iLiveEnterprise.getRegisterStartTime()!=null||iLiveEnterprise.getRegisterEndTime()!=null
				||iLiveEnterprise.getAuthStartTime()!=null||iLiveEnterprise.getAuthEndTime()!=null) {
			@SuppressWarnings("unchecked")
			List<ILiveEnterprise> list = find(finder);
			Iterator<ILiveEnterprise> iterator = list.iterator();
			while (iterator.hasNext()) {
				ILiveEnterprise enterprise = (ILiveEnterprise) iterator.next();
				if ((iLiveEnterprise.getRegisterStartTime()!=null
						||iLiveEnterprise.getRegisterEndTime()!=null) 
						&&enterprise.getApplyTime()==null) {
					iterator.remove();
					continue;
				}
				if ((iLiveEnterprise.getAuthStartTime()==null
						||iLiveEnterprise.getAuthEndTime()==null)
						&&enterprise.getCertTime()==null) {
					iterator.remove();
					continue;
				}
				if (iLiveEnterprise.getRegisterStartTime()!=null&&enterprise.getApplyTime()!=null&&enterprise.getApplyTime().getTime()<iLiveEnterprise.getRegisterStartTime().getTime()) {
					iterator.remove();
					continue;
				}
				if (iLiveEnterprise.getRegisterEndTime()!=null&&enterprise.getApplyTime()!=null&&enterprise.getApplyTime().getTime()>iLiveEnterprise.getRegisterEndTime().getTime()) {
					iterator.remove();
					continue;
				}
				if (iLiveEnterprise.getAuthStartTime()!=null&&enterprise.getCertTime()!=null&&enterprise.getCertTime().getTime()<iLiveEnterprise.getAuthStartTime().getTime()) {
					iterator.remove();
					continue;
				}
				if (iLiveEnterprise.getAuthEndTime()!=null&&enterprise.getCertTime()!=null&&enterprise.getCertTime().getTime()>iLiveEnterprise.getAuthEndTime().getTime()) {
					iterator.remove();
					continue;
				}
			}
			List<ILiveEnterprise> list2 = new ArrayList<>();
			int no = pageNo==null?1:pageNo;
			for(int i=0;i<pageSize;i++) {
				if (i+(no-1)*pageSize>=list.size()) {
					break;
				}
				list2.add(list.get(i+(no-1)*pageSize));
			}
			System.out.println("pageNo： "+pageNo+"   总共多少条数据："+list.size());
			return new Pagination(no, pageSize, list.size(), list2);
		}
		Pagination find2 = find(finder, pageNo == null ? 1 : pageNo, pageSize);
		return find2;
	}

	@Override
	public List<ILiveEnterprise> getList(ILiveEnterprise iLiveEnterprise) {
		Finder finder = null;
		finder = Finder
				.create("select bean from ILiveEnterprise bean where bean.isDel = 0 and bean.certStatus = 4");
		if (iLiveEnterprise.getEnterpriseId()!= null) {
			finder.append(" and bean.enterpriseId = :enterpriseId");
			finder.setParam("enterpriseId", iLiveEnterprise.getEnterpriseId());
		}
		if ((iLiveEnterprise.getEnterpriseName() != null) && (!("").equals(iLiveEnterprise.getEnterpriseName()))) {
			finder.append(" and bean.enterpriseName like :enterpriseName");
			finder.setParam("enterpriseName", "%"+iLiveEnterprise.getEnterpriseName()+"%");
		}
		if ((iLiveEnterprise.getContactName() != null) && (!("").equals(iLiveEnterprise.getContactName()))) {
			finder.append(" and bean.contactName like :contactName");
			finder.setParam("contactName","%" + iLiveEnterprise.getContactName()+"%");
		}
		if (iLiveEnterprise.getUserPhone()!=null && !iLiveEnterprise.getUserPhone().equals("")) {
			finder.append(" and bean.userPhone like :userPhone");
			finder.setParam("userPhone", "%"+iLiveEnterprise.getUserPhone()+"%");
		}
		if (iLiveEnterprise.getDisabled()!=null && iLiveEnterprise.getDisabled()!=-1) {
			finder.append(" and bean.disabled = :disabled");
			finder.setParam("disabled", iLiveEnterprise.getDisabled());
		}
		if (iLiveEnterprise.getEntype()!=null && iLiveEnterprise.getEntype()!=-1) {
			finder.append(" and bean.entype = :entype");
			finder.setParam("entype", iLiveEnterprise.getEntype());
		}
		if (iLiveEnterprise.getStamp()!=null && iLiveEnterprise.getStamp()!=-1) {
			finder.append(" and bean.stamp = :stamp");
			finder.setParam("stamp", iLiveEnterprise.getStamp());
		}
		if (iLiveEnterprise.getAutoUserName()!=null && !iLiveEnterprise.getAutoUserName().equals("")) {
			finder.append(" and bean.autoUserName like :autoUserName");
			finder.setParam("autoUserName", "%"+iLiveEnterprise.getAutoUserName()+"%");
		}
		finder.append(" order by certTime desc ");
		
		if(iLiveEnterprise.getRegisterStartTime()!=null||iLiveEnterprise.getRegisterEndTime()!=null
				||iLiveEnterprise.getAuthStartTime()!=null||iLiveEnterprise.getAuthEndTime()!=null) {
			@SuppressWarnings("unchecked")
			List<ILiveEnterprise> list = find(finder);
			Iterator<ILiveEnterprise> iterator = list.iterator();
			while (iterator.hasNext()) {
				ILiveEnterprise enterprise = (ILiveEnterprise) iterator.next();
				if ((iLiveEnterprise.getRegisterStartTime()!=null
						||iLiveEnterprise.getRegisterEndTime()!=null) 
						&&enterprise.getApplyTime()==null) {
					iterator.remove();
					continue;
				}
				if ((iLiveEnterprise.getAuthStartTime()==null
						||iLiveEnterprise.getAuthEndTime()==null)
						&&enterprise.getCertTime()==null) {
					iterator.remove();
					continue;
				}
				
				if (iLiveEnterprise.getRegisterStartTime()!=null&&enterprise.getApplyTime()!=null&&enterprise.getApplyTime().getTime()<iLiveEnterprise.getRegisterStartTime().getTime()) {
					iterator.remove();
					continue;
				}
				if (iLiveEnterprise.getRegisterEndTime()!=null&&enterprise.getApplyTime()!=null&&enterprise.getApplyTime().getTime()>iLiveEnterprise.getRegisterEndTime().getTime()) {
					iterator.remove();
					continue;
				}
				if (iLiveEnterprise.getAuthStartTime()!=null&&enterprise.getCertTime()!=null&&enterprise.getCertTime().getTime()<iLiveEnterprise.getAuthStartTime().getTime()) {
					iterator.remove();
					continue;
				}
				if (iLiveEnterprise.getAuthEndTime()!=null&&enterprise.getCertTime()!=null&&enterprise.getCertTime().getTime()>iLiveEnterprise.getAuthEndTime().getTime()) {
					iterator.remove();
					continue;
				}
			}
			return list;
		}
		List<ILiveEnterprise> find2 = find(finder);
		return find2;
	}

	@Override
	public Pagination getPageByAppId(String appID, Integer pageNo, Integer pageSize) {
		Finder finder = Finder
				.create("from ILiveEnterprise bean where bean.appId =:appId and bean.isDel = 0");
		finder.setParam("appId", appID);
		finder.append(" order by bean.enterpriseId desc");
		return this.find(finder, pageNo == null ? 1 : pageNo, pageSize == null ? 20 : pageSize);
	}

	@Override
	public Pagination getautoPage(ILiveEnterprise iLiveEnterprise, Integer pageNo, Integer pageSize) {
		Finder finder = null;
		finder = Finder
				.create("select bean from ILiveEnterprise bean where bean.isDel = 0 and bean.certStatus = 4 and bean.autoBuy=1");
		if (iLiveEnterprise.getEnterpriseId()!= null) {
			finder.append(" and bean.enterpriseId = :enterpriseId");
			finder.setParam("enterpriseId", iLiveEnterprise.getEnterpriseId());
		}
		if ((iLiveEnterprise.getEnterpriseName() != null) && (!("").equals(iLiveEnterprise.getEnterpriseName()))) {
			finder.append(" and bean.enterpriseName like :enterpriseName");
			finder.setParam("enterpriseName", "%"+iLiveEnterprise.getEnterpriseName()+"%");
		}
		if ((iLiveEnterprise.getContactName() != null) && (!("").equals(iLiveEnterprise.getContactName()))) {
			finder.append(" and bean.contactName like :contactName");
			finder.setParam("contactName","%" + iLiveEnterprise.getContactName()+"%");
		}
		if (iLiveEnterprise.getUserPhone()!=null && !iLiveEnterprise.getUserPhone().equals("")) {
			finder.append(" and bean.userPhone like :userPhone");
			finder.setParam("userPhone", "%"+iLiveEnterprise.getUserPhone()+"%");
		}
		if (iLiveEnterprise.getDisabled()!=null && iLiveEnterprise.getDisabled()!=-1) {
			finder.append(" and bean.disabled = :disabled");
			finder.setParam("disabled", iLiveEnterprise.getDisabled());
		}
		if (iLiveEnterprise.getEntype()!=null && iLiveEnterprise.getEntype()!=-1) {
			finder.append(" and bean.entype = :entype");
			finder.setParam("entype", iLiveEnterprise.getEntype());
		}
		if (iLiveEnterprise.getStamp()!=null && iLiveEnterprise.getStamp()!=-1) {
			finder.append(" and bean.stamp = :stamp");
			finder.setParam("stamp", iLiveEnterprise.getStamp());
		}
		if (iLiveEnterprise.getAutoUserName()!=null && !iLiveEnterprise.getAutoUserName().equals("")) {
			finder.append(" and bean.autoUserName like :autoUserName");
			finder.setParam("autoUserName", "%"+iLiveEnterprise.getAutoUserName()+"%");
		}
		finder.append(" order by certTime desc ");
		
		if(iLiveEnterprise.getRegisterStartTime()!=null||iLiveEnterprise.getRegisterEndTime()!=null
				||iLiveEnterprise.getAuthStartTime()!=null||iLiveEnterprise.getAuthEndTime()!=null) {
			@SuppressWarnings("unchecked")
			List<ILiveEnterprise> list = find(finder);
			Iterator<ILiveEnterprise> iterator = list.iterator();
			while (iterator.hasNext()) {
				ILiveEnterprise enterprise = (ILiveEnterprise) iterator.next();
				if ((iLiveEnterprise.getRegisterStartTime()!=null
						||iLiveEnterprise.getRegisterEndTime()!=null) 
						&&enterprise.getApplyTime()==null) {
					iterator.remove();
					continue;
				}
				if ((iLiveEnterprise.getAuthStartTime()==null
						||iLiveEnterprise.getAuthEndTime()==null)
						&&enterprise.getCertTime()==null) {
					iterator.remove();
					continue;
				}
				if (iLiveEnterprise.getRegisterStartTime()!=null&&enterprise.getApplyTime()!=null&&enterprise.getApplyTime().getTime()<iLiveEnterprise.getRegisterStartTime().getTime()) {
					iterator.remove();
					continue;
				}
				if (iLiveEnterprise.getRegisterEndTime()!=null&&enterprise.getApplyTime()!=null&&enterprise.getApplyTime().getTime()>iLiveEnterprise.getRegisterEndTime().getTime()) {
					iterator.remove();
					continue;
				}
				if (iLiveEnterprise.getAuthStartTime()!=null&&enterprise.getCertTime()!=null&&enterprise.getCertTime().getTime()<iLiveEnterprise.getAuthStartTime().getTime()) {
					iterator.remove();
					continue;
				}
				if (iLiveEnterprise.getAuthEndTime()!=null&&enterprise.getCertTime()!=null&&enterprise.getCertTime().getTime()>iLiveEnterprise.getAuthEndTime().getTime()) {
					iterator.remove();
					continue;
				}
			}
			List<ILiveEnterprise> list2 = new ArrayList<>();
			int no = pageNo==null?1:pageNo;
			for(int i=0;i<pageSize;i++) {
				if (i+(no-1)*pageSize>=list.size()) {
					break;
				}
				list2.add(list.get(i+(no-1)*pageSize));
			}
			System.out.println("pageNo： "+pageNo+"   总共多少条数据："+list.size());
			return new Pagination(no, pageSize, list.size(), list2);
		}
		Pagination find2 = find(finder, pageNo == null ? 1 : pageNo, pageSize);
		return find2;
	}

}
