package cx.moda.moda.repo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.Validate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ModuleMetaRepository extends ModuleMeta {
	
	private boolean autoDownload;
	private final List<ModuleMetaVersion> versions;
	
	public ModuleMetaRepository(final JsonObject json) throws InvalidMetadataException {
		super(json);
		
		Validate.notNull(json);
		
		if (json.has("versions")) {
			try {
				this.versions = new ArrayList<>();
				for (final JsonElement e : json.get("versions").getAsJsonArray()) {
					this.versions.add(new ModuleMetaVersion(e.getAsJsonObject(), this));
				}
			} catch (final ClassCastException e) {
				throw new InvalidMetadataException("'versions' key is not an array");
			}
		} else {
			throw new InvalidMetadataException("Missing 'versions' key");
		}
		
		if (json.has("auto_download")) {
			try {
				this.autoDownload = json.get("auto_download").getAsBoolean();
			} catch (final ClassCastException e) {
				throw new InvalidMetadataException("Key 'auto_download' must be a boolean");
			}
		} else {
			this.autoDownload = false;
		}
		
		if (this.versions.isEmpty()) {
			throw new InvalidMetadataException("Module must have at least one version");
		}
		
		Collections.sort(this.versions);
	}
	
	public boolean shouldAutoDownload() {
		return this.autoDownload;
	}
	
	public List<ModuleMetaVersion> getAvailableVersions() {
		return this.versions;
	}
	
	public ModuleMetaVersion getLatestVersion() {
		return this.getAvailableVersions().get(0);
	}

	public Optional<ModuleMetaVersion> getLatestVersionThatSupports(final ModuleMinecraftVersion minecraftVersion) {
		for (final ModuleMetaVersion version : this.getAvailableVersions()) {
			if (minecraftVersion.isSupported(version.getSupportedMinecraftVersionsFlag())) {
				return Optional.of(version);
			}
		}
		
		return Optional.empty();
	}

}
