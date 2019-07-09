package com.mineglade.moda.utils.placeholders;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class ModaPlaceholderAPI {

	private static final char PLACEHOLDER_START = '{';
	private static final char PLACEHOLDER_END = '}';

	private static final Map<String, Function<Player, Object>> PLACEHOLDERS = new HashMap<>();

	public static String parsePlaceholders(String string, final Player player) {
		for (final Map.Entry<String, Function<Player, Object>> placeholderEntry : PLACEHOLDERS.entrySet()) {
			if (string.contains(placeholderEntry.getKey())) {
				string = string.replace(placeholderEntry.getKey(), placeholderEntry.getValue().apply(player).toString());
			}
		}
		return string;
	}

	public static void addPlaceholder(final String placeholder, final Function<Player, Object> supplier) {
		PLACEHOLDERS.put(PLACEHOLDER_START + placeholder + PLACEHOLDER_END, supplier);
	}

	public static BaseComponent[] parsePlaceholders(final Player player, final BaseComponent... components) {
		final BaseComponent[] newComponents = new BaseComponent[components.length];
		for (int i = 0; i < components.length; i++) {
			final BaseComponent originalComponent = components[i];
			String text = originalComponent.toPlainText();
			System.out.println("text: " + text);
			for (final Map.Entry<String, Function<Player, Object>> placeholderEntry : PLACEHOLDERS.entrySet()) {
				System.out.println("contains " + placeholderEntry.getKey() + "?    " + text.contains(placeholderEntry.getKey()));
				if (text.contains(placeholderEntry.getKey())) {
					text = text.replace(placeholderEntry.getKey(), placeholderEntry.getValue().apply(player).toString());
				}
			}
			final BaseComponent component = new TextComponent(text);
			component.copyFormatting(originalComponent);
			newComponents[i] = component;
		}
		return newComponents;
	}

}
