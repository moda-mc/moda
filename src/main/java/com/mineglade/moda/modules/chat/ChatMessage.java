package com.mineglade.moda.modules.chat;

import com.mineglade.moda.modules.IMessage;

public enum ChatMessage implements IMessage {
	
	// Mute Command
	COMMAND_MUTE_SUCCESS("command.mute.success", "&7{target} has been muted."),
	COMMAND_MUTE_ERROR_EXEMPT("command.mute.error.exempt", "&c{target} cannot be muted."),
	COMMAND_MUTE_ERROR_INVALID("command.mute.error.invalid", "&c{target} doesn't exist"),
	COMMAND_MUTE_ERROR_UNKNOWN("command.mute.error.unknown","&cThere was an error muting {target}")

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
