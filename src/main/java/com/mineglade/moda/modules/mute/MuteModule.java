package com.mineglade.moda.modules.mute;

import com.mineglade.moda.modules.IMessage;
import com.mineglade.moda.modules.Module;
import com.mineglade.moda.modules.mute.commands.CommandMute;
import com.mineglade.moda.modules.mute.commands.CommandUnmute;
import com.mineglade.moda.modules.mute.storage.MuteDatabaseStorageHandler;
import com.mineglade.moda.modules.mute.storage.MuteFileStorageHandler;
import com.mineglade.moda.modules.mute.storage.MuteStorageHandler;
import com.mineglade.moda.utils.storage.DatabaseStorageHandler;
import com.mineglade.moda.utils.storage.FileStorageHandler;

/**
 * Muting module
 * @author Derkades
 */
public class MuteModule extends Module<MuteStorageHandler> {

	@Override
	public String getName() {
		return "Mute";
	}

	@Override
	public IMessage[] getMessages() {
		return MuteMessage.values();
	}

	@Override
	public FileStorageHandler getFileStorageHandler() {
		return new MuteFileStorageHandler(this);
	}

	@Override
	public DatabaseStorageHandler getDatabaseStorageHandler() {
		return new MuteDatabaseStorageHandler(this);
	}

	@Override
	public void onEnable() {
		this.registerCommand(new CommandMute(this.storage, this.lang));
		this.registerCommand(new CommandUnmute(this.storage, this.lang));
	}

}
