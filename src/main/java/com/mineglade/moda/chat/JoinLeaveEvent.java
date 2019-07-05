package com.mineglade.moda.chat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.mineglade.moda.Moda;
import com.mineglade.moda.utils.PlayerData;
import com.mineglade.moda.utils.PlayerNotLoggedException;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.Chat;
import xyz.derkades.derkutils.bukkit.PlaceholderUtil.Placeholder;

public class JoinLeaveEvent implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
		final Player player = event.getPlayer();
		PlayerData data = new PlayerData(player);

		event.setJoinMessage("");
		data.setLastUsername(player.getName());
		try {
			player.setDisplayName(ChatColor.stripColor(data.getNickName()));
		} catch (PlayerNotLoggedException e) {
			e.printStackTrace();
		}

		// Player placeholders
		Placeholder playerNickName;
		try {
			playerNickName = new Placeholder("{player_nickname}", data.getNickName());
		} catch (PlayerNotLoggedException e) {
			e.printStackTrace();
			playerNickName = new Placeholder("{player_nickname}", player.getName());
		}
		Placeholder playerName = new Placeholder("{player_name}", player.getName());
		Placeholder playerDisplayName = new Placeholder("{player_displayname}", player.getDisplayName());
		Placeholder playerNameColor = new Placeholder("{name_color}", data.getNameColor() + "");
		Placeholder vaultPrefix = new Placeholder("{prefix}", Moda.chat.getPlayerPrefix(player));
		Placeholder vaultSuffix = new Placeholder("{suffix}", Moda.chat.getPlayerSuffix(player));

		// Global placeholders
		Placeholder onlinePlayerCount = new Placeholder("{online}", Bukkit.getOnlinePlayers().size() + "");

		if (Moda.isVanished(player)) {
			return;
		}

		// Join message:
		Bukkit.spigot()
				.broadcast(Chat.toComponentWithPapiPlaceholders(Moda.instance.getConfig(), "chat.join-message", player,
						playerNickName, playerName, playerDisplayName, playerNameColor, vaultPrefix, vaultSuffix,
						onlinePlayerCount));

		// MOTD (visible to the player that just joined only)
		player.spigot()
				.sendMessage(Chat.toComponentWithPapiPlaceholders(Moda.instance.getConfig(), "chat.motd", player,
						playerNickName, playerName, playerDisplayName, playerNameColor, vaultPrefix, vaultSuffix,
						onlinePlayerCount));

	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		PlayerData data = new PlayerData(player);

		event.setQuitMessage("");
		data.setLastUsername(player.getName());

		// Player placeholders
		Placeholder playerNickName;
		try {
			playerNickName = new Placeholder("{player_nickname}", data.getNickName());
		} catch (PlayerNotLoggedException e) {
			e.printStackTrace();
			playerNickName = new Placeholder("{player_nickname}", player.getDisplayName());
		}
		Placeholder playerName = new Placeholder("{player_name}", player.getName());
		Placeholder playerDisplayName = new Placeholder("{player_displayname}", player.getDisplayName());
		Placeholder playerNameColor = new Placeholder("{name_color}", data.getNameColor() + "");
		Placeholder vaultPrefix = new Placeholder("{prefix}", Moda.chat.getPlayerPrefix(player));
		Placeholder vaultSuffix = new Placeholder("{suffix}", Moda.chat.getPlayerSuffix(player));

		// Global placeholders
		Placeholder onlinePlayerCount = new Placeholder("{online}", Bukkit.getOnlinePlayers().size() + "");

		// Quit message
		Bukkit.spigot()
				.broadcast(Chat.toComponentWithPapiPlaceholders(Moda.instance.getConfig(), "chat.leave-message",
						player, playerNickName, playerName, playerDisplayName, playerNameColor, vaultPrefix,
						vaultSuffix, onlinePlayerCount));

	}
}