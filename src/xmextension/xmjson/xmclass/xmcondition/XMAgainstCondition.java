package xmextension.xmjson.xmclass.xmcondition;

/**
 * 逻辑非条件
 * @author 薛米样
 *
 */
public class XMAgainstCondition extends XMLogicCondition{

	@Override
	protected boolean innerCalculate() {
		// TODO Auto-generated method stub
		return !condition1.calculate();
	}

}
