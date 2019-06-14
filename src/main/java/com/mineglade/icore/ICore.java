package com.mineglade.icore;

import com.mineglade.icore.commands.CoreCommand;
import com.mineglade.icore.commands.Ping;
import com.mineglade.icore.commands.Shrug;
import com.mineglade.icore.events.Chat;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

//import org.bukkit.plugin.RegisteredServiceProvider;

public class ICore extends JavaPlugin implements Listener {

    public static ICore instance;
    public static FileConfiguration mutedPlayers;

    public static Economy economy = null;

    public ICore() {
        instance = this;
    }

    public static String getCommandPrefix() {
        return ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("command-prefix"));
    }

    public static String getJoinLeavePrefix(final ChatColor color, final char c) {
        return ChatColor.WHITE + "[" + color + c + ChatColor.WHITE + "]" + ChatColor.GRAY + " | " + color;
    }

//    private boolean setupEconomy() {
//        if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
//            return false;
//        }
//        final RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
//        if (rsp == null) {
//            return false;
//        }
//        economy = rsp.getProvider();
//        return economy != null;
//    }

    public static String getChatPrefix() {
        return ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("chat-prefix"));
    }

    public void onEnable() {

        PluginDescriptionFile pdFile = getDescription();
        Logger logger = getLogger();

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

    public void registerCommands() {
        getCommand("ping").setExecutor(new Ping());
        getCommand("icore").setExecutor(new CoreCommand());
        getCommand("shrug").setExecutor(new Shrug());
    }

    public void registerEvents() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new Chat(), this);
    }

}
