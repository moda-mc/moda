package com.mineglade.icore.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.mineglade.icore.ICore;
import com.mineglade.icore.emotes.EmotePlaceholders;
import com.mineglade.icore.utils.ColorUtil;
import com.mineglade.icore.utils.NickNameUtil;

import xyz.derkades.derkutils.bukkit.Chat;
import xyz.derkades.derkutils.bukkit.Colors;
import xyz.derkades.derkutils.bukkit.PlaceholderUtil.Placeholder;

public class ChatEvent implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		final Player player = event.getPlayer();

		if (player.hasPermission("icore.chat.use_colors")) {
			event.setMessage(Colors.parseColors(event.getMessage()));
		}
		for (Placeholder placeholder : EmotePlaceholders.parseEmote(player)) {
			event.setMessage(placeholder.parse(event.getMessage()));
		}

		Placeholder playerNickName = new Placeholder("{player_nickname}", NickNameUtil.getNickName(player));
		Placeholder playerName = new Placeholder("{player_name}", player.getName());
		Placeholder playerDisplayName = new Placeholder("{player_displayname}", player.getDisplayName());
		Placeholder message = new Placeholder("{message}", event.getMessage());
		Placeholder chatColor = new Placeholder("{chat_color}", "&" + ColorUtil.getChatColor(player));
		Placeholder nameColor = new Placeholder("{name_color}", "&" + ColorUtil.getNameColor(player));
		Placeholder vaultPrefix = new Placeholder("{prefix}", ICore.chat.getPlayerPrefix(player));
		Placeholder vaultSuffix = new Placeholder("{suffix}", ICore.chat.getPlayerSuffix(player));

		for (Player recipient : event.getRecipients()) {

			recipient.spigot()
					.sendMessage(Chat.toComponentWithPapiPlaceholders(ICore.instance.getConfig(), "chat.format", player,
							playerNickName, playerName, playerDisplayName, message, chatColor, nameColor, vaultPrefix,
							vaultSuffix));

			event.getRecipients().clear();
		}
	}
}
