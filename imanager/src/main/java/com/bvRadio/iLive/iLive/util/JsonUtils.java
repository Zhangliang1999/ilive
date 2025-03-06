package com.bvRadio.iLive.iLive.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class JsonUtils {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map jsonToMap(JSONObject jsonObject) throws JSONException {
		Map result = new HashMap();
		Iterator iterator = jsonObject.keys();
		String key = null;
		Object value = null;
		while (iterator.hasNext()) {
			key = (String) iterator.next();
			value = jsonObject.get(key);
			result.put(key, value);
		}
		return result;
	}

}
