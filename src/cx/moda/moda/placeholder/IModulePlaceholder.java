package cx.moda.moda.placeholder;

import cx.moda.moda.module.Module;
import cx.moda.moda.module.storage.ModuleStorageHandler;

public interface IModulePlaceholder extends IPlaceholder {

	Module<ModuleStorageHandler> getModule();
	
	static String getPlaceholderName(final Module<ModuleStorageHandler> module, final String name) {
		return module.getName().toLowerCase().replace(" ", "_") + "_" + name;
	}
	
}
