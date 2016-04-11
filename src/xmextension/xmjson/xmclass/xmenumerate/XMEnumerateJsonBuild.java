package xmextension.xmjson.xmclass.xmenumerate;

import xmextension.xmjson.xmenum.XMJsonType;
import xmextension.xmjson.xminterface.XMEnumerateJsonByKeyPathDelegate;
import xmextension.xmjson.xminterface.XMEnumerateJsonDelegate;

/**
 * �������json������Ľ�����
 * 
 * @author Ѧ����
 *
 */
public class XMEnumerateJsonBuild {

	private XMEnumerateJsonDelegate delegate;
	private XMEnumerateJsonByKeyPathDelegate casesDelegate;
	private String jsonString;
	private String keyPath;
	private String jsonValue;
	private Object condition;

	private XMEnumerateJsonBuild() {
		// TODO Auto-generated constructor stub
	}

	private static class XMEnumerateJsonBuildFactory {
		private static XMEnumerateJsonBuild instance = new XMEnumerateJsonBuild();
	}

	public static XMEnumerateJsonBuild getInstance() {
		return XMEnumerateJsonBuildFactory.instance;
	}

	/**
	 * ��ȡ����json�Ĵ�����
	 * 
	 * @param type
	 *            ����json�ķ�ʽ
	 * @return XMEnumerateJsonDelegate
	 */
	public XMEnumerateJsonDelegate getEnumerateJsonDelegate(XMJsonType type) {
		switch (type) {
		case Add:
			if (jsonString != null && jsonValue != null) {
				if (keyPath == null) {
					delegate = new XMAddJsonString(jsonString, jsonValue);
				} else {
					casesDelegate = XMEnumerateJsonByKeyPathFactory.getEnumerateJsonByKeyPathDelegate(type);
					if (condition != null) {
						delegate = new XMEnumerateJsonByKeyPath(jsonString, keyPath, jsonValue, casesDelegate,
								condition);
					} else {
						delegate = new XMEnumerateJsonByKeyPath(jsonString, keyPath, jsonValue, casesDelegate, null);
					}
				}
			} else {
				delegate = null;
			}
			break;
		case Delete:
			if (jsonString != null && keyPath != null) {
				casesDelegate = XMEnumerateJsonByKeyPathFactory.getEnumerateJsonByKeyPathDelegate(type);
				if (condition != null) {
					delegate = new XMEnumerateJsonByKeyPath(jsonString, keyPath, null, casesDelegate, condition);
				} else {
					delegate = new XMEnumerateJsonByKeyPath(jsonString, keyPath, null, casesDelegate, null);
				}
			} else {
				delegate = null;
			}
			break;
		case GetValue:
			if (jsonString != null) {
				if (keyPath == null) {
					delegate = new XMGetJsonObject();
				} else {
					casesDelegate = XMEnumerateJsonByKeyPathFactory.getEnumerateJsonByKeyPathDelegate(type);
					if (condition != null) {
						delegate = new XMEnumerateJsonByKeyPath(jsonString, keyPath, null, casesDelegate, condition);
					} else {
						delegate = new XMEnumerateJsonByKeyPath(jsonString, keyPath, null, casesDelegate, null);
					}
				}
			} else {
				delegate = null;
			}
			break;
		case SetValue:
			if (jsonString != null && keyPath != null && jsonValue != null) {
				casesDelegate = XMEnumerateJsonByKeyPathFactory.getEnumerateJsonByKeyPathDelegate(type);
				if (condition != null) {
					delegate = new XMEnumerateJsonByKeyPath(jsonString, keyPath, jsonValue, casesDelegate, condition);
				} else {
					delegate = new XMEnumerateJsonByKeyPath(jsonString, keyPath, jsonValue, casesDelegate, null);
				}
			} else {
				delegate = null;
			}
			break;
		case SetKey:
			if (jsonString != null && keyPath != null && jsonValue != null) {
				casesDelegate = XMEnumerateJsonByKeyPathFactory.getEnumerateJsonByKeyPathDelegate(type);
				if (condition != null) {
					delegate = new XMEnumerateJsonByKeyPath(jsonString, keyPath, jsonValue, casesDelegate, condition);
				} else {
					delegate = new XMEnumerateJsonByKeyPath(jsonString, keyPath, jsonValue, casesDelegate, null);
				}
			} else {
				delegate = null;
			}
			break;
		default:
			delegate = null;
			break;
		}
		clear();
		return delegate;
	}

	/**
	 * ��������
	 * 
	 * @param condition
	 *            ����
	 * @return ������
	 */
	public XMEnumerateJsonBuild setCondition(Object condition) {
		if (condition instanceof String) {
			String conditionS = (String) condition;
			int lastIndex = keyPath.lastIndexOf('.');
			if (keyPath.charAt(lastIndex+1) == '[') {
				condition = conditionS.replace(keyPath.subSequence(0, lastIndex), keyPath);
			}
		}
		this.condition = condition;
		return this;
	}

	/**
	 * ���ô���
	 * 
	 * @param delegate
	 *            ����
	 * @return ������
	 */
	public XMEnumerateJsonBuild setDelegate(XMEnumerateJsonDelegate delegate) {
		this.delegate = delegate;
		return this;
	}

	/**
	 * ����json�ַ���
	 * 
	 * @param jsonString
	 *            json�ַ���
	 * @return ������
	 */
	public XMEnumerateJsonBuild setJsonString(String jsonString) {
		this.jsonString = jsonString;
		return this;
	}

	/**
	 * ����jsonֵ
	 * 
	 * @param jsonValue
	 *            jsonֵ
	 * @return ������
	 */
	public XMEnumerateJsonBuild setJsonValue(String jsonValue) {
		this.jsonValue = jsonValue;
		return this;
	}

	/**
	 * ���ü�·��
	 * 
	 * @param keyPath
	 *            ��·��
	 * @return ������
	 */
	public XMEnumerateJsonBuild setKeyPath(String keyPath) {
		this.keyPath = keyPath;
		return this;
	}

	private void clear() {
		jsonString = null;
		keyPath = null;
		jsonValue = null;
		condition = null;
		casesDelegate = null;
	}
}
