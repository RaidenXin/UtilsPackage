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

import com.e2e.epd.exception.EpdException;

public class StringFormatUtils {

	private static final String EMPTY = "";
	private static final String BLANK_SPACE = " ";
	private static final String CHECK_TIME_ONE = "[\\d]{4}[-][\\d]{1,2}[-][\\d]{1,2}(.*[\\d]{1,2}[:][\\d]{1,2}[:][\\d]{1,2})?.*";
	private static final String CHECK_TIME_TWO = "[\\d]{4}[年][\\d]{1,2}[月][\\d]{1,2}[日]?(([\\D]*)[\\d]{1,2}[时][\\d]{1,2}[分][\\d]{1,2}[秒])?$";
	private static final String CHECK_TIME_THREE = "[\\d]{4}[/][\\d]{1,2}[/][\\d]{1,2}(([\\D]*)[\\d]{1,2}[/][\\d]{1,2}[/][\\d]{1,2})?$";
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
			LOGGER.error("String is not a number!");
			return null;
		}
		return new BigDecimal(amount);
	}

	public static Date stringToDate(String time) {
		final Pattern patternOne = Pattern.compile(CHECK_TIME_ONE);
		final Pattern patternTwo = Pattern.compile(CHECK_TIME_TWO);
		final Pattern patternThree = Pattern.compile(CHECK_TIME_THREE);
		Matcher matcherOne = patternOne.matcher(time);
		Matcher matcherTwo = patternTwo.matcher(time);
		Matcher matcherThree = patternThree.matcher(time);
		Date result = null;
		try {
			if (matcherOne.matches()) {
				result = simpleDateFormat(new SimpleDateFormat("yyyy-MM-dd"), null, time);
			}else if(matcherTwo.matches()) {
				result = simpleDateFormat(new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒"), matcherTwo, time);
			}else if(matcherThree.matches()) {
				result = simpleDateFormat(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"), matcherThree, time);
			}else {
				LOGGER.error("String is not a time format!");
			}
		} catch (ParseException e) {
			throw new Exception("Error in time conversion!");
		}
		return result;
	}
	
	private static Date simpleDateFormat(SimpleDateFormat simpleDateFormat,Matcher matcher,String time) throws ParseException{
		if (null != matcher) {
			String temp = matcher.group(2);
			if (StringUtils.isNotBlank(temp)) {
				time = time.replaceAll(matcher.group(2), BLANK_SPACE);			
			}
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

}
