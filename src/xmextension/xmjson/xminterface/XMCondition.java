package xmextension.xmjson.xminterface;

/**
 * 条件接口
 * 
 * @author 薛米样
 *
 */
public interface XMCondition {
	
	/**
	 * 计算
	 * @return boolean
	 */
	public boolean calculate();
	
	
	/**
	 * 清除计算结果
	 */
	public void clearCache();
}

