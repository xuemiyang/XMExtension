package xmextension.xmjson.xminterface;

import xmextension.xmjson.xmenum.XMEnumerateJsonType;

/**
 * ����jsonͨ����·���Ĵ�����ӿ�
 * @author Ѧ����
 *
 */
public interface XMEnumerateJsonByKeyPathDelegate {
	
	/**
	 * ��ȡjson����
	 * @param context ������
	 * @param types ����jsonͨ����·���������������
	 */
	public void getJsonObject(Object context , XMEnumerateJsonType...types);
}
