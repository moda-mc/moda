package moda.plugin.moda.modules;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import moda.plugin.moda.utils.InvalidModuleException;
import moda.plugin.moda.utils.storage.ModuleStorageHandler;

public class ModuleClassLoader extends URLClassLoader {
	
	private final Map<String, Class<?>> classes = new HashMap<>();

	private final ModuleManager manager;
	private final Module<? extends ModuleStorageHandler> module;
	
	@SuppressWarnings("unchecked")
	ModuleClassLoader(final ClassLoader parent, final String moduleName, final File moduleFile, final ModuleManager manager) throws IOException {
		super(new URL[] { moduleFile.toURI().toURL() }, parent);
		this.manager = manager;
		
		final String mainClassName = findMainClass(moduleFile);
		
		final Module<? extends ModuleStorageHandler> module;
		try {
			module = Class.forName(mainClassName, true, this).asSubclass(Module.class).getConstructor().newInstance();
		} catch (final ClassNotFoundException e) {
			throw new InvalidModuleException("Main class not found: " + mainClassName);
		} catch (final InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
			throw new InvalidModuleException(e);
		}
		
		if (!moduleName.equals(module.getName())) {
			throw new InvalidModuleException("Module name mismatch: expected '" + moduleName + "' but getName() says '" + module.getName() + "'.");
		}
	
		module.meta = manager.getLocalMetadata(moduleName);
		this.module = module;
	}
	
	private String findMainClass(final File jarFile) throws IOException {
		try (final ZipFile zip = new ZipFile(jarFile)){
			final ZipEntry moduleYamlEntry = zip.getEntry("module.yaml");
	
			if (moduleYamlEntry == null) {
				throw new InvalidModuleException("Module jar does not contain 'module.yaml' file.");
			}
			
			final InputStream inputStream = zip.getInputStream(moduleYamlEntry);
			final Reader reader = new InputStreamReader(inputStream);
			final FileConfiguration yaml = YamlConfiguration.loadConfiguration(reader);

			if (yaml.contains("main")){
				return yaml.getString("main");
			} else {
				throw new InvalidModuleException("No main class specified in module.yaml");
			}
		}
	}

	public Module<? extends ModuleStorageHandler> getModule() {
		return this.module;
	}
	
    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        return findClass(name, true);
    }

    Class<?> findClass(final String name, final boolean checkGlobal) throws ClassNotFoundException {
        if (name.startsWith("org.bukkit.") || name.startsWith("net.minecraft.")) {
            throw new ClassNotFoundException(name);
        }

        Class<?> result = this.classes.get(name);

        if (result == null) {
            if (checkGlobal) {
                result = this.manager.getClassByName(name);
            }

            if (result == null) {
                result = super.findClass(name);

                if (result != null) {
                    this.manager.cacheClass(name, result);
                }
            }

            this.classes.put(name, result);
        }

        return result;
    }

	Set<String> getClasses() {
        return this.classes.keySet();
    }

}
