package com.mineglade.icore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mineglade.icore.Main;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import xyz.derkades.derkutils.bukkit.reflection.ReflectionUtil;

public class Ping implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("You are the server. your ping is not applicable...");
			return true;
		}
		final Player player = (Player) sender;

		final int ping = ReflectionUtil.getPing(player);

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
