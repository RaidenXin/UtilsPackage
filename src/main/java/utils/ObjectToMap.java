package utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.e2e.epd.base.order.Order;

public class ObjectToMap {

	private static final String DOC_TYPE = "DOC_TYPE";
	
	public static Map<String, Object> object2Map(Object object){
		return object2Map(object, true);
	}
	
	private static Map<String, Object> object2Map(Object object,boolean stamp){
		Map<String, Object> result = new HashMap<>();
		if (null == object) {
			return result;
		}
		String name = object.getClass().getName();
		Field[] fields = object.getClass().getDeclaredFields();
		Map<String, Object> docMap = new HashMap<>(); 
		try {
			for (Field field : fields) {
				field.setAccessible(true);
				Object value = field.get(object);
				if (value instanceof List) {
					List<?> details = (List<?>) field.get(object);
					if (null != details) {
						List<Map<String, Object>> maps = new ArrayList<>();
						String detailName = null;
						for (Object detail : details) {
							detailName = detail.getClass().getName();
							maps.add(object2Map(detail, false));
						}
						result.put(nameToUpperCase(detailName), maps);
					}
				}else if (value instanceof Order) {
					docMap.put(nameToUpperCase(field.getName()), object2Map(value, false));
				}else {
					docMap.put(field.getName(), field.get(object));
				}
			}
			docMap.put(DOC_TYPE, nameToUpperCase(name));
			if (!stamp) {
				return docMap;
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		result.put(nameToUpperCase(name), docMap);
		return result;
	}
	
	private static String nameToUpperCase(String className){
		className = className.substring(className.lastIndexOf(".") + 1);
		char[] chars = className.toCharArray();
		StringBuilder builder = new StringBuilder();
		for (int i = 0,n = chars.length;i < n;i++) {
			char c = chars[i];
			if (c > 64 && c < 91) {
				builder.append(i != 0?"_":"");
				builder.append(c);
			}else if (96 < c && c < 123) {
				c -= 32;
				builder.append(c);
			}
		}
		return builder.toString();
	}
	
	public static Object map2Object(Map<String, Object> map,Object target){
		Class<?> c = target.getClass();
		Field[] fields = c.getDeclaredFields();
		try {
			for (Field field : fields) {
				field.setAccessible(true);
				Class<?> type = field.getType();
				Object velua = map.get(field.getName());
				if (equalsOfClass(type, velua)) {
					field.set(target, velua);					
				}else {
//					field.set(target, ObjectConversionUtils.typeConversion(c, target));
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return target;
	}
	
	private static boolean equalsOfClass(Class<?> c ,Object object){
		if (null == object) {
			return false;
		}
		return c.equals(object.getClass());
	}
}
