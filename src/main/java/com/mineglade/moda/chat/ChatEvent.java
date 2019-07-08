package com.mineglade.moda.chat;

import org.bukkit.event.Listener;

@Deprecated
public class ChatEvent implements Listener {
	/*
	 * @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH) public
	 * void onChat(AsyncPlayerChatEvent event) { final Player player =
	 * event.getPlayer(); PlayerData data = new PlayerData(player);
	 * 
	 * if (player.hasPermission("icore.chat.use_colors")) {
	 * event.setMessage(Colors.parseColors(event.getMessage())); } for (Placeholder
	 * placeholder : EmotePlaceholders.parseEmote(player)) {
	 * event.setMessage(placeholder.parse(event.getMessage())); }
	 * 
	 * Placeholder playerStatistics = new Placeholder("{player-statistics}",
	 * Moda.instance.getConfig().getString("player-statistics")); Placeholder
	 * playerNickName; try { playerNickName = new Placeholder("{player_nickname}",
	 * data.getNickName()); } catch (PlayerNotLoggedException e) {
	 * e.printStackTrace(); playerNickName = new Placeholder("{player_nickname}",
	 * player.getDisplayName()); } Placeholder playerName = new
	 * Placeholder("{player_name}", player.getName()); Placeholder playerDisplayName
	 * = new Placeholder("{player_displayname}", player.getDisplayName());
	 * Placeholder message = new Placeholder("{message}", event.getMessage());
	 * Placeholder chatColor = new Placeholder("{chat_color}", data.getChatColor() +
	 * ""); Placeholder nameColor = new Placeholder("{name_color}",
	 * data.getNameColor() + ""); Placeholder vaultPrefix = new
	 * Placeholder("{prefix}", Moda.chat.getPlayerPrefix(player)); Placeholder
	 * vaultSuffix = new Placeholder("{suffix}", Moda.chat.getPlayerSuffix(player));
	 * 
	 * for (Player recipient : event.getRecipients()) { recipient.spigot()
	 * .sendMessage(Chat.toComponentWithPapiPlaceholders(Moda.instance.getConfig(),
	 * "chat.format", player, playerStatistics, playerNickName, playerName,
	 * playerDisplayName, message, chatColor, nameColor, vaultPrefix, vaultSuffix));
	 * } event.getRecipients().clear(); }
	 */
}
