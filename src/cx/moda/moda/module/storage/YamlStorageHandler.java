package cx.moda.moda.module.storage;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import cx.moda.moda.module.Module;

public class YamlStorageHandler extends FileStorageHandler {

	private static final String UUID_VALUE_PATH = "moda_playerdata";

	private final FileConfiguration yaml;
	private final File file;

	public YamlStorageHandler(final Module<? extends ModuleStorageHandler> module) throws IOException {
		super(module);

		Validate.notNull(module, "Module is null");

		final File fileFileDir = new File(module.getDataFolder() + File.separator + "data");

		fileFileDir.mkdirs();

		this.file = new File(fileFileDir, "data.yaml");

		this.yaml = YamlConfiguration.loadConfiguration(this.file);
	}

	public FileConfiguration getYaml() {
		return this.yaml;
	}

	@Override
	public void save() throws IOException {
		this.yaml.save(this.file);
	}

	public ConfigurationSection uuidValueSection() {
		if (getYaml().isConfigurationSection(UUID_VALUE_PATH)) {
			return getYaml().getConfigurationSection(UUID_VALUE_PATH);
		} else {
			return getYaml().createSection(UUID_VALUE_PATH);
		}
	}

	@Override
	public Collection<UUID> getUuids() {
		return uuidValueSection().getKeys(false).stream().map(UUID::fromString).collect(Collectors.toSet());
	}

	@Override
	public Map<String, Object> getProperties(final UUID uuid) {
		Validate.notNull(uuid, "UUID is null");

		final Map<String, Object> map = new HashMap<>();

		if (uuidValueSection().isConfigurationSection(uuid.toString())) {
			for (final String key : uuidValueSection().getKeys(false)) {
				final Object value = uuidValueSection().get(uuid.toString() + "." + key);
				map.put(key, value);
			}
		}

		return map;
	}

	@Override
	public void setProperties(final UUID uuid, final Map<String, Object> properties) {
		Validate.notNull(uuid, "UUID is null");
		Validate.notNull(properties, "Properties map is null");

		if (uuidValueSection().isConfigurationSection(uuid.toString())) {
			uuidValueSection().set(uuid.toString(), null);
		}

		final ConfigurationSection section = uuidValueSection().createSection(uuid.toString());

		properties.forEach((key, value) -> section.set(key, value));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Optional<T> getProperty(final UUID uuid, final String key) {
		Validate.notNull(uuid, "UUID is null");
		Validate.notNull(key, "Key is null");

		if (!uuidValueSection().isConfigurationSection(uuid.toString())) {
			return Optional.empty();
		}

		final ConfigurationSection section = uuidValueSection().getConfigurationSection(uuid.toString());
		final Object o = section.get(key);
		if (o == null) {
			return Optional.empty();
		} else {
			return Optional.of((T) o);
		}
	}

	@Override
	public <T> void setProperty(final UUID uuid, final String key, final T value) {
		Validate.notNull(uuid, "UUID is null");
		Validate.notNull(key, "Key is null");
		Validate.notNull(value, "Value is null");

		ConfigurationSection section;
		if (uuidValueSection().isConfigurationSection(uuid.toString())) {
			section = uuidValueSection().getConfigurationSection(uuid.toString());
		} else {
			section = uuidValueSection().createSection(uuid.toString());
		}

		section.set(key, value);
	}

	@Override
	public void removeProperty(final UUID uuid, final String key) {
		Validate.notNull(uuid, "UUID is null");
		Validate.notNull(key, "Key is null");

		final ConfigurationSection section = uuidValueSection();
		if (section.isConfigurationSection(uuid.toString())) {
			section.getConfigurationSection(uuid.toString()).set(key, null);
		}
	}

}
