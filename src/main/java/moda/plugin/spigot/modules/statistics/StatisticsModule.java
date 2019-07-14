package moda.plugin.spigot.modules.statistics;

import moda.plugin.spigot.modules.IMessage;
import moda.plugin.spigot.modules.Module;
import moda.plugin.spigot.modules.statistics.storage.StatisticsDatabaseStorageHandler;
import moda.plugin.spigot.modules.statistics.storage.StatisticsFileStorageHandler;
import moda.plugin.spigot.modules.statistics.storage.StatisticsStorageHandler;
import moda.plugin.spigot.utils.storage.DatabaseStorageHandler;
import moda.plugin.spigot.utils.storage.FileStorageHandler;

public class StatisticsModule extends Module<StatisticsStorageHandler> {

	@Override
	public String getName() {
		return "Statistics";
	}

	@Override
	public IMessage[] getMessages() {
		return StatisticsMessage.values();
	}

	@Override
	public FileStorageHandler getFileStorageHandler() {
		return new StatisticsFileStorageHandler(this);
	}

	@Override
	public DatabaseStorageHandler getDatabaseStorageHandler() {
		return new StatisticsDatabaseStorageHandler(this);
	}
}