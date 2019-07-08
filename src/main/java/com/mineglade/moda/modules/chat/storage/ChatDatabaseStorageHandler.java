package com.mineglade.moda.modules.chat.storage;

import java.sql.SQLException;

import org.bukkit.OfflinePlayer;

import com.mineglade.moda.modules.Module;
import com.mineglade.moda.utils.storage.DatabaseStorageHandler;

public class ChatDatabaseStorageHandler extends DatabaseStorageHandler implements ChatStorageHandler {

	public ChatDatabaseStorageHandler(Module<?> module) {
		super(module);
	}

	@Override
	public void setup() throws SQLException {
		db.createTableIfNonexistent("playerMuted", "");
	}

	@Override
	public boolean isMuted(OfflinePlayer player) {
		return false;
	}

	@Override
	public void setMuted(OfflinePlayer player, boolean muted) {
	}

}
