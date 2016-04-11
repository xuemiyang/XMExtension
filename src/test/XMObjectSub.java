package test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class XMObjectSub extends XMObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isSub;

	public float height;
	
	public XMObjectOther other;
	
	public XMObjectSub() {
		// TODO Auto-generated constructor stub
	}
	
	public XMObjectSub deepClone() {
		XMObjectSub sub = null;
		ByteArrayOutputStream out = null;
		ObjectOutputStream outputStream = null;
		ObjectInputStream inputStream = null;
		ByteArrayInputStream in = null;
		try {
			out = new ByteArrayOutputStream();
			outputStream = new ObjectOutputStream(out);
			outputStream.writeObject(this);
			in = new ByteArrayInputStream(out.toByteArray());
			inputStream = new ObjectInputStream(in);
			sub = (XMObjectSub) inputStream.readObject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if (out!=null) {
					out.close();
				}
				if (outputStream!=null) {
					outputStream.close();
				}
				if (in!=null) {
					in.close();
				}
				if (inputStream!=null) {
					inputStream.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		return sub;
	}

	@Override
	public String toString() {
		return "XMObjectSub [isSub=" + isSub + ", height=" + height + ", other=" + other + "]";
	}


}
