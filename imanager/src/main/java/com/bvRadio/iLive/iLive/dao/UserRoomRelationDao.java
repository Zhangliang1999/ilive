package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.UserRoomRelation;

public interface UserRoomRelationDao {
	
	public UserRoomRelation findById(Integer id);

	public UserRoomRelation save(UserRoomRelation bean);
	
	public UserRoomRelation update(UserRoomRelation bean);

	public UserRoomRelation deleteById(Integer id);
	
	public List<UserRoomRelation>  findByUid(Integer uid);

	public List<UserRoomRelation> findBeanAll();
}
