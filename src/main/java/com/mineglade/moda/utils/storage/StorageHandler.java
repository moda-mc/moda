package com.mineglade.moda.utils.storage;

import com.mineglade.moda.modules.Module;

public abstract class StorageHandler {

	protected final Module<? extends ModuleStorageHandler> module;

	public StorageHandler(final Module<? extends ModuleStorageHandler> module) {
		this.module = module;
	}

}
