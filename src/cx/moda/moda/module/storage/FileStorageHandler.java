package cx.moda.moda.module.storage;

import java.io.IOException;

import cx.moda.moda.module.Module;

public abstract class FileStorageHandler extends StorageHandler {

	public FileStorageHandler(final Module<? extends ModuleStorageHandler> module) throws IOException {
		super(module);
	}
	
	public abstract void save() throws IOException;

}
