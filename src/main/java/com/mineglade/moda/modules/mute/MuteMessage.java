package com.mineglade.moda.modules.mute;

import com.mineglade.moda.modules.IMessage;

public enum MuteMessage implements IMessage {

	MUTED("muted", "&cYou are muted."),

	COMMAND_ERROR_GENERIC("command.error.generic", "An error has occured."),
	COMMAND_ERROR_DURATION("command.error.duration", "Invalid duration. The duration should be a number followed by [m|h|d|w|M|y]"),
	COMMAND_ERROR_PLAYERNOTFOUND("command.error.playernotfound", "The provided player name is invalid."),

	COMMAND_MUTE_SUCCESS_PERM("command.mute.success.perm", "{target} has been muted permanently"),
	COMMAND_MUTE_SUCCESS_TEMP("command.mute.success.temp", "{target} has been muted temporarily, until {date}"),
	COMMAND_MUTE_USAGE("command.mute.usage", "Usage: /{command} <player> [duration]"),

	COMMAND_UNMUTE_USAGE("command.unmute.usage", "Usage: /{command} <player>"),
	COMMAND_UNMUTE_SUCCESS("command.unmute.success", "Unmuted {target}"),

	;

	private String path;
	private String defaultMessage;

	MuteMessage(final String path, final String defaultMessage) {
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
