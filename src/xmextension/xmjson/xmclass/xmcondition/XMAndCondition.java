package xmextension.xmjson.xmclass.xmcondition;

/**
 * �߼�������
 * @author Ѧ����
 *
 */
public class XMAndCondition extends XMLogicCondition{

	@Override
	protected boolean innerCalculate() {
		// TODO Auto-generated method stub
		boolean result1 = condition1.calculate();
		boolean result2 = condition2.calculate();
		return result1 && result2;
	}

}
