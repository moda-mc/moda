package moda.plugin.moda.repo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import moda.plugin.moda.Moda;

public class Repository {

	private final URL url;
	private final File index;

	public Repository(final URL url) {
		this.url = url;

		final File reposFolder = new File(Moda.instance.getDataFolder(), "repo-data");
		reposFolder.mkdirs();
		this.index = new File(reposFolder, DigestUtils.md5Hex(url.toString()));
	}

	public URL getUrl() {
		return this.url;
	}

	public List<ModuleMeta> getModules() throws IOException {
		final List<ModuleMeta> list = new ArrayList<>();
		this.getIndex().get("modules").getAsJsonArray().forEach((e) -> list.add(new ModuleMeta(e.getAsJsonObject())));
		return list;
	}

	public JsonObject downloadIndex() throws IOException {
		Moda.instance.getLogger().info("Downloading repository index: " + this.getUrl().toString());

		final URLConnection connection = this.getUrl().openConnection();
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

		if (this.index.exists()) {
			this.index.delete();
		}

		final String content = responseBuilder.toString();

		final JsonObject json = new JsonParser().parse(content).getAsJsonObject();
		json.addProperty("index_time", System.currentTimeMillis());

		try (FileWriter writer = new FileWriter(this.index)){
			writer.write(json.toString());
		}

		return json;
	}

	public void downloadIndexIfOlderThan(final long time) throws IOException {
		// If the index does not exist, it is downloaded by this method. If it is downloaded by this method,
		// it won't be downloaded again because the timestamp has been updated
		final JsonObject json = this.getIndex();
		if (!json.has("index_time") || (json.get("index_time").getAsLong() + time) < System.currentTimeMillis()) {
			this.downloadIndex();
		}
	}

	public JsonObject getIndex() throws IOException {
		JsonObject json = null;

		if (!this.index.exists()) {
			json = this.downloadIndex();
		}

		if (json != null) {
			return json;
		} else {
			return new JsonParser().parse(new FileReader(this.index)).getAsJsonObject();
		}
	}

}
