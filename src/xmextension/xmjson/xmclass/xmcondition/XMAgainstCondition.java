package xmextension.xmjson.xmclass.xmcondition;

/**
 * �߼�������
 * @author Ѧ����
 *
 */
public class XMAgainstCondition extends XMLogicCondition{

	@Override
	protected boolean innerCalculate() {
		// TODO Auto-generated method stub
		return !condition1.calculate();
	}

}
