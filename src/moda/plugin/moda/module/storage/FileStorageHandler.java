package moda.plugin.moda.module.storage;

import java.io.IOException;

import moda.plugin.moda.module.Module;
import moda.plugin.moda.util.BukkitFuture;

public abstract class FileStorageHandler extends StorageHandler {

	public FileStorageHandler(final Module<? extends ModuleStorageHandler> module) throws IOException {
		super(module);
	}
	
	@Deprecated
	public BukkitFuture<Void> saveAsync(){
		return new BukkitFuture<>(() -> {
			save();
			return null;
		});
	}
	
	public abstract void save() throws IOException;

}
