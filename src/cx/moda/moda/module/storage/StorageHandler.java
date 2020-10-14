package cx.moda.moda.module.storage;

import cx.moda.moda.module.Module;

public abstract class StorageHandler implements UuidValueStore {

	protected final Module<? extends ModuleStorageHandler> module;

	public StorageHandler(final Module<? extends ModuleStorageHandler> module) {
		this.module = module;
	}

}
