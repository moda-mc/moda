package com.mineglade.moda.modules.mute;

import java.util.Arrays;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mineglade.moda.modules.LangFile;
import com.mineglade.moda.utils.PlayerNotFoundException;
import com.mineglade.moda.utils.UuidHandler;

class CommandMute extends Command {

	private final MuteStorageHandler storage;
	private final LangFile lang;

	protected CommandMute(final MuteStorageHandler storage, final LangFile lang) {
		super("mute", "Mute other players temporarily or permanently", "/<command> <player> [duration]", Arrays.asList());
		this.storage = storage;
		this.lang = lang;
	}

	@Override
	public boolean execute(final CommandSender sender, final String label, final String[] args) {
		if (args.length == 0 || args.length > 2) {
			this.lang.send(sender, MuteMessage.COMMAND_MUTE_USAGE, "command", label);
			return true;
		} else {
			OfflinePlayer target;
			try {
				target = UuidHandler.getOfflinePlayer(args[0]);
			} catch (final PlayerNotFoundException e) {
				this.lang.send(sender, MuteMessage.COMMAND_ERROR_PLAYERNOTFOUND);
				return true;
			}

			if (args.length == 1) {
				this.storage.permanentMute(target);
				this.lang.send(sender, MuteMessage.COMMAND_MUTE_SUCCESS_PERM, "target", args[0]);
				return true;
			} else {
				long duration;
				try {
					duration = this.parseDuration(args[1]);
				} catch (final DurationFormatException e) {
					this.lang.send(sender, MuteMessage.COMMAND_ERROR_DURATION);
					return true;
				}

				final long expireTime = System.currentTimeMillis() + duration;
				this.storage.tempMute(target, expireTime);
				this.lang.send(sender, MuteMessage.COMMAND_MUTE_SUCCESS_TEMP, "target", args[0]);
				return true;
			}
		}
	}

	/**
	 * @param unparsedDuration
	 * @return Mute expiration timestamp
	 * @throws DurationFormatException
	 */
	private long parseDuration(final String unparsedDuration) throws DurationFormatException {
		if (!unparsedDuration.matches("[mhdwMy]")) {
			throw new DurationFormatException();
		}

		int rawDuration;
		try {
			rawDuration = Integer.parseInt(unparsedDuration.replaceAll("[^0-9]", ""));
		} catch (final NumberFormatException e) {
			throw new DurationFormatException();
		}

		final String multiplier = unparsedDuration.replaceAll("[0-9]", "");

		if (multiplier.equals("m")) {
			return rawDuration * 1000 * 60;
		} else if (multiplier.equals("h")) {
			return rawDuration * 1000 * 60 * 60;
		} else if (multiplier.equals("d")) {
			return rawDuration * 1000 * 60 * 60 * 24;
		} else if (multiplier.equals("w")) {
			return rawDuration * 1000 * 60 * 60 * 24 * 7;
		} else if (multiplier.equals("M")) {
			return rawDuration * 1000 * 60 * 60 * 24 * 7 * 30;
		} else if (multiplier.equals("y")) {
			return rawDuration * 1000 * 60 * 60 * 24 * 7 * 365;
		} else {
			throw new DurationFormatException();
		}
	}

}
