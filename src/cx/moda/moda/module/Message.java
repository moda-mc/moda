package cx.moda.moda.module;

public class Message implements IMessage {

	private final String path;
	private final String defaultMessage;

	public Message(final String path, final String defaultMessage) {
		this.path = path;
		this.defaultMessage = defaultMessage;
	}

	@Override
	public String getPath() {
		return this.path;
	}

	@Override
	public String getDefault() {
		return this.defaultMessage;
	}

}
