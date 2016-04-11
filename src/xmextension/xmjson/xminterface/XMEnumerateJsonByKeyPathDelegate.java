package xmextension.xmjson.xminterface;

import xmextension.xmjson.xmenum.XMEnumerateJsonType;

/**
 * 遍历json通过键路径的代理类接口
 * @author 薛米样
 *
 */
public interface XMEnumerateJsonByKeyPathDelegate {
	
	/**
	 * 获取json对象
	 * @param context 上下文
	 * @param types 遍历json通过键路径遇到的情况类型
	 */
	public void getJsonObject(Object context , XMEnumerateJsonType...types);
}
