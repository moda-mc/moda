package moda.plugin.moda.repo;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.JsonElement;

import moda.plugin.moda.Moda;

public class Repository {

	private final URL url;

	public Repository(final URL url) {
		this.url = url;
	}

	public URL getUrl() {
		return this.url;
	}

	public void download() throws IOException {
		new RepositoryDownloader(this).downloadRepo();
	}

	public List<ModuleMeta> getModules() throws IOException {
		final Logger log = Moda.instance.getLogger();
		final RepositoryDownloader downloader = new RepositoryDownloader(this);

		final List<ModuleMeta> list = new ArrayList<>();
		final Iterator<JsonElement> moduleJsonIterator = downloader.getJson().getAsJsonArray().iterator();

		while (moduleJsonIterator.hasNext()) {

		}

		return list;
	}

}
