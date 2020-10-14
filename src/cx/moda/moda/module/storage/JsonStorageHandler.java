package cx.moda.moda.module.storage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang.Validate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import cx.moda.moda.module.Module;

public class JsonStorageHandler extends FileStorageHandler {

	private static final String UUID_VALUE_OBJECT_KEY = "moda_playerdata";
	
	private final JsonObject json;
	private final File file;
	
	public JsonStorageHandler(final Module<? extends ModuleStorageHandler> module) throws IOException {
		super(module);
		
		Validate.notNull(module, "Module is null");
		
		final File fileFileDir = new File(module.getDataFolder() + File.separator + "data");

		fileFileDir.mkdirs();

		this.file = new File(fileFileDir, "data.json");
		
		if (!this.file.exists()) {
			this.file.createNewFile();
			try (Writer writer = new FileWriter(this.file)){
				writer.write("{}");
				writer.flush();
			}
		}

		try (Reader reader = new FileReader(this.file)){
			try {
				this.json = JsonParser.parseReader(reader).getAsJsonObject();
			} catch (final ClassCastException e) {
				throw new IOException(e);
			}
		}
	}
	
	public JsonObject getJson() {
		return this.json;
	}

	@Override
	public void save() throws IOException {
		try (Writer writer = new FileWriter(this.file)){
			writer.write(getJson().toString());
		}
	}
	
	private JsonObject getUuidValueStoreObject() {
		final JsonObject json = getJson();
		if (json.has(UUID_VALUE_OBJECT_KEY)) {
			return json.getAsJsonObject(UUID_VALUE_OBJECT_KEY);
		} else {
			final JsonObject object = new JsonObject();
			json.add(UUID_VALUE_OBJECT_KEY, object);
			return object;
		}
	}

	@Override
	public Collection<UUID> getUuids() {
		return getUuidValueStoreObject().keySet().stream().map(UUID::fromString).collect(Collectors.toSet());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getProperties(final UUID uuid) {
		Validate.notNull(uuid, "UUID is null");
		final JsonObject json = getUuidValueStoreObject();
		if (json.has(uuid.toString())) {
			return new Gson().fromJson(json.get(uuid.toString()), Map.class);
		} else {
			return new HashMap<>();
		}
	}

	@Override
	public void setProperties(final UUID uuid, final Map<String, Object> properties) {
		Validate.notNull(uuid, "UUID is null");
		Validate.notNull(properties, "Properties map is null");
		final String uuidString = uuid.toString();
		if (this.json.has(uuidString)) {
			this.json.remove(uuidString);
		}
		final JsonObject json = new Gson().toJsonTree(properties).getAsJsonObject();
		getUuidValueStoreObject().add(uuidString, json);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Optional<T> getProperty(final UUID uuid, final String key) {
		Validate.notNull(uuid, "UUID is null");
		Validate.notNull(key, "Key is null");
		final String uuidString = uuid.toString();
		final JsonObject json = getUuidValueStoreObject();
		if (!json.has(uuidString)) {
			return Optional.empty();
		}
		final JsonObject player = json.get(uuidString).getAsJsonObject();
		if (!player.has(key)) {
			return Optional.empty();
		}
		final JsonPrimitive prim = player.get(key).getAsJsonPrimitive();
		
		if (prim.isBoolean()) {
			return (Optional<T>) Optional.of(prim.getAsBoolean());
		} else if (prim.isNumber()) {
			return (Optional<T>) Optional.of(prim.getAsLong());
		} else if (prim.isString()) {
			return (Optional<T>) Optional.of(prim.getAsString());
		} else {
			throw new RuntimeException("Json primitive has unexpected data type");
		}
	}

	@Override
	public <T> void setProperty(final UUID uuid, final String key, final T value) {
		Validate.notNull(uuid, "UUID is null");
		Validate.notNull(key, "Key is null");
		Validate.notNull(value, "Value is null");
		
		final String uuidString = uuid.toString();
		final JsonObject json = getUuidValueStoreObject();
		
		JsonObject player;
		if (json.has(uuidString)) {
			player = json.get(uuidString).getAsJsonObject();
		} else {
			player = new JsonObject();
		}
		
		if (value instanceof Number) {
			player.addProperty(key, (Number) value);
		} else if (value instanceof String) {
			player.addProperty(key, (String) value);
		} else if (value instanceof Character) {
			player.addProperty(key, (Character) value);
		} else if (value instanceof Boolean) {
			player.addProperty(key, (Boolean) value);
		} else {
			throw new IllegalArgumentException("Invalid data type");
		}
	}

	@Override
	public void removeProperty(final UUID uuid, final String key) {
		Validate.notNull(uuid, "UUID is null");
		Validate.notNull(key, "Key is null");
		
		throw new UnsupportedOperationException("not yet implemented");
	}

}
