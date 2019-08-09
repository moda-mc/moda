package moda.plugin.moda.repo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import moda.plugin.moda.Moda;

public class Repository {

	private final URL url;

	public Repository(final URL url) {
		this.url = url;
	}

	public URL getUrl() {
		return this.url;
	}

	public List<RepositoryModule> getModules() throws IOException {
		final Logger log = Moda.instance.getLogger();
		final RepositoryDownloader downloader = new RepositoryDownloader(this);

		final List<RepositoryModule> list = new ArrayList<>();
		final Iterator<JsonElement> moduleJsonIterator = downloader.getJson().getAsJsonArray().iterator();

		while (moduleJsonIterator.hasNext()) {
			final JsonObject object = moduleJsonIterator.next().getAsJsonObject();
			final int id = object.get("id").getAsInt();
			final String name = object.get("name").getAsString();
			final String description = object.get("description").getAsString();
			final String author = object.get("author").getAsString();
			final URL download;
			try {
				download = new URL(object.get("download").getAsString());
			} catch (final MalformedURLException e1) {
				log.warning("Skipped module '" + name + "' by " + author + " with invalid download URL.");
				continue;
			}
			URL source;
			try {
				source = new URL(object.get("source").getAsString());
			} catch (final MalformedURLException e2) {
				source = null;
			}
			URL website;
			try {
				website = new URL(object.get("website").getAsString());
			} catch (final MalformedURLException e3) {
				website = null;
			}
			final int minecraftVersionCode = object.get("minecraft_version").getAsInt();
			final List<ModuleMinecraftVersion> minecraftVersions = ModuleMinecraftVersion.parse(minecraftVersionCode);
			final String version = object.get("version").getAsString();
			final boolean def;
			if (object.has("default")) {
				def = object.get("default").getAsBoolean();
			} else {
				def = false;
			}
			list.add(new RepositoryModule(id, name, description, author, download, source, website, minecraftVersions,
					version, def));
		}

		return list;
	}

}
