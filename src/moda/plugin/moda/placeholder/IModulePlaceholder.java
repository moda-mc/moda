package moda.plugin.moda.placeholder;

import moda.plugin.moda.module.Module;
import moda.plugin.moda.module.storage.ModuleStorageHandler;

public interface IModulePlaceholder extends IPlaceholder {

	Module<ModuleStorageHandler> getModule();
	
	static String getPlaceholderName(final Module<ModuleStorageHandler> module, final String name) {
		return module.getName().toLowerCase().replace(" ", "_") + "_" + name;
	}
	
}
