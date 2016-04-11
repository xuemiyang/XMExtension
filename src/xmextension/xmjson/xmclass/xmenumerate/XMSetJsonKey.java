package xmextension.xmjson.xmclass.xmenumerate;

import xmextension.xmjson.xmclass.XMJson;
import xmextension.xmjson.xmclass.XMJsonConstant;
import xmextension.xmjson.xmclass.XMJsonUtil;

/**
 * 设置json键的代理类
 * 
 * @author 薛米样
 *
 */
public class XMSetJsonKey extends XMDealWithCasesDelegate {

	@Override
	public void dealWithCase1() {
		// TODO Auto-generated method stub
		setjsonkey(context.jsonKey.keyPath, context.off);
	}

	@Override
	public void dealWithCase2() {
		// TODO Auto-generated method stub
		setJsonKey(context.off);
	}

	@Override
	public void dealWithCase3() {
		// TODO Auto-generated method stub
		setjsonkey(context.keyPath, context.off);
	}

	@Override
	public void dealWithCase4() {
		// TODO Auto-generated method stub
		setJsonKey(0);
	}

	@Override
	public void dealWithCase5() {
		// TODO Auto-generated method stub
		setjsonkey(context.jsonKey.keyPath, 0);
	}

	private void setjsonkey(String keyPath, int off) {
		if (context.jsonCondition == null) {
			context.jsonObject = XMJson.setJsonKey(context.valueString, keyPath, context.jsonValue, null);
		} else {
			context.jsonObject = XMJson.setJsonKey(context.valueString, keyPath, context.jsonValue,
					context.jsonCondition);
		}
		if (context.jsonObject != null) {
			context.jsonString = XMSetJsonValue.setSingleValue(context.jsonString, context.valueStart + off,
					context.valueEnd + off, context.jsonObject);
		}
		context.jsonObject = context.jsonString;
	}

	private void setJsonKey(int off) {
		if (!context.key.equals(context.jsonValue)) {
			if (context.jsonCondition == null) {
				context.jsonString = XMSetJsonValue.setSingleValue(context.jsonString, context.keyStart + off,
						context.keyEnd + off, context.jsonValue);
			} else {
				if (context.jsonCondition.calculate()) {
					context.jsonString = XMSetJsonValue.setSingleValue(context.jsonString, context.keyStart + off,
							context.keyEnd + off, context.jsonValue);
				}
			}
			context.jsonObject = context.jsonString;
		} else {
			XMJsonUtil.log(XMJsonConstant.JSONKEYERROR + ", key:" + context.jsonValue);
			context.jsonObject = null;
		}
	}
}
