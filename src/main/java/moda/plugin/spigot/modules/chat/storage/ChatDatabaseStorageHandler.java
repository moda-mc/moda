package moda.plugin.spigot.modules.chat.storage;

import java.sql.SQLException;

import moda.plugin.spigot.modules.Module;
import moda.plugin.spigot.utils.storage.DatabaseStorageHandler;

public class ChatDatabaseStorageHandler extends DatabaseStorageHandler implements ChatStorageHandler {

	public ChatDatabaseStorageHandler(final Module<?> module) {
		super(module);
	}

	@Override
	public void setup() throws SQLException {
	}
}