package com.mineglade.moda.modules.votes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.mineglade.moda.Moda;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import xyz.derkades.derkutils.bukkit.Chat;
import xyz.derkades.derkutils.bukkit.PlaceholderUtil.Placeholder;

public class VoteCommand extends Command {

	protected VoteCommand() {
		super("vote", "returns a list of sites to vote on.", "/vote", new ArrayList<>());
	}

	@Override
	public boolean execute(final CommandSender sender, final String label, final String[] args) {

        final Player player = (Player) sender;
        final FileConfiguration config = Moda.instance.getConfig();

        if (!(Moda.instance.getConfig().getBoolean("mysql.enabled") && Moda.instance.getConfig().getBoolean("voting.enabled"))) {
        	player.spigot().sendMessage(new ComponentBuilder("")
        			.append((Moda.getPrefix()))
        			.append(ChatColor.RED + "Voting has not been enabled on this server.")
        			.create());
        	return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("top")) {
            player.sendMessage("the /vote top command is not functional yet, please be patient.");
        }
        else {
            Bukkit.getScheduler().runTaskAsynchronously(Moda.instance, () -> {
            	final int voteCount;

            	try {
            		final PreparedStatement statement = Moda.db.prepareStatement("SELECT `votes` FROM `votes` WHERE uuid=?", player.getUniqueId());
					final ResultSet result = statement.executeQuery();

					if (result.next()) {
						voteCount = result.getInt("votes");
					}
					else {
						voteCount = 0;
					}

				} catch (final SQLException e) {
					e.printStackTrace();
					return;
				}

            	Bukkit.getScheduler().runTask(Moda.instance, () -> {
            		final Placeholder votes = new Placeholder("%votes%", voteCount + "");
                    player.spigot().sendMessage(Chat.toComponentWithPapiPlaceholders(config, "voting.vote-sites", player, votes));
            	});
            });

        }



        return true;
    }

}