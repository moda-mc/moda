package moda.plugin.moda.module.command;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

import moda.plugin.moda.Moda;
import moda.plugin.moda.module.Module;
import moda.plugin.moda.module.storage.ModuleStorageHandler;

public class ModuleCommandBuilder {
	
	private final PluginCommand command;
	
	public ModuleCommandBuilder(final String name) {
		try {
			final Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
			constructor.setAccessible(true);
			this.command = constructor.newInstance(name, Moda.instance);
		} catch (final InvocationTargetException | InstantiationException |
				IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}
	
	public ModuleCommandBuilder withPermission(final String permission) {
		this.command.setPermission(permission);
		return this;
	}
	
	public ModuleCommandBuilder withAlias(final String alias) {
		this.command.getAliases().add(alias);
		return this;
	}
	
	public ModuleCommandBuilder withAliases(final String... alias) {
		this.command.getAliases().addAll(Arrays.asList(alias));
		return this;
	}
	
	public ModuleCommandBuilder withAliases(final Collection<String> aliases) {
		this.command.getAliases().addAll(aliases);
		return this;
	}
	
	public ModuleCommandBuilder withUsage(final String usage) {
		this.command.setUsage(usage);
		return this;
	}
	
	public ModuleCommandBuilder withDescription(final String description) {
		this.command.setDescription(description);
		return this;
	}

	public ModuleCommandBuilder withExecutor(final CommandExecutor executor) {
		this.command.setExecutor(executor);
		return this;
	}
	
	public ModuleCommandBuilder withTabCompleter(final TabCompleter tabCompleter) {
		this.command.setTabCompleter(tabCompleter);
		return this;
	}
	
	public PluginCommand create() {
		return this.command;
	}
	
	public void register(final Module<? extends ModuleStorageHandler> module) {
		module.registerCommand(this.command);
	}
}
