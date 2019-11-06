package moda.plugin.moda.modules;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.JsonParser;

import moda.plugin.moda.Moda;
import moda.plugin.moda.repo.ModuleMeta;
import moda.plugin.moda.utils.storage.ModuleStorageHandler;

public class ModuleManager {

	private static final ModuleManager manager;

	static {
		manager = new ModuleManager();
	}

	public static ModuleManager getInstance() {
		return manager;
	}

	private ModuleManager() {}

	private final Map<String, ModuleLoader> moduleLoaders = new HashMap<>();

	public void unload(final String name) throws Exception {
		if (!this.moduleLoaders.containsKey(name)) {
			throw new IllegalStateException("No module with the name " + name + " is loaded");
		}

		final ModuleLoader loader = this.moduleLoaders.get(name);
		final Module<?> module = loader.getModule();
		module.disable();

		this.moduleLoaders.remove(name);

		// All references to module classes are gone now. The garbage collector will take care of it.
		System.gc();
	}

	public void load(final String name) throws Exception {
		if (this.moduleLoaders.containsKey(name)) {
			throw new IllegalStateException("A module with the name " + name + " is already loaded");
		}

		final ModuleLoader loader = new ModuleLoader(Moda.instance, name);
		loader.load();
		loader.getModule().enable();
		this.moduleLoaders.put(name, loader);
	}

	public List<Module<? extends ModuleStorageHandler>> getLoadedModules() {
		return this.moduleLoaders.values().stream().map(ModuleLoader::getModule).collect(Collectors.toList());
	}

	public List<String> getInstalledModulesNames() {
		return Arrays.asList(new File("modules").listFiles()).stream()
				.filter((f) -> f.getName().endsWith(".jar"))
				.map((f) -> f.getName().replace(".jar", "")).collect(Collectors.toList());
	}

	public ModuleMeta getMetadata(final String moduleName) {
		final File metaFile = new File("modules", moduleName + ".json");
		try {
			return new ModuleMeta(new JsonParser().parse(new FileReader(metaFile)).getAsJsonObject());
		} catch (final FileNotFoundException e) {
			Moda.instance.getLogger().warning("Metadata file " + metaFile.getPath() + " does not exist. This is normal if the installed module was not downloaded from a repository. Some plugin features may not work properly. If you wish to create a file manually, have a look at the wiki: https://github.com/ModaPlugin/Moda/wiki/Module-metadata-file");
			return null;
		}
	}

}
