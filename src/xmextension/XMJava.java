package xmextension;

import java.net.URL;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * 判断是否为Java系统的类型
 * @author 薛米样
 *
 */
public class XMJava {

	static List<Class<?>> javaClass = null;
	private XMJava() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * 获取Java类型集合
	 * @return List<Class<?>>
	 */
	public static List<Class<?>> getJavaClass() {
		if (javaClass == null) {
			javaClass = new ArrayList<Class<?>>();
			javaClass.add(URL.class);
			javaClass.add(Boolean.class);
			javaClass.add(Character.class);
			javaClass.add(Number.class);
			javaClass.add(Date.class);
			javaClass.add(Error.class);
			javaClass.add(Map.class);
			javaClass.add(Collection.class);
			javaClass.add(Hashtable.class);
			javaClass.add(String.class);
			javaClass.add(AttributedString.class);
		}
		return javaClass;
	}
	
	/**
	 * 判断该类是否为Java系统类型
	 * @param c 类
	 * @return boolean
	 */
	public static boolean isClassFromJavaClass(Class<?> c) {
		if (c == Object.class  || c.isArray() || c.isPrimitive() || c.isEnum()) {// 判断是否为object、数组、基本类型、枚举
			return true;
		}
		boolean result = false;
		for (Class<?> class1 : getJavaClass()) {
			if (class1.isAssignableFrom(c)) {// 判断是否为子类
				result = true;
				break;
			}
		}
		return result;
	}

}
