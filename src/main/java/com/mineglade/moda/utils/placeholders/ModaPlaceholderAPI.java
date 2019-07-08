package com.mineglade.moda.utils.placeholders;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.bukkit.entity.Player;

public class ModaPlaceholderAPI {

	private static final char PLACEHOLDER_START = '{';
	private static final char PLACEHOLDER_END = '}';

	private static final Map<String, Function<Player, Object>> PLACEHOLDERS = new HashMap<>();

	public static String parsePlaceholders(String string, final Player player) {
		for (final Map.Entry<String, Function<Player, Object>> placeholderEntry : PLACEHOLDERS.entrySet()) {
			if (string.contains(formatPlaceholder(placeholderEntry.getKey()))) {
				string = string.replace(formatPlaceholder(placeholderEntry.getKey()), placeholderEntry.getValue().apply(player).toString());
			}
		}
		return string;
	}

	private static String formatPlaceholder(final String placeholder) {
		return PLACEHOLDER_START + placeholder + PLACEHOLDER_END;
	}

	public static void addPlaceholder(final String placeholder, final Function<Player, Object> supplier) {
		PLACEHOLDERS.put(placeholder, supplier);
	}

}
