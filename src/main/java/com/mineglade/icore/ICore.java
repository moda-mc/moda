package com.mineglade.icore;

import com.mineglade.icore.commands.CoreCommand;
import com.mineglade.icore.commands.PingCommand;
import com.mineglade.icore.commands.ShrugCommand;
import com.mineglade.icore.commands.VoteCommand;
import com.mineglade.icore.events.ChatEvent;
import com.mineglade.icore.events.JoinLeaveEvent;
import com.mineglade.icore.events.VoteEvent;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

//import org.bukkit.plugin.RegisteredServiceProvider;

public class ICore extends JavaPlugin implements Listener {

    public static ICore instance;
    public static FileConfiguration mutedPlayers;

    public static Economy economy = null;
    public static Permission permission = null;
    public static Chat chat = null;

    public ICore() {
        instance = this;
    }

    public static String getPrefix(PrefixType type) {
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

    public void onEnable() {

        PluginDescriptionFile pdFile = getDescription();
        Logger logger = getLogger();

        if (!this.setupVault()) {
            this.getLogger().severe("Vault error");
        }
        registerCommands();
        registerEvents();
        logger.info(pdFile.getName() + " has been enabled for version " + pdFile.getVersion());
        saveDefaultConfig();


    }

    public void onDisable() {

        PluginDescriptionFile pdFile = getDescription();
        Logger logger = getLogger();

        logger.info(pdFile.getName() + " has been disabled (v" + pdFile.getVersion() + ")");

    }

    private boolean setupVault() {
        if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        final RegisteredServiceProvider<Economy> rspEcon = this.getServer().getServicesManager().getRegistration(Economy.class);
        if (rspEcon == null) {
            return false;
        }
        economy = rspEcon.getProvider();

        final RegisteredServiceProvider<Permission> rspPerm = this.getServer().getServicesManager().getRegistration(Permission.class);
        if (rspPerm == null) {
            return false;
        }
        permission = rspPerm.getProvider();

        final RegisteredServiceProvider<Chat> rspChat = this.getServer().getServicesManager().getRegistration(Chat.class);
        if (rspChat == null) {
            return false;
        }
        chat = rspChat.getProvider();

        return true;
    }

    public static boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }

    private void registerCommands() {
        getCommand("ping").setExecutor(new PingCommand());
        getCommand("icore").setExecutor(new CoreCommand());
        getCommand("shrug").setExecutor(new ShrugCommand());
        getCommand("vote").setExecutor(new VoteCommand());
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new ChatEvent(), this);
        pm.registerEvents(new JoinLeaveEvent(), this);
        pm.registerEvents(new VoteEvent(), this);
    }
}
