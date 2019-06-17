package com.mineglade.icore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShrugCommand implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("/" + label + " cannot be run from the console.");
        } else {
            Player player = (Player) sender;
            player.chat(String.join(" ", args) + " ¯\\(ツ)/¯" );
        }
        return true;
    }

}
