package com.mineglade.moda.utils;

import org.bukkit.configuration.ConfigurationSection;

import com.mineglade.moda.utils.placeholders.ModaPlaceholderAPI;

import net.md_5.bungee.api.chat.BaseComponent;
import xyz.derkades.derkutils.bukkit.Colors;

public class Chat {

	public static BaseComponent[] getComponentsWithModaPlaceholders(final ConfigurationSection section) {
		return Colors.toComponent(ModaPlaceholderAPI.parsePlaceholders(string));
	}

	public static BaseComponent[] getComponentWithModaAndPapiPlaceholders(final String string) {
		return xyz.derkades.derkutils.bukkit.Chat.toComponentWithPapiPlaceholders(config, path, player, placeholders)
	}

}
