package xmextension.xmjson.xmclass.xmenumerate;

import java.util.List;
import java.util.Map;

import xmextension.xmjson.xmclass.XMJson;
import xmextension.xmjson.xmclass.XMJsonConstant;
import xmextension.xmjson.xmclass.XMJsonUtil;
import xmextension.xmjson.xmenum.XMJsonValueType;

/**
 * 添加json字符串的代理类
 * 
 * @author 薛米样
 *
 */
public class XMAddJsonString extends XMGetJsonObject {
	private String jsonString, jsonValue;
	private int off = 0, length;

	public XMAddJsonString(String jsonString, String jsonValue) {
		// TODO Auto-generated constructor stub
		this.jsonString = jsonString.trim();
		this.jsonValue = jsonValue.trim();
		length = jsonString.trim().length();
	}

	@Override
	public Object getValueFinish(String valueString, XMJsonValueType valueType, int valueStart, int valueEnd) {
		// TODO Auto-generated method stub
		isReturn = true;
		XMJsonUtil.isLog = false;
		if (checkType()) {
			XMJsonUtil.isLog = true;
			jsonString = addJsonValue(jsonString, jsonValue);
			return jsonString;
		} else {
			XMJsonUtil.isLog = true;
			if (isArray) {
				jsonObject = XMJson.addJsonString(valueString, null, jsonValue, null);
				if (jsonObject != null) {
					jsonString = XMSetJsonValue.setSingleValue(jsonString, valueStart + off, valueEnd + off,
							jsonObject);
				}
				off = jsonString.length() - length;
				isReturn = false;
				return jsonString;
			}
			XMJsonUtil.log(XMJsonConstant.ADDJSONSTRINGTYPRERROR+", value:"+valueString);
			return null;
		}
	}

	public static String addJsonValue(String valueString, String jsonValue) {
		int lastIndex = valueString.length() - 1;
		StringBuffer buffer = new StringBuffer(valueString);
		if (valueString.length() > 2) {
			buffer.insert(lastIndex, ", " + jsonValue);
		} else {
			buffer.insert(lastIndex, jsonValue);
		}
		return buffer.toString();
	}

	private boolean checkType() {
		String newJsonValue;
		if (isArray) {
			newJsonValue = "[" + jsonValue + "]";
		} else {
			newJsonValue = "{" + jsonValue + "}";
		}
		Object value = XMJson.getJsonObject(newJsonValue, null, null);
		jsonObject = XMJson.getJsonObject(jsonString, null, null);
		if (value != null && jsonObject != null) {
			if (isArray) {
				@SuppressWarnings("unchecked")
				List<Object> list = (List<Object>) value;
				if (list.get(0) == null && !jsonValue.equals("null")) {
					return false;
				}
			}
			return isAdd(jsonObject, value);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static boolean isAdd(Object jsonObject, Object value) {
		if (jsonObject.getClass() == value.getClass()) {
			if (jsonObject instanceof Map) {
				Map<String, Object> map = (Map<String, Object>) jsonObject;
				Map<String, Object> valueMap = (Map<String, Object>) value;
				for (String key : valueMap.keySet()) {
					if (map.containsKey(key)) {
						return false;
					}
				}
				return true;
			} else if (jsonObject instanceof List) {
				List<Object> list1 = (List<Object>) jsonObject;
				List<Object> list2 = (List<Object>) value;
				return XMSetJsonValue.isSame(list1, list2);
			}
		}
		return false;
	}

}
