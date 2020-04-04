package moda.plugin.moda.utils.cache;

import java.util.Optional;

public class RedisCache implements Cache {

	@Override
	public void set(final Object id, final Object t, final long timeout) {
		// TODO Implementation
	}

	@Override
	public <T> Optional<T> get(final Object id) {
		// TODO Implementation
		return Optional.empty();
	}

}
