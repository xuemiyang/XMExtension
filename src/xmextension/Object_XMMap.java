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
 * 进行模型字典转换的类
 * 
 * @author 薛米样
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
	 * 获取Map通过实例
	 * 
	 * @param allowedKeys
	 *            允许进行模型字典转换的keys
	 * @param ignoredKeys
	 *            忽略进行模型字典转换的keys
	 * @param callBack
	 *            模型完成转换为Mao的回调
	 * @return Map或者本身（基本类型）
	 */
	@SuppressWarnings("unchecked")
	private Object getMapByObject(List<String> allowedKeys, List<String> ignoredKeys,
			XMObjectDidFinishConvertingToMap callBack) {
		if (XMJava.isClassFromJavaClass(c)) {// 是否为模型类
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
				// 获取值
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
				// 赋值
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
						} else {// 最后一个键
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
	 * 获取Map数组通过类标识
	 * 
	 * @param objects
	 *            数组
	 * @param allowedKeys
	 *            模型数组允许进行模型字典转换的keys
	 * @param ignoredKeys
	 *            模型数组忽略进行模型字典转换的keys
	 * @return Map数组或者普通数组（基本数组）
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
	 * 获取Map数组通过类标识
	 * 
	 * @param objects
	 *            数组
	 * @return Map数组或普通数组（基本数组）
	 */
	public List<Object> getMapArrayByClass(List<Object> objects) {
		return getMapArrayByClass(objects, null, null);
	}

	/**
	 * 获取Map数组通过类标识
	 * 
	 * @param objects
	 *            数组
	 * @param allowedKeys
	 *            模型数组允许进行模型字典转换的keys
	 * @return Map数组或者普通数组（基本数组）
	 */
	public List<Object> getMapArrayByClassWithAllowedKeys(List<Object> objects, List<String> allowedKeys) {
		return getMapArrayByClass(objects, allowedKeys, null);
	}

	/**
	 * 获取Map数组通过类标识
	 * 
	 * @param objects
	 *            数组
	 * @param ignoredKeys
	 *            模型数组忽略进行模型字典转换的keys
	 * @return Map数组或普通数组（基本数组）
	 */
	public List<Object> getMapArrayByClassWithIgnoredKeys(List<Object> objects, List<String> ignoredKeys) {
		return getMapArrayByClass(objects, null, ignoredKeys);
	}

	/**
	 * 获取Map通过实例
	 * 
	 * @param callBack
	 *            模型完成转换为Map的回调
	 * @return Map或者本身（基本类型）
	 */
	public Object getMapByObject(XMObjectDidFinishConvertingToMap callBack) {
		return getMapByObject(null, null, callBack);
	}

	/**
	 * 获取Map通过实例
	 * 
	 * @param allowedKeys
	 *            允许进行模型字典转换的keys
	 * @param callBack
	 *            模型完成转换为Map的回调
	 * @return Map或者本身（基本类型）
	 */
	public Object getMapByObjectWithAllowedKeys(List<String> allowedKeys, XMObjectDidFinishConvertingToMap callBack) {
		return getMapByObject(allowedKeys, null, callBack);
	}

	/**
	 * 获取Map通过实例
	 * 
	 * @param ignoredKeys
	 *            忽略进行模型字典转换的keys
	 * @param callBack
	 *            模型完成转换为Map的回调
	 * @return Map或者本身（基本类型）
	 */
	public Object getMapByObjectWithIgnoredKeys(List<String> ignoredKeys, XMObjectDidFinishConvertingToMap callBack) {
		return getMapByObject(null, ignoredKeys, callBack);
	}

	/**
	 * 获取jsonObject通过实例
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
	 * 获取jsonString通过实例
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
	 * 为对象赋值
	 * 
	 * @param map
	 * @param callBack
	 *            Map完成转换为对象的回调
	 * @return 对象
	 */
	public Object setMapByObject(Object map, XMMapDidFinishConvertingToObject callBack) {
		if (map == null || XMJava.isClassFromJavaClass(c)) {
			return obj;
		}
		// 获取jsonObject
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
				// 是否需要过滤
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
				if (!type.isFromJavaClass()) {// 字典 -> 模型
					value = setClass(typeClass).getInstanceByClass(value, callBack);
				} else if (objectClass != null) {// 字典数组 -> 模型数组
					if (URL.class == objectClass) {
						List<String> stringArray = (List<String>) value;
						Class<?> stringClass = stringArray.get(0).getClass();
						if (String.class == stringClass) {// string数组 -> URL数组
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
					} else {// 字典数组 -> 模型数组
						List<Object> valueArray = (List<Object>) value;
						value = setClass(objectClass).getObjectArrayByClass(valueArray);
					}
				} else {// 属性不兼容
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

					if (!typeClass.isPrimitive() && !typeClass.isAssignableFrom(value.getClass())) {// 防止不同类之间的赋值
						value = null;
					}
				}
				// 赋值
				property.setValue(value, obj);
			}
		});

		if (callBack != null) {
			callBack.mapDidFinishConvertingToObject();
		}
		return obj;
	}

	/**
	 * 获取实例通过类标识
	 * 
	 * @param map
	 * @param callBack
	 *            Map完成转换为对象的回调
	 * @return 实例
	 */
	public Object getInstanceByClass(Object map, XMMapDidFinishConvertingToObject callBack) {
		return setMapByObject(map, callBack);
	}

	/**
	 * 获取模型数组通过类标识
	 * 
	 * @param mapArray
	 *            map数组
	 * @return 数组
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getObjectArrayByClass(List<Object> mapArray) {
		// 是否为模型数组
		if (XMJava.isClassFromJavaClass(c)) {
			return mapArray;
		}
		// 获取jsonObject
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
