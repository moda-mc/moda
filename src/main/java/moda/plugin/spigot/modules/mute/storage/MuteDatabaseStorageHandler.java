package moda.plugin.spigot.modules.mute.storage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.OfflinePlayer;

import moda.plugin.spigot.Moda;
import moda.plugin.spigot.modules.Module;
import moda.plugin.spigot.utils.storage.DatabaseStorageHandler;
import xyz.derkades.derkutils.NoParameter;
import xyz.derkades.derkutils.bukkit.BukkitFuture;

public class MuteDatabaseStorageHandler extends DatabaseStorageHandler implements MuteStorageHandler {

	public MuteDatabaseStorageHandler(final Module<?> module) {
		super(module);
	}

	@Override
	public void setup() throws SQLException {
		this.db.createTableIfNonexistent("playerMuted", "CREATE TABLE `" + this.db.getDatabase() + "`.`playerMuted` "
				+ "(`uuid` VARCHAR(100) NOT NULL, `expires` BIGINT() NOT NULL, PRIMARY KEY (`uuid`)) ENGINE = InnoDB");
	}

	@Override
	public BukkitFuture<Boolean> isMuted(final OfflinePlayer player) {
		final UUID uuid = player.getUniqueId();

		return new BukkitFuture<>(Moda.instance, () -> {
			final PreparedStatement statement = this.db.prepareStatement("SELECT `expire` FROM `moda_mute` WHERE uuid=?",
					player.getUniqueId());
			final ResultSet result = statement.executeQuery();

			if (result.next()) {
				final long expireTime = result.getLong(0);
				// Mute is permanent
				if (expireTime < 0) {
					return true;
				}

				// Mute has expired
				if (expireTime < System.currentTimeMillis()) {
					this.unmuteBlocking(uuid);
					return false;
				} else {
					return true;
				}
			} else {
				// Player is not in database, not muted
				return false;
			}
		});
	}

	@Override
	public BukkitFuture<NoParameter> unmute(final OfflinePlayer player) {
		final UUID uuid = player.getUniqueId();
		return new BukkitFuture<>(Moda.instance, () -> {
			this.unmuteBlocking(uuid);
			return null;
		});
	}

	private void unmuteBlocking(final UUID uuid) throws SQLException {
		final PreparedStatement statement = this.db.prepareStatement("DELETE FROM `moda_mute` WHERE uuid=?",
				uuid);
		statement.execute();
	}

	@Override
	public BukkitFuture<NoParameter> permanentMute(final OfflinePlayer player) {
		final UUID uuid = player.getUniqueId();
		return new BukkitFuture<>(Moda.instance, () -> {
			final PreparedStatement statement = this.db.prepareStatement("INESRT INTO `moda_mute`(`uuid`, `expire`) VALUES (?, -1)",
					uuid);
			statement.execute();
			return null;
		});
	}

	@Override
	public BukkitFuture<NoParameter> tempMute(final OfflinePlayer player, final long expireTime) {
		final UUID uuid = player.getUniqueId();
		return new BukkitFuture<>(Moda.instance, () -> {
			final PreparedStatement statement = this.db.prepareStatement("INESRT INTO `moda_mute`(`uuid`, `expire`) VALUES (?, ?)",
					uuid, expireTime);
			statement.execute();
			return null;
		});
	}

}
