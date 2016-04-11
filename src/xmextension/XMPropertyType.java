package xmextension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * ��װ��������
 * 
 * @author Ѧ����
 *
 */
public class XMPropertyType {

	/** �Ƿ�Ϊ��������Number */
	private boolean isNumber;
	/** �Ƿ�����Java��ܵ��� */
	private boolean isFromJavaClass;
	/** ���Ե����� */
	private Class<?> typeClass;
	/** �Ƿ�ɸ�ֵ */
	private boolean isSetDisable;
	/** �Ƿ�Ϊboolean*/
	private boolean isBoolean;

	static final String XMCacheTypeKey = "XMCacheTypeKey";

	private XMPropertyType() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * �Ƿ�Ϊ��������
	 * @return boolean
	 */
	public boolean isNumber() {
		return isNumber;
	}

	/**
	 * �Ƿ�����Java����
	 * @return boolean
	 */
	public boolean isFromJavaClass() {
		return isFromJavaClass;
	}

	/**
	 * ��ȡ���Ե�����
	 * @return Class<?>
	 */
	public Class<?> getTypeClass() {
		return typeClass;
	}

	/**
	 * �Ƿ��������ֵ
	 * @return boolean
	 */
	public boolean isSetDisable() {
		return isSetDisable;
	}
	
	/**
	 * �Ƿ�Ϊboolean����
	 * @return boolean
	 */
	public boolean isBoolean() {
		return isBoolean;
	}

	/**
	 * ��������
	 * @param c ���ʶ
	 */
	private void setClass(Class<?> c) {
		if (c == Method.class || c == Field.class) {
			isSetDisable = true;
		} else if (!XMJava.isClassFromJavaClass(c)) {// �Զ���ģ����
			typeClass = c;
		} else if (XMJava.isClassFromJavaClass(c)) {
			isFromJavaClass = true;
			typeClass = c;
			if (c.isPrimitive()) {// ��������
				isNumber = true;
			}
			if (Number.class.isAssignableFrom(c)) {
				isNumber = true;
			}else if (Boolean.class.isAssignableFrom(c) || boolean.class.isAssignableFrom(c)) {
				isBoolean = true;
				isNumber = false;
			}
			
			
		}
	}

	/**
	 * ��ȡ�������������
	 * @param c ���ʶ
	 * @return XMPropertyType
	 */
	public static XMPropertyType getCacheType(Class<?> c) {
		if (c == null) {
			return null;
		}
		XMPropertyType type = (XMPropertyType) XMDictionaryCache.getObject(c, XMCacheTypeKey);
		if (type == null) {
			type = new XMPropertyType();
			type.setClass(c);
			XMDictionaryCache.setObject(c, type, XMCacheTypeKey);
		}
		return type;
	}

}
