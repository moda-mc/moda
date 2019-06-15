package com.mineglade.icore.commands;

import com.mineglade.icore.ICore;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.derkades.derkutils.bukkit.Chat;

public class VoteCommand implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {

        Player player = (Player) sender;
        FileConfiguration config = ICore.instance.getConfig();

        if (args.length == 1 && args[0].equalsIgnoreCase("top")) {
            player.sendMessage("the /vote top command is not functional yet, please be patient.");
        }
        else {
            player.spigot().sendMessage(Chat.toComponentWithPlaceholders(config.getList("vote-sites"), player));
        }



        return true;
    }

}