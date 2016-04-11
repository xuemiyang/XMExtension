package xmextension;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置属性是否进行模型字典转换和归档
 * @author 薛米样
 *
 */
public class Object_XMClass {

	public static final String XMAllowedPropertyNamesKey = "XMAllowedPropertyNamesKey";
	public static final String XMIgnoredPropertyNamesKey = "XMIgnoredPropertyNamesKey";

	
	public interface XMClassEnumerate {
		/**
		 * 遍历类，（父类）
		 * @param c 类
		 * @param stop 停止标识
		 */
		public void classEnumerate(Class<?> c, boolean stop);
	};

	public interface XMAllowedPropertyNames {
		/**
		 * 获取允许进行模型字典转换的属性名称
		 * @return List<String>
		 */
		public List<String> getAllowedPropertyNames();
	}

	public interface XMIgnoredPropertyNames {
		/**
		 * 获取忽略进行模型字典转换
		 * @return List<String>
		 */
		public List<String> getIgnoredPropertyNames();
	}

	private Class<?> c;

	private Object_XMClass() {
		// TODO Auto-generated constructor stub
	}

	private static class Object_XMClassFactory {
		private static Object_XMClass instance = new Object_XMClass();
	}

	public static Object_XMClass setClass(Class<?> c) {
		Object_XMClass instance = Object_XMClassFactory.instance;
		instance.c = c;
		return instance;
	}

	/**
	 * 遍历所有的XMClassEnumerate 除了Java类
	 * @param enumerate XMClassEnumerate
	 * @return Object_XMClass
	 */
	public Object_XMClass enumerateClasses(XMClassEnumerate enumerate) {
		boolean stop = false;
		while (c != null && !stop) {
			enumerate.classEnumerate(c, stop);
			c = c.getSuperclass();
			if (XMJava.isClassFromJavaClass(c)) {
				break;
			}
		}
		return Object_XMClassFactory.instance;
	}

	/**
	 * 遍历所有类
	 * @param enumerate XMClassEnumerate
	 * @return Object_XMClass
	 */
	public Object_XMClass enumerateAllClasses(XMClassEnumerate enumerate) {
		boolean stop = false;
		while (c != null && !stop) {
			enumerate.classEnumerate(c, stop);
			c = c.getSuperclass();
		}
		return Object_XMClassFactory.instance;
	}

	/**
	 * 配置允许进行模型字典转换
	 * @param allowedPropertyNames XMAllowedPropertyNames
	 * @return Object_XMClass
	 */
	public Object_XMClass setupAllowedPropertyNames(XMAllowedPropertyNames allowedPropertyNames) {
		XMDictionaryCache.setObject(c, allowedPropertyNames.getAllowedPropertyNames(),
				XMAllowedPropertyNamesKey);
		return Object_XMClassFactory.instance;
	}

	/**
	 * 配置忽略进行模型字典转换
	 * @param ignoredPropertyNames XMIgnoredPropertyNames
	 * @return Object_XMClass
	 */
	public Object_XMClass setupIgnoredPropertyNames(XMIgnoredPropertyNames ignoredPropertyNames) {
		XMDictionaryCache.setObject(c, ignoredPropertyNames.getIgnoredPropertyNames(),
				XMIgnoredPropertyNamesKey);
		return Object_XMClassFactory.instance;
	}

	/**
	 * 获取属性名称集合
	 * @param key 属性名称集合key
	 * @return List<String>
	 */
	private List<String> getPropertyNames(String key) {
		List<String>propertyNames = new ArrayList<String>();
		enumerateAllClasses(new XMClassEnumerate() {
			@Override
			public void classEnumerate(Class<?> c, boolean stop) {
				// TODO Auto-generated method stub
				@SuppressWarnings("unchecked")
				List<String>strings  = (List<String>)XMDictionaryCache.getObject(c, key);
				if (strings != null) {
					propertyNames.addAll(strings);
				}
			}
		});
		return propertyNames;
	}
	
	/**
	 * 获取允许进行字典模型转换的属性名集合
	 * @return List<String>
	 */
	public List<String> getAllowedPropertyNames() {
		return getPropertyNames(XMAllowedPropertyNamesKey);
	}
	
	/**
	 * 获取忽略进行字典模型转换的属性名集合
	 * @return List<String>
	 */
	public List<String> getIgnoredPropertyNames() {
		return getPropertyNames(XMIgnoredPropertyNamesKey);
	}
	
}
