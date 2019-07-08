package com.mineglade.moda.modules.chat.storage;

import org.bukkit.OfflinePlayer;

import com.mineglade.moda.utils.storage.ModuleStorageHandler;

public interface ChatStorageHandler extends ModuleStorageHandler {

	public boolean isMuted(OfflinePlayer player);
	
	public void setMuted(OfflinePlayer player, boolean muted);
	
}
