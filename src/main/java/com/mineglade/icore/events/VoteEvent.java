package com.mineglade.icore.events;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.mineglade.icore.ICore;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

import xyz.derkades.derkutils.bukkit.Chat;
import xyz.derkades.derkutils.bukkit.PlaceholderUtil.Placeholder;
import xyz.derkades.derkutils.bukkit.UUIDFetcher;

public class VoteEvent implements Listener {

	@EventHandler
	public void onPlayerVote(VotifierEvent event) {

		Vote vote = event.getVote();
		String player = vote.getUsername();
		FileConfiguration config = ICore.instance.getConfig();

		if (!(ICore.instance.getConfig().getBoolean("voting.database-readonly"))) {
			Bukkit.getScheduler().runTaskAsynchronously(ICore.instance, () -> {
				try {
					PreparedStatement statement = ICore.db.prepareStatement(
							"INSERT INTO votes (uuid, votes) VALUES (?, 1) ON DUPLICATE KEY UPDATE votes=votes+1",
							UUIDFetcher.getUUID(vote.getUsername()));
					statement.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
		}

		ICore.instance.getConfig().getStringList("voting.rewards")
				.forEach((c) -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), c.replace("%player%", player)));

		Placeholder voter = new Placeholder("%player%", player);
		Placeholder voteSite = new Placeholder("%vote-site%", vote.getServiceName());
		Bukkit.spigot().broadcast(Chat.toComponentWithPlaceholders(config, "voting.vote-broadcast", voter, voteSite));
	}

}