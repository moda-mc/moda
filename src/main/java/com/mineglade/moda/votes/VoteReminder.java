package com.mineglade.moda.votes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.mineglade.moda.Moda;

import xyz.derkades.derkutils.bukkit.Chat;
import xyz.derkades.derkutils.bukkit.PlaceholderUtil.Placeholder;

public class VoteReminder implements Runnable {

	@Override
	public void run() {
		final FileConfiguration config = Moda.instance.getConfig();
		for (final Player player : Bukkit.getOnlinePlayers()) {

			Bukkit.getScheduler().runTaskAsynchronously(Moda.instance, () -> {
				final int voteCount;

				try {
					final PreparedStatement statement = Moda.db.prepareStatement("SELECT `votes` FROM `votes` WHERE uuid=?",
							player.getUniqueId());
					final ResultSet result = statement.executeQuery();

					if (result.next()) {
						voteCount = result.getInt("votes");
					} else {
						voteCount = 0;
					}

				} catch (final SQLException e) {
					e.printStackTrace();
					return;
				}

				Bukkit.getScheduler().runTask(Moda.instance, () -> {
					final Placeholder votes = new Placeholder("%votes%", voteCount + "");
					player.spigot().sendMessage(
							Chat.toComponentWithPapiPlaceholders(config, "voting.reminder.message", player, votes));
				});
			});
		}

	}

}
