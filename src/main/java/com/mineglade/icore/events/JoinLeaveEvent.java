package com.mineglade.icore.events;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.mineglade.icore.ICore;
import com.mineglade.icore.utils.ColorUtil;
import com.mineglade.icore.utils.NickNameUtil;

import xyz.derkades.derkutils.bukkit.Chat;
import xyz.derkades.derkutils.bukkit.PlaceholderUtil.Placeholder;

public class JoinLeaveEvent implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		event.setJoinMessage("");

		Placeholder user = new Placeholder("%player%", event.getPlayer().getDisplayName());
		Placeholder online = new Placeholder("%online%", Bukkit.getOnlinePlayers().size() + "");
		final Player player = event.getPlayer();

		if (ICore.isVanished(player)) {
			return;
		}

		FileConfiguration config = ICore.instance.getConfig();

		Placeholder playerNickName = new Placeholder("{player_nickname}", NickNameUtil.getNickName(player));
		Placeholder playerName = new Placeholder("{player_name}", player.getName());
		Placeholder playerDisplayName = new Placeholder("{player_displayname}", player.getDisplayName());
		Placeholder chatColor = new Placeholder("{name_color}", "&" + ColorUtil.getNameColor(player));
		Placeholder vaultPrefix = new Placeholder("{prefix}", ICore.chat.getPlayerPrefix(player));
		Placeholder vaultSuffix = new Placeholder("{suffix}", ICore.chat.getPlayerSuffix(player));

		Bukkit.spigot().broadcast(Chat.toComponentWithPapiPlaceholders(ICore.instance.getConfig(), "chat.join-message",
				player, playerNickName, playerName, playerDisplayName, chatColor, vaultPrefix, vaultSuffix));

		player.spigot().sendMessage(Chat.toComponentWithPapiPlaceholders(config, "motd", player, user, online));

	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		event.setQuitMessage("");

		final Player player = event.getPlayer();

		Placeholder playerNickName = new Placeholder("{player_nickname}", NickNameUtil.getNickName(player));
		Placeholder playerName = new Placeholder("{player_name}", player.getName());
		Placeholder playerDisplayName = new Placeholder("{player_displayname}", player.getDisplayName());
		Placeholder chatColor = new Placeholder("{name_color}", "&" + ColorUtil.getNameColor(player));
		Placeholder vaultPrefix = new Placeholder("{prefix}", ICore.chat.getPlayerPrefix(player));
		Placeholder vaultSuffix = new Placeholder("{suffix}", ICore.chat.getPlayerSuffix(player));

		Bukkit.spigot().broadcast(Chat.toComponentWithPapiPlaceholders(ICore.instance.getConfig(), "chat.leave-message",
				player, playerNickName, playerName, playerDisplayName, chatColor, vaultPrefix, vaultSuffix));

//		Bukkit.spigot()
//				.broadcast(new ComponentBuilder("").append(ICore.getPrefix(PrefixType.LEAVE))
//						.append(TextComponent.fromLegacyText(
//								ChatColor.translateAlternateColorCodes('&', ICore.chat.getPlayerPrefix(player) + " ")))
//						.append(player.getDisplayName()).color(ChatColor.RED)
//						.append(TextComponent.fromLegacyText(
//								ChatColor.translateAlternateColorCodes('&', " " + ICore.chat.getPlayerSuffix(player))))
//						.create());

	}
}