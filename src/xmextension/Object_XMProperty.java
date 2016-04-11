package xmextension;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import xmextension.Object_XMClass.XMClassEnumerate;

/**
 * 配置取值的替换名和模型数组的模型类型
 * 
 * @author 薛米样
 *
 */
public class Object_XMProperty {

	public interface XMPropertyEnumerate {
		/**
		 * 遍历属性
		 * 
		 * @param property
		 *            属性
		 * @param stop
		 *            停止标识
		 */
		public void propertyEnumerate(XMProperty property, boolean stop);
	}

	public static interface XMReplacedKeyFromPropertyName {
		/**
		 * 获取替换的键
		 * 
		 * @param propertyName
		 *            属性名
		 * @return 替换的key
		 */
		public String getReplacedKey(String propertyName);
	}

	public static interface XMObjectClassInArray {
		/**
		 * 获取模型数组的模型类型
		 * 
		 * @param propertyName
		 *            属性名称
		 * @return 模型类型
		 */
		public Class<?> getObjectClassInArray(String propertyName);
	}

	public static interface XMNewValueFromOldValue {
		/**
		 * 过滤旧值获取新值
		 * 
		 * @param oldValue
		 *            旧值
		 * @return 新值
		 */
		public Object getNewVelueFromOldValue(Object oldValue);
	}

	public static final String XMReplacedkeyFromPropertyNameKey = "XMReplacedkeyFromPropertyNameKey";
	public static final String XMObjectClassInArrayKey = "XMObjectClassInArrayKey";
	public static final String XMNewValueFromOldValueKey = "XMNewValueFromOldValueKey";
	public static final String XMCacheProperties = "XMCacheProperties";

	private Class<?> c;

	private Object_XMProperty() {
		// TODO Auto-generated constructor stub
	}

	private static class Object_XMPropertyFactory {
		private static Object_XMProperty instance = new Object_XMProperty();
	}

	public static Object_XMProperty setClass(Class<?> c) {
		Object_XMProperty instance = Object_XMPropertyFactory.instance;
		instance.c = c;
		return instance;
	}

	/**
	 * 配置替换的key从属性名
	 * 
	 * @param replacedKeyFromPropertyName
	 *            XMReplacedKeyFromPropertyName
	 * @return Object_XMProperty
	 */
	public Object_XMProperty setupReplacedKeyFromPropertyName(
			XMReplacedKeyFromPropertyName replacedKeyFromPropertyName) {
		XMDictionaryCache.setObject(c, replacedKeyFromPropertyName, XMReplacedkeyFromPropertyNameKey);
		return Object_XMPropertyFactory.instance;
	}

	/**
	 * 配置模型数组的模型类型
	 * 
	 * @param objectClassInArray
	 *            XMObjectClassInArray
	 * @return Object_XMProperty
	 */
	public Object_XMProperty setupObjectClassInArray(XMObjectClassInArray objectClassInArray) {
		XMDictionaryCache.setObject(c, objectClassInArray, XMObjectClassInArrayKey);
		return Object_XMPropertyFactory.instance;
	}

	/**
	 * 配置过滤值的策略
	 * 
	 * @param newValueFromOldValue
	 *            XMNewValueFromOldValue
	 * @return Object_XMProperty
	 */
	public Object_XMProperty setupNewValueFromOldValue(XMNewValueFromOldValue newValueFromOldValue) {
		XMDictionaryCache.setObject(c, newValueFromOldValue, XMNewValueFromOldValueKey);
		return Object_XMPropertyFactory.instance;
	}

	public Object getNewValueFromOldValue(Object oldValue) {
		final Object[] objs = new Object[1];
		objs[0] = oldValue;
		Object_XMClass.setClass(c).enumerateAllClasses(new XMClassEnumerate() {
			@Override
			public void classEnumerate(Class<?> c, boolean stop) {
				// TODO Auto-generated method stub
				XMNewValueFromOldValue newValueFromOldValue = (XMNewValueFromOldValue) XMDictionaryCache.getObject(c,
						XMNewValueFromOldValueKey);
				if (newValueFromOldValue != null) {
					Object newValue = newValueFromOldValue.getNewVelueFromOldValue(oldValue);
					objs[0] = newValue;
					stop = true;
				}
			}
		});

		return objs[0];
	}

	/**
	 * 遍历所有属性
	 * 
	 * @param propertyEnumerate
	 *            XMPropertyEnumerate
	 * @return Object_XMProperty
	 */
	public Object_XMProperty enumerateProperties(XMPropertyEnumerate propertyEnumerate) {
		boolean stop = false;
		List<XMProperty> properties = getProperties();
		for (XMProperty property : properties) {
			propertyEnumerate.propertyEnumerate(property, stop);
			if (stop) {
				break;
			}
		}
		return Object_XMPropertyFactory.instance;
	}

	/**
	 * 获取属性集合
	 * 
	 * @return List<XMProperty>
	 */
	@SuppressWarnings("unchecked")
	private List<XMProperty> getProperties() {
		List<XMProperty> cacheProperties = (List<XMProperty>) XMDictionaryCache.getObject(c, XMCacheProperties);
		if (cacheProperties == null) {
			Object_XMClass.setClass(c).enumerateClasses(new XMClassEnumerate() {// 遍历模型类的所有父类（不包括java类）
				@Override
				public void classEnumerate(Class<?> c, boolean stop) {
					// TODO Auto-generated method stub
					Field[] fields = c.getFields();
					List<XMProperty> properties = new ArrayList<XMProperty>();
					for (Field field : fields) {
						XMProperty property = XMProperty.getCacheProperty(field);
						property.scrClass = c;
						property.setOriginkey(getPropertyKey(property.name), Object_XMProperty.this.c);
						property.setObjctClassInAraay(getPropertyObjectClass(property.name), Object_XMProperty.this.c);
						properties.add(property);
					}
					List<XMProperty> tempProperties = (List<XMProperty>) XMDictionaryCache
							.getObject(Object_XMProperty.this.c, XMCacheProperties);
					if (tempProperties != null) {
						properties.addAll(tempProperties);
					}
					XMDictionaryCache.setObject(Object_XMProperty.this.c, properties, XMCacheProperties);
				}
			});
			cacheProperties = (List<XMProperty>) XMDictionaryCache.getObject(c, XMCacheProperties);
		}

		return cacheProperties;
	}

	/**
	 * 获取属性键
	 * 
	 * @param propertyName
	 *            属性名
	 * @return 属性键
	 */
	private String getPropertyKey(String propertyName) {
		if (propertyName == null) {
			return null;
		}
		String propertyKey = propertyName;
		XMReplacedKeyFromPropertyName replacedKeyFromPropertyName = (XMReplacedKeyFromPropertyName) XMDictionaryCache
				.getObject(c, XMReplacedkeyFromPropertyNameKey);
		if (replacedKeyFromPropertyName != null) {
			propertyKey = replacedKeyFromPropertyName.getReplacedKey(propertyName);
			if (propertyKey == null) {
				propertyKey = propertyName;
			}
		}
		return propertyKey;
	}

	/**
	 * 获取属性对应的模型数组模型类型
	 * 
	 * @param propertyName
	 *            属性名称
	 * @return 模型类型
	 */
	private Class<?> getPropertyObjectClass(String propertyName) {
		if (propertyName == null) {
			return null;
		}
		Class<?> objectClass = null;
		XMObjectClassInArray objectClassInArray = (XMObjectClassInArray) XMDictionaryCache.getObject(c,
				XMObjectClassInArrayKey);
		if (objectClassInArray != null) {
			objectClass = objectClassInArray.getObjectClassInArray(propertyName);
		}

		return objectClass;
	}

}
