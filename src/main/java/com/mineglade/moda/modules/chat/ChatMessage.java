package com.mineglade.moda.modules.chat;

import com.mineglade.moda.modules.IMessage;

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
