package com.mineglade.moda.teleport;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mineglade.moda.Moda;

public class TeleportCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("icore.command.teleport")) {
			sender.sendMessage(Moda.messages.getString("errors.no-permission").replace("{command}", label));
			return true;
		} 
		if (!(sender instanceof Player)) {
			sender.sendMessage("&cno.");
			return true;
		}
		Player player = (Player) sender;
		if (args.length < 1) {
			player.sendMessage(Moda.messages.getString("teleport.errors.no-target"));
			return true;
		}
		if (args.length == 1) {
			Player target = Bukkit.getPlayer(args[0]);
			if (target != null) {
				player.teleport(target);
				player.sendMessage(Moda.messages.getString("teleport.success.self").replace("{target}", target.getName()));
				return true;
			} else {
				player.sendMessage(Moda.messages.getString("teleport.errors.invalid-target").replace("{target}", args[0]));
				return true;
			} 
			
		}
		return true;
	}
	
}
