package com.mineglade.icore.chat.nicknames;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mineglade.icore.ICore;
import com.mineglade.icore.utils.PlayerData;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import xyz.derkades.derkutils.bukkit.Colors;

public class NickNameCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("icore.command.nickname")) {
			sender.spigot().sendMessage(new ComponentBuilder("")
					.append(ICore.getPrefix())
					.append(Colors.toComponent(ICore.messages.getString("errors.no-permission")
							.replace("{command}", "/" + label)))
					.create());
			return true;
		}
		if (args.length < 1) {
			sender.spigot().sendMessage(new ComponentBuilder("")
					.append(ICore.getPrefix())
					.append(Colors.toComponent(ICore.messages.getString("nickname.errors.improper-usage")))
					.event(new ClickEvent(Action.SUGGEST_COMMAND, "/" + label + " "))
					.append("\n")
					.append(ICore.getPrefix())
					.event((ClickEvent) null)
					.append("Usage: /" + label + " [target] <nickname>").color(ChatColor.RED)
					.event(new ClickEvent(Action.SUGGEST_COMMAND, "/" + label + " "))
					.create());
			return true;
		}

		if (args.length == 1) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + ICore.messages.getString("nickname.errors.no-target"));
				return true;
			}
			Player player = (Player) sender;
			PlayerData data = new PlayerData(player);
			String nickname = args[0];
			if (nickname.equalsIgnoreCase("reset")) {
				data.resetNickName();
				sender.spigot().sendMessage(new ComponentBuilder("")
						.append(ICore.getPrefix())
						.append(Colors.toComponent(ICore.messages.getString("nickname.reset.self")))
						.create());
				return true;			
			}
			
			if (ChatColor.stripColor(Colors.parseColors(nickname)).length() > 16) {
				sender.spigot().sendMessage(new ComponentBuilder("")
						.append(ICore.getPrefix())
						.append(Colors.toComponent(ICore.messages.getString("nickname.errors.too-long")))
						.event(new ClickEvent(Action.SUGGEST_COMMAND, "/" + label + " "))
						.create());
				return true;
			}
			
			data.setNickName(nickname, (bool) -> {
				if (bool) {
					sender.spigot().sendMessage(new ComponentBuilder("")
							.append(ICore.getPrefix())
							.append(Colors.toComponent(ICore.messages.getString("nickname.set.self")
									.replace("{nickname}", nickname)))
							.create());
				} else {
					sender.spigot().sendMessage(new ComponentBuilder("")
							.append(ICore.getPrefix())
							.append(Colors.toComponent(ICore.messages.getString("nickname.errors.exists")
									.replace("{nickname}", nickname)))
							.create());
				}
			});
			
			
		}

		if (args.length > 1) {
			String permission = "icore.command.nickname.others";
			if (!sender.hasPermission(permission)) {
				sender.spigot().sendMessage(new ComponentBuilder("")
						.append(ICore.getPrefix())
						.append(Colors.toComponent(ICore.messages.getString("errors.no-permission")
								.replace("{command}", "/" + label)))
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("")
								.append(Colors.toComponent(
										"&7you need &a" + permission + "&7 to set other people's nicknames")
										)
								.create()))
						.create());
				return true;
			}
			Player target = Bukkit.getPlayer(args[0]);
			String nickname = args[1];
			if (target == null) {
				sender.spigot().sendMessage(new ComponentBuilder("")
						.append(ICore.getPrefix())
						.append(Colors.toComponent(ICore.messages.getString("nickname.errors.target-invalid")))
						.event(new ClickEvent(Action.SUGGEST_COMMAND, "/" + label + " "))
						.create());
				return true;
			}
			
			PlayerData data = new PlayerData(target);
			if (nickname.equalsIgnoreCase("reset")) {
				data.resetNickName();
				sender.spigot().sendMessage(new ComponentBuilder("")
						.append(ICore.getPrefix())
						.append(Colors.toComponent(ICore.messages.getString("nickname.reset.others")
								.replace("{target}", target.getName())))
						.create());
				return true;
			}
			if (ChatColor.stripColor(Colors.parseColors(nickname)).length() > 16) {
				sender.spigot().sendMessage(new ComponentBuilder("")
						.append(ICore.getPrefix())
						.append(Colors.toComponent(ICore.messages.getString("nickname.errors.too-long")))
						.event(new ClickEvent(Action.SUGGEST_COMMAND, "/" + label + " "))
						.create());
				return true;
			}
			if (args.length > 2) {
				sender.spigot().sendMessage(new ComponentBuilder("")
						.append(ICore.getPrefix())
						.append(Colors.toComponent(ICore.messages.getString("nickname.errors.no-spaces")))
						.event(new ClickEvent(Action.SUGGEST_COMMAND, "/" + label + " "))
						.create());

			}

			data.setNickName(nickname, (bool) -> {
				if (bool) {
					sender.spigot().sendMessage(new ComponentBuilder("")
							.append(ICore.getPrefix())
							.append(Colors.toComponent(ICore.messages.getString("nickname.set.others")
									.replace("{nickname}", nickname)
									.replace("{target}", target.getName())))
							.create());
				} else {
					sender.spigot().sendMessage(new ComponentBuilder("")
							.append(ICore.getPrefix())
							.append(Colors.toComponent(ICore.messages.getString("nickname.errors.exists")
									.replace("{nickname}", nickname)))
							.create());
				}
			});
			
		}
		return true;
	}

}
