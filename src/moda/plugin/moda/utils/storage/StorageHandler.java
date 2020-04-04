package moda.plugin.moda.utils.storage;

import moda.plugin.moda.modules.Module;

public abstract class StorageHandler implements UuidValueStore {

	protected final Module<? extends ModuleStorageHandler> module;

	public StorageHandler(final Module<? extends ModuleStorageHandler> module) {
		this.module = module;
	}

}
