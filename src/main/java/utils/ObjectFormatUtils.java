package utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * 
 * @author Raiden
 *
 */
public class ObjectFormatUtils {
	

	public static BigDecimal object2BigDecimal(Object object) {
		BigDecimal result = null;
		if (null != object) {
			if (object instanceof String) {
				result = StringFormatUtils.stringToBigDecimal(Object2String(object));
			}else if (object instanceof BigDecimal) {
				result = (BigDecimal) object;
			}
		}
		return result;
	}
	
	public static String Object2String(Object object) {
		if (null == object) {
			return null;
		}
		if (object instanceof Date) {
			return DateFormatUtils.date2String((Date) object,"yyyy-MM-dd");
		}
		return object.toString();
	}
	
	public static Date object2Date(Object object) {
		Date result = null;
		if (null != object) {
			if (object instanceof String) {
				result = StringFormatUtils.stringToDate(Object2String(object));
			}else if (object instanceof Date) {
				result = (Date) object;
			}
		}
		return result;
	}
	
	public static Integer object2Integer(Object object) {
		if (null == object) {
			return null;
		}
		if (object instanceof Integer) {
			return (Integer) object;
		}
		return Integer.valueOf(Object2String(object));
	}
	
	/**
	 **类之间的相同的属性拷贝
	 * @param source
	 * @param target
	 */
	public static void copy(Object source,Object target) {
		List<Field> fields = FieldUtils.getFields(source.getClass());
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				field.set(target, field.get(source));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
}
