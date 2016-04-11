package xmextension;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import xmextension.Object_XMClass.XMClassEnumerate;

/**
 * ����ȡֵ���滻����ģ�������ģ������
 * 
 * @author Ѧ����
 *
 */
public class Object_XMProperty {

	public interface XMPropertyEnumerate {
		/**
		 * ��������
		 * 
		 * @param property
		 *            ����
		 * @param stop
		 *            ֹͣ��ʶ
		 */
		public void propertyEnumerate(XMProperty property, boolean stop);
	}

	public static interface XMReplacedKeyFromPropertyName {
		/**
		 * ��ȡ�滻�ļ�
		 * 
		 * @param propertyName
		 *            ������
		 * @return �滻��key
		 */
		public String getReplacedKey(String propertyName);
	}

	public static interface XMObjectClassInArray {
		/**
		 * ��ȡģ�������ģ������
		 * 
		 * @param propertyName
		 *            ��������
		 * @return ģ������
		 */
		public Class<?> getObjectClassInArray(String propertyName);
	}

	public static interface XMNewValueFromOldValue {
		/**
		 * ���˾�ֵ��ȡ��ֵ
		 * 
		 * @param oldValue
		 *            ��ֵ
		 * @return ��ֵ
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
	 * �����滻��key��������
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
	 * ����ģ�������ģ������
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
	 * ���ù���ֵ�Ĳ���
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
	 * ������������
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
	 * ��ȡ���Լ���
	 * 
	 * @return List<XMProperty>
	 */
	@SuppressWarnings("unchecked")
	private List<XMProperty> getProperties() {
		List<XMProperty> cacheProperties = (List<XMProperty>) XMDictionaryCache.getObject(c, XMCacheProperties);
		if (cacheProperties == null) {
			Object_XMClass.setClass(c).enumerateClasses(new XMClassEnumerate() {// ����ģ��������и��ࣨ������java�ࣩ
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
	 * ��ȡ���Լ�
	 * 
	 * @param propertyName
	 *            ������
	 * @return ���Լ�
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
	 * ��ȡ���Զ�Ӧ��ģ������ģ������
	 * 
	 * @param propertyName
	 *            ��������
	 * @return ģ������
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
