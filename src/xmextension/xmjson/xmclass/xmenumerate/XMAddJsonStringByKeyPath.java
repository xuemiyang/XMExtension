package xmextension.xmjson.xmclass.xmenumerate;

import java.util.List;

import xmextension.xmjson.xmclass.XMJson;
import xmextension.xmjson.xmclass.XMJsonConstant;
import xmextension.xmjson.xmclass.XMJsonUtil;
import xmextension.xmjson.xmenum.XMJsonValueType;

/**
 * 添加json字符串通过keyPath的代理类
 * 
 * @author 薛米样
 *
 */
public class XMAddJsonStringByKeyPath extends XMDealWithCasesDelegate {

	@Override
	public void dealWithCase1() {
		// TODO Auto-generated method stub
		addJsonString(context.jsonKey.keyPath, context.off);
	}

	@Override
	public void dealWithCase2() {
		// TODO Auto-generated method stub
		if (checkType(context.valueString, context.valueType)) {
			addJsonValue(context.off);
		}
		context.jsonObject = context.jsonString;
	}

	@Override
	public void dealWithCase3() {
		// TODO Auto-generated method stub
		addJsonString(context.keyPath, context.off);
	}

	@Override
	public void dealWithCase4() {
		// TODO Auto-generated method stub
		XMJsonUtil.isLog = false;
		if (checkType(context.valueString, context.valueType)) {
			XMJsonUtil.isLog = true;
			addJsonValue(0);
		} else if (context.valueType == XMJsonValueType.Array) {
			XMJsonUtil.isLog = true;
			addJsonString(context.jsonKey.keyPath, 0);
		} else {
			XMJsonUtil.isLog = true;
			XMJsonUtil.log(XMJsonConstant.JSONVALUETYPEERROR + ", value:" + context.jsonValue);
			context.jsonObject = null;
		}
	}

	@Override
	public void dealWithCase5() {
		// TODO Auto-generated method stub
		addJsonString(context.jsonKey.keyPath, 0);
	}

	private void addJsonValue(int off) {
		if (context.jsonCondition == null) {
			context.valueString = XMAddJsonString.addJsonValue(context.valueString, context.jsonValue);
			context.jsonString = XMSetJsonValue.setSingleValue(context.jsonString, context.valueStart + off,
					context.valueEnd + off, context.valueString);
		} else {
			if (context.jsonCondition.calculate()) {
				context.valueString = XMAddJsonString.addJsonValue(context.valueString, context.jsonValue);
				context.jsonString = XMSetJsonValue.setSingleValue(context.jsonString, context.valueStart + off,
						context.valueEnd + off, context.valueString);
			}
		}
		context.jsonObject = context.jsonString;
	}

	private void addJsonString(String keyPath, int off) {
		if (context.jsonCondition == null) {
			context.jsonObject = XMJson.addJsonString(context.valueString, keyPath, context.jsonValue, null);
		} else {
			context.jsonObject = XMJson.addJsonString(context.valueString, keyPath, context.jsonValue,
					context.jsonCondition);
		}
		if (context.jsonObject != null) {
			context.jsonString = XMSetJsonValue.setSingleValue(context.jsonString, context.valueStart + off,
					context.valueEnd + off, context.jsonObject);
		}
		context.jsonObject = context.jsonString;
	}

	protected boolean checkType(String valueString, XMJsonValueType valueType) {
		String newJsonValue;
		if (valueType == XMJsonValueType.Map) {
			newJsonValue = "{" + context.jsonValue + "}";
		} else if (valueType == XMJsonValueType.Array) {
			newJsonValue = "[" + context.jsonValue + "]";
		} else {
			return false;
		}
		Object value = XMJson.getJsonObject(newJsonValue, null, null);
		Object jsonObject = XMJson.getJsonObject(valueString, null, null);
		if (value != null && jsonObject != null) {
			if (valueType == XMJsonValueType.Array) {
				@SuppressWarnings("unchecked")
				List<Object> list = (List<Object>) value;
				if (list.get(0) == null && !context.jsonValue.equals("null")) {
					return false;
				}
			}
			return XMAddJsonString.isAdd(jsonObject, value);
		}
		return false;
	}

}
