package moda.plugin.spigot.modules.statistics.storage;

import java.sql.SQLException;

import moda.plugin.spigot.modules.Module;
import moda.plugin.spigot.utils.storage.DatabaseStorageHandler;

public class StatisticsDatabaseStorageHandler extends DatabaseStorageHandler implements StatisticsStorageHandler {

	public StatisticsDatabaseStorageHandler(final Module<?> module) {
		super(module);
	}

	@Override
	public void setup() throws SQLException {
		// ChatMessageCount statistic table
		this.db.createTableIfNonexistent("playerChatMessageCount",
				"CREATE TABLE `" + this.db.getDatabase()
				+ "`.`playerChatMessageCount` (`uuid` VARCHAR(100) NOT NULL, "
				+ "`messageCount` INT NOT NULL, "
				+ "PRIMARY KEY (`uuid`)) "
				+ "ENGINE = InnoDB");
	}
}