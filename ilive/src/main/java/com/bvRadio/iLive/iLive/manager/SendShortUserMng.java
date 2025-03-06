package com.bvRadio.iLive.iLive.manager;

import java.sql.Timestamp;
import java.util.HashMap;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.SendShortUser;

public interface SendShortUserMng {

	Pagination getPage(Integer roomId,Long id,String sendUser,Integer pageNo,Integer pageSize);
	
	SendShortUser getById(Long id);
	
	Long save(SendShortUser sendShortUser);
	
	Long operatorSend(SendShortUser sendShortUser,HashMap<String, String> userMessage,String url);

	Long operatorSend1(SendShortUser sendShortUser, HashMap<String, String> userMessage, String url);

	Pagination getPage(Integer roomId,Long id,String sendUser,Integer pageNo,Integer pageSize, Timestamp startTime, Timestamp endTime);
}
