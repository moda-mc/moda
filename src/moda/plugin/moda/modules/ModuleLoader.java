package moda.plugin.moda.modules;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import moda.plugin.moda.Moda;
import moda.plugin.moda.repo.ModuleMeta;
import moda.plugin.moda.utils.InvalidModuleException;
import moda.plugin.moda.utils.JarLoader;
import moda.plugin.moda.utils.storage.ModuleStorageHandler;

public class ModuleLoader extends JarLoader {

	private Module<? extends ModuleStorageHandler> module;
	private final String moduleName;

	public ModuleLoader(final Plugin plugin, final String moduleName) throws IllegalStateException {
		super(plugin);
		this.moduleName = moduleName;
	}

	@SuppressWarnings("unchecked")
	public void load() throws Exception {
		final File jarFile = new File("modules", this.moduleName + ".jar");

		if (!jarFile.exists()) {
			throw new FileNotFoundException("Module file does not exist: " + jarFile.getAbsolutePath());
		}

		final ModuleMeta meta = Modules.getMetadata(this.moduleName);

		final JarLoader jarLoader = new JarLoader(Moda.instance);
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

			jarLoader.loadJar(jarFile);

			Class<?> mainClass;

			try {
				mainClass = Class.forName(mainClassName);
			} catch (final ClassNotFoundException e) {
				throw new InvalidModuleException("Main class not found: " + mainClassName);
			}

			final Object mainClassInstance = mainClass.newInstance();

			Module<? extends ModuleStorageHandler> module;

			if (mainClassInstance instanceof Module) {
				module = (Module<? extends ModuleStorageHandler>) mainClass.newInstance();
			} else {
				throw new InvalidModuleException("Main class is not a subclass of Module");
			}

			if (!module.getName().equals(this.moduleName)) {
				throw new InvalidModuleException("Module name mismatch, " + module.getName() + " isn't " + this.moduleName + ".");
			}

			module.meta = meta;
			this.module = module;
		}
	}

	public Module<? extends ModuleStorageHandler> getModule() {
		if (this.module == null) {
			throw new IllegalStateException("Module is not loaded");
		}

		return this.module;
	}

}
