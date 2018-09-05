package abapci.domain;

public enum ErrorPriority {
	ERROR(1), WARNING(2), INFO(3);

	private final int id;

	ErrorPriority(int id) {
		this.id = id;
	}

	public int getValue() {
		return id;
	}

	public static ErrorPriority getFromInt(int errorId) {
		ErrorPriority errorPriority;
		switch (errorId) {
		case 1:
			errorPriority = ErrorPriority.ERROR;
			break;
		case 2:
			errorPriority = ErrorPriority.WARNING;
			break;
		case 3:
			errorPriority = ErrorPriority.INFO;
			break;
		default:
			errorPriority = ErrorPriority.ERROR;
			break;
		}

		return errorPriority;
	}

}
