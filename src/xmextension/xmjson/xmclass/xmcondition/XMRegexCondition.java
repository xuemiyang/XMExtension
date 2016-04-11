package xmextension.xmjson.xmclass.xmcondition;

/**
 * 正则表达式条件
 * @author 薛米样
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
