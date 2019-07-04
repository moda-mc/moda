package com.mineglade.icore;

import java.io.File;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.mineglade.icore.chat.ChatEvent;
import com.mineglade.icore.chat.JoinLeaveEvent;
import com.mineglade.icore.chat.colors.ColorCommand;
import com.mineglade.icore.chat.nicknames.NickNameCommand;
import com.mineglade.icore.hooks.discord.DiscordListener;
import com.mineglade.icore.hooks.github.SuggestCommand;
import com.mineglade.icore.teleport.TeleportCommand;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import xyz.derkades.derkutils.DatabaseHandler;
import xyz.derkades.derkutils.FileUtils;

public class ICore extends JavaPlugin implements Listener {

	public static ICore instance;

	public static DatabaseHandler db = null;
	public static Economy economy = null;
	public static Permission permission = null;
	public static Chat chat = null;
	public static DiscordListener discord;

	public static FileConfiguration messages;

	public ICore() {
		instance = this;
	}

	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		final File messagesFile = new File(this.getDataFolder(), "messages.yaml");
		if (!messagesFile.exists()) {
			try {
				FileUtils.copyOutOfJar(this.getClass(), "/messages.yaml", messagesFile);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		messages = YamlConfiguration.loadConfiguration(messagesFile);
		final Logger logger = this.getLogger();

		if (ICore.instance.getConfig().getBoolean("discord.enabled")) {
			discord = new DiscordListener();
		} else {
			logger.warning("Discord is not enabled, please set up your config.");
		}

		if (!this.setupVault()) {
			logger.severe("Could not set up Vault, is it installed?");
		}
		if (ICore.instance.getConfig().getBoolean("mysql.enabled")) {
			this.initDataBaseConnection();
		} else {
			logger.warning("MySQL is not enabled, please set up your config.");
		}

		this.registerCommands();
		this.registerEvents();

		for (final Module module : Module.MODULES) {
			try {
				module.enable();
			} catch (final Exception e) {
				this.getLogger().severe("An error occured while enabling " + module.getName());
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onDisable() {
		final Logger logger = this.getLogger();

		if (db != null)
			try {
				logger.info("Shutting down database connections.");
				db.getConnection().close();
			} catch (final SQLException e1) {
				e1.printStackTrace();
			}

		if (discord != null) {
			logger.info("Shutting down JDA hook (Discord)");
			discord.shutdown();
			try {
				logger.info("Waiting 2 seconds for JDA to shut down properly before disabling the plugin.");
				Thread.sleep(2000); // Give JDA time to shut down before bukkit unloads the plugin
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (final Module module : Module.MODULES) {
			try {
				module.disable();
			} catch (final Exception e) {
				this.getLogger().severe("An error occured while disabling " + module.getName());
				e.printStackTrace();
			}
		}
	}

	private boolean setupVault() {
		if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}

		final RegisteredServiceProvider<Economy> rspEcon = this.getServer().getServicesManager()
				.getRegistration(Economy.class);

		if (rspEcon == null) {
			this.getLogger().severe("[VaultError] no Economy plugin found, is it installed?");
			return false;
		}
		economy = rspEcon.getProvider();

		final RegisteredServiceProvider<Permission> rspPerm = this.getServer().getServicesManager()
				.getRegistration(Permission.class);

		if (rspPerm == null) {
			this.getLogger().severe("[VaultError] no Permission plugin found, is it installed?");
			return false;
		}
		permission = rspPerm.getProvider();

		final RegisteredServiceProvider<Chat> rspChat = this.getServer().getServicesManager()
				.getRegistration(Chat.class);

		if (rspChat == null) {
			this.getLogger().severe("[VaultError] no Chat plugin found, is it installed?");
			return false;
		}
		chat = rspChat.getProvider();

		return true;
	}

	public static boolean isVanished(final Player player) {
		for (final MetadataValue meta : player.getMetadata("vanished")) {
			if (meta.asBoolean())
				return true;
		}
		return false;
	}

	public static String getPrefix() {
		return ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("plugin-prefix", "&7[&fi&bCore&7] "));
	}

	public void initDataBaseConnection() {

		if (db != null)
			try {
				db.getConnection().close();
			} catch (final SQLException e1) {
				e1.printStackTrace();
				Bukkit.getPluginManager().disablePlugin(this);
				return;
			}

		Bukkit.getScheduler().runTaskAsynchronously(ICore.instance, () -> {
			try {
				db = new DatabaseHandler(this.getConfig().getString("mysql.host"),
						this.getConfig().getInt("mysql.port"), this.getConfig().getString("mysql.database"),
						this.getConfig().getString("mysql.user"), this.getConfig().getString("mysql.password"));

				this.createTableIfNonexistent("playerUserName",
						"CREATE TABLE `" + this.getConfig().getString("mysql.database") + "`.`playerUserName` "
								+ "(`uuid` VARCHAR(100) NOT NULL," + " `username` VARCHAR(16) NOT NULL,"
								+ " PRIMARY KEY (`uuid`)) " + "ENGINE = InnoDB ");

				this.createTableIfNonexistent("votes",
						"CREATE TABLE `" + this.getConfig().getString("mysql.database") + "`.`votes` "
								+ "(`uuid` VARCHAR(100) NOT NULL," + " `votes` INT NOT NULL,"
								+ " PRIMARY KEY (`uuid`)) " + "ENGINE = InnoDB ");

				this.createTableIfNonexistent("playerChatColor",
						"CREATE TABLE `" + this.getConfig().getString("mysql.database") + "`.`playerChatColor` "
								+ "(`uuid` VARCHAR(100) NOT NULL," + " `color` VARCHAR(1) NOT NULL,"
								+ " PRIMARY KEY (`uuid`)) " + "ENGINE = InnoDB ");

				this.createTableIfNonexistent("playerNameColor",
						"CREATE TABLE `" + this.getConfig().getString("mysql.database") + "`.`playerNameColor` "
								+ "(`uuid` VARCHAR(100) NOT NULL," + " `color` VARCHAR(1) NOT NULL,"
								+ " PRIMARY KEY (`uuid`)) " + "ENGINE = InnoDB ");

				this.createTableIfNonexistent("playerNickName",
						"CREATE TABLE `" + this.getConfig().getString("mysql.database") + "`.`playerNickName` "
								+ "(`uuid` VARCHAR(100) NOT NULL," + " `nickname` VARCHAR(256) NOT NULL,"
								+ " PRIMARY KEY (`uuid`)) " + "ENGINE = InnoDB ");

			} catch (final SQLException e) {
				e.printStackTrace();
				Bukkit.getPluginManager().disablePlugin(this);
				return;
			}
		});
	}

	private void createTableIfNonexistent(final String table, final String sql) throws SQLException {
		final DatabaseMetaData meta = db.getConnection().getMetaData();
		final ResultSet result = meta.getTables(null, null, table, null);

		if (result != null && result.next()) {
			return; // Table exists
		}

		final PreparedStatement statement = db.prepareStatement(sql);
		statement.execute();
	}

	private void registerCommands() {
		// Core Command
		this.getCommand("icore").setExecutor(new CoreCommand());

		// Main Commands
		this.getCommand("iteleport").setExecutor(new TeleportCommand());
		this.getCommand("ping").setExecutor(new PingCommand());

		// Misc Commands
		this.getCommand("color").setExecutor(new ColorCommand());
		this.getCommand("nickname").setExecutor(new NickNameCommand());

		// Suggestions
		this.getCommand("suggest").setExecutor(new SuggestCommand());
	}

	private void registerEvents() {
		final PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new ChatEvent(), this);
		pm.registerEvents(new JoinLeaveEvent(), this);
	}
}
