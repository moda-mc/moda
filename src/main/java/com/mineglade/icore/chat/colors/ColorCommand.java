package com.mineglade.icore.chat.colors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ColorCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {		
		
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length < 1) {
				new ColorPickerMenu(player, player).open();
			}
		} else {
			sender.sendMessage("this command is not functional for the console yet.");
		}
		return true;
	}
	
}