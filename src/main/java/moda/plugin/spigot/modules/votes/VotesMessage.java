package moda.plugin.spigot.modules.votes;

import moda.plugin.spigot.modules.IMessage;

public enum VotesMessage implements IMessage {

	TEST("test", ""),

	;

	private String path;
	private String defaultMessage;

	VotesMessage(final String path, final String defaultMessage) {
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
