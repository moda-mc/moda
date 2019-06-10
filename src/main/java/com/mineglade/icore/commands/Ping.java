package com.mineglade.icore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mineglade.icore.Main;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;

public class Ping implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("You are the server. your ping is not applicable...");
			return true;
		}
		Player player = (Player) sender;
		int ping = ((CraftPlayer) player).getHandle().ping;
		if (label.equalsIgnoreCase("hi")) {
			player.spigot().sendMessage(
					new ComponentBuilder("")
					.append(TextComponent.fromLegacyText(Main.getCommandPrefix()))
					.append("Hi there! (").color(ChatColor.GRAY)
					.append(ping + "ms").color(ChatColor.GREEN)
					.append(")").color(ChatColor.GRAY)
					.create());
		} else {
			player.spigot().sendMessage(
					new ComponentBuilder("")
					.append(TextComponent.fromLegacyText(Main.getCommandPrefix()))
					.append("Your ping to the server is ").color(ChatColor.GRAY)
					.append(ping + "ms").color(ChatColor.GREEN)
					.create());
			
		}
		return true;
	}
}
