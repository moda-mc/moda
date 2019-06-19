package com.mineglade.icore.events;

import com.mineglade.icore.ICore;
import com.mineglade.icore.PrefixType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.derkades.derkutils.bukkit.Chat;
import xyz.derkades.derkutils.bukkit.PlaceholderUtil.Placeholder;

public class JoinLeaveEvent implements Listener {

	
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage("");

        Placeholder user = new Placeholder("%player%", event.getPlayer().getDisplayName());
        final Player player = event.getPlayer();

        if (ICore.isVanished(player)){
            return;
        }

        FileConfiguration config = ICore.instance.getConfig();

        Bukkit.spigot().broadcast(new ComponentBuilder("")
                .append(ICore.getPrefix(PrefixType.JOIN))
                .event((HoverEvent) null)
                .append(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',ICore.chat.getPlayerPrefix(player) + " " )))
                .append(player.getDisplayName()).color(ChatColor.GREEN)
                .append(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&'," " + ICore.chat.getPlayerSuffix(player))))
                .create());

        player.spigot().sendMessage(Chat.toComponentWithPapiPlaceholders(config, "motd", player, user));

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage("");

        final Player player = event.getPlayer();

        Bukkit.spigot()
                .broadcast(new ComponentBuilder("")
                        .append(ICore.getPrefix(PrefixType.LEAVE))
                        .append(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',ICore.chat.getPlayerPrefix(player) + " " )))
                        .append(player.getDisplayName()).color(ChatColor.RED)
                        .append(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&'," " + ICore.chat.getPlayerSuffix(player))))
                        .create());

    }
}