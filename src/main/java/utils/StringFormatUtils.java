package utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class StringFormatUtils {

	private static final String EMPTY = "";
	private static final String BLANK_SPACE = " ";
	private static final String CHECK_TIME_ONE = ".*([\\d]{4}[-][\\d]{1,2}[-][\\d]{1,2}(([\\D]*)[\\d]{1,2}[:][\\d]{1,2}[:][\\d]{1,2})?).*";
	private static final String CHECK_TIME_TWO = ".*([\\d]{4}[年][\\d]{1,2}[月][\\d]{1,2}[日]?(([\\D]*)[\\d]{1,2}[时][\\d]{1,2}[分][\\d]{1,2}[秒])?).*";
	private static final String CHECK_TIME_THREE = ".*([\\d]{4}[/][\\d]{1,2}[/][\\d]{1,2}(([\\D]*)[\\d]{1,2}[:][\\d]{1,2}[:][\\d]{1,2})?).*"; 
	private static final Logger LOGGER = LoggerFactory.getLogger(StringFormatUtils.class);
	
	public static BigDecimal stringToBigDecimal(String amount) {
		final Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]{1,9})?$");
		amount = stringFormat(amount);
		if (StringUtils.isBlank(amount)) {
			LOGGER.error("String is null!");
			return null;
		}
		amount = amount.trim().replace(",", EMPTY);
		Matcher match = pattern.matcher(amount);
		if (!match.matches()) {
			LOGGER.error(amount + " is not a number!");
			return null;
		}
		return new BigDecimal(amount);
	}

	public static Date stringToDate(String time) {
		if (StringUtils.isBlank(time)) {
			return null;
		}
		final Pattern patternOne = Pattern.compile(CHECK_TIME_ONE);
		final Pattern patternTwo = Pattern.compile(CHECK_TIME_TWO);
		final Pattern patternThree = Pattern.compile(CHECK_TIME_THREE);
		Matcher matcher = null;
		Date result = null;
		try {
			if ((matcher = patternOne.matcher(time)).matches()) {
				if (StringUtils.isNotBlank(matcher.group(2))) {
					result = simpleDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), matcher, time, true);		
				}else {
					result = simpleDateFormat(new SimpleDateFormat("yyyy-MM-dd"), matcher, time, false);					
				}
			}else if((matcher = patternTwo.matcher(time)).matches()) {
				if (StringUtils.isNotBlank(matcher.group(2))) {
					result = simpleDateFormat(new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒"), matcher, time, true);
				}else {					
					result = simpleDateFormat(new SimpleDateFormat("yyyy年MM月dd日"), matcher, time, false);
				}
			}else if((matcher = patternThree.matcher(time)).matches()) {
				if (StringUtils.isNotBlank(matcher.group(2))) {
					result = simpleDateFormat(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"), matcher, time, true);
				}else {
					result = simpleDateFormat(new SimpleDateFormat("yyyy/MM/dd"), matcher, time, false);					
				}
			}else {
				LOGGER.error("String is not a time format!");
			}
		} catch (ParseException e) {
		}
		return result;
	}
	
	private static Date simpleDateFormat(SimpleDateFormat simpleDateFormat,Matcher matcher,String time,boolean flag) throws ParseException{
		if (flag) {
			time = time.substring(matcher.start(1), matcher.start(3)) + BLANK_SPACE + time.substring(matcher.end(3),matcher.end(1));
		}else {
			time = time.substring(matcher.start(1), matcher.end(1));
		}
		return simpleDateFormat.parse(time);
	}
	
	public static String stringFormat(String value) {
		if (StringUtils.isNotBlank(value)) {
			value = value.replaceAll("\t", EMPTY).replaceAll("\n", EMPTY)
					.replaceAll(" ", EMPTY).trim();
		}
		return value;
	}
	
	public static Object string2Object(String value) {
		BigDecimal bigDecimal = StringFormatUtils.stringToBigDecimal(value);
		if (null != bigDecimal) {
			return bigDecimal;
		}
		Date date = StringFormatUtils.stringToDate(value);
		if (null != date) {
			return date;
		}
		return value;
	}

}
