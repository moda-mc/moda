package com.mineglade.moda.modules.mute;

import org.bukkit.OfflinePlayer;

import com.mineglade.moda.Moda;
import com.mineglade.moda.modules.Module;
import com.mineglade.moda.utils.storage.FileStorageHandler;
import com.mineglade.moda.utils.storage.ModuleStorageHandler;

import xyz.derkades.derkutils.NoParameter;
import xyz.derkades.derkutils.bukkit.BukkitFuture;

public class MuteFileStorageHandler extends FileStorageHandler implements MuteStorageHandler {

	public MuteFileStorageHandler(final Module<? extends ModuleStorageHandler> module) {
		super(module);
	}

	@Override
	public BukkitFuture<Boolean> isMuted(final OfflinePlayer player) {
		return new BukkitFuture<>(Moda.instance, () -> {
			if (!this.file.contains(player.getUniqueId().toString())) {
				return false;
			}

			final long expireTime = this.file.getLong(player.getUniqueId().toString());

			// Mute is permanent
			if (expireTime < 0) {
				return true;
			}

			// Mute has expired
			if (expireTime < System.currentTimeMillis()) {
				this.file.set(player.getUniqueId().toString(), null);
				return false;
			} else {
				return true;
			}
		});
	}

	@Override
	public BukkitFuture<NoParameter> unmute(final OfflinePlayer player) {
		return new BukkitFuture<>(Moda.instance, () -> {
			this.file.set(player.getUniqueId().toString(), null);
			return null;
		});
	}

	@Override
	public BukkitFuture<NoParameter> permanentMute(final OfflinePlayer player) {
		return new BukkitFuture<>(Moda.instance, () -> {
			this.file.set(player.getUniqueId().toString(), -1);
			return null;
		});
	}

	@Override
	public BukkitFuture<NoParameter> tempMute(final OfflinePlayer player, final long expireTime) {
		return new BukkitFuture<>(Moda.instance, () -> {
			this.file.set(player.getUniqueId().toString(), expireTime);
			return null;
		});
	}

}
