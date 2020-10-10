package moda.plugin.moda.module.command;

import org.bukkit.command.CommandExecutor;

import moda.plugin.moda.module.Module;
import moda.plugin.moda.module.ModuleElement;
import moda.plugin.moda.module.storage.ModuleStorageHandler;

public abstract class ModuleCommandExecutor<Mod extends Module<? extends ModuleStorageHandler>> implements ModuleElement<Mod>, CommandExecutor {
	
	private final Mod module;
	
	public ModuleCommandExecutor(final Mod module) {
		this.module = module;
	}
	
	@Override
	public Mod getModule() {
		return this.module;
	}

}
