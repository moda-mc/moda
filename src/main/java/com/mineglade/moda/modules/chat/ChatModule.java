package com.mineglade.moda.modules.chat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.mineglade.moda.modules.IMessage;
import com.mineglade.moda.modules.Module;
import com.mineglade.moda.modules.chat.storage.ChatDatabaseStorageHandler;
import com.mineglade.moda.modules.chat.storage.ChatFileStorageHandler;
import com.mineglade.moda.modules.chat.storage.ChatStorageHandler;
import com.mineglade.moda.utils.placeholders.ModaPlaceholderAPI;
import com.mineglade.moda.utils.storage.DatabaseStorageHandler;
import com.mineglade.moda.utils.storage.FileStorageHandler;
import com.mineglade.moda.utils.vault.VaultHandler;
import com.mineglade.moda.utils.vault.VaultNotAvailableException;

import xyz.derkades.derkutils.bukkit.Chat;
import xyz.derkades.derkutils.bukkit.Colors;
import xyz.derkades.derkutils.bukkit.PlaceholderUtil.Placeholder;

public class ChatModule extends Module<ChatStorageHandler> {

	private VaultHandler vault;

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
		return new ChatFileStorageHandler(this);
	}

	@Override
	public DatabaseStorageHandler getDatabaseStorageHandler() {
		return new ChatDatabaseStorageHandler(this);
	}

	@Override
	public void onEnable() {
		try {
			this.vault = new VaultHandler(this);
			this.vault.getChat();
		} catch (final VaultNotAvailableException e) {
			this.logger.info("Vault not installed, vault Placeholders will not work.");
			this.vault = null;
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onChat(final AsyncPlayerChatEvent event) {
		final Player player = event.getPlayer();

		if (player.hasPermission("moda.chat.use_colors")) {
			event.setMessage(Colors.parseColors(event.getMessage()));
		}

		final Placeholder message = new Placeholder("{MESSAGE}", event.getMessage());

		final Placeholder vaultPrefix = new Placeholder("{PREFIX}", this.vault == null ? "&c[NoVault]" : this.vault.getChat().getPlayerPrefix(player));
		final Placeholder vaultSuffix = new Placeholder("{SUFFIX}", this.vault == null ? "&c[NoVault] " : this.vault.getChat().getPlayerSuffix(player));
		final Placeholder vaultGroup = new Placeholder("{GROUP}", this.vault == null ? "&c[NoVault]" : this.vault.getChat().getPrimaryGroup(player));

		for (final Player recipient : event.getRecipients()) {
			recipient.spigot().sendMessage(
					ModaPlaceholderAPI.parsePlaceholders(player,
							Chat.toComponentWithPapiPlaceholders(this.config, "format", player,
									message, vaultPrefix, vaultSuffix, vaultGroup)));
		}
		event.getRecipients().clear();
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onJoin(final PlayerJoinEvent event) {

		event.setJoinMessage("");

		final Player player = event.getPlayer();

		final Placeholder vaultPrefix = new Placeholder("{PREFIX}", this.vault == null ? "&c[NoVault]" : this.vault.getChat().getPlayerPrefix(player));
		final Placeholder vaultSuffix = new Placeholder("{SUFFIX}", this.vault == null ? "&c[NoVault] " : this.vault.getChat().getPlayerSuffix(player));
		final Placeholder vaultGroup = new Placeholder("{GROUP}", this.vault == null ? "&c[NoVault]" : this.vault.getChat().getPrimaryGroup(player));

		Bukkit.spigot().broadcast(
				ModaPlaceholderAPI.parsePlaceholders(player,
						Chat.toComponentWithPapiPlaceholders(this.config, "join-message", player,
								vaultPrefix, vaultSuffix, vaultGroup)));
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onQuit(final PlayerQuitEvent event) {

		event.setQuitMessage("");

		final Player player = event.getPlayer();

		final Placeholder vaultPrefix = new Placeholder("{PREFIX}", this.vault == null ? "&c[NoVault]" : this.vault.getChat().getPlayerPrefix(player));
		final Placeholder vaultSuffix = new Placeholder("{SUFFIX}", this.vault == null ? "&c[NoVault] " : this.vault.getChat().getPlayerSuffix(player));
		final Placeholder vaultGroup = new Placeholder("{GROUP}", this.vault == null ? "&c[NoVault]" : this.vault.getChat().getPrimaryGroup(player));

		Bukkit.spigot().broadcast(
				ModaPlaceholderAPI.parsePlaceholders(player,
						Chat.toComponentWithPapiPlaceholders(this.config, "leave-message", player,
								vaultPrefix, vaultSuffix, vaultGroup)));

	}
}
