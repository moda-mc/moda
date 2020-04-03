package moda.plugin.moda.utils.storage;

public interface StorageMigrator<T extends ModuleStorageHandler> {

	void migrate(T from, T to);
	
}
