package com.mineglade.icore.emotes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import xyz.derkades.derkutils.bukkit.PlaceholderUtil.Placeholder;

public class EmotePlaceholders {
	public static List<Placeholder> parseEmote(Player player) {
		List<Placeholder> placeholders = new ArrayList<Placeholder>();
		for (Emote emote : Emote.values()) {
			if (player.hasPermission("icore.emotes." + emote.toString().toLowerCase())) {
				Placeholder placeholder = new Placeholder("_" + emote.toString().toLowerCase(), emote.emote);
				placeholders.add(placeholder);
			}
		}
		return placeholders;
	}
}
