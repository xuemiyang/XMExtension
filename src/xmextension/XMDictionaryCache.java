package xmextension;

import java.util.HashMap;
import java.util.Map;

/**
 * �����ֵ�
 * @author Ѧ����
 *
 */
public class XMDictionaryCache {

	private static Map<String, Object> map = new HashMap<>();
	private static Map<Object, Object>cacheMap = new HashMap<>();

	private XMDictionaryCache() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * ��ȡ�������
	 * @param key ��
	 * @return �������
	 */
	public static Object getObject(Object key) {
		return cacheMap.get(key);
	}
	
	/**
	 * ���û������
	 * @param key ��
	 * @param value ֵ
	 */
	public static void setObject(Object key,Object value) {
		cacheMap.put(key, value);
	}
	
	/**
	 * ������󵽻����ֵ�
	 * @param key ��
	 * @param objc ����
	 * @return Map<Class<?>, Object> �ֵ�
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
	 * ��ȡ�ֵ��еĶ���
	 * @param key ��
	 * @return Object ����
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
	 * ��ȡ�ֵ�
	 * @return Map<String, Object> �ֵ�
	 */
	@SuppressWarnings("unchecked")
	public static Map<Class<?>, Object> getDict(String dictId) {
		return (Map<Class<?>, Object>) map.get(dictId);
	}
}
