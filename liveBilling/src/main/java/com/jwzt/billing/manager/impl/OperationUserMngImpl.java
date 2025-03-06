package com.jwzt.billing.manager.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jwzt.billing.entity.bo.OperationUser;
import com.jwzt.billing.manager.OperationUserMng;
import com.jwzt.billing.utils.ConfigUtils;
import com.jwzt.common.utils.HttpUtils;
import com.jwzt.common.utils.JsonUtils;

/**
 * @author ysf
 */
@Service
public class OperationUserMngImpl implements OperationUserMng {

	@SuppressWarnings("unchecked")
	@Override
	public List<OperationUser> listByParams() throws IOException {
		String getLogUrl = ConfigUtils.get("get_operation_user_list_url");
		Map<String, String> params = new HashMap<String, String>();
		String httpReturnString = HttpUtils.sendPost(getLogUrl, params, "utf-8");
		Map<?, ?> jsonToMap = JsonUtils.jsonToMap(httpReturnString);
		List<OperationUser> list = null;
		Integer code = (Integer) jsonToMap.get("code");
		if (null != code && code.equals(1)) {
			list = (List<OperationUser>) jsonToMap.get("data");
		}
		return list;
	}

}
