package abapci.Exception;

public class ContinuousIntegrationConfigFileParseException extends Exception {
	private static final long serialVersionUID = 1L;

	public ContinuousIntegrationConfigFileParseException() {
		super();
	}

	public ContinuousIntegrationConfigFileParseException(String message) {
		super(message);
	}

	public ContinuousIntegrationConfigFileParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public ContinuousIntegrationConfigFileParseException(Throwable cause) {
		super(cause);
	}

}
