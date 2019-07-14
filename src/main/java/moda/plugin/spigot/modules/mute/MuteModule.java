package moda.plugin.spigot.modules.mute;

import moda.plugin.spigot.modules.IMessage;
import moda.plugin.spigot.modules.Module;
import moda.plugin.spigot.modules.mute.commands.CommandMute;
import moda.plugin.spigot.modules.mute.commands.CommandUnmute;
import moda.plugin.spigot.modules.mute.storage.MuteDatabaseStorageHandler;
import moda.plugin.spigot.modules.mute.storage.MuteFileStorageHandler;
import moda.plugin.spigot.modules.mute.storage.MuteStorageHandler;
import moda.plugin.spigot.utils.storage.DatabaseStorageHandler;
import moda.plugin.spigot.utils.storage.FileStorageHandler;

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
