package com.mineglade.icore.votes;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitTask;

import com.mineglade.icore.ICore;
import com.mineglade.icore.IMessage;
import com.mineglade.icore.Module;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

import xyz.derkades.derkutils.bukkit.Chat;
import xyz.derkades.derkutils.bukkit.PlaceholderUtil.Placeholder;
import xyz.derkades.derkutils.bukkit.UUIDFetcher;

public class Votes extends Module {

	private BukkitTask voteReminder;

	@Override
	public void onEnable() {
		if (!ICore.instance.getConfig().getBoolean("mysql.enabled")) {
			this.logger.severe("The voting module doesn't work without mysql enabled.");
			this.disable();
			return;
		}

		final long interval = this.config.getInt("reminder.interval") * 60 * 20;
		final long delay = this.config.getInt("reminder.delay") * 60 * 20;
		this.scheduler.interval(delay, interval, new VoteReminder());

		this.scheduler.async(() -> {
			try {
				ICore.createTableIfNonexistent("votes",
						"CREATE TABLE `" + this.plugin.getConfig().getString("mysql.database") + "`.`votes` "
								+ "(`uuid` VARCHAR(100) NOT NULL," + " `votes` INT NOT NULL,"
								+ " PRIMARY KEY (`uuid`)) " + "ENGINE = InnoDB ");

			} catch (final SQLException e) {
				e.printStackTrace();
				this.disable();
				return;
			}
		});
	}

	@Override
	public void onDisable() {
		this.voteReminder.cancel();
	}

	@EventHandler
	public void onPlayerVote(final VotifierEvent event) {
		final Vote vote = event.getVote();
		final String player = vote.getUsername();
		final FileConfiguration config = ICore.instance.getConfig();

		if (!(ICore.instance.getConfig().getBoolean("voting.database-readonly"))) {
			Bukkit.getScheduler().runTaskAsynchronously(ICore.instance, () -> {
				try {
					final PreparedStatement statement = ICore.db.prepareStatement(
							"INSERT INTO votes (uuid, votes) VALUES (?, 1) ON DUPLICATE KEY UPDATE votes=votes+1",
							UUIDFetcher.getUUID(vote.getUsername()));
					statement.execute();
				} catch (final SQLException e) {
					e.printStackTrace();
				}
			});
		}

		ICore.instance.getConfig().getStringList("voting.rewards")
				.forEach((c) -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), c.replace("%player%", player)));

		final Placeholder voter = new Placeholder("%player%", player);
		final Placeholder voteSite = new Placeholder("%vote-site%", vote.getServiceName());
		Bukkit.spigot().broadcast(Chat.toComponentWithPlaceholders(config, "voting.broadcast-message", voter, voteSite));
	}

	@Override
	public String getName() {
		return "Votes";
	}

	@Override
	public IMessage[] getMessages() {
		return VotesMessage.values();
	}

}
