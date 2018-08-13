package abapci.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import abapci.domain.InvalidItem;

public class InvalidItemUtil {

	public static String getOutputForUnitTest(InvalidItem invalidItem) {
		String className = extractClassName(invalidItem.getFirstStackEntry().getDescription());

		return String.format("%s: %s", className, invalidItem.getDescription());
	}

	private static String extractClassName(String description) {
		Pattern pattern = Pattern.compile("<(.*?)=");
		Matcher matcher = pattern.matcher(description);
		return matcher.find() ? matcher.group(1) : "";
	}

}
