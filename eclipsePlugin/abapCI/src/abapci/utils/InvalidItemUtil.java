package abapci.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import abapci.domain.InvalidItem;

public class InvalidItemUtil {

	public static String getOutputForUnitTest(InvalidItem invalidItem) {
		String description = invalidItem.getDescription();
		description = description.replaceAll("Critical Assertion Error:", "");
		description = description.replaceAll("Kritischer Assertion-Fehler:", "");
		return String.format("%s: %s", invalidItem.getClassName(), description + "; " + invalidItem.getDetail());
	}

	public static String getOutputForUnitTest(List<InvalidItem> invalidItems) {
		StringBuilder invalidItemsOutput = new StringBuilder();
		for (InvalidItem invalidItem : invalidItems) {
			invalidItemsOutput.append(getOutputForUnitTest(invalidItem) + System.getProperty("line.separator"));
		}
		if (invalidItems.size() > 1) {
			invalidItemsOutput.append(String.format("There are in total %d failed unit tests", invalidItems.size()));
		}

		return invalidItemsOutput.toString();
	}

	public static String getOutputForAtcTest(List<InvalidItem> invalidItems) {
		StringBuilder invalidItemsOutput = new StringBuilder();
		for (InvalidItem invalidItem : invalidItems) {
			invalidItemsOutput.append(getOutputForAtcTest(invalidItem) + System.getProperty("line.separator"));
		}
		if (invalidItems.size() > 1) {
			invalidItemsOutput.append(String.format("There are in total %d ATC findings", invalidItems.size()));
		}
		return invalidItemsOutput.toString();
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
