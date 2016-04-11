package xmextension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * ��Object����м�ֵ�Ը�ֵ
 * 
 * @author Ѧ����
 *
 */
public class Object_XMKVC {

	/** Ҫ���õĶ��� */
	private Object obj;

	private Object_XMKVC() {
		// TODO Auto-generated constructor stub
	}

	private static class XMKVCFactory {
		private static Object_XMKVC instance = new Object_XMKVC();
	}

	/**
	 * ���ö���
	 * 
	 * @param obj
	 *            ����
	 * @return Object_XMKVC ʵ��
	 */
	public static Object_XMKVC setObject(Object obj) {
		Object_XMKVC instance = XMKVCFactory.instance;
		instance.obj = obj;
		return instance;
	}

	/**
	 * Ϊ��������ֵ
	 * 
	 * @param value
	 *            ֵ
	 * @param key
	 *            ��
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean setValue(Object value, String key) {
		if (obj instanceof Map) {
			((Map<String, Object>) obj).put(key, value);
			return true;
		}
		if (obj instanceof List) {
			try {
				List<Object>list = (List<Object>) obj;
				int index = Integer.parseInt(key);
				if (index < list.size() && index >= 0) {
					list.set(Integer.parseInt(key), value);
					return true;
				}else {
					return false;
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				return false;
			}
		}
		
		Field field = getField(key, obj.getClass());
		if (field != null) {
			try {
				field.set(obj, value);
				return true;
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				return false;
			}
		}
		return false;
	}

	/**
	 * ����ֵʹ�ü�·��
	 * 
	 * @param value
	 *            ֵ
	 * @param keyPath
	 *            ��·��
	 * @return boolean
	 */
	public boolean setValueWithKeyPath(Object value, String keyPath) {
		if (obj instanceof Map || obj instanceof List) {
			return false;
		}
		int start = keyPath.indexOf(".");
		if (start != -1) {
			Object fieldObj = obj;
			Object fieldValue = value;
			Field field = null;
			String[] keys = keyPath.split("\\.");
			int length = keys.length;
			for (int i = 0; i < length; i++) {
				field = getField(keys[i], fieldObj.getClass());
				if (field == null) {
					return false;
				}
				try {
					if (i != length - 1) {
						fieldValue = field.get(fieldObj);
						if (fieldValue == null) {
							fieldValue = field.getType().newInstance();
							field.set(fieldObj, fieldValue);
						}
						fieldObj = fieldValue;
					} else {
						field.set(fieldObj, value);
						return true;
					}
				} catch (InstantiationException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * ��ȡField��Ա����
	 * 
	 * @param key
	 *            ��
	 * @param c
	 *            ���ʶ
	 * @return
	 */
	private Field getField(String key, Class<?> c) {
		Field field = null;
		while (c != null) {
			try {
				field = c.getField(key);
				c = null;
			} catch (NoSuchFieldException | SecurityException e) {
				// TODO Auto-generated catch block
				c = c.getSuperclass();
			}
		}
		return field;
	}

	/**
	 * ��ȡֵ
	 * 
	 * @param key
	 *            ��
	 * @return ֵ
	 */
	@SuppressWarnings("unchecked")
	public Object getValue(String key) {
		if (obj instanceof Map) {
			return ((Map<String, Object>)obj).get(key);
		}
		if (obj instanceof List) {
			try {
				List<Object>list = (List<Object>) obj;
				int index = Integer.parseInt(key);
				if (index < list.size() && index >= 0) {
					return list.get(Integer.parseInt(key));
				}else {
					return null;
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				return null;
			}
		}
		Field field = getField(key, obj.getClass());
		if (field != null) {
			try {
				Object value = field.get(obj);
				return value;
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				return null;
			}
		}
		return null;
	}

	/**
	 * ��ȡֵͨ����·��
	 * 
	 * @param keyPath
	 *            ��·��
	 * @return ֵ
	 */
	public Object getValueWithKeyPath(String keyPath) {
		if (obj instanceof Map || obj instanceof List) {
			return null;
		}
		int start = keyPath.indexOf(".");
		if (start != -1) {
			String[] keys = keyPath.split("\\.");
			Field field = null;
			Object fieldValue = obj;
			for (String key : keys) {
				field = getField(key, fieldValue.getClass());
				if (field == null) {
					return null;
				}
				try {
					fieldValue = field.get(fieldValue);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					return null;
				}
			}
			return fieldValue;
		}
		return null;
	}

}
