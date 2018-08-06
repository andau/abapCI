package abapci.Exception;

public class InactivatedObjectEvaluationException extends Exception {

	private static final long serialVersionUID = 1L;

	public InactivatedObjectEvaluationException() {
		super();
	}

	public InactivatedObjectEvaluationException(String message) {
		super(message);
	}

	public InactivatedObjectEvaluationException(String message, Throwable cause) {
		super(message, cause);
	}

	public InactivatedObjectEvaluationException(Throwable cause) {
		super(cause);
	}

}
