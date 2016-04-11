package xmextension.xmjson.xmclass.xmenumerate;

import xmextension.xmjson.xmenum.XMEnumerateJsonType;
import xmextension.xmjson.xminterface.XMEnumerateJsonByKeyPathDelegate;

/**
 * �������json�ַ���ͨ����·��������������ĳ�����
 * 
 * @author Ѧ����
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
	 * ������arrayJson�У�keyΪisRang��keyPath��Ϊnull�����
	 */
	public abstract void dealWithCase1();

	/**
	 * ������arrayJson�У�keyΪisRang�����
	 */
	public abstract void dealWithCase2();

	/**
	 * ������arrayJson�е����
	 */
	public abstract void dealWithCase3();

	/**
	 * ������mapJson�У�keyPath��Ϊnull�����
	 */
	public abstract void dealWithCase4();

	/**
	 * ������mapJson�е����
	 */
	public abstract void dealWithCase5();

}
