package abapci.domain;

import java.net.URI;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InvalidItem {

	private String className;
	private String description;
	private boolean suppressed;
	private URI uriToError;
	private String detail;

	public InvalidItem(String className, String description, boolean suppressed, URI uriToError, String detail) {
		this.className = className;
		this.description = description;
		this.suppressed = suppressed;
		this.uriToError = uriToError;
		this.detail = detail;
	}

	public InvalidItem(String className, String description, boolean suppressed, URI uriToError) {
		this.className = className;
		this.description = description;
		this.suppressed = suppressed;
		this.uriToError = uriToError;
	}

	public String getDescription() {
		return description;
	}

	public String getDetail() {
		return detail;
	}

	public String getClassName() {
		return className;
	}

	public boolean isSuppressed() {
		return suppressed;
	}

	public URI getUriToError() {
		return uriToError;
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
