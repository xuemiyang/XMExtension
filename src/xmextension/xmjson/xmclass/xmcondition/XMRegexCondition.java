package xmextension.xmjson.xmclass.xmcondition;

/**
 * ������ʽ����
 * @author Ѧ����
 *
 */
public class XMRegexCondition extends XMValueCondition{

	@Override
	protected boolean innerCalculate() {
		// TODO Auto-generated method stub
		if (!super.innerCalculate()) {
			return false;
		}
		return value1.matches(value2);
	}
	
}
