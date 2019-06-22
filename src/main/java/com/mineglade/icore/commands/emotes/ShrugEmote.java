package com.mineglade.icore.commands.emotes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;import com.mineglade.icore.ICore;
import com.mineglade.icore.PrefixType;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class ShrugEmote implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("/" + label + " cannot be run from the console.");
            return true;
        }
        Player player = (Player) sender;
        if (!(player.hasPermission("icore.command.emotes.shrug"))) {
        	player.spigot().sendMessage(
                    new ComponentBuilder("")
                    .append(ICore.getPrefix(PrefixType.COMMAND))
                    .append("You do not have permission to use this emote.").color(ChatColor.RED)
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("you need ").color(ChatColor.GRAY)
                            .append("icore.emotes.shrug").color(ChatColor.GREEN)
                            .append(" to run this command").color(ChatColor.GRAY)
                            .create()))
                    .create());
        	return true;
        }
        if (args.length > 0) {
            player.chat(String.join(" ", args) + " ¯\\_(ツ)_/¯" );
           
        } else {
            player.chat("¯\\_(ツ)_/¯");
        }
        return true;
    }

}
