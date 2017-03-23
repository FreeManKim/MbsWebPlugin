package com.mobisoft.library.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import android.util.Log;

public class JsonUtil {

	/**
	 * json2list: json字符串转换为list.
	 */
	public static <T> List<T> json2list(String json, Class<T> clazz) {

		if (json == null || StringUtil.isEmpty(json)) {
			return null;
		}
		List<T> list = JSON.parseArray(json, clazz);
		return list;
	}

	/**
	 * json2map: json字符串转换为map.
	 */
	public static <K, V> Map<K, V> json2map(String json) {

		if (json == null || StringUtil.isEmpty(json)) {
			return null;
		}
		@SuppressWarnings("unchecked")
		Map<K, V> map = (Map<K, V>) JSON.parse(json);
		return map;

	}

	/**
	 * map 转json
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String map2json(Map map) {
		if (map == null) {
			map = new HashMap();
		}
		return JSON.toJSONString(map);
	}


	/**
	 * json2entity: json字符串转换为entity.
	 */
	public static <T> T json2entity(String json, Class<T> clazz) {

		if (json == null || StringUtil.isEmpty(json)) {
			return null;
		}
		T entity = JSON.parseObject(json, clazz);
		return entity;

	}

	/**
	 * obj2entity: obj字符串转换为entity.
	 */
	public static <T> T obj2entity(Object obj, Class<T> clazz) {
		if (obj == null) {
			return null;
		}
		return json2entity(obj.toString(), clazz);
	}

	/**
	 * obj2list: json字符串转换为list.
	 */
	public static <T> List<T> obj2list(Object obj, Class<T> clazz) {

		if (obj == null) {
			return null;
		}
		return json2list(obj.toString(), clazz);
	}

	public static String obj2Str(Object obj) {
		if (obj == null) {
			obj = new Object();
		}
		SerializeConfig ser = new SerializeConfig();
		ser.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
		System.out.println(JSON.toJSONString(obj, ser,SerializerFeature.WriteNullStringAsEmpty));
		return JSON.toJSONString(obj, ser);
	}
}
