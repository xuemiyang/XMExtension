package xmextension.xmjson.xmclass.xmcondition;

/**
 * ֵ��������
 * @author Ѧ����
 *
 */
public class XMEqualCondition extends XMValueCondition{

	@Override
	protected boolean innerCalculate() {
		// TODO Auto-generated method stub
		if (!super.innerCalculate()) {
			return false;
		}
		try {
			Double d1 = Double.parseDouble(value1);
			Double d2 = Double.parseDouble(value2);
			return (d1 - d2) == 0.0;
		} catch (Exception e) {
			// TODO: handle exception
			return value1.equals(value2);
		}
	}

}
