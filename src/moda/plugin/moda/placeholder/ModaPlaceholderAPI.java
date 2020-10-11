package moda.plugin.moda.placeholder;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Supplier;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class ModaPlaceholderAPI {

	private static final char PLACEHOLDER_START = '{';
	private static final char PLACEHOLDER_END = '}';

	private static final Map<String, Function<Player, String>> PLAYER_PLACEHOLDERS = new HashMap<>();
	private static final Map<String, Supplier<String>> GLOBAL_PLACEHOLDERS = new HashMap<>();
	
	public static String parsePlaceholders(String string, final Player player) {
		for (final Map.Entry<String, Function<Player, String>> placeholderEntry : PLAYER_PLACEHOLDERS.entrySet()) {
			if (string.contains(placeholderEntry.getKey())) {
				string = string.replace(placeholderEntry.getKey(), placeholderEntry.getValue().apply(player));
			}
		}
		
		return parsePlaceholders(string);
	}
	
	public static String parsePlaceholders(String string) {
		for (final Map.Entry<String, Supplier<String>> placeholderEntry : GLOBAL_PLACEHOLDERS.entrySet()) {
			if (string.contains(placeholderEntry.getKey())) {
				string = string.replace(placeholderEntry.getKey(), placeholderEntry.getValue().get());
			}
		}
		return string;
	}

	public static void addPlaceholder(final String placeholder, final Function<Player, String> supplier) {
		PLAYER_PLACEHOLDERS.put(PLACEHOLDER_START + placeholder + PLACEHOLDER_END, supplier);
	}
	
	public static void addPlaceholder(final String placeholder, final Supplier<String> supplier) {
		GLOBAL_PLACEHOLDERS.put(PLACEHOLDER_START + placeholder + PLACEHOLDER_END, supplier);
	}

	public static BaseComponent[] parsePlaceholders(final Player player, final BaseComponent... components) {
		final BaseComponent[] newComponents = new BaseComponent[components.length];
		for (int i = 0; i < components.length; i++) {
			final BaseComponent originalComponent = components[i];
			String text = originalComponent.toPlainText();
			for (final Entry<String, Function<Player, String>> placeholderEntry : PLAYER_PLACEHOLDERS.entrySet()) {
				if (text.contains(placeholderEntry.getKey())) {
					text = text.replace(placeholderEntry.getKey(), placeholderEntry.getValue().apply(player));
				}
			}
			final BaseComponent component = new TextComponent(text);
//			component.setColor(originalComponent.getColor());
//			component.setBold(originalComponent.isBold());
//			component.setItalic(originalComponent.isItalic());
//			component.setStrikethrough(originalComponent.isStrikethrough());
//			component.setObfuscated(originalComponent.isObfuscated());
//			component.setHoverEvent(originalComponent.getHoverEvent());
//			component.setClickEvent(originalComponent.getClickEvent());
			component.copyFormatting(originalComponent);
			newComponents[i] = component;
		}
		return parsePlaceholders(newComponents);
	}
	
	public static BaseComponent[] parsePlaceholders(final BaseComponent... components) {
		final BaseComponent[] newComponents = new BaseComponent[components.length];
		for (int i = 0; i < components.length; i++) {
			final BaseComponent originalComponent = components[i];
			String text = originalComponent.toPlainText();
			for (final Entry<String, Supplier<String>> placeholderEntry : GLOBAL_PLACEHOLDERS.entrySet()) {
				if (text.contains(placeholderEntry.getKey())) {
					text = text.replace(placeholderEntry.getKey(), placeholderEntry.getValue().get());
				}
			}
			final BaseComponent component = new TextComponent(text);
			component.copyFormatting(originalComponent);
			newComponents[i] = component;
		}
		return newComponents;
	}

}
