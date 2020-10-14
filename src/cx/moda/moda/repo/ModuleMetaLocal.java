package cx.moda.moda.repo;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.apache.commons.lang.Validate;

import com.google.gson.JsonObject;

public class ModuleMetaLocal extends ModuleMeta {
	
	private ModuleMetaVersion downloadedVersion;
	private Optional<URL> repository;
	
	public ModuleMetaLocal(final JsonObject json) throws InvalidMetadataException, FileNotFoundException {
		super(json);
		
		Validate.notNull(json);
		
		if (json.has("downloaded_version")) {
			try {
				this.downloadedVersion = new ModuleMetaVersion(json.get("website").getAsJsonObject(), this);
			} catch (final ClassCastException e) {
				throw new InvalidMetadataException("Key 'downloaded_version' is not a json object");
			}
		} else {
			throw new InvalidMetadataException("Missing key 'downloaded_version'");
		}
		
		if (json.has("repository")) {
			try {
				this.repository = Optional.of(new URL(json.get("repository").getAsString()));
			} catch (ClassCastException | MalformedURLException e) {
				throw new InvalidMetadataException("Website URL invalid");
			}
		} else {
			this.repository = Optional.empty();
		}
	}
	
	public ModuleMetaVersion getDownloadedVersion() {
		return this.downloadedVersion;
	}
	
	public Optional<URL> getRepository() {
		return this.repository;
	}
	
	

}
