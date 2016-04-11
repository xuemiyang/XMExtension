package xmextension.xmjson.xmclass.xmenumerate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xmextension.xmjson.xmclass.XMJson;
import xmextension.xmjson.xmclass.XMJsonConstant;
import xmextension.xmjson.xmclass.XMJsonUtil;
import xmextension.xmjson.xmenum.XMJsonValueType;

/**
 * 设置json值的代理类
 * 
 * @author 薛米样
 *
 */
public class XMSetJsonValue extends XMDealWithCasesDelegate {

	@Override
	public void dealWithCase1() {
		// TODO Auto-generated method stub
		setJsonValue(context.jsonKey.keyPath, context.off);
	}

	@Override
	public void dealWithCase2() {
		// TODO Auto-generated method stub
		if (checkType(context.valueString, context.valueType)) {
			setJsonValue(context.off);
		}
		context.jsonObject = context.jsonString;
	}

	@Override
	public void dealWithCase3() {
		// TODO Auto-generated method stub
		setJsonValue(context.keyPath, context.off);
	}

	@Override
	public void dealWithCase4() {
		// TODO Auto-generated method stub
		if (context.jsonValue.equals("null") || context.valueString.equals("null")) {
			setJsonValue(0);
		} else if (checkType(context.valueString, context.valueType)) {
			setJsonValue(0);
		} else {
			XMJsonUtil.log(XMJsonConstant.JSONVALUETYPEERROR + ", value:" + context.jsonValue);
			context.jsonObject = null;
		}
	}

	@Override
	public void dealWithCase5() {
		// TODO Auto-generated method stub
		setJsonValue(context.jsonKey.keyPath, 0);
	}

	private void setJsonValue(String keyPath, int off) {
		if (context.jsonCondition == null) {
			context.jsonObject = XMJson.setJsonValue(context.valueString, keyPath, context.jsonValue, null);
		} else {
			context.jsonObject = XMJson.setJsonValue(context.valueString, keyPath, context.jsonValue,
					context.jsonCondition);
		}
		if (context.jsonObject != null) {
			context.jsonString = setSingleValue(context.jsonString, context.valueStart + off, context.valueEnd + off,
					context.jsonObject);
		}
		context.jsonObject = context.jsonString;
	}

	private void setJsonValue(int off) {
		if (context.jsonCondition == null) {
			context.jsonString = setSingleValue(context.jsonString, context.valueStart + off, context.valueEnd + off,
					context.jsonValue);
		} else {
			if (context.jsonCondition.calculate()) {
				context.jsonString = setSingleValue(context.jsonString, context.valueStart + off,
						context.valueEnd + off, context.jsonValue);
			}
		}
		context.jsonObject = context.jsonString;
	}

	public static String setSingleValue(String jsonString, int valueStart, int valueEnd, Object jsonObject) {
		return jsonString.substring(0, valueStart + 1) + jsonObject + jsonString.substring(valueEnd + 1);
	}

	protected boolean checkType(String valueString, XMJsonValueType valueType) {
		String newJsonValue;
		if (context.isArray) {
			context.jsonObject = new ArrayList<>();
			newJsonValue = "[" + context.jsonValue + "]";
		} else {
			context.jsonObject = new HashMap<>();
			newJsonValue = "{\"" + context.jsonKey.name + "\":" + context.jsonValue + "}";
		}
		Object value = XMJson.getJsonObject(newJsonValue, null, null);
		XMGetJsonObject.putKeyValue(context.key, valueString, context.jsonObject, valueType);
		if (value != null && context.jsonObject != null) {
			if (context.isArray) {
				@SuppressWarnings("unchecked")
				List<Object> list = (List<Object>) value;
				if (list.get(0) == null && !context.jsonValue.equals("null")) {
					return false;
				}
			}
			if (isSame(context.jsonObject, value)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static boolean isSame(Object jsonObject, Object value) {
		if (jsonObject == null || value == null) {
			return true;
		}
		if (jsonObject.getClass() == value.getClass()) {
			if (jsonObject instanceof Map) {
				Map<String, Object> map1 = (Map<String, Object>) jsonObject;
				Map<String, Object> map2 = (Map<String, Object>) value;
				for (String key : map1.keySet()) {
					if (!map2.containsKey(key)) {
						return false;
					}
					Object obj2 = map2.get(key);
					Object obj1 = map1.get(key);
					if (!isSame(obj1, obj2)) {
						return false;
					}
				}
				return true;
			} else if (jsonObject instanceof List) {
				List<Object> list1 = (List<Object>) jsonObject;
				List<Object> list2 = (List<Object>) value;
				Object obj1 = list1.get(0);
				for (Object obj2 : list2) {
					if (!isSame(obj1, obj2)) {
						return false;
					}
				}
				return true;
			} else {
				return true;
			}
		}
		return false;
	}

}
