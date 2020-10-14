package cx.moda.moda.repo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import com.google.gson.JsonObject;

public abstract class ModuleMeta {

	private String name;
	private String description;
	private String author;
	private Optional<URL> source;
	private Optional<URL> website;
	
	public ModuleMeta(final JsonObject json) throws InvalidMetadataException {
		if (json.has("name")) {
			try {
				this.name = json.get("name").getAsString();
			} catch (final ClassCastException e) {
				throw new InvalidMetadataException("Key 'name' has wrong type");
			}
		} else {
			throw new InvalidMetadataException("Missing key 'name'");
		}
		
		if (json.has("description")) {
			try {
				this.description = json.get("description").getAsString();
			} catch (final ClassCastException e) {
				throw new InvalidMetadataException("Key 'description' has wrong type");
			}
		} else {
			throw new InvalidMetadataException("Missing key 'description'");
		}
		
		if (json.has("author")) {
			try {
				this.author = json.get("author").getAsString();
			} catch (final ClassCastException e) {
				throw new InvalidMetadataException("Key 'author' has wrong type");
			}
		} else {
			throw new InvalidMetadataException("Missing key 'author'");
		}
		
		if (json.has("source")) {
			try {
				this.source = Optional.of(new URL(json.get("source").getAsString()));
			} catch (ClassCastException | MalformedURLException e) {
				throw new InvalidMetadataException("Source URL invalid");
			}
		} else {
			this.source = Optional.empty();
		}
		
		if (json.has("website")) {
			try {
				this.website = Optional.of(new URL(json.get("website").getAsString()));
			} catch (ClassCastException | MalformedURLException e) {
				throw new InvalidMetadataException("Website URL invalid");
			}
		} else {
			this.website = Optional.empty();
		}
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public Optional<URL> getSourceUrl() {
		return this.source;
	}
	
	public Optional<URL> getWebsiteUrl() {
		return this.website;
	}

//	public boolean isDefault() {
//		if (this.json.has("default")) {
//			return this.json.get("default").getAsBoolean();
//		} else {
//			return false;
//		}
//	}
//
//	/**
//	 * will throw NPE for undownloaded modules
//	 */
//	public int getDownloadedBuild() {
//		return this.json.get("downloaded_build").getAsInt();
//	}
//
//	/**
//	 *
//	 */
//	public String getDownloadedVersionString() {
//		final ModuleMetaVersion version = this.getDownloadedVersion();
//		if (version == null) {
//			return "Unknown";
//		} else {
//			return version.getVersion();
//		}
//	}
//
//	/**
//	 * returns null downloaded version does not exist in metadata / repo
//	 */
//	public ModuleMetaVersion getDownloadedVersion() {
//		final int downloadedBuild = this.getDownloadedBuild();
//		for (final ModuleMetaVersion version : this.getVersions()) {
//			if (version.getBuild() == downloadedBuild) {
//				return version;
//			}
//		}
//		return null;
//	}
//
//	public boolean isDownloaded() {
//		final File jarFile = new File("modules", this.getName() + ".jar");
//		return jarFile.exists();
//	}

}
