package moda.plugin.moda.modules;

public class Modules {

//	public static final List<Module<? extends ModuleStorageHandler>> ENABLED = new ArrayList<>();
//
//	public static final List<Module<? extends ModuleStorageHandler>> LOADED = new ArrayList<>();
//
//	public static Module<? extends ModuleStorageHandler> getLoadedModuleByName(final String name){
//		for (final Module<? extends ModuleStorageHandler> module : LOADED) {
//			if (module.getName().equals(name)) {
//				return module;
//			}
//		}
//		return null;
//	}
//
//	@SuppressWarnings("unchecked")
//	public static void load(final String name) throws Exception {
//		if (getLoadedModuleByName(name) != null) {
//			throw new IllegalStateException("A module with the name " + name + " is already loaded");
//		}
//
//		final File jarFile = new File("modules", name + ".jar");
//
//		if (!jarFile.exists()) {
//			throw new FileNotFoundException("Module file does not exist: " + jarFile.getAbsolutePath());
//		}
//
//		final ModuleMeta meta = Modules.getMetadata(name);
//
//		final JarLoader jarLoader = new JarLoader(Moda.instance);
//		try (final ZipFile zip = new ZipFile(jarFile)){
//			final ZipEntry moduleYamlEntry = zip.getEntry("module.yaml");
//
//			if (moduleYamlEntry == null) {
//				throw new InvalidModuleException("Module jar does not contain 'module.yaml' file.");
//			}
//
//			final InputStream inputStream = zip.getInputStream(moduleYamlEntry);
//			final Reader reader = new InputStreamReader(inputStream);
//			final FileConfiguration yaml = YamlConfiguration.loadConfiguration(reader);
//
//			String mainClassName;
//			if (yaml.contains("main")){
//				mainClassName = yaml.getString("main");
//			} else {
//				throw new InvalidModuleException("No main class specified");
//			}
//
//			jarLoader.loadJar(jarFile);
//
//			Class<?> mainClass;
//
//			try {
//				mainClass = Class.forName(mainClassName);
//			} catch (final ClassNotFoundException e) {
//				throw new InvalidModuleException("Main class not found: " + mainClassName);
//			}
//
//			final Object mainClassInstance = mainClass.newInstance();
//
//			Module<? extends ModuleStorageHandler> module;
//
//			if (mainClassInstance instanceof Module) {
//				module = (Module<? extends ModuleStorageHandler>) mainClass.newInstance();
//			} else {
//				throw new InvalidModuleException("Main class is not a subclass of Module");
//			}
//
//			if (!module.getName().equals(name)) {
//				throw new InvalidModuleException("Module name mismatch");
//			}
//
//			LOADED.add(module);
//			module.initLogger();
//			module.loadLang();
//
//			final ZipEntry configYamlEntry = zip.getEntry("config.yaml");
//
//			if (configYamlEntry == null) {
//				module.getLogger().debug("Module jar does not contain 'config.yaml' file.");
//			} else {
//				final File output = new File(module.getDataFolder(), "config.yaml");
//				if (!output.exists()) {
//					module.getLogger().debug("Config yaml file does not exist, copying from jar file..");
//					final InputStream inputStream2 = zip.getInputStream(configYamlEntry);
//					final Path outputPath = Paths.get(output.toURI());
//					Files.copy(inputStream2, outputPath);
//				} else {
//					module.getLogger().debug("Config yaml file already exists");
//				}
//			}
//
//			module.meta = meta;
//
//			if (meta != null) {
//				module.getLogger().debug("Loaded module " + meta.getName() + " by " + meta.getAuthor() + " version " + meta.getDownloadedVersionString());
//			} else {
//				module.getLogger().debug("Loaded module " + name);
//			}
//
//			module.enable();
//		} catch (final Exception e) {
//			throw new Exception(e);
//		}
//	}
//
//	public static List<String> getInstalledModulesNames(){
//		return Arrays.asList(new File("modules").listFiles()).stream()
//				.filter((f) -> f.getName().endsWith(".jar"))
//				.map((f) -> f.getName().replace(".jar", "")).collect(Collectors.toList());
//	}
	
}
