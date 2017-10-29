package abapci.domain;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InvalidItem {

	private String className;
	private String description;
	private boolean suppressed;

	public InvalidItem(String className, String description, boolean suppressed) {
		this.className = className;
		this.description = description;
		this.suppressed = suppressed;
	}

	public String getDescription() {
		return description;
	}

	public String getClassName() {
		return className;
	}

	public static List<InvalidItem> filterInvalidItems(List<InvalidItem> invalidItems, Predicate<InvalidItem> predicate) {
		return invalidItems.stream().filter(predicate).collect(Collectors.<InvalidItem>toList());
	}

	public static Predicate<InvalidItem> isSuppressed() {
		return p -> p.suppressed;
	}

	public static Predicate<InvalidItem> isActive() {
		return p -> p.suppressed == false;
	}

}
