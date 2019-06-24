package com.mineglade.icore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mineglade.icore.ICore;
import com.mineglade.icore.PrefixType;
import com.mineglade.icore.utils.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class ColorCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {		
		
		if (args.length < 1) {
			sender.sendMessage(ICore.getPrefix(PrefixType.PLUGIN) + ChatColor.DARK_GRAY + "==== " + ChatColor.GREEN
					+ "All iCore Commands" + ChatColor.DARK_GRAY + " ====");
			CommandUtil.helpListEntry(sender, label, "name", "Allows you to set your name color", "icore.color.name", args);
		}
		
		if (args[0] == "name") {

			return true;
		}
		
		else if (args[0] == "chat") {
			if (!(sender instanceof Player)) {
				
			}
			return true;
		}
		
		else {
			sender.spigot().sendMessage(new ComponentBuilder("")
					.append("You need to specify a type of color")
					.create());
			return false;
		}
		
	}
	
}



// 
// command: /color <chat|name> [player] <color>