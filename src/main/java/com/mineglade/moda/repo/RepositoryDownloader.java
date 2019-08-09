package com.mineglade.moda.repo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mineglade.moda.Moda;

public class RepositoryDownloader {

	private final Repository repo;
	private final File repoFile;

	public RepositoryDownloader(final Repository repo) {
		this.repo = repo;

		final File reposFolder = new File(Moda.instance.getDataFolder(), "repo-data");
		reposFolder.mkdirs();
		this.repoFile = new File(reposFolder,
				this.repo.getUrl().toString().replace("/", "").replace("https:", "").replace("http:", ""));
	}

	public void downloadRepo() throws IOException {
		Moda.instance.getLogger().info("Downloading repository index: " + this.repo.getUrl().toString());

		final HttpsURLConnection connection = (HttpsURLConnection) this.repo.getUrl().openConnection();

		connection.setRequestMethod("GET");
		connection.addRequestProperty("User-Agent", "Moda v" + Moda.instance.getDescription().getVersion());

		// Initialize input stream
		final InputStream inputStream = connection.getInputStream();

		// Handle response
		final BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		final StringBuilder responseBuilder = new StringBuilder();

		String responseString;
		while ((responseString = streamReader.readLine()) != null) {
			responseBuilder.append(responseString);
		}

		if (this.repoFile.exists()) {
			this.repoFile.delete();
		}

		try (FileWriter writer = new FileWriter(this.repoFile)){
			writer.write(responseBuilder.toString());
		}
	}

	public List<RepositoryModule> getModules() throws IOException {
		final Logger log = Moda.instance.getLogger();
		if (!this.repoFile.exists()) {
			this.downloadRepo();
		}

		final List<RepositoryModule> list = new ArrayList<>();
		final Iterator<JsonElement> moduleJsonIterator = new JsonParser().parse(new FileReader(this.repoFile))
				.getAsJsonArray().iterator();

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
