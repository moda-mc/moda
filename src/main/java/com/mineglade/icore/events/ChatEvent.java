package com.mineglade.icore.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.mineglade.icore.ICore;
import com.mineglade.icore.PrefixType;
import com.mineglade.icore.utils.NameColorUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import xyz.derkades.derkutils.bukkit.Colors;

public class ChatEvent implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		final Player player = event.getPlayer();
		final String playerChatColor = NameColorUtil.getChatColor(player);

		if (player.hasPermission("icore.chat.use_colors")) {
			event.setMessage(Colors.parseColors(event.getMessage()));
		}

		for (Player recipient : event.getRecipients()) {
			recipient.spigot().sendMessage(new ComponentBuilder("")
					.append(TextComponent.fromLegacyText(ICore.getPrefix(PrefixType.CHAT)))
					.append(TextComponent.fromLegacyText(
							ChatColor.translateAlternateColorCodes('&', ICore.chat.getPlayerPrefix(player) + " ")))
					.append(player.getDisplayName())
					.append(TextComponent.fromLegacyText(
							ChatColor.translateAlternateColorCodes('&', " " + ICore.chat.getPlayerSuffix(player))))
					.append(TextComponent.fromLegacyText(
							"» " + Colors.parseColors("&" + playerChatColor) + event.getMessage()), FormatRetention.ALL)
					.event((HoverEvent) null).create());
		}
		
		
		event.getRecipients().clear();
	}

}
