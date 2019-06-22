package com.mineglade.icore.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.mineglade.icore.ICore;
import com.mineglade.icore.PrefixType;
import com.mineglade.icore.utils.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import xyz.derkades.derkutils.ListUtils;

public class CoreCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] args) {

		if (args.length < 1) {
			Bukkit.dispatchCommand(sender, label + " help");
			return true;
		}

		else if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h")) {
				sender.sendMessage(ICore.getPrefix(PrefixType.COMMAND) + ChatColor.DARK_GRAY + "==== " + ChatColor.GREEN
						+ "All iCore Commands" + ChatColor.DARK_GRAY + " ====");
				CommandUtil.helpListEntry(sender, label, "help", "returns a list of all iCore Commands", "icore.help", "help",
						"h", "");
				CommandUtil.helpListEntry(sender, label, "support", "ask the staff team for support.", "icore.support",
						"support", "sp");
				CommandUtil.helpListEntry(sender, label, "reload", "reloads all iCore configs.", "icore.reload", "reload ",
						"rl");
		}

		else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {

			if (sender.hasPermission("icore.command.reload")) {
				ICore.instance.reloadConfig();
				sender.sendMessage(ICore.getPrefix(PrefixType.COMMAND) + ChatColor.GREEN
						+ "all iCore configs have been reloaded.");
				ICore.instance.initDataBaseConnection();
				sender.sendMessage(ICore.getPrefix(PrefixType.COMMAND) + ChatColor.GREEN
						+ "all database connections have been reloaded.");
				Bukkit.getScheduler().runTaskAsynchronously(ICore.instance, ICore.discord::restart);
				sender.sendMessage(ICore.getPrefix(PrefixType.COMMAND) + ChatColor.GREEN
						+ "iCore's JDA hook has been reloaded (Discord).");
			} else {
				sender.spigot()
						.sendMessage(new ComponentBuilder("").append(ICore.getPrefix(PrefixType.COMMAND))
								.append("You do not have permission to use this command.").color(ChatColor.RED)
								.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + label + " support "))
								.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
										new ComponentBuilder("you need ").color(ChatColor.GRAY).append("icore.reload")
												.color(ChatColor.GREEN).append(" to run this command")
												.color(ChatColor.GRAY).create()))
								.create());
			}
		}

		else if (args[0].equalsIgnoreCase("support") || args[0].equalsIgnoreCase("sp")
				|| args[0].equalsIgnoreCase("helpivefallenandicantgetup")) {
			if (args.length < 2) {
				sender.spigot()
						.sendMessage(new ComponentBuilder("")
								.append("You need to provide a description for this command")
								.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + label + " support "))
								.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("")
										.append("Usage: /" + label + " support <description>").create()))
								.create());
			} else {
				final String description = String.join(" ", ListUtils.removeFirstStringFromArray(args));
				sender.sendMessage(ICore.getPrefix(PrefixType.COMMAND) + ChatColor.GREEN
						+ "Your support query has been sent to the staff team.");
				Bukkit.broadcast(
						ICore.getPrefix(PrefixType.COMMAND) + "[Support] " + sender.getName() + ": " + description,
						"icore.command.support.receive");
			}
		}

		else {
			Bukkit.dispatchCommand(sender, label + " help");
			return true;
		}

		return true;
	}

}
