package moda.plugin.moda.placeholder;

import java.util.function.Supplier;

import moda.plugin.moda.module.Module;
import moda.plugin.moda.module.storage.ModuleStorageHandler;

public class ModulePlaceholder extends ModaPlaceholder implements IModulePlaceholder {
	
	private final Module<ModuleStorageHandler> module;

	public ModulePlaceholder(final Module<ModuleStorageHandler> module, final String name, final Supplier<String> value) {
		this(module, name, value, false);
	}
	
	public ModulePlaceholder(final Module<ModuleStorageHandler> module, final String name, final Supplier<String> value, final boolean async) {
		super(IModulePlaceholder.getPlaceholderName(module, name), value, async);
		this.module = module;
	}
	
	@Override
	public Module<ModuleStorageHandler> getModule() {
		return this.module;
	}

}
