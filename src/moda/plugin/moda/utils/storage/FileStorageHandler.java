package moda.plugin.moda.utils.storage;

import java.io.IOException;

import moda.plugin.moda.modules.Module;
import moda.plugin.moda.utils.BukkitFuture;

public abstract class FileStorageHandler extends StorageHandler {

	public FileStorageHandler(final Module<? extends ModuleStorageHandler> module) throws IOException {
		super(module);
	}
	
	public BukkitFuture<Void> saveAsync(){
		return new BukkitFuture<>(() -> {
			save();
			return null;
		});
	}
	
	public abstract void save() throws IOException;

}
