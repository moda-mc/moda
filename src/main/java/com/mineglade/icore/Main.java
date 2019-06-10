package com.mineglade.icore;

import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.mineglade.icore.commands.Icore;
import com.mineglade.icore.commands.Ping;
import com.mineglade.icore.commands.Shrug;
import com.mineglade.icore.events.Chat;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener {
	
	public static Main instance;
	public static FileConfiguration mutedPlayers;

	public Main() {
		instance = this;
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
	
//	private boolean initVault() {
//		if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
//            return false;
//        }
//		return true;
//		
//	}
	
	public void registerCommands() {
		getCommand("ping").setExecutor(new Ping());
		getCommand("icore").setExecutor(new Icore());
		getCommand("shrug").setExecutor(new Shrug());
	}
	
	public void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(new Chat(), this);
	}
	
	public static String getCommandPrefix() {
		return ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("command-prefix"));
	}
	
	public static String getChatPrefix() {
		return ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("chat-prefix"));
	}
	
}
