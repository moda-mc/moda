package moda.plugin.moda;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import moda.plugin.moda.module.Module;
import moda.plugin.moda.module.ModuleManager;
import moda.plugin.moda.module.ModulesConfig;
import moda.plugin.moda.module.storage.ModuleStorageHandler;
import moda.plugin.moda.module.storage.StorageType;
import moda.plugin.moda.placeholder.ModaPlaceholder;
import moda.plugin.moda.placeholder.ModaPlaceholderAPI;
import moda.plugin.moda.placeholder.ModaPlayerPlaceholder;
import moda.plugin.moda.repo.ModuleMetaRepository;
import moda.plugin.moda.repo.ModuleMetaVersion;
import moda.plugin.moda.repo.ModuleMinecraftVersion;
import moda.plugin.moda.repo.Repositories;
import moda.plugin.moda.repo.Repository;
import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.DatabaseHandler;

public class Moda extends JavaPlugin implements Listener {

	public static Moda instance;

	public static DatabaseHandler db = null;

	public static FileConfiguration messages;

	public static ModuleMinecraftVersion minecraftVersion;

	public Moda() {
		instance = this;
	}

	@Override
	public void onLoad() {
		this.saveDefaultConfig();

		minecraftVersion = ModuleMinecraftVersion.getServerVersion();

		final File modulesDirectory = new File("modules");
		modulesDirectory.mkdirs(); // Create modules directory. It can be assumed that it exists in other code.

		// Connect to database if storage type is set to MySQL
		if (this.getStorageType().equals(StorageType.MYSQL)) {
			try {
				db = new DatabaseHandler(this.getConfig().getString("mysql.host"),
						this.getConfig().getInt("mysql.port"),
						this.getConfig().getString("mysql.database"),
						this.getConfig().getString("mysql.user"),
						this.getConfig().getString("mysql.password"));
			} catch (final SQLException e) {
				this.getLogger().severe("Initializing MySQL failed. Please configure MySQL properly or switch to file storage.");
				throw new RuntimeException(e);
			}
		}

		// Download default modules
		try (final ModulesConfig modulesConfig = new ModulesConfig()){
			for (final Repository repo : Repositories.getRepositories()) {
				List<ModuleMetaRepository> metas;
				try {
					metas = repo.getModules();
				} catch (final Exception e) {
					this.getLogger().warning("Failed to connect to repository " + repo.getUrl().toString() +
							": " + e.getMessage());
					continue;
				}

				for (final ModuleMetaRepository meta : metas) {
					if (!meta.shouldAutoDownload()) {
						continue;
					}
					
					final ModuleManager manager = ModuleManager.getInstance();

					if (manager.isDownloaded(meta.getName())) {
						continue;
					}
					
					this.getLogger().info("The repository " + repo.getUrl() + " wants module " + meta.getName() + " to be automatically installed. Downloading..");
					
					try {
						final Optional<ModuleMetaVersion> version = meta.getLatestVersionThatSupports(Moda.minecraftVersion);
						if (version.isPresent()) {
							manager.download(meta, version.get());
						} else {
							this.getLogger().warning("The module " + meta.getName() + " does not support your minecraft version");
							this.getLogger().warning("Supported versions for the latest version: " + meta.getLatestVersion().getSupportedMinecraftVersionsAsCommaSeparatedString());
							continue;
						}
						modulesConfig.addModule(meta.getName(), false);
						Moda.instance.getLogger().info(String.format("New module installed: '%s'", meta.getName()));
					} catch (final Exception e) {
						this.getLogger().warning("Failed to download module " + meta.getName());
						e.printStackTrace();
					}
				}
			}
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onEnable() {
		// Register core command
		this.getCommand("moda").setExecutor(new ModaCommand());

		// Add core placeholders.
		this.addCorePlaceholders();

		// Load all enabled modules
		try (final ModulesConfig modulesConfig = new ModulesConfig()){
			final ModuleManager manager = ModuleManager.getInstance();
			for (final String name : manager.getInstalledModulesNames()) {
				try {
					if (modulesConfig.isEnabled(name)) {
						manager.load(name);
					}
				} catch (final Exception | IllegalAccessError e) {
					this.getLogger().severe("An error occured while loading module '" + name + "'");
					e.printStackTrace();
				}
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}

		// bStats Metrics
		new Stats(this);

		// Refresh indexes async, to not slow down server startup
		Bukkit.getScheduler().runTaskLaterAsynchronously(this, () -> {
			for (final Repository repo : Repositories.getRepositories()) {
				try {
					repo.downloadIndexIfOlderThan(60*60*1000); // Download if older than 1 hour
				} catch (final Exception e) {
					this.getLogger().warning("Failed to connect to repository " + repo.getUrl().toString());
				}
			}
		}, 10*20);
	}

	@Override
	public void onDisable() {
		final ModuleManager manager = ModuleManager.getInstance();
		for (final Module<? extends ModuleStorageHandler> module : new ArrayList<>(manager.getLoadedModules())) {
			try {
				manager.unload(module.getName());
			} catch (final Exception e) {
				this.getLogger().severe("An error occured while unloading module '" + module.getName() + "'");
				e.printStackTrace();
			}
		}

		if (db != null) {
			try {
				db.getConnection().close();
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public StorageType getStorageType() {
		final String configString = this.getConfig().getString("storage.type", "file").toUpperCase();
		try {
			return StorageType.valueOf(configString);
		} catch (final IllegalArgumentException e) {
			this.getLogger()
					.severe(String.format("Invalid storage type specified: %s. Choose from: %s", configString,
							String.join(", ", (String[]) Arrays.asList(StorageType.values()).stream()
									.map((s) -> s.toString().toLowerCase()).toArray())));
			Bukkit.getPluginManager().disablePlugin(this);
			return null;
		}
	}

	/**
	 * this returns the configured (or default) moda plugin prefix
	 * @return the moda plugin prefix
	 */
	public static String getPrefix() {
		return ChatColor.translateAlternateColorCodes('&',
				instance.getConfig().getString("prefix", "&5[&dModa&5] &7"));
	}

	/**
	 * Adds the core Moda placeholders
	 */
	private void addCorePlaceholders() {
		ModaPlaceholderAPI.registerPlaceholder(new ModaPlayerPlaceholder("USERNAME", Player::getName));
		final Supplier<String> onlineCount = () -> String.valueOf(Bukkit.getServer().getOnlinePlayers().size());
		ModaPlaceholderAPI.registerPlaceholder(new ModaPlaceholder("ONLINECOUNT", onlineCount));
		ModaPlaceholderAPI.registerPlaceholder(new ModaPlayerPlaceholder("DISPLAYNAME", Player::getDisplayName));
	}

}

