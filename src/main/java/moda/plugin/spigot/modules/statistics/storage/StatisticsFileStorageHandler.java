package moda.plugin.spigot.modules.statistics.storage;

import moda.plugin.spigot.modules.Module;
import moda.plugin.spigot.utils.storage.FileStorageHandler;

public class StatisticsFileStorageHandler extends FileStorageHandler implements StatisticsStorageHandler {

	public StatisticsFileStorageHandler(final Module<?> module) {
		super(module);
	}
}
