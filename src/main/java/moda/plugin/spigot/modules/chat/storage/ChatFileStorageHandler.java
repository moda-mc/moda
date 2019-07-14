package moda.plugin.spigot.modules.chat.storage;

import moda.plugin.spigot.modules.Module;
import moda.plugin.spigot.utils.storage.FileStorageHandler;

public class ChatFileStorageHandler extends FileStorageHandler implements ChatStorageHandler {

	public ChatFileStorageHandler(final Module<?> module) {
		super(module);
	}
}
