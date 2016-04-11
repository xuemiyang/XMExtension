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
 * һ����������json������
 * 
 * @author Ѧ����
 *
 */
public class XMJson {

	/** ���ּ��ϣ�0-9�� */
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
	 * ��ȡjsonObjectͨ������
	 * 
	 * @param jsonString
	 *            json�ַ���
	 * @param keyPaths
	 *            ��·��
	 * @param condition
	 *            ����
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
	 * ��ȡjsonObjectͨ������
	 * 
	 * @param jsonString
	 *            json�ַ���
	 * @param keyPath
	 *            �Ӽ�·��
	 * @param condition
	 *            ����
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
	 * ��ȡjson�ַ���
	 * 
	 * @param jsonObj
	 *            json����
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
	 * ����jsonString��ֵͨ������
	 * 
	 * @param jsonString
	 *            json�ַ���
	 * @param keyPaths
	 *            ��·��
	 * @param jsonValues
	 *            jsonֵ
	 * @param condition
	 *            ����
	 * @return json�ַ���
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
	 * ����jsonString��ֵͨ������
	 * 
	 * @param jsonString
	 *            json�ַ���
	 * @param keyPath
	 *            �Ӽ�·��
	 * @param jsonValue
	 *            ��jsonֵ
	 * @param condition
	 *            ����
	 * @return json�ַ���
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
	 * ����json��
	 * 
	 * @param jsonString
	 *            json�ַ���
	 * @param keyPaths
	 *            ��·��
	 * @param jsonKeys
	 *            json��
	 * @param condition
	 *            ����
	 * @return json�ַ���
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
	 * ����json��
	 * 
	 * @param jsonString
	 *            json�ַ���
	 * @param keyPath
	 *            �Ӽ�·��
	 * @param jsonKey
	 *            ��json��
	 * @param condition
	 *            ����
	 * @return json�ַ���
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
	 * ɾ��json�ַ���ͨ������
	 * 
	 * @param jsonString
	 *            json�ַ���
	 * @param keyPaths
	 *            ��·��
	 * @param condition
	 *            ����
	 * @return json�ַ���
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
	 * ɾ��json�ַ���ͨ������
	 * 
	 * @param jsonString
	 *            json�ַ���
	 * @param keyPath
	 *            �Ӽ�·��
	 * @param condition
	 *            ����
	 * @return json�ַ���
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
	 * ���json�ַ���
	 * 
	 * @param jsonString
	 *            json�ַ���
	 * @param keyPaths
	 *            ��·��
	 * @param jsonValues
	 *            jsonֵ
	 * @param condition
	 *            ����
	 * @return json�ַ���
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
	 * ���json�ַ���
	 * 
	 * @param jsonString
	 *            json�ַ���
	 * @param keyPath
	 *            ��·��
	 * @param jsonValue
	 *            ��jsonֵ
	 * @param condition
	 *            ����
	 * @return json�ַ���
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
	 * ������
	 * 
	 * @param database
	 *            ���ݿ�json�ַ���
	 * @param table
	 *            ����
	 * @param keyValues
	 *            ��ֵ��
	 * @param condition
	 *            ����
	 * @return ���ݿ�json�ַ���
	 */
	public static String createTable(String database, String table, String keyValues) {
		if (XMJsonUtil.isNull(table, keyValues)) {
			XMJsonUtil.log("table,keyValues����Ϊ��");
			return null;
		}
		if (database == null || database.length() == 0) {
			database = "{}";
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) getJsonObjectWithSubPath(keyValues, null, null);
		if (map == null) {
			XMJsonUtil.log("keyValues����");
			return null;
		}
		for (Object value : map.values()) {
			if (value == null) {
				XMJsonUtil.log("keyValues������nullֵ");
				return null;
			}
		}
		String jsonString = XMJsonUtil.addQuotes(table) + ":[" + keyValues + "]";
		database = XMAddJsonString.addJsonValue(database, jsonString);
		addJsonStringToCache(database);
		return database;
	}

	/**
	 * ��������
	 * 
	 * @param database
	 *            ���ݿ�json�ַ���
	 * @param table
	 *            ����
	 * @param keyValues
	 *            ��ֵ��
	 * @return ���ݿ�json�ַ���
	 */
	public static String insertKeyValues(String database, String table, String keyValues, Object condition) {
		if (XMJsonUtil.isNull(database, table, keyValues)) {
			XMJsonUtil.log("database,table,keyValues����Ϊ��");
			return null;
		}
		condition = addPrefixForCondition(table, condition);
		String jsonString = addJsonStringWithSubPath(database, table, keyValues, condition);
		addJsonStringToCache(jsonString);
		return jsonString;
	}

	/**
	 * ɾ��ֵ
	 * 
	 * @param database
	 *            ���ݿ�json�ַ���
	 * @param table
	 *            ����
	 * @param condition
	 *            ����
	 * @return ���ݿ�json�ַ���
	 */
	public static String deleteValues(String database, String table, Object condition) {
		if (XMJsonUtil.isNull(database, table)) {
			XMJsonUtil.log("database,table����Ϊ��");
			return null;
		}
		condition = addPrefixForCondition(table, condition);
		String jsonString = deleteJsonStringWithSubPath(database, table, condition);
		addJsonStringToCache(jsonString);
		return jsonString;
	}

	/**
	 * ����ֵ
	 * 
	 * @param database
	 *            ���ݿ�json�ַ���
	 * @param table
	 *            ����
	 * @param keys
	 *            ��
	 * @param values
	 *            ֵ
	 * @param condition
	 *            ����
	 * @return ���ݿ�json�ַ���
	 */
	public static String updateValues(String database, String table, String keys, String values, Object condition) {
		if (XMJsonUtil.isNull(database, table, keys, values)) {
			XMJsonUtil.log("database,table,keys,values����Ϊ��");
			return null;
		}
		condition = addPrefixForCondition(table, condition);
		keys = addPrefixForKeys(table, keys);
		String jsonString = setJsonValue(database, keys, values, condition);
		addJsonStringToCache(jsonString);
		return jsonString;
	}

	/**
	 * ���¼�
	 * 
	 * @param database
	 *            ���ݿ�json�ַ���
	 * @param table
	 *            ����
	 * @param keys
	 *            ��
	 * @param newKeys
	 *            �½�
	 * @param condition
	 *            ����
	 * @return ���ݿ�json�ַ���
	 */
	public static String updateKeys(String database, String table, String keys, String newKeys, Object condition) {
		if (XMJsonUtil.isNull(database, table, keys, newKeys)) {
			XMJsonUtil.log("database,table,keys,newKeys����Ϊ��");
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
			XMJsonUtil.log("database,keys����Ϊ��");
			return null;
		}
		return getJsonObject(database, keys, condition);
	}

	/**
	 * Ϊ�������ǰ׺
	 * 
	 * @param table
	 *            ��
	 * @param condition
	 *            ����
	 * @return ����
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
	 * Ϊ�����ǰ׺
	 * 
	 * @param table
	 *            ��
	 * @param keys
	 *            ��
	 * @return ��
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
	 * ���jsonString������list
	 * 
	 * @param jsonString
	 *            json�ַ���
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
	 * ��ȡ���ݿ⻺���jsonString
	 * 
	 * @param index
	 *            �±�
	 * @return �����jsonString
	 */
	public static String getCacheJsonString(int index) {
		if (index < getCacheJsonStringList().size()) {
			return getCacheJsonStringList().get(index);
		} else {
			XMJsonUtil.log("��ȡjsonString���±곬������ĳ���");
			return null;
		}
	}

	/**
	 * �������ݿ⻺���jsonString
	 * 
	 * @return �����jsonString
	 */
	public static String revertJsonString() {
		if (getCacheJsonStringList().size() > 1) {
			return getCacheJsonStringList().get(getCacheJsonStringList().size() - 2);
		} else {
			return null;
		}
	}

	/**
	 * ����json�ַ���
	 * 
	 * @param jsonString
	 *            json�ַ���
	 * @param delegate
	 *            �����ַ�������
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
					if (nextCh == ',') {// ֵ�Ŀ�ʼ��Ҳ��ֵ�Ľ���
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
	 * ����Ƿ�Ϊ�����ַ�
	 * 
	 * @param ch
	 *            �ַ�
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
	 * �Ƿ�Ҫ��ӱ�ʶ
	 * 
	 * @param objString
	 *            �����ַ���
	 * @param i
	 *            �±�
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
	 * ���key��ͷ
	 * 
	 * @param ch
	 *            �ַ�
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
	 * ���key��ĩβ
	 * 
	 * @param ch
	 *            �ַ�
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
	 * ���ֵ�Ŀ�ͷ
	 * 
	 * @param ch
	 *            �ַ�
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
	 * ��ȡpart��ʶ
	 * 
	 * @param part
	 *            part��ʶ
	 * @param ch
	 *            �ַ�
	 * @param nextCh
	 *            ��һ���ַ�
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
	 * ��ȡjsonֵ����
	 * 
	 * @param jsonValues
	 *            jsonֵ
	 * @return jsonֵ����
	 */
	private static List<String> getJsonKeyValues(String jsonValues, XMJsonType type) {
		jsonValues = XMJsonUtil.addQuotes(jsonValues);
		XMEnumerateJsonDelegate delegate = new XMGetJsonKeyValue(type);
		@SuppressWarnings("unchecked")
		List<String> list = (List<String>) enumerateJsonString(jsonValues, delegate);
		return list;
	}
}
