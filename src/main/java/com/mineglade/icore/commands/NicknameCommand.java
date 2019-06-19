package com.mineglade.icore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mineglade.icore.ICore;
import com.mineglade.icore.PrefixType;

import net.md_5.bungee.api.chat.ComponentBuilder;
import xyz.derkades.derkutils.bukkit.Colors;

public class NicknameCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("nicknames can only be set as a player.");
			return true;
		}
		Player player = (Player) sender;
		if (args.length == 0) {
			player.spigot().sendMessage(new ComponentBuilder("")
					.append(ICore.getPrefix(PrefixType.COMMAND))
					.append("A nickname cannot be blank, please specify a nickname.")
					.create());
		}
		if (args.length == 1) {
			String nick = args[0];
			player.spigot().sendMessage(new ComponentBuilder("")
					.append(ICore.getPrefix(PrefixType.COMMAND))
					.append("Your nickname has been set to " + Colors.parseColors(nick) + ".")
					.create());
		}
		
		return false;
	}
	
}
