package com.mineglade.moda.repo;

import java.net.URL;
import java.util.List;

public class RepositoryModule {

	private final int id;
	private final String name;
	private final URL download;
	private final URL source;
	private final URL website;
	private final List<ModuleMinecraftVersion> minecraftVersions;
	private final String version;
	private final boolean def;

	public RepositoryModule(final int id, final String name,
			final String description, final String author,
			final URL download, final URL source, final URL website,
			final List<ModuleMinecraftVersion> minecraftVersions, final String version, final boolean def) {
		this.id = id;
		this.name = name;
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
		return this.minecraftVersions;
	}

	public String getVersion() {
		return this.version;
	}

	public boolean isDefault() {
		return this.def;
	}

}
