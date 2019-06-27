package com.mineglade.icore.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mineglade.icore.ICore;
import com.mineglade.icore.utils.PlayerData;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import xyz.derkades.derkutils.bukkit.Colors;
import xyz.derkades.derkutils.bukkit.reflection.ReflectionUtil;

public class PingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    	Player player = (Player) sender;
		if (!sender.hasPermission("icore.command.ping")) {
			sender.spigot().sendMessage(new ComponentBuilder("")
					.append(ICore.getPrefix())
					.append(Colors.toComponent(ICore.messages.getString("errors.no-permission")
							.replace("{command}", "/" + label)))
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("")
							.append(Colors.toComponent(
									"&7you need &aicore.command.ping&7 to check your ping")
									)
							.create()))
					.create());
			return true;
		}
		
		if (args.length > 0) {
			if (!sender.hasPermission("icore.command.ping.others")) {
				sender.spigot().sendMessage(new ComponentBuilder("")
						.append(ICore.getPrefix())
						.append(Colors.toComponent(ICore.messages.getString("errors.no-permission")
								.replace("{command}", "/" + label + " [target]")))
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("")
								.append(Colors.toComponent(
										"&7you need &aicore.command.ping.others&7 to check other people's ping")
										)
								.create()))
						.create());
				return true;
			}
			Player target = Bukkit.getPlayer(args[0]);
			if (target == null) {
				sender.spigot().sendMessage(new ComponentBuilder("")
						.append(ICore.getPrefix())
						.append(Colors.toComponent(ICore.messages.getString("ping.errors.target-invalid")))
						.event(new ClickEvent(Action.SUGGEST_COMMAND, "/" + label + " "))
						.create());
				return true;
			}
			PlayerData data = new PlayerData(target);
			String targetNickName = data.getNickName();
			final int ping = ReflectionUtil.getPing(target);
			player.spigot().sendMessage(new ComponentBuilder("")
					.append(ICore.getPrefix())
					.append(Colors.toComponent(ICore.messages.getString("ping.responses.others.ping")
							.replace("{ping}", ping + "")
							.replace("{target_nickname}", targetNickName)))
					.create());
			return true;
		}

        final int ping = ReflectionUtil.getPing(player);

        if (label.equalsIgnoreCase("hi")) {
            player.spigot().sendMessage(
                    new ComponentBuilder("")
                    .append(ICore.getPrefix())
                    .append(Colors.toComponent(ICore.messages.getString("ping.responses.self.hi").replace("{ping}", ping + "")))
                    .create());
            return true;
		}
		player.spigot().sendMessage(new ComponentBuilder("")
				.append(ICore.getPrefix())
				.append(Colors.toComponent(ICore.messages.getString("ping.responses.self.ping").replace("{ping}", ping + "")))
				.create());
        return true;
    }
}
