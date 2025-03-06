package com.jwzt.billing.manager;

import java.util.List;

import com.jwzt.billing.entity.FunctionInfo;

/**
 * @author ysf
 */
public interface FunctionInfoMng {
	List<FunctionInfo> listByParams(Integer parentId, boolean initBean);

	FunctionInfo getBeanById(Integer id, boolean initBean);
}
