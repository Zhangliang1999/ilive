package com.jwzt.billing.dao;

import java.util.List;

import com.jwzt.billing.entity.FunctionInfo;

/**
 * @author ysf
 */
public interface FunctionInfoDao {

	List<FunctionInfo> listByParams(Integer parentId);

	FunctionInfo getBeanById(Integer id);

}
