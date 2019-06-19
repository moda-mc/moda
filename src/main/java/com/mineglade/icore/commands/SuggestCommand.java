package com.mineglade.icore.commands;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import com.mineglade.icore.ICore;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.Chat;
import xyz.derkades.derkutils.bukkit.PlaceholderUtil.Placeholder;

public class SuggestCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Player player = (Player) sender;
		FileConfiguration config = ICore.instance.getConfig();
		
		if (args.length < 3) {
			player.sendMessage("Please provide a longer description");
			return true;
		}
		
		Bukkit.getScheduler().runTaskAsynchronously(ICore.instance, () -> {
			try {
				final String user = config.getString("github.username");
				final String pass = config.getString("github.password");
				final GitHub github = GitHub.connectUsingPassword(user, pass);
				final GHRepository repo = github.getRepository(config.getString("github.repository"));
				final String description = String.join(" ", args);
				final GHIssue issue = repo.createIssue("[" + player.getName() + "] " + description)
						.body(description).create();
				issue.addLabels(repo.getLabel("enhancement"));
				final Placeholder url = new Placeholder("%url%", issue.getUrl() + "");
				player.spigot().sendMessage(Chat.toComponentWithPlaceholders(config, "github.response", url));;
			} catch (final IOException e) {
				player.sendMessage(config.getString("command-prefix") + ChatColor.RED + "there was an issue posting your suggestion.");
				e.printStackTrace();
			}
		});
		
		return true;
	}

}
