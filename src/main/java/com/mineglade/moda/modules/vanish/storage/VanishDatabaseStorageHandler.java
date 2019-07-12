package com.mineglade.moda.modules.vanish.storage;

import java.sql.SQLException;

import com.mineglade.moda.modules.Module;
import com.mineglade.moda.utils.storage.DatabaseStorageHandler;

public class VanishDatabaseStorageHandler extends DatabaseStorageHandler implements VanishStorageHandler {

	public VanishDatabaseStorageHandler(final Module<?> module) {
		super(module);
	}

	@Override
	public void setup() throws SQLException {
		this.db.createTableIfNonexistent("playerVanished", "");
	}

}
