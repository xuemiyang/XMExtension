package xmextension.xmjson.xmclass.xmcondition;

import java.util.HashSet;
import java.util.Set;

import xmextension.xmjson.xmenum.XMConditionStringType;
import xmextension.xmjson.xmenum.XMConditionType;
import xmextension.xmjson.xminterface.XMCondition;
import xmextension.xmjson.xminterface.XMConditionCallBack;

/**
 * 复合条件
 * 
 * @author 薛米样
 *
 */
public class XMCompositeCondition extends XMBasicCondition {
	private String jsonString, condition, value1 = null;
	private XMCondition finalCondition;
	private Set<String> valuesBridge, logicBridge;
	private int part = 0, i;
	private XMConditionType conditionType;
	private int valueStart = -1;
	private XMConditionStringType stringType;
	private XMValueCondition tempValueCondition;
	private XMLogicCondition tempLogicCondition;
	private boolean isConditionStart = true;

	public XMCompositeCondition(String jsonString, String condition) {
		// TODO Auto-generated constructor stub
		this.jsonString = jsonString;
		this.condition = condition.trim() + "||";
		buildFinalCondition();
	}

	private Set<String> getlogicBridge() {
		if (logicBridge == null) {
			logicBridge = new HashSet<>();
			logicBridge.add("&&");
			logicBridge.add("||");
		}
		return logicBridge;
	}

	private Set<String> getValuesBridge() {
		if (valuesBridge == null) {
			valuesBridge = new HashSet<>();
			valuesBridge.add("==");
			valuesBridge.add("!=");
			valuesBridge.add(">");
			valuesBridge.add(">=");
			valuesBridge.add("<");
			valuesBridge.add("<=");
			valuesBridge.add("?");
		}
		return valuesBridge;
	}

	private void buildFinalCondition() {
		char ch, nextCh;
		for (i = 0; i < condition.length() - 1; i++) {
			ch = condition.charAt(i);
			if (ch == ' ') {
				continue;
			}
			nextCh = condition.charAt(i + 1);
			if (isConditionStart) {
				isConditionStart = false;
				if (!getConditionInfo(ch, nextCh)) {
					return;
				}
				continue;
			}

			switch (stringType) {
			case Composite:
				getPart(ch, nextCh, new XMConditionCallBack() {
					@Override
					public void findPartFinish(String value) {
						// TODO Auto-generated method stub
						XMCompositeCondition compositeCondition = getCompositeCondition(value);
						setFinalCondition(compositeCondition);
					}
				});
				break;
			case Against:
				getPart(ch, nextCh, new XMConditionCallBack() {
					@Override
					public void findPartFinish(String value) {
						// TODO Auto-generated method stub
						XMCompositeCondition compositeCondition = getCompositeCondition(value);
						XMLogicCondition againstCondition = XMConditionFactory.getLogicCondition(conditionType);
						againstCondition.setConditions(compositeCondition);
						setFinalCondition(againstCondition);
					}
				});
				break;
			case Value:
				getPart(ch, nextCh, new XMConditionCallBack() {
					@Override
					public void findPartFinish(String value) {
						// TODO Auto-generated method stub
						value1 = value;
						i--;
					}
				});
				break;
			case logic:
				getPart(ch, nextCh, new XMConditionCallBack() {
					@Override
					public void findPartFinish(String value) {
						// TODO Auto-generated method stub
						tempValueCondition.setValue(jsonString, value1,value);
						setFinalCondition(tempValueCondition);
						i--;
					}
				});
				break;
			default:
				break;
			}
		}
	}

	private boolean getConditionInfo(char ch, char nextCh) {
		conditionType = getConditionType(ch, nextCh);
		switch (conditionType) {
		case Against:
			valueStart = i + 2;
			stringType = XMConditionStringType.Against;
			i += 2;
			break;
		case Composite:
			valueStart = i + 1;
			stringType = XMConditionStringType.Composite;
			i++;
			break;
		case Other:
			valueStart = i;
			stringType = XMConditionStringType.Value;
			i--;
			break;
		case And:
		case Or:
			valueStart = i + 2;
			stringType = XMConditionStringType.Value;
			tempLogicCondition = XMConditionFactory.getLogicCondition(conditionType);
			i += 2;
			isConditionStart = true;
			break;
		case Equal:
		case NotEqual:
		case GreaterEqual:
		case lessEqual:
			valueStart = i + 2;
			stringType = XMConditionStringType.logic;
			tempValueCondition = XMConditionFactory.getValueCondition(conditionType);
			i += 2;
			break;
		case Greater:
		case Less:
		case Regex:
			valueStart = i + 1;
			stringType = XMConditionStringType.logic;
			tempValueCondition = XMConditionFactory.getValueCondition(conditionType);
			i++;
			break;
		default:
			return false;
		}
		return true;
	}

	private XMCompositeCondition getCompositeCondition(String tempCondition) {
		XMCompositeCondition compositeCondition = new XMCompositeCondition(jsonString,tempCondition);
		return compositeCondition;
	}

	private void getPart(char ch, char nextCh, XMConditionCallBack callBack) {
		switch (stringType) {
		case Value:
			if (getValuesBridge().contains("" + ch + nextCh)) {
				part = 1;
			} else if (getValuesBridge().contains("" + ch)) {
				part = 1;
			} else {
				part = 0;
			}
			break;
		case logic:
			if (getlogicBridge().contains("" + ch + nextCh)) {
				part = 1;
			} else {
				part = 0;
			}
			break;
		case Composite:
			if (ch == '(') {
				part--;
			} else if (ch == ')') {
				part++;
			}
			break;
		case Against:
			if (ch == '(') {
				part--;
			} else if (ch == ')') {
				part++;
			}
			break;
		default:
			part = 0;
			break;
		}
		if (part == 1) {
			part = 0;
			int valueEnd = i;
			String value = condition.substring(valueStart, valueEnd).trim();
			isConditionStart = true;
			if (callBack != null) {
				callBack.findPartFinish(value);
			}
		}
	}

	private XMConditionType getConditionType(char ch, char nextCh) {
		if (ch == '!') {
			if (nextCh != '(' && nextCh != '=') {
				return XMConditionType.Error;
			}
		} else if (ch == '&') {
			if (nextCh != '&') {
				return XMConditionType.Error;
			}
		} else if (ch == '|') {
			if (nextCh != '|') {
				return XMConditionType.Error;
			}
		} else if (ch == '=') {
			if (nextCh != '=') {
				return XMConditionType.Error;
			}
		}

		switch ("" + ch + nextCh) {
		case "!(":
			return XMConditionType.Against;
		case "&&":
			return XMConditionType.And;
		case "||":
			return XMConditionType.Or;
		case ">=":
			return XMConditionType.GreaterEqual;
		case "<=":
			return XMConditionType.lessEqual;
		case "==":
			return XMConditionType.Equal;
		case "!=":
			return XMConditionType.NotEqual;
		}
		switch ("" + ch) {
		case ">":
			return XMConditionType.Greater;
		case "<":
			return XMConditionType.Less;
		case "?":
			return XMConditionType.Regex;
		case "(":
			return XMConditionType.Composite;
		default:
			return XMConditionType.Other;
		}
	}

	private void setFinalCondition(XMCondition condition) {
		if (finalCondition == null) {
			finalCondition = condition;
		} else {
			tempLogicCondition.setConditions(finalCondition, condition);
			finalCondition = tempLogicCondition;
		}
	}

	@Override
	public void clearCache() {
		// TODO Auto-generated method stub
		super.clearCache();
		finalCondition.clearCache();
	}

	@Override
	protected boolean innerCalculate() {
		// TODO Auto-generated method stub
		if (finalCondition == null) {
			return false;
		}
		return finalCondition.calculate();
	}
}
