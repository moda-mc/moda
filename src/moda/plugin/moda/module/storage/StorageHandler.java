package moda.plugin.moda.module.storage;

import moda.plugin.moda.module.Module;

public abstract class StorageHandler implements UuidValueStore {

	protected final Module<? extends ModuleStorageHandler> module;

	public StorageHandler(final Module<? extends ModuleStorageHandler> module) {
		this.module = module;
	}

}
