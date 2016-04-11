package xmextension.xmjson.xmclass;

import xmextension.xmjson.xmenum.XMJsonValueType;

/**
 * XMJson������
 * 
 * @author Ѧ����
 *
 */
public class XMJsonUtil {

	/**
	 * �Ƿ��ӡ��־
	 */
	public static boolean isLog = true;

	private XMJsonUtil() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * ��ȡ�ַ����м�����
	 * 
	 * @param string
	 *            �ַ���
	 * @return String
	 */
	public static String getContent(String string) {
		return string.substring(1, string.length() - 1);
	}

	/**
	 * ��ȡjson����
	 * 
	 * @param jsonString
	 *            json�ַ���
	 * @return XMJsonValueType
	 */
	public static XMJsonValueType getJsonType(String jsonString) {
		char start = jsonString.charAt(0);
		char end = jsonString.charAt(jsonString.length() - 1);
		if (start == '{' && end == '}') {
			return XMJsonValueType.Map;
		}
		if (start == '[' && end == ']') {
			return XMJsonValueType.Array;
		} else {
			return XMJsonValueType.Error;
		}
	}

	/**
	 * ���˫����
	 * 
	 * @param string
	 *            �ַ���
	 * @return �ַ���
	 */
	public static String addQuotes(String string) {
		if (string != null) {
			return "\"" + string + "\"";
		}
		return null;
	}

	/**
	 * ��ӡ��־
	 * 
	 * @param message
	 *            ��Ϣ
	 */
	public static void log(String message) {
		if (isLog) {
			String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
			String className = Thread.currentThread().getStackTrace()[2].getClassName();
			System.out.println(className + "---->" + methodName + "---->" + message);
		}
	}

	/**
	 * �жϲ����Ƿ�Ϊnull
	 * 
	 * @param strings
	 *            �ַ���
	 * @return boolean
	 */
	public static boolean isNull(String... strings) {
		for (int i = 0; i < strings.length; i++) {
			if (strings[i] == null || strings[i].equals("")) {
				return true;
			}
		}
		return false;
	}
}
