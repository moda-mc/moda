package moda.plugin.moda.utils.storage;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface UuidValueStore {
	
	public Collection<UUID> getUuids();
	
	public Map<String, Object> getProperties(UUID uuid);

	public void setProperties(UUID uuid, Map<String, Object> properties);
	
	public <T> Optional<T> getProperty(UUID uuid, String key);
	
	public <T> void setProperty(UUID uuid, String key, T value);
	
	public void removeProperty(UUID uuid, String id);

}
