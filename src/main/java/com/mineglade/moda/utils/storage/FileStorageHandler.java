package com.mineglade.moda.utils.storage;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.mineglade.moda.Moda;
import com.mineglade.moda.modules.Module;

import xyz.derkades.derkutils.NoParameter;
import xyz.derkades.derkutils.bukkit.BukkitFuture;

public abstract class FileStorageHandler extends StorageHandler {

	protected FileConfiguration file;

	private final File fileFile;

	public FileStorageHandler(final Module module) {
		super(module);

		this.fileFile = new File(module.getDataFolder() + File.separator + "data", "data.yaml");

		this.fileFile.mkdirs();
		this.file = YamlConfiguration.loadConfiguration(this.fileFile);
	}

	public BukkitFuture<NoParameter> save() {
		final BukkitFuture<NoParameter> future = new BukkitFuture<>(Moda.instance, () ->  {
			this.file.save(this.fileFile);
			return null;
		});
		return future;
	}

}
