package moda.plugin.moda.module.storage;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import moda.plugin.moda.module.Module;
import xyz.derkades.derkutils.DatabaseHandler;

public abstract class DatabaseStorageHandler extends StorageHandler {

	protected DatabaseHandler db;

	public DatabaseStorageHandler(final Module<? extends ModuleStorageHandler> module) {
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
	public final void closeConnection() throws SQLException {
		this.db.getConnection().close();
	}

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
	
	@Override
	public Collection<UUID> getUuids() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, Object> getProperties(final UUID uuid) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setProperties(final UUID uuid, final Map<String, Object> properties) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> Optional<T> getProperty(final UUID uuid, final String id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> void setProperty(final UUID uuid, final String id, final T value) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void removeProperty(final UUID uuid, final String id) {
		throw new UnsupportedOperationException();
	}

}
