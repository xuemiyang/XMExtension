package xmextension.xmjson.xmclass.xmenumerate;

import xmextension.xmjson.xmenum.XMJsonType;
import xmextension.xmjson.xminterface.XMEnumerateJsonByKeyPathDelegate;

/**
 * 创建XMEnumerateJsonByKeyPathDelegate实例的工厂类
 * 
 * @author 薛米样
 *
 */
public class XMEnumerateJsonByKeyPathFactory {

	private XMEnumerateJsonByKeyPathFactory() {
		// TODO Auto-generated constructor stub
	}

	public static XMEnumerateJsonByKeyPathDelegate getEnumerateJsonByKeyPathDelegate(XMJsonType type) {
		XMEnumerateJsonByKeyPathDelegate delegate = null;
		switch (type) {
		case GetValue:
			delegate = new XMGetJsonObjectByKeyPath();
			break;
		case Delete:
			delegate = new XMDeleteJsonString();
			break;
		case Add:
			delegate = new XMAddJsonStringByKeyPath();
			break;
		case SetValue:
			delegate = new XMSetJsonValue();
			break;
		case SetKey:
			delegate = new XMSetJsonKey();
			break;
		default:
			break;
		}
		return delegate;
	}

}
