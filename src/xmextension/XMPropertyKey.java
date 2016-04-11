package xmextension;

import java.util.List;
import java.util.Map;

enum XMPropertyKeyType{
	Dictionary,	// �ֵ�
	Array 		// ����
}

/**
 * ���Ե�key�����ֵ��ȡֵkey��
 * @author Ѧ����
 *
 */
public class XMPropertyKey {
	/** ��������*/
	public String name;
	/** key�������������ͣ��������ֵ䣨map��������*/
	public XMPropertyKeyType type;
	
	public XMPropertyKey() {
		// TODO Auto-generated constructor stub
		type = XMPropertyKeyType.Dictionary;
	}
	
	/**
	 * ��������ȡֵ
	 * @param obj �����������ֵ䣨map��������
	 * @return Object ֵ
	 */
	public Object valueInObject(Object obj) {
		if (obj instanceof List && type == XMPropertyKeyType.Array) {
			@SuppressWarnings("unchecked")
			List<Object>array =  (List<Object>) obj;
			int index = Integer.parseInt(name);
			return array.get(index);
		}else if (obj instanceof Map && type == XMPropertyKeyType.Dictionary) {
			@SuppressWarnings("unchecked")
			Map<String, Object>map = (Map<String, Object>) obj;
			return map.get(name);
		}
		return null;
	}

	
}
