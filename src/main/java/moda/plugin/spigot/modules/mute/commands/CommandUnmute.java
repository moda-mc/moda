package moda.plugin.spigot.modules.mute.commands;

import java.util.Arrays;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import moda.plugin.spigot.modules.LangFile;
import moda.plugin.spigot.modules.mute.MuteMessage;
import moda.plugin.spigot.modules.mute.storage.MuteStorageHandler;
import moda.plugin.spigot.utils.PlayerNotFoundException;
import moda.plugin.spigot.utils.UuidHandler;

public class CommandUnmute extends Command {

	private final MuteStorageHandler storage;
	private final LangFile lang;

	public CommandUnmute(final MuteStorageHandler storage, final LangFile lang) {
		super("unmute", "Unmute other players", "/<command> <player>", Arrays.asList("umute"));
		this.storage = storage;
		this.lang = lang;
	}

	@Override
	public boolean execute(final CommandSender sender, final String label, final String[] args) {
		if ((args.length == 0) || (args.length > 2)) {
			this.lang.send(sender, MuteMessage.COMMAND_UNMUTE_USAGE, "command", label);
			return true;
		} else {
			OfflinePlayer target;
			try {
				target = UuidHandler.getOfflinePlayer(args[0]);
			} catch (final PlayerNotFoundException e) {
				this.lang.send(sender, MuteMessage.COMMAND_ERROR_PLAYERNOTFOUND);
				return true;
			}

			this.storage.unmute(target);
			this.lang.send(sender, MuteMessage.COMMAND_UNMUTE_SUCCESS, "target", args[0]);
			return true;
		}
	}

}
