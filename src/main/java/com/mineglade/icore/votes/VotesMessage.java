package com.mineglade.icore.votes;

import com.mineglade.icore.IMessage;

public enum VotesMessage implements IMessage {

	TEST("", ""),

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
