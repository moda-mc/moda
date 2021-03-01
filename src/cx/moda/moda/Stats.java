package cx.moda.moda;

import java.util.HashMap;
import java.util.Map;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.AdvancedPie;
import org.bstats.charts.SimplePie;
import org.bukkit.plugin.java.JavaPlugin;

import cx.moda.moda.module.Module;
import cx.moda.moda.module.ModuleManager;

public class Stats extends Metrics {

	public Stats(final JavaPlugin plugin) {
		super(plugin, 5106);

		this.addCustomChart(new SimplePie("storage_type", () -> Moda.instance.getStorageType().name().toString()));
		this.addCustomChart(new SimplePie("debug", () -> Moda.instance.getConfig().getBoolean("debug") + ""));

//		this.addCustomChart(new AdvancedPie("loaded_modules", () -> {
//			final Map<String, Integer> map = new HashMap<>();
//
//			for (final Module<?> module : Modules.LOADED) {
//				map.put(module.getName(), 1);
//			}
//
//			return map;
//		}));

		this.addCustomChart(new AdvancedPie("enabled_modules", () -> {
			final Map<String, Integer> map = new HashMap<>();

			for (final Module<?> module : ModuleManager.getInstance().getLoadedModules()) {
				map.put(module.getName(), 1);
			}

			return map;
		}));
	}

}
