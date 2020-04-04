package moda.plugin.moda.utils.cache;

import java.util.Optional;

public class HashMapCache implements Cache {

	// TODO implement this ourselves instead of using derkutils API
	
	@Override
	public void set(final Object id, final Object t, final long timeout) {
		xyz.derkades.derkutils.caching.Cache.set(id.toString(), t, timeout / 1000);
	}

	@Override
	public <T> Optional<T> get(final Object id) {
		return xyz.derkades.derkutils.caching.Cache.get(id.toString());
	}
	
}
