package com.bvRadio.iLive.iLive.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.constants.ILivePlayStatusConstant;
import com.bvRadio.iLive.iLive.dao.ILiveLiveRoomDao;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;

@Repository
public class ILiveLiveRoomDaoImpl extends HibernateBaseDao<ILiveLiveRoom, Integer> implements ILiveLiveRoomDao {
	public Pagination getPage(Integer id, String roomName, Integer roomId, String urlAddr, String pullAddr,
			Integer isOpened, Timestamp createTime, String userId, String userName, int pageNo, int pageSize) {
		Finder finder = Finder.create("select bean from ILiveLiveRoom bean");
		finder.append(" where 1=1");
		if (null != id) {
			finder.append(" and bean.id = :id");
			finder.setParam("id", id);
		}
		if (!StringUtils.isBlank(roomName)) {
			finder.append(" and bean.roomName = :roomName");
			finder.setParam("roomName", "%" + roomName + "%");
		}
		if (null != roomId) {
			finder.append(" and bean.roomId = :roomId");
			finder.setParam("roomId", roomId);
		}
		if (!StringUtils.isBlank(urlAddr)) {
			finder.append(" and bean.urlAddr = :urlAddr");
			finder.setParam("urlAddr", "%" + urlAddr + "%");
		}
		if (!StringUtils.isBlank(pullAddr)) {
			finder.append(" and bean.pullAddr = :pullAddr");
			finder.setParam("pullAddr", "%" + pullAddr + "%");
		}
		if (null != isOpened) {
			finder.append(" and bean.isOpened = :isOpened");
			finder.setParam("isOpened", isOpened);
		}
		if (!StringUtils.isBlank(userId)) {
			finder.append(" and bean.userId = :userId");
			finder.setParam("userId", "%" + userId + "%");
		}
		if (!StringUtils.isBlank(userName)) {
			finder.append(" and bean.userName  like :userName");
			finder.setParam("userName", "%" + userName + "%");
		}
		finder.append(" order by bean.createTime desc");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public List<ILiveLiveRoom> findByNumber(String roomName) {
		Finder finder = Finder.create("select bean from ILiveLiveRoom bean");
		finder.append(" where 1=1");
		if (!StringUtils.isBlank(roomName)) {
			finder.append(" and bean.roomName like:roomName");
			finder.setParam("roomName", "%" + roomName + "%");
		}
		return find(finder);
	}

	public ILiveLiveRoom findById(Integer id) {
		ILiveLiveRoom entity = get(id);
		return entity;
	}

	public ILiveLiveRoom save(ILiveLiveRoom bean) {
		getSession().save(bean);
		return bean;
	}

	public void update(ILiveLiveRoom bean) {
		getSession().update(bean);
	}

	public ILiveLiveRoom deleteById(Integer id) {
		ILiveLiveRoom entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	public ILiveLiveRoom findByRoomId(Integer roomId) {
		Finder finder = Finder.create("select bean from ILiveLiveRoom bean");
		finder.append(" where 1=1");
		if (null != roomId) {
			finder.append(" and bean.roomId = :roomId");
			finder.setParam("roomId", roomId);
		}
		List find1 = find(finder);
		if (find1.isEmpty()) {
			return null;
		} else {
			return (ILiveLiveRoom) find1.get(0);
		}
	}

	@Override
	public List<ILiveLiveRoom> findRoomList() {
		Finder finder = Finder.create(
				"select bean from ILiveLiveRoom bean WHERE bean.openStatus=:openStatus  order by bean.createTime desc");
		finder.setParam("openStatus", 1);
		return find(finder);
	}

	@Override
	protected Class<ILiveLiveRoom> getEntityClass() {
		return ILiveLiveRoom.class;
	}

	@Override
	public Pagination getPager(String roomName, Integer liveStatus, Integer roomType,Integer pageNo, Integer pageSize, Long managerId) {
		Finder finder = Finder.create("select room from ILiveLiveRoom room where 1=1 ");
		if (liveStatus != null) {
			if (liveStatus == 12) {
				finder.append(" and room.openStatus =:openStatus");
				finder.setParam("openStatus", 0);
			} else {
				if (liveStatus != ILivePlayStatusConstant.NONE_STATE) {
					finder.append(" and room.liveEvent.liveStatus =:liveStatus");
					finder.setParam("liveStatus", liveStatus);
					finder.append(" and room.openStatus =:openStatus");
					finder.setParam("openStatus", 1);
				}

				if (liveStatus == ILivePlayStatusConstant.PLAY_ING) {
					finder.append(" and room.liveEvent.liveStatus in (1,2) ");
					finder.append(" and room.openStatus =:openStatus");
					finder.setParam("openStatus", 1);
				}
			}
		}
		if (roomType != null) {
			if (roomType == 1) {
				finder.append(" and room.roomType =:roomType");
				finder.setParam("roomType", 1);
			} else {
				    finder.append(" and room.roomType =:roomType");
					finder.setParam("roomType", 0);
				
			}
		}
		if (roomName != null) {
			// 如果是数字
			boolean roomNameInt = StringPatternUtil.isInteger(roomName);
			if (roomNameInt) {
				finder.append(" and (room.roomId=:roomId or room.liveEvent.liveTitle like :liveTitle)");
				finder.setParam("roomId", Integer.parseInt(roomName));
				finder.setParam("liveTitle", "%" + roomName + "%");
			} else {
				// 如果是文字
				finder.append(" and room.liveEvent.liveTitle like :liveTitle");
				finder.setParam("liveTitle", "%" + roomName + "%");
			}
		}
		finder.append(" and room.managerId =:managerId");
		finder.setParam("managerId", managerId);
		finder.append(" and room.deleteStatus =:deleteStatus");
		finder.setParam("deleteStatus", 0);
		finder.append(" order by room.createTime desc ");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public void updateILiveRoom(ILiveLiveRoom liveRoom) {
		this.update(liveRoom);
	}

	@Override
	public boolean saveRoom(ILiveLiveRoom liveRoom) {
		liveRoom.setIsSystemGift(0);
		liveRoom.setIsUserGift(0);
		this.getSession().save(liveRoom);
		return true;
	}

	@Override
	@Transactional(readOnly=true)
	public ILiveLiveRoom getILiveRoom(Integer liveRoomId) {
		return this.get(liveRoomId);
	}

	@Override
	public Pagination getPager(Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("select room from ILiveLiveRoom room where 1=1 ");
		finder.append(" order by room.createTime desc ");
		return find(finder, pageNo == null ? 1 : pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveLiveRoom> getPagerForView(String keyWord, Integer pageNo, Integer pageSize, Integer searchType) {
		List<ILiveLiveRoom> viewRoom = new ArrayList<>();
		Finder finder = Finder.create(
				"select room from ILiveLiveRoom room,ILiveEnterprise enterprise where room.enterpriseId=enterprise.enterpriseId and enterprise.certStatus=4 and enterprise.isDel=0 ");
		if (keyWord != null) {
			if (pageNo == 1) {
				boolean roomNameInt = StringPatternUtil.isInteger(keyWord);
				if (roomNameInt) {
					finder.append(" and room.roomId =:roomId");
					finder.setParam("roomId", Integer.parseInt(keyWord));
					finder.append(" and room.deleteStatus =:deleteStatus");
					finder.setParam("deleteStatus", 0);
					List<ILiveLiveRoom> find = this.find(finder);
					if (find != null && !find.isEmpty()) {
						viewRoom.addAll(find);
						finder = Finder.create(
								"select room from ILiveLiveRoom room ,ILiveEnterprise enterprise where room.enterpriseId=enterprise.enterpriseId and enterprise.certStatus=4 and enterprise.isDel=0 and room.liveEvent.liveTitle like :liveTitle and room.deleteStatus=0 ");
						finder.append(" order by room.createTime desc ");
						finder.setParam("liveTitle", "%" + keyWord + "%");
						Pagination find2 = find(finder, 1, pageSize - 1);
						if (find2 != null) {
							List<ILiveLiveRoom> list = (List<ILiveLiveRoom>) find2.getList();
							if (list != null && !list.isEmpty()) {
								viewRoom.addAll(list);
							}
						}
					} else {
						finder = Finder.create(
								"select room from ILiveLiveRoom room,ILiveEnterprise enterprise where room.enterpriseId=enterprise.enterpriseId and enterprise.certStatus=4 and enterprise.isDel=0 and room.liveEvent.liveTitle like :liveTitle and room.deleteStatus=0 ");
						finder.append(" order by room.createTime desc ");
						finder.setParam("liveTitle", "%" + keyWord + "%");
						Pagination find2 = find(finder, pageNo, pageSize);
						if (find2 != null) {
							List<ILiveLiveRoom> list = (List<ILiveLiveRoom>) find2.getList();
							if (list != null && !list.isEmpty()) {
								viewRoom.addAll(list);
							}
						}
					}
				} else {
					finder = Finder.create(
							"select room from ILiveLiveRoom room ,ILiveEnterprise enterprise where room.enterpriseId=enterprise.enterpriseId and enterprise.certStatus=4 and room.liveEvent.liveTitle like :liveTitle and room.deleteStatus=0 ");
					finder.append(" order by room.createTime desc ");
					finder.setParam("liveTitle", "%" + keyWord + "%");
					Pagination find2 = find(finder, pageNo, pageSize);
					if (find2 != null) {
						List<ILiveLiveRoom> list = (List<ILiveLiveRoom>) find2.getList();
						if (list != null && !list.isEmpty()) {
							viewRoom.addAll(list);
						}
					}
				}
			} else {
				finder = Finder.create(
						"select room from ILiveLiveRoom room ,ILiveEnterprise enterprise where room.enterpriseId=enterprise.enterpriseId and enterprise.certStatus=4 and room.liveEvent.liveTitle like :liveTitle and room.deleteStatus=0 ");
				finder.append(" order by room.createTime desc ");
				finder.setParam("liveTitle", "%" + keyWord + "%");
				Pagination find2 = find(finder, pageNo, pageSize);
				if (find2 != null) {
					List<ILiveLiveRoom> list = (List<ILiveLiveRoom>) find2.getList();
					if (list != null && !list.isEmpty()) {
						viewRoom.addAll(list);
					}
				}
			}
		}
		return viewRoom;
	}

	@Override
	public Pagination getPagerForOperator(Long userId, Integer pageNo, Integer pageSize,Integer liveStatus,Integer roomType) {
		Finder finder = Finder.create("select room from ILiveLiveRoom room where 1=1 ");
		if (userId != null) {
			finder.append(" and (room.managerId =:managerId or room.roomId in (select liveRoomId from IliveSubRoom where subAccountId=:managerId))");
			finder.setParam("managerId", userId);
		}
		if (liveStatus != null) {
			finder.append(" and room.liveEvent.liveStatus=:liveStatus");
			finder.setParam("liveStatus", liveStatus);
		}
		if (roomType == null||roomType==0) {
			finder.append(" and room.roomType!=:roomType");
			finder.setParam("roomType", 1);
		}else if(roomType==1){
			finder.append(" and room.roomType=:roomType");
			finder.setParam("roomType", roomType);
		}
		
		finder.append(" and room.deleteStatus =:deleteStatus");
		finder.setParam("deleteStatus", 0);
		finder.append(" and room.openStatus =:openStatus");
		finder.setParam("openStatus", 1);
		finder.append(" order by room.createTime desc ");
		return find(finder, pageNo, pageSize);
	}
	@Override
	public Pagination getPagerForOperator(Long userId, Integer pageNo, Integer pageSize,Integer liveStatus,String key) {
		Finder finder = Finder.create("select room from ILiveLiveRoom room where 1=1 ");
		if (userId != null) {
			finder.append(" and (room.managerId =:managerId or room.roomId in (select liveRoomId from IliveSubRoom where subAccountId=:managerId))");
			finder.setParam("managerId", userId);
		}
		if (liveStatus != null) {
			finder.append(" and room.liveEvent.liveStatus=:liveStatus");
			finder.setParam("liveStatus", liveStatus);
		}
		if(key!=null){
			finder.append(" and room.liveEvent.liveTitle like :liveTitle");
			finder.setParam("liveTitle", "%"+key+"%");
		}
		finder.append(" and room.deleteStatus =:deleteStatus");
		finder.setParam("deleteStatus", 0);
		finder.append(" and room.openStatus =:openStatus");
		finder.setParam("openStatus", 1);
		finder.append(" order by room.createTime desc ");
		return find(finder, pageNo, pageSize);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveLiveRoom> findRoomListByEnterprise(Integer enterpriseId) {
		Finder finder = Finder.create(
				"select bean from ILiveLiveRoom bean WHERE bean.openStatus=:openStatus and bean.enterpriseId = :enterpriseId  order by bean.createTime desc");
		finder.setParam("openStatus", 1);
		finder.setParam("enterpriseId", enterpriseId);
		return find(finder);
	}

	@Override
	public List<ILiveLiveRoom> findRoomListPassByEnterprise(Integer enterpriseId) {
		Finder finder = Finder.create(
				"select bean from ILiveLiveRoom bean WHERE bean.openStatus=:openStatus and bean.enterpriseId = :enterpriseId and bean.liveEvent.liveStatus in(0,1,2,3) and bean.deleteStatus = :deleteStatus  order by bean.createTime desc");
		finder.setParam("openStatus", 1);
		finder.setParam("deleteStatus", 0);
		finder.setParam("enterpriseId", enterpriseId);
		return find(finder);
	}

	@Override
	public Pagination getTop1LiveByEnterpriseId(Integer enterpriseId) {
		Finder create = Finder.create(
				" select room from ILiveLiveRoom room ,ILiveEnterprise enterprise where enterprise.enterpriseId=room.enterpriseId and enterprise.certStatus=4 and enterprise.isDel=0 and  room.enterpriseId=:enterpriseId and room.openStatus=1 and room.deleteStatus=0 and room.liveEvent.liveStatus=1 order by room.liveEvent.liveStartTime desc");
		create.setParam("enterpriseId", enterpriseId);
		Pagination find = this.find(create, 1, 1);
		// 直播中的
		if (find.getList() != null && !find.getList().isEmpty()) {
			return find;
		}

		// 暂停中的
		create = Finder.create(
				"select room from ILiveLiveRoom room,ILiveEnterprise enterprise where enterprise.enterpriseId=room.enterpriseId and enterprise.isDel=0 and enterprise.certStatus=4 and room.enterpriseId=:enterpriseId and room.openStatus=1 and room.deleteStatus=0 and room.liveEvent.liveStatus=2 order by room.liveEvent.liveStartTime desc");
		create.setParam("enterpriseId", enterpriseId);
		find = this.find(create, 1, 1);
		// 直播中的
		if (find.getList() != null && !find.getList().isEmpty()) {
			return find;
		}

		create = Finder.create(
				"select room from ILiveLiveRoom room,ILiveEnterprise enterprise where enterprise.enterpriseId=room.enterpriseId and enterprise.isDel=0 and enterprise.certStatus=4 and room.enterpriseId=:enterpriseId and room.openStatus=1 and room.deleteStatus=0 and room.liveEvent.liveStatus=0 order by room.liveEvent.liveStartTime desc ");
		create.setParam("enterpriseId", enterpriseId);
		// 预告的
		find = this.find(create, 1, 1);
		if (find.getList() != null && !find.getList().isEmpty()) {
			return find;
		}

		create = Finder.create(
				"select room from ILiveLiveRoom room,ILiveEnterprise enterprise where enterprise.enterpriseId=room.enterpriseId and enterprise.isDel=0 and enterprise.certStatus=4 and room.enterpriseId=:enterpriseId and room.openStatus=1 and room.deleteStatus=0 and room.liveEvent.liveStatus=3 order by room.liveEvent.liveStartTime desc");
		create.setParam("enterpriseId", enterpriseId);
		find = this.find(create, 1, 1);
		// 结束s的
		if (find.getList() != null && !find.getList().isEmpty()) {
			return find;
		}

		return null;
	}

	@Override
	public ILiveLiveRoom findByEventId(Long liveEventId) {
		String hql = "select bean from ILiveLiveRoom bean WHERE bean.liveEvent.liveEventId = :liveEventId and bean.liveEvent.liveStatus in (0,1,2)";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("liveEventId", liveEventId);
		return (ILiveLiveRoom) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveLiveRoom> getTop4ForView(String keyWord) {
		List<ILiveLiveRoom> viewRoom = new ArrayList<>();
		Finder finder = Finder.create(
				"select room from ILiveLiveRoom room,ILiveEnterprise enterprise where room.enterpriseId=enterprise.enterpriseId and enterprise.certStatus=4 and enterprise.isDel=0 ");
		if (keyWord != null) {
			int pageNo = 1;
			int pageSize = 4;
			if (pageNo == 1) {
				boolean roomNameInt = StringPatternUtil.isInteger(keyWord);
				if (roomNameInt) {
					finder.append(" and room.roomId =:roomId");
					finder.setParam("roomId", Integer.parseInt(keyWord));
					finder.append(" and room.openStatus=1 and room.deleteStatus =:deleteStatus");
					finder.setParam("deleteStatus", 0);
					List<ILiveLiveRoom> find = this.find(finder);
					if (find != null && !find.isEmpty()) {
						viewRoom.addAll(find);
						finder = Finder.create(
								"select room from ILiveLiveRoom room ,ILiveEnterprise enterprise where room.enterpriseId=enterprise.enterpriseId and enterprise.certStatus=4 and enterprise.isDel=0 and room.liveEvent.liveTitle like :liveTitle and room.deleteStatus=0 ");
						finder.append(" order by room.createTime desc ");
						finder.setParam("liveTitle", "%" + keyWord + "%");
						Pagination find2 = find(finder, 1, pageSize - find.size());
						if (find2 != null) {
							List<ILiveLiveRoom> list = (List<ILiveLiveRoom>) find2.getList();
							if (list != null && !list.isEmpty()) {
								viewRoom.addAll(list);
							}
						}
					} else {
						finder = Finder.create(
								"select room from ILiveLiveRoom room,ILiveEnterprise enterprise where room.enterpriseId=enterprise.enterpriseId and enterprise.certStatus=4 and enterprise.isDel=0 and room.liveEvent.liveTitle like :liveTitle and room.deleteStatus=0 and room.openStatus=1 and room.roomType!=1 ");
						finder.append(" order by room.createTime desc ");
						finder.setParam("liveTitle", "%" + keyWord + "%");
						Pagination find2 = find(finder, pageNo, pageSize);
						if (find2 != null) {
							List<ILiveLiveRoom> list = (List<ILiveLiveRoom>) find2.getList();
							if (list != null && !list.isEmpty()) {
								viewRoom.addAll(list);
							}
						}
					}
				} else {
					finder = Finder.create(
							" select room from ILiveLiveRoom room,ILiveEnterprise enterprise where room.enterpriseId=enterprise.enterpriseId and enterprise.certStatus=4 and enterprise.isDel=0 and room.liveEvent.liveTitle like :liveTitle and room.deleteStatus=0 and room.openStatus=1 and room.roomType!=1 ");
					finder.append(" order by room.createTime desc ");
					finder.setParam("liveTitle", "%" + keyWord + "%");
					Pagination find2 = find(finder, pageNo, pageSize);
					if (find2 != null) {
						List<ILiveLiveRoom> list = (List<ILiveLiveRoom>) find2.getList();
						if (list != null && !list.isEmpty()) {
							viewRoom.addAll(list);
						}
					}
				}
			}
		}
		return viewRoom;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveLiveRoom> findRoomListPassByEnterpriseAndSize(Integer enterpriseId, Integer num) {
		Finder finder = Finder.create(
				"select bean from ILiveLiveRoom bean WHERE bean.openStatus=:openStatus and bean.deleteStatus=0 and bean.enterpriseId =:enterpriseId  order by bean.liveEvent.liveStatus asc");
		finder.setParam("openStatus", 1);
		finder.setParam("enterpriseId", enterpriseId);
		finder.setFirstResult(0);
		finder.setMaxResults(num);
		return find(finder);
	}

	@Override
	public List<ILiveLiveRoom> getIliveRoomByUserId(Long userId) {
		Finder finder = Finder.create(
				"from ILiveLiveRoom where deleteStatus=0 and openStatus=1 and managerId=:managerId order by createTime desc");
		finder.setParam("managerId", userId);
		List find = this.find(finder);
		return find;
	}

	@Override
	public Pagination getCertStatusRoom(Long userId, Integer pageNo, Integer pageSize) {
		String hql = "from ILiveLiveRoom room where room.deleteStatus=0 and room.openStatus=1 and room.managerId=:managerId order by room.createTime desc";
		Finder finder = Finder.create(hql);
		finder.setParam("managerId", userId);
		Pagination find = this.find(finder, pageNo, pageSize);
		return find;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveLiveRoom> getAllLivingRoom() {
		Finder finder = Finder.create(
				"from ILiveLiveRoom where deleteStatus=0 and openStatus=1 and liveEvent.liveStatus=:liveStatus order by createTime desc");
		finder.setParam("liveStatus", ILivePlayStatusConstant.PLAY_ING);
		return this.find(finder);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveLiveRoom> findByEnterpriseIdAndName(String name, Integer enterpriseId) {
		Finder finder = Finder.create(
				"select bean from ILiveLiveRoom bean WHERE bean.openStatus=:openStatus and bean.enterpriseId = :enterpriseId and bean.liveEvent.liveStatus in(0,1,2,3) and bean.deleteStatus = :deleteStatus and bean.liveEvent.liveTitle like :name  order by bean.createTime desc");
		finder.setParam("openStatus", 1);
		finder.setParam("deleteStatus", 0);
		finder.setParam("enterpriseId", enterpriseId);
		finder.setParam("name", "%"+name+"%");
		return find(finder);
	}

	@Override
	public Pagination getPagerEnterpriseId(String roomName, Integer liveStatus, Integer roomType,Integer pageNo, Integer pageSize, Integer enterpriseId,Long userId) {
		Finder finder = Finder.create("select room from ILiveLiveRoom room where 1=1 ");
		if(userId==null){
			//主管理人员查询
			if (liveStatus != null) {
				if (liveStatus == 12) {
					finder.append(" and room.openStatus =:openStatus");
					finder.setParam("openStatus", 0);
				} else {
					if (liveStatus != ILivePlayStatusConstant.NONE_STATE) {
						finder.append(" and room.liveEvent.liveStatus =:liveStatus");
						finder.setParam("liveStatus", liveStatus);
						finder.append(" and room.openStatus =:openStatus");
						finder.setParam("openStatus", 1);
					}

					if (liveStatus == ILivePlayStatusConstant.PLAY_ING) {
						finder.append(" and room.liveEvent.liveStatus in (1,2) ");
						finder.append(" and room.openStatus =:openStatus");
						finder.setParam("openStatus", 1);
					}
				}
			}
			if (roomType != null) {
				if (roomType == 1) {
					finder.append(" and room.roomType =:roomType");
					finder.setParam("roomType", 1);
				} else {
					    finder.append(" and room.roomType =:roomType");
						finder.setParam("roomType", 0);
					
				}
			}
			if (roomName != null) {
				// 如果是数字
				boolean roomNameInt = StringPatternUtil.isInteger(roomName);
				if (roomNameInt) {
					finder.append(" and (room.roomId=:roomId or room.liveEvent.liveTitle like :liveTitle)");
					finder.setParam("roomId", Integer.parseInt(roomName));
					finder.setParam("liveTitle", "%" + roomName + "%");
				} else {
					// 如果是文字
					finder.append(" and room.liveEvent.liveTitle like :liveTitle");
					finder.setParam("liveTitle", "%" + roomName + "%");
				}
			}
			finder.append(" and room.enterpriseId =:enterpriseId");
			finder.setParam("enterpriseId", enterpriseId);
			finder.append(" and room.deleteStatus =:deleteStatus");
			finder.setParam("deleteStatus", 0);
			finder.append(" order by room.createTime desc ");
		}else{
			//	子账户查询结果
			if (liveStatus != null) {
				if (liveStatus == 12) {
					finder.append(" and room.openStatus =:openStatus");
					finder.setParam("openStatus", 0);
				} else {
					if (liveStatus != ILivePlayStatusConstant.NONE_STATE) {
						finder.append(" and room.liveEvent.liveStatus =:liveStatus");
						finder.setParam("liveStatus", liveStatus);
						finder.append(" and room.openStatus =:openStatus");
						finder.setParam("openStatus", 1);
					}

					if (liveStatus == ILivePlayStatusConstant.PLAY_ING) {
						finder.append(" and room.liveEvent.liveStatus in (1,2) ");
						finder.append(" and room.openStatus =:openStatus");
						finder.setParam("openStatus", 1);
					}
				}
			}
			if (roomName != null) {
				// 如果是数字
				boolean roomNameInt = StringPatternUtil.isInteger(roomName);
				if (roomNameInt) {
					finder.append(" and (room.roomId=:roomId or room.liveEvent.liveTitle like :liveTitle)");
					finder.setParam("roomId", Integer.parseInt(roomName));
					finder.setParam("liveTitle", "%" + roomName + "%");
				} else {
					// 如果是文字
					finder.append(" and room.liveEvent.liveTitle like :liveTitle");
					finder.setParam("liveTitle", "%" + roomName + "%");
				}
			}
			finder.append(" and room.enterpriseId =:enterpriseId");
			finder.setParam("enterpriseId", enterpriseId);
			finder.append(" and (room.managerId=:subAccountId or room.roomId in (select liveRoomId from IliveSubRoom where subAccountId=:subAccountId))");
			finder.setParam("subAccountId", userId);
			finder.append(" and room.deleteStatus =:deleteStatus");
			finder.setParam("deleteStatus", 0);
			finder.append(" order by room.createTime desc ");
		}
		return find(finder, pageNo, pageSize);
	}

	@Override
	public List<ILiveLiveRoom> getliveroomlevel(Integer roomId) {
		Finder finder = Finder.create(
				"select bean from ILiveLiveRoom bean WHERE bean.roomId=:roomId");
		finder.setParam("roomId", roomId);
		return find(finder);
	}

	@Override
	public Pagination getPager(Integer enterpriseId,Integer liveStatus, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("select room from ILiveLiveRoom room where deleteStatus = 0 and liveEvent.liveStatus = :liveStatus");
		finder.append(" and enterpriseId=:enterpriseId");
		finder.setParam("enterpriseId", enterpriseId);
		finder.append(" order by room.createTime desc ");
		finder.setParam("liveStatus", liveStatus);
		return find(finder, pageNo == null ? 1 : pageNo, pageSize);
	}

	@Override
	public Pagination getNoMeetPager(String roomName,Integer managerId, Integer type, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("select room from ILiveLiveRoom room where 1=1 ");
		if (type != null) {
				finder.append(" and room.roomType =:type");
				finder.setParam("type", 0);
			
		}
		if (roomName != null&&!"".equals(roomName)) {
			// 如果是数字
			boolean roomNameInt = StringPatternUtil.isInteger(roomName);
			if (roomNameInt) {
				finder.append(" and (room.roomId=:roomId or room.liveEvent.liveTitle like :liveTitle)");
				finder.setParam("roomId", Integer.parseInt(roomName));
				finder.setParam("liveTitle", "%" + roomName + "%");
			} else {
				// 如果是文字
				finder.append(" and room.liveEvent.liveTitle like :liveTitle");
				finder.setParam("liveTitle", "%" + roomName + "%");
			}
		}
		finder.append(" and room.enterpriseId =:managerId");
		finder.setParam("managerId", managerId);
		finder.append(" and room.deleteStatus =:deleteStatus");
		finder.setParam("deleteStatus", 0);
		finder.append(" order by room.createTime desc ");
		return find(finder, pageNo, pageSize);
	}

}