package xmextension;

import java.util.List;
import java.util.Map;

enum XMPropertyKeyType{
	Dictionary,	// 字典
	Array 		// 数组
}

/**
 * 属性的key（在字典的取值key）
 * @author 薛米样
 *
 */
public class XMPropertyKey {
	/** 键的名称*/
	public String name;
	/** key所在容器的类型，可能是字典（map）、数组*/
	public XMPropertyKeyType type;
	
	public XMPropertyKey() {
		// TODO Auto-generated constructor stub
		type = XMPropertyKeyType.Dictionary;
	}
	
	/**
	 * 从容器中取值
	 * @param obj 容器可以是字典（map）、数组
	 * @return Object 值
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
