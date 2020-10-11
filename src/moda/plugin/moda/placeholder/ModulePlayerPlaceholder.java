package moda.plugin.moda.placeholder;

import org.bukkit.entity.Player;

import com.google.common.base.Function;

import moda.plugin.moda.module.Module;
import moda.plugin.moda.module.storage.ModuleStorageHandler;

public class ModulePlayerPlaceholder extends ModaPlayerPlaceholder implements IModulePlaceholder {

	private final Module<ModuleStorageHandler> module;
	
	public ModulePlayerPlaceholder(final Module<ModuleStorageHandler> module, final String name, final Function<Player, String> value) {
		this(module, name, value, false);
	}
	
	public ModulePlayerPlaceholder(final Module<ModuleStorageHandler> module, final String name, final Function<Player, String> value, final boolean async) {
		super(IModulePlaceholder.getPlaceholderName(module, name), value, async);
		this.module = module;
	}
	
	@Override
	public Module<ModuleStorageHandler> getModule() {
		return this.module;
	}

}
