package moda.plugin.moda.utils.storage;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import moda.plugin.moda.modules.Module;

public class YamlStorageHandler extends FileStorageHandler {
	
	private final FileConfiguration yaml;
	private final File file;

	public YamlStorageHandler(final Module<? extends ModuleStorageHandler> module) throws IOException {
		super(module);

		final File fileFileDir = new File(module.getDataFolder() + File.separator + "data");

		fileFileDir.mkdirs();

		this.file = new File(fileFileDir, "data.yaml");

		this.yaml = YamlConfiguration.loadConfiguration(this.file);
	}
	
	public FileConfiguration getYaml() {
		return this.yaml;
	}

	@Override
	public void save() throws IOException {
		this.yaml.save(this.file);
	}

}
