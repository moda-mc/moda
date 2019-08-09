package com.mineglade.moda.repo;

import java.net.URL;

public class Repository {

	private final URL url;

	public Repository(final URL url) {
		this.url = url;
	}

	public URL getUrl() {
		return this.url;
	}

}
