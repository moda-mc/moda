package com.mineglade.moda.modules.vanish;

import com.mineglade.moda.modules.IMessage;
import com.mineglade.moda.modules.Module;
import com.mineglade.moda.modules.vanish.storage.VanishStorageHandler;
import com.mineglade.moda.utils.storage.DatabaseStorageHandler;
import com.mineglade.moda.utils.storage.FileStorageHandler;

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
