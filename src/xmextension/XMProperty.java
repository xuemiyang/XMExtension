package xmextension;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ��װ����
 * @author Ѧ����
 *
 */
public class XMProperty {

	/** ��Ա*/
	public Field field;
	/** ��������*/
	public String name;
	/** ��������*/
	public XMPropertyType type;
	/** ������Դ���Ǹ��� �������Ǹ��ࣩ*/
	public Class<?>scrClass;
	
	/** ����key����map*/
	private Map<Class<?>, List<XMPropertyKey>>propertyKeysMap = null;
	/** ģ������������Map*/
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
	 * ��ȡ���Լ�����map
	 * @return Map<Class<?>, List<XMPropertyKey>>
	 */
	private Map<Class<?>, List<XMPropertyKey>> getPropertyKeysMap() {
		if (propertyKeysMap == null) {
			propertyKeysMap = new HashMap<Class<?>, List<XMPropertyKey>>();
		}
		return propertyKeysMap;
	}
	
	/**
	 * ��ȡģ������������
	 * @return Map<Class<?>, Class<?>>
	 */
	private Map<Class<?>, Class<?>> getObjectClassInArray() {
		if (objectClassInArrayMap == null) {
			objectClassInArrayMap = new HashMap<>();
		}
		return objectClassInArrayMap;
	}
	
	/**
	 * �������ֵ�ȡֵ��key
	 * @param originKey �ֵ�ȡֵ��key
	 * @param c ���ʶ
	 */
	public void setOriginkey(String originKey, Class<?>c) {
			List<XMPropertyKey> propertyKeys = getPropertyKeys(originKey);
			setPropertyKeys(propertyKeys, c);
	}
	
	/**
	 * �������key���鵽����key����map
	 * @param propertyKeys ����key����
	 * @param c ���ʶ
	 */
	private void setPropertyKeys(List<XMPropertyKey> propertyKeys, Class<?>c) {
		if (propertyKeys.size() == 0) {
			return;
		}
		getPropertyKeysMap().put(c, propertyKeys);
	}
	
	/**
	 * ��ȡ����key����
	 * @param c ���ʶ
	 * @return List<XMPropertyKey>
	 */
	public List<XMPropertyKey> getPropertyKeys(Class<?>c) {
		return getPropertyKeysMap().get(c);
	}
	
	/**
	 * ��ȡ����key����
	 * @param stringKey �ַ���key
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
			if (start != -1) {// �������ļ�
				String prefixKey = oldKey.substring(0, start);
				String indexKey = prefixKey;
				if (prefixKey.length() != 0) {
					XMPropertyKey propertyKey = new XMPropertyKey();
					propertyKey.name = prefixKey;
					propertyKeys.add(propertyKey);
					indexKey = oldKey.replace(prefixKey, "");
				}
				// ��������
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
	 * Ϊ����ֵ
	 * @param value ֵ
	 * @param obj ����
	 * @return boolean
	 */
	public boolean setValue(Object value, Object obj) {
		if (type.isSetDisable() || obj == null) {
			return false;
		}
		return Object_XMKVC.setObject(obj).setValue(value, name);
	}
	
	/**
	 * ��ȡ�����ֵ
	 * @param obj ����
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
	 * �����������Ե�ģ������
	 * @param objClass ģ������
	 * @param c ���ʶ
	 */
	public void setObjctClassInAraay(Class<?>objClass, Class<?>c) {
		if (objClass == null) {
			return;
		}
		getObjectClassInArray().put(c, objClass);
	}
	
	/**
	 * ��ȡ�������Ե�ģ������
	 * @param c ���ʶ
	 * @return ģ������
	 */
	public Class<?> getObjectClassInArray(Class<?>c) {
		return getObjectClassInArray().get(c);
	}

}
