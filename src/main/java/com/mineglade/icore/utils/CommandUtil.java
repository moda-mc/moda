package com.mineglade.icore.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import com.mineglade.icore.ICore;
import com.mineglade.icore.PrefixType;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CommandUtil {

	public static void helpListEntry(final CommandSender sender, final String label, final String subcommand,
			final String description, final String permission, final String... aliases) {
		sender.spigot().sendMessage(new ComponentBuilder("")
				.append(TextComponent.fromLegacyText(ICore.getPrefix(PrefixType.COMMAND))).event((HoverEvent) null)
				.append("/" + label + " " + subcommand).color(ChatColor.GREEN)
				.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + label + " " + subcommand))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("")
						.append("/" + label + " " + subcommand).bold(true).color(ChatColor.GREEN).append("\n")
						.bold(false).color(ChatColor.RESET)
						.append(TextComponent
								.fromLegacyText(ChatColor.GREEN + "  Description: " + ChatColor.GRAY + description))
						.append("\n").color(ChatColor.RESET)
						.append(TextComponent.fromLegacyText(
								ChatColor.GREEN + "  Usage: " + ChatColor.GRAY + "/" + label + " " + subcommand))
						.append("\n").color(ChatColor.RESET)
						.append(TextComponent.fromLegacyText(ChatColor.GREEN + "  Aliases: " + ChatColor.GRAY
								+ String.join(", ",
										Arrays.asList(aliases).stream().map((s) -> String.format("/%s %s", label, s))
												.collect(Collectors.toSet()))))
						.append("\n").color(ChatColor.RESET)
						.append(TextComponent
								.fromLegacyText(ChatColor.GREEN + "  Permission: " + ChatColor.WHITE + permission))
						.create()))
				.create());

	}

}
