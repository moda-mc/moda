package com.mineglade.moda.modules.statistics;

import com.mineglade.moda.modules.IMessage;

public enum StatisticsMessage implements IMessage {
	;

	private String path;
	private String defaultMessage;

	StatisticsMessage(final String path, final String defaultMessage) {
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
