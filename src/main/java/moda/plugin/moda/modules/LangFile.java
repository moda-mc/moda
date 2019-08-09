package moda.plugin.moda.modules;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import moda.plugin.moda.Moda;
import moda.plugin.moda.utils.storage.ModuleStorageHandler;
import xyz.derkades.derkutils.bukkit.Colors;

public class LangFile {

	private final FileConfiguration file;

	public LangFile(final File fileFile, final Module<? extends ModuleStorageHandler> module) throws IOException {
		this.file = YamlConfiguration.loadConfiguration(fileFile);

		boolean changes = false;

		for (final IMessage message : module.getMessages()) {
			this.file.addDefault(message.getPath(), message.getDefault());
			if (!this.file.contains(message.getPath())) {
				module.logger.debug("Adding language option to config %s: %s", message.getPath(), message.getDefault());
				this.file.set(message.getPath(), message.getDefault());
				changes = true;
			}
		}

		if (changes) {
			this.file.save(fileFile);
		}
	}

	public String getMessage(final IMessage message) {
		return Colors.parseColors(this.getPrefix() + this.file.getString(message.getPath(), message.getDefault()));
	}

	/**
	 * Uses {@link #getMessage()} then replaces placeholders.
	 * <br><br>
	 * "Visit {link} {number} times"
	 * @param placeholders ["link", "https://example.com", "number", 3]
	 * @return "Visit https://example.com 3 times"
	 */
	public String getMessage(final IMessage message, final Object... placeholders) {
		if (placeholders.length % 2 != 0) { // False if length is 1, 3, 5, 6, etc.
			throw new IllegalArgumentException("Placeholder array length must be an even number");
		}

		if (placeholders.length == 0) {
			return this.getMessage(message);
		}

		final Map<String, String> placeholderMap = new HashMap<>();

		Object key = null;

		for (final Object object : placeholders) {
			if (key == null) {
				// 'placeholder' is a key
				key = object;
			} else {
				// Key has been set previously, 'placeholder' must be a value
				placeholderMap.put(key.toString(), object.toString());
				key = null; // Next 'placeholder' is a key
			}
		}

		String string = this.getMessage(message);

		for(final Map.Entry<String, String> entry : placeholderMap.entrySet()) {
			string = string.replace("{" + entry.getKey() + "}", entry.getValue());
		}

		return this.getPrefix() + Colors.parseColors(string);
	}

	public void send(final CommandSender sender, final IMessage message) {
		sender.sendMessage(this.getMessage(message));
	}

	public void send(final CommandSender sender, final IMessage message, final Object... placeholders) {
		sender.sendMessage(this.getMessage(message, placeholders));
	}

	private String getPrefix() {
		return Moda.instance.getConfig().getString("prefix", "");
	}

}
