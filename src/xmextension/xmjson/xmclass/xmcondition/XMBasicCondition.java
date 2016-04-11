package xmextension.xmjson.xmclass.xmcondition;

import java.util.LinkedList;
import java.util.List;

import xmextension.xmjson.xminterface.XMCondition;

public abstract class XMBasicCondition implements XMCondition {

	private Boolean cacheResult;
	
	/**
	 * 经历的路径
	 */
	static public List<String>cmps = new LinkedList<String>();
	
	public XMBasicCondition() {
		// TODO Auto-generated constructor stub
		cmps.clear();
	}

	protected abstract boolean innerCalculate();
	
	@Override
	public boolean calculate() {
		// TODO Auto-generated method stub
		if (cacheResult != null) {
			return cacheResult;
		}
		cacheResult = innerCalculate();
		return cacheResult;
	}

	@Override
	public void clearCache() {
		// TODO Auto-generated method stub
		cacheResult = null;
	}

}
