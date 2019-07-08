package com.mineglade.moda.modules.chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.mineglade.moda.modules.IMessage;
import com.mineglade.moda.modules.Module;
import com.mineglade.moda.modules.chat.storage.ChatStorageHandler;
import com.mineglade.moda.utils.storage.DatabaseStorageHandler;
import com.mineglade.moda.utils.storage.FileStorageHandler;

import net.md_5.bungee.api.chat.ComponentBuilder;
import xyz.derkades.derkutils.bukkit.Colors;

public class ChatModule extends Module<ChatStorageHandler> {

	@Override
	public String getName() {
		return "Chat";
	}

	@Override
	public IMessage[] getMessages() {
		return ChatMessage.values();
	}

	@Override
	public FileStorageHandler getFileStorageHandler() {
		return null;
	}

	@Override
	public DatabaseStorageHandler getDatabaseStorageHandler() {
		return null;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onChat(AsyncPlayerChatEvent event) {
		final Player player = event.getPlayer();

		if (player.hasPermission("moda.chat.use_colors")) {
			event.setMessage(Colors.parseColors(event.getMessage()));
		}
		/*
		 * for (Placeholder placeholder : EmotePlaceholders.parseEmote(player)) {
		 * event.setMessage(placeholder.parse(event.getMessage())); }
		 */

		for (Player recipient : event.getRecipients()) {
			recipient.spigot()
					.sendMessage(new ComponentBuilder(event.getMessage()).create());
		}
		event.getRecipients().clear();
	}
}
