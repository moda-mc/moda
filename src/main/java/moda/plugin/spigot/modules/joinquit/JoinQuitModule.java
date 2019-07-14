package moda.plugin.spigot.modules.joinquit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import moda.plugin.spigot.modules.IMessage;
import moda.plugin.spigot.modules.Module;
import moda.plugin.spigot.utils.placeholders.ModaPlaceholderAPI;
import moda.plugin.spigot.utils.storage.DatabaseStorageHandler;
import moda.plugin.spigot.utils.storage.FileStorageHandler;
import moda.plugin.spigot.utils.storage.NoStorageHandler;
import moda.plugin.spigot.utils.vault.VaultHandler;
import moda.plugin.spigot.utils.vault.VaultNotAvailableException;
import xyz.derkades.derkutils.bukkit.Chat;
import xyz.derkades.derkutils.bukkit.PlaceholderUtil.Placeholder;

public class JoinQuitModule extends Module<NoStorageHandler> {

	private VaultHandler vault;

	@Override
	public String getName() {
		return "JoinQuit";
	}

	@Override
	public IMessage[] getMessages() {
		return null;
	}

	@Override
	public FileStorageHandler getFileStorageHandler() {
		return null;
	}

	@Override
	public DatabaseStorageHandler getDatabaseStorageHandler() {
		return null;
	}

	@Override
	public void onEnable(){
		try {
			this.vault = new VaultHandler(this);
			this.vault.getChat();
		} catch (final VaultNotAvailableException e) {
			this.logger.info("Vault not installed, vault Placeholders will not work.");
			this.vault = null;
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onJoin(final PlayerJoinEvent event) {

		event.setJoinMessage("");

		final Player player = event.getPlayer();

		final JoinMessageSendEvent messageEvent = new JoinMessageSendEvent(player);

		Bukkit.getPluginManager().callEvent(messageEvent);

		if (messageEvent.isCancelled()) {
			return;
		}

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

		final QuitMessageSendEvent messageEvent = new QuitMessageSendEvent(player);

		Bukkit.getPluginManager().callEvent(messageEvent);

		if (messageEvent.isCancelled()) {
			return;
		}

		final Placeholder vaultPrefix = new Placeholder("{PREFIX}", this.vault == null ? "&c[NoVault]" : this.vault.getChat().getPlayerPrefix(player));
		final Placeholder vaultSuffix = new Placeholder("{SUFFIX}", this.vault == null ? "&c[NoVault] " : this.vault.getChat().getPlayerSuffix(player));
		final Placeholder vaultGroup = new Placeholder("{GROUP}", this.vault == null ? "&c[NoVault]" : this.vault.getChat().getPrimaryGroup(player));

		Bukkit.spigot().broadcast(
				ModaPlaceholderAPI.parsePlaceholders(player,
						Chat.toComponentWithPapiPlaceholders(this.config, "leave-message", player,
								vaultPrefix, vaultSuffix, vaultGroup)));

	}
}