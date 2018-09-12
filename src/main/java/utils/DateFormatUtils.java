package utils;

import java.text.SimpleDateFormat;
import java.util.Date;



public class DateFormatUtils {
	
	public static String date2String(Date date,String format) {
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		String result = simpleDateFormat.format(date);
		return result;
	}
}
