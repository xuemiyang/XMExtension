package xmextension.xmjson.xmclass.xmenumerate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xmextension.xmjson.xmclass.XMJson;

/**
 * 获取json对象通过keyPath的代理类
 * 
 * @author 薛米样
 *
 */
public class XMGetJsonObjectByKeyPath extends XMDealWithCasesDelegate {

	@Override
	public void dealWithCase1() {
		// TODO Auto-generated method stub
		Object obj = getJsonObject(context.jsonKey.keyPath);
		addObjToArray(obj, context.jsonObject);
	}

	@Override
	public void dealWithCase2() {
		// TODO Auto-generated method stub
		getJsonObject();
	}

	@Override
	public void dealWithCase3() {
		// TODO Auto-generated method stub
		Object obj = getJsonObject(context.keyPath);
		addObjToArray(obj, context.jsonObject);
	}

	@Override
	public void dealWithCase4() {
		// TODO Auto-generated method stub
		if (context.isArray) {
			context.jsonObject = new ArrayList<>();
		} else {
			context.jsonObject = new HashMap<>();
		}
		getJsonObject();
	}

	@Override
	public void dealWithCase5() {
		// TODO Auto-generated method stub
		context.jsonObject = getJsonObject(context.jsonKey.keyPath);
	}

	private Object getJsonObject(String keyPath) {
		Object obj;
		if (context.jsonCondition == null) {
			obj = XMJson.getJsonObject(context.valueString, keyPath, null);
		} else {
			obj = XMJson.getJsonObject(context.valueString, keyPath, context.jsonCondition);
		}
		return obj;
	}

	private void getJsonObject() {
		if (context.jsonCondition == null) {
			XMGetJsonObject.putKeyValue(context.key, context.valueString, context.jsonObject, context.valueType);
		} else {
			if (context.jsonCondition.calculate()) {
				XMGetJsonObject.putKeyValue(context.key, context.valueString, context.jsonObject, context.valueType);
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void addObjToArray(Object obj, Object jsonObject) {
		if (obj instanceof List) {
			List<Object> list = (List<Object>) obj;
			((List<Object>) jsonObject).addAll(list);
		} else if (obj != null) {
			if (obj instanceof Map) {// 获取键下面的值
				Map<String, Object> map = (Map<String, Object>) obj;
				((List<Object>) jsonObject).addAll(map.values());
			} else {
				((List<Object>) jsonObject).add(obj);
			}
		}
	}
}
