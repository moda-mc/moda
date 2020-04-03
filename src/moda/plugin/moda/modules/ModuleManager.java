package moda.plugin.moda.modules;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import moda.plugin.moda.Moda;
import moda.plugin.moda.repo.InvalidMetadataException;
import moda.plugin.moda.repo.ModuleMetaLocal;
import moda.plugin.moda.repo.ModuleMetaRepository;
import moda.plugin.moda.repo.ModuleMetaVersion;
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

	private final Map<String, ModuleClassLoader> loaders = new HashMap<>();
	private final Map<String, Class<?>> classCache = new HashMap<>();

	public void unload(final String name) throws Exception {
		if (!this.loaders.containsKey(name)) {
			throw new IllegalStateException("No module with the name " + name + " is loaded");
		}

		final ModuleClassLoader loader = this.loaders.get(name);
		final Module<?> module = loader.getModule();
		
		// Unregisters all listeners, disable all tasks, save data, call onDisable().
		module.disable();
		
		// Remove reference to old ClassLoader. The garbage collector will take care of the rest.
		this.loaders.remove(name);
	}

	public void load(final String name) throws Exception {
		if (this.loaders.containsKey(name)) {
			throw new IllegalStateException("A module with the name " + name + " is already loaded");
		}
		
		final File jarFile = new File("modules", name + ".jar");

		if (!jarFile.exists()) {
			throw new FileNotFoundException("Module file does not exist: " + jarFile.getAbsolutePath());
		}

		final ModuleClassLoader loader = new ModuleClassLoader(Moda.instance.getClass().getClassLoader(), name, jarFile, this);
		this.loaders.put(name, loader);
		
		loader.getModule().enable();
	}

	public List<Module<? extends ModuleStorageHandler>> getLoadedModules() {
		return this.loaders.values().stream().map(ModuleClassLoader::getModule).collect(Collectors.toList());
	}

	public List<String> getInstalledModulesNames() {
		return Arrays.asList(new File("modules").listFiles()).stream()
				.filter((f) -> f.getName().endsWith(".jar"))
				.map((f) -> f.getName().replace(".jar", "")).collect(Collectors.toList());
	}

	public Optional<ModuleMetaLocal> getLocalMetadata(final String moduleName) {
		final File metaFile = new File("modules", moduleName + ".json");
		try {
			final JsonObject json = new JsonParser().parse(new FileReader(metaFile)).getAsJsonObject();
			return Optional.of(new ModuleMetaLocal(json));
		} catch (final FileNotFoundException e) {
			Moda.instance.getLogger().warning("Metadata file " + metaFile.getPath() + " does not exist. This is normal if the installed module was not downloaded from a repository. Some plugin features may not work properly. If you wish to create a file manually, have a look at the wiki: https://github.com/ModaPlugin/Moda/wiki/Module-metadata-file");
			return Optional.empty();
		} catch (JsonIOException | JsonSyntaxException | InvalidMetadataException e) {
			Moda.instance.getLogger().warning("Error occured while reading module metadata for module '" + moduleName + "'" + e.getMessage());
			return Optional.empty();
		}
	}
	
	public boolean isDownloaded(final String name) {
		final File jarFile = new File("modules", name + ".jar");
		return jarFile.exists();
	}
	
	public void download (final ModuleMetaRepository meta, final ModuleMetaVersion version) {
		
	}

	Class<?> getClassByName(final String name) {
		Class<?> cachedClass = this.classCache.get(name);

		if (cachedClass != null) {
			return cachedClass;
		}

		// Try to find classes in modules one by one
		for (final ModuleClassLoader loader : this.loaders.values()) {
			try {
				// checkGlobal set to false to avoid infinite recursion
				cachedClass = loader.findClass(name, false);
			} catch (final ClassNotFoundException e) {}

			if (cachedClass != null) {
				return cachedClass;
			}
		}

		// Class not found in any module
		return null;
	}
	
    void cacheClass(final String name, final Class<?> clazz) {
        if (!this.classCache.containsKey(name)) {
        	this.classCache.put(name, clazz);
        }
    }

}
