package test;

import java.io.Serializable;
import java.net.URL;

public class XMObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String name;
	public int age;
	public double money;
	
	public URL url;
	public boolean isXM;
	
	public XMObject() {
		// TODO Auto-generated constructor stub
	}
	
	public static void showFather() {
		System.out.println("���Ǹ���ľ�̬����");
	}

	@Override
	public String toString() {
		return "XMObject [name=" + name + ", age=" + age + ", money=" + money + ", url=" + url + ", isXM=" + isXM + "]";
	}
	
	

	
	
	

}
