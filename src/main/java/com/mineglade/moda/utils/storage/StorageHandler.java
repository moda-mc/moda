package com.mineglade.moda.utils.storage;

import com.mineglade.moda.modules.Module;

public abstract class StorageHandler {

	protected final Module module;

	public StorageHandler(final Module module) {
		this.module = module;
	}

}
