package xmextension.xmjson.xmclass.xmcondition;

import xmextension.xmjson.xmenum.XMConditionType;

/**
 * ����������
 * @author Ѧ����
 *
 */
public class XMConditionFactory {
	
	private XMConditionFactory() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * ��ȡֵ����
	 * @param conditionType ��������
	 * @return ֵ����
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
	 * ��ȡ�߼�����
	 * @param conditionType ��������
	 * @return �߼�����
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
	 * ��ȡ��������
	 * @param jsonString json�ַ���
	 * @param condition ����
	 * @return ��������
	 */
	public static XMCompositeCondition getCompositeCondition(String jsonString, String condition) {
		return new XMCompositeCondition(jsonString, condition); 
	}

}
