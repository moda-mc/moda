package moda.plugin.moda;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import moda.plugin.moda.modules.Module;
import moda.plugin.moda.modules.ModuleManager;
import moda.plugin.moda.modules.ModulesConfig;
import moda.plugin.moda.repo.ModuleMeta;
import moda.plugin.moda.repo.ModuleMetaVersion;
import moda.plugin.moda.repo.ModuleMinecraftVersion;
import moda.plugin.moda.repo.Repositories;
import moda.plugin.moda.repo.Repository;
import moda.plugin.moda.utils.placeholders.ModaPlaceholderAPI;
import moda.plugin.moda.utils.storage.ModuleStorageHandler;
import moda.plugin.moda.utils.storage.StorageType;
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
				List<ModuleMeta> modules;
				try {
					modules = repo.getModules();
				} catch (final Exception e) {
					this.getLogger().warning("Failed to connect to repository " + repo.getUrl().toString() +
							": " + e.getMessage());
					continue;
				}

				for (final ModuleMeta module : modules) {
					System.out.println(String.format("[debug] module name %s default %s is downloaded %s\n", module.getName(),module.isDefault(),module.isDownloaded())); // TODO remove debug

					if (!module.isDefault()) {
						continue;
					}

					if (module.isDownloaded()) {
						continue;
					}

					try {
						final ModuleMetaVersion version = module.getLatestVersionThatSupports(Moda.minecraftVersion);
						if (version != null) {
							version.download();
						} else {
							this.getLogger().warning("The module " + module.getName() + " does not support your minecraft version");
							this.getLogger().warning("Supported versions for the latest version: " + module.getLatestVersion().getSupportedMinecraftVersionsAsCommaSeparatedString());
							continue;
						}
						modulesConfig.addModule(module.getName(), false);
						Moda.instance.getLogger().info(String.format("New module installed: '%s'", module.getName()));
					} catch (final Exception e) {
						this.getLogger().warning("Failed to download module " + module.getName());
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

	public static String getPrefix() {
		return ChatColor.translateAlternateColorCodes('&',
				instance.getConfig().getString("prefix", "&5[&dModa&5] &7"));
	}

	private void addCorePlaceholders() {
		ModaPlaceholderAPI.addPlaceholder("USERNAME", Player::getName);
		ModaPlaceholderAPI.addPlaceholder("ONLINECOUNT", player -> Bukkit.getServer().getOnlinePlayers().size());
		ModaPlaceholderAPI.addPlaceholder("DISPLAYNAME", Player::getDisplayName);
	}

//	db = new DatabaseHandler(this.getConfig().getString("mysql.host"),
//	this.getConfig().getInt("mysql.port"), this.getConfig().getString("mysql.database"),
//	this.getConfig().getString("mysql.user"), this.getConfig().getString("mysql.password"));
//
//createTableIfNonexistent("playerUserName",
//	"CREATE TABLE `" + this.getConfig().getString("mysql.database") + "`.`playerUserName` "
//			+ "(`uuid` VARCHAR(100) NOT NULL," + " `username` VARCHAR(16) NOT NULL,"
//			+ " PRIMARY KEY (`uuid`)) " + "ENGINE = InnoDB ");
//
//createTableIfNonexistent("playerChatColor",
//	"CREATE TABLE `" + this.getConfig().getString("mysql.database") + "`.`playerChatColor` "
//			+ "(`uuid` VARCHAR(100) NOT NULL," + " `color` VARCHAR(1) NOT NULL,"
//			+ " PRIMARY KEY (`uuid`)) " + "ENGINE = InnoDB ");
//
//createTableIfNonexistent("playerNameColor",
//	"CREATE TABLE `" + this.getConfig().getString("mysql.database") + "`.`playerNameColor` "
//			+ "(`uuid` VARCHAR(100) NOT NULL," + " `color` VARCHAR(1) NOT NULL,"
//			+ " PRIMARY KEY (`uuid`)) " + "ENGINE = InnoDB ");
//
//createTableIfNonexistent("playerNickName",
//	"CREATE TABLE `" + this.getConfig().getString("mysql.database") + "`.`playerNickName` "
//			+ "(`uuid` VARCHAR(100) NOT NULL," + " `nickname` VARCHAR(256) NOT NULL,"
//			+ " PRIMARY KEY (`uuid`)) " + "ENGINE = InnoDB ");
//
//}
}

