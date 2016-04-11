# XMExtension
一个轻量级的模型转Map，Map转模型，listMap转list模型，list模型转listMap，JSON解析成Map、list。可以对JSON进行数据库一样的操作（增删改查），可以建立以JSON字符串为内容的类似数据库的容器。

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
		System.out.println("\nªÒ»°ƒ£–Õ->Map--------------------------------\n");
		Map<String, Object> map = (Map<String, Object>) Object_XMMap.setObject(xmObject)
				.getMapByObject(new XMObjectDidFinishConvertingToMap() {

					@Override
					public void objectDidFinishConvertingToMap() {
						// TODO Auto-generated method stub
						System.out.println("father finish Converting");
					}
				});

		System.out.println(map);

		System.out.println("\n∆’Õ®¿‡–Õ->Map---------------------------------\n");
		Double double1 = 12.9;
		Object object = Object_XMMap.setObject(double1).getMapByObject(null);
		System.out.println(object);

		System.out.println("\nMap->Map--------------------------------------\n");
		object = Object_XMMap.setObject(map).getMapByObject(null);
		System.out.println(object);

		System.out.println("\nƒ£–Õ◊”¿‡->Map----------------------------------\n");
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

		System.out.println("\nƒ£–Õ£®∞¸∫¨ƒ£–Õ ˝◊È£©->Map----------------------------------\n");
		XMObjectArray objectArray = new XMObjectArray();
		objectArray.name = "objectArray";
		List<XMObjectSub> xmObjectSubs = new ArrayList<XMObjectSub>();
		for (int i = 0; i < 3; i++) {
			xmObjectSubs.add(xmObjectSub.deepClone());
		}
		objectArray.xmObjectSubs = xmObjectSubs;
		map = (Map<String, Object>) Object_XMMap.setObject(objectArray).getMapByObject(null);
		System.out.println(map);

		System.out.println("\nƒ£–Õ ˝◊È->Map ˝◊È----------------------------------\n");
		List<Object> objectList = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			objectList.add(xmObject);
		}
		List<Object> list = Object_XMMap.setClass(XMObject.class).getMapArrayByClass(objectList);
		System.out.println(list);

		System.out.println("\nƒ£–Õ ˝◊È->Map ˝◊È£®‘ –ÌΩ¯––ƒ£–Õ◊÷µ‰◊™ªª£©----------------------------------\n");
		List<String> allowedKeys = new ArrayList<>();
		allowedKeys.add("name");
		allowedKeys.add("age");
		allowedKeys.add("money");
		list = Object_XMMap.setClass(XMObject.class).getMapArrayByClassWithAllowedKeys(objectList, allowedKeys);
		System.out.println(list);

		System.out.println("\nƒ£–Õ ˝◊È->Map ˝◊È£®∫ˆ¬‘Ω¯––ƒ£–Õ◊÷µ‰◊™ªª£©----------------------------------\n");
		List<String> ignoredKeys = new ArrayList<>();
		ignoredKeys.add("url");
		list = Object_XMMap.setClass(XMObject.class).getMapArrayByClassWithIgnoredKeys(objectList, ignoredKeys);
		System.out.println(list);

		System.out.println("\nƒ£–Õ->Map£®‘ –ÌΩ¯––ƒ£–Õ◊÷µ‰◊™ªª£©----------------------------------\n");
		map = (Map<String, Object>) Object_XMMap.setObject(xmObject).getMapByObjectWithAllowedKeys(allowedKeys, null);
		System.out.println(map);

		System.out.println("\nƒ£–Õ->Map£®∫ˆ¬‘Ω¯––ƒ£–Õ◊÷µ‰◊™ªª£©----------------------------------\n");
		map = (Map<String, Object>) Object_XMMap.setObject(xmObject).getMapByObjectWithIgnoredKeys(ignoredKeys, null);
		System.out.println(map);

		System.out.println("\n…Ë÷√classµƒ‘ –ÌΩ¯––ƒ£–Õ◊÷µ‰◊™ªªKey----------------------------------\n");
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

		System.out.println("\n…Ë÷√classµƒ∫ˆ¬‘Ω¯––ƒ£–Õ◊÷µ‰◊™ªªKey----------------------------------\n");
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

		System.out.println("\nMap->ƒ£–Õ----------------------------------\n");
		map.clear();
		map.put("name", "xuemiyang");
		map.put("age", 22);
		map.put("money", 100.5);
		map.put("url", "http://www.xuemiyang.com");
		map.put("isXM", "yes");
		xmObject = (XMObject) Object_XMMap.setClass(XMObject.class).getInstanceByClass(map, null);
		System.out.println(xmObject.toString());

		System.out.println("\n…Ë÷√ÃÊªªµƒKey----------------------------------\n");
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

		System.out.println("\nlist->ƒ£–Õ ˝◊È----------------------------------\n");
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
		
		System.out.println("\nƒ£–Õ->jsonString----------------------------------\n");
		String jsonString = Object_XMMap.setObject(xmObject).getJsonStringByObject();
		System.out.println(jsonString);
		
		System.out.println("\njsonString->jsonObejct----------------------------------\n");
		jsonString = "{\"name\":\"xuemiyang\", \"message\":\"i like xmextension\"}";
		map = (Map<String, Object>) Object_XMMap.setObject(jsonString).getJsonObjectByObject();
		System.out.println(map);
		
		System.out.println("\nxmjsonΩÈ…‹----------------------------------------------------\n");
		System.out.println("\nºÚµ•µƒjson◊™ªª----------------------------------------------------\n");
		jsonString = "{\"name\":\"xuemiyang\", \"age\":22, \"height\":174.5, \"isMan\":true, \"url\":null}";
		map = (Map<String, Object>) XMJson.getJsonObject(jsonString, null, null);
		System.out.println(map);
		jsonString = "[{\"name\":\"jack\", \"age\":22}, {\"name\":\"rose\",\"age\":21}]";
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
		
		System.out.println("\nªÒ»°jsonString÷–µƒƒ≥∏ˆ¬∑æ∂µƒ÷µ----------------------------------------------------\n");
		jsonString = "{\"name\":\"xuemiyang\", \"age\":22, \"money\":100.6, \"isMan\":true}";
		object = XMJson.getJsonObject(jsonString, "name", null);
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "isMan", null);
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "money", null);
		System.out.println(object);
		
		System.out.println("\nªÒ»°º¸¬∑æ∂µƒ÷µ----------------------------------------------------\n");
		jsonString = "{\"name\":\"xuemiyang\", \"age\":22, \"numArray\":[1,2,3,4,5], \"mapArray\":[{\"name\":\"jack\", \"isMan\":true}, {\"name\":\"rose\", \"isMan\":false}, {\"name\":\"tom\", \"isMan\":true}]}";
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
		
		System.out.println("\nªÒ»°∑÷“≥ ˝æ›----------------------------------------------------\n");
		object = XMJson.getJsonObject(jsonString, "numArray.[1-3]", null);
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "mapArray.[1-2]", null);
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "mapArray.[1-2].name", null);
		System.out.println(object);
		
		System.out.println("\nªÒ»°∂‡Ãıº¸¬∑æ∂µƒ÷µ----------------------------------------------------\n");
		object = XMJson.getJsonObject(jsonString, "numArray.[1-3], mapArray.[1-2], mapArray.[1-2].name", null);
		System.out.println(object);
		
		System.out.println("\nªÒ»°º¸¬∑æ∂µƒ÷µÕ®π˝Ãıº˛----------------------------------------------------\n");
		jsonString = "{\"array\":[{\"name\":\"jack\", \"age\":22, \"money\":200.4}, {\"name\":\"rose\", \"age\":20, \"money\":1056.2}, {\"name\":\"tom\", \"age\":25, \"money\":20053.1}]}";
		object = XMJson.getJsonObject(jsonString, "array.name", "\"array.age\" == 20");
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "array.name", "\"array.age\" >= 22");
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "array.name", "\"array.age\" != 22");
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "array.name", "\"array.age\" >= 20 && \"array.money\" < 2000");
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "array.name", "!(\"array.age\" >= 20 && \"array.money\" < 2000)");
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "array.name", "min(\"array.money\") == \"array.money\"");
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "array.name", "max(\"array.age\") == \"array.age\"");
		System.out.println(object);
		object = XMJson.getJsonObject(jsonString, "array.name", "avg(\"array.money\") >= \"array.money\"");
		System.out.println(object);
		
		XMCondition condition = XMConditionFactory.getCompositeCondition(jsonString, "\"array.age\" == 20");
		object = XMJson.getJsonObject(jsonString, "array.name", condition);
		System.out.println(object);
		
		System.out.println("\nxmconditionµƒΩÈ…‹----------------------------------------------------\n");
		condition = XMConditionFactory.getCompositeCondition(null, "1 < 0 || (!(5 > 6) && 6 > 5)");
		System.out.println(condition.calculate());
		condition = XMConditionFactory.getCompositeCondition(null, "11155566 ? ^1{3}5{3}6{2}");
		System.out.println(condition.calculate());
		
		System.out.println("\nŒ™jsonString…Ë÷√÷µ----------------------------------------------------\n");
		jsonString = XMJson.setJsonValue(jsonString, "array.money", "300", "\"array.name\" == rose");
		System.out.println(jsonString);
		jsonString = XMJson.setJsonValue(jsonString, "array.money", "900", "\"array.money\" == min(\"array.money\")");
		System.out.println(jsonString);
		jsonString = XMJson.setJsonValue(jsonString, "array.[1-2].money", "110", null);
		System.out.println(jsonString);
		jsonString = XMJson.setJsonValue(jsonString, "array.age", "18", null);
		System.out.println(jsonString);
		jsonString = XMJson.setJsonValue(jsonString, "array.age, array.money", "20, 600.5", null);
		System.out.println(jsonString);
		jsonString = XMJson.setJsonValue(jsonString, "array.age", "null", null);
		System.out.println(jsonString);
		
		System.out.println("\nŒ™jsonString…Ë÷√º¸----------------------------------------------------\n");
		jsonString = XMJson.setJsonKey(jsonString, "array", "peoples", null);
		System.out.println(jsonString);
		jsonString = XMJson.setJsonKey(jsonString, "peoples.name, peoples.money", "nickName, property", null);
		System.out.println(jsonString);
		
		System.out.println("\nŒ™jsonStringÃÌº”jsonString----------------------------------------------------\n");
		jsonString = XMJson.addJsonString(jsonString, null, "\"dogs\":[{\"name\":\"do\"}], \"nums\":[1,2,3]", null);
		System.out.println(jsonString);
		jsonString = XMJson.addJsonString(jsonString, "peoples, dogs, nums", "\"donging\":{\"type\":\"play ball\"}, \"isMale\":true, 22", null);
		System.out.println(jsonString);
		jsonString = XMJson.addJsonString(jsonString, "peoples.donging", "\"count\":2", null);
		System.out.println(jsonString);
		
		System.out.println("\nŒ™jsonString…æ≥˝jsonString----------------------------------------------------\n");
		jsonString = XMJson.deleteJsonString(jsonString, "peoples.donging.count, dogs, nums", null);
		System.out.println(jsonString);
		jsonString = XMJson.deleteJsonString(jsonString, "peoples.age", "\"peoples.age\" == null");
		System.out.println(jsonString);
		
		System.out.println("\nÕ®π˝json¿¥¥¥Ω® ˝æ›ø‚----------------------------------------------------\n");
		String database = XMJson.createTable(null, "goods", "{\"type\":\" ˝¬Î≤˙∆∑\", \"subGoods\":[{\"name\":\"–°√◊3\", \"price\":999.9}]}");
		System.out.println(database);
		database = XMJson.createTable(database, "houses", "{\"name\":\"ºÚ¬™Œ›◊”\", \"address\":\"π„Œ˜π∆Ω –xx’ÚxxΩ÷xxx∫≈\", \"size\":89}");
		System.out.println(database);
		
		System.out.println("\n≤Â»Î ˝æ›----------------------------------------------------\n");
		database = XMJson.insertKeyValues(database, "goods", "{\"type\":\"ÀÆπ˚\", \"subGoods\":[{\"name\":\"œ„Ω∂\", \"price\":1.5}]}",null);
		System.out.println(database);
		database = XMJson.insertKeyValues(database, "goods.subGoods", "{\"name\":\"ª™Œ™»Ÿ“´3C\", \"price\":555}", "\"type\" ==  ˝¬Î≤˙∆∑");
		System.out.println(database);
		database = XMJson.insertKeyValues(database, "goods.subGoods", "{\"name\":\"iphone6\", \"price\":4800}", "\"type\" ==  ˝¬Î≤˙∆∑");
		System.out.println(database);
		database = XMJson.insertKeyValues(database, "goods.subGoods", "{\"name\":\"˜»◊Â\", \"price\":1700}", "\"type\" ==  ˝¬Î≤˙∆∑");
		System.out.println(database);
		database = XMJson.insertKeyValues(database, "houses", "{\"name\":\"ª™¿ˆŒ›◊”\", \"address\":\"π„÷› –∫£÷È«¯xxΩ÷xx∫≈\", \"size\":200}", null);
		System.out.println(database);
		
		System.out.println("\n…æ≥˝ ˝æ›----------------------------------------------------\n");
		database = XMJson.deleteValues(database, "houses.[0-*]", "\"name\" == ºÚ¬™Œ›◊”");
		System.out.println(database);
		database = XMJson.deleteValues(database, "goods.subGoods.[0-*]", "\"type\" ==  ˝¬Î≤˙∆∑  && \"subGoods.name\" == –°√◊3");
		System.out.println(database);
		database = XMJson.deleteValues(database, "houses", null);
		System.out.println(database);
		
		System.out.println("\n∏¸–¬÷µ----------------------------------------------------\n");
		database = XMJson.updateValues(database, "goods", "subGoods.price, subGoods.name", "890, \"ª™Œ™»Ÿ“´«‡¥∫∞Ê\"", "\"subGoods.name\" == ª™Œ™»Ÿ“´3C");
		System.out.println(database);
		database = XMJson.updateValues(database, "goods.subGoods", "price, name", "1650, \"˜»◊Â2\"", "\"subGoods.name\" == ˜»◊Â");
		System.out.println(database);
		
		System.out.println("\n∏¸–¬º¸----------------------------------------------------\n");
		database = XMJson.updateKeys(database, "goods", "type", "goodType", null);
		System.out.println(database);
		
		System.out.println("\n≤È—Ø÷µ----------------------------------------------------\n");
		object = XMJson.selectJsonObject(database, "goods", null);
		System.out.println(object);
		object = XMJson.selectJsonObject(database, "goods", "\"goods.goodType\" ==  ˝¬Î≤˙∆∑");
		System.out.println(object);
		object = XMJson.selectJsonObject(database, "goods.subGoods.name", "\"goods.goodType\" ==  ˝¬Î≤˙∆∑");
		System.out.println(object);
		object = XMJson.selectJsonObject(database, "goods.subGoods.[1-2]", "\"goods.goodType\" ==  ˝¬Î≤˙∆∑");
		System.out.println(object);
		object = XMJson.selectJsonObject(database, "goods.subGoods.[1-2].name", "\"goods.goodType\" ==  ˝¬Î≤˙∆∑");
		System.out.println(object);
		object = XMJson.selectJsonObject(database, "goods.subGoods.[1-2].name, goods.subGoods.[1-2].price", "\"goods.goodType\" ==  ˝¬Î≤˙∆∑");
		System.out.println(object);
	}
