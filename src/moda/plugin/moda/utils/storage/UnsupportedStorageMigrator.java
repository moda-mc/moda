package moda.plugin.moda.utils.storage;

public class UnsupportedStorageMigrator<T extends ModuleStorageHandler> implements StorageMigrator<T> {
	
	@Override
	public void migrate(final T from, final T to) {
		throw new UnsupportedOperationException("This module does not support storage migration");
	}
	
	class FakeModuleStorageHandler implements ModuleStorageHandler {}

}
