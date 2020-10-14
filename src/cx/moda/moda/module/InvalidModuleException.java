package cx.moda.moda.module;

public class InvalidModuleException extends RuntimeException {

	private static final long serialVersionUID = 95893264859591863L;

	public InvalidModuleException(final String string) {
		super(string);
	}
	
	public InvalidModuleException(final Throwable cause) {
		super(cause);
	}

}
