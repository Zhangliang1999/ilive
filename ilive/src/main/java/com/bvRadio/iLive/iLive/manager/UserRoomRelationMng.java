package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.UserRoomRelation;
/**
 * 监控管理
 * @author xuehaipeng
 *
 */
public interface UserRoomRelationMng {
	
	public UserRoomRelation findById(Integer id);

	public UserRoomRelation save(UserRoomRelation bean);
	
	public UserRoomRelation update(UserRoomRelation bean);

	public UserRoomRelation deleteById(Integer id);
	
	public UserRoomRelation[] deleteByIds(Integer[] ids);
	
	public List<UserRoomRelation> findByIds(String[] ids);

	public List<UserRoomRelation> findByUid(Integer uid);

	public List<UserRoomRelation> getUserRoomRelationAll();

}
