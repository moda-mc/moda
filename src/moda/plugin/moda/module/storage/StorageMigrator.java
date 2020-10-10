package moda.plugin.moda.module.storage;

public interface StorageMigrator<T extends ModuleStorageHandler> {

	void migrate(T from, T to) throws Exception;
	
}
