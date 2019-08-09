package com.mineglade.moda.repo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.mineglade.moda.Moda;

public class Repositories {

	public static List<Repository> getRepositories() {
		final List<Repository> repositories = new ArrayList<>();
		for (final String urlString : Moda.instance.getConfig().getStringList("repositories")) {
			try {
				repositories.add(new Repository(new URL(urlString)));
			} catch (final MalformedURLException e) {
				Moda.instance.getLogger().warning("Skipped repository with malformed URL: " + urlString);
				continue;
			}
		}
		return repositories;
	}

}
