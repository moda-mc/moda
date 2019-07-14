package moda.plugin.spigot.modules.vanish;

import moda.plugin.spigot.modules.IMessage;
import moda.plugin.spigot.modules.Module;
import moda.plugin.spigot.modules.vanish.storage.VanishStorageHandler;
import moda.plugin.spigot.utils.storage.DatabaseStorageHandler;
import moda.plugin.spigot.utils.storage.FileStorageHandler;

public class VanishModule extends Module<VanishStorageHandler> {

	@Override
	public String getName() {
		return null;
	}

	@Override
	public IMessage[] getMessages() {
		return null;
	}

	@Override
	public FileStorageHandler getFileStorageHandler() {
		return null;
	}

	@Override
	public DatabaseStorageHandler getDatabaseStorageHandler() {
		return null;
	}

}
