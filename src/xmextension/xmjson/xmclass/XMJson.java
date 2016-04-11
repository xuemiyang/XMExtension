package xmextension.xmjson.xmclass;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import xmextension.xmjson.xmclass.xmenumerate.XMAddJsonString;
import xmextension.xmjson.xmclass.xmenumerate.XMEnumerateJsonBuild;
import xmextension.xmjson.xmclass.xmenumerate.XMGetJsonKeyValue;
import xmextension.xmjson.xmenum.XMJsonCheckKeyEndType;
import xmextension.xmjson.xmenum.XMJsonCheckKeyStartType;
import xmextension.xmjson.xmenum.XMJsonCheckType;
import xmextension.xmjson.xmenum.XMJsonType;
import xmextension.xmjson.xmenum.XMJsonValueType;
import xmextension.xmjson.xminterface.XMEnumerateJsonDelegate;

/**
 * 一个轻量级的json解析类
 * 
 * @author 薛米样
 *
 */
public class XMJson {

	/** 数字集合（0-9） */
	private static Set<Character> numberSet;

	private static Set<Character> nextAddMarkSet;

	private static Set<Character> beforeAddMarkSet;

	private static List<String> cacheJsonStringList;
	
	private XMJson() {
		// TODO Auto-generated constructor stub
	}

	private static List<String> getCacheJsonStringList() {
		if (cacheJsonStringList == null) {
			cacheJsonStringList = new LinkedList<>();
		}
		return cacheJsonStringList;
	}

	/**
	 * 获取jsonObject通过条件
	 * 
	 * @param jsonString
	 *            json字符串
	 * @param keyPaths
	 *            键路径
	 * @param condition
	 *            条件
	 * @return jsonObject
	 */
	public static Object getJsonObject(String jsonString, String keyPaths, Object condition) {
		if (jsonString == null) {
			XMJsonUtil.log(XMJsonConstant.JSONSTRINGERROR);
			return null;
		}
		if (keyPaths == null) {
			return getJsonObjectWithSubPath(jsonString, keyPaths, condition);
		} else {
			String[] keyPathCmps = keyPaths.split(",");
			if (keyPathCmps.length == 1) {
				return getJsonObjectWithSubPath(jsonString, keyPaths, condition);
			}
			List<Object> list = new ArrayList<>();
			for (String keyPath : keyPathCmps) {
				list.add(getJsonObjectWithSubPath(jsonString, keyPath, condition));
			}
			return list;
		}
	}

	/**
	 * 获取jsonObject通过条件
	 * 
	 * @param jsonString
	 *            json字符串
	 * @param keyPath
	 *            子键路径
	 * @param condition
	 *            条件
	 * @return jsonObject
	 */
	private static Object getJsonObjectWithSubPath(String jsonString, String keyPath, Object condition) {
		XMEnumerateJsonDelegate delegate = null;
		if (keyPath == null) {
			delegate = XMEnumerateJsonBuild.getInstance().setJsonString(jsonString)
					.getEnumerateJsonDelegate(XMJsonType.GetValue);
		} else if (condition == null || condition.equals("")) {
			delegate = XMEnumerateJsonBuild.getInstance().setJsonString(jsonString).setKeyPath(keyPath)
					.getEnumerateJsonDelegate(XMJsonType.GetValue);
		} else {
			delegate = XMEnumerateJsonBuild.getInstance().setJsonString(jsonString).setKeyPath(keyPath)
					.setCondition(condition).getEnumerateJsonDelegate(XMJsonType.GetValue);
		}
		return enumerateJsonString(jsonString, delegate);
	}

	/**
	 * 获取json字符串
	 * 
	 * @param jsonObj
	 *            json对象
	 * @return String
	 */
	public static String getJsonString(Object jsonObj) {
		if (jsonObj == null) {
			return null;
		}
		String objString = jsonObj.toString();
		StringBuffer jsonString = new StringBuffer();
		char ch;
		boolean isNextAdd;
		for (int i = 0; i < objString.length() - 1; i++) {
			ch = objString.charAt(i);
			isNextAdd = isNextAddMark(objString, i);
			if (ch == '=') {
				ch = ':';
			}
			jsonString.append(ch);
			if (isNextAdd) {
				jsonString.append('"');
			}
		}
		if (jsonObj instanceof Map) {
			jsonString = jsonString.append('}');
		} else if (jsonObj instanceof List) {
			jsonString = jsonString.append(']');
		}
		return jsonString.toString();
	}

	/**
	 * 设置jsonString的值通过条件
	 * 
	 * @param jsonString
	 *            json字符串
	 * @param keyPaths
	 *            键路径
	 * @param jsonValues
	 *            json值
	 * @param condition
	 *            条件
	 * @return json字符串
	 */
	public static String setJsonValue(String jsonString, String keyPaths, String jsonValues, Object condition) {
		if (jsonString == null || keyPaths == null || jsonValues == null) {
			XMJsonUtil.log(XMJsonConstant.JSONSTRINGERROR + ", " + XMJsonConstant.KEYPATHERROR + ", "
					+ XMJsonConstant.JSONVALUEERROR);
			return null;
		}
		String[] keyPathCmps = keyPaths.split(",");
		List<String> list = getJsonKeyValues(jsonValues, XMJsonType.SetValue);
		if (list == null) {
			XMJsonUtil.log(XMJsonConstant.JSONVALUEERROR);
			return null;
		}
		int length = Math.min(keyPathCmps.length, list.size());
		String keyPath = null, jsonValue = null;
		for (int i = 0; i < length; i++) {
			if (i < keyPathCmps.length) {
				keyPath = keyPathCmps[i];
			}
			if (i < list.size()) {
				jsonValue = list.get(i);
			}
			jsonString = setJsonValueWithSubPath(jsonString, keyPath, jsonValue, condition);
			if (jsonString == null) {
				return null;
			}
		}
		return jsonString;
	}

	/**
	 * 设置jsonString的值通过条件
	 * 
	 * @param jsonString
	 *            json字符串
	 * @param keyPath
	 *            子键路径
	 * @param jsonValue
	 *            子json值
	 * @param condition
	 *            条件
	 * @return json字符串
	 */
	private static String setJsonValueWithSubPath(String jsonString, String keyPath, String jsonValue,
			Object condition) {
		XMEnumerateJsonDelegate delegate;
		if (condition == null || condition.equals("")) {
			delegate = XMEnumerateJsonBuild.getInstance().setJsonString(jsonString).setKeyPath(keyPath)
					.setJsonValue(jsonValue).getEnumerateJsonDelegate(XMJsonType.SetValue);
		} else {
			delegate = XMEnumerateJsonBuild.getInstance().setJsonString(jsonString).setKeyPath(keyPath)
					.setJsonValue(jsonValue).setCondition(condition).getEnumerateJsonDelegate(XMJsonType.SetValue);
		}
		return (String) enumerateJsonString(jsonString, delegate);
	}

	/**
	 * 设置json键
	 * 
	 * @param jsonString
	 *            json字符串
	 * @param keyPaths
	 *            键路径
	 * @param jsonKeys
	 *            json键
	 * @param condition
	 *            条件
	 * @return json字符串
	 */
	public static String setJsonKey(String jsonString, String keyPaths, String jsonKeys, Object condition) {
		if (jsonString == null || keyPaths == null || jsonKeys == null) {
			XMJsonUtil.log(XMJsonConstant.JSONSTRINGERROR + ", " + XMJsonConstant.KEYPATHERROR + ", "
					+ XMJsonConstant.JSONVALUEERROR);
			return null;
		}
		String[] keyPathCmps = keyPaths.split(",");
		String[] jsonKeyCmps = jsonKeys.split(",");
		String keyPath = null, jsonKey = null;
		for (int i = 0; i < keyPathCmps.length; i++) {
			keyPath = keyPathCmps[i];
			if (i < jsonKeyCmps.length) {
				jsonKey = jsonKeyCmps[i];
			}
			jsonString = setJsonKeyWithSubPath(jsonString, keyPath, jsonKey, condition);
			if (jsonString == null) {
				return null;
			}
		}
		return jsonString;
	}

	/**
	 * 设置json键
	 * 
	 * @param jsonString
	 *            json字符串
	 * @param keyPath
	 *            子键路径
	 * @param jsonKey
	 *            子json键
	 * @param condition
	 *            条件
	 * @return json字符串
	 */
	private static String setJsonKeyWithSubPath(String jsonString, String keyPath, String jsonKey, Object condition) {
		XMEnumerateJsonDelegate delegate;
		if (condition == null || condition.equals("")) {
			delegate = XMEnumerateJsonBuild.getInstance().setJsonString(jsonString).setKeyPath(keyPath)
					.setJsonValue(jsonKey).getEnumerateJsonDelegate(XMJsonType.SetKey);
		} else {
			delegate = XMEnumerateJsonBuild.getInstance().setJsonString(jsonString).setKeyPath(keyPath)
					.setJsonValue(jsonKey).setCondition(condition).getEnumerateJsonDelegate(XMJsonType.SetKey);
		}
		return (String) enumerateJsonString(jsonString, delegate);
	}

	/**
	 * 删除json字符串通过条件
	 * 
	 * @param jsonString
	 *            json字符串
	 * @param keyPaths
	 *            键路径
	 * @param condition
	 *            条件
	 * @return json字符串
	 */
	public static String deleteJsonString(String jsonString, String keyPaths, Object condition) {
		if (jsonString == null || keyPaths == null) {
			XMJsonUtil.log(XMJsonConstant.JSONSTRINGERROR + ", " + XMJsonConstant.KEYPATHERROR);
			return null;
		}
		String[] keyPathCmps = keyPaths.split(",");
		for (String keyPath : keyPathCmps) {
			jsonString = deleteJsonStringWithSubPath(jsonString, keyPath, condition);
		}
		return jsonString;
	}

	/**
	 * 删除json字符串通过条件
	 * 
	 * @param jsonString
	 *            json字符串
	 * @param keyPath
	 *            子键路径
	 * @param condition
	 *            条件
	 * @return json字符串
	 */
	private static String deleteJsonStringWithSubPath(String jsonString, String keyPath, Object condition) {
		XMEnumerateJsonDelegate delegate;
		if (condition == null || condition.equals("")) {
			delegate = XMEnumerateJsonBuild.getInstance().setJsonString(jsonString).setKeyPath(keyPath)
					.getEnumerateJsonDelegate(XMJsonType.Delete);
		} else {
			delegate = XMEnumerateJsonBuild.getInstance().setJsonString(jsonString).setKeyPath(keyPath)
					.setCondition(condition).getEnumerateJsonDelegate(XMJsonType.Delete);
		}
		return (String) enumerateJsonString(jsonString, delegate);
	}

	/**
	 * 添加json字符串
	 * 
	 * @param jsonString
	 *            json字符串
	 * @param keyPaths
	 *            键路径
	 * @param jsonValues
	 *            json值
	 * @param condition
	 *            条件
	 * @return json字符串
	 */
	public static String addJsonString(String jsonString, String keyPaths, String jsonValues, Object condition) {
		if (jsonString == null || jsonValues == null) {
			XMJsonUtil.log(XMJsonConstant.JSONSTRINGERROR + ", " + XMJsonConstant.JSONVALUEERROR);
			return null;
		}
		List<String> list = getJsonKeyValues(jsonValues, XMJsonType.Add);
		if (list == null) {
			XMJsonUtil.log(XMJsonConstant.JSONVALUEERROR);
			return null;
		}
		if (keyPaths == null) {
			for (String jsonValue : list) {
				jsonString = addJsonStringWithSubPath(jsonString, keyPaths, jsonValue, condition);
				if (jsonString == null) {
					return null;
				}
			}
			return jsonString;
		}
		String[] keyPathCmps = keyPaths.split(",");
		String keyPath = null, jsonValue = null;
		int length = Math.max(keyPathCmps.length, list.size());
		for (int i = 0; i < length; i++) {
			if (i < keyPathCmps.length) {
				keyPath = keyPathCmps[i];
			}
			if (i < list.size()) {
				jsonValue = list.get(i);
			}
			jsonString = addJsonStringWithSubPath(jsonString, keyPath, jsonValue, condition);
			if (jsonString == null) {
				return null;
			}
		}
		return jsonString;
	}

	/**
	 * 添加json字符串
	 * 
	 * @param jsonString
	 *            json字符串
	 * @param keyPath
	 *            键路径
	 * @param jsonValue
	 *            子json值
	 * @param condition
	 *            条件
	 * @return json字符串
	 */
	private static String addJsonStringWithSubPath(String jsonString, String keyPath, String jsonValue,
			Object condition) {
		XMEnumerateJsonDelegate delegate;
		if (keyPath == null) {
			delegate = XMEnumerateJsonBuild.getInstance().setJsonString(jsonString).setJsonValue(jsonValue)
					.getEnumerateJsonDelegate(XMJsonType.Add);
		} else if (condition == null || condition.equals("")) {
			delegate = XMEnumerateJsonBuild.getInstance().setJsonString(jsonString).setKeyPath(keyPath)
					.setJsonValue(jsonValue).getEnumerateJsonDelegate(XMJsonType.Add);
		} else {
			delegate = XMEnumerateJsonBuild.getInstance().setJsonString(jsonString).setKeyPath(keyPath)
					.setJsonValue(jsonValue).setCondition(condition).getEnumerateJsonDelegate(XMJsonType.Add);
		}
		return (String) enumerateJsonString(jsonString, delegate);
	}

	/**
	 * 创建表
	 * 
	 * @param database
	 *            数据库json字符串
	 * @param table
	 *            表名
	 * @param keyValues
	 *            键值对
	 * @param condition
	 *            条件
	 * @return 数据库json字符串
	 */
	public static String createTable(String database, String table, String keyValues) {
		if (XMJsonUtil.isNull(table, keyValues)) {
			XMJsonUtil.log("table,keyValues不能为空");
			return null;
		}
		if (database == null || database.length() == 0) {
			database = "{}";
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) getJsonObjectWithSubPath(keyValues, null, null);
		if (map == null) {
			XMJsonUtil.log("keyValues错误");
			return null;
		}
		for (Object value : map.values()) {
			if (value == null) {
				XMJsonUtil.log("keyValues不能有null值");
				return null;
			}
		}
		String jsonString = XMJsonUtil.addQuotes(table) + ":[" + keyValues + "]";
		database = XMAddJsonString.addJsonValue(database, jsonString);
		addJsonStringToCache(database);
		return database;
	}

	/**
	 * 插入数据
	 * 
	 * @param database
	 *            数据库json字符串
	 * @param table
	 *            表名
	 * @param keyValues
	 *            键值对
	 * @return 数据库json字符串
	 */
	public static String insertKeyValues(String database, String table, String keyValues, Object condition) {
		if (XMJsonUtil.isNull(database, table, keyValues)) {
			XMJsonUtil.log("database,table,keyValues不能为空");
			return null;
		}
		condition = addPrefixForCondition(table, condition);
		String jsonString = addJsonStringWithSubPath(database, table, keyValues, condition);
		addJsonStringToCache(jsonString);
		return jsonString;
	}

	/**
	 * 删除值
	 * 
	 * @param database
	 *            数据库json字符串
	 * @param table
	 *            表名
	 * @param condition
	 *            条件
	 * @return 数据库json字符串
	 */
	public static String deleteValues(String database, String table, Object condition) {
		if (XMJsonUtil.isNull(database, table)) {
			XMJsonUtil.log("database,table不能为空");
			return null;
		}
		condition = addPrefixForCondition(table, condition);
		String jsonString = deleteJsonStringWithSubPath(database, table, condition);
		addJsonStringToCache(jsonString);
		return jsonString;
	}

	/**
	 * 更新值
	 * 
	 * @param database
	 *            数据库json字符串
	 * @param table
	 *            表名
	 * @param keys
	 *            键
	 * @param values
	 *            值
	 * @param condition
	 *            条件
	 * @return 数据库json字符串
	 */
	public static String updateValues(String database, String table, String keys, String values, Object condition) {
		if (XMJsonUtil.isNull(database, table, keys, values)) {
			XMJsonUtil.log("database,table,keys,values不能为空");
			return null;
		}
		condition = addPrefixForCondition(table, condition);
		keys = addPrefixForKeys(table, keys);
		String jsonString = setJsonValue(database, keys, values, condition);
		addJsonStringToCache(jsonString);
		return jsonString;
	}

	/**
	 * 更新键
	 * 
	 * @param database
	 *            数据库json字符串
	 * @param table
	 *            表名
	 * @param keys
	 *            键
	 * @param newKeys
	 *            新建
	 * @param condition
	 *            条件
	 * @return 数据库json字符串
	 */
	public static String updateKeys(String database, String table, String keys, String newKeys, Object condition) {
		if (XMJsonUtil.isNull(database, table, keys, newKeys)) {
			XMJsonUtil.log("database,table,keys,newKeys不能为空");
			return null;
		}
		condition = addPrefixForCondition(table, condition);
		keys = addPrefixForKeys(table, keys);
		String jsonString = setJsonKey(database, keys, newKeys, condition);
		addJsonStringToCache(jsonString);
		return jsonString;
	}

	public static Object selectJsonObject(String database, String keys, Object condition) {
		if (XMJsonUtil.isNull(database, keys)) {
			XMJsonUtil.log("database,keys不能为空");
			return null;
		}
		return getJsonObject(database, keys, condition);
	}

	/**
	 * 为条件添加前缀
	 * 
	 * @param table
	 *            表
	 * @param condition
	 *            条件
	 * @return 条件
	 */
	private static Object addPrefixForCondition(String table, Object condition) {
		String conditionS = null;
		if (condition instanceof String) {
			conditionS = (String) condition;
		}else {
			return condition;
		}
		table = table.trim();
		conditionS = conditionS.trim();
		int index = table.indexOf('.');
		if (index != -1) {
			table = table.substring(0,index);
		}
		char ch;
		int count = 0, off = 0, length = conditionS.length();
		String newString = conditionS;
		for (int i = 0; i < length; i++) {
			ch = conditionS.charAt(i);
			if (ch == ' ' || ch != '"') {
				continue;
			}
			count++;
			if (count % 2 == 1) {
				newString = newString.subSequence(0, i + 1 + off) + table + "." + newString.substring(i + 1 + off);
				off = newString.length() - length;
			}
		}
		return newString;
	}

	/**
	 * 为键添加前缀
	 * 
	 * @param table
	 *            表
	 * @param keys
	 *            键
	 * @return 键
	 */
	private static String addPrefixForKeys(String table, String keys) {
		table = table.trim();
		keys = keys.trim();
		char ch;
		keys = table + "." + keys;
		int off = 0, length = keys.length();
		String newString = keys;
		boolean isAdd = false;
		for (int i = 0; i < length; i++) {
			ch = keys.charAt(i);
			if (ch == ' ' || ch == ',') {
				if (ch == ',') {
					isAdd = true;
				}
				continue;
			}
			if (isAdd) {
				newString = newString.substring(0, i + off) + table + "." + newString.substring(i + off);
				off = newString.length() - length;
				isAdd = false;
			}
		}
		return newString;
	}

	/**
	 * 添加jsonString到缓存list
	 * 
	 * @param jsonString
	 *            json字符串
	 */
	private static void addJsonStringToCache(String jsonString) {
		if (getCacheJsonStringList().size() < 5) {
			getCacheJsonStringList().add(jsonString);
		} else {
			getCacheJsonStringList().remove(0);
			getCacheJsonStringList().add(jsonString);
		}
	}

	/**
	 * 获取数据库缓存的jsonString
	 * 
	 * @param index
	 *            下表
	 * @return 缓存的jsonString
	 */
	public static String getCacheJsonString(int index) {
		if (index < getCacheJsonStringList().size()) {
			return getCacheJsonStringList().get(index);
		} else {
			XMJsonUtil.log("获取jsonString的下标超过缓存的长度");
			return null;
		}
	}

	/**
	 * 反悔数据库缓存的jsonString
	 * 
	 * @return 缓存的jsonString
	 */
	public static String revertJsonString() {
		if (getCacheJsonStringList().size() > 1) {
			return getCacheJsonStringList().get(getCacheJsonStringList().size() - 2);
		} else {
			return null;
		}
	}

	/**
	 * 遍历json字符串
	 * 
	 * @param jsonString
	 *            json字符串
	 * @param delegate
	 *            遍历字符串代理
	 * @return Object
	 */
	private static Object enumerateJsonString(String jsonString, XMEnumerateJsonDelegate delegate) {
		jsonString = jsonString.trim();
		XMJsonCheckType checkType = delegate.beginConvert(jsonString);
		if (checkType == null) {
			return null;
		}
		XMJsonValueType valueType = null;
		Object jsonObject = null;
		jsonString = XMJsonUtil.getContent(jsonString) + ",";
		int keyStart = 0, keyEnd, part = 0, valueStart = 0, valueEnd;
		String key = null, valueString;
		char ch, nextCh;
		for (int i = 0; i < jsonString.length() - 1; i++) {
			ch = jsonString.charAt(i);
			nextCh = jsonString.charAt(i + 1);
			if (ch == ' ') {
				continue;
			}
			switch (checkType) {
			case KeyStart:
				XMJsonCheckKeyStartType keyStartType = checkKeyStart(ch);
				if (keyStartType == XMJsonCheckKeyStartType.Error) {
					XMJsonUtil.log(XMJsonConstant.KEYERROR + ", index:" + i);
					return null;
				} else {
					checkType = XMJsonCheckType.KeyEnd;
					keyStart = i + 1;
				}
				break;
			case KeyEnd:
				XMJsonCheckKeyEndType keyEndType = checkKeyEnd(ch, nextCh);
				if (keyEndType == XMJsonCheckKeyEndType.Error) {
					XMJsonUtil.log(XMJsonConstant.KEYERROR + ", key:" + jsonString.substring(keyStart, i));
					return null;
				} else if (keyEndType == XMJsonCheckKeyEndType.Success) {
					keyEnd = i;
					i++;
					key = jsonString.substring(keyStart, keyEnd);
					checkType = XMJsonCheckType.ValueStart;
					delegate.getKeyFinish(key, keyStart, keyEnd);
				}
				break;
			case ValueStart:
				valueType = checkValueStart(ch);
				if (valueType == XMJsonValueType.Error) {
					XMJsonUtil.log(XMJsonConstant.VALUEERROR + ", index:" + i);
					return null;
				} else {
					valueStart = i;
					checkType = XMJsonCheckType.ValueEnd;
					if (nextCh == ',') {// 值的开始，也是值的结束
						i--;
					}
				}
				break;
			case ValueEnd:
				part = getPart(part, ch, nextCh, valueType);
				if (part == 1) {
					part = 0;
					if (nextCh != ',') {
						XMJsonUtil
								.log(XMJsonConstant.VALUEERROR + ", value:" + jsonString.substring(valueStart, i + 1));
						return null;
					} else {
						valueEnd = i + 1;
						valueString = jsonString.substring(valueStart, valueEnd);
						if ((valueType == XMJsonValueType.Null && !valueString.equals("null"))
								|| (valueType == XMJsonValueType.Boolean
										&& (!valueString.endsWith("true") && !valueString.equals("false")))) {
							XMJsonUtil.log(XMJsonConstant.VALUEERROR + ", value:"
									+ jsonString.substring(valueStart, valueEnd));
							return null;
						}
						jsonObject = delegate.getValueFinish(valueString, valueType, valueStart, valueEnd);
						if (delegate.isReturnObject()) {
							return jsonObject;
						}
						checkType = delegate.finishCovert();
						i++;
					}
				}
				break;
			default:
				break;
			}
		}
		return jsonObject;
	}

	/**
	 * 检查是否为数字字符
	 * 
	 * @param ch
	 *            字符
	 * @return boolean
	 */
	private static boolean isNum(char ch) {
		if (numberSet == null) {
			String num = "1234567890";
			numberSet = new HashSet<>();
			for (int i = 0; i < num.length(); i++) {
				numberSet.add(num.charAt(i));
			}
		}
		return numberSet.contains(ch);
	}

	/**
	 * 是否要添加标识
	 * 
	 * @param objString
	 *            对象字符串
	 * @param i
	 *            下标
	 * @return boolean
	 */
	private static boolean isNextAddMark(String objString, int i) {
		if (nextAddMarkSet == null) {
			nextAddMarkSet = new HashSet<>();
			String mark = "{[ =";
			for (int j = 0; j < mark.length(); j++) {
				nextAddMarkSet.add(mark.charAt(j));
			}
		}
		if (beforeAddMarkSet == null) {
			beforeAddMarkSet = new HashSet<>();
			String mark = "=}],";
			for (int j = 0; j < mark.length(); j++) {
				beforeAddMarkSet.add(mark.charAt(j));
			}
		}
		char ch = objString.charAt(i);
		char nextCh = objString.charAt(i + 1);
		boolean isNextAdd = false;
		if (nextAddMarkSet.contains(ch) && !nextAddMarkSet.contains(nextCh) && !isNum(nextCh)) {
			isNextAdd = true;
		}
		if (!beforeAddMarkSet.contains(ch) && beforeAddMarkSet.contains(nextCh) && !isNum(ch)) {
			isNextAdd = true;
		}
		String string;
		if (i + 5 < objString.length() && isNextAdd) {
			string = objString.substring(i + 1, i + 5);
			if (string.equals("true") || string.equals("null")) {
				isNextAdd = false;
			} else if (i + 6 < objString.length()) {
				string = objString.substring(i + 1, i + 6);
				if (string.equals("false")) {
					isNextAdd = false;
				}
			}
		}

		if (i > 3 && isNextAdd) {
			string = objString.substring(i - 3, i + 1);
			if (string.equals("true") || string.equals("null")) {
				isNextAdd = false;
			} else if (i > 4) {
				string = objString.substring(i - 4, i + 1);
				if (string.equals("false")) {
					isNextAdd = false;
				}
			}
		}

		return isNextAdd;
	}

	/**
	 * 检查key开头
	 * 
	 * @param ch
	 *            字符
	 * @return XMJsonCheckKeyStartType
	 */
	private static XMJsonCheckKeyStartType checkKeyStart(char ch) {
		if (ch != '"') {
			return XMJsonCheckKeyStartType.Error;
		} else {
			return XMJsonCheckKeyStartType.Success;
		}
	}

	/**
	 * 检查key的末尾
	 * 
	 * @param ch
	 *            字符
	 * @param nextCh
	 * @return XMJsonCheckKeyEndType
	 */
	private static XMJsonCheckKeyEndType checkKeyEnd(char ch, char nextCh) {
		if (ch == '"') {
			if (nextCh == ':') {
				return XMJsonCheckKeyEndType.Success;
			}
			return XMJsonCheckKeyEndType.Error;
		}
		return XMJsonCheckKeyEndType.Pass;
	}

	/**
	 * 检查值的开头
	 * 
	 * @param ch
	 *            字符
	 * @return XMJsonValueType
	 */
	private static XMJsonValueType checkValueStart(char ch) {
		if (ch == '{') {
			return XMJsonValueType.Map;
		} else if (ch == '[') {
			return XMJsonValueType.Array;
		} else if (ch == '"') {
			return XMJsonValueType.String;
		} else if (isNum(ch)) {
			return XMJsonValueType.Number;
		} else if (ch == 'n') {
			return XMJsonValueType.Null;
		} else if (ch == 't' || ch == 'f') {
			return XMJsonValueType.Boolean;
		} else {
			return XMJsonValueType.Error;
		}
	}

	/**
	 * 获取part标识
	 * 
	 * @param part
	 *            part标识
	 * @param ch
	 *            字符
	 * @param nextCh
	 *            下一个字符
	 * @param valueType
	 *            XMJsonValueType
	 * @return int
	 */
	private static int getPart(int part, char ch, char nextCh, XMJsonValueType valueType) {
		char mark = 0, otherMark = 0;
		switch (valueType) {
		case Map:
			mark = '}';
			otherMark = '{';
			break;
		case Array:
			mark = ']';
			otherMark = '[';
			break;
		case String:
			if (ch == '"') {
				return 1;
			} else {
				return 0;
			}
		case Number:
			if (nextCh == ',') {
				return 1;
			} else {
				return 0;
			}
		case Null:
			if (nextCh == ',') {
				return 1;
			} else {
				return 0;
			}
		case Boolean:
			if (nextCh == ',') {
				return 1;
			} else {
				return 0;
			}
		default:
			return 1;
		}
		if (ch == mark) {
			return ++part;
		} else if (ch == otherMark) {
			return --part;
		} else {
			return part;
		}
	}

	/**
	 * 获取json值集合
	 * 
	 * @param jsonValues
	 *            json值
	 * @return json值集合
	 */
	private static List<String> getJsonKeyValues(String jsonValues, XMJsonType type) {
		jsonValues = XMJsonUtil.addQuotes(jsonValues);
		XMEnumerateJsonDelegate delegate = new XMGetJsonKeyValue(type);
		@SuppressWarnings("unchecked")
		List<String> list = (List<String>) enumerateJsonString(jsonValues, delegate);
		return list;
	}
}
