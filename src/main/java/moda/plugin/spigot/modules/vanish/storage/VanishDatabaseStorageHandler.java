package moda.plugin.spigot.modules.vanish.storage;

import java.sql.SQLException;

import moda.plugin.spigot.modules.Module;
import moda.plugin.spigot.utils.storage.DatabaseStorageHandler;

public class VanishDatabaseStorageHandler extends DatabaseStorageHandler implements VanishStorageHandler {

	public VanishDatabaseStorageHandler(final Module<?> module) {
		super(module);
	}

	@Override
	public void setup() throws SQLException {
		this.db.createTableIfNonexistent("playerVanished", "");
	}

}
