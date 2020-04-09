package moda.plugin.moda.module;

import moda.plugin.moda.module.storage.ModuleStorageHandler;

public interface ModuleElement<Mod extends Module<? extends ModuleStorageHandler>> {
	
	Mod getModule();

}
