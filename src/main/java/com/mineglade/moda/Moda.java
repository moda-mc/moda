package com.mineglade.moda;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.mineglade.moda.modules.Module;
import com.mineglade.moda.utils.placeholders.ModaPlaceholderAPI;
import com.mineglade.moda.utils.storage.ModuleStorageHandler;
import com.mineglade.moda.utils.storage.StorageType;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.DatabaseHandler;

public class Moda extends JavaPlugin implements Listener {

	public static Moda instance;

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
				//e.printStackTrace();
				//Bukkit.getPluginManager().disablePlugin(this);
				//return;
				throw new RuntimeException(e);
			}
		}

		// Load all internal modules
		for (final Module<?> module : Module.INTERNAL_MODULES) {
			try {
				Module.loadInternal(module);
			} catch (final Exception e) {
				this.getLogger().severe("An error occured while loading internal module '" + module.getName() + "'");
				e.printStackTrace();
			}
		}

		// Load all external modules
		final File modulesDirectory = new File(this.getDataFolder(), "modules");
		modulesDirectory.mkdirs();
		final Set<File> jarFiles = Arrays.asList(modulesDirectory.listFiles()).stream().filter((f) -> f.getAbsolutePath().endsWith(".jar")).collect(Collectors.toSet());
		for (final File jarFile : jarFiles) {
			try {
				Module.loadExternal(jarFile);
			} catch (final Exception e) {
				this.getLogger().severe("An error occured while loading external module '" + jarFile.getPath() + "'");
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

		final File modulesConfigFile = new File(Moda.instance.getDataFolder(), "modules.yaml");
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
		final Metrics metrics = new Metrics(this);
		metrics.addCustomChart(new Metrics.AdvancedPie("enabled_modules", null));
	}

	@Override
	public void onDisable() {
		for (final Module<? extends ModuleStorageHandler> module : Module.ENABLED) {
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

