package com.mineglade.moda.modules.statistics.storage;

import com.mineglade.moda.modules.Module;
import com.mineglade.moda.utils.storage.FileStorageHandler;

public class StatisticsFileStorageHandler extends FileStorageHandler implements StatisticsStorageHandler {

	public StatisticsFileStorageHandler(final Module<?> module) {
		super(module);
	}
}
