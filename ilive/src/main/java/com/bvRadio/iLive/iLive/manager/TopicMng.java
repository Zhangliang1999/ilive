package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.Topic;

public interface TopicMng {

	Pagination getPage(Long userId,String name,Integer pageNo,Integer pageSize);
	
	Long save(Topic topic);
	
	void update(Topic topic);
	
	List<Topic> getByEnterpriseId(Integer enterpriseId);
	
	Topic getById(Long id);
	
	void delete(Long id);
	
	void sessionToDB(Topic topic);
	
	void sortColumnType(Topic topic,String str,Long typeId);
	
	void sortManyImg(Topic topic,String str,Long typeId,Integer num);
	
}
