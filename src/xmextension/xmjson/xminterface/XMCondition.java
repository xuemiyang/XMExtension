package xmextension.xmjson.xminterface;

/**
 * �����ӿ�
 * 
 * @author Ѧ����
 *
 */
public interface XMCondition {
	
	/**
	 * ����
	 * @return boolean
	 */
	public boolean calculate();
	
	
	/**
	 * ���������
	 */
	public void clearCache();
}

