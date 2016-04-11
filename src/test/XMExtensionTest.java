package test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import xmextension.Object_XMClass;
import xmextension.Object_XMClass.XMAllowedPropertyNames;
import xmextension.Object_XMClass.XMIgnoredPropertyNames;
import xmextension.Object_XMMap;
import xmextension.Object_XMMap.XMObjectDidFinishConvertingToMap;
import xmextension.Object_XMProperty;
import xmextension.Object_XMProperty.XMReplacedKeyFromPropertyName;
import xmextension.XMDictionaryCache;
import xmextension.xmjson.xmclass.XMJson;
import xmextension.xmjson.xmclass.xmcondition.XMConditionFactory;
import xmextension.xmjson.xminterface.XMCondition;

public class XMExtensionTest {

	@SuppressWarnings("unchecked")
	@Test
	public void test() {
		XMObject xmObject = new XMObject();
		xmObject.name = "jack";
		xmObject.age = 24;
		xmObject.money = 999.96;
		try {
			xmObject.url = new URL("http://www.baidu.com");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xmObject.isXM = true;
		System.out.println("\\n\252\322\273\260\u0192\243\u2013\325->Map--------------------------------\\n");
		Map<String, Object> map = (Map<String, Object>) Object_XMMap.setObject(xmObject)
				.getMapByObject(new XMObjectDidFinishConvertingToMap() {

					@Override
					public void objectDidFinishConvertingToMap() {
						// TODO Auto-generated method stub
						System.out.println("father finish Converting");
					}
				});

		System.out.println(map);

		System.out.println("\\n\u2206\u2019\325\256\277\u2021\u2013\325->Map---------------------------------\\n");
		Double double1 = 12.9;
		Object object = Object_XMMap.setObject(double1).getMapByObject(null);
		System.out.println(object);

		System.out.println("\\nMap->Map--------------------------------------\\n");
		object = Object_XMMap.setObject(map).getMapByObject(null);
		System.out.println(object);

		System.out.println("\\n\u0192\243\u2013\325\u25ca\u201d\277\u2021->Map----------------------------------\\n");
		XMObjectSub xmObjectSub = new XMObjectSub();
		xmObjectSub.name = "rose";
		xmObjectSub.age = 25;
		xmObjectSub.money = 10059.5;
		xmObjectSub.isXM = false;
		xmObjectSub.isSub = true;
		xmObjectSub.height = 180.6f;
		XMObjectOther objectOther = new XMObjectOther();
		objectOther.name = "otherObject";
		objectOther.age = 54;
		XMObjectOther2 objectOther2 = new XMObjectOther2();
		objectOther2.name = "otherObject2";
		objectOther2.age = 108;
		objectOther.other = objectOther2;
		xmObjectSub.other = objectOther;
		map = (Map<String, Object>) Object_XMMap.setObject(xmObjectSub).getMapByObject(null);
		System.out.println(map);

		System.out.println("\\n\u0192\243\u2013\325\243\256\u221e\270\u222b\250\u0192\243\u2013\325\240\u02dd\u25ca\310\243\251->Map----------------------------------\\n");
		XMObjectArray objectArray = new XMObjectArray();
		objectArray.name = "objectArray";
		List<XMObjectSub> xmObjectSubs = new ArrayList<XMObjectSub>();
		for (int i = 0; i < 3; i++) {
			xmObjectSubs.add(xmObjectSub.deepClone());
		}
		objectArray.xmObjectSubs = xmObjectSubs;
		map = (Map<String, Object>) Object_XMMap.setObject(objectArray).getMapByObject(null);
		System.out.println(map);

		System.out.println("\\n\u0192\243\u2013\325\240\u02dd\u25ca\310->Map\240\u02dd\u25ca\310----------------------------------\\n");
		List<Object> objectList = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			objectList.add(xmObject);
		}
		List<Object> list = Object_XMMap.setClass(XMObject.class).getMapArrayByClass(objectList);
		System.out.println(list);

		System.out.println("\\n\u0192\243\u2013\325\240\u02dd\u25ca\310->Map\240\u02dd\u25ca\310\243\256\u2018\240\u2013\314\u03a9\257\u2013\u2013\u0192\243\u2013\325\u25ca\367\265\u2030\u25ca\u2122\252\252\243\251----------------------------------\\n");
		List<String> allowedKeys = new ArrayList<>();
		allowedKeys.add("name");
		allowedKeys.add("age");
		allowedKeys.add("money");
		list = Object_XMMap.setClass(XMObject.class).getMapArrayByClassWithAllowedKeys(objectList, allowedKeys);
		System.out.println(list);

		System.out.println("\\n\u0192\243\u2013\325\240\u02dd\u25ca\310->Map\240\u02dd\u25ca\310\243\256\u222b\u02c6\254\u2018\u03a9\257\u2013\u2013\u0192\243\u2013\325\u25ca\367\265\u2030\u25ca\u2122\252\252\243\251----------------------------------\\n");
		List<String> ignoredKeys = new ArrayList<>();
		ignoredKeys.add("url");
		list = Object_XMMap.setClass(XMObject.class).getMapArrayByClassWithIgnoredKeys(objectList, ignoredKeys);
		System.out.println(list);

		System.out.println("\\n\u0192\243\u2013\325->Map\243\256\u2018\240\u2013\314\u03a9\257\u2013\u2013\u0192\243\u2013\325\u25ca\367\265\u2030\u25ca\u2122\252\252\243\251----------------------------------\\n");
		map = (Map<String, Object>) Object_XMMap.setObject(xmObject).getMapByObjectWithAllowedKeys(allowedKeys, null);
		System.out.println(map);

		System.out.println("\\n\u0192\243\u2013\325->Map\243\256\u222b\u02c6\254\u2018\u03a9\257\u2013\u2013\u0192\243\u2013\325\u25ca\367\265\u2030\u25ca\u2122\252\252\243\251----------------------------------\\n");
		map = (Map<String, Object>) Object_XMMap.setObject(xmObject).getMapByObjectWithIgnoredKeys(ignoredKeys, null);
		System.out.println(map);

		System.out.println("\\n\u2026\313\367\u221aclass\265\u0192\u2018\240\u2013\314\u03a9\257\u2013\u2013\u0192\243\u2013\325\u25ca\367\265\u2030\u25ca\u2122\252\252Key----------------------------------\\n");
		Object_XMClass.setClass(XMObject.class).setupAllowedPropertyNames(new XMAllowedPropertyNames() {

			@Override
			public List<String> getAllowedPropertyNames() {
				// TODO Auto-generated method stub
				allowedKeys.clear();
				allowedKeys.add("name");
				allowedKeys.add("age");
				return allowedKeys;
			}
		});
		map = (Map<String, Object>) Object_XMMap.setObject(xmObject).getMapByObject(null);
		System.out.println(map);

		Object_XMClass.setClass(XMObject.class).setupAllowedPropertyNames(new XMAllowedPropertyNames() {

			@Override
			public List<String> getAllowedPropertyNames() {
				// TODO Auto-generated method stub
				return null;
			}
		});

		System.out.println("\\n\u2026\313\367\u221aclass\265\u0192\u222b\u02c6\254\u2018\u03a9\257\u2013\u2013\u0192\243\u2013\325\u25ca\367\265\u2030\u25ca\u2122\252\252Key----------------------------------\\n");
		Object_XMClass.setClass(XMObject.class).setupIgnoredPropertyNames(new XMIgnoredPropertyNames() {

			@Override
			public List<String> getIgnoredPropertyNames() {
				// TODO Auto-generated method stub
				ignoredKeys.clear();
				ignoredKeys.add("url");
				ignoredKeys.add("isXM");
				return ignoredKeys;
			}
		});
		map = (Map<String, Object>) Object_XMMap.setObject(xmObject).getMapByObject(null);
		System.out.println(map);
		
		Object_XMClass.setClass(XMObject.class).setupIgnoredPropertyNames(new XMIgnoredPropertyNames() {

			@Override
			public List<String> getIgnoredPropertyNames() {
				// TODO Auto-generated method stub
				return null;
			}
		});

		System.out.println("\\nMap->\u0192\243\u2013\325----------------------------------\\n");
		map.clear();
		map.put("name", "xuemiyang");
		map.put("age", 22);
		map.put("money", 100.5);
		map.put("url", "http://www.xuemiyang.com");
		map.put("isXM", "yes");
		xmObject = (XMObject) Object_XMMap.setClass(XMObject.class).getInstanceByClass(map, null);
		System.out.println(xmObject.toString());

		System.out.println("\\n\u2026\313\367\u221a\303\312\252\252\265\u0192Key----------------------------------\\n");
		Object_XMProperty.setClass(XMObjectOther.class)
				.setupReplacedKeyFromPropertyName(new XMReplacedKeyFromPropertyName() {

					@Override
					public String getReplacedKey(String propertyName) {
						// TODO Auto-generated method stub
						if (propertyName.equals("name")) {
							return "otherName";
						} else if (propertyName.equals("age")) {
							return "otherAge";
						} else if (propertyName.equals("other")) {
							return "other2";
						}
						return null;
					}
				});
		Object_XMProperty.setClass(XMObjectOther2.class)
				.setupReplacedKeyFromPropertyName(new XMReplacedKeyFromPropertyName() {

					@Override
					public String getReplacedKey(String propertyName) {
						// TODO Auto-generated method stub
						if (propertyName.equals("name")) {
							return "otherName2";
						} else if (propertyName.equals("age")) {
							return "otherAge2";
						}
						return null;
					}
				});

		map.put("isSub", "no");
		map.put("height", "174.6");
		Map<String, Object> otherMap = new HashMap<String, Object>();
		otherMap.put("otherName", "xm");
		otherMap.put("otherAge", "2");
		Map<String, Object> otherMap2 = new HashMap<>();
		otherMap2.put("otherName2", "water");
		otherMap2.put("otherAge2", 1);
		otherMap.put("other2", otherMap2);
		map.put("other", otherMap);
		XMDictionaryCache.setObject(XMObjectOther.class, null, Object_XMProperty.XMCacheProperties);
		XMDictionaryCache.setObject(XMObjectOther2.class, null, Object_XMProperty.XMCacheProperties);
		xmObjectSub = (XMObjectSub) Object_XMMap.setClass(XMObjectSub.class).getInstanceByClass(map, null);
		System.out.println(xmObjectSub.toString());

		System.out.println("\\nlist->\u0192\243\u2013\325\240\u02dd\u25ca\310----------------------------------\\n");
		map.clear();
		map.put("name", "xuemiyang");
		map.put("age", 22);
		map.put("money", 100.5);
		map.put("url", "http://www.xuemiyang.com");
		map.put("isXM", "yes");
		list.clear();
		for (int i = 0; i < 3; i++) {
			list.add(map);
		}
		objectList = Object_XMMap.setClass(XMObject.class).getObjectArrayByClass(list);
		System.out.println(objectList);
		
		System.out.println("\\n\u0192\243\u2013\325->jsonString----------------------------------\\n");
		String jsonString = Object_XMMap.setObject(xmObject).getJsonStringByObject();
		System.out.println(jsonString);
		
		System.out.println("\\njsonString->jsonObejct----------------------------------\\n");
		jsonString = "{\\"name\\":\\"xuemiyang\\", \\"message\\":\\"i like xmextension\\"}";
		map = (Map<String, Object>) Object_XMMap.setObject(jsonString).getJsonObjectByObject();
		System.out.println(map);
		
		System.out.println("\\nxmjson\u03a9\310\u2026\u2039----------------------------------------------------\\n");
		System.out.println("\\n\272\332\265\u2022\265\u0192json\u25ca\u2122\252\252----------------------------------------------------\\n");
		jsonString = "{\\"name\\":\\"xuemiyang\\", \\"age\\":22, \\"height\\":174.5, \\"isMan\\":true, \\"url\\":null}";
		map = (Map<String, Object>) XMJson.getJsonObject(jsonString, null, null);
		System.out.println(map);
		jsonString = "[{\\"name\\":\\"jack\\", \\"age\\":22}, {\\"name\\":\\"rose\\",\\"age\\":21}]";
		list = (List<Object>) XMJson.getJsonObject(jsonString, null, null);
		System.out.println(list);
		
		map.clear();
		map.put("name", "xm");
		map.put("age", 22);
		map.put("money", 19.5);
		map.put("isMan", true);
		jsonString = XMJson.getJsonString(map);
		System.out.println(jsonString);
		
		list.clear();
		list.add(map);
		list.add(map);
		jsonString = XMJson.getJsonString(list);
		System.out.println(jsonString);
		
		System.out.println("\\n\252\322\273\260jsonString\367\u2013\265\u0192\u0192\u2265\u220f\u02c6\254\u2211\346\u2202\265\u0192\367\265----------------------------------------------------\\n");
		jsonString = "{\\"name\\":\\"xuemiyang\\", \\"age\\":22, \\"money\\":100.6, \\"isMan\\":true}";
		object = XMJson.getJsonObject(jsonString, "name", null);
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "isMan", null);
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "money", null);
		System.out.println(object);
		
		System.out.println("\\n\252\322\273\260\272\270\254\u2211\346\u2202\265\u0192\367\265----------------------------------------------------\\n");
		jsonString = "{\\"name\\":\\"xuemiyang\\", \\"age\\":22, \\"numArray\\":[1,2,3,4,5], \\"mapArray\\":[{\\"name\\":\\"jack\\", \\"isMan\\":true}, {\\"name\\":\\"rose\\", \\"isMan\\":false}, {\\"name\\":\\"tom\\", \\"isMan\\":true}]}";
		object = XMJson.getJsonObject(jsonString, "numArray.[2]", null);
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "mapArray.[1]", null);
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "mapArray.[0].name", null);
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "mapArray.[1].isMan", null);
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "mapArray.name", null);
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "mapArray.isMan", null);
		System.out.println(object);
		
		System.out.println("\\n\252\322\273\260\u2211\367\u201c\u2265\240\u02dd\346\u203a----------------------------------------------------\\n");
		object = XMJson.getJsonObject(jsonString, "numArray.[1-3]", null);
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "mapArray.[1-2]", null);
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "mapArray.[1-2].name", null);
		System.out.println(object);
		
		System.out.println("\\n\252\322\273\260\u2202\u2021\303\u0131\272\270\254\u2211\346\u2202\265\u0192\367\265----------------------------------------------------\\n");
		object = XMJson.getJsonObject(jsonString, "numArray.[1-3], mapArray.[1-2], mapArray.[1-2].name", null);
		System.out.println(object);
		
		System.out.println("\\n\252\322\273\260\272\270\254\u2211\346\u2202\265\u0192\367\265\325\256\u03c0\u02dd\303\u0131\272\u02db----------------------------------------------------\\n");
		jsonString = "{\\"array\\":[{\\"name\\":\\"jack\\", \\"age\\":22, \\"money\\":200.4}, {\\"name\\":\\"rose\\", \\"age\\":20, \\"money\\":1056.2}, {\\"name\\":\\"tom\\", \\"age\\":25, \\"money\\":20053.1}]}";
		object = XMJson.getJsonObject(jsonString, "array.name", "\\"array.age\\" == 20");
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "array.name", "\\"array.age\\" >= 22");
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "array.name", "\\"array.age\\" != 22");
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "array.name", "\\"array.age\\" >= 20 && \\"array.money\\" < 2000");
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "array.name", "!(\\"array.age\\" >= 20 && \\"array.money\\" < 2000)");
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "array.name", "min(\\"array.money\\") == \\"array.money\\"");
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "array.name", "max(\\"array.age\\") == \\"array.age\\"");
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "array.name", "avg(\\"array.money\\") >= \\"array.money\\"");
		System.out.println(object);
		
		XMCondition condition = XMConditionFactory.getCompositeCondition(jsonString, "\\"array.age\\" == 20");
		object = XMJson.getJsonObject(jsonString, "array.name", condition);
		System.out.println(object);
		
		System.out.println("\\nxmcondition\265\u0192\u03a9\310\u2026\u2039----------------------------------------------------\\n");
		condition = XMConditionFactory.getCompositeCondition(null, "1 < 0 || (!(5 > 6) && 6 > 5)");
		System.out.println(condition.calculate());
		condition = XMConditionFactory.getCompositeCondition(null, "11155566 ? ^1{3}5{3}6{2}");
		System.out.println(condition.calculate());
		
		System.out.println("\\n\u0152\u2122jsonString\u2026\313\367\u221a\367\265----------------------------------------------------\\n");
		jsonString = XMJson.setJsonValue(jsonString, "array.money", "300", "\\"array.name\\" == rose");
		System.out.println(jsonString);
		jsonString = XMJson.setJsonValue(jsonString, "array.money", "900", "\\"array.money\\" == min(\\"array.money\\")");
		System.out.println(jsonString);
		jsonString = XMJson.setJsonValue(jsonString, "array.[1-2].money", "110", null);
		System.out.println(jsonString);
		jsonString = XMJson.setJsonValue(jsonString, "array.age", "18", null);
		System.out.println(jsonString);
		jsonString = XMJson.setJsonValue(jsonString, "array.age, array.money", "20, 600.5", null);
		System.out.println(jsonString);
		jsonString = XMJson.setJsonValue(jsonString, "array.age", "null", null);
		System.out.println(jsonString);
		
		System.out.println("\\n\u0152\u2122jsonString\u2026\313\367\u221a\272\270----------------------------------------------------\\n");
		jsonString = XMJson.setJsonKey(jsonString, "array", "peoples", null);
		System.out.println(jsonString);
		jsonString = XMJson.setJsonKey(jsonString, "peoples.name, peoples.money", "nickName, property", null);
		System.out.println(jsonString);
		
		System.out.println("\\n\u0152\u2122jsonString\303\314\272\u201djsonString----------------------------------------------------\\n");
		jsonString = XMJson.addJsonString(jsonString, null, "\\"dogs\\":[{\\"name\\":\\"do\\"}], \\"nums\\":[1,2,3]", null);
		System.out.println(jsonString);
		jsonString = XMJson.addJsonString(jsonString, "peoples, dogs, nums", "\\"donging\\":{\\"type\\":\\"play ball\\"}, \\"isMale\\":true, 22", null);
		System.out.println(jsonString);
		jsonString = XMJson.addJsonString(jsonString, "peoples.donging", "\\"count\\":2", null);
		System.out.println(jsonString);
		
		System.out.println("\\n\u0152\u2122jsonString\u2026\346\u2265\u02ddjsonString----------------------------------------------------\\n");
		jsonString = XMJson.deleteJsonString(jsonString, "peoples.donging.count, dogs, nums", null);
		System.out.println(jsonString);
		jsonString = XMJson.deleteJsonString(jsonString, "peoples.age", "\\"peoples.age\\" == null");
		System.out.println(jsonString);
		
		System.out.println("\\n\325\256\u03c0\u02ddjson\277\245\245\245\u03a9\256\240\u02dd\346\u203a\370\u201a----------------------------------------------------\\n");
		String database = XMJson.createTable(null, "goods", "{\\"type\\":\\"\240\u02dd\254\316\u2264\u02d9\u2206\u2211\\", \\"subGoods\\":[{\\"name\\":\\"\u2013\260\u221a\u25ca3\\", \\"price\\":999.9}]}");
		System.out.println(database);
		database = XMJson.createTable(database, "houses", "{\\"name\\":\\"\272\332\254\u2122\u0152\u203a\u25ca\u201d\\", \\"address\\":\\"\u03c0\u201e\u0152\u02dc\u03c0\uf8ff\u2206\u03a9\240\u2013xx\u2019\332xx\u03a9\367xxx\u222b\u2248\\", \\"size\\":89}");
		System.out.println(database);
		
		System.out.println("\\n\u2264\302\273\316\240\u02dd\346\u203a----------------------------------------------------\\n");
		database = XMJson.insertKeyValues(database, "goods", "{\\"type\\":\\"\300\306\u03c0\u02da\\", \\"subGoods\\":[{\\"name\\":\\"\u0153\u201e\u03a9\u2202\\", \\"price\\":1.5}]}",null);
		System.out.println(database);
		database = XMJson.insertKeyValues(database, "goods.subGoods", "{\\"name\\":\\"\252\u2122\u0152\u2122\273\u0178\u201c\2643C\\", \\"price\\":555}", "\\"type\\" == \240\u02dd\254\316\u2264\u02d9\u2206\u2211");
		System.out.println(database);
		database = XMJson.insertKeyValues(database, "goods.subGoods", "{\\"name\\":\\"iphone6\\", \\"price\\":4800}", "\\"type\\" == \240\u02dd\254\316\u2264\u02d9\u2206\u2211");
		System.out.println(database);
		database = XMJson.insertKeyValues(database, "goods.subGoods", "{\\"name\\":\\"\u02dc\273\u25ca\302\\", \\"price\\":1700}", "\\"type\\" == \240\u02dd\254\316\u2264\u02d9\u2206\u2211");
		System.out.println(database);
		database = XMJson.insertKeyValues(database, "houses", "{\\"name\\":\\"\252\u2122\277\u02c6\u0152\u203a\u25ca\u201d\\", \\"address\\":\\"\u03c0\u201e\367\u203a\240\u2013\u222b\243\367\310\253\257xx\u03a9\367xx\u222b\u2248\\", \\"size\\":200}", null);
		System.out.println(database);
		
		System.out.println("\\n\u2026\346\u2265\u02dd\240\u02dd\346\u203a----------------------------------------------------\\n");
		database = XMJson.deleteValues(database, "houses.[0-*]", "\\"name\\" == \272\332\254\u2122\u0152\u203a\u25ca\u201d");
		System.out.println(database);
		database = XMJson.deleteValues(database, "goods.subGoods.[0-*]", "\\"type\\" == \240\u02dd\254\316\u2264\u02d9\u2206\u2211  && \\"subGoods.name\\" == \u2013\260\u221a\u25ca3");
		System.out.println(database);
		database = XMJson.deleteValues(database, "houses", null);
		System.out.println(database);
		
		System.out.println("\\n\u220f\270\u2013\254\367\265----------------------------------------------------\\n");
		database = XMJson.updateValues(database, "goods", "subGoods.price, subGoods.name", "890, \\"\252\u2122\u0152\u2122\273\u0178\u201c\264\253\u2021\245\u222b\u221e\312\\"", "\\"subGoods.name\\" == \252\u2122\u0152\u2122\273\u0178\u201c\2643C");
		System.out.println(database);
		database = XMJson.updateValues(database, "goods.subGoods", "price, name", "1650, \\"\u02dc\273\u25ca\3022\\"", "\\"subGoods.name\\" == \u02dc\273\u25ca\302");
		System.out.println(database);
		
		System.out.println("\\n\u220f\270\u2013\254\272\270----------------------------------------------------\\n");
		database = XMJson.updateKeys(database, "goods", "type", "goodType", null);
		System.out.println(database);
		
		System.out.println("\\n\u2264\310\u2014\330\367\265----------------------------------------------------\\n");
		object = XMJson.selectJsonObject(database, "goods", null);
		System.out.println(object);
		object = XMJson.selectJsonObject(database, "goods", "\\"goods.goodType\\" == \240\u02dd\254\316\u2264\u02d9\u2206\u2211");
		System.out.println(object);
		object = XMJson.selectJsonObject(database, "goods.subGoods.name", "\\"goods.goodType\\" == \240\u02dd\254\316\u2264\u02d9\u2206\u2211");
		System.out.println(object);
		object = XMJson.selectJsonObject(database, "goods.subGoods.[1-2]", "\\"goods.goodType\\" == \240\u02dd\254\316\u2264\u02d9\u2206\u2211");
		System.out.println(object);
		object = XMJson.selectJsonObject(database, "goods.subGoods.[1-2].name", "\\"goods.goodType\\" == \240\u02dd\254\316\u2264\u02d9\u2206\u2211");
		System.out.println(object);
		object = XMJson.selectJsonObject(database, "goods.subGoods.[1-2].name, goods.subGoods.[1-2].price", "\\"goods.goodType\\" == \240\u02dd\254\316\u2264\u02d9\u2206\u2211");
		System.out.println(object);
	}

}
