package com.mineglade.icore.chat.colors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mineglade.icore.ICore;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import xyz.derkades.derkutils.bukkit.Colors;

public class ColorCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {		
		
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length < 1) {
				new ColorMenu(player, player).open();
			} else {
				Player target = Bukkit.getPlayer(args[0]);
				if (target == null) {
					sender.spigot().sendMessage(new ComponentBuilder("")
							.append(ICore.getPrefix())
							.append(Colors.toComponent(ICore.messages.getString("color.errors.target-invalid")))
							.event(new ClickEvent(Action.SUGGEST_COMMAND, "/" + label + " "))
							.create());
					return true;
				}
				new ColorMenu(player, target).open();
			}
		} else {
			sender.sendMessage("this command is not functional for the console yet.");
		}
		return true;
	}
	
}