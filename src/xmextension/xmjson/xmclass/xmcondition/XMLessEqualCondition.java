package xmextension.xmjson.xmclass.xmcondition;

/**
 * 值小于等于条件
 * @author 薛米样
 *
 */
public class XMLessEqualCondition extends XMValueCondition{

	@Override
	protected boolean innerCalculate() {
		// TODO Auto-generated method stub
		if (!super.innerCalculate()) {
			return false;
		}
		try {
			Double d1 = Double.parseDouble(value1);
			Double d2 = Double.parseDouble(value2);
			return d1 <= d2;
		} catch (Exception e) {
			// TODO: handle exception
			return value1.compareTo(value2) <= 0;
		}
	}

}
