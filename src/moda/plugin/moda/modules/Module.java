package moda.plugin.moda.modules;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.UnknownDependencyException;

import moda.plugin.moda.Moda;
import moda.plugin.moda.repo.ModuleMeta;
import moda.plugin.moda.utils.storage.DatabaseStorageHandler;
import moda.plugin.moda.utils.storage.FileStorageHandler;
import moda.plugin.moda.utils.storage.ModuleStorageHandler;
import moda.plugin.moda.utils.storage.StorageType;
import xyz.derkades.derkutils.AssertionException;

public abstract class Module<T extends ModuleStorageHandler> {

	ModuleMeta meta;
	private FileConfiguration config;
	private LangFile lang;
	private ModuleLogger logger;
	private Scheduler scheduler;
	private T storage;
	private final List<Listener> listeners =  new ArrayList<>();

	public abstract String getName();

	public ModuleMeta getMeta() {
		return this.meta;
	}

	public FileConfiguration getConfig() {
		return this.config;
	}

	public LangFile getLang() {
		return this.lang;
	}

	public ModuleLogger getLogger() {
		return this.logger;
	}

	public Scheduler getScheduler() {
		return this.scheduler;
	}

	public T getStorage() {
		return this.storage;
	}

	public final File getDataFolder() {
		return new File("modules", this.getName());
	}

	public DatabaseStorageHandler getDatabaseStorageHandler() {
		return null;
	}

	public FileStorageHandler getFileStorageHandler() {
		return null;
	}

	public IMessage[] getMessages() {
		return null;
	}

	public String[] getPluginDependencies() {
		return new String[] {};
	}

	public void onDisable() throws Exception {}

	public void onEnable() throws Exception {}

	public void registerCommand(final Command command) {
		this.logger.debug("Registering command: [name=%s, description=%s, usage=%s, aliases=%s]",
				command.getName(),
				command.getDescription(),
				command.getUsage(),
				String.join(".", command.getAliases().toArray(new String[] {})));
		try {
			final Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			field.setAccessible(true);
			final CommandMap map = (CommandMap) field.get(Bukkit.getServer());
			map.register(Moda.instance.getName(), command);
		} catch (final IllegalAccessException | NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	public void registerListener(final Listener listener) {
		this.listeners.add(listener);
		Bukkit.getPluginManager().registerEvents(listener, Moda.instance);
	}

	public void enable() throws Exception {
		this.initLogger();

		if (this.meta != null) {
			this.getLogger().debug("Enabling module " + this.meta.getName() + " by " + this.meta.getAuthor() + " version " + this.meta.getDownloadedVersionString());
		} else {
			this.getLogger().debug("Enabling module " + this.getName());
		}

		// Check dependencies
		for (final String dependencyString : this.getPluginDependencies()) {
			final Plugin dependency = Bukkit.getPluginManager().getPlugin(dependencyString);
			if ((dependency == null) || !dependency.isEnabled()) {
				throw new UnknownDependencyException("This module could not be enabled, because it requires the plugin " + dependencyString);
			}
		}

		this.scheduler = new Scheduler(this);
		this.initLang();
		this.initConfig();
		this.initStorage();

		this.onEnable();

		if (this.meta != null) {
			this.getLogger().info("Enabled module " + this.meta.getName() + " by " + this.meta.getAuthor() + " version " + this.meta.getDownloadedVersionString());
		} else {
			this.getLogger().info("Enabled module " + this.getName());
		}
	}

	public void disable() throws Exception {
		if (this.meta != null) {
			this.getLogger().debug("Disabling module " + this.meta.getName() + " by " + this.meta.getAuthor() + " version " + this.meta.getDownloadedVersionString());
		} else {
			this.getLogger().debug("Disabling module " + this.getName());
		}

		this.listeners.forEach(HandlerList::unregisterAll);
		Scheduler.cancelAllTasks(this);

		if (this.storage instanceof FileStorageHandler) {
			((FileStorageHandler) this.storage).saveBlocking();
		}

		if (this.storage instanceof DatabaseStorageHandler) {
			((DatabaseStorageHandler) this.storage).closeConnection();
		}

		if (this.meta != null) {
			this.getLogger().info("Disabled module " + this.meta.getName() + " by " + this.meta.getAuthor() + " version " + this.meta.getDownloadedVersionString());
		} else {
			this.getLogger().info("Disabled module " + this.getName());
		}
	}

	private final void initLogger() {
		// Initialize logger
		this.logger = new ModuleLogger(Moda.instance.getLogger(), this);
	}

	private final void initLang() throws IOException {
		// Load language file
		if (this.getMessages() != null) {
			this.logger.debug("Loading language file");
			final File langFile = new File(this.getDataFolder(), "lang.yaml");
			this.lang = new LangFile(langFile, this);
		} else {
			this.logger.debug("No IMessage array provided, skipping language file loading");
		}
	}

	private final void initConfig() throws ZipException, IOException {
		final File jarFile = new File("modules", this.getName() + ".jar");
		try (final ZipFile zip = new ZipFile(jarFile)) {
			final ZipEntry configYamlEntry = zip.getEntry("config.yaml");

			if (configYamlEntry == null) {
				this.getLogger().debug("Module jar does not contain 'config.yaml' file.");
			} else {
				final File output = new File(this.getDataFolder(), "config.yaml");
				if (!output.exists()) {
					this.getLogger().debug("Config yaml file does not exist, copying from jar file..");
					final InputStream inputStream2 = zip.getInputStream(configYamlEntry);
					final Path outputPath = Paths.get(output.toURI());
					Files.copy(inputStream2, outputPath);
				} else {
					this.getLogger().debug("Config yaml file already exists");
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private final void initStorage() throws SQLException {
		// Initialize data storage
		final StorageType storageType = Moda.instance.getStorageType();
		if (storageType == StorageType.MYSQL) {
			if (this.getDatabaseStorageHandler() != null) {
				this.logger.debug("Mysql enabled, using database");
				final DatabaseStorageHandler handler = this.getDatabaseStorageHandler();
				handler.setDatabaseHandler(Moda.db);
				handler.setup();
				this.storage = (T) handler;
			} else {
				this.logger.debug("No mysql storage handler provided, using file storage instead.");
				final FileStorageHandler handler = this.getFileStorageHandler();
				this.storage = (T) handler;

				if (handler != null) {
					// Save config periodically
					this.scheduler.interval(5*60*20, 5*60*20, () -> {
						this.logger.debug("Saving config");
						this.scheduler.async(handler::save);
					});
				}
			}
		} else if (storageType == StorageType.FILE) {
			this.logger.debug("Using file storage");
			final FileStorageHandler handler = this.getFileStorageHandler();
			this.storage = (T) handler;

			if (handler != null) {
				// Save config periodically
				this.scheduler.interval(5*60*20, 5*60*20, () -> {
					this.logger.debug("Saving config");
					this.scheduler.async(handler::save);
				});
			}
		} else {
			throw new AssertionException();
		}
	}

}
