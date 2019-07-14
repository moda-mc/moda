package moda.plugin.spigot.modules.vanish.storage;

import moda.plugin.spigot.modules.Module;
import moda.plugin.spigot.utils.storage.FileStorageHandler;

public class VanishFileStorageHandler extends FileStorageHandler implements VanishStorageHandler {

	public VanishFileStorageHandler(final Module<?> module) {
		super(module);
	}

}
