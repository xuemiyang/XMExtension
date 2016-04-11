package xmextension.xmjson.xminterface;

import xmextension.xmjson.xmenum.XMJsonCheckType;
import xmextension.xmjson.xmenum.XMJsonValueType;

/**
 * ����json�Ĵ�����ӿ�
 * 
 * @author Ѧ����
 *
 */
public interface XMEnumerateJsonDelegate {
	/**
	 * ��ʼת��
	 * 
	 * @param jsonString
	 *            json�ַ���
	 * @return json�������
	 */
	public XMJsonCheckType beginConvert(String jsonString);

	/**
	 * ��ȡ������
	 * 
	 * @param key
	 *            ��
	 */
	public void getKeyFinish(String key, int keyStart, int keyEnd);

	/**
	 * ��ȡֵ����
	 * 
	 * @param valueString
	 *            ֵ�ַ���
	 * @param valueType
	 *            ֵ����
	 * @return jsonObject
	 */
	public Object getValueFinish(String valueString, XMJsonValueType valueType, int valueStart, int valueEnd);

	/**
	 * �Ƿ񷵻�ֵ����������
	 * 
	 * @return boolean
	 */
	public boolean isReturnObject();

	/**
	 * ��ɼ�ֵת��
	 * 
	 * @return ��һ��json�������
	 */
	public XMJsonCheckType finishCovert();
}
