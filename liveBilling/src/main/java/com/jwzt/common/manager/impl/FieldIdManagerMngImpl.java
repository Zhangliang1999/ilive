package com.jwzt.common.manager.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.common.cache.Cache;
import com.jwzt.common.cache.CacheManager;
import com.jwzt.common.dao.FieldIdManagerDao;
import com.jwzt.common.entity.bo.CacheFieldIdManager;
import com.jwzt.common.manager.FieldIdManagerMng;

@Service
@Transactional
public class FieldIdManagerMngImpl implements FieldIdManagerMng {

	private static final Logger log = LogManager.getLogger();

	@Override
	public synchronized Long getNextId(String tableName, String fieldName, Long step) {
		log.debug("getNextId, tableName:{}, fieldName:{}, step:{}", tableName, fieldName, step);
		String cacheKey = tableName + "|" + fieldName;
		Cache beanInCache = CacheManager.getCacheInfo(cacheKey);
		if (null == beanInCache) {
			log.debug("缓存中没有，从数据库中查询，然后再取100个放入缓存，cacheKey:{}", cacheKey);
			Long cacheMaxId = fieldIdManagerDao.getNextId(tableName, fieldName, step + 100);
			Long nextId = cacheMaxId - 100;
			Long cacheNextId = cacheMaxId - 99;
			log.debug("cacheKey:{}, cacheNextId:{}, cacheMaxId:{}", cacheKey, cacheNextId, cacheMaxId);
			CacheFieldIdManager cacheFieldIdManager = new CacheFieldIdManager(tableName, fieldName, cacheNextId,
					cacheMaxId);
			CacheManager.putCacheInfo(cacheKey, cacheFieldIdManager, 24 * 60 * 60 * 1000);
			return nextId;
		} else {
			CacheFieldIdManager cacheFieldIdManager = (CacheFieldIdManager) beanInCache.getValue();
			Long cacheMaxId = cacheFieldIdManager.getMaxId();
			Long cacheNextId = cacheFieldIdManager.getNextId();
			// 剩余未使用的字段ID数量
			Long unusedIdNum = cacheMaxId - cacheNextId + 1;
			if (unusedIdNum >= step) {
				log.debug("缓冲中有，且剩余未使用的字段ID数量大于等于需要生成的ID数量，直接中缓存获取并更新缓存。");
				Long nextId = cacheNextId + step - 1;
				Long newCacheNextId = cacheNextId + step;
				cacheFieldIdManager.setNextId(newCacheNextId);
				return nextId;
			} else {
				log.debug("缓冲中有，但剩余未使用的字段ID数量小于需要生成的ID数量，从数据库中查询。");
				Long needStep = step - unusedIdNum;
				Long newCacheMaxId = fieldIdManagerDao.getNextId(tableName, fieldName, needStep + 100);
				Long nextId = newCacheMaxId - 100;
				Long newCacheNextId = newCacheMaxId - 99;
				log.debug("cacheKey:{}, cacheNextId:{}, cacheMaxId:{}", cacheKey, newCacheNextId, newCacheMaxId);
				cacheFieldIdManager.setNextId(newCacheNextId);
				cacheFieldIdManager.setMaxId(newCacheMaxId);
				return nextId;
			}
		}
	}

	@Autowired
	private FieldIdManagerDao fieldIdManagerDao;

}
