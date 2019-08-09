package moda.plugin.moda.repo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import moda.plugin.moda.Moda;

public class RepositoryDownloader {

	private final Repository repo;
	private final File repoFile;

	public RepositoryDownloader(final Repository repo) {
		this.repo = repo;

		final File reposFolder = new File(Moda.instance.getDataFolder(), "repo-data");
		reposFolder.mkdirs();
		this.repoFile = new File(reposFolder, DigestUtils.md5Hex(repo.getUrl().toString()));
	}

	public void downloadRepo() throws IOException {
		Moda.instance.getLogger().info("Downloading repository index: " + this.repo.getUrl().toString());

		final URLConnection connection = this.repo.getUrl().openConnection();
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

	public JsonElement getJson() throws IOException {
		if (!this.repoFile.exists()) {
			this.downloadRepo();
		}

		return new JsonParser().parse(new FileReader(this.repoFile));
	}

}
