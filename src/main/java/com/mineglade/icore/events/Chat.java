package com.mineglade.icore.events;

import com.mineglade.icore.ICore;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Chat implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage("");

        final Player player = event.getPlayer();

        Bukkit.spigot()
                .broadcast(new ComponentBuilder("")
                        .append(ICore.getJoinLeavePrefix(ChatColor.GREEN, '+'))
                        .event((HoverEvent) null)
                        .append(player.getDisplayName()).color(ChatColor.GREEN)
                        .create());

        // if (Bukkit.getServer().getServerName().equals("hub")) {
        // event.getPlayer().spigot().sendMessage(new ComponentBuilder("").create());
        // }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage("");

        final Player player = event.getPlayer();

        Bukkit.spigot()
                .broadcast(new ComponentBuilder("")
                        .append(ICore.getJoinLeavePrefix(ChatColor.RED, '-'))
                        .append(player.getDisplayName()).color(ChatColor.RED).create());

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        final Player player = event.getPlayer();

        for (Player recipient : event.getRecipients()) {
            recipient.spigot()
                    .sendMessage(new ComponentBuilder("")
                            .append(TextComponent.fromLegacyText(ICore.getChatPrefix()))
                            .append(player.getDisplayName())
                            .append(" » " + event.getMessage()).event((HoverEvent) null)
                            .create());
        }

        event.getRecipients().clear();
    }

}
