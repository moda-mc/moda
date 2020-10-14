package cx.moda.moda.module.command;

import cx.moda.moda.module.storage.ModuleStorageHandler;
import org.bukkit.command.CommandExecutor;

import cx.moda.moda.module.Module;
import cx.moda.moda.module.ModuleElement;

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
