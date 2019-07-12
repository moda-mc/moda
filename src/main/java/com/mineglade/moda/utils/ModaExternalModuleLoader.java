package com.mineglade.moda.utils;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.mineglade.moda.Moda;
import com.mineglade.moda.modules.ExternalModule;
import com.mineglade.moda.utils.storage.ModuleStorageHandler;

public class ModaExternalModuleLoader {

	private final File moduleDir;
	private final JarLoader jarLoader;
	private final Logger logger;

	public ModaExternalModuleLoader(final File moduleDir) {
		this.moduleDir = moduleDir;
		moduleDir.mkdirs();
		this.jarLoader = new JarLoader(Moda.instance);
		this.logger = Moda.instance.getLogger();
	}

	@SuppressWarnings("unchecked")
	public void load() {
		final File[] jarFiles = (File[]) Arrays.asList(this.moduleDir.listFiles()).stream().filter((f) -> f.getAbsolutePath().endsWith(".jar")).toArray();
		for (final File jarFile : jarFiles) {
			try (final ZipFile zip = new ZipFile(jarFile)){
				final ZipEntry moduleYamlEntry = zip.getEntry("module.yaml");

				if (moduleYamlEntry == null) {
					throw new InvalidModuleException("Module jar does not contain 'module.yaml' file.");
				}

				final InputStream inputStream = zip.getInputStream(moduleYamlEntry);
				final Reader reader = new InputStreamReader(inputStream);
				final FileConfiguration yaml = YamlConfiguration.loadConfiguration(reader);

				String mainClassName;
				if (yaml.contains("main")){
					mainClassName = yaml.getString("main");
				} else {
					throw new InvalidModuleException("No main class specified");
				}

				this.jarLoader.loadJar(jarFile);

				Class<?> mainClass;

				try {
					mainClass = Class.forName(mainClassName);
				} catch (final ClassNotFoundException e) {
					throw new InvalidModuleException("Main class not found: " + mainClassName);
				}

				final Object mainClassInstance = mainClass.newInstance();

				ExternalModule<? extends ModuleStorageHandler> module;

				if (mainClassInstance instanceof ExternalModule) {
					module = (ExternalModule<? extends ModuleStorageHandler>) mainClass.newInstance();
				} else {
					throw new InvalidModuleException("Main class is not a subclass of ExternalModule");
				}

				Moda.registerModule(module);
			} catch (final Exception e) {
				this.logger.severe("An error occured while loading " + jarFile.getAbsolutePath());
				e.printStackTrace();
			}
		}
	}

}
