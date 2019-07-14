package moda.plugin.spigot.utils.storage;

import moda.plugin.spigot.modules.Module;

public abstract class StorageHandler {

	protected final Module module;

	public StorageHandler(final Module module) {
		this.module = module;
	}

}
