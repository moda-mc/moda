package cx.moda.moda.placeholder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.Validate;
import org.bukkit.entity.Player;

import cx.moda.moda.module.Module;
import cx.moda.moda.module.ModuleManager;
import cx.moda.moda.module.storage.ModuleStorageHandler;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class ModaPlaceholderAPI {

	private static final char PLACEHOLDER_START = '{';
	private static final char PLACEHOLDER_END = '}';

	private static final Map<String, IPlaceholder> PLACEHOLDERS = new HashMap<>();

	public static String parsePlaceholders(final Optional<Player> player, String string) {

		final List<String> placeholdersFound = new ArrayList<>();
		boolean inPlaceholder = false;
		final StringBuilder placeholderName = new StringBuilder();
		for (final char c : string.toCharArray()) {
			if (inPlaceholder) {
				if (c == PLACEHOLDER_END) {
					inPlaceholder = false;
					if (placeholderName.length() > 0) {
						placeholdersFound.add(placeholderName.toString());
						placeholderName.setLength(0);
					}
				} else {
					placeholderName.append(c);
				}
			} else {
				if (c == PLACEHOLDER_START) {
					inPlaceholder = true;
				}
			}
		}

		for (final String name : placeholdersFound) {
			final String value = parseSinglePlaceholder(player, name);

			string = string.replace(PLACEHOLDER_START + name + PLACEHOLDER_END, value);
		}

		return string;
	}

	public static String parseSinglePlaceholder(final Optional<Player> player, final String placeholderName) {
		final IPlaceholder placeholder = PLACEHOLDERS.get(placeholderName);
		if (placeholder == null) {
			return "[Unknown placeholder '" + placeholderName + "']";
		} else if (placeholder instanceof IPlayerPlaceholder) {
			if (player.isPresent()) {
				return ((IPlayerPlaceholder) placeholder).getValue(player.get());
			} else {
				return "[Player specific placeholder in global context '" + placeholderName + "']";
			}
		} else if (placeholder instanceof IGlobalPlaceholder) {
			return ((IGlobalPlaceholder) placeholder).getValue();
		} else {
			return "[Placeholder of " + placeholderName + " with unknown superclass]";
		}
	}

	@Deprecated
	public static BaseComponent[] parsePlaceholders(final Optional<Player> player, final BaseComponent... components) {
//		final BaseComponent[] newComponents = new BaseComponent[components.length];
		for (int i = 0; i < components.length; i++) {
			final BaseComponent originalComponent = components[i];
			String text = originalComponent.toPlainText();
			text = parsePlaceholders(player, text);
			final BaseComponent component = new TextComponent(text);
//			component.setColor(originalComponent.getColor());
//			component.setBold(originalComponent.isBold());
//			component.setItalic(originalComponent.isItalic());
//			component.setStrikethrough(originalComponent.isStrikethrough());
//			component.setObfuscated(originalComponent.isObfuscated());
//			component.setHoverEvent(originalComponent.getHoverEvent());
//			component.setClickEvent(originalComponent.getClickEvent());
			component.copyFormatting(originalComponent);
			components[i] = component;
		}
		return components;
	}

	public static void registerPlaceholder(final IPlaceholder placeholder) {
		Validate.notNull(placeholder, "Placeholder is null");

		if (placeholder instanceof IModulePlaceholder) {
			final IModulePlaceholder modulePlaceholder = (IModulePlaceholder) placeholder;
			if (!ModuleManager.getInstance().isLoaded(modulePlaceholder.getModule())) {
				throw new IllegalStateException("Attempted to register placeholder from unloaded module");
			}
		}

		final String name = placeholder.getName();
		PLACEHOLDERS.put(name, placeholder);
	}

	public static void unregisterPlaceholders(final Module<ModuleStorageHandler> module) {
		PLACEHOLDERS.entrySet().removeIf(e -> {
			if (e.getValue() instanceof IModulePlaceholder) {
				final IModulePlaceholder placeholder = (IModulePlaceholder) e.getValue();
				return module == placeholder.getModule();
			} else {
				return false;
			}
		});
	}

}
