package moda.plugin.moda;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import moda.plugin.moda.modules.Module;
import moda.plugin.moda.repo.ModuleDownloader;
import moda.plugin.moda.repo.Repositories;
import moda.plugin.moda.repo.Repository;
import moda.plugin.moda.repo.ModuleMeta;
import moda.plugin.moda.utils.placeholders.ModaPlaceholderAPI;
import moda.plugin.moda.utils.storage.ModuleStorageHandler;
import moda.plugin.moda.utils.storage.StorageType;
import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.DatabaseHandler;

public class Moda extends JavaPlugin implements Listener {

	public static Moda instance;

	public static List<Repository> repositories;

	public static DatabaseHandler db = null;

	public static FileConfiguration messages;

	public Moda() {
		instance = this;
	}

	@Override
	public void onLoad() {
		this.saveDefaultConfig();

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

		// Initialize repositories
		Moda.repositories = Repositories.getRepositories();

		final File modulesDirectory = new File("modules");
		modulesDirectory.mkdirs();

		for (final Repository repo : repositories) {
			try {
				for (final ModuleMeta module : repo.getModules()) {
					if (module.isDefault()) {
						try {
							final File moduleJarFile = new File(modulesDirectory, module.getName() + ".jar");
							if (!moduleJarFile.exists()) {
								this.getLogger().info("Default module " + module.getName() + " is not installed, downloading..");
								new ModuleDownloader(module).download(moduleJarFile);
							}
						} catch (final Exception e) {
							this.getLogger().warning("Failed to download module " + module.getName() + " from " +
									module.getDownloadURL().toString());
							e.printStackTrace();
						}
					}
				}
			} catch (final Exception e) {
				this.getLogger().warning("Failed to connect to repository " + repo.getUrl().toString());
				e.printStackTrace();
			}
		}

		// Load all modules

		final Set<File> jarFiles = Arrays.asList(modulesDirectory.listFiles()).stream().filter((f) -> f.getAbsolutePath().endsWith(".jar")).collect(Collectors.toSet());
		for (final File jarFile : jarFiles) {
			try {
				Module.load(jarFile);
			} catch (final Exception e) {
				this.getLogger().severe("An error occured while loading module '" + jarFile.getPath() + "'");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onEnable() {
		// Register core command
		this.getCommand("moda").setExecutor(new ModaCommand());

		// Add core placeholders.
		this.addCorePlaceholders();

		final File modulesConfigFile = new File("modules", "modules.yaml");
		final FileConfiguration modulesConfig = YamlConfiguration.loadConfiguration(modulesConfigFile);

		// Enable all loaded modules that are enabled in modules.yaml
		for (final Module<? extends ModuleStorageHandler> module : Module.LOADED) {
			final boolean external = module.isExternal();
			if (!modulesConfig.contains(module.getName())) {
				Moda.instance.getLogger().info(String.format("New module installed: '%s'", module.getName()));
				modulesConfig.set(module.getName(), external); // Disable internal modules by default, enable external modules by default

			}

			if (modulesConfig.getBoolean(module.getName())) {
				try {
					module.enable();
				} catch (final Exception e) {
					this.getLogger().severe("An error occured while enabling module " + module.getName());
					e.printStackTrace();
				}
			}
		}

		try {
			modulesConfig.save(modulesConfigFile);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		// bStats Metrics
		new Stats(this);

		Bukkit.getScheduler().runTaskLaterAsynchronously(this, () -> {
			for (final Repository repo : repositories) {
				try {
					repo.download();
				} catch (final Exception e) {
					this.getLogger().warning("Failed to connect to repository " + repo.getUrl().toString());
					e.printStackTrace();
				}
			}
		}, 10*20);
	}

	@Override
	public void onDisable() {
		for (final Module<? extends ModuleStorageHandler> module : new ArrayList<>(Module.ENABLED)) {
			try {
				module.disable();
			} catch (final Exception e) {
				this.getLogger().severe("An error occured while disabling module '" + module.getName() + "'");
				e.printStackTrace();
			}
		}

		if (db != null) {
			try {
				db.getConnection().close();
			} catch (final SQLException e1) {
				e1.printStackTrace();
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
				instance.getConfig().getString("plugin-prefix", "&7[&bModa&7] "));
	}

	private void addCorePlaceholders() {
		ModaPlaceholderAPI.addPlaceholder("PLAYER", Player::getName);
		ModaPlaceholderAPI.addPlaceholder("ONLINE", player -> Bukkit.getServer().getOnlinePlayers().size());
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

