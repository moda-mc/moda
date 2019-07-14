package moda.plugin.spigot.utils.storage;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import moda.plugin.spigot.modules.Module;
import xyz.derkades.derkutils.DatabaseHandler;

public abstract class DatabaseStorageHandler extends StorageHandler {

	protected DatabaseHandler db;

	public DatabaseStorageHandler(final Module<?> module) {
		super(module);
	}

	public void setDatabaseHandler(final DatabaseHandler handler) {
		this.db = handler;
	}

	/**
	 * Called when the module is loaded. Can be used to create tables using {@link #createTableIfNonexistent(String, String)}
	 * @throws SQLException
	 */
	public abstract void setup() throws SQLException;

	@Deprecated
	protected void createTableIfNonexistent(final String table, final String sql) throws SQLException {
		final DatabaseMetaData meta = this.db.getConnection().getMetaData();
		final ResultSet result = meta.getTables(null, null, table, null);

		if (result != null && result.next()) {
			return; // Table exists
		}

		result.close();

		final PreparedStatement statement = this.db.prepareStatement(sql);
		statement.execute();
		statement.close();
	}

}
