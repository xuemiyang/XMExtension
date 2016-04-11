package xmextension;

import java.util.ArrayList;
import java.util.List;

/**
 * ���������Ƿ����ģ���ֵ�ת���͹鵵
 * @author Ѧ����
 *
 */
public class Object_XMClass {

	public static final String XMAllowedPropertyNamesKey = "XMAllowedPropertyNamesKey";
	public static final String XMIgnoredPropertyNamesKey = "XMIgnoredPropertyNamesKey";

	
	public interface XMClassEnumerate {
		/**
		 * �����࣬�����ࣩ
		 * @param c ��
		 * @param stop ֹͣ��ʶ
		 */
		public void classEnumerate(Class<?> c, boolean stop);
	};

	public interface XMAllowedPropertyNames {
		/**
		 * ��ȡ�������ģ���ֵ�ת������������
		 * @return List<String>
		 */
		public List<String> getAllowedPropertyNames();
	}

	public interface XMIgnoredPropertyNames {
		/**
		 * ��ȡ���Խ���ģ���ֵ�ת��
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
	 * �������е�XMClassEnumerate ����Java��
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
	 * ����������
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
	 * �����������ģ���ֵ�ת��
	 * @param allowedPropertyNames XMAllowedPropertyNames
	 * @return Object_XMClass
	 */
	public Object_XMClass setupAllowedPropertyNames(XMAllowedPropertyNames allowedPropertyNames) {
		XMDictionaryCache.setObject(c, allowedPropertyNames.getAllowedPropertyNames(),
				XMAllowedPropertyNamesKey);
		return Object_XMClassFactory.instance;
	}

	/**
	 * ���ú��Խ���ģ���ֵ�ת��
	 * @param ignoredPropertyNames XMIgnoredPropertyNames
	 * @return Object_XMClass
	 */
	public Object_XMClass setupIgnoredPropertyNames(XMIgnoredPropertyNames ignoredPropertyNames) {
		XMDictionaryCache.setObject(c, ignoredPropertyNames.getIgnoredPropertyNames(),
				XMIgnoredPropertyNamesKey);
		return Object_XMClassFactory.instance;
	}

	/**
	 * ��ȡ�������Ƽ���
	 * @param key �������Ƽ���key
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
	 * ��ȡ��������ֵ�ģ��ת��������������
	 * @return List<String>
	 */
	public List<String> getAllowedPropertyNames() {
		return getPropertyNames(XMAllowedPropertyNamesKey);
	}
	
	/**
	 * ��ȡ���Խ����ֵ�ģ��ת��������������
	 * @return List<String>
	 */
	public List<String> getIgnoredPropertyNames() {
		return getPropertyNames(XMIgnoredPropertyNamesKey);
	}
	
}
