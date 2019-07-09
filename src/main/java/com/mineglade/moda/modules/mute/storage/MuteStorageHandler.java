package com.mineglade.moda.modules.mute.storage;

import org.bukkit.OfflinePlayer;

import com.mineglade.moda.utils.storage.ModuleStorageHandler;

import xyz.derkades.derkutils.NoParameter;
import xyz.derkades.derkutils.bukkit.BukkitFuture;

public interface MuteStorageHandler extends ModuleStorageHandler {

	public BukkitFuture<Boolean> isMuted(OfflinePlayer player);

	public BukkitFuture<NoParameter> unmute(OfflinePlayer player);

	public BukkitFuture<NoParameter> permanentMute(OfflinePlayer player);

	public BukkitFuture<NoParameter> tempMute(OfflinePlayer player, long expireTime);

}
