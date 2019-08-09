package moda.plugin.moda.repo;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import moda.plugin.moda.Moda;

public class ModuleDownloader {

	private final RepositoryModule module;

	public ModuleDownloader(final RepositoryModule module) {
		this.module = module;
	}

	public void download(final File outputFile) throws IOException {
		FileUtils.copyURLToFile(this.module.getDownloadURL(), outputFile,
				Moda.instance.getConfig().getInt("timeout"), Moda.instance.getConfig().getInt("timeout"));
	}

}
