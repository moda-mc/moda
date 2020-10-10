package moda.plugin.moda.module.storage;

import java.io.IOException;

import moda.plugin.moda.module.Module;

public abstract class FileStorageHandler extends StorageHandler {

	public FileStorageHandler(final Module<? extends ModuleStorageHandler> module) throws IOException {
		super(module);
	}
	
	public abstract void save() throws IOException;

}
