package com.mineglade.moda.modules.chat.storage;

import java.sql.SQLException;

import com.mineglade.moda.modules.Module;
import com.mineglade.moda.utils.storage.DatabaseStorageHandler;

public class ChatDatabaseStorageHandler extends DatabaseStorageHandler implements ChatStorageHandler {

	public ChatDatabaseStorageHandler(final Module<?> module) {
		super(module);
	}

	@Override
	public void setup() throws SQLException {
		this.db.createTableIfNonexistent("playerMuted", "");
	}

}
