package com.mineglade.moda.utils.storage;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.mineglade.moda.Moda;
import com.mineglade.moda.modules.Module;

import xyz.derkades.derkutils.NoParameter;
import xyz.derkades.derkutils.bukkit.BukkitFuture;

public abstract class FileStorageHandler extends StorageHandler {

	protected FileConfiguration file;

	private final File fileFile;

	public FileStorageHandler(final Module<? extends ModuleStorageHandler> module) {
		super(module);

		final File fileFileDir = new File(module.getDataFolder() + File.separator + "data");

		fileFileDir.mkdirs();

		this.fileFile = new File(fileFileDir, "data.yaml");

		this.file = YamlConfiguration.loadConfiguration(this.fileFile);
	}

	public BukkitFuture<NoParameter> save() {
		return new BukkitFuture<>(Moda.instance, () ->  {
			this.file.save(this.fileFile);
			return null;
		});
	}

	public void saveBlocking() throws IOException {
		this.file.save(this.fileFile);
	}

}
