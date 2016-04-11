package test;

import java.io.Serializable;

public class XMObjectOther2 implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String name;
	
	public int age;
	
	public XMObjectOther2() {
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public String toString() {
		return "XMObjectOther [name=" + name + ", age=" + age + "]";
	}
	
	

}
