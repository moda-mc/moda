package moda.plugin.spigot.modules.chat;

import moda.plugin.spigot.modules.IMessage;

public enum ChatMessage implements IMessage {

	// Mute Command
	ERRORS_UNKNOWN("errors.unknown", "&cthere was an error sending your chat message."),

	;

	private String path;
	private String defaultMessage;

	ChatMessage(final String path, final String defaultMessage) {
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
