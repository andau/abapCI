package abapci.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import abapci.domain.InvalidItem;

public class InvalidItemUtil {

	public static String getOutputForUnitTest(InvalidItem invalidItem) {
		String description = invalidItem.getDescription();
		description = description.replaceAll("Critical Assertion Error:", "");
		description = description.replaceAll("Kritischer Assertion-Fehler:", "");
		return String.format("%s: %s", invalidItem.getClassName(), description);
	}

	public static String getOutputForAtcTest(InvalidItem invalidItem) {
		return invalidItem.getClassName() + ": " + invalidItem.getDescription();
	}

	public static String extractClassName(String description) {
		Pattern pattern = Pattern.compile("<(.*?)=");
		Matcher matcher = pattern.matcher(description);
		return matcher.find() ? matcher.group(1) : "";
	}

}
