package test;

import java.io.Serializable;

public class XMObjectOther implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String name;
	
	public int age;
	
	public XMObjectOther2 other;
	
	public XMObjectOther() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "XMObjectOther [name=" + name + ", age=" + age + ", other=" + other + "]";
	}
	

	
	
	

}
