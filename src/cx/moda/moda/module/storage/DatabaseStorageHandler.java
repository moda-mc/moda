package cx.moda.moda.module.storage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.zaxxer.hikari.HikariDataSource;

import cx.moda.moda.Moda;
import cx.moda.moda.module.Module;

public abstract class DatabaseStorageHandler extends StorageHandler {

	private HikariDataSource dataSource = Moda.getHikariDataSource();

	public DatabaseStorageHandler(final Module<? extends ModuleStorageHandler> module) {
		super(module);
	}

	public void setDataSource(final HikariDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Connection getConnection() throws SQLException {
		return this.dataSource.getConnection();
	}

	/**
	 * Called when the module is loaded. Can be used to create tables using {@link #createTableIfNonexistent(String, String)}
	 * @throws SQLException
	 */
	public abstract void setup() throws SQLException;

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
