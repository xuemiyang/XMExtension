package xmextension;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包装属性
 * @author 薛米样
 *
 */
public class XMProperty {

	/** 成员*/
	public Field field;
	/** 属性名称*/
	public String name;
	/** 属性类型*/
	public XMPropertyType type;
	/** 属性来源于那个类 （可能是父类）*/
	public Class<?>scrClass;
	
	/** 属性key数组map*/
	private Map<Class<?>, List<XMPropertyKey>>propertyKeysMap = null;
	/** 模型属性在数组Map*/
	private Map<Class<?>, Class<?>>objectClassInArrayMap = null;
	
	private XMProperty() {
		// TODO Auto-generated constructor stub
	}
	
	public static XMProperty getCacheProperty(Field field) {
		XMProperty property = (XMProperty) XMDictionaryCache.getObject(field);
		if (property == null) {
			property = new XMProperty();
			property.setField(field);
			XMDictionaryCache.setObject(field, property);
		}
		return property;
	}
	
	private void setField(Field field) {
		this.field = field;
		name = field.getName();
		type = XMPropertyType.getCacheType(field.getType());
	}
	
	
	/**
	 * 获取属性键数组map
	 * @return Map<Class<?>, List<XMPropertyKey>>
	 */
	private Map<Class<?>, List<XMPropertyKey>> getPropertyKeysMap() {
		if (propertyKeysMap == null) {
			propertyKeysMap = new HashMap<Class<?>, List<XMPropertyKey>>();
		}
		return propertyKeysMap;
	}
	
	/**
	 * 获取模型类型在数组
	 * @return Map<Class<?>, Class<?>>
	 */
	private Map<Class<?>, Class<?>> getObjectClassInArray() {
		if (objectClassInArrayMap == null) {
			objectClassInArrayMap = new HashMap<>();
		}
		return objectClassInArrayMap;
	}
	
	/**
	 * 设置在字典取值的key
	 * @param originKey 字典取值的key
	 * @param c 类标识
	 */
	public void setOriginkey(String originKey, Class<?>c) {
			List<XMPropertyKey> propertyKeys = getPropertyKeys(originKey);
			setPropertyKeys(propertyKeys, c);
	}
	
	/**
	 * 添加属性key数组到属性key数组map
	 * @param propertyKeys 属性key数组
	 * @param c 类标识
	 */
	private void setPropertyKeys(List<XMPropertyKey> propertyKeys, Class<?>c) {
		if (propertyKeys.size() == 0) {
			return;
		}
		getPropertyKeysMap().put(c, propertyKeys);
	}
	
	/**
	 * 获取属性key数组
	 * @param c 类标识
	 * @return List<XMPropertyKey>
	 */
	public List<XMPropertyKey> getPropertyKeys(Class<?>c) {
		return getPropertyKeysMap().get(c);
	}
	
	/**
	 * 获取属性key数组
	 * @param stringKey 字符串key
	 * @return List<XMPropertyKey>
	 */
	private List<XMPropertyKey> getPropertyKeys(String stringKey) {
		if (stringKey == null || stringKey.length() == 0) {
			return null;
		}
		List<XMPropertyKey>propertyKeys = new ArrayList<>();
		String[] oldKeys = stringKey.split("\\.");
		for (String oldKey : oldKeys) {
			int start = oldKey.indexOf("[");
			if (start != -1) {// 有索引的键
				String prefixKey = oldKey.substring(0, start);
				String indexKey = prefixKey;
				if (prefixKey.length() != 0) {
					XMPropertyKey propertyKey = new XMPropertyKey();
					propertyKey.name = prefixKey;
					propertyKeys.add(propertyKey);
					indexKey = oldKey.replace(prefixKey, "");
				}
				// 处理数组
				indexKey = indexKey.replace("[", "");
				String[] cmps = indexKey.split("]");
				for (String cmp : cmps) {
					XMPropertyKey propertyKey = new XMPropertyKey();
					propertyKey.name = cmp;
					propertyKey.type = XMPropertyKeyType.Array;
					propertyKeys.add(propertyKey);
				}
			}else {
				XMPropertyKey propertyKey = new XMPropertyKey();
				propertyKey.name = oldKey;
				propertyKeys.add(propertyKey);
			}
		}
		return propertyKeys;
	}
	
	/**
	 * 为对象赋值
	 * @param value 值
	 * @param obj 对象
	 * @return boolean
	 */
	public boolean setValue(Object value, Object obj) {
		if (type.isSetDisable() || obj == null) {
			return false;
		}
		return Object_XMKVC.setObject(obj).setValue(value, name);
	}
	
	/**
	 * 获取对象的值
	 * @param obj 对象
	 * @return value 
	 */
	public Object getValue(Object obj) {
		if (type.isSetDisable() || obj == null) {
			return null;
		}
		Object value = Object_XMKVC.setObject(obj).getValue(name);
		return value;
	}
	
	/**
	 * 设置数组属性的模型类型
	 * @param objClass 模型类型
	 * @param c 类标识
	 */
	public void setObjctClassInAraay(Class<?>objClass, Class<?>c) {
		if (objClass == null) {
			return;
		}
		getObjectClassInArray().put(c, objClass);
	}
	
	/**
	 * 获取数组属性的模型类型
	 * @param c 类标识
	 * @return 模型类型
	 */
	public Class<?> getObjectClassInArray(Class<?>c) {
		return getObjectClassInArray().get(c);
	}

}
