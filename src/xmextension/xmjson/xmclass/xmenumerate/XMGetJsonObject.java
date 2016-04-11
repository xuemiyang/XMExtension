package xmextension.xmjson.xmclass.xmenumerate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xmextension.xmjson.xmclass.XMJson;
import xmextension.xmjson.xmclass.XMJsonConstant;
import xmextension.xmjson.xmclass.XMJsonUtil;
import xmextension.xmjson.xmenum.XMJsonCheckType;
import xmextension.xmjson.xmenum.XMJsonValueType;
import xmextension.xmjson.xminterface.XMEnumerateJsonDelegate;


/**
 * 获取json对象的代理类
 * 
 * @author 薛米样
 *
 */
public class XMGetJsonObject implements XMEnumerateJsonDelegate {
	protected boolean isArray, isReturn = false;
	protected Object jsonObject;
	private String key;
	
	@Override
	public XMJsonCheckType beginConvert(String jsonString) {
		// TODO Auto-generated method stub
		jsonString = jsonString.trim();
		XMJsonValueType valueType = XMJsonUtil.getJsonType(jsonString);
		XMJsonCheckType checkType;
		if (valueType == XMJsonValueType.Map) {
			isArray = false;
			checkType = XMJsonCheckType.KeyStart;
		} else if (valueType == XMJsonValueType.Array) {
			isArray = true;
			checkType = XMJsonCheckType.ValueStart;
		} else {
			XMJsonUtil.log(XMJsonConstant.JSONSTRINGERROR);
			return null;
		}
		return checkType;
	}

	@Override
	public void getKeyFinish(String key, int keyStart, int keyEnd) {
		// TODO Auto-generated method stub
		this.key = key;
	}

	@Override
	public Object getValueFinish(String valueString, XMJsonValueType valueType, int valueStart, int valueEnd) {
		// TODO Auto-generated method stub
		if (jsonObject == null) {
			if (isArray) {
				jsonObject = new ArrayList<>();
			} else {
				jsonObject = new HashMap<>();
			}
		}
		putKeyValue(key, valueString, jsonObject, valueType);
		return jsonObject;
	}

	@Override
	public XMJsonCheckType finishCovert() {
		// TODO Auto-generated method stub
		if (isArray) {
			return XMJsonCheckType.ValueStart;
		} else {
			return XMJsonCheckType.KeyStart;
		}
	}

	@Override
	public boolean isReturnObject() {
		// TODO Auto-generated method stub
		return isReturn;
	}
	
	/**
	 * 填充值到jsonObject中
	 * 
	 * @param key
	 *            键
	 * @param valueString
	 *            值字符串
	 * @param jsonObject
	 *            jsonObject
	 * @param valueType
	 *            值类型
	 */
	@SuppressWarnings("unchecked")
	public static void putKeyValue(String key, String valueString, Object jsonObject, XMJsonValueType valueType) {
		boolean isMap = jsonObject instanceof Map;
		Object value = null;
		switch (valueType) {
		case Map:
			value = XMJson.getJsonObject(valueString, null, null);
			break;
		case Array:
			value = XMJson.getJsonObject(valueString, null, null);
			break;
		case String:
			value = XMJsonUtil.getContent(valueString);
			break;
		case Number:
			value = Double.parseDouble(valueString);
			break;
		case Boolean:
			value = valueString.equals("true") ? true : false;
		default:
			break;
		}
		if (isMap) {
			((Map<String, Object>) jsonObject).put(key, value);
		} else {
			((List<Object>) jsonObject).add(value);
		}
	}
}
