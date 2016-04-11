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
 * �ж��Ƿ�ΪJavaϵͳ������
 * @author Ѧ����
 *
 */
public class XMJava {

	static List<Class<?>> javaClass = null;
	private XMJava() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * ��ȡJava���ͼ���
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
	 * �жϸ����Ƿ�ΪJavaϵͳ����
	 * @param c ��
	 * @return boolean
	 */
	public static boolean isClassFromJavaClass(Class<?> c) {
		if (c == Object.class  || c.isArray() || c.isPrimitive() || c.isEnum()) {// �ж��Ƿ�Ϊobject�����顢�������͡�ö��
			return true;
		}
		boolean result = false;
		for (Class<?> class1 : getJavaClass()) {
			if (class1.isAssignableFrom(c)) {// �ж��Ƿ�Ϊ����
				result = true;
				break;
			}
		}
		return result;
	}

}
