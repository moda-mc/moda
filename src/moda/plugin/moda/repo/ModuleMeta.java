package moda.plugin.moda.repo;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import moda.plugin.moda.Moda;

public class ModuleMeta {

	protected final JsonObject json;

	public ModuleMeta(final JsonObject json) {
		this.json = json;
	}

	public JsonObject getJson() {
		return this.json;
	}

	public String getName() {
		return this.json.get("name").getAsString();
	}

	public String getDescription() {
		return this.json.get("description").getAsString();
	}

	public String getAuthor() {
		return this.json.get("author").getAsString();
	}

	public URL getSourceUrl() {
		if (this.json.has("source")) {
			try {
				return new URL(this.json.get("source").getAsString());
			} catch (final MalformedURLException e) {
				Moda.instance.getLogger().warning("Invalid URL: " + e.getMessage());
				try {
					return new URL("http://plugin.moda");
				} catch (final MalformedURLException no) { throw new RuntimeException(no); }
			}
		} else {
			return null;
		}
	}

	public URL getWebsiteUrl() {
		if (this.json.has("website")) {
			try {
				return new URL(this.json.get("website").getAsString());
			} catch (final MalformedURLException e) {
				Moda.instance.getLogger().warning("Invalid URL: " + e.getMessage());
				try {
					return new URL("http://plugin.moda");
				} catch (final MalformedURLException no) { throw new RuntimeException(no); }
			}
		} else {
			return null;
		}
	}

	public List<ModuleMetaVersion> getVersions(){
		final List<ModuleMetaVersion> versions = new ArrayList<>();
		this.json.get("versions").getAsJsonArray().forEach((e) -> versions.add(new ModuleMetaVersion(this, e.getAsJsonObject())));
		return versions;
	}

	public ModuleMetaVersion getLatestVersion() {
		final List<ModuleMetaVersion> versions = this.getVersions();

		if (versions.size() == 0) {
			return null;
		}

		if (versions.size() == 1) {
			return this.getVersions().get(0);
		}

		int n = -1;
		ModuleMetaVersion v = null;
		for (final ModuleMetaVersion v2 : versions) {
			final int n2 = v2.getBuild();
			if (n2 > n) {
				n = n2;
				v = v2;
			}
		}

		return v;
	}

	public ModuleMetaVersion getLatestVersionThatSupports(final ModuleMinecraftVersion minecraftVersion) {
		final List<ModuleMetaVersion> versions = this.getVersions();

		if (versions.size() == 0) {
			return null;
		}

		if (versions.size() == 1) {
			final ModuleMetaVersion version = this.getVersions().get(0);
			if (version.getSupportedMinecraftVersionsAsList().contains(minecraftVersion)) {
				return this.getVersions().get(0);
			} else {
				return null;
			}
		}

		int n = -1;
		ModuleMetaVersion v = null;
		for (final ModuleMetaVersion v2 : versions) {
			if (v2.getSupportedMinecraftVersionsAsList().contains(minecraftVersion)){
				final int n2 = v2.getBuild();
				if (n2 > n) {
					n = n2;
					v = v2;
				}
			}
		}

		return v;
	}

	public boolean isDefault() {
		if (this.json.has("default")) {
			return this.json.get("default").getAsBoolean();
		} else {
			return false;
		}
	}

	/**
	 * will throw NPE for undownloaded modules
	 */
	public int getDownloadedBuild() {
		return this.json.get("downloaded_build").getAsInt();
	}

	/**
	 *
	 */
	public String getDownloadedVersionString() {
		final ModuleMetaVersion version = this.getDownloadedVersion();
		if (version == null) {
			return "Unknown";
		} else {
			return version.getVersion();
		}
	}

	/**
	 * returns null downloaded version does not exist in metadata / repo
	 */
	public ModuleMetaVersion getDownloadedVersion() {
		final int downloadedBuild = this.getDownloadedBuild();
		for (final ModuleMetaVersion version : this.getVersions()) {
			if (version.getBuild() == downloadedBuild) {
				return version;
			}
		}
		return null;
	}

	public boolean isDownloaded() {
		final File jarFile = new File("modules", this.getName() + ".jar");
		return jarFile.exists();
	}

}
