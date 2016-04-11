package xmextension.xmjson.xminterface;

import xmextension.xmjson.xmenum.XMJsonCheckType;
import xmextension.xmjson.xmenum.XMJsonValueType;

/**
 * 遍历json的代理类接口
 * 
 * @author 薛米样
 *
 */
public interface XMEnumerateJsonDelegate {
	/**
	 * 开始转换
	 * 
	 * @param jsonString
	 *            json字符串
	 * @return json检查类型
	 */
	public XMJsonCheckType beginConvert(String jsonString);

	/**
	 * 获取键结束
	 * 
	 * @param key
	 *            键
	 */
	public void getKeyFinish(String key, int keyStart, int keyEnd);

	/**
	 * 获取值结束
	 * 
	 * @param valueString
	 *            值字符串
	 * @param valueType
	 *            值类型
	 * @return jsonObject
	 */
	public Object getValueFinish(String valueString, XMJsonValueType valueType, int valueStart, int valueEnd);

	/**
	 * 是否返回值，跳出遍历
	 * 
	 * @return boolean
	 */
	public boolean isReturnObject();

	/**
	 * 完成键值转换
	 * 
	 * @return 下一个json检查类型
	 */
	public XMJsonCheckType finishCovert();
}
