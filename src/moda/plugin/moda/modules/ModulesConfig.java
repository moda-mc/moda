package moda.plugin.moda.modules;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import moda.plugin.moda.Moda;

public class ModulesConfig implements Closeable, AutoCloseable {

	private final File file;
	private FileConfiguration config;
	private boolean requireSaving = false;

	public ModulesConfig() {
		this.file = new File("modules", "modules.yaml");
		this.reload();
	}

	/**
	 * Adds a module to the modules config if it is not yet present.
	 * @param defaultEnabled Whether the module is enabled by default
	 */
	public void addModule(final String name, final boolean defaultEnabled) {
		if (!this.config.contains(name)) {
			this.config.set(name, defaultEnabled);
			this.requireSaving = true;
		}
	}

	public boolean isEnabled(final String name) {
		if (!this.config.contains(name)) {
			// If it is missing from the config it must have been installed manually. Enable by default.
			this.addModule(name, true);
			Moda.instance.getLogger().warning("Module " + name + " is missing from the modules config. Did you install it manually?");
		}

		return this.config.getBoolean(name);
	}

	public void reload() {
		this.config = YamlConfiguration.loadConfiguration(this.file);
	}

	@Override
	public void close() throws IOException {
		if (this.requireSaving) {
			this.config.save(this.file);
		}
	}

}
