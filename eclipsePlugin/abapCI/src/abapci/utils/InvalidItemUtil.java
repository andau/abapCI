package abapci.utils;

import abapci.domain.InvalidItem;

public class InvalidItemUtil {

	public static String getOutputForUnitTest(InvalidItem invalidItem) {
		return invalidItem.getClassName();
	}

	private static void extractFailInfo(String description) {
		// TODO
	}

}
