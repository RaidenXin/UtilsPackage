package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


/**
 * XML解析工具
 * @author Raiden
 *
 */
public class XMLParseUtils {

	/**
	 * 获取某个节点下子节点的内容
	 * @param XML
	 * @param rootName
	 * @param nodenName
	 * @return
	 */
	public static String getValueByRootName(String XML,String rootName, String nodenName) {
		List<Map<String, String>> maps = XMLParseUtils.getChildElementValuesByRootName(XML, rootName);
		String value = null;
		if (null != maps && !maps.isEmpty()) {
			Map<String, String> map = maps.get(0);
			value = map.get(nodenName);
			if (StringUtils.isNotBlank(value)) {
				value = value.replaceAll("\t", "").replaceAll("\n", "").trim();
			}
			return value;
		}
		return "";
	}
	/**
	 * 根据名字获取节点下所有子节点的内容，封装在map中返回
	 * @param XML
	 * @param name
	 * @return
	 */
	public static List<Map<String, String>> getChildElementValuesByRootName(String XML,String name){
		if (StringUtils.isAnyBlank(XML,name)) {
			return Collections.emptyList();
		}
		List<Map<String, String>> result = new ArrayList<>();
		Document document;
		try {
			document = DocumentHelper.parseText(XML);
			List<?> elements = document.selectNodes("//" + name);
			for (Object object : elements) {
				Map<String, String> valuesMap = new HashMap<>();
				Element root = (Element) object;
				List<?> childElements = root.elements();
				for (Object o : childElements) {
					Element element = (Element) o;
					valuesMap.put(element.getName(), element.getStringValue().replaceAll("\t", "").replaceAll("\n", "").trim());
				}
				result.add(valuesMap);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 *  以XML格式的获取节点内容
	 * @param XML
	 * @param name
	 * @return
	 */
	public static String getChildElementXMLByRootName(String XML,String name){
		if (StringUtils.isAnyBlank(XML,name)) {
			return "";
		}
		Document document;
		StringBuilder builder = new StringBuilder("");
		try {
			document = DocumentHelper.parseText(XML);
			List<?> elements = document.selectNodes("//" + name);
			for (Object object : elements) {
				Element root = (Element) object;
				builder.append(root.asXML());
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
	/**
	 * 获取所以子元素内容
	 * @param document
	 * @return
	 */
	public static Map<String, String> getChildElementValues(Document document){
		if(null == document) {
			return Collections.emptyMap();
		}
		Element root = document.getRootElement();
		List<?> childElements = root.elements();
		Map<String, String> valuesMap = new HashMap<>();
		for (Object object : childElements) {
			Element element = (Element) object;
			valuesMap.put(element.getName(), element.getStringValue());
		}
		return valuesMap;
	}
}
