package cx.moda.moda.module;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.UnknownDependencyException;

import co.aikar.taskchain.TaskChainFactory;
import cx.moda.moda.Moda;
import cx.moda.moda.module.scheduler.ModuleTaskChainFactory;
import cx.moda.moda.module.storage.DatabaseStorageHandler;
import cx.moda.moda.module.storage.FileStorageHandler;
import cx.moda.moda.module.storage.ModuleStorageHandler;
import cx.moda.moda.module.storage.StorageMigrator;
import cx.moda.moda.module.storage.StorageType;
import cx.moda.moda.module.storage.UnsupportedStorageMigrator;
import cx.moda.moda.module.storage.UuidValueStore;
import cx.moda.moda.repo.ModuleMetaLocal;
import xyz.derkades.derkutils.AssertionException;
import xyz.derkades.derkutils.bukkit.reflection.ReflectionUtil;

public abstract class Module<T extends ModuleStorageHandler> {

	Optional<ModuleMetaLocal> meta;
	private FileConfiguration config;
	private LangFile lang;
	private ModuleLogger logger;
	private Scheduler scheduler;
	private TaskChainFactory taskChainFactory;
	private T storage;
	private UuidValueStore playerData;
	private final List<Listener> listeners =  new ArrayList<>();
	private final List<PluginCommand> commands = new ArrayList<>();
	private boolean enabled = false;

	public abstract String getName();
	
	public Optional<ModuleMetaLocal> getMeta() {
		return this.meta;
	}
	
	public boolean isEnabled() {
		return this.enabled;
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
	
	public TaskChainFactory getTaskChainFactory() {
		return this.taskChainFactory;
	}

	public T getStorage() {
		return this.storage;
	}

	public final File getDataFolder() {
		return new File("modules", this.getName());
	}

	public DatabaseStorageHandler getDatabaseStorageHandler() throws Exception {
		return null;
	}

	public FileStorageHandler getFileStorageHandler() throws Exception {
		return null;
	}
	
	public StorageMigrator<T> getStorageMigrator() {
		return new UnsupportedStorageMigrator<>();
	}

	public IMessage[] getMessages() {
		return null;
	}

	public String[] getPluginDependencies() {
		return new String[] {};
	}
	
	public UuidValueStore getPlayerData() {
		return this.playerData;
	}

	public void onDisable() throws Exception {}

	public void onEnable() throws Exception {}

	public void registerCommand(final PluginCommand command) {
		final String commandName = command.getName();
		this.logger.debug("Registering command: [name=%s, description=%s, usage=%s, aliases=%s]",
				commandName,
				command.getDescription(),
				command.getUsage(),
				String.join(".", command.getAliases().toArray(new String[] {})));
		
		Validate.isTrue(!ReflectionUtil.getKnownCommands().containsKey(commandName), "A command with this name is already registered");
		
		this.commands.add(command);
		
		ReflectionUtil.registerCommand(Moda.instance.getName() + "_" + this.getName(), command);
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

		if (this.getMeta().isPresent()) {
			final ModuleMetaLocal meta = this.getMeta().get();
			this.getLogger().debug("Enabling module " + meta.getName() + " by " + meta.getAuthor() + " version " + meta.getDownloadedVersion().getVersion());
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
		this.getDataFolder().mkdirs();
		this.initLang();
		this.initConfig();
		this.initStorage();
		
		this.taskChainFactory = ModuleTaskChainFactory.create(this);

		this.onEnable();

		this.enabled = true;
		
		if (this.getMeta().isPresent()) {
			final ModuleMetaLocal meta = this.getMeta().get();
			this.getLogger().info("Enabled module " + meta.getName() + " by " + meta.getAuthor() + " version " + meta.getDownloadedVersion().getVersion());
		} else {
			this.getLogger().info("Enabled module " + this.getName());
		}
	}

	public void disable() throws Exception {
		if (this.getMeta().isPresent()) {
			final ModuleMetaLocal meta = this.getMeta().get();
			this.getLogger().debug("Disabling module " + meta.getName() + " by " + meta.getAuthor() + " version " + meta.getDownloadedVersion().getVersion());
		} else {
			this.getLogger().debug("Disabling module " + this.getName());
		}
		
		// Catch any exceptions for later, to make sure module is always unloaded
		// properly even if disabling fails.
		Exception onDisableException = null;
		try {
			this.onDisable();
		} catch (final Exception e) {
			onDisableException = e;
		}

		this.listeners.forEach(HandlerList::unregisterAll);
		
		this.commands.forEach(ReflectionUtil::unregisterCommand);
		
		Scheduler.cancelAllTasks(this);

		if (this.storage instanceof FileStorageHandler) {
			((FileStorageHandler) this.storage).save();
		}
		
		this.taskChainFactory.shutdown(60, TimeUnit.SECONDS);
		
		this.enabled = false;

		if (this.getMeta().isPresent()) {
			final ModuleMetaLocal meta = this.getMeta().get();
			this.getLogger().info("Disabled module " + meta.getName() + " by " + meta.getAuthor() + " version " + meta.getDownloadedVersion().getVersion());
		} else {
			this.getLogger().info("Disabled module " + this.getName());
		}
		
		if (onDisableException != null) {
			throw new RuntimeException("Error occured in module onDisable", onDisableException);
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
				final File file = new File(this.getDataFolder(), "config.yaml");
				if (!file.exists()) {
					this.getLogger().debug("Config file does not exist, copying from jar file..");
					file.createNewFile();
					final InputStream input = zip.getInputStream(configYamlEntry);
					final OutputStream output = new FileOutputStream(file);
					IOUtils.copy(input, output);
				} else {
					this.getLogger().debug("Config file already exists");
				}
				
				this.config = YamlConfiguration.loadConfiguration(file);
			}
		}
	}

	private final void initStorage() throws Exception {
		final StorageType storageType = Moda.instance.getStorageType();
		if (storageType == StorageType.MYSQL) {
			initDatabaseStorage();
		} else if (storageType == StorageType.FILE) {
			initFileStorage();
		} else {
			throw new AssertionException();
		}
	}
	
	@SuppressWarnings("unchecked")
	private final void initDatabaseStorage() throws SQLException {
		this.logger.debug("Trying to use database storage");
		final DatabaseStorageHandler handler;
		try {
			handler = this.getDatabaseStorageHandler();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
		
		if (handler == null) {
			this.logger.warning("Moda is configured to use database storage but module " + this.getName() + " only supports file storage.");
			initFileStorage();
			return;
		}
		
		if (!(handler instanceof ModuleStorageHandler)) {
			throw new IllegalArgumentException("Module database storage handler must implement a ModuleStorageHandler subinterface.");
		}

		handler.setDatabaseHandler(Moda.db);
		handler.setup();
		this.storage = (T) handler;
	}
	
	@SuppressWarnings("unchecked")
	private final void initFileStorage() {
		this.logger.debug("Trying to use file storage");
		final FileStorageHandler handler;
		try {
			handler = this.getFileStorageHandler();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
		
		if (handler == null) {
			this.logger.debug("File storage does not exist");
			return;
		}
		
		if (!(handler instanceof ModuleStorageHandler)) {
			throw new IllegalArgumentException("Module file storage handler must implement a ModuleStorageHandler subinterface.");
		}
		
		this.storage = (T) handler;

		// Save config periodically
		this.scheduler.interval(5*60*20, 5*60*20, () -> {
			this.logger.debug("Saving config");
			this.getScheduler().async(() -> {
				try {
					handler.save();
					this.logger.debug("Saved config");
				} catch (final IOException e) {
					this.logger.warning("Error saving config");
					e.printStackTrace();
				}
			});
		});
	}

}
