package xmextension.xmjson.xmclass.xmenumerate;

import xmextension.xmjson.xmenum.XMEnumerateJsonType;
import xmextension.xmjson.xminterface.XMEnumerateJsonByKeyPathDelegate;

/**
 * 处理遍历json字符串通过键路径所遇到的情况的抽象类
 * 
 * @author 薛米样
 *
 */
public abstract class XMDealWithCasesDelegate implements XMEnumerateJsonByKeyPathDelegate {
	private XMEnumerateJsonType[] case1, case2, case3, case4, case5;
	protected XMEnumerateJsonByKeyPath context;

	public XMDealWithCasesDelegate() {
		// TODO Auto-generated constructor stub
		case1 = new XMEnumerateJsonType[] { XMEnumerateJsonType.ArrayJson, XMEnumerateJsonType.isRange,
				XMEnumerateJsonType.KeyPathNotEnd };
		case2 = new XMEnumerateJsonType[] { XMEnumerateJsonType.ArrayJson, XMEnumerateJsonType.isRange };
		case3 = new XMEnumerateJsonType[] { XMEnumerateJsonType.ArrayJson };
		case4 = new XMEnumerateJsonType[] { XMEnumerateJsonType.MapJson, XMEnumerateJsonType.KeyPathNotEnd };
		case5 = new XMEnumerateJsonType[] { XMEnumerateJsonType.MapJson };
	}

	@Override
	public void getJsonObject(Object context, XMEnumerateJsonType... types) {
		// TODO Auto-generated method stub
		this.context = (XMEnumerateJsonByKeyPath) context;
		if (isSame(types, case1)) {
			dealWithCase1();
		} else if (isSame(types, case2)) {
			dealWithCase2();
		} else if (isSame(types, case3)) {
			dealWithCase3();
		} else if (isSame(types, case4)) {
			dealWithCase4();
		} else if (isSame(types, case5)) {
			dealWithCase5();
		}
	}

	private boolean isSame(XMEnumerateJsonType[] types1, XMEnumerateJsonType[] types2) {
		if (types1.length != types2.length) {
			return false;
		}
		for (int i = 0; i < types2.length; i++) {
			if (types1[i] != types2[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 处理在arrayJson中，key为isRang，keyPath不为null的情况
	 */
	public abstract void dealWithCase1();

	/**
	 * 处理在arrayJson中，key为isRang的情况
	 */
	public abstract void dealWithCase2();

	/**
	 * 处理在arrayJson中的情况
	 */
	public abstract void dealWithCase3();

	/**
	 * 处理在mapJson中，keyPath不为null的情况
	 */
	public abstract void dealWithCase4();

	/**
	 * 处理在mapJson中的情况
	 */
	public abstract void dealWithCase5();

}
