package xmextension.xmjson.xmclass.xmenumerate;

import xmextension.xmjson.xmclass.XMJson;

/**
 * 删除json字符串的代理类
 * 
 * @author 薛米样
 *
 */
public class XMDeleteJsonString extends XMDealWithCasesDelegate {

	@Override
	public void dealWithCase1() {
		// TODO Auto-generated method stub
		deleteJsonString(context.jsonKey.keyPath, context.off);
	}

	@Override
	public void dealWithCase2() {
		// TODO Auto-generated method stub
		deleteJsonString(context.off);
	}

	@Override
	public void dealWithCase3() {
		// TODO Auto-generated method stub
		deleteJsonString(context.keyPath, context.off);
	}

	@Override
	public void dealWithCase4() {
		// TODO Auto-generated method stub
		deleteJsonString(0);
	}

	@Override
	public void dealWithCase5() {
		// TODO Auto-generated method stub
		deleteJsonString(context.jsonKey.keyPath, 0);
	}

	private void deleteJsonString(String keyPath, int off) {
		if (context.jsonCondition == null) {
			context.jsonObject = XMJson.deleteJsonString(context.valueString, keyPath, null);
		} else {
			context.jsonObject = XMJson.deleteJsonString(context.valueString, keyPath, context.jsonCondition);
		}
		if (context.jsonObject != null) {
			context.jsonString = setSingleValue(context.jsonString, context.valueStart + off, context.valueEnd + off,
					context.jsonObject);
		}
		context.jsonObject = context.jsonString;
	}

	private void deleteJsonString(int off) {
		if (context.jsonCondition == null) {
			context.jsonString = deleteString(context.jsonString, context.keyStart + off, context.valueStart + off,
					context.valueEnd + off);
		} else {
			if (context.jsonCondition.calculate()) {
				context.jsonString = deleteString(context.jsonString, context.keyStart + off, context.valueStart + off,
						context.valueEnd + off);
			}
		}
		context.jsonObject = context.jsonString;
	}

	protected String setSingleValue(String jsonString, int valueStart, int valueEnd, Object jsonObject) {
		if (jsonObject.equals("")) {
			if (context.isArray) {
				return deleteString(jsonString, valueStart, valueStart, valueEnd);
			}
			jsonObject = "null";
		}
		return jsonString.substring(0, valueStart + 1) + jsonObject + jsonString.substring(valueEnd + 1);
	}

	protected String deleteString(String jsonString, int keyStart, int valueStart, int valueEnd) {
		if (jsonString.charAt(valueEnd + 1) == ',') {
			if (context.isArray) {
				jsonString = jsonString.substring(0, valueStart + 1) + jsonString.substring(valueEnd + 3);
			} else {
				jsonString = jsonString.substring(0, keyStart) + jsonString.substring(valueEnd + 3);
			}
		} else {
			if (context.isArray) {
				if (valueStart < 1) {
					jsonString = "";
				} else {
					jsonString = jsonString.substring(0, valueStart - 1) + jsonString.substring(valueEnd + 1);
				}
			} else {
				if (keyStart < 2) {
					jsonString = "";
				} else {
					jsonString = jsonString.substring(0, keyStart - 2) + jsonString.substring(valueEnd + 1);
				}
			}
		}
		return jsonString;
	}

}
