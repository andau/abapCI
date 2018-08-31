package abapci.utils;

public class StringUtils {

	public static String EMPTY = "";

	public static boolean IsNullOrEmpty(String string) {
		return (string == null || string.length() == 0);
	}

}
