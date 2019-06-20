package com.mineglade.icore;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.mineglade.icore.commands.CoreCommand;
import com.mineglade.icore.commands.PingCommand;
import com.mineglade.icore.commands.SuggestCommand;
import com.mineglade.icore.commands.VoteCommand;
import com.mineglade.icore.commands.emotes.LennyCommand;
import com.mineglade.icore.commands.emotes.ShrugCommand;
import com.mineglade.icore.discord.DiscordListener;
import com.mineglade.icore.events.ChatEvent;
import com.mineglade.icore.events.JoinLeaveEvent;
import com.mineglade.icore.events.VoteEvent;
import com.mineglade.icore.tasks.VoteReminder;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import xyz.derkades.derkutils.DatabaseHandler;

public class ICore extends JavaPlugin implements Listener {

	public static ICore instance;
	public static FileConfiguration mutedPlayers;

	public static DatabaseHandler db = null;
	public static Economy economy = null;
	public static Permission permission = null;
	public static Chat chat = null;
	public static DiscordListener discord;

	public ICore() {
		instance = this;
	}

	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		final PluginDescriptionFile pdFile = this.getDescription();
		final Logger logger = this.getLogger();

		if (ICore.instance.getConfig().getBoolean("discord.enabled")) {
			logger.info("Enabling JDA hook (Discord).");
			discord = new DiscordListener();
		} else {
			logger.warning("Discord is not enabled, please set up your config.");
		}

		if (!this.setupVault()) {
			logger.severe("Could not set up Vault, is it installed?");
		} else {
			logger.info("Succesfully hooked into Vault.");
		}
		if (ICore.instance.getConfig().getBoolean("mysql.enabled")) {
			logger.info("Connecting to MySQL Database");
			this.initDataBaseConnection();
		} else {
			logger.warning("MySQL is not enabled, please set up your config.");
		}
		logger.info(pdFile.getName() + " has been enabled for version " + pdFile.getVersion());
		
		this.registerCommands();
		this.registerEvents();
		if (ICore.instance.getConfig().getBoolean("voting.enabled")
				&& ICore.instance.getConfig().getBoolean("mysql.enabled")) {
			new VoteReminder().runTaskTimer(this, 20 * 60 * 10, 20 * 60 * 10);
		}
	}

	@Override
	public void onDisable() {
		final PluginDescriptionFile pdFile = this.getDescription();
		final Logger logger = this.getLogger();

		if (db != null)
			try {
				logger.info("Shutting down database connections.");
				db.getConnection().close();
			} catch (final SQLException e1) {
				e1.printStackTrace();
				Bukkit.getPluginManager().disablePlugin(this);
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
		logger.info(pdFile.getName() + " has been fully disabled (v" + pdFile.getVersion() + ")");
	}

	private boolean setupVault() {
		if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}

		final RegisteredServiceProvider<Economy> rspEcon = this.getServer().getServicesManager()
				.getRegistration(Economy.class);

		if (rspEcon == null) {
			getLogger().severe("[VaultError] no Economy plugin found, is it installed?");
			return false;
		}
		economy = rspEcon.getProvider();

		final RegisteredServiceProvider<Permission> rspPerm = this.getServer().getServicesManager()
				.getRegistration(Permission.class);

		if (rspPerm == null) {
			getLogger().severe("[VaultError] no Permission plugin found, is it installed?");
			return false;
		}
		permission = rspPerm.getProvider();

		final RegisteredServiceProvider<Chat> rspChat = this.getServer().getServicesManager()
				.getRegistration(Chat.class);

		if (rspChat == null) {
			getLogger().severe("[VaultError] no Chat plugin found, is it installed?");
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

	public static String getPrefix(final PrefixType type) {
		if (type == null) {
			throw new IllegalArgumentException();
		}
		if (type == PrefixType.CHAT) {
			return ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("prefixes.chat", ""));
		} else if (type == PrefixType.COMMAND) {
			return ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("prefixes.command", ""));
		} else if (type == PrefixType.JOIN) {
			return ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("prefixes.join", ""));
		} else if (type == PrefixType.LEAVE) {
			return ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("prefixes.leave", ""));
		} else {
			throw new AssertionError();
		}
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

				final DatabaseMetaData meta = db.getConnection().getMetaData();
				final ResultSet result = meta.getTables(null, null, "votes", null);

				if (result != null && result.next()) {
					return; // Table exists
				}

				final PreparedStatement statement = db.prepareStatement("CREATE TABLE `"
						+ this.getConfig().getString("mysql.database") + "`.`votes` " + "(`uuid` VARCHAR(100) NOT NULL,"
						+ " `votes` INT NOT NULL," + " PRIMARY KEY (`uuid`)) " + "ENGINE = InnoDB ");
				statement.execute();

			} catch (final SQLException e) {
				e.printStackTrace();
				Bukkit.getPluginManager().disablePlugin(this);
				return;
			}
		});
	}

	private void registerCommands() {
		// Misc Commands
		this.getCommand("ping").setExecutor(new PingCommand());
		// Emotes
		this.getCommand("shrug").setExecutor(new ShrugCommand());
		this.getCommand("lenny").setExecutor(new LennyCommand());
		// Core Command
		this.getCommand("icore").setExecutor(new CoreCommand());
		// Voting
		if (ICore.instance.getConfig().getBoolean("voting.enabled")
				&& ICore.instance.getConfig().getBoolean("mysql.enabled")) {
			this.getCommand("vote").setExecutor(new VoteCommand());
		}
		// Suggestions
		if (ICore.instance.getConfig().getBoolean("github.enabled")) {
			this.getCommand("suggest").setExecutor(new SuggestCommand());
		}
	}

	private void registerEvents() {
		final PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new ChatEvent(), this);
		pm.registerEvents(new JoinLeaveEvent(), this);
		if (ICore.instance.getConfig().getBoolean("voting.enabled")
				&& ICore.instance.getConfig().getBoolean("mysql.enabled")) {
			pm.registerEvents(new VoteEvent(), this);
		}
	}
}
