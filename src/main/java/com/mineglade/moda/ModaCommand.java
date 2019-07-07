package com.mineglade.moda;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ModaCommand extends Command {
	
	static ArrayList<String> aliases = new ArrayList<String>();
	
	public static void main(){		
		ModaCommand.aliases.add("m");
	}
	
	protected ModaCommand() {
		super("moda", "The main command for Moda.", "/moda [subcommand]", aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		
		Bukkit.broadcastMessage(sender + ", " + commandLabel + ", " + args);
		return true;
	}

}
