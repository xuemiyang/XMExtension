package xmextension;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xmextension.Object_XMProperty.XMPropertyEnumerate;
import xmextension.xmjson.xmclass.XMJson;

/**
 * ����ģ���ֵ�ת������
 * 
 * @author Ѧ����
 *
 */
public class Object_XMMap {

	public static interface XMObjectDidFinishConvertingToMap {
		public void objectDidFinishConvertingToMap();
	}

	public static interface XMMapDidFinishConvertingToObject {
		public void mapDidFinishConvertingToObject();
	}

	private Class<?> c;
	private Object obj;
	private boolean ReferenceReplacedKey;

	private Object_XMMap() {
		// TODO Auto-generated constructor stub
		ReferenceReplacedKey = true;
	}

	public static Object_XMMap setClass(Class<?> c) {
		Object_XMMap instance = new Object_XMMap();
		instance.c = c;
		try {
			instance.obj = c.getConstructor().newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return instance;
	}

	public static Object_XMMap setObject(Object obj) {
		Object_XMMap instance = new Object_XMMap();
		instance.obj = obj;
		instance.c = obj.getClass();
		return instance;
	}

	public void setReferenceReplacedKey(boolean referenceReplacedKey) {
		ReferenceReplacedKey = referenceReplacedKey;
	}

	/**
	 * ��ȡMapͨ��ʵ��
	 * 
	 * @param allowedKeys
	 *            �������ģ���ֵ�ת����keys
	 * @param ignoredKeys
	 *            ���Խ���ģ���ֵ�ת����keys
	 * @param callBack
	 *            ģ�����ת��ΪMao�Ļص�
	 * @return Map���߱����������ͣ�
	 */
	@SuppressWarnings("unchecked")
	private Object getMapByObject(List<String> allowedKeys, List<String> ignoredKeys,
			XMObjectDidFinishConvertingToMap callBack) {
		if (XMJava.isClassFromJavaClass(c)) {// �Ƿ�Ϊģ����
			return obj;
		}
		List<String> allowedPropertyNames = Object_XMClass.setClass(c).getAllowedPropertyNames();
		List<String> ignoredPropertyNames = Object_XMClass.setClass(c).getIgnoredPropertyNames();
		Map<String, Object> map = new HashMap<String, Object>();
		Object_XMProperty.setClass(c).enumerateProperties(new XMPropertyEnumerate() {
			@Override
			public void propertyEnumerate(XMProperty property, boolean stop) {
				// TODO Auto-generated method stub
				if (allowedPropertyNames.size() != 0 && !allowedPropertyNames.contains(property.name)) {
					return;
				}
				if (ignoredPropertyNames.contains(property.name)) {
					return;
				}
				if (allowedKeys != null && !allowedKeys.contains(property.name)) {
					return;
				}
				if (ignoredKeys != null && ignoredKeys.contains(property.name)) {
					return;
				}
				// ��ȡֵ
				Object value = property.getValue(obj);
				if (value == null) {
					return;
				}
				XMPropertyType type = property.type;
				Class<?> typeClass = type.getTypeClass();
				if (!type.isFromJavaClass()) {
					value = setObject(value).getMapByObject(allowedKeys, ignoredKeys, callBack);
				} else if (List.class.isAssignableFrom(typeClass)) {
					value = getMapArrayByClass((List<Object>) value);
				} else if (URL.class.isAssignableFrom(typeClass)) {
					value = value.toString();
				}
				// ��ֵ
				if (ReferenceReplacedKey) {
					List<XMPropertyKey> propertyKeys = property.getPropertyKeys(c);
					Object innerContainer = map;
					for (int i = 0; i < propertyKeys.size(); i++) {
						XMPropertyKey key = propertyKeys.get(i);
						XMPropertyKey nextKey = null;
						if (i < propertyKeys.size() - 1) {
							nextKey = propertyKeys.get(i + 1);
						}
						if (nextKey != null) {
							Object tempInnerContainer = Object_XMKVC.setObject(innerContainer).getValue(key.name);
							if (tempInnerContainer == null) {
								if (nextKey.type == XMPropertyKeyType.Dictionary) {
									tempInnerContainer = new HashMap<>();
								} else {
									tempInnerContainer = new ArrayList<>();
								}
								if (key.type == XMPropertyKeyType.Dictionary) {
									((Map<String, Object>) innerContainer).put(key.name, tempInnerContainer);
								} else {
									((List<Object>) innerContainer).add(Integer.parseInt(key.name), tempInnerContainer);
								}
							}

							innerContainer = tempInnerContainer;
						} else {// ���һ����
							if (key.type == XMPropertyKeyType.Dictionary) {
								((Map<String, Object>) innerContainer).put(key.name, value);
							} else {
								((List<Object>) innerContainer).add(Integer.parseInt(key.name), value);
							}
						}
					}
				} else {
					map.put(property.name, value);
				}
			}
		});
		if (callBack != null) {
			callBack.objectDidFinishConvertingToMap();
		}
		return map;
	}

	/**
	 * ��ȡMap����ͨ�����ʶ
	 * 
	 * @param objects
	 *            ����
	 * @param allowedKeys
	 *            ģ�������������ģ���ֵ�ת����keys
	 * @param ignoredKeys
	 *            ģ��������Խ���ģ���ֵ�ת����keys
	 * @return Map���������ͨ���飨�������飩
	 */
	private static List<Object> getMapArrayByClass(List<Object> objects, List<String> allowedKeys,
			List<String> ignoredKeys) {
		List<Object> mapArray = new ArrayList<>();
		for (Object obj : objects) {
			if (allowedKeys != null) {
				mapArray.add(setObject(obj).getMapByObjectWithAllowedKeys(allowedKeys, null));
			} else {
				mapArray.add(setObject(obj).getMapByObjectWithIgnoredKeys(ignoredKeys, null));
			}
		}
		return mapArray;
	}

	/**
	 * ��ȡMap����ͨ�����ʶ
	 * 
	 * @param objects
	 *            ����
	 * @return Map�������ͨ���飨�������飩
	 */
	public List<Object> getMapArrayByClass(List<Object> objects) {
		return getMapArrayByClass(objects, null, null);
	}

	/**
	 * ��ȡMap����ͨ�����ʶ
	 * 
	 * @param objects
	 *            ����
	 * @param allowedKeys
	 *            ģ�������������ģ���ֵ�ת����keys
	 * @return Map���������ͨ���飨�������飩
	 */
	public List<Object> getMapArrayByClassWithAllowedKeys(List<Object> objects, List<String> allowedKeys) {
		return getMapArrayByClass(objects, allowedKeys, null);
	}

	/**
	 * ��ȡMap����ͨ�����ʶ
	 * 
	 * @param objects
	 *            ����
	 * @param ignoredKeys
	 *            ģ��������Խ���ģ���ֵ�ת����keys
	 * @return Map�������ͨ���飨�������飩
	 */
	public List<Object> getMapArrayByClassWithIgnoredKeys(List<Object> objects, List<String> ignoredKeys) {
		return getMapArrayByClass(objects, null, ignoredKeys);
	}

	/**
	 * ��ȡMapͨ��ʵ��
	 * 
	 * @param callBack
	 *            ģ�����ת��ΪMap�Ļص�
	 * @return Map���߱����������ͣ�
	 */
	public Object getMapByObject(XMObjectDidFinishConvertingToMap callBack) {
		return getMapByObject(null, null, callBack);
	}

	/**
	 * ��ȡMapͨ��ʵ��
	 * 
	 * @param allowedKeys
	 *            �������ģ���ֵ�ת����keys
	 * @param callBack
	 *            ģ�����ת��ΪMap�Ļص�
	 * @return Map���߱����������ͣ�
	 */
	public Object getMapByObjectWithAllowedKeys(List<String> allowedKeys, XMObjectDidFinishConvertingToMap callBack) {
		return getMapByObject(allowedKeys, null, callBack);
	}

	/**
	 * ��ȡMapͨ��ʵ��
	 * 
	 * @param ignoredKeys
	 *            ���Խ���ģ���ֵ�ת����keys
	 * @param callBack
	 *            ģ�����ת��ΪMap�Ļص�
	 * @return Map���߱����������ͣ�
	 */
	public Object getMapByObjectWithIgnoredKeys(List<String> ignoredKeys, XMObjectDidFinishConvertingToMap callBack) {
		return getMapByObject(null, ignoredKeys, callBack);
	}

	/**
	 * ��ȡjsonObjectͨ��ʵ��
	 * 
	 * @return jsonObject
	 */
	public Object getJsonObjectByObject() {
		if (String.class == c) {
			return XMJson.getJsonObject((String) obj,null,null);
		} else if (byte[].class == c) {
			return XMJson.getJsonObject(obj.toString(),null,null);
		}
		return getMapByObject(null);
	}

	/**
	 * ��ȡjsonStringͨ��ʵ��
	 * 
	 * @return jsonString
	 */
	public String getJsonStringByObject() {
		if (String.class == c) {
			return (String) obj;
		} else if (byte[].class == c) {
			return new String((byte[]) obj);
		}
		return XMJson.getJsonString(getJsonObjectByObject());
	}

	/**
	 * Ϊ����ֵ
	 * 
	 * @param map
	 * @param callBack
	 *            Map���ת��Ϊ����Ļص�
	 * @return ����
	 */
	public Object setMapByObject(Object map, XMMapDidFinishConvertingToObject callBack) {
		if (map == null || XMJava.isClassFromJavaClass(c)) {
			return obj;
		}
		// ��ȡjsonObject
		map = setObject(map).getJsonObjectByObject();
		if (!(map instanceof Map)) {
			return null;
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> keyValues = (Map<String, Object>) map;
		List<String> allowedPropertyNames = Object_XMClass.setClass(c).getAllowedPropertyNames();
		List<String> ignoredPropertyNames = Object_XMClass.setClass(c).getIgnoredPropertyNames();
		Object_XMProperty.setClass(obj.getClass()).enumerateProperties(new XMPropertyEnumerate() {
			@SuppressWarnings("unchecked")
			@Override
			public void propertyEnumerate(XMProperty property, boolean stop) {
				// TODO Auto-generated method stub
				if (allowedPropertyNames.size() != 0 && !allowedPropertyNames.contains(property.name)) {
					return;
				}
				if (ignoredPropertyNames.contains(property.name)) {
					return;
				}
				List<XMPropertyKey> propertyKeys = property.getPropertyKeys(c);
				Object value = keyValues;
				for (XMPropertyKey propertyKey : propertyKeys) {
					value = propertyKey.valueInObject(value);
				}
				// �Ƿ���Ҫ����
				Object newValue = Object_XMProperty.setClass(c).getNewValueFromOldValue(value);
				if (newValue != value) {
					property.setValue(newValue, obj);
					return;
				}
				if (value == null) {
					return;
				}
				XMPropertyType type = property.type;
				Class<?> typeClass = type.getTypeClass();
				Class<?> objectClass = property.getObjectClassInArray(c);
				if (!type.isFromJavaClass()) {// �ֵ� -> ģ��
					value = setClass(typeClass).getInstanceByClass(value, callBack);
				} else if (objectClass != null) {// �ֵ����� -> ģ������
					if (URL.class == objectClass) {
						List<String> stringArray = (List<String>) value;
						Class<?> stringClass = stringArray.get(0).getClass();
						if (String.class == stringClass) {// string���� -> URL����
							List<URL> urlArray = new ArrayList<>();
							for (String string : stringArray) {
								try {
									urlArray.add(new URL(string));
								} catch (MalformedURLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							value = urlArray;
						}
					} else {// �ֵ����� -> ģ������
						List<Object> valueArray = (List<Object>) value;
						value = setClass(objectClass).getObjectArrayByClass(valueArray);
					}
				} else {// ���Բ�����
					if (value.getClass() != String.class && typeClass == String.class) {
						value = value.toString();
					} else if (value.getClass() == String.class) {
						if (typeClass == URL.class) {
							try {
								value = new URL(value.toString());
							} catch (MalformedURLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else if (type.isNumber()) {
							if (typeClass == int.class || typeClass == Integer.class) {
								value = Integer.parseInt(value.toString());
							} else if (typeClass == float.class || typeClass == Float.class) {
								value = Float.parseFloat(value.toString());
							} else if (typeClass == double.class || typeClass == Double.class) {
								value = Double.parseDouble(value.toString());
							}
						} else if (type.isBoolean()) {
							String string = value.toString().toLowerCase();
							if (string.equals("true") || string.equals("yes")) {
								value = true;
							} else if (string.equals("false") || string.equals("no")) {
								value = false;
							}
						}
					}

					if (!typeClass.isPrimitive() && !typeClass.isAssignableFrom(value.getClass())) {// ��ֹ��ͬ��֮��ĸ�ֵ
						value = null;
					}
				}
				// ��ֵ
				property.setValue(value, obj);
			}
		});

		if (callBack != null) {
			callBack.mapDidFinishConvertingToObject();
		}
		return obj;
	}

	/**
	 * ��ȡʵ��ͨ�����ʶ
	 * 
	 * @param map
	 * @param callBack
	 *            Map���ת��Ϊ����Ļص�
	 * @return ʵ��
	 */
	public Object getInstanceByClass(Object map, XMMapDidFinishConvertingToObject callBack) {
		return setMapByObject(map, callBack);
	}

	/**
	 * ��ȡģ������ͨ�����ʶ
	 * 
	 * @param mapArray
	 *            map����
	 * @return ����
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getObjectArrayByClass(List<Object> mapArray) {
		// �Ƿ�Ϊģ������
		if (XMJava.isClassFromJavaClass(c)) {
			return mapArray;
		}
		// ��ȡjsonObject
		mapArray = (List<Object>) setObject(mapArray).getJsonObjectByObject();
		if (!(mapArray instanceof List)) {
			return null;
		}
		List<Object> objectArray = new ArrayList<>();
		for (Object map : mapArray) {
			if (map instanceof List) {
				objectArray.add(getObjectArrayByClass((List<Object>) map));
			} else {
				Object obj = getInstanceByClass(map, null);
				if (obj != null) {
					objectArray.add(obj);
				}
			}
		}
		return objectArray;
	}

}
