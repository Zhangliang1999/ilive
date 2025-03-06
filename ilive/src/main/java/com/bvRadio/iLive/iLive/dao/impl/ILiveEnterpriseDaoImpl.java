package com.bvRadio.iLive.iLive.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveEnterpriseDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;

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

	@Override
	public Pagination getPagerForView(String keyWord, Integer pageNo, Integer pageSize, Integer searchType) {
		Finder finder = Finder.create("select enterprise from ILiveEnterprise enterprise where 1=1 ");
		if (keyWord != null) {
			boolean roomNameInt = StringPatternUtil.isInteger(keyWord);
			if (roomNameInt) {
				finder.append(" and enterprise.enterpriseId=:enterpriseId");
				finder.setParam("enterpriseId", Integer.parseInt(keyWord));
				finder.append(" and enterprise.certStatus=4 and enterprise.isDel=0 ");
			}
			finder.append(
					" or ( enterprise.enterpriseName like :enterpriseName and enterprise.certStatus=4 and enterprise.isDel=0 )");
			finder.setParam("enterpriseName", "%" + keyWord + "%");
		}
		return find(finder, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveEnterprise> getEnterPriseByIds(List<Integer> myEnterprise) {
		Integer[] ints = new Integer[myEnterprise.size()];
		for (int i = 0; i < ints.length; i++) {
			ints[i] = myEnterprise.get(i);
		}
		Finder finder = Finder.create(
				"from ILiveEnterprise enterprise where 1=1 and enterpriseId in (:enterpriseIds)");
		finder.setParamList("enterpriseIds", ints);
		return this.find(finder);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveEnterprise> getTop4ForView(String keyWord) {
		Finder finder = Finder.create(
				"select enterprise from ILiveEnterprise enterprise where 1=1 and enterprise.certStatus=4 and enterprise.isDel=0 ");
		List<ILiveEnterprise> viewEnterprise = new ArrayList<>();
		if (keyWord != null) {
			int pageNo = 1;
			int pageSize = 4;
			if (pageNo == 1) {
				boolean roomNameInt = StringPatternUtil.isInteger(keyWord);
				if (roomNameInt) {
					finder.append(" and enterprise.enterpriseId=:enterpriseId ");
					finder.setParam("enterpriseId", Integer.parseInt(keyWord));
					List<ILiveEnterprise> find = this.find(finder);
					if (find != null && !find.isEmpty()) {
						viewEnterprise.addAll(find);
						finder = Finder.create(
								"select enterprise from ILiveEnterprise enterprise where 1=1 and enterprise.certStatus=4 and enterprise.isDel=0  and enterprise.enterpriseName like :enterpriseName ");
						finder.setParam("enterpriseName", "%" + keyWord + "%");
						finder.append(" order by applyTime desc ");
						Pagination find2 = find(finder, 1, pageSize - find.size());
						if (find2 != null) {
							List<ILiveEnterprise> list = (List<ILiveEnterprise>) find2.getList();
							if (list != null && !list.isEmpty()) {
								viewEnterprise.addAll(list);
							}
						}
					} else {
						finder = Finder.create(
								"select enterprise from ILiveEnterprise enterprise where 1=1 and enterprise.certStatus=4 and enterprise.isDel=0  and enterprise.enterpriseName like :enterpriseName ");
						finder.setParam("enterpriseName", "%" + keyWord + "%");
						finder.append(" order by applyTime desc ");
						Pagination find2 = find(finder, pageNo, pageSize);
						if (find2 != null) {
							List<ILiveEnterprise> list = (List<ILiveEnterprise>) find2.getList();
							if (list != null && !list.isEmpty()) {
								viewEnterprise.addAll(list);
							}
						}
					}
				} else {
					finder = Finder.create(
							"select enterprise from ILiveEnterprise enterprise where 1=1 and enterprise.certStatus=4 and enterprise.isDel=0  and enterprise.enterpriseName like :enterpriseName ");
					finder.setParam("enterpriseName", "%" + keyWord + "%");
					finder.append(" order by applyTime desc ");
					Pagination find2 = find(finder, pageNo, pageSize);
					if (find2 != null) {
						List<ILiveEnterprise> list = (List<ILiveEnterprise>) find2.getList();
						if (list != null && !list.isEmpty()) {
							viewEnterprise.addAll(list);
						}
					}
				}
			}
		}
		return viewEnterprise;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveEnterprise> getPagerForView(String keyWord, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create(
				"select enterprise from ILiveEnterprise enterprise where 1=1 and enterprise.certStatus=4 and enterprise.isDel=0");
		List<ILiveEnterprise> viewEnterprise = new ArrayList<>();
		if (keyWord != null && !keyWord.equals("")) {
			if (pageNo == 1) {
				boolean roomNameInt = StringPatternUtil.isInteger(keyWord);
				if (roomNameInt) {
					finder.append(" and enterprise.enterpriseId=:enterpriseId ");
					finder.setParam("enterpriseId", Integer.parseInt(keyWord));
					List<ILiveEnterprise> find = this.find(finder);
					if (find != null && !find.isEmpty()) {
						viewEnterprise.addAll(find);
						finder = Finder.create(
								"select enterprise from ILiveEnterprise enterprise where 1=1 and enterprise.certStatus=4 and enterprise.isDel=0  and enterprise.enterpriseName like :enterpriseName ");
						finder.setParam("enterpriseName", "%" + keyWord + "%");
						finder.append(" order by applyTime desc ");
						Pagination find2 = find(finder, 1, pageSize - find.size());
						if (find2 != null) {
							List<ILiveEnterprise> list = (List<ILiveEnterprise>) find2.getList();
							if (list != null && !list.isEmpty()) {
								viewEnterprise.addAll(list);
							}
						}
					} else {
						finder = Finder.create(
								"select enterprise from ILiveEnterprise enterprise where 1=1 and enterprise.certStatus=4 and enterprise.isDel=0  and enterprise.enterpriseName like :enterpriseName ");
						finder.setParam("enterpriseName", "%" + keyWord + "%");
						finder.append(" order by applyTime desc ");
						Pagination find2 = find(finder, pageNo, pageSize);
						if (find2 != null) {
							List<ILiveEnterprise> list = (List<ILiveEnterprise>) find2.getList();
							if (list != null && !list.isEmpty()) {
								viewEnterprise.addAll(list);
							}
						}
					}
				} else {
					finder = Finder.create(
							"select enterprise from ILiveEnterprise enterprise where 1=1 and enterprise.certStatus=4 and enterprise.isDel=0  and enterprise.enterpriseName like :enterpriseName ");
					finder.setParam("enterpriseName", "%" + keyWord + "%");
					finder.append(" order by applyTime desc ");
					Pagination find2 = find(finder, pageNo, pageSize);
					if (find2 != null) {
						List<ILiveEnterprise> list = (List<ILiveEnterprise>) find2.getList();
						if (list != null && !list.isEmpty()) {
							viewEnterprise.addAll(list);
						}
					}
				}
			} else {
				finder = Finder.create(
						"select enterprise from ILiveEnterprise enterprise where 1=1 and enterprise.certStatus=4 and enterprise.isDel=0  and enterprise.enterpriseName like :enterpriseName ");
				finder.setParam("enterpriseName", "%" + keyWord + "%");
				finder.append(" order by applyTime desc ");
				Pagination find2 = find(finder, pageNo, pageSize);
				if (find2 != null) {
					List<ILiveEnterprise> list = (List<ILiveEnterprise>) find2.getList();
					if (list != null && !list.isEmpty()) {
						viewEnterprise.addAll(list);
					}
				}
			}
		} else {
			finder = Finder.create(
					"select enterprise from ILiveEnterprise enterprise where 1=1 and enterprise.certStatus=4 and enterprise.isDel=0 ");
			finder.append(" order by enterprise.applyTime desc ");
			Pagination find2 = find(finder, pageNo, pageSize);
			if (find2 != null) {
				List<ILiveEnterprise> list = (List<ILiveEnterprise>) find2.getList();
				if (list != null && !list.isEmpty()) {
					viewEnterprise.addAll(list);
				}
			}
		}
		return viewEnterprise;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveEnterprise> getTop4ForView1(String keyWord) {
		Finder finder = Finder.create(
				"select enterprise from ILiveEnterprise enterprise where 1=1 and enterprise.certStatus=4 and enterprise.isDel=0 and (enterprise.privacy=0 or enterprise.privacy is null)");
		List<ILiveEnterprise> viewEnterprise = new ArrayList<>();
		if (keyWord != null) {
			int pageNo = 1;
			int pageSize = 4;
			if (pageNo == 1) {
				boolean roomNameInt = StringPatternUtil.isInteger(keyWord);
				if (roomNameInt) {
					finder.append(" and enterprise.enterpriseId=:enterpriseId ");
					finder.setParam("enterpriseId", Integer.parseInt(keyWord));
					List<ILiveEnterprise> find = this.find(finder);
					if (find != null && !find.isEmpty()) {
						viewEnterprise.addAll(find);
						finder = Finder.create(
								"select enterprise from ILiveEnterprise enterprise where 1=1 and enterprise.certStatus=4 and enterprise.isDel=0 and (enterprise.privacy=0 or enterprise.privacy is null) and enterprise.enterpriseName like :enterpriseName ");
						finder.setParam("enterpriseName", "%" + keyWord + "%");
						finder.append(" order by applyTime desc ");
						Pagination find2 = find(finder, 1, pageSize - find.size());
						if (find2 != null) {
							List<ILiveEnterprise> list = (List<ILiveEnterprise>) find2.getList();
							if (list != null && !list.isEmpty()) {
								viewEnterprise.addAll(list);
							}
						}
					} else {
						finder = Finder.create(
								"select enterprise from ILiveEnterprise enterprise where 1=1 and enterprise.certStatus=4 and enterprise.isDel=0 and (enterprise.privacy=0 or enterprise.privacy is null) and enterprise.enterpriseName like :enterpriseName ");
						finder.setParam("enterpriseName", "%" + keyWord + "%");
						finder.append(" order by applyTime desc ");
						Pagination find2 = find(finder, pageNo, pageSize);
						if (find2 != null) {
							List<ILiveEnterprise> list = (List<ILiveEnterprise>) find2.getList();
							if (list != null && !list.isEmpty()) {
								viewEnterprise.addAll(list);
							}
						}
					}
				} else {
					finder = Finder.create(
							"select enterprise from ILiveEnterprise enterprise where 1=1 and enterprise.certStatus=4 and enterprise.isDel=0 and (enterprise.privacy=0 or enterprise.privacy is null) and enterprise.enterpriseName like :enterpriseName ");
					finder.setParam("enterpriseName", "%" + keyWord + "%");
					finder.append(" order by applyTime desc ");
					Pagination find2 = find(finder, pageNo, pageSize);
					if (find2 != null) {
						List<ILiveEnterprise> list = (List<ILiveEnterprise>) find2.getList();
						if (list != null && !list.isEmpty()) {
							viewEnterprise.addAll(list);
						}
					}
				}
			}
		}
		return viewEnterprise;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveEnterprise> getPagerForView1(String keyWord, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create(
				"select enterprise from ILiveEnterprise enterprise where 1=1 and enterprise.certStatus=4 and enterprise.isDel=0 and  (enterprise.privacy=0 or enterprise.privacy is null)");
		List<ILiveEnterprise> viewEnterprise = new ArrayList<>();
		if (keyWord != null && !keyWord.equals("")) {
			if (pageNo == 1) {
				boolean roomNameInt = StringPatternUtil.isInteger(keyWord);
				if (roomNameInt) {
					finder.append(" and enterprise.enterpriseId=:enterpriseId ");
					finder.setParam("enterpriseId", Integer.parseInt(keyWord));
					List<ILiveEnterprise> find = this.find(finder);
					if (find != null && !find.isEmpty()) {
						viewEnterprise.addAll(find);
						finder = Finder.create(
								"select enterprise from ILiveEnterprise enterprise where 1=1 and enterprise.certStatus=4 and enterprise.isDel=0 and (enterprise.privacy=0 or enterprise.privacy is null) and enterprise.enterpriseName like :enterpriseName ");
						finder.setParam("enterpriseName", "%" + keyWord + "%");
						finder.append(" order by applyTime desc ");
						Pagination find2 = find(finder, 1, pageSize - find.size());
						if (find2 != null) {
							List<ILiveEnterprise> list = (List<ILiveEnterprise>) find2.getList();
							if (list != null && !list.isEmpty()) {
								viewEnterprise.addAll(list);
							}
						}
					} else {
						finder = Finder.create(
								"select enterprise from ILiveEnterprise enterprise where 1=1 and enterprise.certStatus=4 and enterprise.isDel=0 and (enterprise.privacy=0 or enterprise.privacy is null) and enterprise.enterpriseName like :enterpriseName ");
						finder.setParam("enterpriseName", "%" + keyWord + "%");
						finder.append(" order by applyTime desc ");
						Pagination find2 = find(finder, pageNo, pageSize);
						if (find2 != null) {
							List<ILiveEnterprise> list = (List<ILiveEnterprise>) find2.getList();
							if (list != null && !list.isEmpty()) {
								viewEnterprise.addAll(list);
							}
						}
					}
				} else {
					finder = Finder.create(
							"select enterprise from ILiveEnterprise enterprise where 1=1 and enterprise.certStatus=4 and enterprise.isDel=0 and (enterprise.privacy=0 or enterprise.privacy is null) and enterprise.enterpriseName like :enterpriseName ");
					finder.setParam("enterpriseName", "%" + keyWord + "%");
					finder.append(" order by applyTime desc ");
					Pagination find2 = find(finder, pageNo, pageSize);
					if (find2 != null) {
						List<ILiveEnterprise> list = (List<ILiveEnterprise>) find2.getList();
						if (list != null && !list.isEmpty()) {
							viewEnterprise.addAll(list);
						}
					}
				}
			} else {
				finder = Finder.create(
						"select enterprise from ILiveEnterprise enterprise where 1=1 and enterprise.certStatus=4 and enterprise.isDel=0 and (enterprise.privacy=0 or enterprise.privacy is null) and enterprise.enterpriseName like :enterpriseName ");
				finder.setParam("enterpriseName", "%" + keyWord + "%");
				finder.append(" order by applyTime desc ");
				Pagination find2 = find(finder, pageNo, pageSize);
				if (find2 != null) {
					List<ILiveEnterprise> list = (List<ILiveEnterprise>) find2.getList();
					if (list != null && !list.isEmpty()) {
						viewEnterprise.addAll(list);
					}
				}
			}
		} else {
			finder = Finder.create(
					"select enterprise from ILiveEnterprise enterprise where 1=1 and enterprise.certStatus=4 and enterprise.isDel=0 (enterprise.privacy=0 or enterprise.privacy is null)");
			finder.append(" order by enterprise.applyTime desc ");
			Pagination find2 = find(finder, pageNo, pageSize);
			if (find2 != null) {
				List<ILiveEnterprise> list = (List<ILiveEnterprise>) find2.getList();
				if (list != null && !list.isEmpty()) {
					viewEnterprise.addAll(list);
				}
			}
		}
		return viewEnterprise;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveEnterprise> getBatchEnterpriseForStatics(Integer startId, Integer size) {
		String hql = " from ILiveEnterprise where isDel=0 ";
		Finder finder = Finder.create(hql);
		if (null != startId) {
			finder.append(" and enterpriseId > :startId");
			finder.setParam("startId", startId);
		}
		Query query = getSession().createQuery(finder.getOrigHql());
		finder.setParamsToQuery(query);
		query.setMaxResults(size);
		if (finder.isCacheable()) {
			query.setCacheable(true);
		}
		List<ILiveEnterprise> list = query.list();
		return list;
	}
	@Override
	public void updateByBean(ILiveEnterprise iLiveEnterprise) {
		this.getSession().update(iLiveEnterprise);
	}

	@Override
	public ILiveEnterprise getILiveEnterPriseByAppId(String appId) {
		String hql = " from ILiveEnterprise where isDel=0 ";
		Finder finder = Finder.create(hql);
		if (null != appId) {
			finder.append(" and appId = :appId");
			finder.setParam("appId", appId);
		}
		Query query = getSession().createQuery(finder.getOrigHql());
		finder.setParamsToQuery(query);
		
		List<ILiveEnterprise> list = query.list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
}
