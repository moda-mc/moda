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
import com.mineglade.icore.commands.ShrugCommand;
import com.mineglade.icore.commands.VoteCommand;
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
        
        Bukkit.getScheduler().runTaskAsynchronously(this, DiscordListener::new);

        if (!this.setupVault()) {
            this.getLogger().severe("Vault error");
        }
        
        this.initDataBaseConnection();
        this.registerCommands();
        this.registerEvents();
        logger.info(pdFile.getName() + " has been enabled for version " + pdFile.getVersion());
        
        new VoteReminder().runTaskTimer(this, 20*60*10, 20*60*10);
    }

    @Override
	public void onDisable() {
        final PluginDescriptionFile pdFile = this.getDescription();
        final Logger logger = this.getLogger();

        if (db != null)
			try {
				db.getConnection().close();
			} catch (final SQLException e1) {
				e1.printStackTrace();
				Bukkit.getPluginManager().disablePlugin(this);
			}

        logger.info(pdFile.getName() + " has been disabled (v" + pdFile.getVersion() + ")");

        discord.shutdown();
    }

    private boolean setupVault() {
        if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        final RegisteredServiceProvider<Economy> rspEcon = this.getServer().getServicesManager()
        		.getRegistration(Economy.class);

        if (rspEcon == null) {
            return false;
        }
        economy = rspEcon.getProvider();

        final RegisteredServiceProvider<Permission> rspPerm = this.getServer().getServicesManager()
        		.getRegistration(Permission.class);

        if (rspPerm == null) {
            return false;
        }
        permission = rspPerm.getProvider();

        final RegisteredServiceProvider<Chat> rspChat = this.getServer().getServicesManager()
        		.getRegistration(Chat.class);

        if (rspChat == null) {
            return false;
        }
        chat = rspChat.getProvider();

        return true;
    }

    public static boolean isVanished(final Player player) {
        for (final MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }

    public static String getPrefix(final PrefixType type) {
        if (type == null) {
            throw new IllegalArgumentException();
        }
        if (type == PrefixType.CHAT ) {
            return ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("chat-prefix", ""));
        } else if (type == PrefixType.COMMAND) {
            return ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("command-prefix", ""));
        } else if (type == PrefixType.JOIN) {
            return ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("join-prefix", ""));
        } else if (type == PrefixType.LEAVE) {
            return ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("leave-prefix", ""));
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
    			db = new DatabaseHandler(
    					this.getConfig().getString("mysql.host"),
    					this.getConfig().getInt("mysql.port"),
    					this.getConfig().getString("mysql.database"),
    					this.getConfig().getString("mysql.user"),
    					this.getConfig().getString("mysql.password")
    					);

    			final DatabaseMetaData meta = db.getConnection().getMetaData();
                final ResultSet result = meta.getTables(null, null, "votes", null);

                if (result != null && result.next()) {
                    return; // Table exists
                }

    			final PreparedStatement statement = db.prepareStatement(
    					"CREATE TABLE `" + this.getConfig().getString("mysql.database") + "`.`votes` "
    					+ "(`uuid` VARCHAR(100) NOT NULL,"
    					+ " `votes` INT NOT NULL,"
    					+ " PRIMARY KEY (`uuid`)) "
    					+ "ENGINE = InnoDB ");
    			statement.execute();

    		} catch (final SQLException e) {
    			e.printStackTrace();
    			Bukkit.getPluginManager().disablePlugin(this);
    			return;
    		}
        });
    }

    private void registerCommands() {
        this.getCommand("ping").setExecutor(new PingCommand());
        this.getCommand("icore").setExecutor(new CoreCommand());
        this.getCommand("shrug").setExecutor(new ShrugCommand());
        this.getCommand("vote").setExecutor(new VoteCommand());
    }

    private void registerEvents() {
        final PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new ChatEvent(), this);
        pm.registerEvents(new JoinLeaveEvent(), this);
        pm.registerEvents(new VoteEvent(), this);
    }
}
