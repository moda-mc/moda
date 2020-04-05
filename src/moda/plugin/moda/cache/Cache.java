package moda.plugin.moda.cache;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public interface Cache {
	
	/**
	 * Cache an object
	 * @param id
	 * @param t
	 * @param timeout Cache expire timeout in milliseconds
	 */
	void set(final Object id, final Object t, final long timeout);

	default void set(final Object id, final Object t, final long timeout, final TimeUnit unit) {
		set(id, t, unit.convert(timeout, TimeUnit.MILLISECONDS));
	}

	/**
	 * Cache an object for 1 minute. The expiration time may change in the future.
	 * @param id
	 * @param t
	 */
	default void set(final Object id, final Object t) {
		set(id, t, 1, TimeUnit.MINUTES);
	}
	
	<T> Optional<T> get(final Object id);
	
}
