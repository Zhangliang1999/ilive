package com.jwzt.common.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;

public class JsonUtils {

	public static <T> T jsonToObject(String jsonStr, Class<T> valueType)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = getObjectMapper();
		T t = mapper.readValue(jsonStr, valueType);
		return t;
	}

	public static Map<?, ?> jsonToMap(String jsonStr) throws JsonParseException, JsonMappingException, IOException {
		Map<?, ?> map = jsonToObject(jsonStr, Map.class);
		return map;
	}

	public static String objToJson(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = getObjectMapper();
		String json = mapper.writeValueAsString(obj);
		return json;
	}

	private static ObjectMapper getObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		mapper.disable(SerializationFeature.WRAP_EXCEPTIONS);
		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
		SerializerProvider serializerProvider = mapper.getSerializerProvider();
		serializerProvider.setNullValueSerializer(new JsonSerializer<Object>() {
			@Override
			public void serialize(Object value, JsonGenerator jg, SerializerProvider sp)
					throws IOException, JsonProcessingException {
				jg.writeString("");
			}
		});
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mapper.setDateFormat(sdf);
		return mapper;
	}
}
