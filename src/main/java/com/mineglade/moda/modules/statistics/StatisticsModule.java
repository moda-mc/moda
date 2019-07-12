package com.mineglade.moda.modules.statistics;

import com.mineglade.moda.modules.IMessage;
import com.mineglade.moda.modules.Module;
import com.mineglade.moda.modules.statistics.storage.StatisticsDatabaseStorageHandler;
import com.mineglade.moda.modules.statistics.storage.StatisticsFileStorageHandler;
import com.mineglade.moda.modules.statistics.storage.StatisticsStorageHandler;
import com.mineglade.moda.utils.storage.DatabaseStorageHandler;
import com.mineglade.moda.utils.storage.FileStorageHandler;

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