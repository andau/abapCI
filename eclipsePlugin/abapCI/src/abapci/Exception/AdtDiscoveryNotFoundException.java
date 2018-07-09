package abapci.Exception;

public class AdtDiscoveryNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public AdtDiscoveryNotFoundException() {
		super();
	}

	public AdtDiscoveryNotFoundException(String message) {
		super(message);
	}

	public AdtDiscoveryNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public AdtDiscoveryNotFoundException(Throwable cause) {
		super(cause);
	}
}
