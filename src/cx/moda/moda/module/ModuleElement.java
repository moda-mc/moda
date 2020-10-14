package cx.moda.moda.module;

import cx.moda.moda.module.storage.ModuleStorageHandler;

public interface ModuleElement<Mod extends Module<? extends ModuleStorageHandler>> {
	
	Mod getModule();

}
