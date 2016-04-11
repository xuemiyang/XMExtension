package xmextension.xmjson.xmclass.xmcondition;

import java.util.List;
import java.util.Map;

import xmextension.xmjson.xmclass.XMJson;
import xmextension.xmjson.xmclass.XMJsonUtil;
import xmextension.xmjson.xmenum.XMConditionFunctionType;

/**
 * 值条件的抽象父类
 * 
 * @author 薛米样
 *
 */
public abstract class XMValueCondition extends XMBasicCondition {
	protected String value1, value2, jsonString;
	private Object obj;
	private int index = -1;
	private String key;
	private String[] values;
	private String lastPath;

	public void setValue(String jsonString, String... values) {
		// TODO Auto-generated method stub
		this.jsonString = jsonString;
		this.values = values;
	}

	@SuppressWarnings("unchecked")
	private String getNewValue(String value) {
		XMConditionFunctionType type = getFunctionType(value);
		if (type != XMConditionFunctionType.Error) {
			value = getFunctionContent(value);
		}
		if (isKey(value)) {
			value = XMJsonUtil.getContent(value);
			if (obj == null) {
				int index = value.lastIndexOf('.');
				key = value.substring(index + 1);
				obj = XMJson.getJsonObject(jsonString, value, null);
			}
			if (obj instanceof Map) {
				Map<String, Object> map = (Map<String, Object>) obj;
				if (map.containsKey(key)) {
					if (map.get(key) == null) {
						if (type == XMConditionFunctionType.Count) {
							return "1";
						}
						return "null";
					}
					return map.get(key).toString();
				} else {
					return null;
				}
			} else if (obj instanceof List) {
				List<Object> list = (List<Object>) obj;
				if (type == XMConditionFunctionType.Count) {
					return list.size() + "";
				} else if (type == XMConditionFunctionType.Max || type == XMConditionFunctionType.Min) {
					Object result = list.get(0);
					for (int i = 1; i < list.size(); i++) {
						result = getMaxMin(result, list.get(i), type);
					}
					return result.toString();
				}else if (type == XMConditionFunctionType.Avg) {
					Object result = list.get(0);
					if (result instanceof Double) {
						Double d = (double) 0;
						for (int i = 0; i < list.size(); i++) {
							d += (Double) list.get(i);
						}
						d = d / list.size();
						return d.toString();
					}else {
						return list.get(list.size() / 2).toString();
					}
				}else {
					if (isAdd(value)) {
						index++;
					}
					if (index >= list.size()) {
						index = 0;
					}
					if (list.get(index) == null) {
						return "null";
					}
					if (index != -1) {
						return list.get(index).toString();
					}
					return list.get(0).toString();
				}
			}
			return null;
		} else {
			if (type == XMConditionFunctionType.Count) {
				return "1";
			}
			return value;
		}
	}

	@Override
	protected boolean innerCalculate() {
		// TODO Auto-generated method stub
		getValues();
		if (value1 == null || value2 == null) {
			return false;
		}
		return true;
	}

	private void getValues() {
		if (values.length > 0) {
			value1 = getNewValue(values[0]);
			if (values.length > 1) {
				value2 = getNewValue(values[1]);
			}
		}
	}

	private boolean isKey(String value) {
		char start = value.charAt(0);
		char end = value.charAt(value.length() - 1);
		if (start == '"' && end == '"') {
			return true;
		}
		return false;
	}

	private XMConditionFunctionType getFunctionType(String value) {
		int start = value.indexOf('(');
		if (start == -1) {
			return XMConditionFunctionType.Error;
		}
		String string = value.substring(0, start);
		if (string.equals("max") && value.charAt(value.length() - 1) == ')') {
			return XMConditionFunctionType.Max;
		} else if (string.equals("min") && value.charAt(value.length() - 1) == ')') {
			return XMConditionFunctionType.Min;
		} else if (string.equals("count") && value.charAt(value.length() - 1) == ')') {
			return XMConditionFunctionType.Count;
		}else if (string.equals("avg") && value.charAt(value.length() - 1) == ')') {
			return XMConditionFunctionType.Avg;
		} else {
			return XMConditionFunctionType.Error;
		}
	}

	private String getFunctionContent(String value) {
		int start = value.indexOf('(');
		return value.substring(start + 1, value.length() - 1);
	}

	private Object getMaxMin(Object value1, Object value2, XMConditionFunctionType type) {
		if (value1 instanceof Double && value2 instanceof Double) {
			Double d1 = (Double) value1;
			Double d2 = (Double) value2;
			if ((d1 > d2 && type == XMConditionFunctionType.Max) || (d1 < d2 && type == XMConditionFunctionType.Min)) {
				return d1;
			} else {
				return d2;
			}
		} else {
			String s1 = value1.toString();
			String s2 = value2.toString();
			if ((s1.compareTo(s2) > 0 && type == XMConditionFunctionType.Max)
					|| (s1.compareTo(s2) < 0 && type == XMConditionFunctionType.Min)) {
				return s1;
			} else {
				return s2;
			}
		}
	}
	
	private boolean isAdd(String value) {
		String[] valueCmps = value.split("\\.");
		if (cmps.size() <= valueCmps.length) {
			return true;
		}
		int i,j;
		for (i = 0, j = i; i < valueCmps.length - 1; i++, j++) {
			if (j >= cmps.size()) {// cmp 短
				return true;
			}
			String cmp = cmps.get(j);
			String valueCmp = valueCmps[i];
			if (cmp.contains("[") && !valueCmp.contains("[")) {
				i--;
				continue;
			}
			if (!cmp.equals(valueCmp)) {
				return true;
			}
		}
		if (!cmps.get(j).equals(lastPath)) {
			lastPath = cmps.get(j);
			return true;
		}
		return false;
	}
	
}
