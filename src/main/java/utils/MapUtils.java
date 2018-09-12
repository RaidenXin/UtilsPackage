package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MapUtils {

	private static final Map<String, Object> EMPTY_MAP = new HashMap<>(0);
	
	public static final Map<String, Object> getEntryHashMap(){
		return EMPTY_MAP;
	}
	
	public static final Map<String, Object> copyMap(Map<String, Object> sourceMap){
		Map<String, Object> result = new HashMap<>();
		result.putAll(sourceMap);
		return result;
	}
	
	public static final String map2String(Map<String, Object> map) {
		StringBuilder builder = new StringBuilder();
		for (Entry<String, Object> entry : map.entrySet()) {
			builder.append("{");
			builder.append(entry.getKey());
			builder.append(",");
			builder.append(ObjectFormatUtils.Object2String(entry.getValue()));
			builder.append("};");
		}
		return builder.substring(0, builder.length() -1);
	}
	
	public static final boolean isEmpty(Map<String, Object> map) {
		if (null == map || map.isEmpty()) {
			return true;
		}
		for (Object value : map.values()) {
			if (null == value) {
				return true;
			}
		}
		return false;
	}
}
