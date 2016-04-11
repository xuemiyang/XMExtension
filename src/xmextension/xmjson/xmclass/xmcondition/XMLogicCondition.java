package xmextension.xmjson.xmclass.xmcondition;

import xmextension.xmjson.xminterface.XMCondition;

/**
 * 逻辑条件的抽象父类
 * 
 * @author 薛米样
 *
 */
public abstract class XMLogicCondition extends XMBasicCondition {

	protected XMCondition condition1, condition2;

	public void setConditions(XMCondition... conditions) {
		// TODO Auto-generated method stub
		if (conditions.length > 0) {
			condition1 = conditions[0];
			if (conditions.length > 1) {
				condition2 = conditions[1];
			}
		}
	}
	
	@Override
	public void clearCache() {
		// TODO Auto-generated method stub
		super.clearCache();
		if (condition1 != null) {
			condition1.clearCache();
		}
		if (condition2 != null) {
			condition2.clearCache();
		}
	}
	

}
