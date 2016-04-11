package xmextension.xmjson.xmclass;

import xmextension.xmjson.xmenum.XMJsonKeyType;

/**
 * json¼üÀà
 * 
 * @author Ñ¦Ã×Ñù
 *
 */
public class XMJsonKey {

	public XMJsonKeyType type;
	public String name;
	public String keyPath;
	public boolean isRange = false;
	public int index1, index2;

	public XMJsonKey(String keyPath) {
		// TODO Auto-generated constructor stub
		keyPath = keyPath.trim();
		int index = keyPath.indexOf('.');
		if (index != -1) {
			name = keyPath.substring(0, index);
			this.keyPath = keyPath.substring(index + 1);
		} else {
			name = keyPath;
			keyPath = null;
		}
		type = getJsonKeyType(name);
		if (type == XMJsonKeyType.Array) {
			name = XMJsonUtil.getContent(name);
			try {
				if (name.indexOf('-') != -1) {
					isRange = true;
					String[] cmps = name.split("-");
					if (cmps.length != 2) {
						type = XMJsonKeyType.Error;
					} else {
						index1 = Integer.parseInt(cmps[0]);
						if (cmps[1].equals("*")) {
							name = String.valueOf(Integer.MAX_VALUE);
						}else {
							index2 = Integer.parseInt(cmps[1]);
							name = String.valueOf(index1 + index2 - 1);
						}
					}
				} else {
					Integer.parseInt(name);
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				type = XMJsonKeyType.Error;
			}
		}
	}

	public boolean isSame(String key) {
		if (type == XMJsonKeyType.Array) {
			int keyIndex = Integer.parseInt(key);
			int nameIndex = Integer.parseInt(name);
			if (isRange) {
				return (index1 <= keyIndex && keyIndex <= nameIndex);
			}
			return keyIndex == nameIndex;
		}
		return key.compareTo(name) == 0;
	}

	private XMJsonKeyType getJsonKeyType(String name) {
		char start = name.charAt(0);
		char end = name.charAt(name.length() - 1);
		if (start == '[' && end == ']') {
			return XMJsonKeyType.Array;
		} 
		return XMJsonKeyType.Map;
	}
	
}
