package xmextension.xmjson.xmclass.xmenumerate;

import java.util.ArrayList;

import xmextension.xmjson.xmclass.XMJsonConstant;
import xmextension.xmjson.xmclass.XMJsonKey;
import xmextension.xmjson.xmclass.XMJsonUtil;
import xmextension.xmjson.xmclass.xmcondition.XMBasicCondition;
import xmextension.xmjson.xmclass.xmcondition.XMConditionFactory;
import xmextension.xmjson.xmenum.XMEnumerateJsonType;
import xmextension.xmjson.xmenum.XMJsonCheckType;
import xmextension.xmjson.xmenum.XMJsonKeyType;
import xmextension.xmjson.xmenum.XMJsonValueType;
import xmextension.xmjson.xminterface.XMCondition;
import xmextension.xmjson.xminterface.XMEnumerateJsonByKeyPathDelegate;
import xmextension.xmjson.xminterface.XMEnumerateJsonDelegate;

/**
 * 遍历json字符串通过键路径代理类
 * 
 * @author 薛米样
 *
 */
public class XMEnumerateJsonByKeyPath implements XMEnumerateJsonDelegate{
	public boolean isKeyPathWrong, isFindKey, isArray, isRerun;
	public XMJsonKey jsonKey;
	public int arrayIndex = -1;
	public Object jsonObject;
	public String key, keyPath;
	public int keyStart, keyEnd;
	public String valueString;
	public XMJsonValueType valueType;
	public String jsonString;
	public int length;
	public int off;
	public int valueStart, valueEnd;
	public String jsonValue;
	public XMBasicCondition jsonCondition;
	private XMEnumerateJsonByKeyPathDelegate delegate;

	public XMEnumerateJsonByKeyPath(String jsonString, String keyPath, String jsonValue,
			XMEnumerateJsonByKeyPathDelegate casesDelegate, Object condition) {
		// TODO Auto-generated constructor stub
		this.delegate = casesDelegate;
		this.jsonString = jsonString.trim();
		length = jsonString.trim().length();
		this.keyPath = keyPath.trim();
		jsonKey = new XMJsonKey(keyPath.trim());
		if (jsonKey.type == XMJsonKeyType.Error) {
			isKeyPathWrong = true;
		}
		if (jsonValue != null) {
			this.jsonValue = jsonValue.trim();
		}
		if (condition != null) {
			if (condition instanceof String) {
				this.jsonCondition = XMConditionFactory.getCompositeCondition(jsonString, (String) condition);
			} else if (XMCondition.class.isAssignableFrom(condition.getClass())) {
				this.jsonCondition = (XMBasicCondition) condition;
			}
		}
	}

	@Override
	public XMJsonCheckType beginConvert(String jsonString) {
		// TODO Auto-generated method stub
		if (isKeyPathWrong) {
			XMJsonUtil.log(XMJsonConstant.KEYPATHSTRINGERROR);
			return null;
		}
		XMJsonValueType valueType = XMJsonUtil.getJsonType(jsonString);
		XMJsonCheckType checkType;
		if (valueType == XMJsonValueType.Map) {
			isArray = false;
			checkType = XMJsonCheckType.KeyStart;
			if (jsonKey.type != XMJsonKeyType.Map) {
				XMJsonUtil.log(XMJsonConstant.KEYPATHSTRINGERROR+", 当前键类型不是Map");
				return null;
			}
		} else if (valueType == XMJsonValueType.Array) {
			isArray = true;
			checkType = XMJsonCheckType.ValueStart;
		} else {
			XMJsonUtil.log(XMJsonConstant.KEYPATHSTRINGERROR);
			return null;
		}
		return checkType;
	}

	@Override
	public void getKeyFinish(String key, int keyStart, int keyEnd) {
		// TODO Auto-generated method stub
		isFindKey = jsonKey.isSame(key);
		this.key = key;
		this.keyStart = keyStart;
		this.keyEnd = keyEnd;
	}

	@Override
	public Object getValueFinish(String valueString, XMJsonValueType valueType, int valueStart, int valueEnd) {
		// TODO Auto-generated method stub
		this.valueString = valueString;
		this.valueType = valueType;
		this.valueStart = valueStart;
		this.valueEnd = valueEnd;
		if (isArray) {
			arrayIndex++;
			if (jsonKey.type == XMJsonKeyType.Map) {
				isFindKey = true;
			} else {
				isFindKey = jsonKey.isSame(arrayIndex + "");
			}
		}
		if (isFindKey) {// 容器里有值
			setConditionPath();
			if (isArray && (jsonKey.type != XMJsonKeyType.Array || jsonKey.isRange)) {
				if (jsonCondition != null) {
					jsonCondition.clearCache();
				}
				if (jsonObject == null) {
					jsonObject = new ArrayList<>();
				}
				isRerun = false;
				if (jsonKey.isRange) {
					if (jsonKey.keyPath != null) {
						delegate.getJsonObject(this, XMEnumerateJsonType.ArrayJson, XMEnumerateJsonType.isRange,
								XMEnumerateJsonType.KeyPathNotEnd);
					} else {
						delegate.getJsonObject(this, XMEnumerateJsonType.ArrayJson, XMEnumerateJsonType.isRange);
					}
					int index = Integer.parseInt(jsonKey.name);
					if (arrayIndex == index) {
						isRerun = true;
					}
				} else {
					delegate.getJsonObject(this, XMEnumerateJsonType.ArrayJson);
				}
				off = jsonString.length() - length;
				return jsonObject;
			}
			isRerun = true;
			if (jsonKey.keyPath == null) {// 最后一个键
				delegate.getJsonObject(this, XMEnumerateJsonType.MapJson, XMEnumerateJsonType.KeyPathNotEnd);
			} else {
				delegate.getJsonObject(this, XMEnumerateJsonType.MapJson);
			}
			return jsonObject;
		}
		isRerun = false;
		return null;
	}
	
	private void setConditionPath() {
		if (jsonCondition != null) {
			if (key != null) {
				XMBasicCondition.cmps.add(key);
			}else {
				if (arrayIndex != -1) {
					XMBasicCondition.cmps.add("["+arrayIndex+"]");
				}
			}
		}
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
		if (isRerun && jsonCondition != null && XMBasicCondition.cmps.size() > 0) {
			String last = XMBasicCondition.cmps.get(XMBasicCondition.cmps.size() - 1);
			XMBasicCondition.cmps.remove(XMBasicCondition.cmps.size() - 1);
			if (XMBasicCondition.cmps.size() > 0) {
				String last2 = XMBasicCondition.cmps.get(XMBasicCondition.cmps.size() - 1);
				if (!last.contains("[") && last2.contains("[")) {
					XMBasicCondition.cmps.remove(XMBasicCondition.cmps.size() - 1);
				}
			}
		}
		return isRerun;
	}


}
