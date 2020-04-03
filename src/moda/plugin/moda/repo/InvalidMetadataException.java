package moda.plugin.moda.repo;

public class InvalidMetadataException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public InvalidMetadataException(final Throwable cause) {
		super(cause);
	}
	
	public InvalidMetadataException(final String message) {
		super(message);
	}

}
