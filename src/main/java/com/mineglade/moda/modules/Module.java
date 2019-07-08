package com.mineglade.moda.modules;

import java.io.File;
import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.UnknownDependencyException;

import com.mineglade.moda.Moda;
import com.mineglade.moda.modules.chat.ChatModule;
import com.mineglade.moda.utils.storage.DatabaseStorageHandler;
import com.mineglade.moda.utils.storage.FileStorageHandler;
import com.mineglade.moda.utils.storage.ModuleStorageHandler;
import com.mineglade.moda.utils.storage.StorageType;
import com.mineglade.moda.votes.Votes;

import xyz.derkades.derkutils.FileUtils;

public abstract class Module<T extends ModuleStorageHandler> implements Listener {

	public static final Module<? extends ModuleStorageHandler>[] MODULES = new Module<?>[]{
			new Votes(),
			new ChatModule(),
	};

	protected Moda plugin;
	protected ModuleLogger logger;
	protected FileConfiguration config;
	protected LangFile lang;
	protected Scheduler scheduler;
	protected T storage;

	public Module() {
		this.plugin = Moda.instance;
	}

	public abstract String getName();

	public void onEnable() {}

	public void onDisable() {}

	public abstract IMessage[] getMessages();

	public Command[] getCommands() { return null; }

	public abstract FileStorageHandler getFileStorageHandler();

	public abstract DatabaseStorageHandler getDatabaseStorageHandler();

	public String[] getPluginDependencies() {
		return new String[] {};
	}

	public final File getDataFolder() {
		return new File(this.plugin.getDataFolder(), this.getName());
	}

	@SuppressWarnings("unchecked")
	public final void enable() throws Exception {
		// Initialize logger
		this.logger = new ModuleLogger(Moda.instance.getLogger(), this);

		// Check dependencies
		for (final String dependencyString : this.getPluginDependencies()) {
			final Plugin dependency = Bukkit.getPluginManager().getPlugin(dependencyString);
			if ((dependency == null) || !dependency.isEnabled()) {
				//this.logger.severe("This module could not be enabled, because it requires the plugin " + dependencyString);
				throw new UnknownDependencyException("This module could not be enabled, because it requires the plugin " + dependencyString);
				return;
			}
		}

		// Register listeners
		Bukkit.getPluginManager().registerEvents(this, this.plugin);

		// Create folder for configuration and data files
		this.getDataFolder().mkdirs();

		// Load config file
		final File configOutputFile = new File(this.getDataFolder(), "config.yaml");
		FileUtils.copyOutOfJar(this.getClass(), "/modules/" + this.getName() + "/config.yaml", configOutputFile);
		this.config = YamlConfiguration.loadConfiguration(configOutputFile);

		// Load language file
		final File langFileFile = new File(this.getDataFolder(), "lang.yaml");
		final FileConfiguration langFileFileConfiguration = YamlConfiguration.loadConfiguration(langFileFile);
		this.lang = new LangFile(langFileFileConfiguration, this.getMessages());

		// Register commands
		if ((this.getCommands() != null) && (this.getCommands().length > 0)) {
			try {
				final Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
				field.setAccessible(true);
				final CommandMap map = (CommandMap) field.get(Bukkit.getServer());

				for (final Command command : this.getCommands()) {
					map.register(Moda.instance.getName(), command);
				}
			} catch (final IllegalAccessException | NoSuchFieldException | SecurityException e) {
				throw new RuntimeException();
			}
		}

		// Initialize scheduler
		this.scheduler = new Scheduler(this);

		// Initialize data storage
		final StorageType storageType = Moda.instance.getStorageType();
		if (storageType == StorageType.MYSQL) {
			final DatabaseStorageHandler handler = this.getDatabaseStorageHandler();
			handler.setDatabaseHandler(Moda.db);
			handler.setup();
			this.storage = (T) handler;
		} else if (storageType == StorageType.FILE) {
			final FileStorageHandler handler = this.getFileStorageHandler();
			this.storage = (T) handler;

			// Save config periodically
			this.scheduler.interval(5*60*20, 5*60*20, () -> {
				this.scheduler.async(handler::save);
			});
		} else {
			throw new AssertionError();
		}

		this.logger.debug("Enabled");
	}

	public final void disable() {
		HandlerList.unregisterAll(this);
		Scheduler.cancelAllTasks(this);

		if (this.storage instanceof FileStorageHandler) {
			((FileStorageHandler) this.storage).save();
		}

		this.logger.debug("Disabled");
	}

}
