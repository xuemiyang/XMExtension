package xmextension.xmjson.xmclass;

import xmextension.xmjson.xmenum.XMJsonValueType;

/**
 * XMJson工具类
 * 
 * @author 薛米样
 *
 */
public class XMJsonUtil {

	/**
	 * 是否打印日志
	 */
	public static boolean isLog = true;

	private XMJsonUtil() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 获取字符串中间内容
	 * 
	 * @param string
	 *            字符串
	 * @return String
	 */
	public static String getContent(String string) {
		return string.substring(1, string.length() - 1);
	}

	/**
	 * 获取json类型
	 * 
	 * @param jsonString
	 *            json字符串
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
	 * 添加双引号
	 * 
	 * @param string
	 *            字符串
	 * @return 字符串
	 */
	public static String addQuotes(String string) {
		if (string != null) {
			return "\"" + string + "\"";
		}
		return null;
	}

	/**
	 * 打印日志
	 * 
	 * @param message
	 *            信息
	 */
	public static void log(String message) {
		if (isLog) {
			String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
			String className = Thread.currentThread().getStackTrace()[2].getClassName();
			System.out.println(className + "---->" + methodName + "---->" + message);
		}
	}

	/**
	 * 判断参数是否为null
	 * 
	 * @param strings
	 *            字符串
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
