package xmextension.xmjson.xmclass.xmenumerate;

import java.util.ArrayList;
import java.util.List;

import xmextension.xmjson.xmclass.XMJsonConstant;
import xmextension.xmjson.xmclass.XMJsonUtil;
import xmextension.xmjson.xmenum.XMJsonCheckType;
import xmextension.xmjson.xmenum.XMJsonType;
import xmextension.xmjson.xmenum.XMJsonValueType;
import xmextension.xmjson.xminterface.XMEnumerateJsonDelegate;

/**
 * 获取json键值对的代理类
 * @author 薛米样
 *
 */
public class XMGetJsonKeyValue implements XMEnumerateJsonDelegate {

	private List<String> list;
	private XMJsonType type;
	private String jsonString;
	private int keyStart, start;
	private XMJsonCheckType checkType;

	public XMGetJsonKeyValue(XMJsonType type) {
		// TODO Auto-generated constructor stub
		list = new ArrayList<String>();
		this.type = type;
	}

	@Override
	public XMJsonCheckType beginConvert(String jsonString) {
		// TODO Auto-generated method stub
		this.jsonString = XMJsonUtil.getContent(jsonString);
		checkType = getCheckType(type, 0);
		return checkType;
	}

	@Override
	public void getKeyFinish(String key, int keyStart, int keyEnd) {
		// TODO Auto-generated method stub
		this.keyStart = keyStart - 1;
	}

	@Override
	public Object getValueFinish(String valueString, XMJsonValueType valueType, int valueStart, int valueEnd) {
		// TODO Auto-generated method stub
		if (checkType == XMJsonCheckType.KeyStart) {
			valueString = jsonString.substring(keyStart, valueEnd);
			start = valueEnd + 1;
		}
		list.add(valueString);
		return list;
	}

	@Override
	public boolean isReturnObject() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public XMJsonCheckType finishCovert() {
		// TODO Auto-generated method stub
		if (start < jsonString.length()) {
			checkType = getCheckType(type, start);
		}
		return checkType;
	}

	private XMJsonCheckType getCheckType(XMJsonType type, int start) {
		if (type != XMJsonType.Add && type != XMJsonType.SetValue) {
			XMJsonUtil.log(XMJsonConstant.GETJSONKEYTYPEERROR + ", type:" + type);
			return null;
		}
		if (type == XMJsonType.Add) {
			char ch = jsonString.charAt(start);
			while (ch == ' ') {
				start++;
				ch = jsonString.charAt(start);
			}
			if (ch == '"' && jsonString.indexOf(':') != -1) {
				return XMJsonCheckType.KeyStart;
			}
		}
		return XMJsonCheckType.ValueStart;
	}
}
