package com.mineglade.moda;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ModaCommand implements CommandExecutor {

	String prefix = Moda.getPrefix();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
			
		}
		
		
		
		return true;
	}
	
	

}
