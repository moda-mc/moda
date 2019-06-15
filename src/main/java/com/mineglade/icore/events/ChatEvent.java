package com.mineglade.icore.events;

import com.mineglade.icore.ICore;
import com.mineglade.icore.PrefixType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        final Player player = event.getPlayer();

        for (Player recipient : event.getRecipients()) {
            recipient.spigot()
                    .sendMessage(new ComponentBuilder("")
                            .append(TextComponent.fromLegacyText(ICore.getPrefix(PrefixType.CHAT)))
                            .append(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',ICore.chat.getPlayerPrefix(player) + " " )))
                            .append(player.getDisplayName())
                            .append(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&'," " + ICore.chat.getPlayerSuffix(player))))
                            .append(" » " + event.getMessage()).event((HoverEvent) null)
                            .create());
        }

        event.getRecipients().clear();
    }

}
