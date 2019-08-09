package moda.plugin.moda.repo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.google.gson.JsonObject;

public class ModuleMeta {

	private final int id;
	private final String name;
	private final String description;
	private final String author;
	private final URL download;
	private final URL source;
	private final URL website;
	private final int minecraftVersions;
	private final String version;
	private final boolean def;

	public ModuleMeta(final int id, final String name,
			final String description, final String author,
			final URL download, final URL source, final URL website,
			final int minecraftVersions, final String version, final boolean def) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.author = author;
		this.download = download;
		this.source = source;
		this.website = website;
		this.minecraftVersions = minecraftVersions;
		this.version = version;
		this.def = def;
	}

	public int getId() {
		return this.id;
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

	public URL getDownloadURL() {
		return this.download;
	}

	public URL getSourceURL() {
		return this.source;
	}

	public URL getWebsiteURL() {
		return this.website;
	}

	public List<ModuleMinecraftVersion> getMinecraftVersions() {
		return ModuleMinecraftVersion.parse(this.minecraftVersions);
	}

	public String getVersion() {
		return this.version;
	}

	public boolean isDefault() {
		return this.def;
	}

	public JsonObject toJson() {
		final JsonObject object = new JsonObject();
		object.addProperty("id", this.id);
		object.addProperty("name", this.name);
		object.addProperty("description", this.description);
		object.addProperty("author", this.author);
		object.addProperty("download", this.download.toString());
		if (this.source != null)
			object.addProperty("source", this.source.toString());
		if (this.website != null)
			object.addProperty("website", this.website.toString());
		object.addProperty("minecraft_version", this.minecraftVersions);
		object.addProperty("version", this.version);
		object.addProperty("default", this.def);
		return object;
	}

	public static ModuleMeta fromJson(final JsonObject json) throws MalformedURLException {
		final int id = json.get("id").getAsInt();
		final String name = json.get("name").getAsString();
		final String description = json.get("description").getAsString();
		final String author = json.get("author").getAsString();
		final URL download = new URL(json.get("download").getAsString());
		//try {
		//} catch (final MalformedURLException e1) {
		//	log.warning("Skipped module '" + name + "' by " + author + " with invalid download URL.");
		//	continue;
		//}
		URL source;
		try {
			source = new URL(json.get("source").getAsString());
		} catch (final MalformedURLException e2) {
			source = null;
		}
		URL website;
		try {
			website = new URL(json.get("website").getAsString());
		} catch (final MalformedURLException e3) {
			website = null;
		}
		final int minecraftVersions = json.get("minecraft_version").getAsInt();
		final String version = json.get("version").getAsString();
		final boolean def;
		if (json.has("default")) {
			def = json.get("default").getAsBoolean();
		} else {
			def = false;
		}
		return new ModuleMeta(id, name, description, author, download, source, website, minecraftVersions, version, def);
	}

}
