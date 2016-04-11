package xmextension.xmjson.xmclass.xmcondition;

import xmextension.xmjson.xmenum.XMConditionType;

/**
 * 条件工厂类
 * @author 薛米样
 *
 */
public class XMConditionFactory {
	
	private XMConditionFactory() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 获取值条件
	 * @param conditionType 条件类型
	 * @return 值条件
	 */
	public static XMValueCondition getValueCondition(XMConditionType conditionType) {
		switch (conditionType) {
		case Equal:
			return new XMEqualCondition();
		case NotEqual:
			return new XMNotEqualCondition();
		case Greater:
			return new XMGreaterCondition(); 
		case GreaterEqual:
			return new XMGreaterEqualCondition();
		case Less:
			return new XMLessCondition();
		case lessEqual:
			return new XMLessEqualCondition(); 
		case Regex:
			return new XMRegexCondition(); 
		default:
			return null;
		}
	}
	
	/**
	 * 获取逻辑条件
	 * @param conditionType 条件类型
	 * @return 逻辑条件
	 */
	public static XMLogicCondition getLogicCondition(XMConditionType conditionType) {
		switch (conditionType) {
		case Against:
			return new XMAgainstCondition();
		case And:
			return new XMAndCondition();
		case Or:
			return new XMOrCondition();
		default:
			return null;
		}
	}
	
	/**
	 * 获取复合条件
	 * @param jsonString json字符串
	 * @param condition 条件
	 * @return 复合条件
	 */
	public static XMCompositeCondition getCompositeCondition(String jsonString, String condition) {
		return new XMCompositeCondition(jsonString, condition); 
	}

}
