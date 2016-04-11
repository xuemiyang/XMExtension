package xmextension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 包装属性类型
 * 
 * @author 薛米样
 *
 */
public class XMPropertyType {

	/** 是否为基本类型Number */
	private boolean isNumber;
	/** 是否来自Java框架的类 */
	private boolean isFromJavaClass;
	/** 属性的类型 */
	private Class<?> typeClass;
	/** 是否可赋值 */
	private boolean isSetDisable;
	/** 是否为boolean*/
	private boolean isBoolean;

	static final String XMCacheTypeKey = "XMCacheTypeKey";

	private XMPropertyType() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 是否为数字类型
	 * @return boolean
	 */
	public boolean isNumber() {
		return isNumber;
	}

	/**
	 * 是否来自Java类型
	 * @return boolean
	 */
	public boolean isFromJavaClass() {
		return isFromJavaClass;
	}

	/**
	 * 获取属性的类型
	 * @return Class<?>
	 */
	public Class<?> getTypeClass() {
		return typeClass;
	}

	/**
	 * 是否可以设置值
	 * @return boolean
	 */
	public boolean isSetDisable() {
		return isSetDisable;
	}
	
	/**
	 * 是否为boolean类型
	 * @return boolean
	 */
	public boolean isBoolean() {
		return isBoolean;
	}

	/**
	 * 设置类型
	 * @param c 类标识
	 */
	private void setClass(Class<?> c) {
		if (c == Method.class || c == Field.class) {
			isSetDisable = true;
		} else if (!XMJava.isClassFromJavaClass(c)) {// 自定义模型类
			typeClass = c;
		} else if (XMJava.isClassFromJavaClass(c)) {
			isFromJavaClass = true;
			typeClass = c;
			if (c.isPrimitive()) {// 基本类型
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
	 * 获取缓存的属性类型
	 * @param c 类标识
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
