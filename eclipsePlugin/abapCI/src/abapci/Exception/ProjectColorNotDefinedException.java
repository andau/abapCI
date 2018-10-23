package abapci.Exception;

public class ProjectColorNotDefinedException extends Exception {
	private static final long serialVersionUID = 1L;

	public ProjectColorNotDefinedException() {
		super();
	}

	public ProjectColorNotDefinedException(String message) {
		super(message);
	}

	public ProjectColorNotDefinedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProjectColorNotDefinedException(Throwable cause) {
		super(cause);
	}

}
