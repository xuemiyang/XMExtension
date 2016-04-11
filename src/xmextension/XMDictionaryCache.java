package xmextension;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存字典
 * @author 薛米样
 *
 */
public class XMDictionaryCache {

	private static Map<String, Object> map = new HashMap<>();
	private static Map<Object, Object>cacheMap = new HashMap<>();

	private XMDictionaryCache() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * 获取缓存对象
	 * @param key 键
	 * @return 缓存对象
	 */
	public static Object getObject(Object key) {
		return cacheMap.get(key);
	}
	
	/**
	 * 设置缓存对象
	 * @param key 键
	 * @param value 值
	 */
	public static void setObject(Object key,Object value) {
		cacheMap.put(key, value);
	}
	
	/**
	 * 缓存对象到缓存字典
	 * @param key 键
	 * @param objc 对象
	 * @return Map<Class<?>, Object> 字典
	 */
	public static Map<Class<?>, Object> setObject(Class<?> key, Object objc,String dictId) {
		@SuppressWarnings("unchecked")
		Map<Class<?>, Object>dict = (Map<Class<?>, Object>) map.get(dictId);
		if (dict != null) {
			dict.put(key, objc);
		}else {
			dict = new HashMap<>();
			dict.put(key, objc);
			map.put(dictId, dict);
		}
		return dict;
	}
	
	/**
	 * 获取字典中的对象
	 * @param key 键
	 * @return Object 对象
	 */
	public static Object getObject(Class<?> key,String dictId) {
		@SuppressWarnings("unchecked")
		Map<Class<?>, Object>dict = (Map<Class<?>, Object>) map.get(dictId);
		if (dict != null) {
			return dict.get(key);
		}
		return null;
	}
	
	/**
	 * 获取字典
	 * @return Map<String, Object> 字典
	 */
	@SuppressWarnings("unchecked")
	public static Map<Class<?>, Object> getDict(String dictId) {
		return (Map<Class<?>, Object>) map.get(dictId);
	}
}
