package utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * *类字段获取工具
 * @author Raiden
 *
 */
public class FieldUtils {
	
	/**
	 * *递归获取类所有的字段(包括全部父类)
	 * @param c
	 * @return
	 */
	public final static List<Field> getFields(Class<?> c) {
		List<Field> fields = new ArrayList<>();
		getFields(c, fields, Object.class);
		return fields;
	}
	
	/**
	 * *递归获取类所有的字段(递归到结束父类为止,不包含结束父类的字段)
	 * @param c
	 * @param endSuperClass 结束父类
	 * @return
	 */
	public final static List<Field> getFields(Class<?> c,Class<?> endSuperClass) {
		List<Field> fields = new ArrayList<>();
		getFields(c, fields, endSuperClass);
		return fields;
	}
	
	private static void getFields(Class<?> c,List<Field> fields,Class<?> endSuperClass) {
		for (Field field : c.getDeclaredFields()) {
			fields.add(field);
		}
		Class<?> superClass = c.getSuperclass();
		if (!superClass.equals(endSuperClass)) {
			getFields(superClass, fields, endSuperClass);
		}
	}
}
