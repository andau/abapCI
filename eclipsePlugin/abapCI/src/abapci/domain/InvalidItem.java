package abapci.domain;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.sap.adt.tools.abapsource.abapunit.IAbapUnitAlertStackEntry;

public class InvalidItem {

	private String className;
	private String description;
	private boolean suppressed;
	private IAbapUnitAlertStackEntry firstStackEntry;

	public InvalidItem(String className, String description, boolean suppressed) {
		this.className = className;
		this.description = description;
		this.suppressed = suppressed;
	}

	public InvalidItem(String className, String description, boolean suppressed,
			IAbapUnitAlertStackEntry abapUnitAlertStackEntry) {
		this.className = className;
		this.description = description;
		this.suppressed = suppressed;
		this.firstStackEntry = abapUnitAlertStackEntry;
	}

	public String getDescription() {
		return description;
	}

	public String getClassName() {
		return className;
	}

	public boolean isSuppressed() {
		return suppressed;
	}

	public IAbapUnitAlertStackEntry getFirstStackEntry() {
		return firstStackEntry;
	}

	public static List<InvalidItem> filterInvalidItems(List<InvalidItem> invalidItems,
			Predicate<InvalidItem> predicate) {
		return invalidItems.stream().filter(predicate).collect(Collectors.<InvalidItem>toList());
	}

	public static Predicate<InvalidItem> isSuppressedPredicate() {
		return p -> p.suppressed;
	}

	public static Predicate<InvalidItem> isActive() {
		return p -> !p.suppressed;
	}

}
