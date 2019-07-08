package com.mineglade.moda.modules.chat.storage;

import org.bukkit.OfflinePlayer;

import com.mineglade.moda.modules.Module;
import com.mineglade.moda.utils.storage.FileStorageHandler;

public class ChatFileStorageHandler extends FileStorageHandler implements ChatStorageHandler {

	public ChatFileStorageHandler(Module<?> module) {
		super(module);
	}

	@Override
	public boolean isMuted(OfflinePlayer player) {
		return false;
	}

	@Override
	public void setMuted(OfflinePlayer player, boolean muted) {
	}

}
